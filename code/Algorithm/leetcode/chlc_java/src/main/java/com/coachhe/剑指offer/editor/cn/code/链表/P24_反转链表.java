package com.coachhe.剑指offer.editor.cn.code.链表;

import com.coachhe.leetcode.editor.cn.链表.ListNode;
import org.testng.annotations.Test;

/**
 * @author CoachHe
 * @date 2022/8/14 12:19
 **/
public class P24_反转链表 {

    @Test
    public void test(){
        Solution solution = new Solution();
        ListNode node = new ListNode(3);
        node.next = new ListNode(4);
        node.next.next = new ListNode(1);
        node.next.next.next = new ListNode(2);
        ListNode head = solution.reverseList(node);
        while (head != null) {
            System.out.println(head.val);
            head = head.next;
        }
    }

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
}
