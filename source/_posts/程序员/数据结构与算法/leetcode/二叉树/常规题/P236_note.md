---
title: P236_note  
date: 2023-03-08 00:55:03  
tags: []  
categories:
  - 程序员
  - 数据结构与算法
  - leetcode
  - 二叉树
  - 常规题
---
### 二叉树的最近公共祖先

这次有两个思路。

1. 很简单，先随便用一个遍历，遍历过程中将所有节点的父节点记录下来。然后先用一个节点一直往上遍历，放入一个Set中，然后再将另一个节点也一直往上遍历，直到出现之前的Set中有的元素就结束，这个元素就是最近的公共祖先
2. 比较复杂。

