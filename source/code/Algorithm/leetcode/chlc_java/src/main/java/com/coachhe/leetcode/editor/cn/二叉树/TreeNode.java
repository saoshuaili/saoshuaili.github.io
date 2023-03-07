package com.coachhe.leetcode.editor.cn.二叉树;

/**
 * @author CoachHe
 * @date 2022/8/18 01:07
 **/
public class TreeNode {
    public int val;
    public TreeNode left;
    public TreeNode right;
    public TreeNode() {}
    public TreeNode(int val) { this.val = val; }
    public TreeNode(int val, TreeNode left, TreeNode right) {
        this.val = val;
        this.left = left;
        this.right = right;
    }
}