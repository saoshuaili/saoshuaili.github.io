//给定一个不重复的整数数组 nums 。 最大二叉树 可以用下面的算法从 nums 递归地构建: 
//
// 
// 创建一个根节点，其值为 nums 中的最大值。 
// 递归地在最大值 左边 的 子数组前缀上 构建左子树。 
// 递归地在最大值 右边 的 子数组后缀上 构建右子树。 
// 
//
// 返回 nums 构建的 最大二叉树 。 
//
// 
//
// 示例 1： 
// 
// 
//输入：nums = [3,2,1,6,0,5]
//输出：[6,3,5,null,2,0,null,null,1]
//解释：递归调用如下所示：
//- [3,2,1,6,0,5] 中的最大值是 6 ，左边部分是 [3,2,1] ，右边部分是 [0,5] 。
//    - [3,2,1] 中的最大值是 3 ，左边部分是 [] ，右边部分是 [2,1] 。
//        - 空数组，无子节点。
//        - [2,1] 中的最大值是 2 ，左边部分是 [] ，右边部分是 [1] 。
//            - 空数组，无子节点。
//            - 只有一个元素，所以子节点是一个值为 1 的节点。
//    - [0,5] 中的最大值是 5 ，左边部分是 [0] ，右边部分是 [] 。
//        - 只有一个元素，所以子节点是一个值为 0 的节点。
//        - 空数组，无子节点。
// 
//
// 示例 2： 
// 
// 
//输入：nums = [3,2,1]
//输出：[3,null,2,null,1]
// 
//
// 
//
// 提示： 
//
// 
// 1 <= nums.length <= 1000 
// 0 <= nums[i] <= 1000 
// nums 中的所有整数 互不相同 
// 
//
// Related Topics 栈 树 数组 分治 二叉树 单调栈 👍 507 👎 0


package com.coachhe.leetcode.editor.cn.二叉树.通用解法.P654_最大二叉树_中等;

import com.coachhe.leetcode.editor.cn.二叉树.TreeNode;
import org.testng.annotations.Test;

/**
 * 最大二叉树
 * @author CoachHe
 * @date 2022-08-20 13:18:36
 */
public class P654_MaximumBinaryTree{
	 @Test
	 public void test() {
	 	 //测试代码
	 	 Solution solution = new P654_MaximumBinaryTree().new Solution();
		 int[] nums = new int[]{3, 2, 1, 6, 0, 5};
		 TreeNode treeNode = solution.constructMaximumBinaryTree(nums);
		 System.out.println(treeNode.val + " " + treeNode.left.val + " " + treeNode.right.val);
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
    public TreeNode constructMaximumBinaryTree(int[] nums) {
		HelpClass headClass = maxPositions(nums, 0, nums.length - 1);
		return headClass.node;
    }

	public HelpClass maxPositions(int[] nums, int left, int right) {
		if (left > right) {
			return new HelpClass(null, left);
		}
		int max = Integer.MIN_VALUE;
		int index = left;
		for (int i = left; i <= right; i++) {
			if (max < nums[i]) {
				max = Math.max(nums[i], max);
				index = i;
			}
		}
		TreeNode node = new TreeNode(max);
		HelpClass leftClass = maxPositions(nums, left, index - 1);
		HelpClass rightClass = maxPositions(nums, index + 1, right);
		node.left = leftClass.node;
		node.right = rightClass.node;
		return new HelpClass(node, index);
	}
}

class HelpClass {
	public TreeNode node;
	public int index;
	public HelpClass(TreeNode node, int index) {
		this.node = node;
        this.index = index;
	}
}
//leetcode submit region end(Prohibit modification and deletion)

}
