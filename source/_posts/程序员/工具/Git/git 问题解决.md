---
title: git 问题解决  
date: 2023-03-10  
tags: []  
---

# LF will be replaced by CRLF the next time Git touches it

## Windows 版本

```shell
#提交检出均不转换 
git config --global core.autocrlf false
```

## Linux 版本

```shell
#提交时转换为LF，检出时不转换 
git config --global core.autocrlf input
```



