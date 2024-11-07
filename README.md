# 아웃소싱 팀프로젝트 - 배달 어플리케이션

------------------------------------------------------------------------
![Java](https://img.shields.io/badge/Java-17-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.4-brightgreen)
![MySQL](https://img.shields.io/badge/MySQL-8.0-lightblue)
![RestAPI](https://img.shields.io/badge/RestAPI-red)
![JPA](https://img.shields.io/badge/JPA-orange)
![JWT](https://img.shields.io/badge/JWT-blue)



## <span style="color: pink"> API 명세서
- ### [API 명세서](https://www.notion.so/teamsparta/12-12a2dc3ef5148094aa4bc1d46427d84a#12a2dc3ef51481edae09d1c02c9d635b)

## <span style="color: pink">  ERD 설계
![image (1)](https://github.com/user-attachments/assets/3cfe94e6-b215-4bd8-98b7-d2ef517e3833)

## <span style="color: pink"> 개요 (Overview)
이 배달 애플리케이션은 사용자가 음식점을 검색하고 메뉴를 선택하여 주문할 수 있는 기능을 제공하며, 실시간으로 배달 상태를 추적할 수 있습니다. 주요 기능으로는 음식점 및 메뉴 조회, 장바구니 기능, 주문 및 결제, 리뷰 작성 및 관리 등이 포함되어 있습니다.

이 애플리케이션은 JWT 토큰 기반 인증을 통해 회원가입과 로그인을 지원하며, 인증된 사용자만이 주문 및 리뷰 작성, 즐겨찾기 등록 등의 기능을 이용할 수 있습니다. 또한 관리자 권한이 있는 사용자는 음식점 정보와 메뉴를 등록, 수정, 삭제할 수 있으며, 주문 상태를 관리하고 광고를 활성화하는 등의 추가 권한을 갖습니다.

사용자는 개인 계정으로 로그인하여 주문 내역을 조회하고, 즐겨찾기에 등록한 음식점을 쉽게 접근할 수 있습니다. 각 음식점에 대해 광고 기능이 적용되어, 활성화된 광고는 음식점 검색 시 최상단에 노출되며, 관리자에 의해 광고 활성화/비활성화 및 삭제가 가능합니다.

또한 리뷰 기능이 제공되어 사용자는 배달 완료 후 음식점과 메뉴에 대한 피드백을 남길 수 있으며, 사장님은 이에 대한 답변을 달 수 있어 고객과의 소통이 가능합니다.
<br><br>
## <span style="color: pink"> 주요 기능 (Key Features)
### 1. 사용자 관리
- 회원가입/로그인: 이메일과 소셜 로그인 지원.
- 프로필 관리: 사용자 정보 수정 및 탈퇴 기능.
### 2. 음식점 및 메뉴
- 음식점 검색 및 조회: 위치 기반으로 음식점을 검색하고 상세 정보를 제공.
- 메뉴 조회 및 옵션 선택: 메뉴 옵션 선택, 수량 지정, 설명 확인.
### 3. 주문 및 결제
- 주문 생성: 결제 및 주문 완료 후, 사용자에게 알림.
- 주문 상태 조회: 주문 준비 중, 배달 중, 배달 완료 등 단계별 상태 확인.
### 4. 배달 상태 추적
- 실시간 배달 추적: 주문 후 배달 진행 상황을 실시간으로 추적.
### 5. 리뷰 관리
- 리뷰 작성: 배달 완료 후 음식점과 메뉴에 대한 리뷰 작성 가능.
- 리뷰 관리: 사장님 답변 기능 포함하여 리뷰에 대한 관리 및 수정 가능.
### 6. 광고 관리
- 광고 등록 및 관리: 사장님이 음식점의 광고를 등록하고 관리.
- 광고 상태: 광고 활성화 및 비활성화 설정.
- 광고 노출: 활성화된 광고는 사용자 검색 시 상단에 노출.
### 7. 즐겨찾기
- 음식점 즐겨찾기 등록/삭제: 자주 방문하는 음식점을 즐겨찾기로 등록하고 삭제 가능.
### 8. 관리자 기능
- 음식점 및 메뉴 관리: 사장님이 음식점 및 메뉴 정보 등록, 수정, 삭제 가능.
- 주문 관리: 사장님이 주문을 접수하고, 배달 상태 업데이트 가능.
  <br><br>
## <span style="color: pink"> 기술 스택 (Tech Stack)
- #### **Java**: `Java 17`
- #### **Backend**: `Spring Boot`
- #### **Database**: `MySQL`
- #### **ORM** : `JPA`
- #### **Build Tool**: `Gradle`
- #### **Security** : `JWT`
<br><br>
## <span style="color: pink"> 개발자 가이드 (Developer Guide)
### 1. 환경 변수 설정
- MySQL 데이터베이스 연결을 위한 환경 변수를 설정해야 합니다. `application.properties` 파일에 다음 설정을 추가합니다 <br>
```properties
spring.application.name=todo
spring.datasource.url=jdbc:mysql://localhost:3307/deliverybackend
spring.datasource.username=root
spring.datasource.password={password}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver


spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.database=mysql
spring.jpa.properties.hibernate.default_batch_fetch_size = 10

# jwt
jwt.token.secret.key={tokenKey}
jwt.token.expires.in=1800000

spring.config.import=optional:application-oauth.properties
```
- s3 config 설정을 위한 환경 변수를 설정해야 합니다. `application.yml` 파일에 다음 설정을 추가합니다 <br>
```yml
spring:
  servlet:
    multipart:
      max-file-size: 50MB
      max-request-size: 50MB
cloud:
  aws:
    s3:
      bucket: mydeliveryimage
    region:
      static: ap-northeast-1
      auto: false
    stack:
      auto: false
    credentials:
      access-key: {accessKey}
      secret-key: {secretKey}
```
- oauth 추가 설정을 위한 환경 변수를 설정해야 합니다. `application-oauth.properties` 파일에 다음 설정을 추가합니다. <br>
```properties
# Github
oauth2.user.github.client-id={id}
oauth2.user.github.client-secret={secret}
oauth2.user.github.redirect-uri={oauthUrl}
# Kakao
oauth2.user.kakao.client-id={id}
oauth2.user.kakao.client-secret={secret}
oauth2.user.kakao.redirect-uri={oauthUrl}
# Naver
oauth2.user.naver.client-id={id}
oauth2.user.naver.client-secret={secret}
oauth2.user.naver.redirect-uri={oauthUrl}
# Google
oauth2.user.google.client-id={id}
oauth2.user.google.client-secret={secret}
oauth2.user.google.redirect-uri={oauthUrl}

# Provider
# Github
oauth2.provider.github.token-uri=https://github.com/login/oauth/access_token
oauth2.provider.github.user-info-uri=https://api.github.com/user
# Kakao
oauth2.provider.kakao.token-uri=https://kauth.kakao.com/oauth/token
oauth2.provider.kakao.user-info-uri=https://kapi.kakao.com/v2/user/me
# Naver
oauth2.provider.naver.token-uri=https://nid.naver.com/oauth2.0/token
oauth2.provider.naver.user-info-uri=https://openapi.naver.com/v1/nid/me
# Google
oauth2.provider.google.token-uri=https://oauth2.googleapis.com/token
oauth2.provider.google.user-info-uri=https://www.googleapis.com/userinfo/v2/me
```

### 2. Gradle 의존성 추가 ( Dependency Injection )
- `build.gradle` 파일에 필요한 라이브러리를 의존성 주입을 합니다.<br>
```build.gradle
dependencies {
    //s3
    implementation 'org.springframework.cloud:spring-cloud-starter-aws:2.2.1.RELEASE'
    // JWT
    implementation group: 'io.jsonwebtoken', name: 'jjwt-api', version: '0.11.5'
    runtimeOnly group: 'io.jsonwebtoken', name: 'jjwt-impl', version: '0.11.5'
    runtimeOnly group: 'io.jsonwebtoken', name: 'jjwt-jackson', version: '0.11.5'
    // Jackson core
    implementation 'com.fasterxml.jackson.core:jackson-databind:2.15.2'
    implementation 'com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.15.2'
    // validation
    implementation 'org.springframework.boot:spring-boot-starter-validation'
    // bcrypt
    implementation 'at.favre.lib:bcrypt:0.10.2'
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    implementation 'org.springframework.boot:spring-boot-starter-web'
    compileOnly 'org.projectlombok:lombok'
    runtimeOnly 'com.mysql:mysql-connector-j'
    annotationProcessor 'org.projectlombok:lombok'
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
    testRuntimeOnly 'org.junit.platform:junit-platform-launcher'

    implementation 'commons-io:commons-io:2.11.0'
}

```
