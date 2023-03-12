---
title: Hexo建站教程  
date: 2023-03-12  
tags: []  
---

为了建立个人博客，来回折腾了好多次，这次终于是找到了一个最符合我需求，个人认为最完美的一个博客方案了。

# 效果

效果如下：[CoachHe's Blog](https://coachhe.cn:8081/) 或者 [CoachHe's Blog](https://coachhe.github.io/)

## 首页 

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/Redis/20230312195147.png" width = "50%" />

简单清爽的首页

## 归档

会根据具体文件的上传日期记录笔记，相当于一个流水线功能

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/Redis/20230312195249.png" width = "50%" />

## 分类

**会根据你本地文件夹的格式来进行分类**，这点真的完美！

远端：

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/Redis/20230312195537.png" width = "50%" />

本地：

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/Redis/20230312195639.png" width = "50%" />

可以看到，远端和本地的目录结构是完全相同的。

## 标签

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/Redis/20230312195730.png" width = "50%" />

这里只有一个 BigData 标签是因为我为了测试只做了这个标签，有多个标签的时候完全是没问题的。

## 留言

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/Redis/20230312195824.png" width = "50%" />

## 正文

最重要的当然是正文，这里是使用 markdown 语言，并且也支持评论等功能。

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/Redis/20230312195924.png" width = "50%" />

下面我仔细说说

# 需求

本人腾讯打工的码农一枚，日常学习时会通过 obsidian 做一些笔记，使用 obsidian-git 来进行同步，理论上足以满足日常跨设备同步+查看的需求。但是总感觉自己做的笔记如果不放在网络端查看少了一些仪式感。但是如果自己建一个博客或者传到知乎上，又会感觉很麻烦，因为每次写完笔记还需要去各个平台同步。

因此，是否有一种方案可以满足我写完笔记，就可以用很简单的步骤一键部署到云端和查看呢？ 并且需要满足以下功能：

1. 文档目录会跟你本地文件的文件夹格式相同，避免大量的格式整理
2. 正文需要有评论功能
3. 需要有留言功能
4. 需要友链等一系列功能支持

之前折腾了好几次，都不太满足自身需求，直到现在找到了 Hexo + Github + Obsidian 的这种方式，能完美实现我的所有需求。

# 所需工具

1. 云服务器一台，本人使用的是腾讯云的轻量服务器，位置在香港，2 核 2 g，感觉已经完全可以满足日常需求，不过现在香港的好像没货了
2. 安装了 obsidian-git 的 obsidian
3. Github 账号

# 安装教程

## 安装 hexo

首先肯定需要 hexo 这个博客软件，具体教程很多，我就不赘述了，直接给个网站：[Hexo 快速搭建指南 | EsunR-Blog](https://blog.esunr.xyz/2022/06/64163235c30f.html#1-%E5%AE%89%E8%A3%85%E4%B8%8E%E4%BD%BF%E7%94%A8-Hexo)

## 快速配置 hexo+obsidian+git 实现一键部署和同步上传 github

这块我也是参照了这个很详细的教程：[Hexo + Obsidian + Git 完美的博客部署与编辑方案 | EsunR-Blog](https://blog.esunr.xyz/2022/07/e9b42b453d9f.html#1-%E5%89%8D%E8%A8%80)




