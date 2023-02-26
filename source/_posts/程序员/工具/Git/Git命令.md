---
title: Git命令  
date: 2022-12-04 02:36:45  
tags: []  
categories:
  - 工具
  - Git
---
# 主要部分

## 基础篇

### 1. git commit

Git 仓库中的提交记录保存的是你的目录下所有文件的快照，就像是把整个目录复制，然后再粘贴一样，但比复制粘贴优雅许多！

Git 希望提交记录尽可能地轻量，因此在你每次进行提交时，它并不会盲目地复制整个目录。条件允许的情况下，它会将当前版本与仓库中的上一个版本进行对比，并把所有的差异打包到一起作为一个提交记录。

Git 还保存了提交的历史记录。这也是为什么大多数提交记录的上面都有父节点的原因 —— 我们会在图示中用箭头来表示这种关系。对于项目组的成员来说，维护提交历史对大家都有好处。

关于提交记录太深入的东西咱们就不再继续探讨了，现在你可以把提交记录看作是项目的快照。提交记录非常轻量，可以快速地在这些提交记录之间切换

### 2. git branch

#### 语法

```shell
git branch branchName branchLocation
```

最后一个branchLocation是可选的，如果不写默认是当前所在节点位置，若写了则是某个节点的位置。

例如：

```shell
git branch bugWork main~1^2~1
```

该条语句的作用是去main节点的上一个节点的第二个父节点的父节点上创建一个名为branch的分支。



Git 的分支也非常轻量。它们只是简单地指向某个提交纪录 —— 仅此而已。所以许多 Git 爱好者传颂：

```
早建分支！多用分支！
```

这是因为即使创建再多的分支也不会造成储存或内存上的开销，并且按逻辑分解工作到不同的分支要比维护那些特别臃肿的分支简单多了。

在将分支和提交记录结合起来后，我们会看到两者如何协作。现在只要记住使用分支其实就相当于在说：“我想基于这个提交以及它所有的父提交进行新的工作。”

咱们通过实际操作来看看分支是什么样子的。

接下来，我们将要创建一个到名为 `newImage` 的分支。

```shell
git branch newImage
```

执行前：

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/image-20210613205614428.png" alt="image-20210613205614428" style="zoom:50%;" />

执行后：

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/image-20210613205629447.png" alt="image-20210613205629447" style="zoom:50%;" />

看到了吗，创建分支就是这么容易！新创建的分支 `newImage` 指向的是提交记录 `C1`。

此时执行：

```shell
git commit
```

结果：

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/image-20210613205704756.png" alt="image-20210613205704756" style="zoom:50%;" />

为什么 `main` 分支前进了，但 `newImage` 分支还待在原地呢？！这是因为我们没有“在”这个新分支上，看到 `main` 分支上的那个星号（*）了吗？这表示当前所在的分支是 `main`。

此时需要引用：

### 3. git checkout

现在咱们告诉 Git 我们想要切换到新的分支上

```
git checkout <name>
```

下面的命令会让我们在提交修改之前先切换到新的分支上

代码：

```shell
git checkout newImage
git commit
```

执行前：

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/image-20210613205812585.png" alt="image-20210613205812585" style="zoom:50%;" />

执行后：

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/image-20210613205826513.png" alt="image-20210613205826513" style="zoom:50%;" />

这就对了！我们的修改已经保存到新的分支里了。

#### 创建同时切换git checkout -b

如果你想创建一个新的分支同时切换到新创建的分支的话，可以通过 `git checkout -b <your-branch-name>` 来实现。

### 分支与合并

#### 1. git merge

在 Git 中合并两个分支时会产生一个特殊的提交记录，它有两个父节点。翻译成自然语言相当于：“我要把这两个父节点本身及它们所有的祖先都包含进来。”

我们准备了两个分支，每个分支上各有一个独有的提交。这意味着没有一个分支包含了我们修改的所有内容。咱们通过合并这两个分支来解决这个问题。

我们要把 `bugFix` 合并到 `main` 里：

```shell
git merge bugFix
```

合并前：

![](https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/20210613131618.png)

合并后：

![](https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/20210613131641.png)

首先，`main` 现在指向了一个拥有两个父节点的提交记录。假如从 `main` 开始沿着箭头向上看，在到达起点的路上会经过所有的提交记录。这意味着 `main` 包含了对代码库的所有修改。

咱们再把 `main` 分支合并到 `bugFix`：

```shell
git checkout bugFix
git merge main
```

##### 考核

要想通过这一关，需要以下几步：

- 创建新分支 `bugFix`
- 用 `git checkout bugFix` 命令切换到该分支
- 提交一次
- 用 `git checkout main` 切换回 `main`
- 再提交一次
- 用 `git merge` 把 `bugFix` 合并到 `main`

解答：

```shell
$ git checkout -b bugFix  ## -b可以创建该分支并且直接使用该分支

$ git commit  						## 在bugFix分区上提交一次

$ git checkout main				## 回到main分区

$ git commit							## 在main分区上提交，此时仍然在main分区

$ git merge bugFix				## 将bugFix分区和main分区合并
```



#### 2. git rebase

第二种合并分支的方法是 `git rebase`。Rebase 实际上就是取出一系列的提交记录，“复制”它们，然后在另外一个地方逐个的放下去。

Rebase 的优势就是可以创造更线性的提交历史，这听上去有些难以理解。如果只允许使用 Rebase 的话，代码库的提交历史将会变得异常清晰。

```shell
git rebase
```

使用前：

![](https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/20210613131652.png)

使用后：

![](https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/20210613131700.png)

现在 bugFix 分支上的工作在 main 的最顶端，同时我们也得到了一个更线性的提交序列。

注意，提交记录 C3 依然存在（树上那个半透明的节点），而 C3' 是我们 Rebase 到 main 分支上的 C3 的副本。

现在唯一的问题就是 main 还没有更新

现在我们切换到了 `main` 上。把它 rebase 到 `bugFix` 分支上

```shell
git rebase bugFix
```

![](https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/20210613131710.png)

由于 `bugFix` 继承自 `main`，所以 Git 只是简单的把 `main` 分支的引用向前移动了一下而已。

##### 考核

- 新建并切换到 `bugFix` 分支
- 提交一次
- 切换回 main 分支再提交一次
- 再次切换到 bugFix 分支，rebase 到 main 上

答案：

```shell
$ git checkout -b bugFix

$ git commit

$ git checkout main

$ git commit

$ git checkout bugFix

$ git rebase main
```

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/20210613132014.png" style="zoom:50%;" />

