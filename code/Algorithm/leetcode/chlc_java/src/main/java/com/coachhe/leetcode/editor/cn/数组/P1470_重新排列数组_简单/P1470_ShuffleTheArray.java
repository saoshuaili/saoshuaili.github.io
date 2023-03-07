//给你一个数组 nums ，数组中有 2n 个元素，按 [x1,x2,...,xn,y1,y2,...,yn] 的格式排列。 
//
// 请你将数组按 [x1,y1,x2,y2,...,xn,yn] 格式重新排列，返回重排后的数组。 
//
// 
//
// 示例 1： 
//
// 输入：nums = [2,5,1,3,4,7], n = 3
//输出：[2,3,5,4,1,7] 
//解释：由于 x1=2, x2=5, x3=1, y1=3, y2=4, y3=7 ，所以答案为 [2,3,5,4,1,7]
// 
//
// 示例 2： 
//
// 输入：nums = [1,2,3,4,4,3,2,1], n = 4
//输出：[1,4,2,3,3,2,4,1]
// 
//
// 示例 3： 
//
// 输入：nums = [1,1,2,2], n = 2
//输出：[1,2,1,2]
// 
//
// 
//
// 提示： 
//
// 
// 1 <= n <= 500 
// nums.length == 2n 
// 1 <= nums[i] <= 10^3 
// 
//
// Related Topics 数组 👍 94 👎 0


package com.coachhe.leetcode.editor.cn.数组.P1470_重新排列数组_简单;

import org.testng.annotations.Test;

import java.util.Arrays;

/**
 * 重新排列数组
 *
 * @author CoachHe
 * @date 2022-08-29 00:50:44
 */
public class P1470_ShuffleTheArray {
    @Test
    public void test() {
        //测试代码
        Solution solution = new P1470_ShuffleTheArray().new Solution();
        int[] nums = new int[]{1, 2, 3, 4, 5, 6};
        System.out.println(Arrays.toString(solution.shuffle(nums, 3)));
    }

    //力扣代码
//leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        // 方法1，直接创建一个新数组
//    public int[] shuffle(int[] nums, int n) {
//		int[] resResult = new int[2 * n];
//		int xIndex = 0;
//		int yIndex = n;
//		int i = 0;
//		while (i < 2 * n) {
//			resResult[i++] = nums[xIndex++];
//			resResult[i++] = nums[yIndex++];
//		}
//		return resResult;
//    }


		// 2. 减少空间复杂度的方式
        public int[] shuffle(int[] nums, int n) {
			int xIndex = 0;
			int yIndex = n;
			int i = 0;
			while (i < 2 * n) {
				nums[i++] |= (nums[xIndex++] & 1023) << 10;
				nums[i++] |= (nums[yIndex++] & 1023) << 10;
			}
			i = 0;
			while (i < 2 * n){
				nums[i++] >>= 10;
			}
            return nums;
        }
//leetcode submit region end(Prohibit modification and deletion)

    }

}