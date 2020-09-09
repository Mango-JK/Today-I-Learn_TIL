# # Cross Site Request Forgery(CSRF or XSRF)

> **CSRF란?**
>
> Cross-site request forgery의 약자로 한글로는 주로 "사이트간 요청 위조" 정도로 번역됩니다.
>
> 공격자가 정상 사용자에게 자신이 원하는 행위를 하도록 강제하는 공격이다.
>
> 
>
> **CSRF 공격의 목적**은 수정, 삭제, 등록, 권한상승, 정보유출 등이 됩니다.



> **특징**
>
> 사용자가 직접 코드를 실행하기 때문에 게시글이 자동으로 생성될 때 해커의 흔적이 남지 않는다.
>
> XSS와 달리 자바스크립트를 사용하지 않아도 공격이 가능하다.



> **CSRF 공격 조건**
>
> 해커는 사이트에 공격 전 제공되는 Request/Response를 분석해야 한다.(취약점 분석)
>
> 사이트가 사용자 권한 인증을 Session Token으로만으로 인증 할 때 가능하다.



> **공격 순서**
>
> 1. 해커가 웹 사이트에 CSRF 공격코드(ex. 사용자 정보 수정 코드)를 게시글에 작성
>
> 2. 사용자는 로그인을 한 후 해커가 작성한 게시글을 열람한다. 
>
>    -> 게시글 정보를 얻기 위해 서버로 Request
>
> 3. 서버는 해당 게시글을 Response 해준다.
>
>    -> 해커가 작성한 코드는 아직 동작하지 않음
>
> 4. 사용자가 Response된 게시글을 읽게 되면 해커가 남긴 코드가 동작하게 됨.
>
> **결과 : ** 사용자가 코드를 실행하기 때문에 사용자 권한으로 공격코드(ex. 사용자 정보 수정)가 실행된다.
>
> -> 사용자 정보가 자동으로 수정됨



> **보안 방법**
>
> 1. XSS 취약점이 없도록 확인
> 2. Session Token(Cookie) 값 이외에 사용자를 인증하는 다른 방법이 있어야 함.
> 3. 서버 측에서는 스크립트가 기능을 발휘하지 못하게 막는다.





<hr/>

<hr/>



```
CSRF는 공격자가 웹 어플리케이션의 기능을 미리 알고 있어야 공격이 가능하다. 상위 권한의 사용자를 대상으로 CSRF 공격을 하기 위해서는 상위 권한의 기능을 알고 있어야 한다. 일반적으로 관리자 기능을 대상으로 CSRF 공격을 하게 되는 데 관리자 기능을 미리 알지 못하면 공격이 불가능하다. 따라서 CSRF 공격이 가능한 경우는 관리기능을 미리 알 수 있는 공개용 CMS가 대표적이다.
또 다른 사례로는 권한이 동등한 사용자 간의 공격을 들 수 있다. 예를 들어 메일서비스에 CSRF 공격이 가능하면 다른 사용자의 메일을 지우거나 메일함의 내용을 열람해 볼 수 있는 공격을 수행할 수 있을 것이다.
```



#### Vulnerability: Cross Site Request Forgery (CSRF) 공략: Security Level = Low

- 비밀번호 변경 시, 새로운 비밀번호만 설정하여 바꿀 수 있는 경우



![image-20200909111001983](C:\Users\RYZEN2\AppData\Roaming\Typora\typora-user-images\image-20200909111001983.png)



```html
GET http://192.168.206.136/vulnerabilities/csrf/?password_new=testlow&password_conf=testlow&Change=Change HTTP/1.1
User-Agent: Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:50.0) Gecko/20100101 Firefox/50.0
Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8
Accept-Language: en-US,en;q=0.5
Referer: http://192.168.206.136/vulnerabilities/csrf/
Cookie: PHPSESSID=gulk7ha641o55qk52os77asr42; security=low
Connection: keep-alive
Upgrade-Insecure-Requests: 1
Host: 192.168.206.136

```



다른 사이트에 대해서 이 공격이 성공하기 위해서는 관리자의 세션 쿠키를 알아야 한다.

하지만 - XSS 취약점이 없다면 - 관리자의 세션키를 알기는 매우 어렵다.

이 때문에 공격자는 관리자 권한의 세션키로

​				이미 접속하고 있는 관리자에게 대신 비밀번호 변경 요청을 하도록 유도한다.





#### Vulnerability: Cross Site Request Forgery (CSRF) 공략: Security Level = Middle

ecurity Middle인 경우의 CSRF 문제에서는 약간의 방어책을 마련하였다. PHP 소스를 살펴보자.

```php
// Checks to see where the request came from
    if( eregi( $_SERVER[ 'SERVER_NAME' ], $_SERVER[ 'HTTP_REFERER' ] ) ) {
        // Get input
        $pass_new  = $_GET[ 'password_new' ];
        $pass_conf = $_GET[ 'password_conf' ];
```



