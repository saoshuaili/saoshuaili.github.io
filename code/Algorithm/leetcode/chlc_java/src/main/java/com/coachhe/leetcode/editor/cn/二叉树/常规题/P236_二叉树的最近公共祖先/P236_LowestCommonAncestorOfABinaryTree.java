//给定一个二叉树, 找到该树中两个指定节点的最近公共祖先。 
//
// 百度百科中最近公共祖先的定义为：“对于有根树 T 的两个节点 p、q，最近公共祖先表示为一个节点 x，满足 x 是 p、q 的祖先且 x 的深度尽可能大（
//一个节点也可以是它自己的祖先）。” 
//
// 
//
// 示例 1： 
// 
// 
//输入：root = [3,5,1,6,2,0,8,null,null,7,4], p = 5, q = 1
//输出：3
//解释：节点 5 和节点 1 的最近公共祖先是节点 3 。
// 
//
// 示例 2： 
// 
// 
//输入：root = [3,5,1,6,2,0,8,null,null,7,4], p = 5, q = 4
//输出：5
//解释：节点 5 和节点 4 的最近公共祖先是节点 5 。因为根据定义最近公共祖先节点可以为节点本身。
// 
//
// 示例 3： 
//
// 
//输入：root = [1,2], p = 1, q = 2
//输出：1
// 
//
// 
//
// 提示： 
//
// 
// 树中节点数目在范围 [2, 10⁵] 内。 
// -10⁹ <= Node.val <= 10⁹ 
// 所有 Node.val 互不相同 。 
// p != q 
// p 和 q 均存在于给定的二叉树中。 
// 
//
// Related Topics 树 深度优先搜索 二叉树 👍 1916 👎 0


package com.coachhe.leetcode.editor.cn.二叉树.常规题.P236_二叉树的最近公共祖先;

import com.coachhe.leetcode.editor.cn.二叉树.TreeNode;
import org.testng.annotations.Test;

/**
 * 二叉树的最近公共祖先
 * @author CoachHe
 * @date 2022-08-23 01:40:20
 */
public class P236_LowestCommonAncestorOfABinaryTree{
	 @Test
	 public void test() {
	 	 //测试代码
	 	 Solution solution = new P236_LowestCommonAncestorOfABinaryTree().new Solution();
		 TreeNode node = new TreeNode(3);
		 node.left = new TreeNode(5);
		 node.left.left = new TreeNode(6);
		 node.left.right = new TreeNode(2);
		 node.left.right.left = new TreeNode(7);
		 node.left.right.right = new TreeNode(4);
		 node.right = new TreeNode(1);
		 node.right.left = new TreeNode(0);
		 node.right.right = new TreeNode(8);
//		 System.out.println(solution.lowestCommonAncestor(node, node.left, node.right).val);
		 System.out.println(solution.lowestCommonAncestor(node, node.left, node.left.right.right).val);
	 }
	 
//力扣代码
//leetcode submit region begin(Prohibit modification and deletion)
/**
 * Definition for a binary tree node.
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode(int x) { val = x; }
 * }
 */
class Solution {
//    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
//		Map<TreeNode, TreeNode> map = new HashMap<>();
//		map.put(root, null);
//		Set<TreeNode> set = new HashSet<>();
//		rotate(root, map);
//		while (p != null) {
//			set.add(p);
//			p = map.get(p);
//		}
//		while (q != root) {
//			if (set.contains(q)) {
//				return q;
//			} else {
//				q = map.get(q);
//			}
//		}
//		return root;
//    }
//
//	public void rotate(TreeNode root, Map<TreeNode, TreeNode> map) {
//		if (root == null) {
//			return;
//		}
//		if (root.left != null) {
//			map.put(root.left, root);
//		}
//		if (root.right != null) {
//			map.put(root.right, root);
//		}
//		rotate(root.left, map);
//		rotate(root.right, map);
//	}

	public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
		if (root == null || root == p || root == q) {
			return root;
		}
		TreeNode left = lowestCommonAncestor(root.left, p, q);
		TreeNode right = lowestCommonAncestor(root.right, p, q);
		if (left == null && right == null) {
			return null;
		}
		if (left == null) {
			return right;
		}
		if (right == null) {
			return left;
		}
		return root;
	}

}
//leetcode submit region end(Prohibit modification and deletion)

}
