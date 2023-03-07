//给定一个包含红色、白色和蓝色、共 n 个元素的数组
// nums ，原地对它们进行排序，使得相同颜色的元素相邻，并按照红色、白色、蓝色顺序排列。 
//
// 我们使用整数 0、 1 和 2 分别表示红色、白色和蓝色。 
//
// 
// 
//
// 必须在不使用库的sort函数的情况下解决这个问题。 
//
// 
//
// 示例 1： 
//
// 
//输入：nums = [2,0,2,1,1,0]
//输出：[0,0,1,1,2,2]
// 
//
// 示例 2： 
//
// 
//输入：nums = [2,0,1]
//输出：[0,1,2]
// 
//
// 
//
// 提示： 
//
// 
// n == nums.length 
// 1 <= n <= 300 
// nums[i] 为 0、1 或 2 
// 
//
// 
//
// 进阶： 
//
// 
// 你可以不使用代码库中的排序函数来解决这道题吗？ 
// 你能想出一个仅使用常数空间的一趟扫描算法吗？ 
// 
//
// Related Topics 数组 双指针 排序 👍 1360 👎 0


package com.coachhe.leetcode.editor.cn.排序.快速排序.P75_颜色分类_中等;

import org.testng.annotations.Test;

import java.util.Arrays;

/**
 * 颜色分类
 * @author CoachHe
 * @date 2022-08-05 10:55:28
 */
public class P75_SortColors{
	 @Test
	 public void test() {
	 	 //测试代码
	 	 Solution solution = new P75_SortColors().new Solution();
//		 int[] nums = new int[]{1,1,1,0,2,2,0,1,2};
		 int[] nums = new int[]{2,2};
		 solution.sortColors(nums);
		 System.out.println(Arrays.toString(nums));
	 }
	 
//力扣代码
//leetcode submit region begin(Prohibit modification and deletion)
class Solution {
    public void sortColors(int[] nums) {
		if (nums.length <= 1) {
			return;
		}
		// 以1位基准线做一次排序，比1小的放在左边界以内，比1大的放在右边界以内
		int leftBound = 0;
		int rightBound = nums.length - 1;
		int i = 0;
		while (i <= rightBound) {
			if (nums[i] < 1) {
				swap(nums, leftBound++, i++);
			} else if (nums[i] == 1) {
				i++;
			} else {
				swap(nums, rightBound--, i);
			}
		}
    }

	public void swap(int[] arr, int left, int right) {
		// 若左边界=右边界，直接返回
		if (left == right) {
			return;
		}
		arr[left] = arr[left] ^ arr[right];
		arr[right] = arr[left] ^ arr[right];
		arr[left] = arr[left] ^ arr[right];
	}
}
//leetcode submit region end(Prohibit modification and deletion)

}
