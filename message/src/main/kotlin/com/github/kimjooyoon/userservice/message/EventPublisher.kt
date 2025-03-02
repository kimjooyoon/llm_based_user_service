package com.github.kimjooyoon.userservice.message

import com.github.kimjooyoon.userservice.core.domain.model.event.DomainEvent

/**
 * 도메인 이벤트 발행자 인터페이스
 * 
 * 모든 도메인 이벤트 발행 구현체는 이 인터페이스를 구현해야 합니다.
 */
interface EventPublisher {
    /**
     * 단일 도메인 이벤트 발행
     *
     * @param event 발행할 도메인 이벤트
     */
    suspend fun publish(event: DomainEvent)
    
    /**
     * 여러 도메인 이벤트 발행
     *
     * @param events 발행할 도메인 이벤트 목록
     */
    suspend fun publishAll(events: Collection<DomainEvent>)
}