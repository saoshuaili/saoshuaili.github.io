//给定一个无序的数组 nums，返回 数组在排序之后，相邻元素之间最大的差值 。如果数组元素个数小于 2，则返回 0 。 
//
// 您必须编写一个在「线性时间」内运行并使用「线性额外空间」的算法。 
//
// 
//
// 示例 1: 
//
// 
//输入: nums = [3,6,9,1]
//输出: 3
//解释: 排序后的数组是 [1,3,6,9], 其中相邻元素 (3,6) 和 (6,9) 之间都存在最大差值 3。 
//
// 示例 2: 
//
// 
//输入: nums = [10]
//输出: 0
//解释: 数组元素个数小于 2，因此返回 0。 
//
// 
//
// 提示: 
//
// 
// 1 <= nums.length <= 10⁵ 
// 0 <= nums[i] <= 10⁹ 
// 
//
// Related Topics 数组 桶排序 基数排序 排序 👍 505 👎 0


package com.coachhe.leetcode.editor.cn.排序.桶排序.P164_最大间距_困难;

import org.testng.annotations.Test;

/**
 * 最大间距
 * @author CoachHe
 * @date 2022-08-12 00:27:41
 */
public class P164_MaximumGap{
	 @Test
	 public void test() {
	 	 //测试代码
	 	 Solution solution = new P164_MaximumGap().new Solution();
	 }
	 
//力扣代码
//leetcode submit region begin(Prohibit modification and deletion)
class Solution {
    public int maximumGap(int[] nums) {
		return 0;
    }
}
//leetcode submit region end(Prohibit modification and deletion)

}
