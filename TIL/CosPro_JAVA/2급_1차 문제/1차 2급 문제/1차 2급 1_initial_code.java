// You may use import as below.
import java.util.*;

class Solution {
    public int[] solution(String[] shirtSize) {
        // Write code here.
    	int[] answer = new int[6];

    	for (int i = 0; i < shirtSize.length; i++) {
			if(shirtSize[i].equals("XS")) {
				answer[0]++;
			} else if(shirtSize[i].equals("S")) {
				answer[1]++;
			} else if(shirtSize[i].equals("M")) {
				answer[2]++;
			} else if(shirtSize[i].equals("L")) {
				answer[3]+;
			} else if(shirtSize[i].equals("XL")) {
				answer[4]++;
			} else if(shirtSize[i].equals("XXL")) {
				answer[5]++;
			}
		}
        return answer;
    }
    
    // The following is main method to output testcase.
    public static void main(String[] args) {
        Solution sol = new Solution();
        String[] shirtSize = {"XS", "S", "L", "L", "XL", "S"};
        int[] ret = sol.solution(shirtSize);
 
        // Press Run button to receive output.  
        System.out.println("Solution: return value of the method is " + Arrays.toString(ret) + " .");
    }
}