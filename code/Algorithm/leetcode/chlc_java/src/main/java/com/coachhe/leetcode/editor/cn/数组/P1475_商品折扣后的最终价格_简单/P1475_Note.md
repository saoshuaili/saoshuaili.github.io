# 笔记

这题有两种做法，第一种很简单，没啥好说的，遍历两次，复杂度`n*n`，就不说了。

这题如果真正出现在面试中肯定是会问单调栈的方式。

对于单调栈，一个比较坑的点就是需要考虑到底是从左往右还是从右往左计算，
这题我一开始就从左往右看，看了很久看不出来，后来想到应该需要使用从右往左的方式来操作。

直接上代码，在代码中解释吧。

```java
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

```