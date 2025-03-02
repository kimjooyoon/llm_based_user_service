package com.github.kimjooyoon.userservice.core.domain.model.permission

import com.github.kimjooyoon.userservice.core.domain.model.permission.vo.PermissionId
import com.github.kimjooyoon.userservice.core.domain.model.permission.vo.PermissionName
import com.github.kimjooyoon.userservice.core.domain.model.permission.vo.ResourceType

/**
 * 권한 저장소 인터페이스
 */
interface PermissionRepository {

    /**
     * 권한 저장
     * 
     * @param permission 저장할 권한 객체
     * @return 저장된 권한 객체
     */
    fun save(permission: Permission): Permission

    /**
     * ID로 권한 조회
     * 
     * @param id 권한 ID
     * @return 조회된 권한 객체, 없으면 null
     */
    fun findById(id: PermissionId): Permission?

    /**
     * 이름으로 권한 조회
     * 
     * @param name 권한 이름
     * @return 조회된 권한 객체, 없으면 null
     */
    fun findByName(name: PermissionName): Permission?

    /**
     * 리소스 타입과 액션으로 권한 조회
     * 
     * @param resourceType 리소스 타입
     * @param action 액션(행동)
     * @return 조회된 권한 객체, 없으면 null
     */
    fun findByResourceTypeAndAction(resourceType: ResourceType, action: String): Permission?

    /**
     * 모든 권한 조회
     * 
     * @return 모든 권한 목록
     */
    fun findAll(): List<Permission>

    /**
     * 리소스 타입별 권한 조회
     * 
     * @param resourceType 리소스 타입
     * @return 해당 리소스 타입의 모든 권한 목록
     */
    fun findAllByResourceType(resourceType: ResourceType): List<Permission>

    /**
     * 페이징을 통한 권한 조회
     * 
     * @param offset 오프셋
     * @param limit 한 페이지당 항목 수
     * @return 페이징된 권한 목록
     */
    fun findWithPagination(offset: Int, limit: Int): List<Permission>

    /**
     * 총 권한 수 조회
     * 
     * @return 총 권한 수
     */
    fun count(): Long

    /**
     * 권한 삭제
     * 
     * @param permission 삭제할 권한 객체
     */
    fun delete(permission: Permission)

    /**
     * ID로 권한 삭제
     * 
     * @param id 삭제할 권한 ID
     */
    fun deleteById(id: PermissionId)

    /**
     * 여러 ID로 권한 조회
     * 
     * @param ids 조회할 권한 ID 목록
     * @return 조회된 권한 목록
     */
    fun findAllByIds(ids: Collection<PermissionId>): List<Permission>

    /**
     * 검색 조건에 따른 권한 조회
     * 
     * @param searchTerm 검색어 (이름 또는 설명에 포함된 텍스트)
     * @param offset 오프셋
     * @param limit 한 페이지당 항목 수
     * @return 검색 결과 권한 목록
     */
    fun search(searchTerm: String, offset: Int, limit: Int): List<Permission>

    /**
     * 검색 결과의 총 개수
     * 
     * @param searchTerm 검색어
     * @return 검색 결과의 총 개수
     */
    fun countBySearch(searchTerm: String): Long
}