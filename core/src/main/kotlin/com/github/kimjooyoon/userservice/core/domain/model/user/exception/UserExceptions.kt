package com.github.kimjooyoon.userservice.core.domain.model.user.exception

/**
 * 사용자 도메인의 기본 예외 클래스
 */
abstract class UserDomainException(
    override val message: String,
    override val cause: Throwable? = null
) : RuntimeException(message, cause)

/**
 * 사용자를 찾을 수 없을 때 발생하는 예외
 */
class UserNotFoundException(
    message: String = "사용자를 찾을 수 없습니다.",
    userId: String? = null,
    email: String? = null
) : UserDomainException(
    when {
        userId != null -> "$message (ID: $userId)"
        email != null -> "$message (Email: $email)"
        else -> message
    }
)

/**
 * 이미 존재하는 사용자일 때 발생하는 예외
 */
class UserAlreadyExistsException(
    message: String = "이미 존재하는 사용자입니다.",
    email: String? = null
) : UserDomainException(
    if (email != null) "$message (Email: $email)" else message
)

/**
 * 비활성화된 사용자에 대한 작업 시 발생하는 예외
 */
class UserDeactivatedException(
    message: String = "비활성화된 사용자입니다.",
    userId: String? = null
) : UserDomainException(
    if (userId != null) "$message (ID: $userId)" else message
)

/**
 * 유효하지 않은 이메일 형식일 때 발생하는 예외
 */
class InvalidEmailException(
    message: String = "유효하지 않은 이메일 형식입니다."
) : UserDomainException(message)

/**
 * 유효하지 않은, 또는 일치하지 않는 비밀번호일 때 발생하는 예외
 */
class InvalidPasswordException(
    message: String = "유효하지 않은 비밀번호입니다."
) : UserDomainException(message)

/**
 * 권한이 없을 때 발생하는 예외
 */
class UserUnauthorizedException(
    message: String = "이 작업을 수행할 권한이 없습니다.",
    userId: String? = null,
    requiredPermission: String? = null
) : UserDomainException(
    when {
        userId != null && requiredPermission != null -> 
            "$message (User ID: $userId, Required Permission: $requiredPermission)"
        userId != null -> 
            "$message (User ID: $userId)"
        requiredPermission != null -> 
            "$message (Required Permission: $requiredPermission)"
        else -> message
    }
)