//给你一个整数数组 nums 和两个整数 k 和 t 。请你判断是否存在 两个不同下标 i 和 j，使得 abs(nums[i] - nums[j]) <= 
//t ，同时又满足 abs(i - j) <= k 。 
//
// 如果存在则返回 true，不存在返回 false。 
//
// 
//
// 示例 1： 
//
// 
//输入：nums = [1,2,3,1], k = 3, t = 0
//输出：true 
//
// 示例 2： 
//
// 
//输入：nums = [1,0,1,1], k = 1, t = 2
//输出：true 
//
// 示例 3： 
//
// 
//输入：nums = [1,5,9,1,5,9], k = 2, t = 3
//输出：false 
//
// 
//
// 提示： 
//
// 
// 0 <= nums.length <= 2 * 10⁴ 
// -2³¹ <= nums[i] <= 2³¹ - 1 
// 0 <= k <= 10⁴ 
// 0 <= t <= 2³¹ - 1 
// 
//
// Related Topics 数组 桶排序 有序集合 排序 滑动窗口 👍 641 👎 0


package com.coachhe.leetcode.editor.cn.排序.桶排序.P220_存在重复元素III_中等;

import org.testng.annotations.Test;

/**
 * 存在重复元素 III
 * @author CoachHe
 * @date 2022-08-10 00:08:00
 */
public class P220_ContainsDuplicateIii{
	 @Test
	 public void test() {
	 	 //测试代码
	 	 Solution solution = new P220_ContainsDuplicateIii().new Solution();
	 }
	 
//力扣代码
//leetcode submit region begin(Prohibit modification and deletion)
class Solution {
    public boolean containsNearbyAlmostDuplicate(int[] nums, int k, int t) {
		return false;
    }
}
//leetcode submit region end(Prohibit modification and deletion)

}
