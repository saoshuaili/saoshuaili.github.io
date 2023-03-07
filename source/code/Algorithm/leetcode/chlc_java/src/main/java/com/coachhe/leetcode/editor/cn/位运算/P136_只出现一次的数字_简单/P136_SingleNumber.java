//给定一个非空整数数组，除了某个元素只出现一次以外，其余每个元素均出现两次。找出那个只出现了一次的元素。 
//
// 说明： 
//
// 你的算法应该具有线性时间复杂度。 你可以不使用额外空间来实现吗？ 
//
// 示例 1: 
//
// 输入: [2,2,1]
//输出: 1
// 
//
// 示例 2: 
//
// 输入: [4,1,2,1,2]
//输出: 4 
//
// Related Topics 位运算 数组 👍 2518 👎 0


package com.coachhe.leetcode.editor.cn.位运算.P136_只出现一次的数字_简单;

import org.testng.annotations.Test;

/**
 * 只出现一次的数字
 * @author CoachHe
 * @date 2022-08-04 01:12:58
 */
public class P136_SingleNumber{
	 @Test
	 public void test() {
	 	 //测试代码
	 	 Solution solution = new P136_SingleNumber().new Solution();
		 System.out.println(solution.singleNumber(new int[]{1,2,2,1,3}));
	 }
	 
//力扣代码
//leetcode submit region begin(Prohibit modification and deletion)
class Solution {
    public int singleNumber(int[] nums) {
		int res = 0;
		for (int num : nums) {
			res ^= num;
		}
		return res;
    }
}
//leetcode submit region end(Prohibit modification and deletion)

}
