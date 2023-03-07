package com.coachhe.剑指offer.editor.cn.code.二叉树.二叉搜索树;

import com.coachhe.leetcode.editor.cn.二叉树.TreeNode;
import org.testng.annotations.Test;

/**
 * @author CoachHe
 * @date 2022/8/23 02:32
 **/
public class P53_二叉搜索树中的中序后继 {

    @Test
    public void test() {
        Solution solution = new Solution();
//        TreeNode root = new TreeNode(2);
//        root.left = new TreeNode(1);
//        root.right = new TreeNode(3);
//        TreeNode root = new TreeNode(5);
//        root.left = new TreeNode(3);
//        root.right = new TreeNode(6);
//        root.left.left = new TreeNode(2);
//        root.left.right = new TreeNode(4);
//        root.left.left.left = new TreeNode(1);
        TreeNode root = new TreeNode(2);
        root.left = new TreeNode(1);
//        root.right = new TreeNode(3);
        System.out.println(solution.inorderSuccessor(root, root).val);
    }

    class Solution {
        public TreeNode inorderSuccessor(TreeNode root, TreeNode p) {
            TreeNode successor = null;
            if (p.right != null) {
                successor = p.right;
                while (successor.left != null) {
                    successor = successor.left;
                }
                return successor;
            }
            TreeNode node = root;
            while (node != null) {
                if (node.val > p.val) {
                    successor = node;
                    node = node.left;
                } else {
                    node = node.right;
                }
            }
            return successor;
        }
    }

}
