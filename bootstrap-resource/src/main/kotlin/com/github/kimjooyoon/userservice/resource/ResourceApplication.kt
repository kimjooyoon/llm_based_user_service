package com.github.kimjooyoon.userservice.resource

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

/**
 * 리소스 서버 애플리케이션 메인 클래스
 * 
 * 사용자 리소스 및 API를 제공하는 서버 애플리케이션의 진입점입니다.
 */
@SpringBootApplication
class ResourceApplication

/**
 * 애플리케이션 시작 함수
 */
fun main(args: Array<String>) {
    runApplication<ResourceApplication>(*args)
}