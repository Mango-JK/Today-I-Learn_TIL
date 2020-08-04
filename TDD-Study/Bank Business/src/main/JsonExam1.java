package main;

import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JsonExam1 {

	public static void main(String[] args) throws IOException, ParseException{
		String jsonData = "{\"Persons\":[{\"name\":\"�����\",\"age\":\"30\",\"��α�\":\"ktko.tistory.com\",\"gender\":\"����\"}, {\"name\":\"��ȫ��\",\"age\":\"31\",\"��α�\":\"����\",\"gender\":\"����\"}, {\"name\":\"������\",\"age\":\"30\",\"��α�\":\"����\",\"gender\":\"����\"}], \"Books\":[{\"name\":\"javascript�Ǹ���\",\"price\":\"10000\"},{\"name\":\"java�Ǹ���\",\"price\":\"15000\"}]}";
		
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