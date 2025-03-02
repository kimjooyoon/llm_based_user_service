package com.github.kimjooyoon.userservice.core.application.dto

/**
 * 토큰 정보 DTO
 * - 인증 서비스에서 로그인/토큰 갱신 후 반환하는 토큰 정보를 담습니다.
 */
data class TokenDto(
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String,
    val expiresIn: Long
)

/**
 * 토큰 검증 결과 DTO
 */
data class TokenValidationResultDto(
    val valid: Boolean,
    val userId: String?,
    val message: String?
)

/**
 * OAuth2 인증 정보 요청 DTO
 */
data class OAuth2AuthRequestDto(
    val providerType: String,
    val code: String,
    val redirectUri: String
)

/**
 * 인증 세부 정보 DTO
 */
data class AuthenticationDetailsDto(
    val id: String,
    val userId: String,
    val accessTokenIssued: Boolean,
    val refreshTokenIssued: Boolean,
    val lastAccessAt: String?,
    val createdAt: String,
    val expiresAt: String?
)