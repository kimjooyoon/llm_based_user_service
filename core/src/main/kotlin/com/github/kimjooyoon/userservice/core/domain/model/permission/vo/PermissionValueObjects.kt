package com.github.kimjooyoon.userservice.core.domain.model.permission.vo

import java.util.UUID

/**
 * 권한 ID 값 객체
 */
data class PermissionId(val value: String) {
    init {
        require(value.isNotBlank()) { "권한 ID는 비어있을 수 없습니다." }
    }

    companion object {
        /**
         * 새 권한 ID 생성
         */
        fun newId(): PermissionId {
            return PermissionId(UUID.randomUUID().toString())
        }
    }
}

/**
 * 권한 이름 값 객체
 */
data class PermissionName(val value: String) {
    init {
        require(value.isNotBlank()) { "권한 이름은 비어있을 수 없습니다." }
        require(value.length <= 100) { "권한 이름은 100자를 초과할 수 없습니다." }
    }

    companion object {
        /**
         * 권한 이름 생성
         */
        fun of(value: String): PermissionName {
            return PermissionName(value)
        }
    }
}

/**
 * 역할 ID 값 객체
 */
data class RoleId(val value: String) {
    init {
        require(value.isNotBlank()) { "역할 ID는 비어있을 수 없습니다." }
    }

    companion object {
        /**
         * 새 역할 ID 생성
         */
        fun newId(): RoleId {
            return RoleId(UUID.randomUUID().toString())
        }
    }
}

/**
 * 역할 이름 값 객체
 */
data class RoleName(val value: String) {
    init {
        require(value.isNotBlank()) { "역할 이름은 비어있을 수 없습니다." }
        require(value.length <= 50) { "역할 이름은 50자를 초과할 수 없습니다." }
    }

    companion object {
        /**
         * 역할 이름 생성
         */
        fun of(value: String): RoleName {
            return RoleName(value)
        }
    }
}

/**
 * 리소스 타입 값 객체
 * 
 * 리소스 타입은 권한이 적용되는 대상 리소스의 종류를 나타냅니다.
 * 예: USER, ROLE, PERMISSION, ORDER, PRODUCT 등
 */
data class ResourceType(val value: String) {
    init {
        require(value.isNotBlank()) { "리소스 타입은 비어있을 수 없습니다." }
        require(value.matches(Regex("^[A-Z][A-Z0-9_]*$"))) { 
            "리소스 타입은 대문자와 숫자, 언더스코어(_)만 포함할 수 있으며 대문자로 시작해야 합니다." 
        }
    }

    companion object {
        // 미리 정의된 리소스 타입들
        val USER = ResourceType("USER")
        val ROLE = ResourceType("ROLE")
        val PERMISSION = ResourceType("PERMISSION")
        val AUTHENTICATION = ResourceType("AUTHENTICATION")
        
        /**
         * 리소스 타입 생성
         */
        fun of(value: String): ResourceType {
            return ResourceType(value)
        }
    }
}