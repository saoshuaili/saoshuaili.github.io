//给你一个二叉树的根节点 root ，判断其是否是一个有效的二叉搜索树。 
//
// 有效 二叉搜索树定义如下： 
//
// 
// 节点的左子树只包含 小于 当前节点的数。 
// 节点的右子树只包含 大于 当前节点的数。 
// 所有左子树和右子树自身必须也是二叉搜索树。 
// 
//
// 
//
// 示例 1： 
// 
// 
//输入：root = [2,1,3]
//输出：true
// 
//
// 示例 2： 
// 
// 
//输入：root = [5,1,4,null,null,3,6]
//输出：false
//解释：根节点的值是 5 ，但是右子节点的值是 4 。
// 
//
// 
//
// 提示： 
//
// 
// 树中节点数目范围在[1, 10⁴] 内 
// -2³¹ <= Node.val <= 2³¹ - 1 
// 
//
// Related Topics 树 深度优先搜索 二叉搜索树 二叉树 👍 1725 👎 0


package com.coachhe.leetcode.editor.cn.二叉树.搜索二叉树.P98_验证二叉搜索树;

import com.coachhe.leetcode.editor.cn.二叉树.TreeNode;
import org.testng.annotations.Test;

/**
 * 验证二叉搜索树
 * @author CoachHe
 * @date 2022-08-23 00:36:31
 */
public class P98_ValidateBinarySearchTree{
	 @Test
	 public void test() {
	 	 //测试代码
	 	 Solution solution = new P98_ValidateBinarySearchTree().new Solution();
//		 TreeNode root = new TreeNode(2);
//		 root.left = new TreeNode(1);
//		 root.right = new TreeNode(3);
//		 TreeNode root = new TreeNode(5);
//		 root.left = new TreeNode(1);
//		 root.right = new TreeNode(4);
//		 root.right.left = new TreeNode(3);
//		 root.right.right = new TreeNode(6);
//		 TreeNode root = new TreeNode(32);
//		 root.left = new TreeNode(26);
//		 root.right = new TreeNode(47);
//		 root.left.left = new TreeNode(19);
//		 root.left.left.right = new TreeNode(27);
//		 TreeNode root = new TreeNode(0);
//		 root.left = new TreeNode(-1);
		 TreeNode root = new TreeNode(-2147483648);
		 root.right = new TreeNode(2147483647);
		 System.out.println(solution.isValidBST(root));
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
    public boolean isValidBST(TreeNode root) {
		return process(root).isValidBST;
    }

	public HelpClass process(TreeNode root) {
		if (root == null) {
			return null;
		}
		HelpClass leftClass = process(root.left);
		HelpClass rightClass = process(root.right);

		if (leftClass == null && rightClass == null) {
			return new HelpClass(root.val, root.val, true);
		}
		if (leftClass == null) {
			return new HelpClass(rightClass.max, root.val, rightClass.isValidBST && root.val < rightClass.min);
		}
		if (rightClass == null) {
			return new HelpClass(root.val, leftClass.min, leftClass.isValidBST && leftClass.max < root.val);
		}
		return new HelpClass(rightClass.max, leftClass.min, rightClass.isValidBST && leftClass.isValidBST && root.val > leftClass.max && root.val < rightClass.min);
	}

	class HelpClass {
		public int max;
		public int min;
		public boolean isValidBST;

		public HelpClass(int max, int min, boolean isValidBST) {
			this.max = max;
			this.min = min;
			this.isValidBST = isValidBST;
		}
	}

	// 方法2，用中序遍历
//	public boolean isValidBST(TreeNode root) {
//		if (root == null || (root.left == null && root.right == null)) {
//			return true;
//		}
//		Stack<TreeNode> stack = new Stack<>();
//		while (root != null) {
//			stack.push(root);
//			root = root.left;
//		}
//		Double tmpMax = -Double.MAX_VALUE;
//		while (!stack.isEmpty()) {
//			TreeNode node = stack.pop();
//			if (tmpMax >= node.val) {
//				return false;
//			}
//			tmpMax = Double.valueOf(node.val);
//			if (node.right != null) {
//				stack.push(node.right);
//				node = node.right;
//				while (node.left != null) {
//					stack.push(node.left);
//					node = node.left;
//				}
//			}
//		}
//		return true;
//	}

}
//leetcode submit region end(Prohibit modification and deletion)

}
