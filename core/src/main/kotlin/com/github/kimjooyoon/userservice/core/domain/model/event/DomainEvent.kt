package com.github.kimjooyoon.userservice.core.domain.model.event

import java.time.LocalDateTime
import java.util.UUID

/**
 * 도메인 이벤트 인터페이스
 *
 * 모든 도메인 이벤트는 이 인터페이스를 구현해야 합니다.
 */
interface DomainEvent {
    /**
     * 이벤트의 고유 식별자
     */
    val eventId: String
    
    /**
     * 이벤트가 발생한 시간
     */
    val occurredAt: LocalDateTime
    
    /**
     * 이벤트 타입
     */
    val eventType: String
}