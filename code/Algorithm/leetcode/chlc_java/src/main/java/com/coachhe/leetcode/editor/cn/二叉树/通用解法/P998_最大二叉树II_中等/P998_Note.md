# 思路

假设一个头节点root对应的数组：

`[5,2,3,null,1]`

那么结构是这样的：

```txt
   5
  / \
 2   3
  \
   1
```

对应的数组

`[2,1,5,3]`

当一个数字4输入时，数组变成了

`[2,1,5,3,4]`

可以看到，因为输入的数字4在最右侧，因此当头节点5往右边一直遍历的时候，
如果遇到的节点比4更大，那么啥也不用做，继续往右遍历即可，因为最大数的结构一定和没有4的时候是相同的。

但是遇到一个比4小的节点或者遍历结束了，那么意味着从这个节点开始，剩下数组中的所有部分都变成了4这个节点的左子树。

那么也很简单，就将当时对应的父节点记录下来，让他的右数变成4这个节点，然后当前的节点成为4这个节点的左子树即可。

代码如下:
```java
class Solution {
    public TreeNode insertIntoMaxTree(TreeNode root, int val) {
        if (root == null) {
            return new TreeNode(val);
        }
        TreeNode resRoot = new TreeNode();
        resRoot.right = root;
        // 实例化val所构成的节点
        TreeNode valNode = new TreeNode(val);
        // 判断第一个节点，如果第一个节点值就比val小，那么让其称为val的左节点，返回
        if (val > root.val) {
            valNode.left = root;
            return valNode;
        }
        // 无限循环，直到while有返回值
        while (true) {
            // 这一步的目的是记录父节点并且记录父节点对应的右边子节点
            TreeNode rightNode = resRoot.right;
            // 如果右边节点空了，那么直接放在右节点即可
            if (rightNode == null) {
                resRoot.right = valNode;
                return root;
            }
            // 如果发现右边节点的值还比当前val大，那么继续遍历
            if (rightNode.val > val) {
                resRoot = rightNode;
                continue;
            }
            // 当发现右边节点值比当前val小时，遍历结束，让右边节点称为valNode，
            // 同时让之前的右边节点rightNode成为valNode的左边节点，结束
            resRoot.right = valNode;
            valNode.left = rightNode;
            return root;
        }
    }
}

```