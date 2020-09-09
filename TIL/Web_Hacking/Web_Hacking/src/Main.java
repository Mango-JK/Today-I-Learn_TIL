import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Main {

	public static void main(String[] args) throws MalformedURLException, IOException {
		String target = "https://www.naver.com/";
		HttpURLConnection con = (HttpURLConnection) new URL(target).openConnection();
		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
		String temp = "";
		while((temp = br.readLine()) != null) {
			if(temp.contains("±Ë¡æ« ")) {
				System.out.println(temp);
				break;
			}
		}
		con.disconnect();
		br.close();
	}
}