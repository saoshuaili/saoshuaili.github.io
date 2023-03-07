//给你一个链表，两两交换其中相邻的节点，并返回交换后链表的头节点。你必须在不修改节点内部的值的情况下完成本题（即，只能进行节点交换）。 
//
// 
//
// 示例 1： 
// 
// 
//输入：head = [1,2,3,4]
//输出：[2,1,4,3]
// 
//
// 示例 2： 
//
// 
//输入：head = []
//输出：[]
// 
//
// 示例 3： 
//
// 
//输入：head = [1]
//输出：[1]
// 
//
// 
//
// 提示： 
//
// 
// 链表中节点的数目在范围 [0, 100] 内 
// 0 <= Node.val <= 100 
// 
//
// Related Topics 递归 链表 👍 1518 👎 0


package com.coachhe.leetcode.editor.cn.链表.P24_两两交换链表中的节点_中等;

import com.coachhe.leetcode.editor.cn.链表.ListNode;
import org.testng.annotations.Test;

/**
 * 两两交换链表中的节点
 * @author CoachHe
 * @date 2022-08-16 01:18:51
 */
public class P24_SwapNodesInPairs{
	 @Test
	 public void test() {
	 	 //测试代码
	 	 Solution solution = new P24_SwapNodesInPairs().new Solution();
		 ListNode head = new ListNode(1);
//		 head.next = new ListNode(2);
//		 head.next.next = new ListNode(3);
//		 head.next.next.next = new ListNode(4);
		 ListNode resHead = solution.swapPairs(head);
		 while (resHead != null) {
			 System.out.print(resHead.val + " ");
			 resHead = resHead.next;
		 }
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
    public ListNode swapPairs(ListNode head) {
		if (head == null) {
			return null;
		}
		ListNode dummy = new ListNode(0, head);
		ListNode curNode = dummy;
		while (head != null && head.next != null) {
			curNode.next = head.next;
			head.next = curNode.next.next;
			curNode.next.next = head;
			curNode = head;
			head = head.next;
		}
		return dummy.next;
    }
}
//leetcode submit region end(Prohibit modification and deletion)

}
