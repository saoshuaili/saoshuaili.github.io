//给你二叉树的根节点 root ，返回其节点值的 层序遍历 。 （即逐层地，从左到右访问所有节点）。 
//
// 
//
// 示例 1： 
// 
// 
//输入：root = [3,9,20,null,null,15,7]
//输出：[[3],[9,20],[15,7]]
// 
//
// 示例 2： 
//
// 
//输入：root = [1]
//输出：[[1]]
// 
//
// 示例 3： 
//
// 
//输入：root = []
//输出：[]
// 
//
// 
//
// 提示： 
//
// 
// 树中节点数目在范围 [0, 2000] 内 
// -1000 <= Node.val <= 1000 
// 
//
// Related Topics 树 广度优先搜索 二叉树 👍 1428 👎 0


package com.coachhe.leetcode.editor.cn.二叉树.遍历.P102_二叉树的层序遍历;

import com.coachhe.leetcode.editor.cn.二叉树.TreeNode;
import org.testng.annotations.Test;

import java.util.*;

/**
 * 二叉树的层序遍历
 *
 * @author CoachHe
 * @date 2022-08-19 02:20:11
 */
public class P102_BinaryTreeLevelOrderTraversal {
    @Test
    public void test() {
        //测试代码
        Solution solution = new P102_BinaryTreeLevelOrderTraversal().new Solution();
        TreeNode root = new TreeNode(3);
        root.left = new TreeNode(9);
        root.right = new TreeNode(20);
        root.right.left = new TreeNode(15);
        root.right.right = new TreeNode(7);
        List<List<Integer>> list = new ArrayList<>();
        list = solution.levelOrder(root);
        System.out.println(list);
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
        // 第一种方式，两个队列
//    public List<List<Integer>> levelOrder(TreeNode root) {
//		List<List<Integer>> resList = new ArrayList<>();
//		if (root == null) {
//			return resList;
//		}
//		List<Integer> list = new ArrayList<>();
//		Queue<TreeNode> queue1 = new LinkedList<>();
//		Queue<TreeNode> queue2 = new LinkedList<>();
//		queue1.add(root);
//		while (!queue1.isEmpty()) {
//			while (!queue1.isEmpty()) {
//				TreeNode node = queue1.remove();
//				list.add(node.val);
//				if (node.left != null) {
//					queue2.add(node.left);
//				}
//				if (node.right!= null) {
//					queue2.add(node.right);
//				}
//			}
//			List<Integer> tmpList = list;
//			resList.add(tmpList);
//			list = new ArrayList<>();
//			queue1 = queue2;
//			queue2 = new LinkedList<>();
//		}
//		return resList;
//    }


        // 方法2，使用hashMap记录每个节点对应的层数，然后遍历时发现下一层节点时证明上一层遍历结束，那么结算上一层
//        public List<List<Integer>> levelOrder(TreeNode root) {
//            List<List<Integer>> resList = new ArrayList<>();
//            if (root == null) {
//                return resList;
//            }
//            Queue<TreeNode> queue = new LinkedList<>();
//            Map<TreeNode, Integer> map = new HashMap<>();
//            queue.add(root);
//            map.put(root, 1);
//            int curLevel = 1;
//            int maxNodes = -1;
//            int nowNodes = 0;
//            List<Integer> list = new ArrayList<>();
//            while (!queue.isEmpty()) {
//                TreeNode node = queue.remove();
//                int nodeLevel = map.get(node);
//                // 若两者不相等，则代表遍历到了下一行，那么结算
//                if (curLevel != nodeLevel) {
//                    curLevel = nodeLevel;
//                    maxNodes = Math.max(maxNodes, nowNodes);
//                    List<Integer> newList = list;
//                    resList.add(newList);
//                    list = new ArrayList<>();
//                }
//                // 若相等，则代表还在本行
//                list.add(node.val);
//                nowNodes++;
//                if (node.left != null) {
//                    map.put(node.left, nodeLevel + 1);
//                    queue.add(node.left);
//                }
//                if (node.right != null) {
//                    map.put(node.right, nodeLevel + 1);
//                    queue.add(node.right);
//                }
//            }
//            resList.add(list);
//            return resList;
//        }

        // 方法3，记录当前层的最后一个节点和上一层的最后一个节点。
//        public List<List<Integer>> levelOrder(TreeNode root) {
//            TreeNode curLevelLastNode = root;
//            TreeNode nextLevelLastNode = null;
//            List<List<Integer>> resList = new ArrayList<>();
//            List<Integer> list = new ArrayList<>();
//            Queue<TreeNode> queue = new LinkedList<>();
//            if (root == null) {
//                return resList;
//            }
//            queue.add(root);
//            while (!queue.isEmpty()) {
//                TreeNode node = queue.remove();
//                list.add(node.val);
//                if (node.left!= null) {
//                    queue.add(node.left);
//                    nextLevelLastNode = node.left;
//                }
//                if (node.right!= null) {
//                    queue.add(node.right);
//                    nextLevelLastNode = node.right;
//                }
//                if (node == curLevelLastNode) {
//                    curLevelLastNode = nextLevelLastNode;
//                    List<Integer> newList = list;
//                    resList.add(newList);
//                    list = new ArrayList<>();
//                }
//            }
//            return resList;
//        }


        // 方法4，构造一个新类，用左神给的通用方法解决
        public List<List<Integer>> levelOrder(TreeNode root) {
            List<List<Integer>> resList = new ArrayList<>();
            if (root == null) {
                return resList;
            }
            List<Integer> list = new ArrayList<>();
            Queue<HelpClass> queue = new LinkedList<>();
            queue.add(new HelpClass(0, root));
            int curLevel = 0;
            while (!queue.isEmpty()) {
                HelpClass nowClass = queue.poll();
                if (nowClass.depth != curLevel) {
                    curLevel++;
                    resList.add(list);
                    list = new ArrayList<>();
                }
                list.add(nowClass.node.val);
                if (nowClass.node.left != null) {
                    queue.add(new HelpClass(nowClass.depth + 1, nowClass.node.left));
                }
                if (nowClass.node.right != null) {
                    queue.add(new HelpClass(nowClass.depth + 1, nowClass.node.right));
                }
            }
            resList.add(list);
            return resList;
        }

        // 构造的新类，记录了当前节点的层数和节点值，其实和map是一个道理
        class HelpClass {
            public int depth;
            public TreeNode node;

            public HelpClass(int depth, TreeNode node) {
                this.depth = depth;
                this.node = node;
            }
        }
        }
//leetcode submit region end(Prohibit modification and deletion)

}
