//给你一个整数数组 nums 和一个整数 k ，判断数组中是否存在两个 不同的索引 i 和 j ，满足 nums[i] == nums[j] 且 abs(i 
//- j) <= k 。如果存在，返回 true ；否则，返回 false 。 
//
// 
//
// 示例 1： 
//
// 
//输入：nums = [1,2,3,1], k = 3
//输出：true 
//
// 示例 2： 
//
// 
//输入：nums = [1,0,1,1], k = 1
//输出：true 
//
// 示例 3： 
//
// 
//输入：nums = [1,2,3,1,2,3], k = 2
//输出：false 
//
// 
//
// 
//
// 提示： 
//
// 
// 1 <= nums.length <= 10⁵ 
// -10⁹ <= nums[i] <= 10⁹ 
// 0 <= k <= 10⁵ 
// 
//
// Related Topics 数组 哈希表 滑动窗口 👍 504 👎 0


package com.coachhe.leetcode.editor.cn.暴力法.P219_存在重复元素II_简单;

import org.testng.annotations.Test;

import java.util.HashSet;

/**
 * 存在重复元素 II
 *
 * @author CoachHe
 * @date 2022-08-12 00:31:46
 */
public class P219_ContainsDuplicateIi {
    @Test
    public void test() {
        //测试代码
        Solution solution = new P219_ContainsDuplicateIi().new Solution();
        int[] nums = new int[]{1, 2, 3, 1, 2, 3};
        System.out.println(solution.containsNearbyDuplicate(nums, 2));
    }

    //力扣代码
//leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public boolean containsNearbyDuplicate(int[] nums, int k) {
            int index = 0;
            HashSet<Integer> hashSet = new HashSet<>();
            while (index < nums.length) {
                if (hashSet.contains(nums[index])) {
                    return true;
                }
                hashSet.add(nums[index]);
                if (index >= k) {
                    hashSet.remove(nums[index - k]);
                }
                index++;
            }
            return false;
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}
