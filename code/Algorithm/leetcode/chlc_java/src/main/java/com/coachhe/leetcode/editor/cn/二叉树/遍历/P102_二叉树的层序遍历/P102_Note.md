左神在

[B站视频](https://www.bilibili.com/video/BV13g41157hK?p=7&vd_source=6678074a8e5a52b13a742aa7a335eb49)

中给出了两种解决方式。
1. 使用HashMap记录所有节点对应的层数，遍历时记录
2. 记录当前层的最后一个节点和下一层的最后一个节点，遍历时不断比较当前节点是否为当前层的最后一个节点。
3. 但是我还有一个方式，在这里用方法1给出，就是利用两个队列，当前行的所有节点进队列，然后出队列的节点的左右孩子都进另一个队列，然后不断周而复始更新两个队列。

也是可以的，在这里把三种方式都做一遍

还有最后，根据左神的方法，构造一个新的类，记录了每个节点和对应的层数，其实和map是一个道理
```java
class HelpClass {
    public int depth;
    public TreeNode node;
    public HelpClass(int depth, TreeNode node) {
        this.depth = depth;
        this.node = node;
    }
}
```
不断往左右递归，获取当前节点左孩子和右孩子的状态。记录在队列中。


## 代码
### 方法1
```java
    class Solution {
        // 第一种方式，两个队列
    public List<List<Integer>> levelOrder(TreeNode root) {
		List<List<Integer>> resList = new ArrayList<>();
		if (root == null) {
			return resList;
		}
		List<Integer> list = new ArrayList<>();
		Queue<TreeNode> queue1 = new LinkedList<>();
		Queue<TreeNode> queue2 = new LinkedList<>();
		queue1.add(root);
		while (!queue1.isEmpty()) {
			while (!queue1.isEmpty()) {
				TreeNode node = queue1.remove();
				list.add(node.val);
				if (node.left != null) {
					queue2.add(node.left);
				}
				if (node.right!= null) {
					queue2.add(node.right);
				}
			}
			List<Integer> tmpList = list;
			resList.add(tmpList);
			list = new ArrayList<>();
			queue1 = queue2;
			queue2 = new LinkedList<>();
		}
		return resList;
    }
}
```

### 方法2
```java
    class Solution {


        // 方法2，使用hashMap记录每个节点对应的层数，然后遍历时发现下一层节点时证明上一层遍历结束，那么结算上一层
        public List<List<Integer>> levelOrder(TreeNode root) {
            List<List<Integer>> resList = new ArrayList<>();
            if (root == null) {
                return resList;
            }
            Queue<TreeNode> queue = new LinkedList<>();
            Map<TreeNode, Integer> map = new HashMap<>();
            queue.add(root);
            map.put(root, 1);
            int curLevel = 1;
            int maxNodes = -1;
            int nowNodes = 0;
            List<Integer> list = new ArrayList<>();
            while (!queue.isEmpty()) {
                TreeNode node = queue.remove();
                int nodeLevel = map.get(node);
                // 若两者不相等，则代表遍历到了下一行，那么结算
                if (curLevel != nodeLevel) {
                    curLevel = nodeLevel;
                    maxNodes = Math.max(maxNodes, nowNodes);
                    List<Integer> newList = list;
                    resList.add(newList);
                    list = new ArrayList<>();
                }
                // 若相等，则代表还在本行
                list.add(node.val);
                nowNodes++;
                if (node.left != null) {
                    map.put(node.left, nodeLevel + 1);
                    queue.add(node.left);
                }
                if (node.right != null) {
                    map.put(node.right, nodeLevel + 1);
                    queue.add(node.right);
                }
            }
            resList.add(list);
            return resList;
        }
}
```

### 方法3
```java
    class Solution {

        // 方法3，记录当前层的最后一个节点和上一层的最后一个节点。
        public List<List<Integer>> levelOrder(TreeNode root) {
            TreeNode curLevelLastNode = root;
            TreeNode nextLevelLastNode = null;
            List<List<Integer>> resList = new ArrayList<>();
            List<Integer> list = new ArrayList<>();
            Queue<TreeNode> queue = new LinkedList<>();
            if (root == null) {
                return resList;
            }
            queue.add(root);
            while (!queue.isEmpty()) {
                TreeNode node = queue.remove();
                list.add(node.val);
                if (node.left!= null) {
                    queue.add(node.left);
                    nextLevelLastNode = node.left;
                }
                if (node.right!= null) {
                    queue.add(node.right);
                    nextLevelLastNode = node.right;
                }
                if (node == curLevelLastNode) {
                    curLevelLastNode = nextLevelLastNode;
                    List<Integer> newList = list;
                    resList.add(newList);
                    list = new ArrayList<>();
                }
            }
            return resList;
        }
}
```

### 方法4
```java
    class Solution {
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
```