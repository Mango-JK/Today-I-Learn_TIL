// You may use import as below.
//import java.util.*;

class Solution {

	int[][] map;
	int di = { 0, 1, 0, -1 };
	int dj = { 1, 0, -1, 0 };

	boolean inRange(int i, int j, int n) {
		return 0 <= i && i < n && 0 <= j && j < n;
	}

	public int solution(int n) {
		map = new int[n][n];
		int ci = 0;
		int cj = 0;
		int num = 1;
		
		while(inRange(ci, cj, n) && map[ci][cj] == 0) {
			for (int k = 0; k < 4; k++) {
				if(!inRange(ci, cj, n) || map[ci][cj] != 0)
					break;
				
				map[ci][cj] = num++;
				int ni = ci + di[k];
				int nj = cj + dj[k];
				
				if(!inRange(ni, nj, n) || map[ni][nj] != 0) {
					ci += di[(k+1) % 4];
					cj += dj[(k+1) % 4];
					break;
				}
				
				ci = ni;
				cj = nj;
			}	// end K
		}	// end while
	}

	// The following is main method to output testcase.
	public static void main(String[] args) {
		Solution sol = new Solution();
		int n1 = 3;
		int ret1 = sol.solution(n1);
		for (int i = 0; i < n1; i++) {
			for (int j = 0; j < n1; j++) {
				System.out.println(map[i][j] + " ");
			}
			System.out.println();
		}

		// Press Run button to receive output.
		System.out.println("Solution: return value of the method is " + ret1 + " .");

		int n2 = 2;
		int ret2 = sol.solution(n2);

		// Press Run button to receive output.
		System.out.println("Solution: return value of the method is " + ret2 + " .");
	}
}