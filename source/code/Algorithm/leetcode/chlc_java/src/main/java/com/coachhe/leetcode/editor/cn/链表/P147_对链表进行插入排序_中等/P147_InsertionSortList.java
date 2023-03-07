//给定单个链表的头
// head ，使用 插入排序 对链表进行排序，并返回 排序后链表的头 。 
//
// 插入排序 算法的步骤: 
//
// 
// 插入排序是迭代的，每次只移动一个元素，直到所有元素可以形成一个有序的输出列表。 
// 每次迭代中，插入排序只从输入数据中移除一个待排序的元素，找到它在序列中适当的位置，并将其插入。 
// 重复直到所有输入数据插入完为止。 
// 
//
// 下面是插入排序算法的一个图形示例。部分排序的列表(黑色)最初只包含列表中的第一个元素。每次迭代时，从输入数据中删除一个元素(红色)，并就地插入已排序的列表
//中。 
//
// 对链表进行插入排序。 
//
// 
//
// 
//
// 示例 1： 
//
// 
//
// 
//输入: head = [4,2,1,3]
//输出: [1,2,3,4] 
//
// 示例 2： 
//
// 
//
// 
//输入: head = [-1,5,3,4,0]
//输出: [-1,0,3,4,5] 
//
// 
//
// 提示： 
//
// 
// 
//
// 
// 列表中的节点数在 [1, 5000]范围内 
// -5000 <= Node.val <= 5000 
// 
//
// Related Topics 链表 排序 👍 538 👎 0


package com.coachhe.leetcode.editor.cn.链表.P147_对链表进行插入排序_中等;

import com.coachhe.leetcode.editor.cn.链表.ListNode;
import org.testng.annotations.Test;

/**
 * 对链表进行插入排序
 * @author CoachHe
 * @date 2022-08-13 15:55:18
 */
public class P147_InsertionSortList{
	 @Test
	 public void test() {
	 	 //测试代码
	 	 Solution solution = new P147_InsertionSortList().new Solution();
		 ListNode list = new ListNode(6);
		 list.next = new ListNode(5);
		 list.next.next = new ListNode(3);
		 list.next.next.next = new ListNode(1);
		 list.next.next.next.next = new ListNode(8);
		 list.next.next.next.next.next = new ListNode(7);
		 list.next.next.next.next.next.next = new ListNode(2);
		 list.next.next.next.next.next.next.next = new ListNode(4);
		 ListNode resNode = solution.insertionSortList(list);
		 while (resNode != null) {
			 System.out.print(resNode.val + " ");
			 resNode = resNode.next;
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
    public ListNode insertionSortList(ListNode head) {
		if (head == null || head.next == null) {
			return head;
		}
		ListNode dummy = new ListNode(0, head);
		ListNode curNode = head;
		head = dummy;
		while (curNode != null) {
			if (curNode.next != null && curNode.next.val < curNode.val) {
				while (head.next != null && head.next.val < curNode.next.val) {
					head = head.next;
				}
				ListNode sortNode = curNode.next;
				curNode.next = curNode.next.next;
				sortNode.next = head.next;
				head.next = sortNode;
				curNode = sortNode.next;
				head = dummy;
			} else {
				curNode = curNode.next;
			}
		}

		return dummy.next;
    }
}
//leetcode submit region end(Prohibit modification and deletion)

}
