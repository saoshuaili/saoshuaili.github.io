//将两个升序链表合并为一个新的 升序 链表并返回。新链表是通过拼接给定的两个链表的所有节点组成的。 
//
// 
//
// 示例 1： 
// 
// 
//输入：l1 = [1,2,4], l2 = [1,3,4]
//输出：[1,1,2,3,4,4]
// 
//
// 示例 2： 
//
// 
//输入：l1 = [], l2 = []
//输出：[]
// 
//
// 示例 3： 
//
// 
//输入：l1 = [], l2 = [0]
//输出：[0]
// 
//
// 
//
// 提示： 
//
// 
// 两个链表的节点数目范围是 [0, 50] 
// -100 <= Node.val <= 100 
// l1 和 l2 均按 非递减顺序 排列 
// 
//
// Related Topics 递归 链表 👍 2601 👎 0


package com.coachhe.leetcode.editor.cn.链表.P21_合并两个有序链表_简单;

import com.coachhe.leetcode.editor.cn.链表.ListNode;
import org.testng.annotations.Test;

/**
 * 合并两个有序链表
 * @author CoachHe
 * @date 2022-08-13 15:57:07
 */
public class P21_MergeTwoSortedLists{
	 @Test
	 public void test() {
	 	 //测试代码
	 	 Solution solution = new P21_MergeTwoSortedLists().new Solution();
		 ListNode l1 = new ListNode(1);
		 l1.next = new ListNode(2);
		 l1.next.next = new ListNode(4);
		 ListNode l2 = new ListNode(1);
		 l2.next = new ListNode(3);
		 l2.next.next = new ListNode(4);
		 ListNode resNode = solution.mergeTwoLists(l1, l2);
		 while (resNode != null) {
			 System.out.println(resNode.val);
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
    public ListNode mergeTwoLists(ListNode list1, ListNode list2) {
		if (list1 == null || list2 == null) {
			return list1 == null ? list2 : list1;
		}
		ListNode resList = new ListNode();
		ListNode headList = resList;
		while (list1 != null || list2 != null) {
			if(list1 == null) {
				while (list2 != null) {
					resList.next = list2;
					list2 = list2.next;
					resList = resList.next;
				}
            } else if (list2 == null) {
				while (list1 != null) {
					resList.next = list1;
					list1 = list1.next;
					resList = resList.next;
				}
            } else if (list1.val > list2.val) {
				resList.next = list2;
				list2 = list2.next;
				resList = resList.next;
			} else {
				resList.next = list1;
                list1 = list1.next;
                resList = resList.next;
			}
		}
		return headList.next;
    }

}
//leetcode submit region end(Prohibit modification and deletion)

}
