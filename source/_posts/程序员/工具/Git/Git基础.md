---
title: Git基础  
date: 2022-12-04 02:36:48  
tags: []  
categories:
  - 工具
  - Git
---
# git和github的关系

git是代码管理工具，gitHub是基于git打造的一个开源社区。



# git的使用

## 1. 创建版本库

### 版本库定义
版本库就是仓库，英文是repository，可以理解成其为一个目录，这个目录里所有文件都可以被git管理起来，每个文件的修改，删除，Git都能跟踪，以便任何时刻都可追踪历史，或者在将来某个时刻还可以将文件“还原”。
	
### 创建版本库
在这里创建了名为testgit的仓库
![](https://pic.downk.cc/item/5fc75027394ac523789cfd8d.png)

### 通过git init把这个目录变成git可以管理的仓库：

```git
git init
```

![](https://pic.downk.cc/item/5fc75027394ac523789cfd7a.png)



## 2. git使用

### 先创建一个记事本文件readme.txt

内容为：This is my first git file.
![img](https://pic.downk.cc/item/5fc75027394ac523789cfd6e.png?ynotemdtimestamp=1617365973827)

### 第一步（将文件添加到git中）

使用命令git add readme.txt添加到暂存区里面去，如下： ![img](https://pic.downk.cc/item/5fc75027394ac523789cfd5d.png?ynotemdtimestamp=1617365973827)没有任何提示说明添加成功

### 第二步（将文件提交）

用命令git commit告诉Git，把文件提交到仓库
![img](https://pic.downk.cc/item/5fc75027394ac523789cfd51.png?ynotemdtimestamp=1617365973827)后面双引号内的内容为提交的注释
目前已经提交了一个readme.txt文件了，下面可以通过命令git.ststus来查看是否还有文件未提交，如下： ![img](https://pic.downk.cc/item/5fc75027394ac523789cfd46.png?ynotemdtimestamp=1617365973827)说明没有任何文件未提交

### 第三步（修改并提交修改文件）

#### 1. 修改文件

现在继续修改readme.txt的内容，例如添加"This is the change I made"
![img](https://pic.downk.cc/item/5fc75027394ac523789cfd3b.png?ynotemdtimestamp=1617365973827)

#### 2. 查看修改

如下，然后再用git status来查看结果。
![img](https://pic.downk.cc/item/5fc75026394ac523789cfd31.png?ynotemdtimestamp=1617365973827)这里说明readme.txt已经被修改，但是还没提交。

##### 注意：

- 对文本进行更改后，restore是回到没修改之前的版本，commit是提交还没提交过的文件。
- git status能查看出文件被修改了看，但是不能查询被提交的修改。所有的版本控制系统，只能跟踪文件文本的改动，比如txt文件，程序代码等，git也不例外，版本控制系统可以告诉你每次的改动，但是图片、视频这些二进制文件，虽然可以通过版本控制系统管理，但没法跟踪文件的变化，也就是改了什么不知道。需要查询更改，可以使用以下命令：

```
git diff xxx.xxx
```

#### 3. 修改后add需要被提交的部分来更新

![img](https://pic.downk.cc/item/5fc75026394ac523789cfd26.png?ynotemdtimestamp=1617365973827)

#### 4. 最后提交更改

![img](https://pic.downk.cc/item/5fc75026394ac523789cfd1b.png?ynotemdtimestamp=1617365973827)

### 第四步（回退版本）

#### 1. 再次进行修改

![img](https://pic.downk.cc/item/5fc75026394ac523789cfd12.png?ynotemdtimestamp=1617365973827)

#### 2. 查看历史记录

使用命令git log ![img](https://pic.downk.cc/item/5fc75026394ac523789cfcfa.png?ynotemdtimestamp=1617365973827)
如果觉得显示的信息太多，那么可以使用命令

```
git log --pretty=oneline
```

#### 3. 版本回退

有两种命令：

```
git reset --hard HEAD^
```

若是要恢复之前几个版本，那么可以多使用几个（例如，HEAD^）

##### 注意：

回退版本之后git log也会相应减少

```
git reset --hard 版本号
```

首先通过git reflog得到版本号
![img](https://pic.downk.cc/item/5fc75026394ac523789cfceb.png?ynotemdtimestamp=1617365973827)然后通过

```
git reset --hard 版本号
```

恢复到对应的版本

## 3. 工作区和暂存区的区别

工作区：电脑上看到的目录，例如目录testgit里的文件（除了隐藏的目录版本号）
版本库：工作区有一个隐藏目录.git，这个不属于工作区，这是版本库。其中版本库里存放了很多东西，最重要的是 **stage（暂存区）** ，还有Git为我们自动创建了第一个分支master，以及指向master的一个指针HEAD。

之前介绍过的，使用Git提交文件到版本库有两步，
- 使用git add把文件添加进去，实际上就是把文件添加到暂存区
- 使用git commit提交更改，实际上就是把暂存区的所有内容提交到当前分支上。

## 4. 删除文件

假如现在版本库testgit目录添加一个文件b.txt，然后

### 提交

如下：
![](https://pic.downk.cc/item/5fc75026394ac523789cfcc1.png)	

### 然后删除
- 方法1：直接从文件夹中删除
- 方法2：使用命令rm b.txt
	

### 查看状态
![](https://pic.downk.cc/item/5fc75026394ac523789cfcb4.png)	  
可以看到，b.txt文件已经被删除

### 此时有两种选择
1. 直接commit掉
![](https://pic.downk.cc/item/5fc75026394ac523789cfca7.png)
2. 从版本库中恢复被删除的文件
![](https://pic.downk.cc/item/5fc75026394ac523789cfc9e.png)

##### 注意：
当commit之后就不能恢复之前删除或者修改的文件了！

# github

## github介绍

github是远程仓库，首先需要注册github账号：
CoachHe

## SSH设置

由于本地仓库和github仓库之间的传输是通过SSH加密的，所以需要一点设置：
# 1. 创建SSH Key
在用户主目录下，看看有没有.ssh目录，如果有，再看看有没有id_rsa和id_rsa.pub这两个文件，如果有，直接跳过此如下命令，如果没有，打开命令行输入：
```shell
ssh-keygen -t rsa -C "youremail@example.com"
```
![](https://pic.downk.cc/item/5fc752a0394ac523789ea675.png)


## 注意：
id_rsa是私钥，不能泄露出去，id_rsa.pub是公钥，可以放心告诉任何人。

# 2. 登录github
打开"settings"中的SSH Keys页面，然后点击"Add SSH Key"，填上任意title，在Key文本框中粘贴id_rsa.pub文件的内容，最后得到一个新的SSH Key
![](https://pic.downk.cc/item/5fc752a0394ac523789ea665.png)	## 3 添加远程仓库

## 目前
本地已经有了个Git仓库，又想在github创建一个Git仓库，并且希望这两个仓库进行远程同步，这样github的仓库可以作为备份，又可以其他人通过该仓库来写作。

## 在github上创建Git仓库
点击create new repository，然后repository name是仓库名字，输入testgit，最后点击create repository。
![](https://pic.downk.cc/item/5fc752a0394ac523789ea65b.png)


## 将github与本地仓库相关联
目前GitHub上的testgit仓库是空的，GitHub告诉我们，可以从这个仓库克隆出新的仓库，也可以把一个已有的本地仓库与之关联，然后，把本地仓库的内容推送到GitHub仓库。
根据 GitHub 提示，在本地的 testgit 仓库下运行命令：

```shell
git remote add origin https://github.com/CoachHe/testgit.git
```

![](https://pic.downk.cc/item/5fc752a0394ac523789ea64d.png)

此时 github 仓库就已经和本地仓库关联了

## 将 github 的内容推送到本地仓库

### a. 将本地仓库内容推送到 GitHub 仓库
使用git push命令，实际上是将当前分支master推到到远程。
![](https://pic.downk.cc/item/5fc752a0394ac523789ea63e.png)
结果如下：  
![](https://pic.downk.cc/item/5fc752a0394ac523789ea633.png)  
##### 注意（-u参数）：
由于远程库是空的，我们第一次推送master分支时，加上了-u参数，Git不但会把本地的master分支内容推送的远程新的master分支，还会把本地的master分支与远程的master分支关联起来，在以后的推送或者拉取时就可以简化命令。推送成功后，可以立刻在github页面中看到远程库的内容已经和本地一模一样了。


### b. 从远程GitHub仓库中克隆
先有远程库的时候,如果远程库有了新内容,如何克隆到本地来呢?
#### 首先创建远程库
![](https://pic.downk.cc/item/5fc752a0394ac523789ea624.png)
#### 接着将远程库克隆到本地
```
git clone https://github.com/CoachHe/testgit2
```
效果就是在本地目录中生成了testgit2目录，如下所示：
![](https://pic.downk.cc/item/5fc752a0394ac523789ea607.png)		
##### 注意
这里 testgit2 所生成的位置就是你终端所在的目录。

## 删除远程库
如果添加的时候地址写错了，或者就是想删除远程库，可以用 `git remote rm <name>` 命令，建议先用 `git remote -v ` 查看远程库信息

```shell
[root@coachhe1616341555187-0 ~/company-working]# git remote -v
origin  git@43.154.34.118:/home/git/company-working (fetch)
origin  git@43.154.34.118:/home/git/company-working (push)
```

然后根据名字删除，比如我们这里的仓库名是 origin，那就执行

```shell
[root@coachhe1616341555187-0 ~/company-working]# git remote rm origin
```

此处的“删除”其实是接触了本地和远程的绑定关系，并不是物理上删除了远程库。远程库本身并没有任何更改。要真正删除远程库，需要登录 github，在后台页面找到删除按钮再删除。

# 分支管理
## 1. 创建与合并分支
在版本回退里已经知道，每次提交，Git 都把它们串成一条时间线，这条时间线就是一个分支。截止到目前，只有一条时间线，在 Git 里，这个分支叫主分支，即 `master` 分支。`HEAD` 严格来说不是指向提交，而是指向 `master`，`master` 才是指向提交的，所以，`HEAD` 指向的就是当前分支。

一开始的时候，`master` 分支是一条线，Git 用 `master` 指向最新的提交，再用 `HEAD` 指向 `master`，就能确定当前分支，以及当前分支的提交点。

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/程序员/工具/git/20211226145453.png" width = "30%" />


每次提交，`master` 分支都会向前移动一步，这样，随着你不断提交，`master` 分支的线越来越长。

当我们创建新的分支，例如 `dev` 时，Git 新建了一个指针叫 `dev`，指向 `master` 相同的提交，再把 `HEAD` 指向 `dev`，就表示当前分支在 `dev` 上。

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/程序员/工具/git/20211226145914.png" width = "30%" />

可以看到，Git 创建一个分支很快，因为除了增加一个 `dev` 指针，改变 ` Head` 指向，工作区的文件都没有任何变化！

不过从现在开始，对工作区的修改和提交就是针对 `dev` 分支了，比如新提交一次后，`dev` 指针往前移动一步，而 `master` 指针不变。

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/程序员/工具/git/20211226151308.png" width = "30%" />

假如我们在 `dev` 上的工作完成了，就可以把 `dev` 合并到 `master` 上，Git 怎么合并呢？很简单的方法，就是直接把 `master` 指向 `dev` 的当前提交，就完成了合并。

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/程序员/工具/git/20211226151502.png" width = "30%" />

所以 Git 合并分支也很快，就改改指针，工作区内容也不变！

合并完分支后，甚至可以删除 `dev` 分支。删除 `dev` 分支就是把 `dev` 指针给删掉，删掉后，我们就只剩下了一条 `master` ：

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/程序员/工具/git/20211226151634.png" width = "30%" />

总结一下：

![](https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/20211226151707.png)

### 命令小结
- 查看分支：`git branch`
- 创建分支：`git branch <name>`
- 切换分支：`git checkout <name>` 或者 `git switch <name>`
- 创建+切换分支：`git checkout -b <name>` 或者 `git switch -c <name>`
- 合并某分支到当前分支：`git merge <name>`
- 删除分支：`git branch -d <name>`

## 2. 冲突解决

我们来新建一个仓库 `learngit`
```shell
$ git init 
hint: Using 'master' as the name for the initial branch. This default branch name
hint: is subject to change. To configure the initial branch name to use in all
hint: of your new repositories, which will suppress this warning, call:
hint:
hint: 	git config --global init.defaultBranch <name>
hint:
hint: Names commonly chosen instead of 'master' are 'main', 'trunk' and
hint: 'development'. The just-created branch can be renamed via this command:
hint:
hint: 	git branch -m <name>
Initialized empty Git repository in /Users/heyizhi/Downloads/learngit/.git/
```

然后创建并提交一个新文件README.txt
![](https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/20211226152829.png)

现在我到新建的分支 `feature1` 分支，修改 README. md 文件，然后进行提交：
![](https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/20211226153159.png)

可以看到，将 READMD. md 文件的内容进行了更改。

此时再重新切换回 `master` 分支，然后修改并提交 README. md 文件：

![](https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/20211226153440.png)

目前，`feature1` 分支里的 README. md 文件内容为 `Creating a new branch is quick and simple`，但是 master 分支里的 README. md 文件内容为 `Creating a new branch is quick & simple`，并且他们都有各自的提交，变成了这样：

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/程序员/工具/git/20211226153821.png" width = "30%" />

这种情况，Git 无法执行“快速合并”，只能视图把各自的修改合并起来，但是这种合并就可能会有冲突，我们试试看：

![](https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/20211226153920.png)

果然冲突了，Git 告诉我们，`README.md` 文件存在冲突，必须手动解决冲突后再提交。

此时如果我们直接看文件内容，会是这样：
![](https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/20211226154140.png)

```txt
<<<<<<< HEAD
Creating a new branch is quick & simple
=======
Creating a new branch is quick and simple
>>>>>>> feature1
```

Git 用 `<<<<<<<`, `=======` 和 `>>>>>>` 标记不同分支的内容，我们当前在 HEAD 分支下，所以我们要将文件进行修改：

![](https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/20211226154624.png)

然后重新进行提交
![](https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/20211226160524.png)

可以看到，更改再重新提交之后都不需要对 feature1 进行 merge，因为此时 `feature1` 分支和 master 分支是一样的，所以是这样的：

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/程序员/工具/git/20211226160334.png" width = "30%" />

最后删除 `feature1` 分支，工作完成。

![](https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/20211226160634.png)

```shell
$ git branch -d feature1
```

### 注意
如果两个不同的分支对同一个文件进行修改，但不同修改的同一行，此时是不算冲突的，可以直接合并分支。

## 3. 分支管理策略

### --no-ff 参数
通常，合并分支时，如果可能，Git会用`Fast forward`模式，但这种模式下，删除分支后，会丢掉分支信息。

如果要强制禁用 `Fast forward` 模式，Git 就会在 merge 时生成一个新的 commit，这样，从分支历史上就可以看出分支信息。

## 4. 多人协作

### 推送分支

推送分支，就是把该分支上的所有本地提交推送到远程库。推送时，要指定本地分支，这样，Git就会把该分支推送到远程库对应的远程分支上：

```shell
$ git push origin master
```

如果要推送其他分支，比如`dev`，就改成：

```shell
$ git push origin dev
```

但是，并不是一定要把本地分支往远程推送，那么，哪些分支需要推送，哪些不需要呢？

-   `master`分支是主分支，因此要时刻与远程同步；
-   `dev` 分支是开发分支，团队所有成员都需要在上面工作，所以也需要与远程同步；
-   `bug` 分支只用于在本地修复 bug，就没必要推到远程了，除非老板要看看你每周到底修复了几个 bug；
-   `feature` 分支是否推到远程，取决于你是否和你的小伙伴合作在上面开发。

### 多人协作模式

当你从远程仓库克隆时，实际上Git自动把本地的`master`分支和远程的`master`分支对应起来了，并且，远程仓库的默认名称是`origin`。

多人协作时，大家都会往 `master` 和 `dev` 分支上推送各自的修改。

现在，模拟一个你的小伙伴，可以在另一台电脑（注意要把 SSH Key 添加到 GitHub）或者同一台电脑的另一个目录下克隆：

```
$ git clone git@github. com: michaelliao/learngit. git
Cloning into 'learngit'...
remote: Counting objects: 40, done.
remote: Compressing objects: 100% (21/21), done.
remote: Total 40 (delta 14), reused 40 (delta 14), pack-reused 0
Receiving objects: 100% (40/40), done.
Resolving deltas: 100% (14/14), done.
```

当你的小伙伴从远程库clone时，默认情况下，你的小伙伴只能看到本地的`master`分支。不信可以用`git branch`命令看看：

```
$ git branch
* master
```

现在，你的小伙伴要在`dev`分支上开发，就必须创建远程`origin`的`dev`分支到本地，于是他用这个命令创建本地`dev`分支：

```
$ git checkout -b dev origin/dev
```

现在，他就可以在`dev`上继续修改，然后，时不时地把`dev`分支`push`到远程：

```
$ git add env.txt

$ git commit -m "add env"
[dev 7a5e5dd] add env
 1 file changed, 1 insertion(+)
 create mode 100644 env.txt

$ git push origin dev
Counting objects: 3, done.
Delta compression using up to 4 threads.
Compressing objects: 100% (2/2), done.
Writing objects: 100% (3/3), 308 bytes | 308.00 KiB/s, done.
Total 3 (delta 0), reused 0 (delta 0)
To github.com:michaelliao/learngit.git
   f52c633..7a5e5dd  dev -> dev
```

你的小伙伴已经向`origin/dev`分支推送了他的提交，而碰巧你也对同样的文件作了修改，并试图推送：

```
$ cat env.txt
env

$ git add env.txt

$ git commit -m "add new env"
[dev 7bd91f1] add new env
 1 file changed, 1 insertion(+)
 create mode 100644 env.txt

$ git push origin dev
To github.com:michaelliao/learngit.git
 ! [rejected]        dev -> dev (non-fast-forward)
error: failed to push some refs to 'git@github.com:michaelliao/learngit.git'
hint: Updates were rejected because the tip of your current branch is behind
hint: its remote counterpart. Integrate the remote changes (e.g.
hint: 'git pull ...') before pushing again.
hint: See the 'Note about fast-forwards' in 'git push --help' for details.
```

推送失败，因为你的小伙伴的最新提交和你试图推送的提交有冲突，解决办法也很简单，Git已经提示我们，先用`git pull`把最新的提交从`origin/dev`抓下来，然后，在本地合并，解决冲突，再推送：

```
$ git pull
There is no tracking information for the current branch.
Please specify which branch you want to merge with.
See git-pull(1) for details.

    git pull <remote> <branch>

If you wish to set tracking information for this branch you can do so with:

    git branch --set-upstream-to=origin/<branch> dev
```

`git pull`也失败了，原因是没有指定本地`dev`分支与远程`origin/dev`分支的链接，根据提示，设置`dev`和`origin/dev`的链接：

```
$ git branch --set-upstream-to=origin/dev dev
Branch 'dev' set up to track remote branch 'dev' from 'origin'.
```

再pull：

```
$ git pull
Auto-merging env.txt
CONFLICT (add/add): Merge conflict in env.txt
Automatic merge failed; fix conflicts and then commit the result.
```

这回`git pull`成功，但是合并有冲突，需要手动解决，解决的方法和分支管理中的[解决冲突](http://www.liaoxuefeng.com/wiki/896043488029600/900004111093344)完全一样。解决后，提交，再push：

```
$ git commit -m "fix env conflict"
[dev 57c53ab] fix env conflict

$ git push origin dev
Counting objects: 6, done.
Delta compression using up to 4 threads.
Compressing objects: 100% (4/4), done.
Writing objects: 100% (6/6), 621 bytes | 621.00 KiB/s, done.
Total 6 (delta 0), reused 0 (delta 0)
To github.com:michaelliao/learngit.git
   7a5e5dd..57c53ab  dev -> dev
```

因此，多人协作的工作模式通常是这样：

1.  首先，可以试图用`git push origin <branch-name>`推送自己的修改；
2.  如果推送失败，则因为远程分支比你的本地更新，需要先用 `git pull` 试图合并；
3.  如果合并有冲突，则解决冲突，并在本地提交；
4.  没有冲突或者解决掉冲突后，再用 `git push origin <branch-name>` 推送就能成功！
    

如果`git pull`提示`no tracking information`，则说明本地分支和远程分支的链接关系没有创建，用命令`git branch --set-upstream-to <branch-name> origin/<branch-name>`。

这就是多人协作的工作模式，一旦熟悉了，就非常简单。
