package com.github.kimjooyoon.userservice.core.domain.model.permission.event

import com.github.kimjooyoon.userservice.core.domain.DomainEvent
import java.time.LocalDateTime

/**
 * 권한 관련 기본 이벤트 클래스
 */
abstract class PermissionEvent(
    override val eventId: String = java.util.UUID.randomUUID().toString(),
    override val timestamp: LocalDateTime = LocalDateTime.now(),
    override val eventType: String
) : DomainEvent

/**
 * 권한 생성 이벤트
 */
data class PermissionCreatedEvent(
    val permissionId: String,
    val name: String,
    val resourceType: String,
    val action: String,
    val description: String?,
    val createdAt: LocalDateTime
) : PermissionEvent(
    eventType = "permission.created"
)

/**
 * 권한 업데이트 이벤트
 */
data class PermissionUpdatedEvent(
    val permissionId: String,
    val name: String,
    val resourceType: String,
    val action: String,
    val description: String?,
    val updatedAt: LocalDateTime
) : PermissionEvent(
    eventType = "permission.updated"
)

/**
 * 권한 삭제 이벤트
 */
data class PermissionDeletedEvent(
    val permissionId: String
) : PermissionEvent(
    eventType = "permission.deleted"
)

/**
 * 역할 관련 기본 이벤트 클래스
 */
abstract class RoleEvent(
    override val eventId: String = java.util.UUID.randomUUID().toString(),
    override val timestamp: LocalDateTime = LocalDateTime.now(),
    override val eventType: String
) : DomainEvent

/**
 * 역할 생성 이벤트
 */
data class RoleCreatedEvent(
    val roleId: String,
    val name: String,
    val description: String?,
    val createdAt: LocalDateTime
) : RoleEvent(
    eventType = "role.created"
)

/**
 * 역할 업데이트 이벤트
 */
data class RoleUpdatedEvent(
    val roleId: String,
    val name: String,
    val description: String?,
    val updatedAt: LocalDateTime
) : RoleEvent(
    eventType = "role.updated"
)

/**
 * 역할 삭제 이벤트
 */
data class RoleDeletedEvent(
    val roleId: String
) : RoleEvent(
    eventType = "role.deleted"
)

/**
 * 역할에 권한 추가 이벤트
 */
data class RolePermissionAddedEvent(
    val roleId: String,
    val permissionId: String
) : RoleEvent(
    eventType = "role.permission.added"
)

/**
 * 역할에서 권한 제거 이벤트
 */
data class RolePermissionRemovedEvent(
    val roleId: String,
    val permissionId: String
) : RoleEvent(
    eventType = "role.permission.removed"
)

/**
 * 사용자에 역할 할당 이벤트
 */
data class UserRoleAssignedEvent(
    val userId: String,
    val roleId: String
) : RoleEvent(
    eventType = "user.role.assigned"
)

/**
 * 사용자에서 역할 제거 이벤트
 */
data class UserRoleRevokedEvent(
    val userId: String,
    val roleId: String
) : RoleEvent(
    eventType = "user.role.revoked"
)