package com.github.kimjooyoon.userservice.core.domain.model.user

import com.github.kimjooyoon.userservice.core.domain.model.AggregateRoot
import com.github.kimjooyoon.userservice.core.domain.model.user.event.*
import com.github.kimjooyoon.userservice.core.domain.model.user.exception.InvalidPasswordException
import com.github.kimjooyoon.userservice.core.domain.model.user.exception.UserDeactivatedException
import com.github.kimjooyoon.userservice.core.domain.model.user.vo.Email
import com.github.kimjooyoon.userservice.core.domain.model.user.vo.Password
import com.github.kimjooyoon.userservice.core.domain.model.user.vo.UserId
import com.github.kimjooyoon.userservice.core.domain.model.user.vo.UserStatus
import java.time.LocalDateTime

/**
 * 사용자(User) Aggregate Root 엔티티
 * 
 * 사용자 도메인에서 핵심 비즈니스 규칙을 포함하고 있는 Aggregate Root 엔티티입니다.
 * 모든 사용자 관련 작업은 이 엔티티를 통해 이루어집니다.
 */
class User private constructor(
    val id: UserId,
    val email: Email,
    private var password: Password,
    private var profile: UserProfile,
    private var status: UserStatus = UserStatus.INACTIVE,
    private var lastLoginAt: LocalDateTime? = null,
    private var createdAt: LocalDateTime = LocalDateTime.now(),
    private var updatedAt: LocalDateTime = LocalDateTime.now()
) : AggregateRoot<UserId>(id) {

    /**
     * 사용자 계정 활성화
     *
     * @return 활성화된 User 인스턴스
     * @throws IllegalStateException 이미 활성화된 경우
     */
    fun activate(): User {
        if (status == UserStatus.ACTIVE) {
            throw IllegalStateException("사용자 계정이 이미 활성화되어 있습니다.")
        }
        
        status = UserStatus.ACTIVE
        updatedAt = LocalDateTime.now()
        
        registerEvent(UserActivated(id.value, email.value))
        return this
    }

    /**
     * 사용자 계정 비활성화
     *
     * @return 비활성화된 User 인스턴스
     * @throws IllegalStateException 이미 비활성화된 경우
     */
    fun deactivate(): User {
        if (status == UserStatus.INACTIVE) {
            throw IllegalStateException("사용자 계정이 이미 비활성화되어 있습니다.")
        }
        
        status = UserStatus.INACTIVE
        updatedAt = LocalDateTime.now()
        
        registerEvent(UserDeactivated(id.value, email.value))
        return this
    }

    /**
     * 사용자 비밀번호 변경
     *
     * @param currentPassword 현재 비밀번호
     * @param newPassword 새 비밀번호
     * @return 비밀번호가 변경된 User 인스턴스
     * @throws InvalidPasswordException 현재 비밀번호가 일치하지 않는 경우
     * @throws UserDeactivatedException 비활성화된 사용자인 경우
     */
    fun changePassword(currentPassword: String, newPassword: String): User {
        checkActive()
        
        if (!password.matches(currentPassword)) {
            throw InvalidPasswordException("현재 비밀번호가 올바르지 않습니다.")
        }
        
        val newPasswordObj = Password.of(newPassword)
        this.password = newPasswordObj
        updatedAt = LocalDateTime.now()
        
        registerEvent(UserPasswordChanged(id.value, email.value))
        return this
    }

    /**
     * 사용자 프로필 업데이트
     *
     * @param name 이름
     * @param phoneNumber 전화번호
     * @return 프로필이 업데이트된 User 인스턴스
     * @throws UserDeactivatedException 비활성화된 사용자인 경우
     */
    fun updateProfile(name: String, phoneNumber: String?): User {
        checkActive()
        
        this.profile = profile.update(name, phoneNumber)
        updatedAt = LocalDateTime.now()
        
        registerEvent(UserProfileUpdated(id.value, email.value, profile.name, profile.phoneNumber))
        return this
    }

    /**
     * 사용자 로그인 처리
     *
     * @param password 입력된 비밀번호
     * @return 로그인된 User 인스턴스
     * @throws InvalidPasswordException 비밀번호가 일치하지 않는 경우
     * @throws UserDeactivatedException 비활성화된 사용자인 경우
     */
    fun login(password: String): User {
        checkActive()
        
        if (!this.password.matches(password)) {
            registerEvent(UserLoginFailed(id.value, email.value, "비밀번호 불일치"))
            throw InvalidPasswordException("비밀번호가 올바르지 않습니다.")
        }
        
        lastLoginAt = LocalDateTime.now()
        updatedAt = LocalDateTime.now()
        
        registerEvent(UserLoginSucceeded(id.value, email.value))
        return this
    }

    /**
     * 사용자 상태가 활성화 상태인지 확인
     *
     * @throws UserDeactivatedException 비활성화된 사용자인 경우
     */
    private fun checkActive() {
        if (status != UserStatus.ACTIVE) {
            throw UserDeactivatedException("비활성화된 사용자 계정입니다.")
        }
    }

    /**
     * 사용자 상태 조회
     *
     * @return 사용자 상태 (ACTIVE, INACTIVE)
     */
    fun getStatus(): UserStatus = status

    /**
     * 사용자 프로필 조회
     *
     * @return 사용자 프로필
     */
    fun getProfile(): UserProfile = profile

    /**
     * 마지막 로그인 시간 조회
     *
     * @return 마지막 로그인 시간 (없으면 null)
     */
    fun getLastLoginAt(): LocalDateTime? = lastLoginAt

    /**
     * 생성 시간 조회
     *
     * @return 사용자 계정 생성 시간
     */
    fun getCreatedAt(): LocalDateTime = createdAt

    /**
     * 업데이트 시간 조회
     *
     * @return 사용자 계정 마지막 업데이트 시간
     */
    fun getUpdatedAt(): LocalDateTime = updatedAt
    
    companion object {
        /**
         * 새 사용자 생성
         *
         * @param email 이메일
         * @param password 비밀번호
         * @param name 이름
         * @param phoneNumber 전화번호 (선택)
         * @return 새로 생성된 User 인스턴스
         */
        fun create(email: String, password: String, name: String, phoneNumber: String? = null): User {
            val userId = UserId.newId()
            val userEmail = Email.of(email)
            val userPassword = Password.of(password)
            val userProfile = UserProfile.create(name, phoneNumber)
            
            val user = User(
                id = userId,
                email = userEmail,
                password = userPassword,
                profile = userProfile
            )
            
            user.registerEvent(UserRegistered(userId.value, userEmail.value, userProfile.name))
            return user
        }
    }
}