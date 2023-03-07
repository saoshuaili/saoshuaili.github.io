//给你一棵二叉树的根节点 root ，请你构造一个下标从 0 开始、大小为 m x n 的字符串矩阵 res ，用以表示树的 格式化布局 。构造此格式化布局矩
//阵需要遵循以下规则： 
//
// 
// 树的 高度 为 height ，矩阵的行数 m 应该等于 height + 1 。 
// 矩阵的列数 n 应该等于 2ʰᵉⁱᵍʰᵗ⁺¹ - 1 。 
// 根节点 需要放置在 顶行 的 正中间 ，对应位置为 res[0][(n-1)/2] 。 
// 对于放置在矩阵中的每个节点，设对应位置为 res[r][c] ，将其左子节点放置在 res[r+1][c-2ʰᵉⁱᵍʰᵗ⁻ʳ⁻¹] ，右子节点放置在 
//res[r+1][c+2ʰᵉⁱᵍʰᵗ⁻ʳ⁻¹] 。 
// 继续这一过程，直到树中的所有节点都妥善放置。 
// 任意空单元格都应该包含空字符串 "" 。 
// 
//
// 返回构造得到的矩阵 res 。 
//
// 
//
// 
//
// 示例 1： 
// 
// 
//输入：root = [1,2]
//输出：
//[["","1",""],
// ["2","",""]]
// 
//
// 示例 2： 
// 
// 
//输入：root = [1,2,3,null,4]
//输出：
//[["","","","1","","",""],
// ["","2","","","","3",""],
// ["","","4","","","",""]]
// 
//
// 
//
// 提示： 
//
// 
// 树中节点数在范围 [1, 2¹⁰] 内 
// -99 <= Node.val <= 99 
// 树的深度在范围 [1, 10] 内 
// 
//
// Related Topics 树 深度优先搜索 广度优先搜索 二叉树 👍 136 👎 0


package com.coachhe.leetcode.editor.cn.二叉树.通用解法.P655_输出二叉树;

import com.coachhe.leetcode.editor.cn.二叉树.TreeNode;
import org.testng.annotations.Test;

import java.util.*;

/**
 * 输出二叉树
 * @author CoachHe
 * @date 2022-08-22 01:00:49
 */
public class P655_PrintBinaryTree{
	 @Test
	 public void test() {
	 	 //测试代码
	 	 Solution solution = new P655_PrintBinaryTree().new Solution();
		 TreeNode node = new TreeNode(1);
		 node.left = new TreeNode(2);
		 node.right = new TreeNode(3);
		 node.left.right = new TreeNode(4);
		 List<List<String>> lists = solution.printTree(node);
		 System.out.println(lists);
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
    public List<List<String>> printTree(TreeNode root) {
		List<List<String>> resList = new ArrayList<>();
		if (root == null) {
			return resList;
		}
		int height = maxDepth(root) - 1;
		int heightLength = height + 1;
		int weightLength = (int)Math.pow(2, height + 1) - 1;
		int[][] arr = new int[heightLength][weightLength];
		for (int[] i : arr) {
			Arrays.fill(i, Integer.MIN_VALUE);
		}
		Queue<HelpClass> queue = new LinkedList<>();
		queue.add(new HelpClass(0, (weightLength - 1) / 2, root));
		arr[0][(weightLength - 1) / 2] = root.val;
		while (!queue.isEmpty()) {
			HelpClass nowClass = queue.remove();
			TreeNode leftNode = nowClass.node.left;
			TreeNode rightNode = nowClass.node.right;
			if (leftNode != null) {
				int leftHeight = nowClass.height + 1;
				int leftWeight = nowClass.weight - (int) Math.pow(2, height - nowClass.height - 1);
				queue.add(new HelpClass(leftHeight, leftWeight, leftNode));
				arr[leftHeight][leftWeight] = leftNode.val;
			}
			if (rightNode != null) {
				int rightHeight = nowClass.height + 1;
				int rightWeight = nowClass.weight + (int) Math.pow(2, height - nowClass.height - 1);
				queue.add(new HelpClass(rightHeight, rightWeight, rightNode));
				arr[rightHeight][rightWeight] = rightNode.val;
			}
		}
		for (int[] ints : arr) {
			List<String> list = new ArrayList<>();
			for (int anInt : ints) {
				if (anInt != Integer.MIN_VALUE) {
					list.add(String.format("%s", anInt));
				} else {
					list.add("");
				}
			}
			resList.add(list);
		}

		return resList;
    }

	public int maxDepth(TreeNode root) {
		if (root == null) {
			return 0;
		} else {
			int leftMax = maxDepth(root.left);
			int rightMax = maxDepth(root.right);
			return Math.max(leftMax, rightMax) + 1;
		}
	}

	class HelpClass {
		public int height;
		public int weight;
		public TreeNode node;
		public HelpClass(int height, int weight, TreeNode node) {
			this.height = height;
			this.weight = weight;
			this.node = node;
		}
	}
}
//leetcode submit region end(Prohibit modification and deletion)

}
