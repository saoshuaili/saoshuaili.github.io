---
title: 容器内打开Obsidian教程
tags: []
categories:
  - 程序员
  - 工具
  - obsidian
date: 2023-03-06 20:31:12
---

# 下载 Obsidian Linux 版本

[Obsidian](https://obsidian.md/)

<img src=" https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/Redis/20230306202341.png" width = "100%" />

注意，这里要选择 AppImage 版本。

# 解压 AppImage 文件

命令为：

```shell
chmod u+x Obsidian-1.1.16.AppImage # 赋予执行权限
./Obsidian-1.1.16.AppImage --appimage-extract  # 解压文件
```

此时会获得一个文件夹：

<img src=" https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/Redis/20230306202531.png" width = "50%" />

# 升级 nss

```shell
yum update nss -y
```

这步不知道为啥要，反正执行就是了

# 进入文件夹运行程序

```shell
cd squashfs-root
./obsidian --no-sandbox
```

注意，这步必须要在有图形界面的环境中执行，不能直接在命令行中执行。

# 注意

这里执行完成之后，我们就打开 obsidian 了，此时注意必须要更新一个配置：

<img src=" https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/Redis/20230306202747.png" width = "100%" />

这里需要关闭硬件加速，这是因为硬件加速的原理是使用 GPU 让 Obsidian 运行更流畅，但是 linux 的图形界面不存在 GPU 概念，因此不能使用，否则会卡死。

现在就可以开始美滋滋地使用 linux 版本 obsidian 啦。




