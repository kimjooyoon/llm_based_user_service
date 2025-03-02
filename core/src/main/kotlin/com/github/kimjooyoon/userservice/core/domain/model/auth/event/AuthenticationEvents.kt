package com.github.kimjooyoon.userservice.core.domain.model.auth.event

import com.github.kimjooyoon.userservice.core.domain.model.event.DomainEvent
import java.time.LocalDateTime
import java.util.UUID

/**
 * 인증 도메인 이벤트의 추상 기반 클래스
 */
abstract class AuthenticationEvent(
    override val eventId: String = UUID.randomUUID().toString(),
    override val occurredAt: LocalDateTime = LocalDateTime.now(),
    open val authenticationId: String
) : DomainEvent

/**
 * 인증 객체 생성 이벤트
 */
data class AuthenticationCreated(
    override val authenticationId: String,
    val userId: String,
    override val eventId: String = UUID.randomUUID().toString(),
    override val occurredAt: LocalDateTime = LocalDateTime.now()
) : AuthenticationEvent(eventId, occurredAt, authenticationId) {
    override val eventType: String = "AUTHENTICATION_CREATED"
}

/**
 * 토큰 발급 이벤트
 */
data class TokenIssued(
    override val authenticationId: String,
    val userId: String,
    val token: String,
    val tokenType: String,
    val expiresAt: LocalDateTime,
    override val eventId: String = UUID.randomUUID().toString(),
    override val occurredAt: LocalDateTime = LocalDateTime.now()
) : AuthenticationEvent(eventId, occurredAt, authenticationId) {
    override val eventType: String = "TOKEN_ISSUED"
}

/**
 * 토큰 갱신 이벤트
 */
data class TokenRefreshed(
    override val authenticationId: String,
    val userId: String,
    val token: String,
    val tokenType: String,
    val expiresAt: LocalDateTime,
    override val eventId: String = UUID.randomUUID().toString(),
    override val occurredAt: LocalDateTime = LocalDateTime.now()
) : AuthenticationEvent(eventId, occurredAt, authenticationId) {
    override val eventType: String = "TOKEN_REFRESHED"
}

/**
 * 토큰 폐기 이벤트
 */
data class TokenRevoked(
    override val authenticationId: String,
    val userId: String,
    val token: String,
    val tokenType: String,
    override val eventId: String = UUID.randomUUID().toString(),
    override val occurredAt: LocalDateTime = LocalDateTime.now()
) : AuthenticationEvent(eventId, occurredAt, authenticationId) {
    override val eventType: String = "TOKEN_REVOKED"
}

/**
 * 토큰 검증 이벤트
 */
data class TokenValidated(
    override val authenticationId: String,
    val userId: String,
    val token: String,
    val tokenType: String,
    val isValid: Boolean,
    val message: String,
    override val eventId: String = UUID.randomUUID().toString(),
    override val occurredAt: LocalDateTime = LocalDateTime.now()
) : AuthenticationEvent(eventId, occurredAt, authenticationId) {
    override val eventType: String = "TOKEN_VALIDATED"
}

/**
 * 인증 세션 만료 이벤트
 */
data class AuthenticationExpired(
    override val authenticationId: String,
    val userId: String,
    val reason: String,
    override val eventId: String = UUID.randomUUID().toString(),
    override val occurredAt: LocalDateTime = LocalDateTime.now()
) : AuthenticationEvent(eventId, occurredAt, authenticationId) {
    override val eventType: String = "AUTHENTICATION_EXPIRED"
}