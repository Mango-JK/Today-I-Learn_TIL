import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class Main_세션관리 {

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
}