요청한 URL이 시작된 지점(`$_SERVER[ 'HTTP_REFERER' ]`)의 문자열에 웹서버의 도메인 주소(`$_SERVER[ 'SERVER_NAME' ]`)가 **포함**되어 있는 지를 대소문자 구분없이 검사(`eregi()`)하고 있다. 이 방법은 `**실제 서비스에서도 종종 사용**`되는 데 사용되고 있다는 것을 알기만 하면 쉽게 우회가 가능한 단점이 있다.



첫번째 방법은 `$_SERVER[ 'SERVER_NAME' ]`가 포함된 파일이름을 만들면 된다. 

이 설명서에서 `$_SERVER[ 'SERVER_NAME' ]`의 값은 `192.168.206.136`이다. "`http://h4ck3r.site/192.168.206.136.html`"과 같은 파일에 공격코드를 넣어서 관리자가 접근하게 유도하면 CSRF를 성공시킬 수 있다.



두번째 방법은 대상 홈페이지에 존재하는 XSS 취약점을 이용하는 것이다. 이 경우에는 공격 출발지가 대상 홈페이지 내에 있으므로 Medium 수준의 방어기작이 자동으로 우회된다. 



Security Level = High에서 저장형 XSS 취약점을 이용하여 공격하는 방법이 있으니 참조하기 바란다. 어떤 방법을 쓰던 `<img src="/vulnerabilities/csrf/?password_new=1234&password_conf=1234&Change=Change">`를 관리자가 실행하도록 유도하면 된다.





#### Vulnerability: Cross Site Request Forgery (CSRF) 공략: Security Level = High

다음은 Security Level = High에서의 CSRF 요청 URL이다.

```php
http://192.168.206.136/vulnerabilities/csrf/?password_new=testhigh&password_conf=testhigh&Change=Change&user_token=ea107cddf30ce86b305318ca699457d0
```

기존 비밀번호를 몰라도 비밀번호를 변경할 수 있다. 그런데 `user_token`이라는 새로운 변수를 사용하고 있다. 이 변수는 접속시마다 새로 생성되는 토큰으로 접속자가 아니면 알기가 어렵다(`**접속자는 알 수 있다**`). 이러한 변수를 **Anti-CSRF Token**이라고 한다.





```
**[공격자의 입장에서 취약점 찾기!]**
```

Anti-CSRF Token을 **접속자는 알 수 있다**는 점이 Security Level = High 수준의 CSRF 공격에 대한 단서가 된다. 공격자는 알 수 없지만 정상적인 사용자에게 비밀번호 변경을 요청하도록 만들 때 `user_token`을 공격 URL에 추가해주면 된다. 이때 AJAX가 필요하다. `user_token`은 접속때마다 생성되어 서버의 세션에 저장되므로 **AJAX를 이용하여 이 값을 알아낼 수 있다.** 원하는 AJAX를 실행하기 위해서는 자바스크립트를 실행해야 하므로 공격자는 홈페이지를 뒤져서 XSS 취약점을 찾아낸다. DVWA에서 제공하는 저장형 XSS 실습문제를 이용해 보자.



```java
// Anti-CSRF Token의 경우에는 노출이 가능한 값이므로 
// 이것만으로는 완벽한 방어가 어렵다는 것을 이 실습문제에서 알 수 있다.
// 따라서 회원정보와 같이 중요한 데이터를 변경할 때는 반드시 기존 비밀번호를 확인하여야 한다.
```





#### Vulnerability: Cross Site Request Forgery (CSRF) 공략: Security Level = Impossible

Security Level = Impossible에서는 기존 비밀번호를 입력하여야만 비밀번호 수정이 가능하다. 비밀번호 등과 같이 회원 정보를 수정할 때는 반드시 이와 같은 장치를 마련하여야 안전하다.



```java
http://192.168.206.136/vulnerabilities/csrf/?password_current=testhigh&password_new=password&password_conf=password&Change=Change&user_token=740053672691227e68077179cf79d586
```



CSRF 공격을 막기위한 `user_token`과 현재 비밀번호까지 알아야 하므로 CSRF 공격은 불가능하다.



<hr/>

<hr/>

**세션 탈취가 불가능 할 때, 여전히 스크립팅 공격은 가능하다고 가정 할 때 사용할 수 있는 공격 기법**

**(가장 많이 활용되는 방법)** 



특정 사용자의 권한을 뺏어오는 것은 불가능하지만, 특정 사용자가 스스로 어떠한 행위를 하게 만들 수 있다.



즉, 특정한 계정의 권한 탈취가 목적이 아니라 다른 더욱 변칙적인 공격을 감행할 수 있도록 하는 데 많이 사용되는 기법.



POST로 요청을 보내지만 실제 서버에서는 POST 방식과 GET 방식 처리에 차이를 두지 않음.

