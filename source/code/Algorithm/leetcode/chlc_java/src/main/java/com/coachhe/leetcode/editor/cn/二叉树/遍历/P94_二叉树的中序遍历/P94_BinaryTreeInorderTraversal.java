//给定一个二叉树的根节点 root ，返回 它的 中序 遍历 。 
//
// 
//
// 示例 1： 
// 
// 
//输入：root = [1,null,2,3]
//输出：[1,3,2]
// 
//
// 示例 2： 
//
// 
//输入：root = []
//输出：[]
// 
//
// 示例 3： 
//
// 
//输入：root = [1]
//输出：[1]
// 
//
// 
//
// 提示： 
//
// 
// 树中节点数目在范围 [0, 100] 内 
// -100 <= Node.val <= 100 
// 
//
// 
//
// 进阶: 递归算法很简单，你可以通过迭代算法完成吗？ 
//
// Related Topics 栈 树 深度优先搜索 二叉树 👍 1533 👎 0


package com.coachhe.leetcode.editor.cn.二叉树.遍历.P94_二叉树的中序遍历;

import com.coachhe.leetcode.editor.cn.二叉树.TreeNode;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * 二叉树的中序遍历
 * @author CoachHe
 * @date 2022-08-19 01:30:05
 */
public class P94_BinaryTreeInorderTraversal{
	 @Test
	 public void test() {
	 	 //测试代码
	 	 Solution solution = new P94_BinaryTreeInorderTraversal().new Solution();
		 TreeNode node = new TreeNode(3);
		 node.left = new TreeNode(1);
		 node.right = new TreeNode(2);
		 List<Integer> list = solution.inorderTraversal(node);
		 System.out.println(list);
	 }
	 
//力扣代码
//leetcode submit region begin(Prohibit modification and deletion)
/**
 * Definition for a binary tree node.
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode() {}
 *     TreeNode(int val) { this.val = val; }
 *     TreeNode(int val, TreeNode left, TreeNode right) {
 *         this.val = val;
 *         this.left = left;
 *         this.right = right;
 *     }
 * }
 */
class Solution {

	// 我的答案，比较麻烦
//    public List<Integer> inorderTraversal(TreeNode root) {
//		if (root == null) {
//			return new ArrayList<>();
//		}
//		Stack<TreeNode> stack = new Stack<>();
//		List<Integer> resList = new ArrayList<>();
//		stack.push(root);
//		while(!stack.isEmpty()) {
//			TreeNode tmpNode = stack.pop();
//			while (tmpNode != null) {
//				stack.push(tmpNode);
//				tmpNode = tmpNode.left;
//			}
//			TreeNode nowNode = stack.pop();
//			resList.add(nowNode.val);
//			while (!stack.isEmpty() && nowNode.right == null) {
//				nowNode = stack.pop();
//				resList.add(nowNode.val);
//			}
//			if (nowNode.right != null) {
//				stack.push(nowNode.right);
//			}
//		}
//
//		return resList;
//    }


	// 答案，很简洁
    public List<Integer> inorderTraversal(TreeNode root) {
		if (root == null) {
			return new ArrayList<>();
		}
		Stack<TreeNode> stack = new Stack<>();
		List<Integer> resList = new ArrayList<>();
		while (!stack.isEmpty() || root != null) {
			if (root != null) {
				stack.push(root);
				root = root.left;
			} else {
				root = stack.pop();
				resList.add(root.val);
				root = root.right;
			}
		}

		return resList;
    }

}
//leetcode submit region end(Prohibit modification and deletion)

}
