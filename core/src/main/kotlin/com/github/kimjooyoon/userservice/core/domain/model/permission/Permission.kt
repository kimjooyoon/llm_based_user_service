package com.github.kimjooyoon.userservice.core.domain.model.permission

import com.github.kimjooyoon.userservice.core.domain.AggregateRoot
import com.github.kimjooyoon.userservice.core.domain.model.permission.event.PermissionCreatedEvent
import com.github.kimjooyoon.userservice.core.domain.model.permission.event.PermissionDeletedEvent
import com.github.kimjooyoon.userservice.core.domain.model.permission.event.PermissionUpdatedEvent
import com.github.kimjooyoon.userservice.core.domain.model.permission.vo.PermissionId
import com.github.kimjooyoon.userservice.core.domain.model.permission.vo.PermissionName
import com.github.kimjooyoon.userservice.core.domain.model.permission.vo.ResourceType
import java.time.LocalDateTime

/**
 * 권한(Permission) 애그리게이트 루트 엔티티
 * 
 * 권한은 특정 리소스에 대한 접근 제어를 정의합니다.
 * 각 권한은 고유한 ID, 이름, 리소스 타입, 액션(행동)을 가집니다.
 */
class Permission private constructor(
    val id: PermissionId,
    private var name: PermissionName,
    private var resourceType: ResourceType,
    private var action: String,
    private var description: String?,
    private val createdAt: LocalDateTime = LocalDateTime.now(),
    private var updatedAt: LocalDateTime = LocalDateTime.now()
) : AggregateRoot<PermissionId>() {

    /**
     * 권한 이름 조회
     */
    fun getName(): String = name.value
    
    /**
     * 리소스 타입 조회
     */
    fun getResourceType(): String = resourceType.value
    
    /**
     * 액션 조회
     */
    fun getAction(): String = action
    
    /**
     * 설명 조회
     */
    fun getDescription(): String? = description
    
    /**
     * 생성 시간 조회
     */
    fun getCreatedAt(): LocalDateTime = createdAt
    
    /**
     * 수정 시간 조회
     */
    fun getUpdatedAt(): LocalDateTime = updatedAt
    
    /**
     * 권한 정보 업데이트
     */
    fun update(name: PermissionName, description: String?) {
        this.name = name
        this.description = description
        this.updatedAt = LocalDateTime.now()
        
        registerEvent(PermissionUpdatedEvent(
            permissionId = id.value,
            name = name.value,
            resourceType = resourceType.value,
            action = action,
            description = description,
            updatedAt = updatedAt
        ))
    }
    
    /**
     * 권한 삭제
     */
    fun delete() {
        registerEvent(PermissionDeletedEvent(
            permissionId = id.value
        ))
    }
    
    /**
     * 권한 체크
     * 
     * @param resourceType 리소스 타입
     * @param action 액션(행동)
     * @return 해당 리소스와 액션에 대한 권한이 있는지 여부
     */
    fun matches(resourceType: String, action: String): Boolean {
        return this.resourceType.value == resourceType && this.action == action
    }
    
    companion object {
        /**
         * 새 권한 생성
         */
        fun create(
            name: PermissionName,
            resourceType: ResourceType,
            action: String,
            description: String?
        ): Permission {
            val permissionId = PermissionId.newId()
            val permission = Permission(
                id = permissionId,
                name = name,
                resourceType = resourceType,
                action = action,
                description = description
            )
            
            permission.registerEvent(PermissionCreatedEvent(
                permissionId = permissionId.value,
                name = name.value,
                resourceType = resourceType.value,
                action = action,
                description = description,
                createdAt = permission.createdAt
            ))
            
            return permission
        }
        
        /**
         * 기존 권한 재구성
         */
        fun reconstitute(
            id: PermissionId,
            name: PermissionName,
            resourceType: ResourceType,
            action: String,
            description: String?,
            createdAt: LocalDateTime,
            updatedAt: LocalDateTime
        ): Permission {
            return Permission(
                id = id,
                name = name,
                resourceType = resourceType,
                action = action,
                description = description,
                createdAt = createdAt,
                updatedAt = updatedAt
            )
        }
    }
    
    override fun getId(): PermissionId = id
    
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        
        other as Permission
        
        return id == other.id
    }
    
    override fun hashCode(): Int {
        return id.hashCode()
    }
    
    override fun toString(): String {
        return "Permission(id=$id, name=$name, resourceType=$resourceType, action=$action, description=$description, createdAt=$createdAt, updatedAt=$updatedAt)"
    }
}