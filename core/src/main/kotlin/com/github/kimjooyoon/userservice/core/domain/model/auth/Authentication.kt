package com.github.kimjooyoon.userservice.core.domain.model.auth

import com.github.kimjooyoon.userservice.core.domain.model.AggregateRoot
import com.github.kimjooyoon.userservice.core.domain.model.auth.event.*
import com.github.kimjooyoon.userservice.core.domain.model.auth.vo.AuthenticationId
import com.github.kimjooyoon.userservice.core.domain.model.auth.vo.TokenValue
import com.github.kimjooyoon.userservice.core.domain.model.user.vo.UserId
import java.time.LocalDateTime

/**
 * 인증(Authentication) Aggregate Root 엔티티
 * 
 * 사용자 인증 및 토큰 관리를 담당하는 Aggregate Root 엔티티입니다.
 */
class Authentication private constructor(
    val id: AuthenticationId,
    val userId: UserId,
    private var accessToken: TokenValue? = null,
    private var refreshToken: TokenValue? = null,
    private var lastAuthenticated: LocalDateTime? = null,
    private var createdAt: LocalDateTime = LocalDateTime.now(),
    private var updatedAt: LocalDateTime = LocalDateTime.now()
) : AggregateRoot<AuthenticationId>(id) {

    /**
     * 액세스 토큰 발급
     *
     * @param token 액세스 토큰 값
     * @param expirationTime 만료 시간(초)
     * @return 업데이트된 Authentication 인스턴스
     */
    fun issueAccessToken(token: String, expirationTime: Long): Authentication {
        val tokenValue = TokenValue.of(token, expirationTime)
        this.accessToken = tokenValue
        this.lastAuthenticated = LocalDateTime.now()
        this.updatedAt = LocalDateTime.now()
        
        registerEvent(TokenIssued(id.value, userId.value, token, "ACCESS_TOKEN", tokenValue.expiresAt))
        return this
    }

    /**
     * 리프레시 토큰 발급
     *
     * @param token 리프레시 토큰 값
     * @param expirationTime 만료 시간(초)
     * @return 업데이트된 Authentication 인스턴스
     */
    fun issueRefreshToken(token: String, expirationTime: Long): Authentication {
        val tokenValue = TokenValue.of(token, expirationTime)
        this.refreshToken = tokenValue
        this.updatedAt = LocalDateTime.now()
        
        registerEvent(TokenIssued(id.value, userId.value, token, "REFRESH_TOKEN", tokenValue.expiresAt))
        return this
    }

    /**
     * 액세스 토큰 갱신
     *
     * @param newToken 새 액세스 토큰 값
     * @param expirationTime 만료 시간(초)
     * @return 업데이트된 Authentication 인스턴스
     */
    fun refreshAccessToken(newToken: String, expirationTime: Long): Authentication {
        val oldToken = this.accessToken
        val tokenValue = TokenValue.of(newToken, expirationTime)
        this.accessToken = tokenValue
        this.lastAuthenticated = LocalDateTime.now()
        this.updatedAt = LocalDateTime.now()
        
        registerEvent(TokenRefreshed(id.value, userId.value, newToken, "ACCESS_TOKEN", tokenValue.expiresAt))
        return this
    }

    /**
     * 모든 토큰 폐기
     *
     * @return 업데이트된 Authentication 인스턴스
     */
    fun revokeAllTokens(): Authentication {
        val oldAccessToken = this.accessToken?.value
        val oldRefreshToken = this.refreshToken?.value
        
        this.accessToken = null
        this.refreshToken = null
        this.updatedAt = LocalDateTime.now()
        
        if (oldAccessToken != null) {
            registerEvent(TokenRevoked(id.value, userId.value, oldAccessToken, "ACCESS_TOKEN"))
        }
        
        if (oldRefreshToken != null) {
            registerEvent(TokenRevoked(id.value, userId.value, oldRefreshToken, "REFRESH_TOKEN"))
        }
        
        return this
    }

    /**
     * 액세스 토큰 검증
     *
     * @param token 검증할 토큰 값
     * @return 유효하면 true, 아니면 false
     */
    fun validateAccessToken(token: String): Boolean {
        val currentToken = this.accessToken ?: return false
        
        if (currentToken.isExpired()) {
            registerEvent(TokenValidated(id.value, userId.value, token, "ACCESS_TOKEN", false, "토큰이 만료되었습니다."))
            return false
        }
        
        val isValid = currentToken.value == token
        
        registerEvent(TokenValidated(id.value, userId.value, token, "ACCESS_TOKEN", isValid, 
            if (isValid) "토큰이 유효합니다." else "토큰이 일치하지 않습니다."))
        
        return isValid
    }

    /**
     * 리프레시 토큰 검증
     *
     * @param token 검증할 토큰 값
     * @return 유효하면 true, 아니면 false
     */
    fun validateRefreshToken(token: String): Boolean {
        val currentToken = this.refreshToken ?: return false
        
        if (currentToken.isExpired()) {
            registerEvent(TokenValidated(id.value, userId.value, token, "REFRESH_TOKEN", false, "토큰이 만료되었습니다."))
            return false
        }
        
        val isValid = currentToken.value == token
        
        registerEvent(TokenValidated(id.value, userId.value, token, "REFRESH_TOKEN", isValid, 
            if (isValid) "토큰이 유효합니다." else "토큰이 일치하지 않습니다."))
        
        return isValid
    }

    /**
     * 액세스 토큰 조회
     *
     * @return 액세스 토큰 (없으면 null)
     */
    fun getAccessToken(): TokenValue? = accessToken

    /**
     * 리프레시 토큰 조회
     *
     * @return 리프레시 토큰 (없으면 null)
     */
    fun getRefreshToken(): TokenValue? = refreshToken

    /**
     * 마지막 인증 시간 조회
     *
     * @return 마지막 인증 시간 (없으면 null)
     */
    fun getLastAuthenticated(): LocalDateTime? = lastAuthenticated

    /**
     * 생성 시간 조회
     *
     * @return 생성 시간
     */
    fun getCreatedAt(): LocalDateTime = createdAt

    /**
     * 업데이트 시간 조회
     *
     * @return 업데이트 시간
     */
    fun getUpdatedAt(): LocalDateTime = updatedAt
    
    companion object {
        /**
         * 새 인증 객체 생성
         *
         * @param userId 사용자 ID
         * @return 새로 생성된 Authentication 인스턴스
         */
        fun create(userId: UserId): Authentication {
            val authId = AuthenticationId.newId()
            
            val authentication = Authentication(
                id = authId,
                userId = userId
            )
            
            authentication.registerEvent(AuthenticationCreated(authId.value, userId.value))
            return authentication
        }
    }
}