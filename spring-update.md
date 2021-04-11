## Spring boot Update

##### 1. 스프링 부트 2.1 변경내역 소개

주요 변경 내역

- 자바 11 지원
- 스프링 데이터 JPA, lazy 모드 지원
- 의존성 변경
- 빈 오버라이딩을 기본으로 허용하지 않도록 변경
- Acuator에 "/info"와 "/health" 공개하도록 바뀜
- 프로퍼티 변경
- 로깅 그룹



##### 2. 의존성 변경

의존성 변경

- 스프링 프레임워크 5.0 -> 스프렝 프레임워크 버전 5.1
  - 로거 설정 개선 spring-jcl
  - 컴포넌트 스캐닝 성능 개선이 가능한 "컴포넌트 인덱스" 기능 제공
  - 함수형 프로그래밍 스타일 지원
  - 코틀린 지원
  - 리액티브 프로그래밍 모델 지원
  - JUnit 5 지원
- Unit 4.12 -> JUnit 5.2
  - Jupiter
  - Extension ahepf
  - 람다 지원
  - 더 자바, 애플리케이션을 테스트하는 다양한 방법
- 톰캣 8.5.39 -> 톰캣 9
  - BIO 커넥터 사라지고 NIO 커텍터 기본으로 사용
  - HTTP/2 지원
  - 웹소켓 2.0 지원
  - 서블릿 4.0 / JSP 2.4 지원
- 하이버네이트 5.2 -> 하이버네이트 5.3
  - JPA 2.2 지원
  - Java 8의 Date와 Time API 지원



##### Spring-jcl 등장

```java
// 기존 복잡했던 로그 설정
SLF4J 설정
  - 필요없는 의존성 excluesive(spring-context=>commons-logging)
  - jcl-over-slf4j 	dependency
  - slf4j-api 			dependency
  - slf4j-log4j12 	dependency
  - log4j 					dependency

  
  /** JCL-over-SLF4J 대체제
	- 클래스 패스에 Log4J2가 있다면 JCL을 사용한 코드가 Log4j2를 사용한다.
	- 클래스패스에 SLF4J가 있다면 JCL을 사용한 코드가 SLF4J를 사용한다.
*/

```



##### 로거 변경하기

```java
// 스프링 부트 2.x 로깅 시스템 Log4J2로 변경하기
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter</artifactId>
      <exclusions>
      // 1. spring-boot-starter 에서 logging 제거
        <exclusion>
          <groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot-starter-logging</artifactId>
        </exclusion>
      </exclusions>
    </dependency>

      // 2. spring-boot-starter-log4j2 추가
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-log4j2</artifactId>
    </dependency>
```



##### log4j2.xml 작성 방법



