//给你一个单链表的头节点 head ，请你判断该链表是否为回文链表。如果是，返回 true ；否则，返回 false 。 
//
// 
//
// 示例 1： 
// 
// 
//输入：head = [1,2,2,1]
//输出：true
// 
//
// 示例 2： 
// 
// 
//输入：head = [1,2]
//输出：false
// 
//
// 
//
// 提示： 
//
// 
// 链表中节点数目在范围[1, 10⁵] 内 
// 0 <= Node.val <= 9 
// 
//
// 
//
// 进阶：你能否用 O(n) 时间复杂度和 O(1) 空间复杂度解决此题？ 
//
// Related Topics 栈 递归 链表 双指针 👍 1471 👎 0


package com.coachhe.leetcode.editor.cn.链表.P234_回文链表_简单;

import com.coachhe.leetcode.editor.cn.链表.ListNode;
import org.testng.annotations.Test;

/**
 * 回文链表
 * @author CoachHe
 * @date 2022-08-13 15:57:44
 */
public class P234_PalindromeLinkedList{
	 @Test
	 public void test() {
	 	 //测试代码
	 	 Solution solution = new P234_PalindromeLinkedList().new Solution();
		 ListNode head = new ListNode(1);
		 head.next = new ListNode(2);
		 head.next.next = new ListNode(2);
		 head.next.next.next = new ListNode(1);
		 System.out.println(solution.isPalindrome(head));
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
	// 简单的全放栈里的方法就不展示了
    public boolean isPalindrome(ListNode head) {
		if (head == null || head.next == null) {
			return true;
		}
		ListNode slowPoint = head;
		ListNode remHead = head;
		ListNode fastPoint = head;
		while (fastPoint != null && fastPoint.next != null) {
			slowPoint = slowPoint.next;
			fastPoint = fastPoint.next.next;
			head = head.next;
		}
		ListNode reverseHead = reverseList(slowPoint);
		while (remHead != null && reverseHead != null) {
			if (remHead.val != reverseHead.val) {
				return false;
			}
			remHead = remHead.next;
			reverseHead = reverseHead.next;
		}
		return true;
    }

	public ListNode reverseList(ListNode head) {
		if (head == null || head.next == null) {
			return head;
		}
		ListNode nowNode = head.next;
		ListNode resHead = head;
		head.next = null;
		while (nowNode != null) {
			ListNode tmpNode = nowNode.next;
			nowNode.next = resHead;
			resHead = nowNode;
			nowNode = tmpNode;
		}
		return resHead;
	}
}
//leetcode submit region end(Prohibit modification and deletion)

}
