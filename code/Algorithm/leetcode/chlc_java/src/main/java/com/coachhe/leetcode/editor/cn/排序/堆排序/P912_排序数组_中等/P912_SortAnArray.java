//给你一个整数数组 nums，请你将该数组升序排列。 
//
// 
//
// 
// 
//
// 示例 1： 
//
// 
//输入：nums = [5,2,3,1]
//输出：[1,2,3,5]
// 
//
// 示例 2： 
//
// 
//输入：nums = [5,1,1,2,0,0]
//输出：[0,0,1,1,2,5]
// 
//
// 
//
// 提示： 
//
// 
// 1 <= nums.length <= 5 * 10⁴ 
// -5 * 10⁴ <= nums[i] <= 5 * 10⁴ 
// 
//
// Related Topics 数组 分治 桶排序 计数排序 基数排序 排序 堆（优先队列） 归并排序 👍 635 👎 0


package com.coachhe.leetcode.editor.cn.排序.堆排序.P912_排序数组_中等;

import org.testng.annotations.Test;

import java.util.Arrays;

/**
 * 排序数组
 * @author CoachHe
 * @date 2022-08-07 19:53:33
 */
public class P912_SortAnArray {
	 @Test
	 public void test() {
	 	 //测试代码
	 	 Solution solution = new P912_SortAnArray().new Solution();
//		 int[] nums = new int[]{5, 1, 1, 2, 0, 0};
//		 int[] nums = new int[]{-4, 0, 7, 4, 9, -5, -1, 0, -7, -1};
		 int[] nums = new int[]{-74, 48, -20, 2, 10, -84, -5, -9, 11, -24, -91, 2, -71, 64, 63, 80, 28, -30, -58, -11, -44, -87, -22, 54, -74, -10, -55, -28, -46, 29, 10, 50, -72, 34, 26, 25, 8, 51, 13, 30, 35, -8, 50, 65, -6, 16, -2, 21, -78, 35, -13, 14, 23, -3, 26, -90, 86, 25, -56, 91, -13, 92, -25, 37, 57, -20, -69, 98, 95, 45, 47, 29, 86, -28, 73, -44, -46, 65, -84, -96, -24, -12, 72, -68, 93, 57, 92, 52, -45, -2, 85, -63, 56, 55, 12, -85, 77, -39};
		 for (int i = 0; i < nums.length; i++) {
			 solution.heapinsert(nums, i);
		 }
		 System.out.println(Arrays.toString(nums));

		 int[] nonHeap = new int[]{2, 5, 3, 1, 1, 1, 0};
		 solution.heapify(nonHeap, 0, 6);
		 System.out.println(Arrays.toString(nonHeap));

//		 int[] testHeap = new int[]{5, 1, 1, 2, 0, 0};
//		 int[] testHeap = new int[]{-4, 0, 7, 4, 9, -5, -1, 0, -7, -1};
		 int[] testHeap = new int[]{-74, 48, -20, 2, 10, -84, -5, -9, 11, -24, -91, 2, -71, 64, 63, 80, 28, -30, -58, -11, -44, -87, -22, 54, -74, -10, -55, -28, -46, 29, 10, 50, -72, 34, 26, 25, 8, 51, 13, 30, 35, -8, 50, 65, -6, 16, -2, 21, -78, 35, -13, 14, 23, -3, 26, -90, 86, 25, -56, 91, -13, 92, -25, 37, 57, -20, -69, 98, 95, 45, 47, 29, 86, -28, 73, -44, -46, 65, -84, -96, -24, -12, 72, -68, 93, 57, 92, 52, -45, -2, 85, -63, 56, 55, 12, -85, 77, -39};
		 solution.sortArray(testHeap);
		 System.out.println(Arrays.toString(testHeap));
	 }
	 
//力扣代码
//leetcode submit region begin(Prohibit modification and deletion)
class Solution {
    public int[] sortArray(int[] nums) {
		for (int i = 0; i < nums.length; i++) {
			heapinsert(nums, i);
		}
		int heapSize = nums.length;
		int[] helpArr = new int[nums.length];
		while (heapSize > 0) {
			helpArr[heapSize - 1] = nums[0];
			swap(nums, 0, heapSize - 1);
			heapify(nums, 0, heapSize - 1);
			heapSize--;
		}
		return helpArr;
    }

	public void heapinsert(int[] nums, int index) {
		if (index < 0 || index >= nums.length) {
			return;
		}
		int parentIndex = (index - 1) / 2;
		while (nums[index] > nums[parentIndex]) {
			swap(nums, index, parentIndex);
			index = parentIndex;
			parentIndex = (index - 1) / 2;
		}
	}

	// nums原数组
	// index指的是从哪个位置开始做heapify
	// heapSize用来判断是否越界
	public void heapify(int[] nums, int index, int heapSize) {
		int leftChild = (2 * index) + 1;
		while (leftChild < heapSize) {
			int largestIndex = leftChild + 1 < heapSize && nums[leftChild + 1] > nums[leftChild] ?
					leftChild + 1 : leftChild;
			int largestValue = nums[largestIndex];
			if (nums[index] < largestValue) {
				swap(nums, index, largestIndex);
				index = largestIndex;
				leftChild = (2 * index) + 1;
			} else {
				break;
			}
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
