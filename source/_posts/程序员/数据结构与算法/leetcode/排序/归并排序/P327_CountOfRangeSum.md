---
categories:
  - 程序员
  - 数据结构与算法
  - leetcode
  - 排序
  - 归并排序
---
<p>给你一个整数数组&nbsp;<code>nums</code> 以及两个整数&nbsp;<code>lower</code> 和 <code>upper</code> 。求数组中，值位于范围 <code>[lower, upper]</code> （包含&nbsp;<code>lower</code>&nbsp;和&nbsp;<code>upper</code>）之内的 <strong>区间和的个数</strong> 。</p>

<p><strong>区间和</strong>&nbsp;<code>S(i, j)</code>&nbsp;表示在&nbsp;<code>nums</code>&nbsp;中，位置从&nbsp;<code>i</code>&nbsp;到&nbsp;<code>j</code>&nbsp;的元素之和，包含&nbsp;<code>i</code>&nbsp;和&nbsp;<code>j</code>&nbsp;(<code>i</code> ≤ <code>j</code>)。</p>

<p>&nbsp;</p> 
<strong>示例 1：</strong>

<pre>
<strong>输入：</strong>nums = [-2,5,-1], lower = -2, upper = 2
<strong>输出：</strong>3
<strong>解释：</strong>存在三个区间：[0,0]、[2,2] 和 [0,2] ，对应的区间和分别是：-2 、-1 、2 。
</pre>

<p><strong>示例 2：</strong></p>

<pre>
<strong>输入：</strong>nums = [0], lower = 0, upper = 0
<strong>输出：</strong>1
</pre>

<p>&nbsp;</p>

<p><strong>提示：</strong></p>

<ul> 
 <li><code>1 &lt;= nums.length &lt;= 10<sup>5</sup></code></li> 
 <li><code>-2<sup>31</sup> &lt;= nums[i] &lt;= 2<sup>31</sup> - 1</code></li> 
 <li><code>-10<sup>5</sup> &lt;= lower &lt;= upper &lt;= 10<sup>5</sup></code></li> 
 <li>题目数据保证答案是一个 <strong>32 位</strong> 的整数</li> 
</ul>

<div><div>Related Topics</div><div><li>树状数组</li><li>线段树</li><li>数组</li><li>二分查找</li><li>分治</li><li>有序集合</li><li>归并排序</li></div></div><br><div><li>👍 470</li><li>👎 0</li></div>