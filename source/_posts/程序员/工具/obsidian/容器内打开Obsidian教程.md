# 下载 Obsidian Linux 版本

[Obsidian](https://obsidian.md/)

<img src=" https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/Redis/20230306202341.png" width = "50%" />

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

注意，这步必须要在有图形界面的环境中执行，不能直接

# 注意

这里执行完成之后，我们需要打开

