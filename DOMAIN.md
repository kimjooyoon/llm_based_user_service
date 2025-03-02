# 사용자(User) 마이크로서비스 도메인 이벤트

## 도메인 이벤트 목록

### 회원 관련 이벤트
- `UserRegistered`: 사용자 등록 완료
- `UserActivated`: 사용자 계정 활성화
- `UserDeactivated`: 사용자 계정 비활성화
- `UserDeleted`: 사용자 계정 삭제
- `UserProfileUpdated`: 사용자 프로필 정보 업데이트
- `UserPasswordChanged`: 사용자 비밀번호 변경
- `UserPasswordResetRequested`: 사용자 비밀번호 재설정 요청
- `UserPasswordResetCompleted`: 사용자 비밀번호 재설정 완료
- `UserLoginSucceeded`: 사용자 로그인 성공
- `UserLoginFailed`: 사용자 로그인 실패
- `UserLoggedOut`: 사용자 로그아웃

### 권한 관련 이벤트
- `RoleAssignedToUser`: 사용자에게 역할 할당
- `RoleRemovedFromUser`: 사용자로부터 역할 제거
- `PermissionGranted`: 권한 부여
- `PermissionRevoked`: 권한 철회
- `ResourceAccessRequested`: 리소스 접근 요청
- `ResourceAccessGranted`: 리소스 접근 허용
- `ResourceAccessDenied`: 리소스 접근 거부

### 인증/인가 관련 이벤트
- `TokenIssued`: 토큰 발급
- `TokenRefreshed`: 토큰 갱신
- `TokenRevoked`: 토큰 폐기
- `TokenValidated`: 토큰 검증
- `ServiceAuthenticationRequested`: 서비스 인증 요청
- `ServiceAuthenticationSucceeded`: 서비스 인증 성공
- `ServiceAuthenticationFailed`: 서비스 인증 실패

### 서비스 간 통신 이벤트
- `UserDataRequested`: 사용자 데이터 요청
- `UserDataProvided`: 사용자 데이터 제공
- `UserServiceHealthCheck`: 사용자 서비스 상태 확인
- `ExternalServiceCallInitiated`: 외부 서비스 호출 시작
- `ExternalServiceCallCompleted`: 외부 서비스 호출 완료
- `ExternalServiceCallFailed`: 외부 서비스 호출 실패

## 핵심 Aggregate

### User (사용자) Aggregate
- **Root Entity**: User
- **Entities**: UserProfile, UserPreference
- **Value Objects**: Email, Password, Name, PhoneNumber
- **Repository**: UserRepository

### Authentication (인증) Aggregate
- **Root Entity**: Authentication
- **Entities**: Token, Session
- **Value Objects**: TokenValue, ExpirationTime
- **Repository**: AuthenticationRepository

### Authorization (권한) Aggregate
- **Root Entity**: Role
- **Entities**: Permission
- **Value Objects**: ResourceType, Action
- **Repository**: RoleRepository, PermissionRepository

### ServiceAuthentication (서비스 인증) Aggregate
- **Root Entity**: ServiceClient
- **Entities**: ServiceToken
- **Value Objects**: ClientId, ClientSecret
- **Repository**: ServiceClientRepository