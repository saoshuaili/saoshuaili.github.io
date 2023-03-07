//给你一个整数数组 nums ，按要求返回一个新数组 counts 。数组 counts 有该性质： counts[i] 的值是 nums[i] 右侧小于 
//nums[i] 的元素的数量。 
//
// 
//
// 示例 1： 
//
// 
//输入：nums = [5,2,6,1]
//输出：[2,1,1,0] 
//解释：
//5 的右侧有 2 个更小的元素 (2 和 1)
//2 的右侧仅有 1 个更小的元素 (1)
//6 的右侧有 1 个更小的元素 (1)
//1 的右侧有 0 个更小的元素
// 
//
// 示例 2： 
//
// 
//输入：nums = [-1]
//输出：[0]
// 
//
// 示例 3： 
//
// 
//输入：nums = [-1,-1]
//输出：[0,0]
// 
//
// 
//
// 提示： 
//
// 
// 1 <= nums.length <= 10⁵ 
// -10⁴ <= nums[i] <= 10⁴ 
// 
//
// Related Topics 树状数组 线段树 数组 二分查找 分治 有序集合 归并排序 👍 856 👎 0


package com.coachhe.leetcode.editor.cn.排序.归并排序.P315_计算右侧小于当前元素的个数_困难;


import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 计算右侧小于当前元素的个数
 *
 * @author CoachHe
 * @date 2022-08-05 02:25:20
 */
public class P315_CountOfSmallerNumbersAfterSelf {
    @Test
    public void test() {
        //测试代码
        Solution solution = new P315_CountOfSmallerNumbersAfterSelf().new Solution();
        int[] nums = new int[]{5, 2, 6, 1};
        System.out.println(solution.countSmaller(nums));
        System.out.println(Arrays.toString(nums));
    }

    //力扣代码
//leetcode submit region begin(Prohibit modification and deletion)
    // 归并排序法解决
    class Solution {
        public List<Integer> countSmaller(int[] nums) {
            // 索引数组，用来查找数字的原始位置
            int[] indexArr = new int[nums.length];
            int[] countArr = new int[nums.length];
            // 给索引数组赋值
            for (int i = 0; i < nums.length; i++) {
                indexArr[i] = i;
            }

            countPartSmaller(nums, 0, nums.length - 1, indexArr, countArr);

            int[] res = new int[nums.length];
            System.arraycopy(countArr, 0, res, 0, nums.length);
            List<Integer> resList = new ArrayList<>();
            for (int i : res) {
                resList.add(i);
            }


            return resList;
        }

        public void countPartSmaller(int[] nums, int left, int right, int[] indexArr, int[] countArr) {

            if (left == right) {
                return;
            }

            int mid = left + ((right - left) >> 1);
            countPartSmaller(nums, left, mid, indexArr, countArr);
            countPartSmaller(nums, mid + 1, right, indexArr, countArr);

            int leftIndex = left;
            int rightIndex = mid + 1;
            int[] helpIndexArr = new int[right - left + 1];
            int[] helpArr = new int[right - left + 1];
            int j = 0;
            while (leftIndex <= mid || rightIndex <= right) {
                if (leftIndex > mid) {
                    while (rightIndex <= right) {
                        helpArr[j] = nums[rightIndex];
                        helpIndexArr[j++] = indexArr[rightIndex++];
                    }
                } else if (rightIndex > right) {
                    helpArr[j] = nums[leftIndex];
                    helpIndexArr[j++] = indexArr[leftIndex++];
                    while (leftIndex <= mid) {
                        helpArr[j] = nums[leftIndex];
                        countArr[indexArr[leftIndex]] += (right - mid);
                        helpIndexArr[j++] = indexArr[leftIndex++];
                    }
                } else if (nums[leftIndex] > nums[rightIndex]) {
                    countArr[indexArr[leftIndex]]++;
                    helpArr[j] = nums[rightIndex];
                    helpIndexArr[j++] = indexArr[rightIndex++];
                } else {
                    helpArr[j] = nums[leftIndex];
                    helpIndexArr[j++] = indexArr[leftIndex++];
                    if (leftIndex <= mid) {
                        countArr[indexArr[leftIndex]] += (rightIndex - mid - 1);
                    }
                }
            }
            System.arraycopy(helpIndexArr, 0, indexArr, left, helpIndexArr.length);
            System.arraycopy(helpArr, 0, nums, left, helpArr.length);

        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}
