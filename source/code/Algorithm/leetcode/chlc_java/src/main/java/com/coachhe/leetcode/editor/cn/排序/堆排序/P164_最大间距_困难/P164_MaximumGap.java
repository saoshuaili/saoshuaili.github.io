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


package com.coachhe.leetcode.editor.cn.排序.堆排序.P164_最大间距_困难;

import org.testng.annotations.Test;

/**
 * 最大间距
 * @author CoachHe
 * @date 2022-08-10 00:08:30
 */
public class P164_MaximumGap{
	 @Test
	 public void test() {
	 	 //测试代码
	 	 Solution solution = new P164_MaximumGap().new Solution();
		 int[] nums = new int[]{3, 6, 9, 1};
		 System.out.println(solution.maximumGap(nums));
	 }
	 
//力扣代码
//leetcode submit region begin(Prohibit modification and deletion)
class Solution {
    public int maximumGap(int[] nums) {
		if (nums == null || nums.length <= 1) {
			return 0;
		}
		for (int i = 0; i < nums.length; i++) {
			heapinsert(nums, i);
		}
		int[] helpArr = new int[nums.length];
		int heapSize = nums.length - 1;
		while (heapSize >= 0) {
			helpArr[heapSize] = nums[0];
			swap(nums, 0, heapSize);
			heapify(nums, 0, heapSize);
			heapSize--;
		}
		int maxGap = Integer.MIN_VALUE;
		for (int i = 1; i < nums.length; i++) {
			maxGap = Math.max(maxGap, helpArr[i] - helpArr[i - 1]);
		}
		return maxGap;
    }
	public void heapify(int[] nums, int index, int heapSize) {
		int leftChild = index * 2 + 1;
		while (leftChild < heapSize) {
			int largestIndex = leftChild + 1 < heapSize && nums[leftChild] < nums[leftChild + 1] ?
					leftChild + 1 : leftChild;
			int largestValue = nums[largestIndex];
			if (largestValue < nums[index]) {
				break;
			}
			swap(nums, index, largestIndex);
			index = largestIndex;
			leftChild = index * 2 + 1;
		}
	}

	public void heapinsert(int[] nums, int heapSize) {
		int index = heapSize;
		while (nums[index] > nums[(index - 1) / 2]) {
			swap(nums, index, (index - 1) / 2);
			index = (index - 1) / 2;
		}

	}

	public void swap(int[] nums, int left, int right) {
		if (left == right) {
			return;
		}
		nums[left] = nums[left] ^ nums[right];
		nums[right] = nums[left] ^ nums[right];
		nums[left] = nums[left] ^ nums[right];
	}

}
//leetcode submit region end(Prohibit modification and deletion)

}
