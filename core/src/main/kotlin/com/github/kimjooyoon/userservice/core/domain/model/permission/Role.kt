package com.github.kimjooyoon.userservice.core.domain.model.permission

import com.github.kimjooyoon.userservice.core.domain.AggregateRoot
import com.github.kimjooyoon.userservice.core.domain.model.permission.event.RoleCreatedEvent
import com.github.kimjooyoon.userservice.core.domain.model.permission.event.RoleDeletedEvent
import com.github.kimjooyoon.userservice.core.domain.model.permission.event.RolePermissionAddedEvent
import com.github.kimjooyoon.userservice.core.domain.model.permission.event.RolePermissionRemovedEvent
import com.github.kimjooyoon.userservice.core.domain.model.permission.event.RoleUpdatedEvent
import com.github.kimjooyoon.userservice.core.domain.model.permission.vo.RoleId
import com.github.kimjooyoon.userservice.core.domain.model.permission.vo.RoleName
import java.time.LocalDateTime

/**
 * 역할(Role) 애그리게이트 루트 엔티티
 * 
 * 역할은 사용자에게 부여할 수 있는 권한의 집합입니다.
 * 각 역할은 고유한 ID와 이름을 가지며, 0개 이상의 권한(Permission)을 가질 수 있습니다.
 */
class Role private constructor(
    val id: RoleId,
    private var name: RoleName,
    private var description: String?,
    private val permissions: MutableSet<Permission> = mutableSetOf(),
    private val createdAt: LocalDateTime = LocalDateTime.now(),
    private var updatedAt: LocalDateTime = LocalDateTime.now()
) : AggregateRoot<RoleId>() {

    /**
     * 역할 이름 조회
     */
    fun getName(): String = name.value
    
    /**
     * 역할 설명 조회
     */
    fun getDescription(): String? = description
    
    /**
     * 역할 생성 시간 조회
     */
    fun getCreatedAt(): LocalDateTime = createdAt
    
    /**
     * 역할 수정 시간 조회
     */
    fun getUpdatedAt(): LocalDateTime = updatedAt
    
    /**
     * 역할에 할당된 권한 목록 조회
     */
    fun getPermissions(): Set<Permission> = permissions.toSet()
    
    /**
     * 역할 정보 업데이트
     */
    fun update(name: RoleName, description: String?) {
        this.name = name
        this.description = description
        this.updatedAt = LocalDateTime.now()
        
        registerEvent(RoleUpdatedEvent(
            roleId = id.value,
            name = name.value,
            description = description,
            updatedAt = updatedAt
        ))
    }
    
    /**
     * 역할에 권한 추가
     */
    fun addPermission(permission: Permission) {
        if (permissions.add(permission)) {
            registerEvent(RolePermissionAddedEvent(
                roleId = id.value,
                permissionId = permission.id.value
            ))
        }
    }
    
    /**
     * 역할에서 권한 제거
     */
    fun removePermission(permission: Permission) {
        if (permissions.remove(permission)) {
            registerEvent(RolePermissionRemovedEvent(
                roleId = id.value,
                permissionId = permission.id.value
            ))
        }
    }
    
    /**
     * 특정 권한이 있는지 확인
     */
    fun hasPermission(permission: Permission): Boolean {
        return permissions.contains(permission)
    }
    
    /**
     * 특정 권한이 있는지 확인 (권한 ID로)
     */
    fun hasPermission(permissionId: String): Boolean {
        return permissions.any { it.id.value == permissionId }
    }
    
    /**
     * 역할 삭제
     */
    fun delete() {
        registerEvent(RoleDeletedEvent(
            roleId = id.value
        ))
    }
    
    companion object {
        /**
         * 새 역할 생성
         */
        fun create(name: RoleName, description: String?): Role {
            val roleId = RoleId.newId()
            val role = Role(
                id = roleId,
                name = name,
                description = description
            )
            
            role.registerEvent(RoleCreatedEvent(
                roleId = roleId.value,
                name = name.value,
                description = description,
                createdAt = role.createdAt
            ))
            
            return role
        }
        
        /**
         * 기존 역할 재구성
         */
        fun reconstitute(
            id: RoleId,
            name: RoleName,
            description: String?,
            permissions: Set<Permission>,
            createdAt: LocalDateTime,
            updatedAt: LocalDateTime
        ): Role {
            return Role(
                id = id,
                name = name,
                description = description,
                permissions = permissions.toMutableSet(),
                createdAt = createdAt,
                updatedAt = updatedAt
            )
        }
    }
    
    override fun getId(): RoleId = id
    
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        
        other as Role
        
        return id == other.id
    }
    
    override fun hashCode(): Int {
        return id.hashCode()
    }
    
    override fun toString(): String {
        return "Role(id=$id, name=$name, description=$description, permissions=${permissions.size}, createdAt=$createdAt, updatedAt=$updatedAt)"
    }
}