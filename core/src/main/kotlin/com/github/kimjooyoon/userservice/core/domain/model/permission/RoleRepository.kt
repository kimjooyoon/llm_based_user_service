package com.github.kimjooyoon.userservice.core.domain.model.permission

import com.github.kimjooyoon.userservice.core.domain.model.permission.vo.RoleId
import com.github.kimjooyoon.userservice.core.domain.model.permission.vo.RoleName
import com.github.kimjooyoon.userservice.core.domain.model.user.vo.UserId

/**
 * 역할 저장소 인터페이스
 */
interface RoleRepository {

    /**
     * 역할 저장
     * 
     * @param role 저장할 역할 객체
     * @return 저장된 역할 객체
     */
    fun save(role: Role): Role

    /**
     * ID로 역할 조회
     * 
     * @param id 역할 ID
     * @return 조회된 역할 객체, 없으면 null
     */
    fun findById(id: RoleId): Role?

    /**
     * 이름으로 역할 조회
     * 
     * @param name 역할 이름
     * @return 조회된 역할 객체, 없으면 null
     */
    fun findByName(name: RoleName): Role?

    /**
     * 모든 역할 조회
     * 
     * @return 모든 역할 목록
     */
    fun findAll(): List<Role>

    /**
     * 페이징을 통한 역할 조회
     * 
     * @param offset 오프셋
     * @param limit 한 페이지당 항목 수
     * @return 페이징된 역할 목록
     */
    fun findWithPagination(offset: Int, limit: Int): List<Role>

    /**
     * 총 역할 수 조회
     * 
     * @return 총 역할 수
     */
    fun count(): Long

    /**
     * 역할 삭제
     * 
     * @param role 삭제할 역할 객체
     */
    fun delete(role: Role)

    /**
     * ID로 역할 삭제
     * 
     * @param id 삭제할 역할 ID
     */
    fun deleteById(id: RoleId)

    /**
     * 여러 ID로 역할 조회
     * 
     * @param ids 조회할 역할 ID 목록
     * @return 조회된 역할 목록
     */
    fun findAllByIds(ids: Collection<RoleId>): List<Role>

    /**
     * 검색 조건에 따른 역할 조회
     * 
     * @param searchTerm 검색어 (이름 또는 설명에 포함된 텍스트)
     * @param offset 오프셋
     * @param limit 한 페이지당 항목 수
     * @return 검색 결과 역할 목록
     */
    fun search(searchTerm: String, offset: Int, limit: Int): List<Role>

    /**
     * 검색 결과의 총 개수
     * 
     * @param searchTerm 검색어
     * @return 검색 결과의 총 개수
     */
    fun countBySearch(searchTerm: String): Long

    /**
     * 특정 권한을 가진 모든 역할 조회
     * 
     * @param permissionId 권한 ID
     * @return 해당 권한을 가진 모든 역할 목록
     */
    fun findAllByPermissionId(permissionId: String): List<Role>

    /**
     * 사용자가 가진 역할 목록 조회
     * 
     * @param userId 사용자 ID
     * @return 해당 사용자가 가진 역할 목록
     */
    fun findAllByUserId(userId: UserId): List<Role>

    /**
     * 사용자-역할 관계 추가
     * 
     * @param userId 사용자 ID
     * @param roleId 역할 ID
     */
    fun assignRoleToUser(userId: UserId, roleId: RoleId)

    /**
     * 사용자-역할 관계 제거
     * 
     * @param userId 사용자 ID
     * @param roleId 역할 ID
     */
    fun removeRoleFromUser(userId: UserId, roleId: RoleId)

    /**
     * 사용자가 특정 역할을 가지고 있는지 확인
     * 
     * @param userId 사용자 ID
     * @param roleId 역할 ID
     * @return 역할 보유 여부
     */
    fun hasUserRole(userId: UserId, roleId: RoleId): Boolean

    /**
     * 사용자가 특정 역할을 가지고 있는지 확인 (역할 이름으로)
     * 
     * @param userId 사용자 ID
     * @param roleName 역할 이름
     * @return 역할 보유 여부
     */
    fun hasUserRole(userId: UserId, roleName: RoleName): Boolean
}