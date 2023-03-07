# 思路
事实上，这里有两种方式可以做这题，一种是非常简单，就创建一个长度为2n的数组，
一直往里面加数据就可以了，非常简单。代码如下：

```java
class Solution {
    public int[] shuffle(int[] nums, int n) {
		int[] resResult = new int[2 * n];
		int xIndex = 0;
		int yIndex = n;
		int i = 0;
		while (i < 2 * n) {
			resResult[i++] = nums[xIndex++];
			resResult[i++] = nums[yIndex++];
		}
		return resResult;
    }
}
```

还有一种看了答案之后有一个做法，震惊！

### 原文
因为题目限制了每一个元素 `nums[i]` 最大只有可能是 1000，这就意味着每一个元素只占据了 10 个 bit。（`2^10 - 1 = 1023 > 1000`）

而一个 int 有 32 个 bit，所以我们还可以使用剩下的 22 个 bit 做存储。实际上，每个 int，我们再借 10 个 bit 用就好了。

因此，有这样一个思路，每一个 `nums[i]` 的最低的十个 bit（0-9 位），我们用来存储原来 `nums[i]` 的数字；再往前的十个 bit（10-19 位），我们用来存储重新排列后正确的数字是什么。

在循环中，我们每次首先计算 `nums[i]` 对应的重新排列后的索引 j，之后，取 `nums[i]` 的低 10 位（`nums[i] & 1023`），即 `nums[i]` 的原始信息，把他放到 `nums[j]` 的高十位上。

最后，每个元素都取高 10 位的信息(e >> 10)，即是答案。

```txt
作者：liuyubobobo
链接：https://leetcode.cn/problems/shuffle-the-array/solution/kong-jian-fu-za-du-wei-o1-de-liang-chong-jie-fa-by/
来源：力扣（LeetCode）
著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
```

代码如下：
```java
class Solution {
    public int[] shuffle(int[] nums, int n) {
        int xIndex = 0;
        int yIndex = n;
        int i = 0;
        while (i < 2 * n) {
            nums[i++] |= (nums[xIndex++] & 1023) << 10;
            nums[i++] |= (nums[yIndex++] & 1023) << 10;
        }
        i = 0;
        while (i < 2 * n){
            nums[i++] >>= 10;
        }
        return nums;
    }
}
```

解释：

在这里，`(nums[i] & 1023) << 10`代表着将这个数首先取后10位，
然后再将这10位数向左移动。

注意，因为`n <= 1000`，所以n一定不会占用超过最初10位数字的位置，
所以`nums[j] |= (nums[i] & 1023) << 10`代表着`nums[i]`的内容存在了第11到第20位上，
`nums[j]`的内容存在了第0位到第9位上。

所以最后取的时候只需要取到全部第10位到第19位上的数字。

可以看到，最后代码的呈现上，其实两种方式非常类似，不同之处只在于第一种方式专门开辟了一个数组来存放数据。
第二种方式是将数据存在了原来的数组上，相当于一个数组存放了两份数据，这样就完美结局了问题，真的牛逼！















