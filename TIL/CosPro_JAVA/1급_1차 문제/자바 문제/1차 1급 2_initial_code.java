// You may use import as below.
//import java.util.*;

class Solution {

	int[][] map;
	int[] di = { 0, 1, 0, -1 };
	int[] dj = { 1, 0, -1, 0 };

	boolean isRange(int i, int j, int n) {
		return i < 0 && j < 0 && i >= n && j >= n;
	}

	public int solution(int n) {
		map = new int[n][n];
		int ci = 0;
		int cj = 0;
		int num = 1;

		int ni = 0;
		int nj = 0;

		for (int i = 0; i < n * n; i++) {
			map[ci][cj] = num++;

			for (int k = 0; k < 4; k++) {
				ni = ci + di[k];
				nj = cj + dj[k];
				
				if(!isRange(ni, nj, n) || map[ni][nj] != 0) {
					ni -= di[k];
					nj -= dj[k];
				}
				break;
			}
		}
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				System.out.println(map[i][j] + " ");
			}
			System.out.println();
		}
	}

	// The following is main method to output testcase.
	public static void main(String[] args) {
		Solution sol = new Solution();
		int n1 = 5;
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