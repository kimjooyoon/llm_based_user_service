package com.github.kimjooyoon.userservice.core.application.service

import com.github.kimjooyoon.userservice.core.application.dto.PermissionDto
import com.github.kimjooyoon.userservice.core.application.dto.RoleDto
import com.github.kimjooyoon.userservice.core.domain.model.permission.Permission
import com.github.kimjooyoon.userservice.core.domain.model.permission.PermissionRepository
import com.github.kimjooyoon.userservice.core.domain.model.permission.Role
import com.github.kimjooyoon.userservice.core.domain.model.permission.RoleRepository
import com.github.kimjooyoon.userservice.core.domain.model.permission.vo.PermissionId
import com.github.kimjooyoon.userservice.core.domain.model.permission.vo.PermissionName
import com.github.kimjooyoon.userservice.core.domain.model.permission.vo.ResourceType
import com.github.kimjooyoon.userservice.core.domain.model.permission.vo.RoleId
import com.github.kimjooyoon.userservice.core.domain.model.permission.vo.RoleName
import com.github.kimjooyoon.userservice.core.domain.model.user.vo.UserId
import com.github.kimjooyoon.userservice.message.EventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * 역할 및 권한 관리 서비스 인터페이스
 */
interface RoleService {
    /**
     * 역할 생성
     */
    fun createRole(name: String, description: String?): RoleDto
    
    /**
     * 역할 조회
     */
    fun getRole(roleId: String): RoleDto
    
    /**
     * 역할 목록 조회
     */
    fun getAllRoles(page: Int, size: Int): List<RoleDto>
    
    /**
     * 역할 검색
     */
    fun searchRoles(searchTerm: String, page: Int, size: Int): List<RoleDto>
    
    /**
     * 역할 업데이트
     */
    fun updateRole(roleId: String, name: String, description: String?): RoleDto
    
    /**
     * 역할 삭제
     */
    fun deleteRole(roleId: String)
    
    /**
     * 권한 생성
     */
    fun createPermission(name: String, resourceType: String, action: String, description: String?): PermissionDto
    
    /**
     * 권한 조회
     */
    fun getPermission(permissionId: String): PermissionDto
    
    /**
     * 권한 목록 조회
     */
    fun getAllPermissions(page: Int, size: Int): List<PermissionDto>
    
    /**
     * 리소스 타입별 권한 목록 조회
     */
    fun getPermissionsByResourceType(resourceType: String): List<PermissionDto>
    
    /**
     * 권한 업데이트
     */
    fun updatePermission(permissionId: String, name: String, description: String?): PermissionDto
    
    /**
     * 권한 삭제
     */
    fun deletePermission(permissionId: String)
    
    /**
     * 역할에 권한 추가
     */
    fun addPermissionToRole(roleId: String, permissionId: String): RoleDto
    
    /**
     * 역할에서 권한 제거
     */
    fun removePermissionFromRole(roleId: String, permissionId: String): RoleDto
    
    /**
     * 역할의 권한 목록 조회
     */
    fun getPermissionsForRole(roleId: String): List<PermissionDto>
    
    /**
     * 사용자에게 역할 할당
     */
    fun assignRoleToUser(userId: String, roleId: String)
    
    /**
     * 사용자에게서 역할 제거
     */
    fun removeRoleFromUser(userId: String, roleId: String)
    
    /**
     * 사용자의 역할 목록 조회
     */
    fun getRolesForUser(userId: String): List<RoleDto>
    
    /**
     * 사용자가 역할을 가지고 있는지 확인
     */
    fun userHasRole(userId: String, roleId: String): Boolean
    
    /**
     * 사용자가 역할을 가지고 있는지 확인 (역할 이름으로)
     */
    fun userHasRole(userId: String, roleName: String): Boolean
    
    /**
     * 사용자가 특정 권한을 가지고 있는지 확인
     */
    fun userHasPermission(userId: String, permissionId: String): Boolean
    
    /**
     * 사용자가 특정 리소스와 액션에 대한 권한을 가지고 있는지 확인
     */
    fun userHasPermission(userId: String, resourceType: String, action: String): Boolean
}

/**
 * 역할 및 권한 관리 서비스 구현체
 */
