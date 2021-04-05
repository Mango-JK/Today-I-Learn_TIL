# 스프링 부트 개념과 활용

## 스프링부트 프로젝트 시작

##### `Maven Project로 실행`

```java
<parent>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-parent</artifactId>
  <version>2.4.2</version>
</parent>

<dependencies>
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
  </dependency>
</dependencies>

<build>
  <plugins>
    <plugin>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-maven-plugin</artifactId>
      <version>2.4.2</version>
    </plugin>
  </plugins>
</build>
```



## 스프링 부트 원리

#### `spring-boot-starter-web를 사용하는 이유?`

> **Spring-boot-dependencies** 안에는 각각의 dependencies들의 버전들이 **dependencies Management** 영역에 명시되어 있음. (부모의 부모, 최상위)
>
> -> 따라서 spring-boot-dependencies에 포함되어 있는 의존성들은 버전을 명시하지 않아도 되지만, 그 이외에는 명시해야 함.
>
> 
>
> **Spring-boot-starter-parent**(부모)에서 spring-boot-dependencies에 의존하고 있음.
>
> 
>
> 따라서 우리는 `spring-boot-starter-web` 을 **pom.xml** or **build.gradle** 에서 포함시켜 스프링 의존성 관리를 할 수 있게 된다.
>
> 



추가적으로 dependency를 추가하고자 할 경우에는 `mvnrepository.com` 에서 검색하여 추가해주면 된다.



#### 의존성 관리 이해

**springboot-dependency** 라는 최상위 dependency에 의존성들이 다 정의되어 있다.

- 따라서 spring-boot-starter-parent를 추가하면, springboot-dependency 를 통해 의존성 관리가 이루어진다.

> 내가 원하는 버전으로 사용하고 싶다면, pom.xml에서 <properties></properties> 안에 버전을 명시해주면 된다.



#### 자동 설정 이해

`메인 application에서 @SpringBootApplication 어노테이션만으로 잘 실행되는 이유는 다음 3가지 어노테이션 설정을 갖고 있기 때문이다.`

- **@SpringBootConfiguration**

  - 

- **@ComponentScan**

  해당 위치에서 아래의 어노테이션들을 찾아서 Bean으로 등록해준다.

  - @Component
  - @Configuration, @Repository, @Service, @Controller, @RestController

- **@EnableAutoConfiguration**

  - 스프링에서 빈은 사실 두 단계로 나눠서 읽힘.
  - **Component Scan**으로 Bean 등록 -> **EnableAutoConfiguration**으로 Bean 등록 2단계가 이루어짐
  - 따라서, 웹 어플리케이션을 사용하지 않으려면 다음과 같이 하면 된다.

```java
@Configuration
@ComponentScan
public class Application {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(Application.class);
        application.setWebApplicationType(WebApplicationType.NONE);
        application.run(args);
    }
}
```



#### 자동설정 만들기

1. Class 작성
2. @Configuration 파일 작성
3. src/main/resource/META-INF에 spring.factories 파일 만들기
4. Spring.factories 안에 자동 설정 파일 추가
5. mvn install



#### 자동설정 만들기(2)

- 덮어쓰기 방지하기
  - @ConditionalOnMissingBean



- 빈 재정의 수고 덜기
  - @ConfigurationProperties("holeman")
  - @EnableConfigurationProperties(HolemanProperties)
  - 프로퍼티 키값 자동 완성



```java
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-configuration-processor</artifactId>
	<optional>true</optional>
</dependency>
```

 

<hr/>

#### 내장 서블릿 컨테이너

- **스프링 부트는 서버가 아니다.**
  - 톰캣 객체 생성
  - 포트 설정
  - 톰캣에 컨텍스트 추가
  - 서블릿 만들기
  - 톰캣에 서블릿 추가
  - 컨텍스트에 서블릿 맵핑
  - 톰캣 실행 및 대기
- 이 모든 과정을 보다 상세히 또 유연하게 설정하고 실행해주는게 바로 스프링 부트의 자동 설정.
  - **ServletWebServerFactoryAutoConfiguration(서블릿 웹 서버 생성)**
    - TomcatServletWebServerFactoryCustomizer (서버 커스터마이징)
  - **DispatcherServletAutoConfiguration**
    - 서블릿 만들고 등록



