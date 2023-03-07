//峰值元素是指其值严格大于左右相邻值的元素。 
//
// 给你一个整数数组 nums，找到峰值元素并返回其索引。数组可能包含多个峰值，在这种情况下，返回 任何一个峰值 所在位置即可。 
//
// 你可以假设 nums[-1] = nums[n] = -∞ 。 
//
// 你必须实现时间复杂度为 O(log n) 的算法来解决此问题。 
//
// 
//
// 示例 1： 
//
// 
//输入：nums = [1,2,3,1]
//输出：2
//解释：3 是峰值元素，你的函数应该返回其索引 2。 
//
// 示例 2： 
//
// 
//输入：nums = [1,2,1,3,5,6,4]
//输出：1 或 5 
//解释：你的函数可以返回索引 1，其峰值元素为 2；
//     或者返回索引 5， 其峰值元素为 6。
// 
//
// 
//
// 提示： 
//
// 
// 1 <= nums.length <= 1000 
// -2³¹ <= nums[i] <= 2³¹ - 1 
// 对于所有有效的 i 都有 nums[i] != nums[i + 1] 
// 
//
// Related Topics 数组 二分查找 👍 868 👎 0


package com.coachhe.leetcode.editor.cn.二分法.P162_寻找峰值;

import org.testng.annotations.Test;

/**
 * 寻找峰值
 *
 * @author CoachHe
 * @date 2022-08-05 00:50:21
 */
public class P162_FindPeakElement {
    @Test
    public void test() {
        //测试代码
        Solution solution = new P162_FindPeakElement().new Solution();
        System.out.println(solution.findPeakElement(new int[]{1, 2, 3, 4, 5, 4}));
    }

    //力扣代码
//leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public int findPeakElement(int[] nums) {
            if (nums.length <= 1) {
                return 0;
            }
            if (nums[0] > nums[1]) {
                return 0;
            }
            if (nums[nums.length - 1] > nums[nums.length - 2]) {
                return nums.length - 1;
            }
            return findPartPeakElement(nums, 0, nums.length - 1);
        }

        // 思路
        // 二分法，找到中间那个数，判断周围变化趋势，从而判断是否是局部最大
        public int findPartPeakElement(int[] nums, int left, int right) {
            if (left == right) {
                return nums[left];
            }
            // 获取中间索引
            int mid = left + ((right - left) >> 1);
            if (nums[mid] > nums[mid - 1] && nums[mid] > nums[mid + 1]) {
                return mid;
            } else if (nums[mid] > nums[mid - 1]) {
                return findPartPeakElement(nums, mid, right);
            } else {
                return findPartPeakElement(nums, left, mid);
            }
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}
