//给定 pushed 和 popped 两个序列，每个序列中的 值都不重复，只有当它们可能是在最初空栈上进行的推入 push 和弹出 pop 操作序列的结果时
//，返回 true；否则，返回 false 。 
//
// 
//
// 示例 1： 
//
// 
//输入：pushed = [1,2,3,4,5], popped = [4,5,3,2,1]
//输出：true
//解释：我们可以按以下顺序执行：
//push(1), push(2), push(3), push(4), pop() -> 4,
//push(5), pop() -> 5, pop() -> 3, pop() -> 2, pop() -> 1
// 
//
// 示例 2： 
//
// 
//输入：pushed = [1,2,3,4,5], popped = [4,3,5,1,2]
//输出：false
//解释：1 不能在 2 之前弹出。
// 
//
// 
//
// 提示： 
//
// 
// 1 <= pushed.length <= 1000 
// 0 <= pushed[i] <= 1000 
// pushed 的所有元素 互不相同 
// popped.length == pushed.length 
// popped 是 pushed 的一个排列 
// 
//
// Related Topics 栈 数组 模拟 👍 258 👎 0


package com.coachhe.leetcode.editor.cn.栈.P946_验证栈序列_中等;

import org.testng.annotations.Test;

import java.util.Stack;

/**
 * 验证栈序列
 * @author CoachHe
 * @date 2022-08-31 00:41:42
 */
public class P946_ValidateStackSequences{
	 @Test
	 public void test() {
	 	 //测试代码
	 	 Solution solution = new P946_ValidateStackSequences().new Solution();
		 int[] pushed = new int[]{1, 2, 3, 4, 5};
		 int[] popped = new int[]{4, 5, 3, 2, 1};
//		 int[] popped = new int[]{4, 3, 5, 1, 2};
//		 int[] pushed = new int[]{1, 0};
//		 int[] popped = new int[]{1, 0};
		 System.out.println(solution.validateStackSequences(pushed, popped));
	 }
	 
//力扣代码
//leetcode submit region begin(Prohibit modification and deletion)
class Solution {
    public boolean validateStackSequences(int[] pushed, int[] popped) {
		Stack<Integer> pushStack = new Stack<>();
		int pushIndex = 0;
		int popIndex = 0;
		// 如果push数组的index遍历结束之后pushStack不为空，那么意味着可以继续比较，只要弹出的过程中所有值和pop数组值能对应即可
		while (pushIndex < pushed.length || !pushStack.isEmpty()) {
			// 如果push数组已经为空并且还没遍历结束，那么往里面添加一个元素
			if (pushStack.isEmpty()) {
				pushStack.push(pushed[pushIndex++]);
			}
			// 一直将栈顶元素和popped数组的第popIndex个元素进行比较，如果不同那么继续将pushed数组元素放入栈中，直到遇到相同或者遍历结束
			while (pushIndex < pushed.length && pushStack.peek() != popped[popIndex]) {
				pushStack.push(pushed[pushIndex++]);
			}
			// 无论何种情况结束上面的一个while循环，当前栈顶的元素都应该和popped数组遍历到的元素相同，如果不相同那一定是有问题的。
			if (pushStack.peek() != popped[popIndex]) {
				return false;
			}
			// 通过了上面的所有判断，那么将数组二的index右移，栈顶元素弹出
			pushStack.pop();
			popIndex++;
		}
		return popIndex == popped.length;
    }
}
//leetcode submit region end(Prohibit modification and deletion)

}
