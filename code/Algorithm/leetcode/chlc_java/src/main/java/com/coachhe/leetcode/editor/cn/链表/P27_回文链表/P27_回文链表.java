package com.coachhe.剑指offer.editor.cn.code.链表;

import com.coachhe.leetcode.editor.cn.链表.ListNode;
import org.testng.annotations.Test;

/**
 * @author CoachHe
 * @date 2022/8/14 11:43
 **/
public class P27_回文链表 {

    @Test
    public void test() {
        Solution solution = new Solution();
        ListNode list = new ListNode(1);
        list.next = new ListNode(2);
        list.next.next = new ListNode(3);
        list.next.next.next = new ListNode(3);
        list.next.next.next.next = new ListNode(2);
        list.next.next.next.next.next = new ListNode(1);
        System.out.println(solution.isPalindrome(list));
    }


    class Solution {
        // 最简单的方法，直接放栈里
//        public boolean isPalindrome(ListNode head) {
//            if (head == null || head.next == null) {
//                return true;
//            }
//            Stack<ListNode> stack = new Stack<>();
//            ListNode remHead = head;
//            while (head != null) {
//                stack.push(head);
//                head = head.next;
//            }
//            while (!stack.isEmpty()) {
//                if (remHead.val != stack.pop().val) {
//                    return false;
//                }
//                remHead = remHead.next;
//            }
//            return true;
//        }


        // 方法2，翻转之后比较，放在主题中
        public boolean isPalindrome(ListNode head) {

            return true;
        }

    }

}