-> 이를 이용해 GET으로 등록 요청을 하면서 공격을 시도.



```java

```



### CSRF를 방어할 수 있는 방법

- 스트립트 문구를 마음대로 삽입할 수 없도록 전부 차단하는 방법
- 자체적인 필터링 시스템을 구축해서 모든 경우의 수를 막거나,
- 이미 만들어진 XSS 관련 방어 라이브러리를 사용하는 방법이 있음.







<hr/>

<hr/>

## SEED Lab

주제

1. CSRF(사이트간 요청 위조) 공격
2. CSRF 대책
   - 비밀 토큰 및 동일 사이트 쿠키
3. HTTP GET 및 POST 요청
4. Javascript와 Ajax



```javascript
// Attacker’s website
URL: http://www.csrflabattacker.com
Folder: /var/www/CSRF/Attacker/

// Victim website (Elgg)
URL: http://www.csrflabelgg.com
Folder: /var/www/CSRF/Elgg/
```



























<hr/>

```java
// 기타 웹 해킹 관련 지식
```



<hr/>

## # 네이버 서버 시간 웹 파싱

**웹 파싱 : 특정한 정보를 자기가 원하는 형태로 분석하는 것**



>  사용자가 흔히 네이버에서 특정한 자료를 검색하면, 네이버는 그 자료에 대한 정보를 포함하는 웹 문서를 반환한다. 이러한 과정에서 웹 문서에서 자신이 원하는 정보만 파싱한다면 이것이 **웹 파싱**이 된다.



**파싱**은 **자동화**가 가능하기 때문에 강력한 해킹 기법으로 사용된다.



```java
	public static void main(String[] args) throws MalformedURLException, IOException {
		String target = "https://www.naver.com/";
		HttpURLConnection con = (HttpURLConnection) new URL(target).openConnection();
		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
		String temp = "";
		while((temp = br.readLine()) != null) {
			System.out.println(temp);
		}
		con.disconnect();
		br.close();
	}
```



<br/>

<hr/>

## # 패킷 분석을 통해 GET 방식과 POST 방식을 구분하기

1. **Client**에서 **Server**로 로그인 정보를 가지고 요청**(Request)**을 보낸다.
2. **Server**에서 로그인 정보를 처리하여 **Client**에게 로그인 메시지를 반환**(Response)**한다.

<br/>

이 과정에서 **GET** 방식과 **POST** 방식이 있다.

**GET 방식** : 

- 쉽게 보임 (URL 에 내가 보낸 정보가 바로 보인다.)

<br/>

**POST 방식** : 

- 쉽게 보이지 않음. 

<br/>

**하지만, 아무리 POST 방식이라고 하더라도 패킷을 통해서 네트워크 통신을 한다는 것은 GET 방식과 다르지 않다.** 



WireShark와 같은 툴을 사용한다면, 자신이 어떠한 패킷을 보내는지, 또는 받는지 대부분의 패킷을 캐치할 수 있게 됩니다.



이때 특정 주소(IP)로 보내는 패킷을 필터링 한다면 사이트로 요청을 보내는 정보를 볼 수 있게 된다.

<br/>

<hr/>

## # 세션 관리와 개인 정보 출력

**세션 :** 서버에서 사용자 각각을 구분하기 위한 방법으로 세션을 사용하곤 한다.

<br/>

만약 해커가 존재한다면, 세션을 탈취 또는 복제하여 사용자인 것처럼 속일 수가 있다.

이를 통해 서버로부터 개인 정보를 얻어낼 수 있는데 이를 **세션 탈취**라고 한다.

<br/>

```java
	public static void main(String[] args) throws MalformedURLException, IOException {
		String memberID = "cruiseny";
		String memberPassword = "cruiseny";
		String target = "https://www.dowellcomputer.com/member/memberLoginAction.jsp?memberID=" + memberID + "memberPassword=" + memberPassword;
		HttpsURLConnection con = (HttpsURLConnection) new URL(target).openConnection();
		String cookie = "";
		String temp = con.getHeaderField("Set-Cookie");
		if(temp != null) {
			cookie = temp;
		}
		System.out.println("현재 당신의 세션은 : " + cookie + " 입니다.");
	}
```

<br/>

<hr/>

## # XSS(Cross Site Scripting) 공격

**: 특정한 스크립트를 삽입하는 공격**

<br/>

주로 JavaScript와 같은 언어로 제작된 사이트에서 공격받을 수 있음.

필터링이 약한 경우 2진수로 이루어진 스크립팅 공격을 받을 수 있음.

<br/>

**XSS의 목적은 세션 탈취에 있을 수 있다**!

<hr/>

## # XST(Cross Site Tracing) 공격

웹 서버가 TRACE 프로토콜을 허용하고 있을 때 XML 요청을 이용하여 세션을 탈취하는 공격 방법. 