//给定一个链表的头节点 head ，返回链表开始入环的第一个节点。 如果链表无环，则返回 null。 
//
// 如果链表中有某个节点，可以通过连续跟踪 next 指针再次到达，则链表中存在环。 为了表示给定链表中的环，评测系统内部使用整数 pos 来表示链表尾连接到
//链表中的位置（索引从 0 开始）。如果 pos 是 -1，则在该链表中没有环。注意：pos 不作为参数进行传递，仅仅是为了标识链表的实际情况。 
//
// 不允许修改 链表。 
//
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
//输入：head = [3,2,0,-4], pos = 1
//输出：返回索引为 1 的链表节点
//解释：链表中有一个环，其尾部连接到第二个节点。
// 
//
// 示例 2： 
//
// 
//
// 
//输入：head = [1,2], pos = 0
//输出：返回索引为 0 的链表节点
//解释：链表中有一个环，其尾部连接到第一个节点。
// 
//
// 示例 3： 
//
// 
//
// 
//输入：head = [1], pos = -1
//输出：返回 null
//解释：链表中没有环。
// 
//
// 
//
// 提示： 
//
// 
// 链表中节点的数目范围在范围 [0, 10⁴] 内 
// -10⁵ <= Node.val <= 10⁵ 
// pos 的值为 -1 或者链表中的一个有效索引 
// 
//
// 
//
// 进阶：你是否可以使用 O(1) 空间解决此题？ 
//
// Related Topics 哈希表 链表 双指针 👍 1714 👎 0


package com.coachhe.leetcode.editor.cn.链表.P142_环形链表II_中等;

import com.coachhe.leetcode.editor.cn.链表.ListNode;
import org.testng.annotations.Test;

/**
 * 环形链表 II
 * @author CoachHe
 * @date 2022-08-13 15:55:01
 */
public class P142_LinkedListCycleIi{
	 @Test
	 public void test() {
	 	 //测试代码
	 	 Solution solution = new P142_LinkedListCycleIi().new Solution();
		 ListNode list = new ListNode(3);
		 list.next = new ListNode(2);
		 list.next.next = new ListNode(0);
		 list.next.next.next = new ListNode(-4);
		 list.next.next.next.next = list.next;
		 System.out.println(solution.detectCycle(list).val);
	 }
	 
//力扣代码
//leetcode submit region begin(Prohibit modification and deletion)
/**
 * Definition for singly-linked list.
 * class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode(int x) {
 *         val = x;
 *         next = null;
 *     }
 * }
 */
public class Solution {
    public ListNode detectCycle(ListNode head) {
		ListNode fastPoint = head;
		ListNode slowPoint = head;
		while (fastPoint != null && fastPoint.next != null) {
			fastPoint = fastPoint.next.next;
			slowPoint = slowPoint.next;
			if (fastPoint == slowPoint) {
				fastPoint = head;
				while (fastPoint != slowPoint) {
					fastPoint = fastPoint.next;
					slowPoint = slowPoint.next;
				}
				return fastPoint;
			}
		}

		return null;
    }
}
//leetcode submit region end(Prohibit modification and deletion)

}
