//给定一个二叉树，判断它是否是高度平衡的二叉树。 
//
// 本题中，一棵高度平衡二叉树定义为： 
//
// 
// 一个二叉树每个节点 的左右两个子树的高度差的绝对值不超过 1 。 
// 
//
// 
//
// 示例 1： 
// 
// 
//输入：root = [3,9,20,null,null,15,7]
//输出：true
// 
//
// 示例 2： 
// 
// 
//输入：root = [1,2,2,3,3,null,null,4,4]
//输出：false
// 
//
// 示例 3： 
//
// 
//输入：root = []
//输出：true
// 
//
// 
//
// 提示： 
//
// 
// 树中的节点数在范围 [0, 5000] 内 
// -10⁴ <= Node.val <= 10⁴ 
// 
//
// Related Topics 树 深度优先搜索 二叉树 👍 1119 👎 0


package com.coachhe.leetcode.editor.cn.二叉树.平衡二叉树.P110_平衡二叉树;

import com.coachhe.leetcode.editor.cn.二叉树.TreeNode;
import org.testng.annotations.Test;

/**
 * 平衡二叉树
 * @author CoachHe
 * @date 2022-08-22 23:33:28
 */
public class P110_BalancedBinaryTree{
	 @Test
	 public void test() {
	 	 //测试代码
	 	 Solution solution = new P110_BalancedBinaryTree().new Solution();
		 TreeNode node = new TreeNode(3);
		 node.left = new TreeNode(9);
		 node.right = new TreeNode(20);
		 node.right.left = new TreeNode(15);
		 node.right.right = new TreeNode(7);
		 System.out.println(solution.isBalanced(node));
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
    public boolean isBalanced(TreeNode root) {
		return isBC(root).isBalance;
	}

	public HelpClass isBC(TreeNode root) {
		if (root == null) {
			return new HelpClass(0, true);
		}
		HelpClass leftClass = isBC(root.left);
		HelpClass rightClass = isBC(root.right);
		if (leftClass.isBalance && rightClass.isBalance && Math.abs(leftClass.length - rightClass.length) <= 1) {
			return new HelpClass(Math.max(leftClass.length, rightClass.length) + 1, true);
		}
		return new HelpClass(0, false);
	}


	class HelpClass {
		public int length;
		public boolean isBalance;

		public HelpClass(int length, boolean isBalance) {
			this.length = length;
			this.isBalance = isBalance;
		}
	}
}
//leetcode submit region end(Prohibit modification and deletion)

}
