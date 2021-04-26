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

로깅퍼사드와 로거 차이점

> 로깅퍼사드가 로거를 골라서 사용하는 것
>
> 스프링은 로깅퍼사드를 사용함. 원하는 로거를 사용할 수 있도록 하기 위해서. 프레임워크이기 때문.



JCL은 이제 안쓰이다시피 하고, 스프링은 보통 SLF4J를 사용한다.

보통은 Log4j2 or Logback 둘 중 하나를 사용하지만

- 멀티 스레드 환경에서 Log4j2가 성능이 우수함, 가장 최신 기술임



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



<hr/>

### 3. 빈 오버라이딩 기본 설정 변경

(스프링 프레임워크가 아니라) 스프링 부트는 빈 등록 과정이 두 개.

1. 애플리케이션에 정의한 빈 등록
2. 자동설정이 제공하는 빈 등록



이때 1번에서 정의한 빈을 2번 과정에 등록하는 빈이 재정의(overriding) 할 수도 있는데, 스프링 부트 2.1 이전까지는 그런 기능을 기본으로 허용했지만 2.1 이후부터는 허용하지 않는다.



자동 설정으로 등록하는 빈이 오버라이딩을 시도한 경우 애플리케이션 구동을 멈춤.



#### 스프링 부트 2.1 이후 버전에서는 자동 설정으로 등록하는 빈 오버라이딩 안됨.

##### - Bean 설정 파일에서 @ConditionalOnMissingBean 어노테이션을 사용하면 해당 Bean이 없을 때에만 설정하도록 할 수 있다.



### 4. 자동 설정 지원

스프링 부트 2.1부터 지원하는 자동 설정



- 태스크 실행

@EnableAsync 사용 시 자동 설정 적용(TaskExecutionAutoConfiguration)

spring.task.execution 프로퍼티 제공

TaskExecutorBuilder 제공



- 태스크 스케줄링

@EnableScheduling 사용 시 자동 설정 적용(TaskSchedulingAutoConfiguration)

spring.task.scheduling 프로퍼티 제공

TaskSchedulerBuilder 제공



### 5. 프로퍼티 변경

스프링 부트 2.1에 변경된 프로퍼티 또는 새로 생긴 프로퍼티



- 스프링 데이터 JPA 부트스트랩 모드 지원

애플리케이션 구동 시간을 줄이기 위해 스프링 데이터 JPA 리파지토리 생성을 지연시키는 설정

spring.data.jpa.repositories.bootstrap-mode=deffered



>  **DEFERRED** : 애플리케이션 구동 이후에 리파지토리 인스턴스를 만들어 주입해준다.
>
> LAZY : 애플리케이션 구동 이후에도 만들지 않다가 처음으로 사용할 시점에 만들어 주입해준다.



```java
spring.data.jpa.repositories.bootstrap-mode=deferred
```



### 6. JUnit5

```java
<dependency>
	<groupId>org.junit.jupiter</groupId>
	<artifactId>junit-jupiter-api</artifactId>
	<scop>test</scope>
</dependency>
```



```java
@SpringBootTest
@AutoConfigureMockMvc
class HelloControllerTest{
  
  @Autowired
  MockMvc mockMvc;
  
  @Test
  void testHello() throws Exception {
    mockMvc.perform(get("/hello"))
      .andExpect(status().isOk())
      .andExpect(content().string("hello"));
    
    /* Assertions.assertAll(
    () -> resultActions.andExpect(status().isOk())
    );	*/
  }
}
```



### 7. DataSize

















