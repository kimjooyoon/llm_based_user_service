# 사용자(User) 마이크로서비스

사용자 인증, 인가 및 관리를 위한 마이크로서비스입니다. 이 서비스는 Factor12 앱 원칙을 준수하고 DDD(도메인 주도 설계) 및 TDD(테스트 주도 개발) 방법론을 활용하여 개발되었습니다.

## 아키텍처 구조

본 프로젝트는 Spring Boot 기반의 멀티 모듈 구조를 가지고 있습니다:

```
user-service/
├── core/                  # 핵심 도메인 로직 (POJO)
│   ├── domain/            # 도메인 모델 (Aggregate Root, Entity, Value Object)
│   ├── usecase/           # 유스케이스 구현 (애플리케이션 서비스)
│   └── port/              # 포트 인터페이스 (입/출력 포트)
│
├── message/               # 메시지 처리 (이벤트 발행 및 구독)
│   ├── event/             # 도메인 이벤트 정의
│   ├── publisher/         # 이벤트 발행 구현체
│   └── consumer/          # 이벤트 소비 구현체
│
├── bootstrap-idp/         # ID 제공자 부트스트랩 (인증 서버)
│   ├── config/            # 인증 서버 구성
│   ├── controller/        # API 컨트롤러
│   ├── security/          # 보안 구성
│   └── service/           # 애플리케이션 서비스 구현
│
└── bootstrap-resource/    # 리소스 서버 부트스트랩
    ├── config/            # 리소스 서버 구성
    ├── controller/        # API 컨트롤러
    ├── security/          # 보안 구성
    └── service/           # 애플리케이션 서비스 구현
```

## 주요 기능

1. **회원 인증/인가**
   - 사용자 등록, 로그인, 로그아웃
   - 비밀번호 관리 (변경, 재설정)
   - 토큰 기반 인증 (JWT)
   - 소셜 로그인 지원 (OAuth2)

2. **타 마이크로서비스 도메인 인증/인가**
   - 서비스 간 인증 (Service-to-Service)
   - API 게이트웨이 통합
   - 보안 컨텍스트 전파

3. **서비스 도메인별 RBAC/ABAC 인증/인가**
   - 역할 기반 접근 제어 (RBAC)
   - 속성 기반 접근 제어 (ABAC)
   - 세분화된 권한 관리

4. **서비스 도메인별 메시지 발행**
   - 이벤트 주도 아키텍처 지원
   - 비동기 이벤트 발행
   - 멀티 채널 메시지 발행

## 기술 스택

- **언어**: Kotlin, Java
- **프레임워크**: Spring Boot, Spring Security, Spring Cloud
- **인증/인가**: OAuth2, JWT, Spring Security
- **이벤트 처리**: Kafka, Spring Cloud Stream
- **데이터베이스**: PostgreSQL, Redis (캐싱)
- **빌드 도구**: Gradle (멀티 모듈)
- **테스트**: JUnit 5, Mockito, RestAssured
- **문서화**: SpringDoc (OpenAPI)
- **모니터링**: Micrometer, Prometheus, Grafana
- **배포**: Docker, Kubernetes

## Bounded Context

본 서비스는 다음 Bounded Context로 구성됩니다:

1. **User Management Context**
   - 사용자 등록, 프로필 관리, 계정 상태 관리
   - Aggregate Root: User

2. **Authentication Context**
   - 사용자 인증, 토큰 관리, 세션 처리
   - Aggregate Root: Authentication

3. **Authorization Context**
   - 역할 및 권한 관리, 접근 제어
   - Aggregate Root: Role, Permission

4. **Service Authentication Context**
   - 서비스 간 인증, API 키 관리
   - Aggregate Root: ServiceClient

## Factor12 앱 준수 사항

1. **코드베이스**: 단일 코드베이스를 사용하며 Git으로 버전 관리
2. **의존성**: 명시적으로 선언된 의존성 관리 (Gradle)
3. **설정**: 환경 변수 기반 구성 (Spring Config)
4. **백엔드 서비스**: 데이터베이스 등을 연결 가능한 리소스로 취급
5. **빌드, 릴리즈, 실행**: 빌드와 실행 단계 엄격히 분리
6. **프로세스**: 무상태(Stateless) 프로세스로 실행
7. **포트 바인딩**: 자체 포트에 웹 서버 바인딩
8. **동시성**: 프로세스 모델을 통한 확장
9. **폐기 가능**: 빠른 시작과 정상적인 종료로 안정성 확보
10. **개발/운영 환경 일치**: 개발, 스테이징, 운영 환경의 차이 최소화
11. **로그**: 이벤트 스트림으로 로그 처리
12. **관리 프로세스**: 관리 작업을 일회성 프로세스로 실행

## 시작하기

### 필수 조건
- JDK 17 이상
- Gradle 7.x 이상
- Docker 및 Docker Compose

### 빌드
```bash
./gradlew clean build
```

### 테스트
```bash
./gradlew test
```

### 실행
```bash
./gradlew bootRun -p bootstrap-idp     # 인증 서버 실행
./gradlew bootRun -p bootstrap-resource # 리소스 서버 실행
```

## 개발 워크플로우

1. TDD 방식으로 개발 (자세한 내용은 [TDD.md](TDD.md) 참조)
2. 도메인 이벤트 기반 모델링 (자세한 내용은 [DOMAIN.md](DOMAIN.md) 참조)
3. Pull Request 기반 코드 리뷰
4. CI/CD 파이프라인을 통한 지속적 배포

## 기여하기

1. 저장소 포크 및 클론
2. 기능 브랜치 생성 (`git checkout -b feature/amazing-feature`)
3. 변경 사항 커밋 (`git commit -m 'Add some amazing feature'`)
4. 브랜치 푸시 (`git push origin feature/amazing-feature`)
5. Pull Request 제출

## 라이센스

[MIT License](LICENSE)