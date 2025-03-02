package com.github.kimjooyoon.userservice.core.domain.model.user

import com.github.kimjooyoon.userservice.core.domain.model.user.vo.Email
import com.github.kimjooyoon.userservice.core.domain.model.user.vo.UserId

/**
 * User Aggregate Root에 대한 Repository 인터페이스
 * 
 * DDD 패턴에 따라 Aggregate Root 단위로 저장소를 정의합니다.
 */
interface UserRepository {
    /**
     * 사용자 저장
     *
     * @param user 저장할 사용자 객체
     * @return 저장된 사용자 객체
     */
    fun save(user: User): User
    
    /**
     * ID로 사용자 조회
     *
     * @param id 사용자 ID
     * @return 찾은 사용자 객체 또는 null
     */
    fun findById(id: UserId): User?
    
    /**
     * 이메일로 사용자 조회
     *
     * @param email 사용자 이메일
     * @return 찾은 사용자 객체 또는 null
     */
    fun findByEmail(email: Email): User?
    
    /**
     * ID로 사용자 존재 여부 확인
     *
     * @param id 사용자 ID
     * @return 존재하면 true, 아니면 false
     */
    fun existsById(id: UserId): Boolean
    
    /**
     * 이메일로 사용자 존재 여부 확인
     *
     * @param email 사용자 이메일
     * @return 존재하면 true, 아니면 false
     */
    fun existsByEmail(email: Email): Boolean
    
    /**
     * 사용자 삭제
     *
     * @param user 삭제할 사용자 객체
     */
    fun delete(user: User)
    
    /**
     * ID로 사용자 삭제
     *
     * @param id 삭제할 사용자 ID
     */
    fun deleteById(id: UserId)
    
    /**
     * 페이징을 적용한 사용자 목록 조회
     *
     * @param offset 시작 위치
     * @param limit 최대 개수
     * @return 사용자 목록
     */
    fun findAll(offset: Int, limit: Int): List<User>
    
    /**
     * 검색 조건에 맞는 사용자 목록 조회
     *
     * @param criteria 검색 조건
     * @param offset 시작 위치
     * @param limit 최대 개수
     * @return 사용자 목록
     */
    fun findByCriteria(criteria: UserSearchCriteria, offset: Int, limit: Int): List<User>
    
    /**
     * 검색 조건에 맞는 사용자 수 조회
     *
     * @param criteria 검색 조건
     * @return 사용자 수
     */
    fun countByCriteria(criteria: UserSearchCriteria): Long
}

/**
 * 사용자 검색 조건
 */
data class UserSearchCriteria(
    val emailPart: String? = null,
    val name: String? = null,
    val status: String? = null,
    val fromDate: String? = null,
    val toDate: String? = null
)