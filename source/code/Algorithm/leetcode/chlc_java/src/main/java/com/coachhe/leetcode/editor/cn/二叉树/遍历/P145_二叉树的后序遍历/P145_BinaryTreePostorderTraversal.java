//给你一棵二叉树的根节点 root ，返回其节点值的 后序遍历 。 
//
// 
//
// 示例 1： 
// 
// 
//输入：root = [1,null,2,3]
//输出：[3,2,1]
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
// 树中节点的数目在范围 [0, 100] 内 
// -100 <= Node.val <= 100 
// 
//
// 
//
// 进阶：递归算法很简单，你可以通过迭代算法完成吗？ 
//
// Related Topics 栈 树 深度优先搜索 二叉树 👍 907 👎 0


package com.coachhe.leetcode.editor.cn.二叉树.遍历.P145_二叉树的后序遍历;

import com.coachhe.leetcode.editor.cn.二叉树.TreeNode;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * 二叉树的后序遍历
 * @author CoachHe
 * @date 2022-08-18 01:06:15
 */
public class P145_BinaryTreePostorderTraversal{
	 @Test
	 public void test() {
	 	 //测试代码
	 	 Solution solution = new P145_BinaryTreePostorderTraversal().new Solution();
		 TreeNode node = new TreeNode(1);
		 node.right = new TreeNode(2);
		 node.right.left = new TreeNode(3);
		 List<Integer> list = solution.postorderTraversal(node);
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
    public List<Integer> postorderTraversal(TreeNode root) {
		if (root == null) {
			return new ArrayList<>();
		}
		Stack<TreeNode> stack = new Stack<>();
		List<Integer> resList = new ArrayList<>();
		Stack<TreeNode> helpStack = new Stack<>();
		stack.push(root);
		while (!stack.isEmpty()) {
			TreeNode tmpNode = stack.pop();
			helpStack.push(tmpNode);
			if (tmpNode.left != null) {
				stack.push(tmpNode.left);
			}
			if (tmpNode.right != null) {
				stack.push(tmpNode.right);
			}
		}
		while (!helpStack.isEmpty()) {
			resList.add(helpStack.pop().val);
		}
		return resList;
    }
}
//leetcode submit region end(Prohibit modification and deletion)

}
