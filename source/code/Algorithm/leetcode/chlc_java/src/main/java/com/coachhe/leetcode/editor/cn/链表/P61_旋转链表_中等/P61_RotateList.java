//给你一个链表的头节点 head ，旋转链表，将链表每个节点向右移动 k 个位置。 
//
// 
//
// 示例 1： 
// 
// 
//输入：head = [1,2,3,4,5], k = 2
//输出：[4,5,1,2,3]
// 
//
// 示例 2： 
// 
// 
//输入：head = [0,1,2], k = 4
//输出：[2,0,1]
// 
//
// 
//
// 提示： 
//
// 
// 链表中节点的数目在范围 [0, 500] 内 
// -100 <= Node.val <= 100 
// 0 <= k <= 2 * 10⁹ 
// 
//
// Related Topics 链表 双指针 👍 814 👎 0


package com.coachhe.leetcode.editor.cn.链表.P61_旋转链表_中等;

import com.coachhe.leetcode.editor.cn.链表.ListNode;
import org.testng.annotations.Test;

/**
 * 旋转链表
 * @author CoachHe
 * @date 2022-08-13 15:54:54
 */
public class P61_RotateList{
	 @Test
	 public void test() {
	 	 //测试代码
	 	 Solution solution = new P61_RotateList().new Solution();
//		 ListNode list = new ListNode(1);
//		 list.next = new ListNode(2);
//		 list.next.next = new ListNode(3);
//		 list.next.next.next = new ListNode(4);
//		 list.next.next.next.next = new ListNode(5);
		 ListNode list = new ListNode(0);
		 list.next = new ListNode(1);
		 list.next.next = new ListNode(2);
//		 ListNode list = new ListNode(1);
//		 list.next = new ListNode(2);
//		 list.next.next = new ListNode(3);
		 ListNode resList = solution.rotateRight(list, 4);
		 while (resList != null) {
			 System.out.println(resList.val);
			 resList = resList.next;
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
    public ListNode rotateRight(ListNode head, int k) {
		if (head == null || head.next == null) {
			return head;
		}
		ListNode remNode = head;
		int length = 1;
		while (remNode.next != null) {
			length++;
			remNode = remNode.next;
		}
		int rotateNum = k % length;
		for (int i = 0; i < rotateNum; i++) {
			head = rotateOnce(head);
		}
		return head;
    }

	public ListNode rotateOnce(ListNode head) {
		if (head == null || head.next == null) {
			return head;
		}
		ListNode resList = head.next;
		ListNode afterList = head;
		while (head.next.next != null) {
			head = head.next;
		}
		// 此时head.next.next 为空，代表结尾了，那么head.next就是链表的最后一个值
		ListNode headNode = head.next;
		head.next = null;
		headNode.next = afterList;

		return headNode;
	}
}
//leetcode submit region end(Prohibit modification and deletion)

}
