## TDD

**1. jUnit이란?**

\- Java에서 독립된 단위테스트(Unit Test)를 지원해주는 프레임워크이다.



**2. 단위테스트(Unit Test)란?**

\- 소스코드의 특정 모듈이 의도된 대로 정확히 작동하는지 검증하는 절차이다.

\- 모든 함수와 메소드에 대한 테스트 케이스(Test case)를 작성하는 절차를 말한다.

\- jUnit은 보이지 않고 숨겨진 단위 테스트를 끌어내어 정형화시켜 단위테스트를 쉽게 해주는 테스트 지원 프레임워크이다.



**3. jUnit 특징**

\- **단정(assert) 메서드**로 테스트 케이스의 수행 결과를 판별한다.(ex: assertEquals(예상값, 실제값))

\- jUnit4부터는 테스트를 지원하는 어노테이션을 제공한다.(@Test @Before @After)

\- @Test 메서드가 호출할 때 마다 새로운 인스턴스를 생성하여 독립적인 테스트가 이루어지게 한다.



**4. jUnit에서 테스트를 지원하는 어노테이션(Annotation)**

**@Test**

\- @Test가 선언된 메서드는 테스트를 수행하는 메소드가 된다.

\- jUnit은 각각의 테스트가 서로 영향을 주지 않고 독립적으로 실행됨을 원칙으로 @Test마다 객체를 생성한다.



**@Ignore**

\- @Ignore가 선언된 메서드는 테스트를 실행하지 않게 한다.



**@Before**

\- @Before가 선언된 메서드는 @Test 메서드가 실행되기 전에 반드시 실행되어진다.

\- @Test메서드에서 공통으로 사용하는 코드를 @Before 메서드에 선언하여 사용하면 된다.



**@After**

\- @After가 선언된 메서드는 @Test 메소드가 실행된 후 실행된다.



**@BeforeClass**

\- @BeforeClass 어노테이션은 @Test 메소드보다 먼저 한번만 수행되어야 할 경우에 사용하면 된다.



**@AfterClass**

\- @AfterClass 어노테이션은 @Test 메소드 보다 나중에 한번만 수행되어야 할 경우에 사용하면 된다.





**5. 자주 사용하는 jUnit 메서드**

| assertEquals(a,b);      | 객체 a,b의 값이 일치함을 확인한다.                           |
| ----------------------- | ------------------------------------------------------------ |
| assertArrayEquals(a,b); | 배열 a,b의 값이 일치함을 확인한다.                           |
| assertSame(a,b);        | 객체 a,b가 같은 객체임을 확인한다. 두 객체의 레퍼런스가 동일한가를 확인한다. |
| assertTrue(a);          | 조건 a가 참인가 확인한다.                                    |
| assertNotNull(a);       | 객체 a가 null이 아님을 확인한다.                             |





**6. Spring-Test에서 테스트를 지원하는 어노테이션**

**@RunWith(SpringJUnit4ClassRunner.class)**

\- @RunWith는 jUnit 프레임워크의 테스트 실행방법을 확장할 때 사용하는 어노테이션이다.

\- SpringJUnit4ClassRunner라는 클래스를 지정해주면 jUnit이 테스트를 진행하는 중에 ApplicationContext를 만들고 관리하는 작업을 진행해준다.

\- @RunWith 어노테이션은 각각의 테스트 별로 객체가 생성되더라도 싱글톤(Singletone)의 ApplicationContext를 보장한다.



**@ContextConfiguration**

\- 스프링 빈(Bean) 설정 파일의 위치를 지정할 때 사용되는 어노테이션이다.



**@Autowired**

\- 스프링 DI에서 사용되는 특별한 어노테이션이다.

\- 해당 변수에 자동으로 빈(Bean)을 매핑 해준다.

\- 스프링 빈(Bean) 설정 파일을 읽기 위해 굳이 GenericXmlApplicationContext를 사용할 필요가 없다.

\- 변수, setter메서드, 생성자, 일반메서드에 적용가능하다.

\- 의존하는 객체를 주입할 떄 주로 Type을 이용하게 된다.

\- <property>, <constructor-arg> 태그와 동일한 역할을 한다.

<br/><hr/>

<center><image src="./images/img01.PNG"></center>
<br/>

<center><image src="./images/img02.PNG"></center>
</br>

가장 기본적인 **Class 생성 메소드**

#### => 아주 간단한 메소드지만, 귀찮다고 생각하지 말고 꼭 만드는 습관을 들이자 !

<br/>

<center><image src="./images/img03.PNG"></center>
<br/>

<center><image src="./images/img04.PNG"></center>
<br/>

**생성자를 통해서 Class를 만들고, 3가지 경우에 대해 Test.**

#### 이때 money 처럼 i와 같이 가독성이 떨어지는 코드를 피하자.

<br/>

<center><image src="./images/img05.PNG"></center>
<br/>