##### 注意

代码：

```shell
git rebase main bugFix
```

这句代码的含义是将bugFix移动到main分支的下面



## 高级篇

### 1. 在提交树上移动

#### HEAD

我们首先看一下 “HEAD”。 HEAD 是一个对当前检出记录的符号引用 —— 也就是指向你正在其基础上进行工作的提交记录。

**HEAD 总是指向当前分支上最近一次提交记录**。大多数修改提交树的 Git 命令都是从改变 HEAD 的指向开始的。

HEAD 通常情况下是指向分支名的（如 bugFix）。在你提交时，改变了 bugFix 的状态，这一变化通过 HEAD 变得可见。

##### HEAD的操作

```shell
git checkout C1
git checkout main
git commit
git checkout C2
```

执行前：

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/image-20210613145255682.png" alt="image-20210613145255682" style="zoom:50%;" />

执行后：

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/image-20210613145401223.png" alt="image-20210613145401223" style="zoom:50%;" />

看到了吗？ HEAD 指向了 `main`，随着提交向前移动。

实际这些命令并不是真的在查看 HEAD 指向，看下一屏就了解了。如果想看 HEAD 指向，可以通过 `cat .git/HEAD` 查看， 如果 HEAD 指向的是一个引用，还可以用 `git symbolic-ref HEAD` 查看它的指向。但是该程序不支持这两个命令

##### 分离的HEAD

分离的 HEAD 就是让其指向了某个具体的提交记录而不是分支名。

**命令：**

```shell
git checkout C1
```

**命令执行之前：**

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/image-20210613145624960.png" alt="image-20210613145624960" style="zoom:50%;" />

`HEAD -> main -> C1`

HEAD 指向 main， main 指向 C1

**命令执行之后：**

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/image-20210613145701043.png" alt="image-20210613145701043" style="zoom:50%;" />

`HEAD -> C1`

##### 考核

想完成此关，从 `bugFix` 分支中分离出 HEAD 并让其指向一个提交记录。

通过哈希值指定提交记录。每个提交记录的哈希值显示在代表提交记录的圆圈中。

```shell
git checkout C4
```

### 2. 相对引用

通过指定提交记录哈希值的方式在 Git 中移动不太方便。在实际应用时，并没有像本程序中这么漂亮的可视化提交树供你参考，所以你就不得不用 `git log` 来查查看提交记录的哈希值。

并且哈希值在真实的 Git 世界中也会更长（译者注：基于 SHA-1，共 40 位）。例如前一关的介绍中的提交记录的哈希值可能是 `fed2da64c0efc5293610bdd892f82a58e8cbc5d8`。舌头都快打结了吧...

比较令人欣慰的是，Git 对哈希的处理很智能。你只需要提供能够唯一标识提交记录的前几个字符即可。因此我可以仅输入`fed2` 而不是上面的一长串字符。

正如我前面所说，通过哈希值指定提交记录很不方便，所以 Git 引入了相对引用。这个就很厉害了!

使用相对引用的话，你就可以从一个易于记忆的地方（比如 `bugFix` 分支或 `HEAD`）开始计算。

相对引用非常给力，这里我介绍两个简单的用法：

- 使用 `^` 向上移动 1 个提交记录
- 使用 `~<num>` 向上移动多个提交记录，如 `~3`

#### 操作符(^)

首先看看操作符 (^)。把这个符号加在引用名称的后面，表示让 Git 寻找指定提交记录的父提交。

所以 `main^` 相当于“`main` 的父节点”。

`main^^` 是 `main` 的第二个父节点

现在咱们切换到 main 的父节点

```shell
git checkout main^
```

你也可以将 `HEAD` 作为相对引用的参照。下面咱们就用 `HEAD` 在提交树中向上移动几次。

```shell
git checkout C3
git checkout HEAD^
git checkout HEAD^
git checkout HEAD^
```

##### 考核

要完成此关，切换到 `bugFix` 的父节点。这会进入分离 `HEAD` 状态。

如果你愿意的话，使用哈希值也可以过关，但请尽量使用相对引用！

```shell
git checkout C4
```

#### 操作符(~)

“~”操作符

如果你想在提交树中向上移动很多步的话，敲那么多 `^` 貌似也挺烦人的，Git 当然也考虑到了这一点，于是又引入了操作符 `~`。

该操作符后面可以跟一个数字（可选，不跟数字时与 `^` 相同，向上移动一次），指定向上移动多少次。咱们还是通过实际操作看一下吧

代码：

```shell
git checkout HEAD~4
```

执行前：

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/image-20210613150354658.png" alt="image-20210613150354658" style="zoom:50%;" />

执行后：

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/20210613150410.png" style="zoom:50%;" />

强制修改分支位置

你现在是相对引用的专家了，现在用它来做点实际事情。

我使用相对引用最多的就是移动分支。可以直接使用 `-f` 选项让分支指向另一个提交。例如:

```shell
git branch -f main HEAD~3
```

上面的命令会将 main 分支强制指向 HEAD 的第 3 级父提交。

执行前：

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/image-20210613150511314.png" alt="image-20210613150511314" style="zoom:50%;" />

执行后：

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/image-20210613150531857.png" alt="image-20210613150531857" style="zoom:50%;" />

这就对了! 相对引用为我们提供了一种简洁的引用提交记录 `C1` 的方式， 而 `-f` 则容许我们将分支强制移动到那个位置。

##### 考核

要完成此关，移动 `HEAD`，`main` 和 `bugFix` 到目标所示的位置。

初始位置：

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/image-20210613151711954.png" alt="image-20210613151711954" style="zoom: 50%;" />

最终位置：

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/image-20210613151817891.png" alt="image-20210613151817891" style="zoom:50%;" />

代码：

```shell
$ git branch -f main c6

$ git branch -f bugFix c0

$ git checkout c1
```

### 3. 撤销变更

在 Git 里撤销变更的方法很多。和提交一样，撤销变更由底层部分（暂存区的独立文件或者片段）和上层部分（变更到底是通过哪种方式被撤销的）组成。我们这个应用主要关注的是后者。

主要有两种方法用来撤销变更 —— 一是 `git reset`，还有就是 `git revert`。接下来咱们逐个进行讲解。

#### 1. git reset

代码：

```shell
git reset HEAD~1
```

执行前：

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/image-20210613152200852.png" alt="image-20210613152200852" style="zoom:50%;" />

