import java.util.HashMap;
import java.util.Map;

public class Solution {

    public int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> additions = new HashMap<>(nums.length);
        for (int i = 0; i < nums.length; i++) {
            int currentValue = nums[i];
            if (additions.containsKey(currentValue)) {
                return new int[]{additions.get(currentValue), i};
            }
            additions.put(target - currentValue, i);
        }
        return new int[0];
    }
}