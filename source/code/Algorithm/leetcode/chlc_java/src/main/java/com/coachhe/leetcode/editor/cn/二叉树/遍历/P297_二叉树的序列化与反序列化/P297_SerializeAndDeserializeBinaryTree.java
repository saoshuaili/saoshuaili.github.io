//序列化是将一个数据结构或者对象转换为连续的比特位的操作，进而可以将转换后的数据存储在一个文件或者内存中，同时也可以通过网络传输到另一个计算机环境，采取相反方
//式重构得到原数据。 
//
// 请设计一个算法来实现二叉树的序列化与反序列化。这里不限定你的序列 / 反序列化算法执行逻辑，你只需要保证一个二叉树可以被序列化为一个字符串并且将这个字符串
//反序列化为原始的树结构。 
//
// 提示: 输入输出格式与 LeetCode 目前使用的方式一致，详情请参阅 LeetCode 序列化二叉树的格式。你并非必须采取这种方式，你也可以采用其他的
//方法解决这个问题。 
//
// 
//
// 示例 1： 
// 
// 
//输入：root = [1,2,3,null,null,4,5]
//输出：[1,2,3,null,null,4,5]
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
// 示例 4： 
//
// 
//输入：root = [1,2]
//输出：[1,2]
// 
//
// 
//
// 提示： 
//
// 
// 树中结点数在范围 [0, 10⁴] 内 
// -1000 <= Node.val <= 1000 
// 
//
// Related Topics 树 深度优先搜索 广度优先搜索 设计 字符串 二叉树 👍 955 👎 0


package com.coachhe.leetcode.editor.cn.二叉树.遍历.P297_二叉树的序列化与反序列化;

import com.coachhe.leetcode.editor.cn.二叉树.TreeNode;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * 二叉树的序列化与反序列化
 *
 * @author CoachHe
 * @date 2022-08-24 01:35:59
 */
public class P297_SerializeAndDeserializeBinaryTree {
    @Test
    public void test() {
        //测试代码
        Codec solution = new Codec();
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);
        root.right.left = new TreeNode(4);
        root.right.right = new TreeNode(5);
        String data = solution.serialize(root);
        TreeNode node = solution.deserialize(data);
        System.out.println(node.val);
    }

//力扣代码
//leetcode submit region begin(Prohibit modification and deletion)

    /**
     * Definition for a binary tree node.
     * public class TreeNode {
     * int val;
     * TreeNode left;
     * TreeNode right;
     * TreeNode(int x) { val = x; }
     * }
     */
    public class Codec {

        // Encodes a tree to a single string.
        public String serialize(TreeNode root) {
            if (root == null) {
                return "#";
            }
            int nowNode = root.val;
            String leftSerial = serialize(root.left);
            String rightSerial = serialize(root.right);
            return nowNode + "_" + leftSerial + "_" + rightSerial;
        }

        // Decodes your encoded data to tree.
        public TreeNode deserialize(String data) {
            String[] arrays = data.split("_");
            List<String> list = new LinkedList<>(Arrays.asList(arrays));
            return rds(list);
        }

        public TreeNode rds(List<String> list) {
            if (list.get(0).equals("#")){
                list.remove(0);
                return null;
            }
            TreeNode root = new TreeNode(Integer.parseInt(list.get(0)));
            list.remove(0);
            root.left = rds(list);
            root.right = rds(list);
            return root;
        }



    }

// Your Codec object will be instantiated and called as such:
// Codec ser = new Codec();
// Codec deser = new Codec();
// TreeNode ans = deser.deserialize(ser.serialize(root));
//leetcode submit region end(Prohibit modification and deletion)

}
