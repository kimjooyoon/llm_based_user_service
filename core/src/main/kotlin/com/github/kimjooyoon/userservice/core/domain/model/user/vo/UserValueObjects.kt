package com.github.kimjooyoon.userservice.core.domain.model.user.vo

import com.github.kimjooyoon.userservice.core.domain.model.user.exception.InvalidEmailException
import com.github.kimjooyoon.userservice.core.domain.model.user.exception.InvalidPasswordException
import java.util.UUID
import java.util.regex.Pattern

/**
 * 사용자 ID Value Object
 */
data class UserId(val value: String) {
    init {
        require(value.isNotBlank()) { "사용자 ID는 비어있을 수 없습니다." }
    }
    
    companion object {
        /**
         * 새로운 사용자 ID 생성
         */
        fun newId(): UserId = UserId(UUID.randomUUID().toString())
    }
}

/**
 * 이메일 Value Object
 */
data class Email(val value: String) {
    init {
        if (!isValid(value)) {
            throw InvalidEmailException("유효하지 않은 이메일 형식입니다: $value")
        }
    }
    
    companion object {
        private val EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
        )
        
        /**
         * 이메일 형식 검증
         */
        fun isValid(email: String): Boolean {
            return email.isNotBlank() && EMAIL_PATTERN.matcher(email).matches()
        }
        
        /**
         * 이메일 Value Object 생성
         */
        fun of(email: String): Email = Email(email)
    }
}

/**
 * 비밀번호 Value Object
 */
data class Password private constructor(
    private val hashedValue: String,
    private val algorithm: String = "BCRYPT"
) {
    /**
     * 주어진 원문 비밀번호와 해시된 비밀번호가 일치하는지 검증
     */
    fun matches(rawPassword: String): Boolean {
        // 실제 구현에서는 BCrypt 등의 알고리즘으로 검증해야 함
        // 여기서는 간단하게 예시만 제공
        return when (algorithm) {
            "BCRYPT" -> {
                // BCryptPasswordEncoder().matches(rawPassword, hashedValue)
                rawPassword == hashedValue // 테스트용 임시 구현
            }
            else -> throw IllegalArgumentException("지원하지 않는 암호화 알고리즘입니다: $algorithm")
        }
    }
    
    companion object {
        private const val MIN_LENGTH = 8
        private const val MAX_LENGTH = 100
        
        /**
         * 비밀번호 규칙 검증
         */
        private fun validate(rawPassword: String) {
            if (rawPassword.length < MIN_LENGTH || rawPassword.length > MAX_LENGTH) {
                throw InvalidPasswordException("비밀번호는 $MIN_LENGTH 자 이상, $MAX_LENGTH 자 이하여야 합니다.")
            }
            
            // 최소 하나의 숫자 포함
            if (!rawPassword.any { it.isDigit() }) {
                throw InvalidPasswordException("비밀번호는 최소 하나의 숫자를 포함해야 합니다.")
            }
            
            // 최소 하나의 소문자 포함
            if (!rawPassword.any { it.isLowerCase() }) {
                throw InvalidPasswordException("비밀번호는 최소 하나의 소문자를 포함해야 합니다.")
            }
            
            // 최소 하나의 대문자 포함
            if (!rawPassword.any { it.isUpperCase() }) {
                throw InvalidPasswordException("비밀번호는 최소 하나의 대문자를 포함해야 합니다.")
            }
            
            // 최소 하나의 특수문자 포함
            if (!rawPassword.any { !it.isLetterOrDigit() }) {
                throw InvalidPasswordException("비밀번호는 최소 하나의 특수 문자를 포함해야 합니다.")
            }
        }
        
        /**
         * 비밀번호 해싱 (실제 구현은 BCrypt 등을 사용)
         */
        private fun hash(rawPassword: String): String {
            // 실제 구현에서는 BCrypt 등으로 암호화
            // return BCryptPasswordEncoder().encode(rawPassword)
            return rawPassword // 테스트용 임시 구현
        }
        
        /**
         * 새 비밀번호 생성
         */
        fun of(rawPassword: String): Password {
            validate(rawPassword)
            return Password(hash(rawPassword))
        }
        
        /**
         * 이미 해시된 비밀번호로 객체 생성 (Repository에서 데이터 로드 시 사용)
         */
        fun fromHashed(hashedPassword: String, algorithm: String = "BCRYPT"): Password {
            return Password(hashedPassword, algorithm)
        }
    }
}

/**
 * 사용자 상태 열거형
 */
enum class UserStatus {
    /**
     * 활성화 상태
     */
    ACTIVE,
    
    /**
     * 비활성화 상태
     */
    INACTIVE
}