//给你一个整数数组 nums 以及两个整数 lower 和 upper 。求数组中，值位于范围 [lower, upper] （包含 lower 和 
//upper）之内的 区间和的个数 。 
//
// 区间和 S(i, j) 表示在 nums 中，位置从 i 到 j 的元素之和，包含 i 和 j (i ≤ j)。 
//
// 
//示例 1：
//
// 
//输入：nums = [-2,5,-1], lower = -2, upper = 2
//输出：3
//解释：存在三个区间：[0,0]、[2,2] 和 [0,2] ，对应的区间和分别是：-2 、-1 、2 。
// 
//
// 示例 2： 
//
// 
//输入：nums = [0], lower = 0, upper = 0
//输出：1
// 
//
// 
//
// 提示： 
//
// 
// 1 <= nums.length <= 10⁵ 
// -2³¹ <= nums[i] <= 2³¹ - 1 
// -10⁵ <= lower <= upper <= 10⁵ 
// 题目数据保证答案是一个 32 位 的整数 
// 
//
// Related Topics 树状数组 线段树 数组 二分查找 分治 有序集合 归并排序 👍 470 👎 0


package com.coachhe.leetcode.editor.cn.排序.归并排序.P327_区间和的个数_困难;

import org.testng.annotations.Test;

import java.util.Arrays;

/**
 * 区间和的个数
 *
 * @author CoachHe
 * @date 2022-08-05 02:27:07
 */
public class P327_CountOfRangeSum {
    @Test
    public void test() {
        //测试代码
        Solution solution = new P327_CountOfRangeSum().new Solution();
//        int[] nums = new int[]{0, 0};
        int[] nums = new int[]{2147483647, -2147483648, -1, 0};
//        int[] nums = new int[]{-3,1,2,-2,2,-1};
        System.out.println(solution.countRangeSum(nums, -2, 2));
        System.out.println(Arrays.toString(nums));
    }

    //力扣代码
//leetcode submit region begin(Prohibit modification and deletion)
    // 这题意思其实是找到所有累加和在upper和lower范围内的数组，因此我们先求出数组的累加和，然后用归并排序
    class Solution {
        public int countRangeSum(int[] nums, int lower, int upper) {
            long s = 0;
            long[] sums = new long[nums.length + 1];
            for (int i = 0; i < nums.length; ++i) {
                s += nums[i];
                sums[i + 1] = s;
            }

            return countRangeSumRecursive(sums, lower, upper, 0, sums.length - 1);
        }

        public int countRangeSumRecursive(long[] sum, int lower, int upper, int left, int right) {
            if (left == right) {
                return 0;
            }

//            int mid = left + ((right - left) >> 1);
            int mid = (left + right) / 2;
            int n1 = countRangeSumRecursive(sum, lower, upper, left, mid);
            int n2 = countRangeSumRecursive(sum, lower, upper, mid + 1, right);
            int ret = n1 + n2;

            int i = left;
            int l = mid + 1;
            int r = mid + 1;
            while (i <= mid) {
                while (l <= right && sum[l] - sum[i] < lower) {
                    l++;
                }
                while (r <= right && sum[r] - sum[i] <= upper) {
                    r++;
                }
                ret += r - l;
                i++;
            }

            long[] sorted = new long[right - left + 1];
            int leftIndex = left;
            int rightIndex = mid + 1;
            int p = 0;
            while (leftIndex <= mid || rightIndex <= right) {
                if (leftIndex > mid) {
                    sorted[p++] = sum[rightIndex++];
                } else if (rightIndex > right) {
                    sorted[p++] = sum[leftIndex++];
                } else if (sum[leftIndex] < sum[rightIndex]) {
                    sorted[p++] = sum[leftIndex++];
                } else {
                    sorted[p++] = sum[rightIndex++];
                }
            }

            System.arraycopy(sorted, 0, sum, left, sorted.length);

            return ret;

        }
//leetcode submit region end(Prohibit modification and deletion)

    }
}
