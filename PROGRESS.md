# 프로젝트 진행 상황

## 현재까지 구현된 내용

### 프로젝트 설정
- 멀티 모듈 Gradle 프로젝트 구조 설정
- 개발 환경 설정 및 Makefile 구성
- 코어 의존성 설정

### 도메인 모델 구현
- 도메인 이벤트 인터페이스 정의
- 애그리게이트 루트 추상 클래스 구현

#### 사용자 도메인
- User 애그리게이트 루트 구현
- UserProfile 엔티티 구현
- 사용자 관련 값 객체(Value Object) 구현
- 사용자 도메인 이벤트 정의
- 사용자 예외 클래스 정의
- UserRepository 인터페이스 정의

#### 인증 도메인
- Authentication 애그리게이트 루트 구현
- 인증 관련 값 객체 구현
- 인증 도메인 이벤트 정의
- AuthenticationRepository 인터페이스 정의

#### 권한 도메인
- Role 애그리게이트 루트 구현
- Permission 애그리게이트 루트 구현
- 권한 관련 값 객체 구현
- 권한 도메인 이벤트 정의
- RoleRepository, PermissionRepository 인터페이스 정의

### 애플리케이션 서비스 구현
- UserService 인터페이스 및 구현체 구현
- AuthenticationService 인터페이스 및 구현체 구현
- RoleService 인터페이스 및 구현체 구현
- 서비스 관련 DTO 클래스 구현

### 기타
- 이벤트 발행자 인터페이스 정의
- 공통 유틸리티 클래스 구현
- 문서화 (README.md, DOMAIN.md, TDD.md)

## 다음 단계 구현 계획
다음 단계는 인프라 레이어와 웹 인터페이스 구현이 필요합니다. 세부 계획은 [이슈 #1](https://github.com/kimjooyoon/llm_based_user_service/issues/1)을 참조하세요.

### 인프라 레이어
- JPA 엔티티 클래스 구현
- JPA 리포지토리 구현
- 이벤트 발행 구현 (Kafka)

### 웹 인터페이스
- REST API 컨트롤러 구현
- Spring Security 설정
- 예외 처리 및 응답 포맷 표준화

### 테스트
- 단위 테스트 구현
- 통합 테스트 구현

### 배포
- Docker 컨테이너화
- 환경별 설정 구성