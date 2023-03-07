package com.coachhe.剑指offer.editor.cn.code.链表;

import com.coachhe.leetcode.editor.cn.链表.ListNode;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author CoachHe
 * @date 2022/8/14 13:31
 **/
public class P06_从尾到头打印链表 {

    @Test
    public void test() {
        ListNode list = new ListNode(1);
        list.next = new ListNode(2);
        list.next.next = new ListNode(3);
        list.next.next.next = new ListNode(4);
        Solution solution = new Solution();
        System.out.println(Arrays.toString(solution.reversePrint(list)));
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

        public int[] reversePrint(ListNode head) {

            ListNode reverseHead = reverseList(head);
            List<Integer> list = new ArrayList<>();
            while (reverseHead != null) {
                list.add(reverseHead.val);
                reverseHead = reverseHead.next;
            }
            int[] resArr = new int[list.size()];
            for (int i = 0; i < resArr.length; i++) {
                resArr[i] = list.get(i);
            }
            return resArr;
        }

    }

}
