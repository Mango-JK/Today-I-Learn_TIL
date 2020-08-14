
// 다음과 같이 import를 사용할 수 있습니다.

class Solution {
	public int solution(int[] arr) {
		int answer = 0;
		int currentNum = arr[0];
		int count = 1;
		for (int i = 1; i < arr.length; i++) {
			if (arr[i] > currentNum) {
				count++;
				currentNum = arr[i];
				if (count > answer)
					answer = count;
			} else {
				count = 1;
				currentNum = arr[i];
			}
		}
		return answer;
	}

	// 아래는 테스트케이스 출력을 해보기 위한 main 메소드입니다.
	public static void main(String[] args) {
		Solution sol = new Solution();
		int[] arr = { 3, 1, 2, 4, 5, 1, 2, 2, 3, 4 };
		int ret = sol.solution(arr);

		// [실행] 버튼을 누르면 출력 값을 볼 수 있습니다.
		System.out.println("solution 메소드의 반환 값은 " + ret + " 입니다.");
	}
}