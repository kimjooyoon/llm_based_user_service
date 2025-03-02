package com.github.kimjooyoon.userservice.core.application.service

import com.github.kimjooyoon.userservice.core.application.dto.UserDto
import com.github.kimjooyoon.userservice.core.application.dto.UserRegistrationCommand
import com.github.kimjooyoon.userservice.core.application.dto.UserUpdateCommand
import com.github.kimjooyoon.userservice.core.domain.model.user.User
import com.github.kimjooyoon.userservice.core.domain.model.user.UserRepository
import com.github.kimjooyoon.userservice.core.domain.model.user.exception.UserAlreadyExistsException
import com.github.kimjooyoon.userservice.core.domain.model.user.exception.UserNotFoundException
import com.github.kimjooyoon.userservice.core.domain.model.user.vo.Email
import com.github.kimjooyoon.userservice.core.domain.model.user.vo.UserId
import com.github.kimjooyoon.userservice.message.EventPublisher

/**
 * 사용자 서비스 인터페이스
 * 
 * 사용자 관련 비즈니스 로직을 처리하는 애플리케이션 서비스 인터페이스입니다.
 */
interface UserService {
    /**
     * 사용자 등록
     *
     * @param command 사용자 등록 명령
     * @return 등록된 사용자 정보
     * @throws UserAlreadyExistsException 이미 존재하는 이메일인 경우
     */
    suspend fun registerUser(command: UserRegistrationCommand): UserDto
    
    /**
     * 사용자 정보 조회
     *
     * @param userId 사용자 ID
     * @return 사용자 정보
     * @throws UserNotFoundException 사용자를 찾을 수 없는 경우
     */
    suspend fun getUserById(userId: String): UserDto
    
    /**
     * 이메일로 사용자 정보 조회
     *
     * @param email 사용자 이메일
     * @return 사용자 정보
     * @throws UserNotFoundException 사용자를 찾을 수 없는 경우
     */
    suspend fun getUserByEmail(email: String): UserDto
    
    /**
     * 사용자 정보 업데이트
     *
     * @param userId 사용자 ID
     * @param command 사용자 정보 업데이트 명령
     * @return 업데이트된 사용자 정보
     * @throws UserNotFoundException 사용자를 찾을 수 없는 경우
     */
    suspend fun updateUser(userId: String, command: UserUpdateCommand): UserDto
    
    /**
     * 사용자 계정 활성화
     *
     * @param userId 사용자 ID
     * @return 활성화된 사용자 정보
     * @throws UserNotFoundException 사용자를 찾을 수 없는 경우
     */
    suspend fun activateUser(userId: String): UserDto
    
    /**
     * 사용자 계정 비활성화
     *
     * @param userId 사용자 ID
     * @return 비활성화된 사용자 정보
     * @throws UserNotFoundException 사용자를 찾을 수 없는 경우
     */
    suspend fun deactivateUser(userId: String): UserDto
    
    /**
     * 사용자 비밀번호 변경
     *
     * @param userId 사용자 ID
     * @param currentPassword 현재 비밀번호
     * @param newPassword 새 비밀번호
     * @throws UserNotFoundException 사용자를 찾을 수 없는 경우
     * @throws InvalidPasswordException 현재 비밀번호가 일치하지 않는 경우
     */
    suspend fun changeUserPassword(userId: String, currentPassword: String, newPassword: String)
}

/**
 * 사용자 서비스 구현체
 */
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val eventPublisher: EventPublisher
) : UserService {
    
    override suspend fun registerUser(command: UserRegistrationCommand): UserDto {
        val email = Email.of(command.email)
        
        if (userRepository.existsByEmail(email)) {
            throw UserAlreadyExistsException("이미 등록된 이메일입니다: ${command.email}")
        }
        
        val user = User.create(
            email = command.email,
            password = command.password,
            name = command.name,
            phoneNumber = command.phoneNumber
        )
        
        val savedUser = userRepository.save(user)
        
        // 도메인 이벤트 발행
        val events = savedUser.getDomainEvents()
        eventPublisher.publishAll(events)
        savedUser.clearDomainEvents()
        
        return UserDto.fromUser(savedUser)
    }
    
    override suspend fun getUserById(userId: String): UserDto {
        val user = userRepository.findById(UserId(userId))
            ?: throw UserNotFoundException("사용자를 찾을 수 없습니다", userId)
        
        return UserDto.fromUser(user)
    }
    
    override suspend fun getUserByEmail(email: String): UserDto {
        val user = userRepository.findByEmail(Email.of(email))
            ?: throw UserNotFoundException("사용자를 찾을 수 없습니다", null, email)
        
        return UserDto.fromUser(user)
    }
    
    override suspend fun updateUser(userId: String, command: UserUpdateCommand): UserDto {
        val user = userRepository.findById(UserId(userId))
            ?: throw UserNotFoundException("사용자를 찾을 수 없습니다", userId)
        
        val updatedUser = user.updateProfile(command.name, command.phoneNumber)
        val savedUser = userRepository.save(updatedUser)
        
        // 도메인 이벤트 발행
        val events = savedUser.getDomainEvents()
        eventPublisher.publishAll(events)
        savedUser.clearDomainEvents()
        
        return UserDto.fromUser(savedUser)
    }
    
    override suspend fun activateUser(userId: String): UserDto {
        val user = userRepository.findById(UserId(userId))
            ?: throw UserNotFoundException("사용자를 찾을 수 없습니다", userId)
        
        val activatedUser = user.activate()
        val savedUser = userRepository.save(activatedUser)
        
        // 도메인 이벤트 발행
        val events = savedUser.getDomainEvents()
        eventPublisher.publishAll(events)
        savedUser.clearDomainEvents()
        
        return UserDto.fromUser(savedUser)
    }
    
    override suspend fun deactivateUser(userId: String): UserDto {
        val user = userRepository.findById(UserId(userId))
            ?: throw UserNotFoundException("사용자를 찾을 수 없습니다", userId)
        
        val deactivatedUser = user.deactivate()
        val savedUser = userRepository.save(deactivatedUser)
        
        // 도메인 이벤트 발행
        val events = savedUser.getDomainEvents()
        eventPublisher.publishAll(events)
        savedUser.clearDomainEvents()
        
        return UserDto.fromUser(savedUser)
    }
    
    override suspend fun changeUserPassword(userId: String, currentPassword: String, newPassword: String) {
        val user = userRepository.findById(UserId(userId))
            ?: throw UserNotFoundException("사용자를 찾을 수 없습니다", userId)
        
        val updatedUser = user.changePassword(currentPassword, newPassword)
        val savedUser = userRepository.save(updatedUser)
        
        // 도메인 이벤트 발행
        val events = savedUser.getDomainEvents()
        eventPublisher.publishAll(events)
        savedUser.clearDomainEvents()
    }
}