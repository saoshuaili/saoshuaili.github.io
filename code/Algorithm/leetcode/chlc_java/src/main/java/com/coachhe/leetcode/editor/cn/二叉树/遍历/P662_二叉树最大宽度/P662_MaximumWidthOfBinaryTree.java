//给你一棵二叉树的根节点 root ，返回树的 最大宽度 。 
//
// 树的 最大宽度 是所有层中最大的 宽度 。 
//
// 
// 
// 每一层的 宽度 被定义为该层最左和最右的非空节点（即，两个端点）之间的长度。将这个二叉树视作与满二叉树结构相同，两端点间会出现一些延伸到这一层的 
//null 节点，这些 null 节点也计入长度。 
// 
// 
//
// 题目数据保证答案将会在 32 位 带符号整数范围内。 
//
// 
//
// 示例 1： 
// 
// 
//输入：root = [1,3,2,5,3,null,9]
//输出：4
//解释：最大宽度出现在树的第 3 层，宽度为 4 (5,3,null,9) 。
// 
//
// 示例 2： 
// 
// 
//输入：root = [1,3,2,5,null,null,9,6,null,7]
//输出：7
//解释：最大宽度出现在树的第 4 层，宽度为 7 (6,null,null,null,null,null,7) 。
// 
//
// 示例 3： 
// 
// 
//输入：root = [1,3,2,5]
//输出：2
//解释：最大宽度出现在树的第 2 层，宽度为 2 (3,2) 。
// 
//
// 
//
// 提示： 
//
// 
// 树中节点的数目范围是 [1, 3000] 
// -100 <= Node.val <= 100 
// 
//
// Related Topics 树 深度优先搜索 广度优先搜索 二叉树 👍 403 👎 0


package com.coachhe.leetcode.editor.cn.二叉树.遍历.P662_二叉树最大宽度;

import com.coachhe.leetcode.editor.cn.二叉树.TreeNode;
import org.testng.annotations.Test;

import java.util.*;

/**
 * 二叉树最大宽度
 *
 * @author CoachHe
 * @date 2022-08-21 02:28:24
 */
public class P662_MaximumWidthOfBinaryTree {
    @Test
    public void test() {
        //测试代码
        Solution solution = new P662_MaximumWidthOfBinaryTree().new Solution();
//        TreeNode root = new TreeNode(1);
//        root.left = new TreeNode(3);
//        root.left.left = new TreeNode(5);
//        root.left.left.left = new TreeNode(6);
//        root.right = new TreeNode(2);
//        root.right.right = new TreeNode(9);
//        root.right.right.right = new TreeNode(7);
//        TreeNode root = new TreeNode(1);
//        root.left = new TreeNode(3);
//        root.right = new TreeNode(2);
//        root.left.left = new TreeNode(5);
//        root.left.right = new TreeNode(3);
//        root.right.right = new TreeNode(9);
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(1);
        root.right = new TreeNode(1);
        root.left.left = new TreeNode(1);
        root.left.right = new TreeNode(1);
        root.right.left = new TreeNode(1);
        root.right.right = new TreeNode(1);
        root.left.right.right = new TreeNode(1);
        root.left.right.right.left = new TreeNode(2);
        root.left.right.right.right = new TreeNode(2);
        root.left.right.right.left.left = new TreeNode(2);
        root.left.right.right.left.right = new TreeNode(2);
        root.left.right.right.right.left = new TreeNode(2);
        root.left.right.right.right.right = new TreeNode(2);
        root.left.right.right.left.left.left = new TreeNode(2);
        root.left.right.right.left.right.left = new TreeNode(2);
        root.left.right.right.right.left.right = new TreeNode(2);
        root.left.right.right.right.right.right = new TreeNode(2);
        System.out.println(solution.widthOfBinaryTree(root));
    }

//力扣代码
//leetcode submit region begin(Prohibit modification and deletion)

    /**
     * Definition for a binary tree node.
     * public class TreeNode {
     * int val;
     * TreeNode left;
     * TreeNode right;
     * TreeNode() {}
     * TreeNode(int val) { this.val = val; }
     * TreeNode(int val, TreeNode left, TreeNode right) {
     * this.val = val;
     * this.left = left;
     * this.right = right;
     * }
     * }
     */
    class Solution {
        public int widthOfBinaryTree(TreeNode root) {
            if (root == null) {
                return 0;
            }
            return rotate(root);
        }

        public int rotate(TreeNode node) {
            if (node == null) {
                return 0;
            }
            int max = -1;
            int curLevel = 1;
            int headPosition = 0;
            Queue<HelpClass> queue = new LinkedList<>();
            queue.add(new HelpClass(0, 1, node));
            HelpClass item = null;
            while (!queue.isEmpty()){
                item = queue.poll();
                if (item.node.left != null) {
                    queue.add(new HelpClass(item.position * 2 + 1, item.depth + 1, item.node.left));
                }
                if (item.node.right != null) {
                    queue.add(new HelpClass(item.position * 2 + 2, item.depth + 1, item.node.right));
                }
                if (item.depth == curLevel) {
                    max = Math.max(max, item.position - headPosition + 1);
                } else {
                    curLevel++;
                    headPosition = item.position;
                }
            }

            return Math.max(max, item.position - headPosition);
        }
        class HelpClass {
            public int position;
            public int depth;
            public TreeNode node;

            public HelpClass(int position, int depth, TreeNode node) {
                this.position = position;
                this.depth = depth;
                this.node = node;
            }
        }
    }


    //leetcode submit region end(Prohibit modification and deletion)
}