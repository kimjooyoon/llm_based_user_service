package com.github.kimjooyoon.userservice.core.application.dto

import com.github.kimjooyoon.userservice.core.domain.model.user.User
import java.time.LocalDateTime

/**
 * 사용자 DTO
 */
data class UserDto(
    val id: String,
    val email: String,
    val name: String,
    val phoneNumber: String?,
    val status: String,
    val lastLoginAt: LocalDateTime?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    companion object {
        /**
         * 도메인 모델에서 DTO로 변환
         */
        fun fromUser(user: User): UserDto {
            return UserDto(
                id = user.id.value,
                email = user.email.value,
                name = user.getProfile().name,
                phoneNumber = user.getProfile().phoneNumber,
                status = user.getStatus().name,
                lastLoginAt = user.getLastLoginAt(),
                createdAt = user.getCreatedAt(),
                updatedAt = user.getUpdatedAt()
            )
        }
    }
}

/**
 * 사용자 등록 명령 DTO
 */
data class UserRegistrationCommand(
    val email: String,
    val password: String,
    val name: String,
    val phoneNumber: String?
)

/**
 * 사용자 정보 업데이트 명령 DTO
 */
data class UserUpdateCommand(
    val name: String,
    val phoneNumber: String?
)

/**
 * 사용자 비밀번호 변경 명령 DTO
 */
data class PasswordChangeCommand(
    val userId: String,
    val currentPassword: String,
    val newPassword: String
)

/**
 * 사용자 비밀번호 재설정 요청 명령 DTO
 */
data class PasswordResetRequestCommand(
    val email: String
)

/**
 * 사용자 비밀번호 재설정 완료 명령 DTO
 */
data class PasswordResetCommand(
    val token: String,
    val newPassword: String
)

/**
 * 사용자 로그인 명령 DTO
 */
data class UserLoginCommand(
    val email: String,
    val password: String
)

/**
 * 사용자 조회 결과 페이징 DTO
 */
data class UserPageDto(
    val users: List<UserDto>,
    val totalCount: Long,
    val page: Int,
    val size: Int,
    val totalPages: Int
) {
    companion object {
        /**
         * 사용자 목록과 총 개수로 페이징 DTO 생성
         */
        fun of(users: List<UserDto>, totalCount: Long, page: Int, size: Int): UserPageDto {
            val totalPages = if (size > 0) (totalCount + size - 1) / size else 0
            return UserPageDto(
                users = users,
                totalCount = totalCount,
                page = page,
                size = size,
                totalPages = totalPages.toInt()
            )
        }
    }
}