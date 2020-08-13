import java.io.BufferedReader;
import java.io.BufferedWriter;i
import java.io.OutputStreamWriter;mport java.io.InputStreamReader;

public class 달팽이숫자 {

	public static void main(String[] args) {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
		int TC = br.readLine().trim();

		for (int tc = 0; tc < TC; tc++) {
			int N = Integer.parseInt(br.readLine().trim());
			int[][] map = new int[n][n];
			int ci = 0;
			int cj = 0;

			for (int i = 0; i < N * N; i++) {
				
			}	// end value

			for (int i = 0; i < N; i++) {
				for (int j = 0; j < N; j++) {
					System.out.print(map[i][j] + " ");
				}
				System.out.println();
			}
		}	// end TC
	}
}
