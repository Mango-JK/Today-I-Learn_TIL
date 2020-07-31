# JSON 개념 잡기

### **JSON 개념**

[JSON](http://json.org/json-ko.html)은 JavaScript Object Notation의 약자로서 경량화 되어 있는 데이터 교환 방식중에 하나입니다. 그리고 서로 다른 언어들간에 데이터를 주고받을 수 있도록 한 텍스트 형식으로 되어 있습니다.

JSON전에 XML을 이용하여 데이터를 교환하였지만 JSON에 비해 데이터가 무거워지고 데이터가 아닌 tag들을 사용하게 되면서 양이 필요이상으로 많아진다는 (다른 이유들도 더 있지만) JSON을 많이 사용하고 있습니다.



### **JSON의 사용 이유**

JSON은 네트워크 연결을 통해 데이터를 주고 받을 때 직렬화하여 전송할 수 있고 브라우저를 포함한 Javascript기반 응용 프로그램을 개발할 때 사용됩니다. 또한 Web서비스 또는 Open API에서 공개데이터를 제공할 때 JSON을 사용합니다.



### **JSON의 포맷과 기본 자료형**

JSON은 프로그래밍언어가 아니라 Key : value 형식으로 이루어진 데이터 교환 포멧입니다.

JSON은 PHP, JAVA, JAVASCRIPT 등등 여기저기서 사용되고 있지만 개발 언어가 아닌 단순한 데이터의 표기방법입니다.



### **JSON 포맷**

데이터는 "Key" : "Value" 형으로 표현된다.

연속된 데이터는 ,로 분리합니다.



### **JSON의 기본 자료형**

| TYPE    | DESCRIPTION                                                  |
| ------- | ------------------------------------------------------------ |
| Number  | 정수, 실수를 사용하고 8진수와 16진수는 제공하지 않는다.      |
| String  | 항상 큰 따옴표(")로 묶어야 하며, 그 안에는 유니코드 문자들이 나열된다. 유니코드 중 역슬래시(\)와 큰따옴표(")는 바로 사용할 수 없다. 역슬래시는 제어문자를 표현하기 위해 사용되며 다음과 같은 의미를 지닌다. |
| Boolean | True/False                                                   |
| Array   | 배열은 대괄호[]로 나타낸다. 배열의 각 요소는 기본 자료형이거나 배열, 객체이다. 각 요소들은 쉼표(,)로 구별된다. 각 요소가 나타나는 순서에 의미가 있다. |
| Object  | 객체는 이름/값 쌍의 집합으로, 중괄호{}를 사용한다. 이름은 문자열이기 때문에 반드시 따옴표를 하며, 값은 기본 자료형이거나 배열, 객체이다. 각 쌍들은 쉼표(,)로 구별된다. 각 쌍이 나오는 순서는 의미가 없다. |
| Null    | Null                                                         |



### **JSON의 두 가지 Method**

JSON에는 자주 사용되는 Method가 있는데 JSON.parse(str)와 JSON.stringify(obj) 가 있습니다.

JSON.parse는 JSON 포맷의 문자열을 객체로 변환 시켜주고, 

JSON.stringify는 객체를 JSON 포맷의 문자열로 변환 시킵니다.



# **JAVA에서 JSON 데이터 만들기**



**먼저 JSON을 사용하기 위해 JSON에 필요한 라이브러리(jar)이 필요하다.**

**직접 다운받거나, Spring를 사용할 경우 Depencency를 추가하면 된다.**





**JSON 다운로드 http://code.google.com/p/json-simple/downloads/detail?name=json_simple-1.1.jar&can=2&q**



**Spring에서 Depencency 추가할 경우**

```
        <dependency>
            <groupId>com.googlecode.json-simple</groupId>
            <artifactId>json-simple</artifactId>
            <version>1.1</version>
        </dependency>
```



JSONObject 객체를 생성하여 데이터를 .put 할 수 있다.

JSONObject에 배열을 포함하고 싶다면 JSONArray 객체를 만들어 데이터를 집어 넣고 JSONObject에 JSONArray객체를 put 하면 된다.



```java
        
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("SECR_KEY", "ktko.tistory.com");
        jsonObject.put("KEY", "ktko");
        
        JSONObject data = new JSONObject();
        data.put("BANK_CD", "088");
        data.put("SEARCH_ACCT_NO", "1231231234");
        data.put("ACNM_NO", "123456");
        data.put("ICHE_AMT", "0");
        data.put("TRSC_SEQ_NO", "0000001");
        
        JSONArray req_array = new JSONArray();
        req_array.add(data);
        
        jsonObject.put("REQ_DATA", req_array);


```



# JSON 파싱하기



```java
public class JsonExam1 {

	public static void main(String[] args) throws IOException, ParseException {
		String jsonData = "{\"Persons\":[{\"name\":\"고경태\",\"age\":\"30\",\"블로그\":\"ktko.tistory.com\",\"gender\":\"남자\"}, {\"name\":\"이홍준\",\"age\":\"31\",\"블로그\":\"없음\",\"gender\":\"남자\"}, {\"name\":\"서정윤\",\"age\":\"30\",\"블로그\":\"없음\",\"gender\":\"여자\"}], \"Books\":[{\"name\":\"javascript의모든것\",\"price\":\"10000\"},{\"name\":\"java의모든것\",\"price\":\"15000\"}]}";
		
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = (JSONObject) jsonParser.parse(jsonData);
		
		JSONArray jsonDataArray = (JSONArray) jsonObject.get("Persons");
		
		for (int i = 0; i < jsonDataArray.size(); i++) {
			JSONObject personObject = (JSONObject) jsonDataArray.get(i);
			System.out.println("name : " + personObject.get("name"));
			System.out.println("age : " + personObject.get("age"));
		}
		
		System.out.println("===================================");
		
		JSONArray jsonBookArray = (JSONArray)jsonObject.get("Books");
		for (int i = 0; i < jsonBookArray.size(); i++) {
			JSONObject bookObject = (JSONObject)jsonBookArray.get(i);
			System.out.println("name : " + bookObject.get("name"));
			System.out.println("price : " + bookObject.get("price"));
		}
	}
}
```

