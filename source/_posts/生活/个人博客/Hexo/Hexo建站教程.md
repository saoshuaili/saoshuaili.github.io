---
title: Hexo建站教程  
date: 2023-03-12  
tags: []  
---

为了建立个人博客，来回折腾了好多次，这次终于是找到了一个最符合我需求，个人认为最完美的一个博客方案了。

# 效果

效果如下：[CoachHe's Blog](https://coachhe.cn:8081/) 或者 [CoachHe's Blog](https://coachhe.github.io/)

对应的 github 仓库为：[CoachHe/coachhe.github.io](https://github.com/CoachHe/coachhe.github.io)

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

## 额外功能

具体的功能配置都在官网可以找到：[配置指南 | Hexo Fluid 用户手册 (fluid-dev.com)](https://hexo.fluid-dev.com/docs/guide/#%E5%85%B3%E4%BA%8E%E6%8C%87%E5%8D%97)

这里我针对几个展开讲解：

### 安装评论等功能

前面的都是参照了 EsunR-Blog 的内容，接下来我自己补充了一些功能，例如这块是需要新增评论等功能。

编辑 `_config.fluid.yml` 文件，找到文章页。

```yml
#---------------------------
# 文章页
# Post Page
#---------------------------
```

下面的

```yml
  # 评论插件
  # Comment plugin
  comments:
    enable: false
    # 指定的插件，需要同时设置对应插件的必要参数
    # The specified plugin needs to set the necessary parameters at the same time
    # Options: utterances | disqus | gitalk | valine | waline | changyan | livere | remark42 | twikoo | cusdis | giscus
    type: disqus
```

将 enable 改为 true，也就是：

```yml
  # 评论插件
  # Comment plugin
  comments:
    enable: true
    # 指定的插件，需要同时设置对应插件的必要参数
    # The specified plugin needs to set the necessary parameters at the same time
    # Options: utterances | disqus | gitalk | valine | waline | changyan | livere | remark42 | twikoo | cusdis | giscus
    type: disqus
```

这样评论功能就打开了，type 这里我选择的是 disqus，效果就是最开始的时候我展示的效果，其实 twikoo 是更好的，也不需要外网支持，不过需要依赖腾讯云的云开发，现在没有免费的了，一个月要 20 块，想想还是划不来，所以选择了 disqus，这里只需要去注册一个账号就可以了，很方便。

### 新增友链

新增友链功能更简单，打开 `_config.fluid.yml`，找到 menu

```yml
  # 导航栏菜单，可自行增减，key 用来关联 languages/*.yml，如不存在关联则显示 key 本身的值；icon 是 css class，可以省略；增加 name 可以强制显示指定名称
  # Navigation bar menu. `key` is used to associate languages/*.yml. If there is no association, the value of `key` itself will be displayed; if `icon` is a css class, it can be omitted; adding `name` can force the display of the specified name
  menu:
    - { key: "home", link: "/", icon: "iconfont icon-home-fill" }
    - { key: "archive", link: "/archives/", icon: "iconfont icon-archive-fill" }
    - { key: "category", link: "/categories/", icon: "iconfont icon-category-fill" }
    - { key: "tag", link: "/tags/", icon: "iconfont icon-tags-fill" }
    - { key: "about", link: "/about/", icon: "iconfont icon-user-fill" }
    #- { key: "links", link: "/links/", icon: "iconfont icon-link-fill" }
```

将 key 为 links 的那行展开，然后如果要打开评论区等功能，就找到下面的友链页面，

```yml
#---------------------------
# 友链页
# Links Page
#---------------------------
```

里面的 Comments：

```yml
  # 评论插件
  # Comment plugin
  comments:
    enable: false
    # 指定的插件，需要同时设置对应插件的必要参数
    # The specified plugin needs to set the necessary parameters at the same time
    # Options: utterances | disqus | gitalk | valine | waline | changyan | livere | remark42 | twikoo | cusdis | giscus
    type: disqus
```

同样将 false 改为 true 即可。这样评论功能也就打开了

### 新增留言页

因为留言页不像友链这样，已经预设好了，因此需要手动进行创建。首先在根目录下使用 hexo 命令创建一个新的页面：

```shell
hexo new page remark
```

然后更新 `source/remark/index.md` 的内容：

```md
---
title: 留言板
date: 2023-03-12 15:46:47
comment: 'disqus'
---

欢迎留言，请留下邮箱方便我回复您！
```

注意，这里 comment 必须要加上，否则不会打开留言功能。

最后再 `_config.fluid.yml` 文件中的 menu 属性中新增对应的 key：

```yml
  # 导航栏菜单，可自行增减，key 用来关联 languages/*.yml，如不存在关联则显示 key 本身的值；icon 是 css class，可以省略；增加 name 可以强制显示指定名称
  # Navigation bar menu. `key` is used to associate languages/*.yml. If there is no association, the value of `key` itself will be displayed; if `icon` is a css class, it can be omitted; adding `name` can force the display of the specified name
  menu:
    - { key: "home", link: "/", icon: "iconfont icon-home-fill" }
    - { key: "archive", link: "/archives/", icon: "iconfont icon-archive-fill" }
    - { key: "category", link: "/categories/", icon: "iconfont icon-category-fill" }
    - { key: "tag", link: "/tags/", icon: "iconfont icon-tags-fill" }
    - { key: "about", link: "/about/", icon: "iconfont icon-user-fill" }
    - { key: "remark", link: "/remark/", icon: "iconfont icon-user-fill", name: "留言"}
    - { key: "links", link: "/links/", icon: "iconfont icon-link-fill" }
```

可以看到，有一个 key 为 remark 的，就是我们对应的留言界面，这里我新增了一个 name 属性，否则默认名会是 remark。

至此，基本功能已经完成。另外还有一个很重要的，就是怎么在不同设备中同步 obsidian，因为这块部署在 github 中的其实是 release 这个 branch，但是我们本地存放原始 md 笔记的其实不是这个 branch，那么怎么同步呢？ 这里有一个对应的教程：

### Obsiidan 多端同步

[(24条消息) hexo博客 多终端同步使用_hexo多端同步_Mliak的博客-CSDN博客](https://blog.csdn.net/qq_34291777/article/details/98723401)

# 总结

还是非常棒的，目前使用方式是：

1. 新建一个笔记
2. 使用 obsidian-git 会自动同步到远端
3. 当闲暇时，登陆远端，执行：`hexo clean & hexo g & hexo d`，会生成同步到 github 的配置文件，并且会将笔记加好目录
4. 在远端执行 `git add . & git commit -m "reformat category" & git push`
5. 最后在本地执行 `git 