package com.github.kimjooyoon.userservice.core.application.service

import com.github.kimjooyoon.userservice.core.application.dto.TokenDto
import com.github.kimjooyoon.userservice.core.application.dto.UserLoginCommand
import com.github.kimjooyoon.userservice.core.domain.model.auth.Authentication
import com.github.kimjooyoon.userservice.core.domain.model.auth.AuthenticationRepository
import com.github.kimjooyoon.userservice.core.domain.model.user.User
import com.github.kimjooyoon.userservice.core.domain.model.user.UserRepository
import com.github.kimjooyoon.userservice.core.domain.model.user.vo.UserEmail
import com.github.kimjooyoon.userservice.message.EventPublisher
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

/**
 * 인증 서비스 인터페이스
 */
interface AuthenticationService {
    /**
     * 사용자 로그인 처리
     *
     * @param command 로그인 명령
     * @return 토큰 정보
     */
    fun login(command: UserLoginCommand): TokenDto

    /**
     * 로그아웃 처리
     *
     * @param accessToken 액세스 토큰
     */
    fun logout(accessToken: String)

    /**
     * 액세스 토큰 갱신
     *
     * @param refreshToken 리프레시 토큰
     * @return 새로운 토큰 정보
     */
    fun refreshToken(refreshToken: String): TokenDto

    /**
     * 토큰 검증
     *
     * @param token 검증할 토큰
     * @return 토큰이 유효한지 여부
     */
    fun validateToken(token: String): Boolean

    /**
     * 만료된 인증 정보 정리
     *
     * @return 정리된 레코드 수
     */
    fun cleanupExpiredAuthentications(): Int
}

/**
 * 인증 서비스 구현체
 */
@Service
class AuthenticationServiceImpl(
    private val userRepository: UserRepository,
    private val authenticationRepository: AuthenticationRepository,
    private val passwordEncoder: PasswordEncoder,
    private val eventPublisher: EventPublisher
) : AuthenticationService {

    companion object {
        // 액세스 토큰 만료 시간 (30분)
        private const val ACCESS_TOKEN_EXPIRATION_TIME = 30L * 60L
        
        // 리프레시 토큰 만료 시간 (2주)
        private const val REFRESH_TOKEN_EXPIRATION_TIME = 14L * 24L * 60L * 60L
    }

    /**
     * 사용자 로그인 처리
     */
    @Transactional
    override fun login(command: UserLoginCommand): TokenDto {
        // 이메일로 사용자 찾기
        val user = userRepository.findByEmail(UserEmail.of(command.email))
            ?: throw IllegalArgumentException("유효하지 않은 이메일 또는 비밀번호입니다.")

        // 비밀번호 검증
        if (!passwordEncoder.matches(command.password, user.getPassword())) {
            throw IllegalArgumentException("유효하지 않은 이메일 또는 비밀번호입니다.")
        }

        // 사용자 상태 확인
        if (!user.isActive()) {
            throw IllegalStateException("비활성화된 사용자 계정입니다.")
        }

        // 로그인 처리
        user.login()
        userRepository.save(user)

        // 도메인 이벤트 발행
        user.clearEvents().forEach { eventPublisher.publish(it) }

        // 기존 인증 정보가 있으면 삭제
        val existingAuth = authenticationRepository.findByUserId(user.id)
        existingAuth?.let {
            authenticationRepository.delete(it)
        }

        // 새 인증 정보 생성
        return createAuthenticationForUser(user)
    }

    /**
     * 로그아웃 처리
     */
    @Transactional
    override fun logout(accessToken: String) {
        val authentication = authenticationRepository.findByAccessToken(accessToken)
            ?: return // 인증 정보가 없으면 무시

        // 모든 토큰 폐기
        authentication.revokeAllTokens()
        
        // 인증 정보 삭제
        authenticationRepository.delete(authentication)
        
        // 도메인 이벤트 발행
        authentication.clearEvents().forEach { eventPublisher.publish(it) }
    }

    /**
     * 액세스 토큰 갱신
     */
    @Transactional
    override fun refreshToken(refreshToken: String): TokenDto {
        val authentication = authenticationRepository.findByRefreshToken(refreshToken)
            ?: throw IllegalArgumentException("유효하지 않은 리프레시 토큰입니다.")

        // 리프레시 토큰 유효성 검증
        authentication.validateRefreshToken(refreshToken)

        // 새 액세스 토큰 발급
        val newAccessToken = generateToken()
        authentication.refreshAccessToken(newAccessToken, ACCESS_TOKEN_EXPIRATION_TIME)
        
        // 저장
        authenticationRepository.save(authentication)
        
        // 도메인 이벤트 발행
        authentication.clearEvents().forEach { eventPublisher.publish(it) }

        return TokenDto(
            accessToken = newAccessToken,
            refreshToken = refreshToken,
            expiresIn = ACCESS_TOKEN_EXPIRATION_TIME,
            tokenType = "Bearer"
        )
    }

    /**
     * 토큰 검증
     */
    @Transactional(readOnly = true)
    override fun validateToken(token: String): Boolean {
        val authentication = authenticationRepository.findByAccessToken(token)
            ?: return false

        try {
            authentication.validateAccessToken(token)
            return true
        } catch (e: Exception) {
            return false
        }
    }

    /**
     * 만료된 인증 정보 정리
     */
    @Transactional
    override fun cleanupExpiredAuthentications(): Int {
        return authenticationRepository.cleanupExpiredAuthentications()
    }

    /**
     * 사용자에 대한 인증 정보 생성
     */
    private fun createAuthenticationForUser(user: User): TokenDto {
        // 토큰 생성
        val accessToken = generateToken()
        val refreshToken = generateToken()

        // 인증 객체 생성
        val authentication = Authentication.create(user.id)
        authentication.issueAccessToken(accessToken, ACCESS_TOKEN_EXPIRATION_TIME)
        authentication.issueRefreshToken(refreshToken, REFRESH_TOKEN_EXPIRATION_TIME)
        
        // 저장
        authenticationRepository.save(authentication)
        
        // 도메인 이벤트 발행
        authentication.clearEvents().forEach { eventPublisher.publish(it) }

        return TokenDto(
            accessToken = accessToken,
            refreshToken = refreshToken,
            expiresIn = ACCESS_TOKEN_EXPIRATION_TIME,
            tokenType = "Bearer"
        )
    }

    /**
     * 토큰 생성
     */
    private fun generateToken(): String {
        return java.util.UUID.randomUUID().toString()
    }
}