package com.github.kimjooyoon.userservice.core.domain.model.auth

import com.github.kimjooyoon.userservice.core.domain.model.auth.vo.AuthenticationId
import com.github.kimjooyoon.userservice.core.domain.model.user.vo.UserId

/**
 * Authentication Aggregate Root에 대한 Repository 인터페이스
 * 
 * DDD 패턴에 따라 Aggregate Root 단위로 저장소를 정의합니다.
 */
interface AuthenticationRepository {
    /**
     * 인증 정보 저장
     *
     * @param authentication 저장할 인증 객체
     * @return 저장된 인증 객체
     */
    fun save(authentication: Authentication): Authentication
    
    /**
     * ID로 인증 정보 조회
     *
     * @param id 인증 ID
     * @return 찾은 인증 객체 또는 null
     */
    fun findById(id: AuthenticationId): Authentication?
    
    /**
     * 사용자 ID로 인증 정보 조회
     *
     * @param userId 사용자 ID
     * @return 찾은 인증 객체 또는 null
     */
    fun findByUserId(userId: UserId): Authentication?
    
    /**
     * 액세스 토큰으로 인증 정보 조회
     *
     * @param accessToken 액세스 토큰
     * @return 찾은 인증 객체 또는 null
     */
    fun findByAccessToken(accessToken: String): Authentication?
    
    /**
     * 리프레시 토큰으로 인증 정보 조회
     *
     * @param refreshToken 리프레시 토큰
     * @return 찾은 인증 객체 또는 null
     */
    fun findByRefreshToken(refreshToken: String): Authentication?
    
    /**
     * 인증 정보 삭제
     *
     * @param authentication 삭제할 인증 객체
     */
    fun delete(authentication: Authentication)
    
    /**
     * ID로 인증 정보 삭제
     *
     * @param id 삭제할 인증 ID
     */
    fun deleteById(id: AuthenticationId)
    
    /**
     * 사용자 ID로 인증 정보 삭제
     *
     * @param userId 삭제할 사용자 ID
     */
    fun deleteByUserId(userId: UserId)
    
    /**
     * 만료된 인증 정보 정리
     * 
     * @return 정리된 인증 정보 수
     */
    fun cleanupExpiredAuthentications(): Int
}