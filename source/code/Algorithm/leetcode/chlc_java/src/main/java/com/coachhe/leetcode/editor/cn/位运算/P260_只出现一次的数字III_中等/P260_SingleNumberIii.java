//给定一个整数数组 nums，其中恰好有两个元素只出现一次，其余所有元素均出现两次。 找出只出现一次的那两个元素。你可以按 任意顺序 返回答案。 
//
// 
//
// 进阶：你的算法应该具有线性时间复杂度。你能否仅使用常数空间复杂度来实现？ 
//
// 
//
// 示例 1： 
//
// 
//输入：nums = [1,2,1,3,2,5]
//输出：[3,5]
//解释：[5, 3] 也是有效的答案。
// 
//
// 示例 2： 
//
// 
//输入：nums = [-1,0]
//输出：[-1,0]
// 
//
// 示例 3： 
//
// 
//输入：nums = [0,1]
//输出：[1,0]
// 
//
// 提示： 
//
// 
// 2 <= nums.length <= 3 * 10⁴ 
// -2³¹ <= nums[i] <= 2³¹ - 1 
// 除两个只出现一次的整数外，nums 中的其他数字都出现两次 
// 
//
// Related Topics 位运算 数组 👍 636 👎 0


package com.coachhe.leetcode.editor.cn.位运算.P260_只出现一次的数字III_中等;

import org.testng.annotations.Test;

import java.util.Arrays;

/**
 * 只出现一次的数字 III
 * @author CoachHe
 * @date 2022-08-04 01:22:31
 */
public class P260_SingleNumberIii{
	 @Test
	 public void test() {
	 	 //测试代码
	 	 Solution solution = new P260_SingleNumberIii().new Solution();
		 System.out.println(Arrays.toString(solution.singleNumber(new int[]{1, 2, 3, 3, 4, 4, 5, 5})));
	 }
	 
//力扣代码
//leetcode submit region begin(Prohibit modification and deletion)
class Solution {
    public int[] singleNumber(int[] nums) {
		int[] res = new int[2];
		int res1 = 0;
		// 此时得到两个只出现一次的数字的异或
		for (int num : nums){
			res1 ^= num;
		}

		// 此时res1位两个只出现一次的数字的异或的结果，因为两个数字不相等，因此肯定不能为0
		// 既然不为0，那么用2进制表示则一定有1存在，因此我们找到最右边的那个1，这个1是两个异或
		// 的对应位数一定不相同的（比如res1的那位为0，res2的那位为1），那么我们找到所有那位为0的，
		// 异或之后就得到res1了

		// 这个midNum为一个类似0000100这样的数，只有一个1存在，其他都是0
		int midNum = res1 & (~res1 + 1);
		int res2 = 0;
		for (int num : nums) {
			if ((num & midNum) == 0) {
				res2 ^= num;
			}
		}

		res1 = res1 ^ res2;

		res[0] = res1;
		res[1] = res2;

		return res;
    }
}
//leetcode submit region end(Prohibit modification and deletion)

}
