package com.github.kimjooyoon.userservice.core.application.dto

import com.github.kimjooyoon.userservice.core.domain.model.permission.Permission
import com.github.kimjooyoon.userservice.core.domain.model.permission.Role
import java.time.LocalDateTime

/**
 * 권한 DTO
 */
data class PermissionDto(
    val id: String,
    val name: String,
    val resourceType: String,
    val action: String,
    val description: String?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    companion object {
        /**
         * 도메인 모델에서 DTO로 변환
         */
        fun fromPermission(permission: Permission): PermissionDto {
            return PermissionDto(
                id = permission.id.value,
                name = permission.getName(),
                resourceType = permission.getResourceType(),
                action = permission.getAction(),
                description = permission.getDescription(),
                createdAt = permission.getCreatedAt(),
                updatedAt = permission.getUpdatedAt()
            )
        }
    }
}

/**
 * 역할 DTO
 */
data class RoleDto(
    val id: String,
    val name: String,
    val description: String?,
    val permissions: List<PermissionDto> = emptyList(),
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    companion object {
        /**
         * 도메인 모델에서 DTO로 변환
         */
        fun fromRole(role: Role): RoleDto {
            return RoleDto(
                id = role.id.value,
                name = role.getName(),
                description = role.getDescription(),
                permissions = role.getPermissions().map { PermissionDto.fromPermission(it) },
                createdAt = role.getCreatedAt(),
                updatedAt = role.getUpdatedAt()
            )
        }
    }
}

/**
 * 역할 생성 명령 DTO
 */
data class RoleCreateCommand(
    val name: String,
    val description: String?
)

/**
 * 역할 업데이트 명령 DTO
 */
data class RoleUpdateCommand(
    val name: String,
    val description: String?
)

/**
 * 권한 생성 명령 DTO
 */
data class PermissionCreateCommand(
    val name: String,
    val resourceType: String,
    val action: String,
    val description: String?
)

/**
 * 권한 업데이트 명령 DTO
 */
data class PermissionUpdateCommand(
    val name: String,
    val description: String?
)

/**
 * 역할-권한 연결 명령 DTO
 */
data class RolePermissionCommand(
    val roleId: String,
    val permissionId: String
)

/**
 * 사용자-역할 연결 명령 DTO
 */
data class UserRoleCommand(
    val userId: String,
    val roleId: String
)

/**
 * 역할 목록 조회 결과 DTO
 */
data class RoleListDto(
    val roles: List<RoleDto>,
    val totalCount: Long,
    val page: Int,
    val size: Int
)

/**
 * 권한 목록 조회 결과 DTO
 */
data class PermissionListDto(
    val permissions: List<PermissionDto>,
    val totalCount: Long,
    val page: Int,
    val size: Int
)

/**
 * 사용자 권한 검증 요청 DTO
 */
data class PermissionCheckRequestDto(
    val userId: String,
    val resourceType: String,
    val action: String
)

/**
 * 사용자 권한 검증 결과 DTO
 */
data class PermissionCheckResultDto(
    val userId: String,
    val resourceType: String,
    val action: String,
    val hasPermission: Boolean
)