package com.github.kimjooyoon.userservice.core.domain.model.user

/**
 * 사용자 프로필 엔티티
 *
 * 사용자의 프로필 정보를 담고 있는 엔티티입니다.
 * User Aggregate Root에 포함되는 엔티티입니다.
 */
class UserProfile private constructor(
    val name: String,
    val phoneNumber: String?
) {
    /**
     * 프로필 정보 업데이트
     *
     * @param name 이름
     * @param phoneNumber 전화번호 (선택)
     * @return 업데이트된 UserProfile 인스턴스
     */
    fun update(name: String, phoneNumber: String?): UserProfile {
        return UserProfile(name, phoneNumber)
    }
    
    companion object {
        /**
         * 새 사용자 프로필 생성
         *
         * @param name 이름
         * @param phoneNumber 전화번호 (선택)
         * @return 새로 생성된 UserProfile 인스턴스
         */
        fun create(name: String, phoneNumber: String? = null): UserProfile {
            require(name.isNotBlank()) { "이름은 비어있을 수 없습니다." }
            
            // 전화번호 형식 검증 (선택적)
            phoneNumber?.let {
                require(isValidPhoneNumber(it)) { "유효하지 않은 전화번호 형식입니다: $it" }
            }
            
            return UserProfile(name, phoneNumber)
        }
        
        /**
         * 전화번호 형식 검증
         * 
         * @param phoneNumber 검증할 전화번호
         * @return 유효한 형식이면 true, 아니면 false
         */
        private fun isValidPhoneNumber(phoneNumber: String): Boolean {
            // 간단한 전화번호 형식 검증 (필요에 따라 수정)
            val regex = Regex("^\\+?[0-9]{10,15}$")
            return regex.matches(phoneNumber)
        }
    }
}