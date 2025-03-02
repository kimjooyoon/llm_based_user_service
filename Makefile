.PHONY: clean build test lint security all run-idp run-resource docker-build docker-up docker-down

# Gradle wrapper에 대한 변수
GRADLE = ./gradlew
GRADLE_W = ./gradlew wrapper

# Docker 관련 변수
DOCKER_COMPOSE = docker-compose
DOCKER_BUILD = docker build

# 빌드 디렉토리 초기화
clean:
	$(GRADLE) clean

# 전체 빌드
build:
	$(GRADLE) build -x test

# 테스트 실행
test:
	$(GRADLE) test

# 코드 품질 검사 (ktlint, detekt)
lint:
	$(GRADLE) ktlintCheck
	$(GRADLE) detekt

# 보안 취약점 검사
security:
	$(GRADLE) dependencyCheckAnalyze

# 전체 검증 작업 실행
all:
	$(GRADLE) makeAll

# Gradle 래퍼 업데이트
update-wrapper:
	$(GRADLE_W) --gradle-version=7.6

# IDP 서버 실행
run-idp:
	$(GRADLE) :bootstrap-idp:bootRun

# Resource 서버 실행
run-resource:
	$(GRADLE) :bootstrap-resource:bootRun

# Docker 이미지 빌드
docker-build:
	$(DOCKER_BUILD) -t user-service/idp:latest -f ./bootstrap-idp/Dockerfile .
	$(DOCKER_BUILD) -t user-service/resource:latest -f ./bootstrap-resource/Dockerfile .

# Docker Compose로 전체 서비스 시작
docker-up:
	$(DOCKER_COMPOSE) up -d

# Docker Compose로 전체 서비스 중지
docker-down:
	$(DOCKER_COMPOSE) down

# 모듈 생성 도우미
create-module:
	@read -p "모듈 이름을 입력하세요: " module_name; \
	mkdir -p $$module_name/src/main/kotlin/com/github/kimjooyoon/userservice/$$module_name; \
	mkdir -p $$module_name/src/test/kotlin/com/github/kimjooyoon/userservice/$$module_name; \
	echo "plugins {\n    kotlin(\"jvm\")\n}\n\ndependencies {\n    implementation(project(\":core\"))\n}" > $$module_name/build.gradle.kts; \
	echo "새 모듈이 생성되었습니다: $$module_name"; \
	echo "settings.gradle.kts 파일에 다음 내용을 추가하세요:"; \
	echo "include(\"$$module_name\")"

# 기본 명령
help:
	@echo "사용 가능한 명령:"
	@echo "  make clean              - 빌드 디렉토리 초기화"
	@echo "  make build              - 전체 빌드"
	@echo "  make test               - 테스트 실행"
	@echo "  make lint               - 코드 품질 검사"
	@echo "  make security           - 보안 취약점 검사"
	@echo "  make all                - 전체 검증 작업 실행"
	@echo "  make update-wrapper     - Gradle 래퍼 업데이트"
	@echo "  make run-idp            - IDP 서버 실행"
	@echo "  make run-resource       - Resource 서버 실행"
	@echo "  make docker-build       - Docker 이미지 빌드"
	@echo "  make docker-up          - Docker Compose로 전체 서비스 시작"
	@echo "  make docker-down        - Docker Compose로 전체 서비스 중지"
	@echo "  make create-module      - 새 모듈 생성 도우미"
	@echo "  make help               - 이 도움말 표시"

# 기본 명령
default: help