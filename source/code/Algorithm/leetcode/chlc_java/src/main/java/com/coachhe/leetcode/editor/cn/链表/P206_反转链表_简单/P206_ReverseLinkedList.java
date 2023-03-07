//给你单链表的头节点 head ，请你反转链表，并返回反转后的链表。
//
// 
// 
// 
// 
// 
//
// 示例 1： 
// 
// 
//输入：head = [1,2,3,4,5]
//输出：[5,4,3,2,1]
// 
//
// 示例 2： 
// 
// 
//输入：head = [1,2]
//输出：[2,1]
// 
//
// 示例 3： 
//
// 
//输入：head = []
//输出：[]
// 
//
// 
//
// 提示： 
//
// 
// 链表中节点的数目范围是 [0, 5000] 
// -5000 <= Node.val <= 5000 
// 
//
// 
//
// 进阶：链表可以选用迭代或递归方式完成反转。你能否用两种方法解决这道题？ 
//
// Related Topics 递归 链表 👍 2691 👎 0


package com.coachhe.leetcode.editor.cn.链表.P206_反转链表_简单;

import com.coachhe.leetcode.editor.cn.链表.ListNode;
import org.testng.annotations.Test;

/**
 * 反转链表
 * @author CoachHe
 * @date 2022-08-13 15:55:34
 */
public class P206_ReverseLinkedList{
	 @Test
	 public void test() {
	 	 //测试代码
	 	 Solution solution = new P206_ReverseLinkedList().new Solution();
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
    public ListNode reverseList(ListNode head) {
		if (head == null || head.next == null) {
			return head;
		}
		ListNode nowNode = head.next;
		ListNode resHead = head;
		resHead.next = null;
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