执行后：

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/image-20210613152216391.png" alt="image-20210613152216391" style="zoom:50%;" />

Git 把 main 分支移回到 `C1`；现在我们的本地代码库根本就不知道有 `C2` 这个提交了。

（译者注：在reset后， `C2` 所做的变更还在，但是处于未加入暂存区状态。）

#### 2. git revert

虽然在你的本地分支中使用 `git reset` 很方便，但是这种“改写历史”的方法对大家一起使用的远程分支是无效的哦！

为了撤销更改并**分享**给别人，我们需要使用 `git revert`。来看演示：

代码：

```shell
git revert HEAD
```

执行前：

如上

执行后：

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/image-20210613152358148.png" alt="image-20210613152358148" style="zoom:50%;" />

奇怪！在我们要撤销的提交记录后面居然多了一个新提交！这是因为新提交记录 `C2'` 引入了**更改** —— 这些更改刚好是用来撤销 `C2` 这个提交的。也就是说 `C2'` 的状态与 `C1` 是相同的。

revert 之后就可以把你的更改推送到远程仓库与别人分享啦。

#### 考核

要完成此关，分别撤销 `local` 分支和 `pushed` 分支上的最近一次提交。共需要撤销两个提交（每个分支一个）。

记住 `pushed` 是远程分支，`local` 是本地分支 —— 这么说你应该知道用分别哪种方法了吧？

初始图形：

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/image-20210613152521615.png" alt="image-20210613152521615" style="zoom:50%;" />

目标图形：

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/20210613152534.png" style="zoom:50%;" />

代码：

```shell
$ git reset local~1

$ git checkout pushed

$ git revert pushed
```

## 移动提交记录

到现在我们已经学习了 Git 的基础知识 —— 提交、分支以及在提交树上移动。 这些概念涵盖了 Git 90% 的功能，同样也足够满足开发者的日常需求

