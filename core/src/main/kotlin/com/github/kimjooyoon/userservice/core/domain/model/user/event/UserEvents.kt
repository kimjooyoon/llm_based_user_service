package com.github.kimjooyoon.userservice.core.domain.model.user.event

import com.github.kimjooyoon.userservice.core.domain.model.event.DomainEvent
import java.time.LocalDateTime
import java.util.UUID

/**
 * 사용자 도메인 이벤트의 추상 기반 클래스
 */
abstract class UserEvent(
    override val eventId: String = UUID.randomUUID().toString(),
    override val occurredAt: LocalDateTime = LocalDateTime.now(),
    open val userId: String
) : DomainEvent

/**
 * 사용자 등록 완료 이벤트
 */
data class UserRegistered(
    override val userId: String,
    val email: String,
    val name: String,
    override val eventId: String = UUID.randomUUID().toString(),
    override val occurredAt: LocalDateTime = LocalDateTime.now()
) : UserEvent(eventId, occurredAt, userId) {
    override val eventType: String = "USER_REGISTERED"
}

/**
 * 사용자 계정 활성화 이벤트
 */
data class UserActivated(
    override val userId: String,
    val email: String,
    override val eventId: String = UUID.randomUUID().toString(),
    override val occurredAt: LocalDateTime = LocalDateTime.now()
) : UserEvent(eventId, occurredAt, userId) {
    override val eventType: String = "USER_ACTIVATED"
}

/**
 * 사용자 계정 비활성화 이벤트
 */
data class UserDeactivated(
    override val userId: String,
    val email: String,
    override val eventId: String = UUID.randomUUID().toString(),
    override val occurredAt: LocalDateTime = LocalDateTime.now()
) : UserEvent(eventId, occurredAt, userId) {
    override val eventType: String = "USER_DEACTIVATED"
}

/**
 * 사용자 비밀번호 변경 이벤트
 */
data class UserPasswordChanged(
    override val userId: String,
    val email: String,
    override val eventId: String = UUID.randomUUID().toString(),
    override val occurredAt: LocalDateTime = LocalDateTime.now()
) : UserEvent(eventId, occurredAt, userId) {
    override val eventType: String = "USER_PASSWORD_CHANGED"
}

/**
 * 사용자 비밀번호 재설정 요청 이벤트
 */
data class UserPasswordResetRequested(
    override val userId: String,
    val email: String,
    val resetToken: String,
    override val eventId: String = UUID.randomUUID().toString(),
    override val occurredAt: LocalDateTime = LocalDateTime.now()
) : UserEvent(eventId, occurredAt, userId) {
    override val eventType: String = "USER_PASSWORD_RESET_REQUESTED"
}

/**
 * 사용자 비밀번호 재설정 완료 이벤트
 */
data class UserPasswordResetCompleted(
    override val userId: String,
    val email: String,
    override val eventId: String = UUID.randomUUID().toString(),
    override val occurredAt: LocalDateTime = LocalDateTime.now()
) : UserEvent(eventId, occurredAt, userId) {
    override val eventType: String = "USER_PASSWORD_RESET_COMPLETED"
}

/**
 * 사용자 프로필 업데이트 이벤트
 */
data class UserProfileUpdated(
    override val userId: String,
    val email: String,
    val name: String,
    val phoneNumber: String?,
    override val eventId: String = UUID.randomUUID().toString(),
    override val occurredAt: LocalDateTime = LocalDateTime.now()
) : UserEvent(eventId, occurredAt, userId) {
    override val eventType: String = "USER_PROFILE_UPDATED"
}

/**
 * 사용자 로그인 성공 이벤트
 */
data class UserLoginSucceeded(
    override val userId: String,
    val email: String,
    override val eventId: String = UUID.randomUUID().toString(),
    override val occurredAt: LocalDateTime = LocalDateTime.now()
) : UserEvent(eventId, occurredAt, userId) {
    override val eventType: String = "USER_LOGIN_SUCCEEDED"
}

/**
 * 사용자 로그인 실패 이벤트
 */
data class UserLoginFailed(
    override val userId: String,
    val email: String,
    val reason: String,
    override val eventId: String = UUID.randomUUID().toString(),
    override val occurredAt: LocalDateTime = LocalDateTime.now()
) : UserEvent(eventId, occurredAt, userId) {
    override val eventType: String = "USER_LOGIN_FAILED"
}

/**
 * 사용자 로그아웃 이벤트
 */
data class UserLoggedOut(
    override val userId: String,
    val email: String,
    override val eventId: String = UUID.randomUUID().toString(),
    override val occurredAt: LocalDateTime = LocalDateTime.now()
) : UserEvent(eventId, occurredAt, userId) {
    override val eventType: String = "USER_LOGGED_OUT"
}