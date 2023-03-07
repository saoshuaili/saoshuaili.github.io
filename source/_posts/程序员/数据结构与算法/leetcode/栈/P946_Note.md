# 思路

既然是从最初的空栈上进行push和pull上得到的结果。那么很明显我们可以模拟入栈和出栈的过程来判断是否符合要求。

例如对于数组 `[1,2,3,4,5]` 和 `[4,5,3,2,1]`

我们准备两个指针，分别代表两个数组从左到右遍历到的位置。

因为题目给定的条件是两个数组中不会有重复的元素，那么意味着在第一个数组遍历元素、将每个元素入栈的时候，一旦栈顶这个元素值和另一个数组当前遍历到的值相同，那么必须马上弹出。

所以我们一直遍历第一个数组，往栈中加元素，直到栈顶元素和数组二当前值相同，那么栈中的数据弹出，数组二往后走一位。

遍历完数组1之后，判断栈是否为空，如果不为空那么继续比较，否则如果数组2的遍历指针没有走完，那么是有问题的。

代码：

```java
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

```