package com.github.kimjooyoon.userservice.core.domain.model.auth.vo

import java.time.LocalDateTime
import java.util.UUID

/**
 * 인증 ID Value Object
 */
data class AuthenticationId(val value: String) {
    init {
        require(value.isNotBlank()) { "인증 ID는 비어있을 수 없습니다." }
    }
    
    companion object {
        /**
         * 새로운 인증 ID 생성
         */
        fun newId(): AuthenticationId = AuthenticationId(UUID.randomUUID().toString())
    }
}

/**
 * 토큰 값 Value Object
 */
data class TokenValue private constructor(
    val value: String,
    val expiresAt: LocalDateTime
) {
    /**
     * 토큰 만료 여부 확인
     *
     * @return 만료되었으면 true, 아니면 false
     */
    fun isExpired(): Boolean {
        return LocalDateTime.now().isAfter(expiresAt)
    }
    
    /**
     * 토큰 유효 시간(초) 조회
     *
     * @return 유효 시간(초), 만료되었으면 0
     */
    fun getValidityInSeconds(): Long {
        if (isExpired()) {
            return 0L
        }
        
        val now = LocalDateTime.now()
        return java.time.Duration.between(now, expiresAt).seconds
    }
    
    companion object {
        /**
         * 토큰 값 객체 생성
         *
         * @param value 토큰 문자열
         * @param expirationTimeInSeconds 만료 시간(초)
         * @return 새로 생성된 TokenValue 인스턴스
         */
        fun of(value: String, expirationTimeInSeconds: Long): TokenValue {
            require(value.isNotBlank()) { "토큰 값은 비어있을 수 없습니다." }
            require(expirationTimeInSeconds > 0) { "만료 시간은 0보다 커야 합니다." }
            
            val expiresAt = LocalDateTime.now().plusSeconds(expirationTimeInSeconds)
            return TokenValue(value, expiresAt)
        }
        
        /**
         * 이미 만료 시간이 계산된 토큰 객체 생성
         *
         * @param value 토큰 문자열
         * @param expiresAt 만료 시간
         * @return 새로 생성된 TokenValue 인스턴스
         */
        fun ofWithExpiresAt(value: String, expiresAt: LocalDateTime): TokenValue {
            require(value.isNotBlank()) { "토큰 값은 비어있을 수 없습니다." }
            require(expiresAt.isAfter(LocalDateTime.now())) { "만료 시간은 현재 시간 이후여야 합니다." }
            
            return TokenValue(value, expiresAt)
        }
    }
}