### 两种方式解

1. 左神提供的，一个辅助类，里面存放左子树的最大值，最小值以及是否是搜索二叉树的boolean类型
2. 搜索二叉树有一个特点就是中序遍历之后一定是单调递增的，那么可以中序遍历的时候查看值是否一直增长。

在这里两种方式我都做一遍

首先是辅助类：
```java
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
}
```

在这里要注意，如果是使用中序遍历，那么初始值的设置一定要注意。因为最开始会设置一个最小值，然后在遍历时不断比较这个最小值，如果发现遍历中的值小于等于这个最小值那么直接报错。
这样思路是没错的，但是如果这个最小值设置成`Integer.MIN_VALUE`，那么如果开头那个值就是这个最小值，那么直接报错了。

因此需要设置为DOUBLE的最小值。代码如下：
```java
class Solution {
    public boolean isValidBST(TreeNode root) {
        if (root == null || (root.left == null && root.right == null)) {
            return true;
        }
        Stack<TreeNode> stack = new Stack<>();
        while (root != null) {
            stack.push(root);
            root = root.left;
        }
        Double tmpMax = -Double.MAX_VALUE;
        while (!stack.isEmpty()) {
            TreeNode node = stack.pop();
            if (tmpMax >= node.val) {
                return false;
            }
            tmpMax = Double.valueOf(node.val);
            if (node.right != null) {
                stack.push(node.right);
                node = node.right;
                while (node.left != null) {
                    stack.push(node.left);
                    node = node.left;
                }
            }
        }
        return true;
    }
}
```