###  🚩 실습 1. 최종 코드

<hr/>
```java
package test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import main.Account;

public class AccountTest {
	private Account account;

	@Before
	private void setup() {
		account = new Account(10000);
	}

	@Test
	public void testAccount() throws Exception {
	}

	@Test
	public void testGetBalance() throws Exception {
		assertEquals(10000, account.getBalance());

		account = new Account(1000);
		assertEquals(1000, account.getBalance());

		account = new Account(0);
		assertEquals(0, account.getBalance());
	}

	@Test
	public void testDeposit() throws Exception {
		account.deposit(1000);
		assertEquals(11000, account.getBalance());
	}

	@Test
	public void testWithdraw() throws Exception {
		account.withdraw(1000);
		assertEquals(9000, account.getBalance());
	}
}
```

<br/>

```java
package main;

public class Account {
	private int balance;
	
	public Account(int money) {
		this.balance = money;
	}

	public int getBalance() {
		return this.balance;
	}

	public void deposit(int money) {
		this.balance += money;
	}

	public void withdraw(int money) {
		this.balance -= money;
	}
}
```

<br/>

<hr/>

3개의 주석(`//given, //when, //then`)은 다음과 같은 내용을 나타냅니다.

- given
  - 테스트 기반 환경을 구축하는 단계
  - 여기선
  - `@builder`의 사용법도 같이 확인
- when
  - 테스트 하고자 하는 행위 선언
  - 여기선 Posts가 DB에 insert 되는것을 확인하기 위함
- then
  - 테스트 결과 검증
  - 실제로 DB에 insert 되었는지 확인하기 위해 조회후, 입력된 값 확인













<hr>

# 📑 Test Code 예시

1. ## Class 생성 테스트 코드

```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class PostsTest {
    
	@Test
    public void 기본생성자_테스트() throws Exception{
        Posts posts = new Posts();
    }
}
```



> Class의 기본 생성자를 Test한다.



2. ## Save 테스트 코드

```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class PostsRepositoryTest {

    @Autowired
    PostsRepository postsRepository;

    @After
    public void cleanup() {
        /** 
        이후 테스트 코드에 영향을 끼치지 않기 위해 
        테스트 메소드가 끝날때 마다 respository 전체 비우는 코드
        **/
        postsRepository.deleteAll();
    }

    @Test
    public void 게시글저장_불러오기() {
        //given
        postsRepository.save(Posts.builder()
                .title("테스트 게시글")
                .content("테스트 본문")
                .author("jojoldu@gmail.com")
                .build());

        //when
        List<Posts> postsList = postsRepository.findAll();

        //then
        Posts posts = postsList.get(0);
        assertThat(posts.getTitle(), is("테스트 게시글"));
        assertThat(posts.getContent(), is("테스트 본문"));
    }
}
```



> Dto 클래스가 service.save 메소드에 전달되면, DB에 잘 저장되었는지 검증하는 것입니다.



3. ## Update 테스트 코드

```java
    @Test
    public void Posts_수정된다() throws Exception {
        //given
        Posts savePosts = postsRepository.save(Posts.builder()
        .title("title")
        .content("content")
        .author("author")
        .build());

        Long updateId = savePosts.getId();
        String expectedTitle = "title2";
        String expectedContent = "content2";

        PostsUpdateRequestDto requestDto = PostsUpdateRequestDto.builder()
                .title(expectedTitle)
                .content(expectedContent)
                .build();

        String url = "http://localhost:" + port + "/api/v1/posts/" + updateId;

        HttpEntity<PostsUpdateRequestDto> requestEntity = new HttpEntity<>(requestDto);

        //when
        ResponseEntity<Long> responseEntity = restTemplate.exchange(url, HttpMethod.PUT,
                requestEntity, Long.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(expectedTitle);
        assertThat(all.get(0).getContent()).isEqualTo(expectedContent);
    }
```



> Update 메소드가 잘 실행되는지 테스트







## #1. JPA Auditing 테스트 코드



```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class PostsRepositoryTest {

    @Autowired
    PostsRepository postsRepository;

    @After
    public void cleanup() {
        postsRepository.deleteAll();
    }

    @Test
    public void BaseTimeEntity_등록 () {
        //given
        LocalDateTime now = LocalDateTime.now();
        postsRepository.save(Posts.builder()
                .title("테스트 게시글")
                .content("테스트 본문")
                .author("jojoldu@gmail.com")
                .build());

        //when
        List<Posts> postsList = postsRepository.findAll();

        //then
        Posts posts = postsList.get(0);
        assertTrue(posts.getCreatedDate().isAfter(now));
        assertTrue(posts.getModifiedDate().isAfter(now));
    }
}
```



> BaseTimeEntity가 잘 적용되었는지 테스트



<hr/>












































#### 출처 : https://repo.yona.io/doortts/blog/issue/2

