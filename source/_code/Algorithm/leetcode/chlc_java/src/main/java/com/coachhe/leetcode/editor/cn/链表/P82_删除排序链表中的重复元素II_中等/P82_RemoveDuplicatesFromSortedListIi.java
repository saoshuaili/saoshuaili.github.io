//给定一个已排序的链表的头 head ， 删除原始链表中所有重复数字的节点，只留下不同的数字 。返回 已排序的链表 。 
//
// 
//
// 示例 1： 
// 
// 
//输入：head = [1,2,3,3,4,4,5]
//输出：[1,2,5]
// 
//
// 示例 2： 
// 
// 
//输入：head = [1,1,1,2,3]
//输出：[2,3]
// 
//
// 
//
// 提示： 
//
// 
// 链表中节点数目在范围 [0, 300] 内 
// -100 <= Node.val <= 100 
// 题目数据保证链表已经按升序 排列 
// 
//
// Related Topics 链表 双指针 👍 967 👎 0


package com.coachhe.leetcode.editor.cn.链表.P82_删除排序链表中的重复元素II_中等;

import com.coachhe.leetcode.editor.cn.链表.ListNode;
import org.testng.annotations.Test;

/**
 * 删除排序链表中的重复元素 II
 *
 * @author CoachHe
 * @date 2022-08-15 02:01:16
 */
public class P82_RemoveDuplicatesFromSortedListIi {
    @Test
    public void test() {
        //测试代码
        Solution solution = new P82_RemoveDuplicatesFromSortedListIi().new Solution();
        ListNode head = new ListNode(1);
        head.next = new ListNode(2);
        head.next.next = new ListNode(3);
        head.next.next.next = new ListNode(3);
        head.next.next.next.next = new ListNode(4);
        head.next.next.next.next.next = new ListNode(4);
        head.next.next.next.next.next.next = new ListNode(5);
//        ListNode head = new ListNode(1);
//        head.next = new ListNode(1);
//        head.next.next = new ListNode(1);
//        head.next.next.next = new ListNode(2);
//        head.next.next.next.next = new ListNode(2);
//        head.next.next.next.next.next = new ListNode(3);
//        ListNode head = new ListNode(1);
//        head.next = new ListNode(2);
//        head.next.next = new ListNode(2);
        ListNode resNode = solution.deleteDuplicates(head);
        while (resNode != null) {
            System.out.print(resNode.val + " ");
            resNode = resNode.next;
        }
    }

    //力扣代码
//leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        // 解法1.我自己的解法，有点麻烦
        public ListNode deleteDuplicates(ListNode head) {
            if (head == null || head.next == null) {
                return head;
            }
            ListNode resHead = head;
            int index = 0;
            // 先检查开头开始有没有重复元素，如果有则往后，直到第一个不重复的元素出现
            while (resHead != null && resHead.next != null && resHead.next.val == head.val) {
                index = 0;
                while (resHead.next != null && resHead.next.val == head.val) {
                    index++;
                    resHead = resHead.next;
                }
                head = index == 0 ? resHead : resHead.next;
                resHead = head;
            }
            if (head == null) {
                return null;
            }
            ListNode nowHead = head.next;
            while (nowHead != null) {
                int size = 0;
                while (nowHead.next != null && nowHead.next.val == nowHead.val) {
                    nowHead = nowHead.next;
                    size++;
                }
                if (size == 0) {
                    head.next = nowHead;
                    head = head.next;
                }
                nowHead = nowHead.next;
            }
            head.next = null;
            return resHead;
        }
//leetcode submit region end(Prohibit modification and deletion)

        // 2.标准答案，比我的简单很多
        public ListNode deleteDuplicates2(ListNode head) {
            if (head == null) {
                return null;
            }
            // 定义一个当前值为0，next为head的节点
            ListNode dummy = new ListNode(0, head);
            ListNode cur = dummy;
            while (cur.next != null && cur.next.next != null) {
                if (cur.next.val == cur.next.next.val) {
                    int x = cur.next.val;
                    while (cur.next != null && cur.next.val == x) {
                        cur.next = cur.next.next;
                    }
                } else {
                    cur = cur.next;
                }
            }
            return dummy.next;
        }
    }
}
