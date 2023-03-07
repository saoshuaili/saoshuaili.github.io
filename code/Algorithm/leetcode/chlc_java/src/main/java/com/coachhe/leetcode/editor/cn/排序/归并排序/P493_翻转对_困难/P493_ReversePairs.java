//给定一个数组 nums ，如果 i < j 且 nums[i] > 2*nums[j] 我们就将 (i, j) 称作一个重要翻转对。 
//
// 你需要返回给定数组中的重要翻转对的数量。 
//
// 示例 1: 
//
// 
//输入: [1,3,2,3,1]
//输出: 2
// 
//
// 示例 2: 
//
// 
//输入: [2,4,3,5,1]
//输出: 3
// 
//
// 注意: 
//
// 
// 给定数组的长度不会超过50000。 
// 输入数组中的所有数字都在32位整数的表示范围内。 
// 
//
// Related Topics 树状数组 线段树 数组 二分查找 分治 有序集合 归并排序 👍 366 👎 0


package com.coachhe.leetcode.editor.cn.排序.归并排序.P493_翻转对_困难;

import org.testng.annotations.Test;

/**
 * 翻转对
 * @author CoachHe
 * @date 2022-08-05 02:26:52
 */
public class P493_ReversePairs{
	 @Test
	 public void test() {
	 	 //测试代码
	 	 Solution solution = new P493_ReversePairs().new Solution();
//		 System.out.println(solution.reversePairs(new int[]{1,3,2,3,1}));
//		 System.out.println(solution.reversePairs(new int[]{2, 4, 3, 5, 1}));
//		 System.out.println(solution.reversePairs(new int[]{7,5,6,4}));
		 System.out.println(solution.reversePairs(new int[]{2147483647,2147483647,-2147483647,-2147483647,-2147483647,2147483647}));
//		 System.out.println(solution.reversePairs(new int[]{5,4,3,2,1}));
	 }
	 
//力扣代码
//leetcode submit region begin(Prohibit modification and deletion)
class Solution {
    public int reversePairs(int[] nums) {
		if (nums.length <= 1) {
			return 0;
		}
		return reversePartPairs(nums, 0, nums.length - 1);
    }

	public int reversePartPairs(int[] nums, int left, int right) {
		if (left == right) {
			return 0;
		}
		// 获取中点
		int mid = left + ((right - left) >> 1);
		int leftPair = reversePartPairs(nums, left, mid);
		int rightPair = reversePartPairs(nums, mid + 1, right);
		int leftIndex = left;
		int rightIndex = mid + 1;
		int tmpSum = leftPair + rightPair;
		// 这里计算，计算之后再排序
		while (leftIndex <= mid) {
			while (rightIndex <= right && (long) nums[leftIndex] > 2 * (long) nums[rightIndex]) {
				rightIndex++;
			}
			tmpSum += (rightIndex - mid - 1);
			leftIndex++;
		}


		leftIndex = left;
		rightIndex = mid + 1;
		int[] help = new int[right - left + 1];
		int i = 0;
		while(i <= help.length) {
			if (nums[leftIndex] > nums[rightIndex]) {
				help[i++] = nums[rightIndex++];
			} else if (leftIndex < mid){
				help[i++] = nums[leftIndex++];
			} else {
				help[i++] = nums[leftIndex++];
			}

			if (rightIndex > right) {
				while (leftIndex < mid) {
					help[i++] = nums[leftIndex++];
				}
				help[i] = nums[leftIndex];
				break;
			}
			if (leftIndex > mid) {
				while (rightIndex <= right) {
					help[i++] = nums[rightIndex++];
				}
				break;
			}
		}

		System.arraycopy(help, 0, nums, left, help.length);

		return tmpSum;
	}

}
//leetcode submit region end(Prohibit modification and deletion)

}
