---
title: obsidian 同步  
date: 2023-03-12  
tags: []  
---

在不同设备间同步 obsidian 时，如果直接使用 obsidian git 同步配置文件（`.obsidian` 文件），那经常会导致一些插件冲突，比如 remember cursor plugin 会导致文件位置的一些变化冲突之类，因为我们不打算采用 obsidian git 来同步配置文件，只用来同步代码和笔记，那么就需要使用其他方式来同步配置文件了