然而, 剩余的 10% 在处理复杂的工作流时(或者当你陷入困惑时）可能就显得尤为重要了。接下来要讨论的这个话题是“整理提交记录” —— 开发人员有时会说“我想要把这个提交放到这里, 那个提交放到刚才那个提交的后面”, 而接下来就讲的就是它的实现方式，非常清晰、灵活，还很生动。

看起来挺复杂, 其实是个很简单的概念。

### 1. git cherry-pick

本系列的第一个命令是 `git cherry-pick`, 命令形式为:

- `git cherry-pick <提交号>...`

如果你想将一些提交复制到当前所在的位置（`HEAD`）下面的话， Cherry-pick 是最直接的方式了。我个人非常喜欢 `cherry-pick`，因为它特别简单。

这里有一个仓库, 我们想将 `side` 分支上的工作复制到 `main` 分支，你立刻想到了之前学过的 `rebase` 了吧？但是咱们还是看看 `cherry-pick` 有什么本领吧。

代码：

```shell
git cherry-pick C2 C4
```

执行前：

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/image-20210613203911386.png" alt="image-20210613203911386" style="zoom:50%;" />

执行后：

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/image-20210613203950062.png" alt="image-20210613203950062" style="zoom:50%;" />

这就是了！我们只需要提交记录 `C2` 和 `C4`，所以 Git 就将被它们抓过来放到当前分支下了。 就是这么简单!

#### 考核

要通过此关, 只需要简单的将三个分支中的提交记录复制到 main 上就可以了。

原始图形：

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/image-20210613204140350.png" alt="image-20210613204140350" style="zoom:50%;" />

目标图形：

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/20210613204156.png" style="zoom:50%;" />

代码：

```shell
$ git cherry-pick bugFix c4 another
```

### 2. 交互式的rebase

当你知道你所需要的提交记录（**并且**还知道这些提交记录的哈希值）时, 用 cherry-pick 再好不过了 —— 没有比这更简单的方式了。

但是如果你不清楚你想要的提交记录的哈希值呢? 幸好 Git 帮你想到了这一点, 我们可以利用交互式的 rebase —— 如果你想从一系列的提交记录中找到想要的记录, 这就是最好的方法了

交互式 rebase 指的是使用带参数 `--interactive` 的 rebase 命令, 简写为 `-i`

如果你在命令后增加了这个选项, Git 会打开一个 UI 界面并列出将要被复制到目标分支的备选提交记录，它还会显示每个提交记录的哈希值和提交说明，提交说明有助于你理解这个提交进行了哪些更改。

在实际使用时，所谓的 UI 窗口一般会在文本编辑器 —— 如 Vim —— 中打开一个文件。 考虑到课程的初衷，我弄了一个对话框来模拟这些操作。

#### rebase UI界面打开能做的操作

当 rebase UI界面打开时, 你能做3件事:

- 调整提交记录的顺序（通过鼠标拖放来完成）
- 删除你不想要的提交（通过切换 `pick` 的状态来完成，关闭就意味着你不想要这个提交记录）
- 合并提交。 遗憾的是由于某种逻辑的原因，我们的课程不支持此功能，因此我不会详细介绍这个操作。简而言之，它允许你把多个提交记录合并成一个。

接下来咱们看个实例

当你点击下面的按钮时，会出现一个交互对话框。对提交记录做个排序（当然你也可以删除某些提交），点击确定看结果

代码：

```shell
git rebase -i HEAD~4
```

执行前：

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/image-20210613204633183.png" alt="image-20210613204633183" style="zoom:50%;" />

执行后：

首先会出现UI界面：

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/image-20210613204724523.png" alt="image-20210613204724523" style="zoom:50%;" />

可以任意更改顺序，例如：

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/image-20210613204824214.png" alt="image-20210613204824214" style="zoom:50%;" />

在这里我将C3放在第一个，C4、C5、C2放在接下来的位置。

结果：

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/image-20210613204904110.png" alt="image-20210613204904110" style="zoom:50%;" />

结果严格按照我刚才的顺序进行复制。

#### 考核

要通过本关, 做一次交互式的 rebase，整理成目标窗口中的提交顺序。

原始图形：

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/image-20210613205104703.png" alt="image-20210613205104703" style="zoom:50%;" />

目标图形：

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/image-20210613205123405.png" alt="image-20210613205123405" style="zoom:50%;" />

代码：

```shell
git rebase -i main~4
```

会出现图形界面，选择：

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/image-20210613205320523.png" alt="image-20210613205320523" style="zoom:50%;" />

## 杂项

### 1. 只取一个提交记录

来看一个在开发中经常会遇到的情况：我正在解决某个特别棘手的 Bug，为了便于调试而在代码中添加了一些调试命令并向控制台打印了一些信息。

这些调试和打印语句都在它们各自的提交记录里。最后我终于找到了造成这个 Bug 的根本原因，解决掉以后觉得沾沾自喜！

最后就差把 `bugFix` 分支里的工作合并回 `main` 分支了。你可以选择通过 fast-forward 快速合并到 `main` 分支上，但这样的话 `main` 分支就会包含我这些调试语句了。你肯定不想这样，应该还有更好的方式……

实际我们只要让 Git 复制解决问题的那一个提交记录就可以了。跟之前我们在“整理提交记录”中学到的一样，我们可以使用

- `git rebase -i`
- `git cherry-pick`

来达到目的。

由于我们刚刚闯过类似的关卡，所以要不要再尝试一次就看你自己了。但是如果你想试一把的话，确保 `main` 分支能得到 `bugFix` 分支上的相关提交。

#### 考核

原始图形：

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/image-20210613223019483.png" alt="image-20210613223019483" style="zoom:50%;" />

目标图形：

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/image-20210613223034691.png" alt="image-20210613223034691" style="zoom:50%;" />

代码：

```shell
git rebase -i bugFix~3
```

选择omit C2和omit C3

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/image-20210613224029595.png" alt="image-20210613224029595" style="zoom:50%;" />

结果：

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/image-20210613224047926.png" alt="image-20210613224047926" style="zoom:50%;" />

最后

```shell
git branch -f main bugFix
```

### 2. 提交的技巧1

接下来这种情况也是很常见的：你之前在 `newImage` 分支上进行了一次提交，然后又基于它创建了 `caption` 分支，然后又提交了一次。

此时你想对某个以前的提交记录进行一些小小的调整。比如设计师想修改一下 `newImage` 中图片的分辨率，尽管那个提交记录并不是最新的了。

我们可以通过下面的方法来克服困难：

- 先用 `git rebase -i` 将提交重新排序，然后把我们想要修改的提交记录挪到最前
- 然后用 `git commit --amend` 来进行一些小修改
- 接着再用 `git rebase -i` 来将他们调回原来的顺序
- 最后我们把 main 移到修改的最前端（用你自己喜欢的方法），就大功告成啦！

当然完成这个任务的方法不止上面提到的一种（我知道你在看 cherry-pick 啦），之后我们会多点关注这些技巧啦，但现在暂时只专注上面这种方法。 最后有必要说明一下目标状态中的那几个`'` —— 我们把这个提交移动了两次，每移动一次会产生一个 `'`；而 C2 上多出来的那个是我们在使用了 amend 参数提交时产生的，所以最终结果就是这样了。

也就是说，我在对比结果的时候只会对比提交树的结构，对于 `'` 的数量上的不同，并不纳入对比范围内。只要你的 `main` 分支结构与目标结构相同，我就算你通过。

#### 考核

原始图形：

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/image-20210613224445662.png" alt="image-20210613224445662" style="zoom:50%;" />

目标图形：

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/image-20210613224502290.png" alt="image-20210613224502290" style="zoom:50%;" />

代码：

首先，执行

```shell
git rebase -i caption~3
```

出现图形界面并调整顺序：

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/image-20210613224642202.png" alt="image-20210613224642202" style="zoom:50%;" />

接着进行修改：

```shell
git commit --amend
```

最后再次执行：

```shell
git rebase -i caption~4
git branch -f main caption
```

重新更改顺序并且将main分支移动到caption位置处

### 3. 提交的技巧2

*如果你还没有完成“提交的技巧 #1”（前一关）的话，请先通过以后再来！*

正如你在上一关所见到的，我们可以使用 `rebase -i` 对提交记录进行重新排序。只要把我们想要的提交记录挪到最前端，我们就可以很轻松的用 `--amend` 修改它，然后把它们重新排成我们想要的顺序。

但这样做就唯一的问题就是要进行两次排序，而这有可能造成由 rebase 而导致的冲突。下面还是看看 `git cherry-pick` 是怎么做的吧。

要在心里牢记 cherry-pick 可以将提交树上任何地方的提交记录取过来追加到 HEAD 上（只要不是 HEAD 上游的提交就没问题）。

来看看这个例子：

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/image-20210613225109384.png" alt="image-20210613225109384" style="zoom:50%;" />

执行：

```shell
git cherry-pick C2
```

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/image-20210613225145140.png" alt="image-20210613225145140" style="zoom:50%;" />

#### 考核

这一关的目标和上一关一样，通过 `--amend` 改变提交记录 `C2`，但你不能用 `rebase -i`。自己想想要怎么解决吧！ :D

对了，提交记录上面的`'`的数量并不重要，只是引用的不同而已。也就是说如果你的最终结果在某个提交记录上多了个`'`，我也会算你通过的。

原始图形：

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/image-20210613225453038.png" alt="image-20210613225453038" style="zoom:50%;" />

目标图形：

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/image-20210613225733090.png" alt="image-20210613225733090" style="zoom:50%;" />

思路：我们可以首先定位到newImage这个branch，然后进行一次提交，会生成新的分支，然后将main定位到newImage上，此时main和提交之后的newImage属于同一个分支，而caption不是，再将caption使用cherry-pick重新定位回该分支即可

代码：

```shell
$ git checkout newImage						## 定位到newImage

$ git commit --amend							## 提交，产生平行的一个c2

$ git branch -f main newImage			## 将main定位到newImage上

$ git checkout main								## 将HEAD定位到main上

$ git cherry-pick caption					## 将caption加入到main的分支里
```

### 4. git tag

相信通过前面课程的学习你已经发现了：分支很容易被人为移动，并且当有新的提交时，它也会移动。分支很容易被改变，大部分分支还只是临时的，并且还一直在变。

你可能会问了：有没有什么可以*永远*指向某个提交记录的标识呢，比如软件发布新的大版本，或者是修正一些重要的 Bug 或是增加了某些新特性，有没有比分支更好的可以永远指向这些提交的方法呢？

当然有了！Git 的 tag 就是干这个用的啊，它们可以（在某种程度上 —— 因为标签可以被删除后重新在另外一个位置创建同名的标签）永久地将某个特定的提交命名为里程碑，然后就可以像分支一样引用了。

更难得的是，它们并不会随着新的提交而移动。你也不能检出到某个标签上面进行修改提交，它就像是提交树上的一个锚点，标识了某个特定的位置。

咱们来看看标签到底是什么样。

咱们先建立一个标签，指向提交记录 `C1`，表示这是我们 1.0 版本。

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/image-20210613233617910.png" alt="image-20210613233617910" style="zoom:50%;" />

代码：

```shell
git tag v1 C1
```

执行之后：

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/image-20210613233650497.png" alt="image-20210613233650497" style="zoom:50%;" />

#### 考核

在这个关卡中，按照目标建立两个标签，然后检出到 `v1` 上面，要注意你会进到分离 `HEAD` 的状态 —— 这是因为不能直接在`v1` 上面做 commit。

在下个关卡中我们会介绍更多关于标签的有趣的应用。

原始图形：

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/image-20210613233736967.png" alt="image-20210613233736967" style="zoom:50%;" />

目标图形：

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/20210613233751.png" style="zoom:50%;" />

代码：

```shell
$ git tag v0 C1

$ git tag v1 C2

$ git checkout v1
```

### 5. git describe

由于标签在代码库中起着“锚点”的作用，Git 还为此专门设计了一个命令用来**描述**离你最近的锚点（也就是标签），它就是 `git describe`！

Git Describe 能帮你在提交历史中移动了多次以后找到方向；当你用 `git bisect`（一个查找产生 Bug 的提交记录的指令）找到某个提交记录时，或者是当你坐在你那刚刚度假回来的同事的电脑前时， 可能会用到这个命令。

`git describe` 的语法是：

```
git describe <ref>
```

`<ref>` 可以是任何能被 Git 识别成提交记录的引用，如果你没有指定的话，Git 会以你目前所检出的位置（`HEAD`）。

它输出的结果是这样的：

```
<tag>_<numCommits>_g<hash>
```

`tag` 表示的是离 `ref` 最近的标签， `numCommits` 是表示这个 `ref` 与 `tag` 相差有多少个提交记录， `hash` 表示的是你所给定的 `ref` 所表示的提交记录哈希值的前几位。

当 `ref` 提交记录上有某个标签时，则只输出标签名称

原始图形：

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/image-20210614131816054.png" alt="image-20210614131816054" style="zoom:50%;" />

代码：

```shell
git describe main  ## describe表示main
git describe side  ## describe表示side
```

输出：

```shell
v1_2_gC2		## main的输出
v2_1_gC4		## side的输出
```

## 高级话题

### 1. 多分支rebase

哥们儿，我们准备了很多分支！咱们把这些分支 rebase 到 main 上吧。

#### 考核

但是你的领导给你提了点要求 —— 他们希望得到有序的提交历史，也就是我们最终的结果应该是 `C6'` 在 `C7'` 上面， `C5'` 在 `C6'` 上面，依此类推。

即使你搞砸了也没关系，用 `reset` 命令就可以重新开始了。记得看看我们提供的答案，看你能否使用更少的命令来完成任务！

原始图形：

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/image-20210614132319810.png" alt="image-20210614132319810" style="zoom:50%;" />

目标图形：

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/image-20210614132333781.png" alt="image-20210614132333781" style="zoom:50%;" />

代码：

```shell
$ git rebase main bugFix

$ git rebase bugFix C5

$ git rebase HEAD side

$ git rebase side another

$ git branch -f main another
```

### 2. 两个父节点

操作符 `^` 与 `~` 符一样，后面也可以跟一个数字。

但是该操作符后面的数字与 `~` 后面的不同，并不是用来指定向上返回几代，而是指定合并提交记录的某个父提交。还记得前面提到过的一个合并提交有两个父提交吧，所以遇到这样的节点时该选择哪条路径就不是很清晰了。

Git 默认选择合并提交的“第一个”父提交，在操作符 `^` 后跟一个数字可以改变这一默认行为。

这里有一个合并提交记录。如果不加数字修改符直接检出 `main^`，会回到第一个父提交记录。

(*在我们的图示中，第一个父提交记录是指合并提交记录正上方的那个提交记录。*)

执行前：

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/image-20210614134400763.png" alt="image-20210614134400763" style="zoom:50%;" />

代码：

```shell
git checkout main^
```

执行后：

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/image-20210614134429848.png" alt="image-20210614134429848" style="zoom:50%;" />

这是常规用法，现在试试

```shell
git checkout main^2
```

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/image-20210614134502950.png" alt="image-20210614134502950" style="zoom:50%;" />

我们回到了另一个父提交上。



#### 考核

要完成此关，在指定的目标位置创建一个新的分支。

很明显可以简单地直接使用提交记录的哈希值（比如 `C6`），但我要求你使用刚刚讲到的相对引用修饰符！

原始图形：

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/image-20210614135609480.png" alt="image-20210614135609480" style="zoom:50%;" />

目标图形：

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/image-20210614135623479.png" alt="image-20210614135623479" style="zoom:50%;" />

代码：

```shell
git branch bugWork main~1^2~1
```

### 3. 纠缠不清的分支

现在我们的 `main` 分支是比 `one`、`two` 和 `three` 要多几个提交。出于某种原因，我们需要把 `main` 分支上最近的几次提交做不同的调整后，分别添加到各个的分支上。

`one` 需要重新排序并删除 `C5`，`two` 仅需要重排排序，而 `three` 只需要提交一次。

原始图形：

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/image-20210614135716088.png" alt="image-20210614135716088" style="zoom:50%;" />

目标图形：

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/image-20210614135727790.png" alt="image-20210614135727790" style="zoom:50%;" />

代码：(我的、7条)

```shell
$ git branch -f one main

$ git checkout one

$ git rebase -i one~4

$ git branch -f three C2

$ git branch -f two main

$ git checkout two

$ git rebase -i two~4
```

答案（5条）：

```shell
$ git checkout one

$ git cherry-pick C4 C3 C2

$ git checkout two

$ git cherry-pick C5 C4 C3 C2

$ git branch -f three C2
```



# 远程部分

## Push & Pull -Git 远程仓库

是时候分享你的代码了，让编码变得社交化吧

远程仓库并不复杂, 在如今的云计算盛行的世界很容易把远程仓库想象成一个富有魔力的东西, 但实际上它们只是你的仓库在另个一台计算机上的拷贝。你可以通过因特网与这台计算机通信 —— 也就是增加或是获取提交记录

话虽如此, 远程仓库却有一系列强大的特性

- 首先也是最重要的的点, 远程仓库是一个强大的备份。本地仓库也有恢复文件到指定版本的能力, 但所有的信息都是保存在本地的。有了远程仓库以后，即使丢失了本地所有数据, 你仍可以通过远程仓库拿回你丢失的数据。
- 还有就是, 远程让代码社交化了! 既然你的项目被托管到别的地方了, 你的朋友可以更容易地为你的项目做贡献(或者拉取最新的变更)

现在用网站来对远程仓库进行可视化操作变得越发流行了(像 [GitHub](https://github.com/) 或 [Phabricator](http://phabricator.org/)), 但远程仓库**永远**是这些工具的顶梁柱, 因此理解其概念非常的重要!

直到现在, 教程都聚焦于**本地**仓库的操作（branch、merge、rebase 等等）。但我们现在需要学习远程仓库的操作 —— 我们需要一个配置这种环境的命令, 它就是 `git clone`。 

### 1. git clone

从技术上来讲，`git clone` 命令在真实的环境下的作用是在**本地**创建一个远程仓库的拷贝（比如从 github.com）。 但在我们的教程中使用这个命令会有一些不同 —— 它会在远程创建一个你本地仓库的副本。显然这和真实命令的意思刚好相反，但是它帮咱们把本地仓库和远程仓库关联到了一起，在教程中就凑合着用吧。

远程仓库（在图示中）的样子：

```shell
git clone
```

执行前：

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/20210613132540.png" style="zoom:50%;" />

执行后：

<img src="/Users/heyizhi/Library/Application Support/typora-user-images/image-20210613132618606.png" alt="image-20210613132618606" style="zoom:50%;" />

就是它了! 现在我们有了一个自己项目的远程仓库。除了远程仓库使用虚线之外, 它们几乎没有什么差别 —— 在后面的关卡中, 你将会学习怎样在本地仓库和远程仓库间分享工作成果。

### 2. 远程分支

既然你已经看过 `git clone` 命令了，咱们深入地看一下发生了什么。

你可能注意到的第一个事就是在我们的本地仓库多了一个名为 `o/main` 的分支, 这种类型的分支就叫**远程**分支。由于远程分支的特性导致其拥有一些特殊属性。

远程分支反映了远程仓库(在你上次和它通信时)的**状态**。这会有助于你理解本地的工作与公共工作的差别 —— 这是你与别人分享工作成果前至关重要的一步.

#### 远程分支的特别属性：

在你检出时自动进入**分离 HEAD** 状态。Git 这么做是出于不能直接在这些分支上进行操作的原因, 你必须在别的地方完成你的工作, （更新了远程分支之后）再用远程分享你的工作成果。（这个在下面有图解）

#### 为什么有 `o/`？

你可能想问这些远程分支的前面的 `o/` 是什么意思呢？好吧, 远程分支有一个命名规范 —— 它们的格式是:

- `<remote name>/<branch name>`

因此，如果你看到一个名为 `o/main` 的分支，那么这个分支就叫 `main`，远程仓库的名称就是 `o`。

大多数的开发人员会将它们主要的远程仓库命名为 `origin`，并不是 `o`。这是因为当你用 `git clone` 某个仓库时，Git 已经帮你把远程仓库的名称设置为 `origin` 了

不过 `origin` 对于我们的 UI 来说太长了，因此不得不使用简写 `o` :) 但是要记住, 当你使用真正的 Git 时, 你的远程仓库默认为 `origin`!

#### 如果检出远程分支会怎么样呢？

```shell
git checkout o/main
git commit
```

提交前：

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/20210613133051.png" style="zoom:50%;" />

提交后：

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/20210613133110.png" style="zoom:50%;" />

正如你所见，Git 变成了分离 HEAD 状态，当添加新的提交时 `o/main` 也不会更新。这是因为 `o/main` 只有在远程仓库中相应的分支更新了以后才会更新。

#### 考核

要通过本关，在 `main` 分支上做一次提交；然后检出 `o/main`，再做一提交。这有助于你理解远程分支的不同，他们的更新只是反映了远程的状态。

```shell
$ git commit

$ git checkout o/main

$ git commit
```

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/20210613133437.png" style="zoom:50%;" />

### 3.git fetch

Git 远程仓库相当的操作实际可以归纳为两点：向远程仓库传输数据以及从远程仓库获取数据。既然我们能与远程仓库同步，那么就可以分享任何能被 Git 管理的更新（因此可以分享代码、文件、想法、情书等等）。

本节课我们将学习如何从远程仓库获取数据 —— 命令如其名，它就是 `git fetch`。

你会看到当我们从远程仓库获取数据时, 远程分支也会更新以反映最新的远程仓库。在上一节课程中我们已经提及过这一点了。

在解释 `git fetch` 前，我们先看看实例。这里我们有一个远程仓库, 它有两个我们本地仓库中没有的提交。

```shell
git fetch
```

执行前：

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/20210613133632.png" style="zoom:50%;" />

执行后：

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/20210613133654.png" style="zoom:50%;" />

就是这样了! `C2`,`C3` 被下载到了本地仓库，同时远程分支 `o/main` 也被更新，反映到了这一变化

#### git fetch做了什么？

`git fetch` 完成了仅有的但是很重要的两步:

- 从远程仓库下载本地仓库中缺失的提交记录
- 更新远程分支指针(如 `o/main`)

`git fetch` 实际上将本地仓库中的远程分支更新成了远程仓库相应分支最新的状态。

如果你还记得上一节课程中我们说过的，远程分支反映了远程仓库在你**最后一次与它通信时**的状态，`git fetch` 就是你与远程仓库通信的方式了！希望我说的够明白了，你已经了解 `git fetch` 与远程分支之间的关系了吧。

`git fetch` 通常通过互联网（使用 `http://` 或 `git://` 协议) 与远程仓库通信。

#### git fetch不会做的工作

`git fetch` 并不会改变你本地仓库的状态。它不会更新你的 `main` 分支，也不会修改你磁盘上的文件。

理解这一点很重要，因为许多开发人员误以为执行了 `git fetch` 以后，他们本地仓库就与远程仓库同步了。它可能已经将进行这一操作所需的所有数据都下载了下来，但是**并没有**修改你本地的文件。我们在后面的课程中将会讲解能完成该操作的命令 ` :D`

所以, 你可以将 `git fetch` 的理解为单纯的下载操作。

### 4. git pull

既然我们已经知道了如何用 `git fetch` 获取远程的数据, 现在我们学习如何将这些变化更新到我们的工作当中。

其实有很多方法的 —— 当远程分支中有新的提交时，你可以像合并本地分支那样来合并远程分支。也就是说就是你可以执行以下命令:

- `git cherry-pick o/main`
- `git rebase o/main`
- `git merge o/main`
- 等等

实际上，由于先抓取更新再合并到本地分支这个流程很常用，因此 Git 提供了一个专门的命令来完成这两个操作。它就是我们要讲的 `git pull`。

#### fetch、merge依次执行的效果

```shell
git fetch
git merge o/main
```

执行前：

![image-20210613134429761](https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/image-20210613134429761.png)

执行后：

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/image-20210613134515205.png" alt="image-20210613134515205" style="zoom:50%;" />

我们用 `fetch` 下载了 `C3`, 然后通过 `git merge o/main` 合并了这一提交记录。现在我们的 `main` 分支包含了远程仓库中的更新（在本例中远程仓库名为 `origin`）

#### 使用git pull的效果

```shell
git pull
```

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/image-20210613134735472.png" alt="image-20210613134735472" style="zoom:50%;" />

同样的结果！这清楚地说明了 `git pull` 就是 git fetch 和 git merge 的缩写！

### 5. 模拟团队合作

这里有一件棘手的事 —— 为了接下来的课程, 我们需要先教你如何制造远程仓库的变更。

这意味着，我们需要“假装”你的同事、朋友、合作伙伴更新了远程仓库，有可能是某个特定的分支，或是几个提交记录。

为了做到这点，我们引入一个自造命令 `git fakeTeamwork`！它的名称已经说明了一切

`fakeTeamwork` 默认操作就是在远程仓库的 main 分支上做一次提交。

``` shell
git fakeTeamwork ## 这不是一个git命令，只是为了模拟在远程产生的效果
```

执行前：

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/image-20210613135011538.png" alt="image-20210613135011538" style="zoom:50%;" />

执行后：

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/image-20210613135038560.png" alt="image-20210613135038560" style="zoom:50%;" />

完成了 —— 远程仓库增加了一个新提交，我们还没有下载它，因为我们还没有执行 `git fetch`。

还可以指定提交的分支或是数量，只需要在命令后面加上它们就可以了

```shell
git fakeTeamwork foo 3
```

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/image-20210613135141331.png" alt="image-20210613135141331" style="zoom:50%;" />

通过一个命令，我们就模拟队友推送了 3 个提交记录到远程仓库的 foo 分支。

接下来的关卡会相当的困难，所以在本关会让你做许多事情，先来热热身。

#### 考核

克隆一个远程仓库（用 `git clone`），再在刚创建的远程仓库中模拟一些修改，然后在你自己的本地分支上做一些提交，再拉取远程仓库的变更。

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/image-20210613135523641.png" alt="image-20210613135523641" style="zoom:50%;" />

```shell
$ git clone

local branch "main" set to track remote branch "o/main"

$ git fakeTeamwork main 2

$ git commit

$ git pull
```

### 6. git push

OK，我们已经学过了如何从远程仓库获取更新并合并到本地的分支当中。这非常棒……但是我如何与大家分享**我的**成果呢？

嗯，上传自己分享内容与下载他人的分享刚好相反，那与 `git pull` 相反的命令是什么呢？`git push`！

`git push` 负责将**你的**变更上传到指定的远程仓库，并在远程仓库上合并你的新提交记录。一旦 `git push` 完成, 你的朋友们就可以从这个远程仓库下载你分享的成果了！

你可以将 `git push` 想象成发布你成果的命令。它有许多应用技巧，稍后我们会了解到，但是咱们还是先从基础的开始吧……

*注意 —— `git push` 不带任何参数时的行为与 Git 的一个名为 `push.default` 的配置有关。它的默认值取决于你正使用的 Git 的版本，但是在教程中我们使用的是 `upstream`。 这没什么太大的影响，但是在你的项目中进行推送之前，最好检查一下这个配置。*

```shell
git push
```

执行前：

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/image-20210613135821575.png" alt="image-20210613135821575" style="zoom:50%;" />

执行后：

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/image-20210613135840775.png" alt="image-20210613135840775" style="zoom:50%;" />

过去了, 远程仓库接收了 `C2`，远程仓库中的 `main` 分支也被更新到指向 `C2` 了，我们的远程分支 (o/main) 也同样被更新了。所有的分支都同步了！

### 7. 偏离的工作

现在我们已经知道了如何从其它地方 `pull` 提交记录，以及如何 `push` 我们自己的变更。看起来似乎没什么难度，但是为何还会让人们如此困惑呢？

困难来自于远程库提交历史的**偏离**。

假设你周一克隆了一个仓库，然后开始研发某个新功能。到周五时，你新功能开发测试完毕，可以发布了。但是 —— 天啊！你的同事这周写了一堆代码，还改了许多你的功能中使用的 API，这些变动会导致你新开发的功能变得不可用。但是他们已经将那些提交推送到远程仓库了，因此你的工作就变成了基于项目**旧版**的代码，与远程仓库最新的代码不匹配了。

这种情况下, `git push` 就不知道该如何操作了。如果你执行 `git push`，Git 应该让远程仓库回到星期一那天的状态吗？还是直接在新代码的基础上添加你的代码，亦或由于你的提交已经过时而直接忽略你的提交？

因为这情况（历史偏离）有许多的不确定性，Git 是不会允许你 `push` 变更的。实际上它会强制你先合并远程最新的代码，然后才能分享你的工作。

#### 实际案例

```shell
git push
```

提交前：

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/image-20210613140436373.png" alt="image-20210613140436373" style="zoom:50%;" />

此时远程代码和本地代码版本不同（一个c2一个c3）

提交后：

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/image-20210613140524333.png" alt="image-20210613140524333" style="zoom:50%;" />

什么都没有变，因为命令失败了！`git push` 失败是因为你最新提交的 `C3` 基于远程分支中的 `C1`。而远程仓库中该分支已经更新到 `C2` 了，所以 Git 拒绝了你的推送请求。

#### 如何解决

##### 方法1：git rebase

很简单，你需要做的就是使你的工作基于最新的远程分支。

有许多方法做到这一点呢，不过最直接的方法就是通过 rebase 调整你的工作。咱们继续，看看怎么 rebase！

```shell
git fetch
git rebase o/main
git push
```

执行前：

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/image-20210613140648977.png" alt="image-20210613140648977" style="zoom:50%;" />

执行后：

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/image-20210613140722949.png" alt="image-20210613140722949" style="zoom:50%;" />

我们用 `git fetch` 更新了本地仓库中的远程分支，然后用 rebase 将我们的工作移动到最新的提交记录下，最后再用 `git push` 推送到远程仓库。

##### 方法2：git merge

还有其它的方法可以在远程仓库变更了以后更新我的工作吗? 当然有，我们还可以使用 `merge`

尽管 `git merge` 不会移动你的工作（它会创建新的合并提交），但是它会告诉 Git 你已经合并了远程仓库的所有变更。这是因为远程分支现在是你本地分支的祖先，也就是说你的提交已经包含了远程分支的所有变化。

```shell
git fetch
git merge o/main
git push
```

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/image-20210613140933501.png" alt="image-20210613140933501" style="zoom:50%;" />

我们用 `git fetch` 更新了本地仓库中的远程分支，然后**合并**了新变更到我们的本地分支（为了包含远程仓库的变更），最后我们用 `git push` 把工作推送到远程仓库

很好！但是要敲那么多命令，有没有更简单一点的？

当然 —— 前面已经介绍过 `git pull` 就是 fetch 和 merge 的简写，类似的 `git pull --rebase` 就是 fetch 和 rebase 的简写！

#### 考核

- 克隆你的仓库
- 模拟一次远程提交（fakeTeamwork）
- 完成一次本地提交
- 用 *rebase* 发布你的工作

```shell
$ git clone

local branch "main" set to track remote branch "o/main"

$ git fakeTeamwork

$ git commit

$ git pull --rebase

$ git push
```

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/image-20210613144732833.png" alt="image-20210613144732833" style="zoom:50%;" />



### 8. 远程服务器拒绝

如果你是在一个大的合作团队中工作, 很可能是main被锁定了, 需要一些Pull Request流程来合并修改。如果你直接提交(commit)到本地main, 然后试图推送(push)修改, 你将会收到这样类似的信息:

```shell
! [远程服务器拒绝] main -> main (TF402455: 不允许推送(push)这个分支; 你必须使用pull request来更新这个分支.)
```

远程服务器拒绝直接推送(push)提交到main, 因为策略配置要求 pull requests 来提交更新.

你应该按照流程,新建一个分支, 推送(push)这个分支并申请pull request,但是你忘记并直接提交给了main.现在你卡住并且无法推送你的更新.

新建一个分支feature, 推送到远程服务器. 然后reset你的main分支和远程服务器保持一致, 否则下次你pull并且他人的提交和你冲突的时候就会有问题.

#### 考核

原始图形：

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/image-20210614141122199.png" alt="image-20210614141122199" style="zoom:50%;" />

目标图形：

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/image-20210614141136943.png" alt="image-20210614141136943" style="zoom:50%;" />

代码：

```shell
$ git checkout -b feature		## 在本地创建并使用feature分支
	
$ git push									## 将该分支push到远程仓库

local branch "feature" set to track remote branch "o/feature"

$ git branch -f main o/main	## 将main分支移动回o/main分支
```

## origin和它的周边 - Git远程仓库高级操作

### 1. 推送主分支

既然你应该很熟悉 fetch、pull、push 了，现在我们要通过一个新的工作流来测试你的这些技能。

在大型项目中开发人员通常会在（从 `main` 上分出来的）特性分支上工作，工作完成后只做一次集成。这跟前面课程的描述很相像（把 side 分支推送到远程仓库），不过本节我们会深入一些.

但是有些开发人员只在 main 上做 push、pull —— 这样的话 main 总是最新的，始终与远程分支 (o/main) 保持一致。

对于接下来这个工作流，我们集成了两个步骤：

- 将特性分支集成到 `main` 上
- 推送并更新远程分支

#### 快速更新main分支并推送到远端

```shell
git pull --rebase
git push
```

执行前：

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/image-20210614141838313.png" alt="image-20210614141838313" style="zoom:50%;" />

main分支比o/main多一次提交。并且远程main分支比本机o/mian也多一次提交。

执行后：

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/image-20210614141945129.png" alt="image-20210614141945129" style="zoom:50%;" />

我们执行了两个命令:

- 将我们的工作 rebase 到远程分支的最新提交记录
- 向远程仓库推送我们的工作

#### 考核

这个关卡的 Boss 很厉害 —— 以下是通关提示：

- 这里共有三个特性分支 —— `side1` `side2` 和 `side3`
- 我需要将这三分支按顺序推送到远程仓库
- 因为远程仓库已经被更新过了，所以我们还要把那些工作合并过来

原始图形：

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/image-20210614142106202.png" alt="image-20210614142106202" style="zoom:50%;" />

目标图形：

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/image-20210614142121124.png" alt="image-20210614142121124" style="zoom:50%;" />

代码：

```shell
$ git checkout main										## 首先切换到main分支

$ git pull --rebase										## 将远端的c8拉取过来，创建一个新的支路

$ git cherry-pick c2 c3 c4 c5 c6 c7		## 将节点按顺序放入到main之后

$ git push														## 提交到远端
```

### 2. 使用merge

为了 push 新变更到远程仓库，你要做的就是**包含**远程仓库中最新变更。意思就是只要你的本地分支包含了远程分支（如 `o/main`）中的最新变更就可以了，至于具体是用 rebase 还是 merge，并没有限制。

那么既然没有规定限制，为何前面几节都在着重于 rebase 呢？为什么在操作远程分支时不喜欢用 `merge` 呢？

在开发社区里，有许多关于 merge 与 rebase 的讨论。以下是关于 rebase 的优缺点：

优点:

- Rebase 使你的提交树变得很干净, 所有的提交都在一条线上

缺点:

- Rebase 修改了提交树的历史

比如, 提交 C1 可以被 rebase 到 C3 之后。这看起来 C1 中的工作是在 C3 之后进行的，但实际上是在 C3 之前。

一些开发人员喜欢保留提交历史，因此更偏爱 merge。而其他人（比如我自己）可能更喜欢干净的提交树，于是偏爱 rebase。仁者见仁，智者见智。 

#### 考核

原始图形：

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/image-20210614142705802.png" alt="image-20210614142705802" style="zoom:50%;" />

目标图形：

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/image-20210614142724225.png" alt="image-20210614142724225" style="zoom:50%;" />

代码：

```shell
$ git checkout main

$ git pull --rebase

$ git merge side1

$ git merge side2

$ git merge side3

$ git push
```

