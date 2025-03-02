package com.github.kimjooyoon.userservice.core.domain.model

import com.github.kimjooyoon.userservice.core.domain.model.event.DomainEvent
import java.util.Collections

/**
 * Aggregate Root 추상 클래스
 *
 * 도메인 주도 설계(DDD)에서 Aggregate Root 역할을 하는 추상 클래스.
 * 모든 Aggregate Root 엔티티는 이 클래스를 상속받아 구현합니다.
 * 도메인 이벤트 발행 기능을 포함하고 있습니다.
 *
 * @param ID Aggregate Root의 식별자 타입
 * @property id Aggregate Root의 고유 식별자
 */
abstract class AggregateRoot<ID>(
    protected val id: ID
) {
    private val domainEvents: MutableList<DomainEvent> = mutableListOf()
    
    /**
     * 도메인 이벤트 등록
     *
     * @param event 등록할 도메인 이벤트
     */
    protected fun registerEvent(event: DomainEvent) {
        domainEvents.add(event)
    }
    
    /**
     * 발행되지 않은 도메인 이벤트 조회
     *
     * @return 도메인 이벤트 리스트 (읽기 전용)
     */
    fun getDomainEvents(): List<DomainEvent> {
        return Collections.unmodifiableList(domainEvents)
    }
    
    /**
     * 도메인 이벤트 초기화
     * 이벤트가 처리된 후 호출하여 이벤트 목록을 비웁니다.
     */
    fun clearDomainEvents() {
        domainEvents.clear()
    }
}