package main;

import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JsonExam1 {

	public static void main(String[] args) throws IOException, ParseException{
		String jsonData = "{\"Persons\":[{\"name\":\"고경태\",\"age\":\"30\",\"블로그\":\"ktko.tistory.com\",\"gender\":\"남자\"}, {\"name\":\"이홍준\",\"age\":\"31\",\"블로그\":\"없음\",\"gender\":\"남자\"}, {\"name\":\"서정윤\",\"age\":\"30\",\"블로그\":\"없음\",\"gender\":\"여자\"}], \"Books\":[{\"name\":\"javascript의모든것\",\"price\":\"10000\"},{\"name\":\"java의모든것\",\"price\":\"15000\"}]}";
		
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = (JSONObject)jsonParser.parse(jsonData);
		JSONArray jsonDataArray = (JSONArray) jsonObject.get("Persons");
		
		for (int i = 0; i < jsonDataArray.size(); i++) {
			JSONObject personObject = (JSONObject)jsonDataArray.get(i);
			System.out.println("=============");
			System.out.println(personObject.get("name"));
			System.out.println(personObject.get("age"));
		}
		
		System.out.println("00000000000000000000000000000000000");
		
		JSONArray jsonBookArray = (JSONArray)jsonObject.get("Books");
		for (int i = 0; i < jsonBookArray.size(); i++) {
			JSONObject bookData = (JSONObject)jsonBookArray.get(i);
			System.out.println(bookData.get("name"));
			System.out.println(bookData.get("price"));
		}
	}
}