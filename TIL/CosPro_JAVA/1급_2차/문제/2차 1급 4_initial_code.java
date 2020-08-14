
// 다음과 같이 import를 사용할 수 있습니다.
import java.util.*;

class Solution {
	public int solution(int[] arr, int K) {
		int answer = 0;

		for (int i = 0; i < arr.length; i++) {
			for (int j = i + 1; j < arr.length; j++) {
				for (int j2 = j + 1; j2 < arr.length; j2++) {
					if (arr[i] + arr[j] + arr[j2] % K == 0)
						answer++;
				}
			}
		}

		return answer;
	}

	// 아래는 테스트케이스 출력을 해보기 위한 main 메소드입니다.
	public static void main(String[] args) {
		Solution sol = new Solution();
		int[] arr = { 1, 2, 3, 4, 5 };
		int K = 3;
		int ret = sol.solution(arr, K);

		// [실행] 버튼을 누르면 출력 값을 볼 수 있습니다.
		System.out.println("solution 메소드의 반환 값은 " + ret + " 입니다.");
	}
}