@Service
class RoleServiceImpl(
    private val roleRepository: RoleRepository,
    private val permissionRepository: PermissionRepository,
    private val eventPublisher: EventPublisher
) : RoleService {
    
    @Transactional
    override fun createRole(name: String, description: String?): RoleDto {
        // 이름 검증
        val roleName = RoleName.of(name)
        
        // 이름 중복 확인
        if (roleRepository.findByName(roleName) != null) {
            throw IllegalArgumentException("이미 존재하는 역할 이름입니다: $name")
        }
        
        // 역할 생성
        val role = Role.create(roleName, description)
        
        // 역할 저장
        val savedRole = roleRepository.save(role)
        
        // 이벤트 발행
        savedRole.clearEvents().forEach { eventPublisher.publish(it) }
        
        // DTO 변환
        return RoleDto.fromRole(savedRole)
    }
    
    @Transactional(readOnly = true)
    override fun getRole(roleId: String): RoleDto {
        val role = roleRepository.findById(RoleId(roleId))
            ?: throw IllegalArgumentException("존재하지 않는 역할입니다: $roleId")
        
        return RoleDto.fromRole(role)
    }
    
    @Transactional(readOnly = true)
    override fun getAllRoles(page: Int, size: Int): List<RoleDto> {
        val offset = page * size
        val roles = roleRepository.findWithPagination(offset, size)
        
        return roles.map { RoleDto.fromRole(it) }
    }
    
    @Transactional(readOnly = true)
    override fun searchRoles(searchTerm: String, page: Int, size: Int): List<RoleDto> {
        val offset = page * size
        val roles = roleRepository.search(searchTerm, offset, size)
        
        return roles.map { RoleDto.fromRole(it) }
    }
    
    @Transactional
    override fun updateRole(roleId: String, name: String, description: String?): RoleDto {
        val role = roleRepository.findById(RoleId(roleId))
            ?: throw IllegalArgumentException("존재하지 않는 역할입니다: $roleId")
        
        val roleName = RoleName.of(name)
        
        // 이름이 변경된 경우 중복 확인
        val existingRole = roleRepository.findByName(roleName)
        if (existingRole != null && existingRole.id.value != roleId) {
            throw IllegalArgumentException("이미 존재하는 역할 이름입니다: $name")
        }
        
        // 역할 업데이트
        role.update(roleName, description)
        
        // 역할 저장
        val updatedRole = roleRepository.save(role)
        
        // 이벤트 발행
        updatedRole.clearEvents().forEach { eventPublisher.publish(it) }
        
        // DTO 변환
        return RoleDto.fromRole(updatedRole)
    }
    
    @Transactional
    override fun deleteRole(roleId: String) {
        val role = roleRepository.findById(RoleId(roleId))
            ?: throw IllegalArgumentException("존재하지 않는 역할입니다: $roleId")
        
        // 역할 삭제 이벤트 등록
        role.delete()
        
        // 이벤트 발행
        role.clearEvents().forEach { eventPublisher.publish(it) }
        
        // 역할 삭제
        roleRepository.delete(role)
    }
    
    @Transactional
    override fun createPermission(name: String, resourceType: String, action: String, description: String?): PermissionDto {
        // 값 객체 생성
        val permissionName = PermissionName.of(name)
        val resourceTypeObj = ResourceType.of(resourceType)
        
        // 중복 검증
        if (permissionRepository.findByResourceTypeAndAction(resourceTypeObj, action) != null) {
            throw IllegalArgumentException("이미 존재하는 리소스 타입과 액션입니다: $resourceType - $action")
        }
        
        // 권한 생성
        val permission = Permission.create(permissionName, resourceTypeObj, action, description)
        
        // 권한 저장
        val savedPermission = permissionRepository.save(permission)
        
        // 이벤트 발행
        savedPermission.clearEvents().forEach { eventPublisher.publish(it) }
        
        // DTO 변환
        return PermissionDto.fromPermission(savedPermission)
    }
    
    @Transactional(readOnly = true)
    override fun getPermission(permissionId: String): PermissionDto {
        val permission = permissionRepository.findById(PermissionId(permissionId))
            ?: throw IllegalArgumentException("존재하지 않는 권한입니다: $permissionId")
        
        return PermissionDto.fromPermission(permission)
    }
    
    @Transactional(readOnly = true)
    override fun getAllPermissions(page: Int, size: Int): List<PermissionDto> {
        val offset = page * size
        val permissions = permissionRepository.findWithPagination(offset, size)
        
        return permissions.map { PermissionDto.fromPermission(it) }
    }
    
    @Transactional(readOnly = true)
    override fun getPermissionsByResourceType(resourceType: String): List<PermissionDto> {
        val resourceTypeObj = ResourceType.of(resourceType)
        val permissions = permissionRepository.findAllByResourceType(resourceTypeObj)
        
        return permissions.map { PermissionDto.fromPermission(it) }
    }
    
    @Transactional
    override fun updatePermission(permissionId: String, name: String, description: String?): PermissionDto {
        val permission = permissionRepository.findById(PermissionId(permissionId))
            ?: throw IllegalArgumentException("존재하지 않는 권한입니다: $permissionId")
        
        val permissionName = PermissionName.of(name)
        
        // 권한 업데이트
        permission.update(permissionName, description)
        
        // 권한 저장
        val updatedPermission = permissionRepository.save(permission)
        
        // 이벤트 발행
        updatedPermission.clearEvents().forEach { eventPublisher.publish(it) }
        
        // DTO 변환
        return PermissionDto.fromPermission(updatedPermission)
    }
    
    @Transactional
    override fun deletePermission(permissionId: String) {
        val permission = permissionRepository.findById(PermissionId(permissionId))
            ?: throw IllegalArgumentException("존재하지 않는 권한입니다: $permissionId")
        
        // 권한 삭제 이벤트 등록
        permission.delete()
        
        // 이벤트 발행
        permission.clearEvents().forEach { eventPublisher.publish(it) }
        
        // 권한 삭제
        permissionRepository.delete(permission)
    }
    
    @Transactional
    override fun addPermissionToRole(roleId: String, permissionId: String): RoleDto {
        val role = roleRepository.findById(RoleId(roleId))
            ?: throw IllegalArgumentException("존재하지 않는 역할입니다: $roleId")
        
        val permission = permissionRepository.findById(PermissionId(permissionId))
            ?: throw IllegalArgumentException("존재하지 않는 권한입니다: $permissionId")
        
        // 역할에 권한 추가
        role.addPermission(permission)
        
        // 역할 저장
        val updatedRole = roleRepository.save(role)
        
        // 이벤트 발행
        updatedRole.clearEvents().forEach { eventPublisher.publish(it) }
        
        // DTO 변환
        return RoleDto.fromRole(updatedRole)
    }
    
    @Transactional
    override fun removePermissionFromRole(roleId: String, permissionId: String): RoleDto {
        val role = roleRepository.findById(RoleId(roleId))
            ?: throw IllegalArgumentException("존재하지 않는 역할입니다: $roleId")
        
        val permission = permissionRepository.findById(PermissionId(permissionId))
            ?: throw IllegalArgumentException("존재하지 않는 권한입니다: $permissionId")
        
        // 역할에서 권한 제거
        role.removePermission(permission)
        
        // 역할 저장
        val updatedRole = roleRepository.save(role)
        
        // 이벤트 발행
        updatedRole.clearEvents().forEach { eventPublisher.publish(it) }
        
        // DTO 변환
        return RoleDto.fromRole(updatedRole)
    }
    
    @Transactional(readOnly = true)
    override fun getPermissionsForRole(roleId: String): List<PermissionDto> {
        val role = roleRepository.findById(RoleId(roleId))
            ?: throw IllegalArgumentException("존재하지 않는 역할입니다: $roleId")
        
        return role.getPermissions().map { PermissionDto.fromPermission(it) }
    }
    
    @Transactional
    override fun assignRoleToUser(userId: String, roleId: String) {
        val roleIdObj = RoleId(roleId)
        val userIdObj = UserId(userId)
        
        // 역할 존재 확인
        if (roleRepository.findById(roleIdObj) == null) {
            throw IllegalArgumentException("존재하지 않는 역할입니다: $roleId")
        }
        
        // 사용자-역할 관계 추가
        roleRepository.assignRoleToUser(userIdObj, roleIdObj)
    }
    
    @Transactional
    override fun removeRoleFromUser(userId: String, roleId: String) {
        val roleIdObj = RoleId(roleId)
        val userIdObj = UserId(userId)
        
        // 역할 존재 확인
        if (roleRepository.findById(roleIdObj) == null) {
            throw IllegalArgumentException("존재하지 않는 역할입니다: $roleId")
        }
        
        // 사용자-역할 관계 제거
        roleRepository.removeRoleFromUser(userIdObj, roleIdObj)
    }
    
    @Transactional(readOnly = true)
    override fun getRolesForUser(userId: String): List<RoleDto> {
        val userIdObj = UserId(userId)
        val roles = roleRepository.findAllByUserId(userIdObj)
        
        return roles.map { RoleDto.fromRole(it) }
    }
    
    @Transactional(readOnly = true)
    override fun userHasRole(userId: String, roleId: String): Boolean {
        val userIdObj = UserId(userId)
        val roleIdObj = RoleId(roleId)
        
        return roleRepository.hasUserRole(userIdObj, roleIdObj)
    }
    
    @Transactional(readOnly = true)
    override fun userHasRole(userId: String, roleName: String): Boolean {
        val userIdObj = UserId(userId)
        val roleNameObj = RoleName.of(roleName)
        
        return roleRepository.hasUserRole(userIdObj, roleNameObj)
    }
    
    @Transactional(readOnly = true)
    override fun userHasPermission(userId: String, permissionId: String): Boolean {
        val userIdObj = UserId(userId)
        
        // 사용자의 모든 역할 조회
        val roles = roleRepository.findAllByUserId(userIdObj)
        
        // 어떤 역할이든 해당 권한을 가지고 있는지 확인
        return roles.any { it.hasPermission(permissionId) }
    }
    
    @Transactional(readOnly = true)
    override fun userHasPermission(userId: String, resourceType: String, action: String): Boolean {
        val userIdObj = UserId(userId)
        val resourceTypeObj = ResourceType.of(resourceType)
        
        // 리소스 타입과 액션에 해당하는 권한 찾기
        val permission = permissionRepository.findByResourceTypeAndAction(resourceTypeObj, action)
            ?: return false
        
        // 사용자의 모든 역할 조회
        val roles = roleRepository.findAllByUserId(userIdObj)
        
        // 어떤 역할이든 해당 권한을 가지고 있는지 확인
        return roles.any { it.hasPermission(permission) }
    }
}