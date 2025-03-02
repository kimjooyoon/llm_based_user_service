package com.github.kimjooyoon.userservice.idp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

/**
 * ID 제공자(IDP) 서버 애플리케이션 메인 클래스
 * 
 * 인증 관련 기능을 제공하는 서버 애플리케이션의 진입점입니다.
 */
@SpringBootApplication
class IdpApplication

/**
 * 애플리케이션 시작 함수
 */
fun main(args: Array<String>) {
    runApplication<IdpApplication>(*args)
}