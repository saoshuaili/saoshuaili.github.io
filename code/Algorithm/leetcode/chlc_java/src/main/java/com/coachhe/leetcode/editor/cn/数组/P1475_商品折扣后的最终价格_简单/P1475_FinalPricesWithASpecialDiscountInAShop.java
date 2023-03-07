//给你一个数组 prices ，其中 prices[i] 是商店里第 i 件商品的价格。 
//
// 商店里正在进行促销活动，如果你要买第 i 件商品，那么你可以得到与 prices[j] 相等的折扣，其中 j 是满足 j > i 且 prices[j] 
//<= prices[i] 的 最小下标 ，如果没有满足条件的 j ，你将没有任何折扣。 
//
// 请你返回一个数组，数组中第 i 个元素是折扣后你购买商品 i 最终需要支付的价格。 
//
// 
//
// 示例 1： 
//
// 输入：prices = [8,4,6,2,3]
//输出：[4,2,4,2,3]
//解释：
//商品 0 的价格为 price[0]=8 ，你将得到 prices[1]=4 的折扣，所以最终价格为 8 - 4 = 4 。
//商品 1 的价格为 price[1]=4 ，你将得到 prices[3]=2 的折扣，所以最终价格为 4 - 2 = 2 。
//商品 2 的价格为 price[2]=6 ，你将得到 prices[3]=2 的折扣，所以最终价格为 6 - 2 = 4 。
//商品 3 和 4 都没有折扣。
// 
//
// 示例 2： 
//
// 输入：prices = [1,2,3,4,5]
//输出：[1,2,3,4,5]
//解释：在这个例子中，所有商品都没有折扣。
// 
//
// 示例 3： 
//
// 输入：prices = [10,1,1,6]
//输出：[9,0,1,6]
// 
//
// 
//
// 提示： 
//
// 
// 1 <= prices.length <= 500 
// 1 <= prices[i] <= 10^3 
// 
//
// Related Topics 栈 数组 单调栈 👍 81 👎 0


package com.coachhe.leetcode.editor.cn.数组.P1475_商品折扣后的最终价格_简单;

import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Stack;

/**
 * 商品折扣后的最终价格
 *
 * @author CoachHe
 * @date 2022-09-01 01:10:37
 */
public class P1475_FinalPricesWithASpecialDiscountInAShop {
    @Test
    public void test() {
        //测试代码
        Solution solution = new P1475_FinalPricesWithASpecialDiscountInAShop().new Solution();
		 int[] arr = new int[]{8, 4, 6, 2, 3};
//		 int[] arr = new int[]{1, 2, 3, 4, 5};
//		 int[] arr = new int[]{10, 1, 1, 6};
//        int[] arr = new int[]{4, 7, 1, 9, 4, 8, 8, 9, 4};
        System.out.println(Arrays.toString(solution.finalPrices(arr)));
    }

    //力扣代码
//leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public int[] finalPrices(int[] prices) {
            if (prices == null || prices.length == 0) {
                return prices;
            }

            // 最终的返回数组，里面是减少过折扣的价格
            int[] resArr = new int[prices.length];
            // 单调栈
            Stack<Integer> stack = new Stack<>();

            // 首先往栈中放入最右边的元素
            stack.push(prices[prices.length - 1]);
            // 对于返回数组中最右边的元素，因为它右边已经没有数字了，因此肯定没有折扣，将原价放入
            resArr[prices.length - 1] = prices[prices.length - 1];

            // 从右往左遍历数组中的所有数字
            for (int i = prices.length - 2; i >= 0; i--) {

                // 对于单调栈，其栈顶的元素一定是目前遍历到的所有数中最靠近遍历到的数字的最大的数字
                // 因此如果栈顶元素比遍历到的数字更大，那么这个数字就没用了，直接丢掉
                while (!stack.isEmpty() && stack.peek() > prices[i]) {
                    stack.pop();
                }
                // 否则判断是否栈中已经没有元素了，如果没有了，说明数组右边已经没有比当前遍历到的数字更小的数字了
                // 那么将这个位置的数字置为当前数字
                resArr[i] = stack.isEmpty() ? prices[i] : prices[i] - stack.peek();

                // 此时无论栈中的数字是否为空，都需要将当前数放入栈中
                stack.push(prices[i]);

            }

            return resArr;
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}