```java
// None Web Application 으로 실행하기
// application.properties
spring.main.web-application-type=none
```



#### HTTPS와 HTTP2



- HTTPS 설정하기
  - 키스토어 만들기
  - HTTP는 못쓰네?
- HTTP2 설정
  - server.http2.enable
  - 사용하는 서블릿 컨테이너 마다 다름.
- HTTP 커넥터는 코딩으로 설정하기



```java
// in Terminal
keytool -genkey -alias spring -storetype PKCS12 -keyalg RSA -keysize 2048 -keystore keystore.p12 -validit 4000
  
// in application.properties
server.ssl.key-store= keystore.p12
server.ssl.key-store-type= PKCS12
server.ssl.key-store-password= 123456
server.ssl.key-alias= spring
/** lets encrypt... 처럼 공인된 인증서가 아니기 때문에 https 접속 시 빨간색으로 뜬다. **/
  

// in Terminal
curl -I -k --http2 https://localhost:8080/hello

/** Http와 Https 사용하기 위해 커넥터 추가하기 **/
// in main
    @Bean
    public ServletWebServerFactory serverFactory() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
        tomcat.addAdditionalTomcatConnectors(createStandardConnector());
        return tomcat;
    }

    private Connector createStandardConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setPort(8080);
        return connector;
    }
```



<hr/>

## 스프링 부트 활용

#### Banner 사용

```java
// banner.txt 사용, in resources
========================
 Spring boot banner Test
========================
  
// banner를 끄는 방법
        SpringApplication app = new SpringApplication(Demospringboot51Application.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);


// 시작 시 이벤트 리스너 작동
@Component
public class SampleListener implements ApplicationListener<ApplicationStartedEvent> {

    @Override
    public void onApplicationEvent(ApplicationStartedEvent applicationStartedEvent) {
        System.out.println("======================");
        System.out.println("Application is Starting");
        System.out.println("======================");
    }
}
```



#### 외부 설정

사용할 수 있는 외부 설정

- properties
- YAML
- 환경 변수
- 커맨드 라인 아규먼트



```java
// 외부 설정을 빈으로 등록해서 다른 빈에 등록할 수 있다.
@Component
@ConfigurationProperties("keesun")
public class KeesunProperties {
	private String name;
	private int age;
	private String fullName;
	
	...
}

// in main
@Autowired
KeesunProperties keesunProperties;

System.out.println(keesunProperties.getName());
...
  
```



#### 프로파일

##### @Profile 애노테이션은 어디에?

- @Configuration
- Component

##### 어떤 프로파일을 활성화 할 것인가?

- spring.profiles.active

##### 어떤 프로파일을 추가할 것인가?

- spring.profiles.include

##### 프로파일용 프로퍼티

- application-{profile}.properties



```java
// 이 Bean은 Profile이 "test"일 때 사용이 됨.
@Profile("test")
@Configuration
public class TestConfiguration {
    @Bean
    public String hello() {
        return "Hello Test";
    }
}

// in application.properties
spring.profiles.active=test
  
// 실행 시
  java -jar spring.jar --spring.profiles.active=prod
  
// 또는
  application-prod.properties 설정 파일을 생성함
  
// 결과 확인
    @Autowired
    private String hello;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("=======================");
        System.out.println(hello);
        System.out.println("=======================");
    }
```



<hr/>

#### 로깅



**<u>로깅 퍼사드</u> 장점: framework에서 사용하는 모든 애플리케이션이 사용하고 싶은 로거를 바꿔가며 사용할 수 있다.**



로깅 퍼사드 VS 로거

- Commons Logging, Slf4j
  - Logger를 추상화해놓은 것
- JUL, Log4j2, Logback



```java
// in application.properties
spring.output.ansi.enabled=always // log color로 보기
logging.path:logs	// log 디렉토리에 spring.log 파일로 로그 저장
logging.level.me.whiteship.springinit=DEBUG	// log 디버그 모드로 보기

// in code
private Logger logger = new LoggerFactory.getLogger(SampleRunner.class);
logger.info("hello");
```



##### 커스텀 로그 설정 파일 사용하기

- Logback : logback-spring.xml
- Log4J2 : log4j2-spring.xml



