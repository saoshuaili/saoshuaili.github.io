//给定一个长度为 n 的链表 head 
//
// 对于列表中的每个节点，查找下一个 更大节点 的值。也就是说，对于每个节点，找到它旁边的第一个节点的值，这个节点的值 严格大于 它的值。 
//
// 返回一个整数数组 answer ，其中 answer[i] 是第 i 个节点( 从1开始 )的下一个更大的节点的值。如果第 i 个节点没有下一个更大的节点
//，设置 answer[i] = 0 。 
//
// 
//
// 示例 1： 
//
// 
//
// 
//输入：head = [2,1,5]
//输出：[5,5,0]
// 
//
// 示例 2： 
//
// 
//
// 
//输入：head = [2,7,4,3,5]
//输出：[7,0,5,5,0]
// 
//
// 
//
// 提示： 
//
// 
// 链表中节点数为 n 
// 1 <= n <= 10⁴ 
// 1 <= Node.val <= 10⁹ 
// 
//
// Related Topics 栈 数组 链表 单调栈 👍 208 👎 0


package com.coachhe.leetcode.editor.cn.链表.P1019_链表中的下一个更大节点_中等;

import com.coachhe.leetcode.editor.cn.链表.ListNode;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Stack;

/**
 * 链表中的下一个更大节点
 * @author CoachHe
 * @date 2022-08-14 13:49:14
 */
public class P1019_NextGreaterNodeInLinkedList{
	 @Test
	 public void test() {
	 	 //测试代码
	 	 Solution solution = new P1019_NextGreaterNodeInLinkedList().new Solution();
//		 ListNode head = new ListNode(2);
//		 head.next = new ListNode(7);
//		 head.next.next = new ListNode(4);
//		 head.next.next.next = new ListNode(3);
//		 head.next.next.next.next = new ListNode(5);
		 ListNode head = new ListNode(3);
		 head.next = new ListNode(3);
		 int[] resArr = solution.nextLargerNodes(head);
		 System.out.println(Arrays.toString(resArr));
	 }
	 
//力扣代码
//leetcode submit region begin(Prohibit modification and deletion)
/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode() {}
 *     ListNode(int val) { this.val = val; }
 *     ListNode(int val, ListNode next) { this.val = val; this.next = next; }
 * }
 */
class Solution {
    public int[] nextLargerNodes(ListNode head) {
		if (head == null || head.next == null) {
			return new int[]{0};
		}
		head = reverseList(head);
		ListNode remHead = head;
		int size = 0;
		while (remHead != null) {
			size++;
            remHead = remHead.next;
		}
		int[] resArr = new int[size];

		Stack<Integer> stack = new Stack<>();
		while (head != null) {
			if (stack.isEmpty()) {
				stack.push(head.val);
				resArr[size - 1] = 0;
			} else if (head.val >= stack.peek()) {
				while (!stack.isEmpty() && head.val >= stack.peek()) {
					stack.pop();
				}
				resArr[size - 1] = stack.isEmpty() ? 0 : stack.peek();
				stack.push(head.val);
			} else {
				resArr[size - 1] = stack.peek();
				stack.push(head.val);
			}
			size--;
			head = head.next;
		}
		return resArr;
    }

	public ListNode reverseList(ListNode head) {
		if (head == null || head.next == null) {
			return head;
		}
		ListNode nowNode = head.next;
		ListNode resNode = head;
		head.next = null;
		while (nowNode != null) {
			ListNode tmpNode = nowNode.next;
			nowNode.next = resNode;
			resNode = nowNode;
			nowNode = tmpNode;
		}
		return resNode;
	}



}
//leetcode submit region end(Prohibit modification and deletion)

}
