//给你一个长度为 n 的链表，每个节点包含一个额外增加的随机指针 random ，该指针可以指向链表中的任何节点或空节点。 
//
// 构造这个链表的 深拷贝。 深拷贝应该正好由 n 个 全新 节点组成，其中每个新节点的值都设为其对应的原节点的值。新节点的 next 指针和 random 
//指针也都应指向复制链表中的新节点，并使原链表和复制链表中的这些指针能够表示相同的链表状态。复制链表中的指针都不应指向原链表中的节点 。 
//
// 例如，如果原链表中有 X 和 Y 两个节点，其中 X.random --> Y 。那么在复制链表中对应的两个节点 x 和 y ，同样有 x.random 
//--> y 。 
//
// 返回复制链表的头节点。 
//
// 用一个由 n 个节点组成的链表来表示输入/输出中的链表。每个节点用一个 [val, random_index] 表示： 
//
// 
// val：一个表示 Node.val 的整数。 
// random_index：随机指针指向的节点索引（范围从 0 到 n-1）；如果不指向任何节点，则为 null 。 
// 
//
// 你的代码 只 接受原链表的头节点 head 作为传入参数。 
//
// 
//
// 示例 1： 
//
// 
//
// 
//输入：head = [[7,null],[13,0],[11,4],[10,2],[1,0]]
//输出：[[7,null],[13,0],[11,4],[10,2],[1,0]]
// 
//
// 示例 2： 
//
// 
//
// 
//输入：head = [[1,1],[2,1]]
//输出：[[1,1],[2,1]]
// 
//
// 示例 3： 
//
// 
//
// 
//输入：head = [[3,null],[3,0],[3,null]]
//输出：[[3,null],[3,0],[3,null]]
// 
//
// 
//
// 提示： 
//
// 
// 0 <= n <= 1000
// 
// -10⁴ <= Node.val <= 10⁴ 
// Node.random 为 null 或指向链表中的节点。 
// 
//
// Related Topics 哈希表 链表 👍 969 👎 0


package com.coachhe.leetcode.editor.cn.链表.P138_复制带随机指针的链表_中等;

import com.coachhe.leetcode.editor.cn.链表.Node;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * 复制带随机指针的链表
 * @author CoachHe
 * @date 2022-08-17 03:11:18
 */
public class P138_CopyListWithRandomPointer{
	 @Test
	 public void test() {
	 	 //测试代码
	 	 Solution solution = new P138_CopyListWithRandomPointer().new Solution();
//		 Node node = new Node(7);
//		 node.next = new Node(13);
//		 node.next.next = new Node(11);
//		 node.next.next.next = new Node(10);
//		 node.next.next.next.next = new Node(1);
//		 node.random = null;
//		 node.next.random = node;
//		 node.next.next.random = node.next.next.next.next;
//		 node.next.next.next.random = node.next.next;
//		 node.next.next.next.next.random = node;
		 Node node = new Node(-1);
		 node.random = null;
		 Node resNode = solution.copyRandomList(node);
		 while (resNode != null) {
			 int i = resNode.random == null ? 0 : resNode.random.val;
			 System.out.println(resNode.val + " " + i);
			 resNode = resNode.next;
		 }
	 }
	 
//力扣代码
//leetcode submit region begin(Prohibit modification and deletion)
/*
// Definition for a Node.
class Node {
    int val;
    Node next;
    Node random;

    public Node(int val) {
        this.val = val;
        this.next = null;
        this.random = null;
    }
}
*/

class Solution {
	// 用map的版本，另一个版本放在剑指offer中
    public Node copyRandomList(Node head) {
		if (head == null) {
			return null;
		}
		if (head.next == null) {
			Node resNode = new Node(head.val);
			if (head.random == head) {
				resNode.random = resNode;
			}
			return resNode;
		}
		Map<Node, Node> nodeMap = new HashMap<>();
		Node resHead = head;
		while (head != null) {
			Node tmpNode = new Node(head.val);
			nodeMap.put(head, tmpNode);
			head = head.next;
		}
		Node remHead = nodeMap.get(resHead);
		while (resHead != null) {
			Node tmpNode = nodeMap.get(resHead);
			tmpNode.next = resHead.next == null ? null : nodeMap.get(resHead.next);
			tmpNode.random = resHead.random == null ? null : nodeMap.get(resHead.random);
			resHead = resHead.next;
		}

		return remHead;
    }

}
//leetcode submit region end(Prohibit modification and deletion)

}