##### 로거를 Log4j2로 변경하기

- pom.xml "spring-boot-starter" 에서 exclude를 통해 spring-boot-starter-logging 제거
- <artifactId>spring-boot-starter-log4j2</artifactId> 추가



<hr/>

#### 테스트

시작은 일단 spring-boot-test를 추가하는 것부터

```java
// in pom.xml
spring-boot-starter-test 추가
```



##### @SpringBootTest

- 빈 설정 파일은 설정을 안해주나? 
  - 알아서 찾는다.(@SpringBootApplication)
- webEnvironment
  - MOCK : mock servlet environment. 내장 톰캣 구동 안함
  - RANDOM_PORT, DEFINED_PORT : 내장 톰캣 사용
  - NONE : 서블릿 환경 제공 안함



```java
// MOCK 을 사용한 경우
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class SampleControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void hello() {
        try {
            mockMvc.perform(get("/hello"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("hello keesun"))
                    .andDo(print());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

// TestRestTemplate 를 사용한 경우
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SampleControllerTest {

    @Autowired
    TestRestTemplate testRestTemplate;

    @Test
    public void hello() {
        try {
            String result = testRestTemplate.getForObject("/hello", String.class);
            assertThat(result).isEqualTo("hello keesun");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

// Service만 테스트하고 싶은 경우
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SampleControllerTest {

    @MockBean
    SampleService mockSampleService;

    @Test
    public void hello() {
        try {
            when(mockSampleService.getName()).thenReturn("keesun");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

  

@MockBean

- ApplicationContext에 들어 있는 빈을 Mock으로 만든 객체로 교체함.
- 모든 @Test 마다 자동으로 리셋.

슬라이스 테스트

- 레이버렬로 잘라서 테스트하고 싶을 때
- @Json Test
- @WebMvc Test
- WebFlux Test
- DataJpa Test
- ...



WebTestClient

- WebFlux를 사용하는 경우는 비동기이므로 테스트에서 비동기를 사용할 경우 이용함

```java
@Auto
```



<hr/>

스프링 웹 MVC 1부

- 스프링 부트 MVC
  - 자동 설정으로 제공하는 여러 기본 기능
- 스프링 MVC 확장
  - @Configuration + WebMvcConfigurer
- 스프링 MVC 재정의
  - @Configuration + @EnableWebMvc



### HttpMessageConverters

HTTP 요청 본문을 객체로 변경하거나, 객체를 HTTP 응답 본문으로 변경할 때 사용.

{"username":"keesun", "password":"123} <--> User

- RequestBody
- ResponseBody



```java
// UserControllerTest 
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

// String으로 JSON 입력 - USER 형태 JSON 출력
    @Test
    public void createUser_Json() throws Exception {
        String userJson = "{\"username\":\"keesun\", \"password\":\"123\"}";
        mockMvc.perform(post("/users/create")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(userJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username",
                        is(equalTo("keesun"))))
                .andExpect(jsonPath("$.password",
                        is(equalTo("123"))));
    }
}


// in UserController
    @PostMapping("/users/create")
    public User create(@RequestBody User user) {
        return user;
     }
```



스프링 부트

- 뷰 리졸버 설정 제공
- HttpMessageConvertersAutoConfiguration

```java
// XML 메시지 컨버터 추가하기
<dependency>
  <groupId>com.fasterxml.jackson.dataformat</groupId>
  <artifactId>jackson-dataformat-xml</artifactId>
