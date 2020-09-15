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
> 해야 할 작업 : **Elgg에서 HTTP GET 요청 및 HTTP POST 요청을 캡처하기**



#### 3-2. GET Request를 이용한 CSRF 공격

> 이 상황을 실험하기 위해 두 사람이 필요합니다. 엘리스와 보비.
>
> 보비는 앨리스와 친구가 되고 싶어하지만, 앨리스는 보비를 추가하는 것을 거부한다.
>
> **보비는 CSRF 공격을 활용해 목표를 달성하기로 한다.**



##### CSRF 공격 과정

> 1. 보비는 앨리스에게 (이메일or Elgg 포스팅을 통해) URL을 보낸다.
>
> 2. 앨리스가 궁금해하며 클릭한다.
> 3. 이 URL에서 앨리스는 보비의 웹사이트 (www.csrflabattacker.com)로 이동한다.
> 4. 당신이 보비라고 가정하고, 어떻게 웹 페이지의 내용을 구성할 수 있는지 설명해보자.
> 5. 앨리스가 URL을 클릭하자마자 보비가 친구 목록에 추가된다.



##### 실습

> 'HTTP Header Live' Tool을 사용하여 HTTP 요청이 어떤 모습인지 조사할 수 있다.
>
> 이 작업에서는 CSRF 공격을 하기 위해서 Javascript를 활용할 수 없다.
>
> 앨리스가 웹 페이지를 방문하는 즉시 그 페이지를 클릭하지 않고 공격을 성공시켜야 한다.
>
> **(힌트는, HTTP GET 요청을 트리거하는 img 태그를 사용할 수 있다.)**

