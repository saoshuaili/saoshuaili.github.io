package com.coachhe.剑指offer.editor.cn.code.排序;

import org.testng.annotations.Test;

/**
 * @author CoachHe
 * @date 2022/8/6 13:57
 **/
public class P51_数组中的逆序对 {
    @Test
    public void test() {
        //测试代码
        Solution solution = new Solution();
//		 System.out.println(solution.reversePairs(new int[]{1,3,2,3,1}));
//		 System.out.println(solution.reversePairs(new int[]{2, 4, 3, 5, 1}));
//		 System.out.println(solution.reversePairs(new int[]{7,5,6,4}));
        System.out.println(solution.reversePairs(new int[]{2147483647, 2147483647, -2147483647, -2147483647, -2147483647, 2147483647}));
//		 System.out.println(solution.reversePairs(new int[]{5,4,3,2,1}));
    }

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

            int[] help = new int[right - left + 1];
            int i = 0;
            while (i <= help.length) {
                if (nums[leftIndex] > nums[rightIndex]) {
                    help[i++] = nums[rightIndex++];
                    tmpSum++;
                } else if (leftIndex < mid) {
                    help[i++] = nums[leftIndex++];
                    tmpSum += (rightIndex - mid - 1);
                } else {
                    help[i++] = nums[leftIndex++];
                }
                if (rightIndex > right) {
                    while (leftIndex < mid) {
                        help[i++] = nums[leftIndex++];
                        tmpSum += (right - mid);
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
}
