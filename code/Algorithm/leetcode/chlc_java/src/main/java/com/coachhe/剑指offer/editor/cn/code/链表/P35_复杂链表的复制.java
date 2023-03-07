package com.coachhe.剑指offer.editor.cn.code.链表;

import com.coachhe.leetcode.editor.cn.链表.Node;
import org.testng.annotations.Test;

/**
 * @author CoachHe
 * @date 2022/8/18 00:41
 **/
public class P35_复杂链表的复制 {

    @Test
    public void test() {
        Solution solution = new Solution();
        Node node = new Node(7);
        node.next = new Node(13);
        node.next.next = new Node(11);
        node.next.next.next = new Node(10);
        node.next.next.next.next = new Node(1);
        node.random = null;
        node.next.random = node;
        node.next.next.random = node.next.next.next.next;
        node.next.next.next.random = node.next.next;
        node.next.next.next.next.random = node;
//        Node node = new Node(-1);
//        node.random = null;
        Node resNode = solution.copyRandomList(node);
        while (resNode != null) {
            int i = resNode.random == null ? 0 : resNode.random.val;
            System.out.println(resNode.val + " " + i);
            resNode = resNode.next;
        }

    }

    class Solution {
        public Node copyRandomList(Node head) {
            if (head == null) {
                return null;
            }
            Node resHead = head;
            while (head != null) {
                Node tmpNode = new Node(head.val);
                Node helpNode = head.next;
                head.next = tmpNode;
                tmpNode.next = helpNode;
                head = tmpNode.next;
            }
            head = resHead;
            while (head != null) {
                head.next.random = head.random == null ? null : head.random.next;
                head = head.next.next;
            }
            head = resHead.next;
            while (head != null && head.next != null) {
                head.next = head.next.next;
                head = head.next;
            }
            return resHead.next;
        }
    }
}