</dependency>
```

​    

정적 리소스 지원

정적 리소스 맵핑 "/**"

- 기본 리소스 위치
  - Classpath:/static
  - Classpath:/public
  - Classpath:/resources
  - Classpath:/META-INF/resources
  - spring.mvc.static-path-pattern: 맵핑 설정 변경 가능
  - spring.mvc.static-locations: 리소스 찾을 위치 변경 가능
- Last-Modified 헤더를 보고 304 응답을 보냄
- ResourceHttpRequestHandler 가 처리함.
  - **<u>WebMvcConfigurer의 addResourceHandler로 커스터마이징 할 수 있음</u>**

```java
// in config/WebConfig
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/m/**")
                .addResourceLocations("classpath:/m/")
                .setCachePeriod(20);
    }
}

// 이후
localhost:8080/m/hello.html 로 확인
```



#### Thymeleaf

스프링 부트가 자동 설정을 지원하는 템플릿 엔진

- FreeMarker
- Groovy
- Thymeleaf
- Mustache



JSP 사용은 권장하지 않음

- JAR 패키징 할 때는 동작하지 않고, WAR 패키징 해야 함



##### HTML Unit

- HTML 테스트를 체계적으로 할 수 있는 툴
  - dependency 추가해서 사용할 수 있음

**WebClient** 를 주입받아서 사용함



#####  ExceptionHandler

스프링 @MVC 예외 처리 방법

- **@ControllerAdvice**
- **@ExchangeHandler**



스프링 부트가 제공하는 기본 예외 처리기

- BasicErrorController
  - HTML과 JSON 응답 지원
- 커스터마이징 방법
  - ErrorController 구현



커스텀 에러 페이지

- 상태 코드 값에 따라 에러 페이지 보여주기
- src/main/resources/static/template/error
- 404 html
- 5xx.html
- ErrorViewResolver 구현



##### SOP와 CORS

- **Single-Origin Policy** (SOP)
- **Cross-Origin Resource Sharing** (CORS)
- **Origin?**
  - URI 스키마 (http, https)
  - hostname (whiteship.me, localhost)
  - 포트 (8080, 18080)



-> 하나의 오리진 (ex- localhost:8080)이 다른 오리진 (ex- localhost:18080)의 리소스를 가져오려고 해도 막혀있다.

-> 이를 우회하기 위한 방법으로 Cross-Origin Resource Sharing 기술이 생겨났고

-> Springboot가 제공하는 @CrossOrigin 어노테이션을 통해 CORS 기술을 사용할 수 있게 되었다.



##### 스프링 MVC @Origin

```java
@CrossOrigin(origins="http://localhost:8080")
@GetMapping("/hello")
public String hello(){
	return "Hello";
}
```

<hr/>

#### 스프링 데이터

##### 인메모리 데이터베이스

지원하는 인-메모리 데이터베이스

- H2(추천, 콘솔 때문에..)
- HSQL
- Derby
- Spring-JDBC가 클래스패스에 있으면 자동 설정이 필요한 빈을 설정 해줍니다.
  - DataSource
  - JdbcTemplate



인-메모리 데이터베이스 기본 연결 정보 확인하는 방법

- URL: "testdb"
- Username: "sa"
- password: ""



H2 콘솔 사용하는 방법

- spring-boot-devtools를 추가하거나
- spring.h2.console.enabled=true 만 추가
- /h2-consolefh wjqthr



```java
@Component
public class H2Runner implements ApplicationRunner {

	@Autowired
	DataSource dataSource;

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		try (Connection connection = dataSource.getConnection()) {
			connection.getMetaData().getURL();
			connection.getMetaData().getUserName();

			Statement statement = connection.createStatement();
			String sql = "CREATE TABLE USER(ID INTEGER NOT NULL, name VARCHAR(255), PRIMARY KEY (id))";
			statement.executeUpdate(sql);
		}
		jdbcTemplate.execute("INSERT INTO USER VALUES (1, 'keesun')");
	}
}
```



##### MYSQL 설정하기

지원하는 DBCP

- HikariCP (기본)
- Tomcat CP
- Commons DBCP2



DBCP 설정

- **spring.datasource.hikari**
- spring.datasource.tomcat
- spring.datasource.dbcp2



MySQL용 Datasource 설정

- Spring.datasource.url=jdbc:mysql://localhost:330/springboot?useSSL=false
- Spring.datasource.username=keesun
- spring.datasource.password=pass
- 운영 DB용 설정
  - Spring.datasource.testWhileIdle=true
  - spring.datasource.validationQuery=SELECT 1



```java
// ConnectionTimeout
어플리케이션이 DB 커넥션풀에서 커넥션을 받아올 때,
	얼마 동안 기다릴지에 대한 설정. 기본값 30초 -> 연결 요청 후 30초가 지나면 에러
    
// maximumPoolSize
최대 커넥션풀의 커넥션 갯수. (무조건 많다고 좋은게 아니다.)
    기본값 10개 -> 최적화 값을 찾는게 좋다.
    
```



