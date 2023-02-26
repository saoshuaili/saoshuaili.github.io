---
title: jdk编译过程（MACOS）  
date: 2022-12-04 02:22:41  
tags: []  
categories:
  - 编程语言
  - Java
  - jdk
---
# 背景
我们平时在写java程序时免不得要进行调试，并且在学习例如反射等知识时，对源码的一些关键信息我们需要写一写注解，但如果使用下载的jdk，我们不能编译，只能把注释写在行尾，不能写多行注释，非常难受。
并且做了这么久Java程序员，难道不想拥有一个属于自己的（装逼用的）jdk吗？你甚至能在每次`System.out.println("")`时输出的都是`xxx最帅`。
# 目的
该教程的目的是利用MacOS编译openjdk11。
讲述了编译的过程以及遇到了一大堆坑
# 环境
- 操作系统版本
	<img src=https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/Redis/20210809182736.png height=300>
- bootJDK	
	编译新的jdk需要本身有一个jdk存在。这个版本需要`≥ 你所需要编译的jdk版本 -1`
	我的电脑有两个jdk环境（jdk1.8和jdk11），编译时可以指定安装的jdk版本，注意，在这里只能用jdk11，不能用jdk1.8
	
	```shell
 	$ ls
	jaotc       javap       jfr         jmap        jstat       rmiregistry
	jar         jcmd        jhsdb       jmod        jstatd      serialver
	jarsigner   jconsole    jimage      jps         keytool     unpack200
	java        jdb         jinfo       jrunscript  pack200
	javac       jdeprscan   jjs         jshell      rmic
	javadoc     jdeps       jlink       jstack      rmid
	$ ./java -version
	openjdk version "11.0.8-vectorapi" 2019-03-30
	OpenJDK Runtime Environment TencentKonaJDK (build 11.0.8-vectorapi+3-20210106)
	OpenJDK 64-Bit Server VM TencentKonaJDK (build 11.0.8-vectorapi+3-20210106, mixed mode)
	```
	
	注：我这里用的不是官方的jdk，用的是我们公司内部的jdk，不过没有影响，同样可以进行编译
- clang版本

	```shell
	$ clang --version 
	Apple clang version 12.0.5 (clang-1205.0.22.9)
	Target: x86_64-apple-darwin20.6.0
	Thread model: posix
	InstalledDir: /Library/Developer/CommandLineTools/usr/bin
	```
	
- clang++版本

	```shell
	$ clang++ --version
	Apple clang version 12.0.5 (clang-1205.0.22.9)
	Target: x86_64-apple-darwin20.6.0
	Thread model: posix
	InstalledDir: /Library/Developer/CommandLineTools/usr/bin
	```
	
- autoconf
	没有安装autoconf，可以通过`brew install autoconf`安装
	
	```shell
	$ autoconf --version
	autoconf (GNU Autoconf) 2.71
	Copyright (C) 2021 Free Software Foundation, Inc.
	License GPLv3+/Autoconf: GNU GPL version 3 or later
	<https://gnu.org/licenses/gpl.html>, <https://gnu.org/licenses/exceptions.html>
	This is free software: you are free to change and redistribute it.
	There is NO WARRANTY, to the extent permitted by law.
	Written by David J. MacKenzie and Akim Demaille.
	```
	
- make版本

	```shell
	$ make --version
	GNU Make 3.81
	Copyright (C) 2006  Free Software Foundation, Inc.
	This is free software; see the source for copying conditions.
	There is NO warranty; not even for MERCHANTABILITY or FITNESS FOR A
	PARTICULAR PURPOSE.
	This program built for i386-apple-darwin11.3.0
	```
	
- freetype版本
	同样通过`brew install freetype`
	
	```shell
	$ freetype-config --version
	24.0.18
	```

- xcodebuild工具安装
  直接在App Store上安装Xcode（大概11个g，安装也要很久，请耐心等待）。若是不安装，在执行configure脚本检查环境时系统报错：
  
    ```shell
	configure: error: No xcodebuild tool and no system framework headers found, use --with-sysroot or --with-sdk-name to provide a path to a valid SDK
	/Users/coachhe/Tools/jdk-compile/openjdk11/build/.configure-support/generated-configure.sh: line 84: 5: Bad file descriptor
	configure exiting with result code 1
    ```
	
	解决方法是：
	1. 安装xcode工具
		<img src=https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/Redis/20210809193244.png height=200>
	2. 执行以下命令
	
		```shell
		$ sudo xcode-select --switch /Applications/Xcode.app/Contents/Developer
		```

# 编译
## 1. 解压源码
直接去下载源码压缩包是最快的，[openjdk11源码下载链接](https://github.com/openjdk/jdk/releases/tag/jdk-11%2B28)， 当然，也可以直接在[openjdk github仓库](https://github.com/openjdk/jdk)clone 参考文章[【openJDK系列1】macos编译openJDK 8](https://segmentfault.com/a/1190000038268721)

然后将其安装在某个本地文件夹并解压：

```shell
$ pwd
/Users/coachhe/Tools/jdk-compile/openjdk11
$ ls
ADDITIONAL_LICENSE_INFO README                  make
ASSEMBLY_EXCEPTION      bin                     src
LICENSE                 configure               test
Makefile                doc
```

## 2. 执行configure脚本检查环境
在该目录下执行configure脚本。
注意：如果bootjdk版本已经是系统的java环境那就不需要额外执行，直接`bash configure`即可，但是因为我的本地环境有两个jdk版本，并且默认的是jdk11，所以需要指定bootjdk版本。

```shell
$ bash configure --with-target-bits=64 --with-boot-jdk=/Library/Java/JavaVirtualMachines/tencent_jdk11
```

<img src=https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/Redis/20210809193653.png>

## 3. 开始编译
编译过程中一共改了4个文件才最终编译成功。
执行make：
### 第1个错误

```shell
$  make
Building target 'default (exploded-image)' in configuration 'macosx-x86_64-normal-server-release'
Warning: No mercurial configuration present and no .src-rev
Compiling 8 files for BUILD_TOOLS_LANGTOOLS

/Users/coachhe/Documents/jdk/jdk11/src/hotspot/share/runtime/arguments.cpp:1461:35: error: result of comparison against a string literal is unspecified (use an explicit string comparison function instead) [-Werror,-Wstring-compare]
      if (old_java_vendor_url_bug != DEFAULT_VENDOR_URL_BUG) {
                                  ^  ~~~~~~~~~~~~~~~~~~~~~~
1 error generated.
make[3]: *** [/Users/coachhe/Documents/jdk/jdk11/build/macosx-x86_64-normal-server-release/hotspot/variant-server/libjvm/objs/arguments.o] Error 1
make[3]: *** Waiting for unfinished jobs....
Compiling 90 properties into resource bundles for java.desktop
Compiling 2961 files for java.base
make[2]: *** [hotspot-server-libs] Error 2
make[2]: *** Waiting for unfinished jobs....

ERROR: Build failed for target 'default (exploded-image)' in configuration 'macosx-x86_64-normal-server-release' (exit code 2) 
Stopping sjavac server

=== Output from failing command(s) repeated here ===
* For target hotspot_variant-server_libjvm_objs_arguments.o:
/Users/coachhe/Documents/jdk/jdk11/src/hotspot/share/runtime/arguments.cpp:1461:35: error: result of comparison against a string literal is unspecified (use an explicit string comparison function instead) [-Werror,-Wstring-compare]
      if (old_java_vendor_url_bug != DEFAULT_VENDOR_URL_BUG) {
                                  ^  ~~~~~~~~~~~~~~~~~~~~~~
1 error generated.

* All command lines available in /Users/coachhe/Documents/jdk/jdk11/build/macosx-x86_64-normal-server-release/make-support/failure-logs.
=== End of repeated output ===

No indication of failed target found.
Hint: Try searching the build log for '] Error'.
Hint: See doc/building.html#troubleshooting for assistance.

make[1]: *** [main] Error 2
make: *** [default] Error 2
```

根据提示，需要修改`src/hotspot/share/runtime/arguments.cpp`文件的源码：
将第1461行的常量从`DEFAULT_VENDOR_URL_BUG`更改为`0`
更改前：

```shell
  1461       if (old_java_vendor_url_bug != DEFAULT_VENDOR_URL_BUG) {
```

更改后：

```shell
  1461       if (old_java_vendor_url_bug != 0) {
```

### 第2个错误
更改源码之后，重新make clean，然后make

```shell
$  make clean
Building target 'clean' in configuration 'macosx-x86_64-normal-server-release'
... ...
$  make
... ...

=== Output from failing command(s) repeated here ===
* For target hotspot_variant-server_libjvm_objs_sharedRuntime.o:
/Users/coachhe/Documents/jdk/jdk11/src/hotspot/share/runtime/sharedRuntime.cpp:2798:85: error: expression does not compute the number of elements in this array; element type is 'double', not 'relocInfo' [-Werror,-Wsizeof-array-div]
      buffer.insts()->initialize_shared_locs((relocInfo*)locs_buf, sizeof(locs_buf) / sizeof(relocInfo));
                                                                          ~~~~~~~~  ^
/Users/coachhe/Documents/jdk/jdk11/src/hotspot/share/runtime/sharedRuntime.cpp:2797:14: note: array 'locs_buf' declared here
      double locs_buf[20];
             ^
/Users/coachhe/Documents/jdk/jdk11/src/hotspot/share/runtime/sharedRuntime.cpp:2798:85: note: place parentheses around the 'sizeof(relocInfo)' expression to silence this warning
      buffer.insts()->initialize_shared_locs((relocInfo*)locs_buf, sizeof(locs_buf) / sizeof(relocInfo));
                                                                                    ^
1 error generated.

* All command lines available in /Users/coachhe/Documents/jdk/jdk11/build/macosx-x86_64-normal-server-release/make-support/failure-logs.
=== End of repeated output ===

No indication of failed target found.
Hint: Try searching the build log for '] Error'.
Hint: See doc/building.html#troubleshooting for assistance.

make[1]: *** [main] Error 2
make: *** [default] Error 2
```

根据，提示，我们需要修改`src/hotspot/share/runtime/sharedRuntime.cpp`这个文件的源码。
更改前：

```java
2798       buffer.insts()->initialize_shared_locs((relocInfo*)locs_buf, sizeof(locs_buf) / sizeof(relocInfo));
```

更改后：

```java
2798       buffer.insts()->initialize_shared_locs((relocInfo*)locs_buf, (sizeof(locs_buf)) / (sizeof(relocInfo)));
```

你没看错。。就是增加了两个括号而已，但是就是这么神奇，不加就报错，加了就不报错

### 第3个错误
重新make clean后，再次make，这时候出现了第三个编译错误。。。

```shell
$  make clean
Building target 'clean' in configuration 'macosx-x86_64-normal-server-release'
... ...
$  make
... ... 

=== Output from failing command(s) repeated here ===
* For target support_native_java.desktop_libawt_lwawt_CSystemColors.o:
/Users/coachhe/Documents/jdk/jdk11/src/java.desktop/macosx/native/libawt_lwawt/awt/CSystemColors.m:134:9: error: converting the result of '?:' with integer constants to a boolean always evaluates to 'true' [-Werror,-Wtautological-constant-compare]
    if (colorIndex < (useAppleColor) ? sun_lwawt_macosx_LWCToolkit_NUM_APPLE_COLORS : java_awt_SystemColor_NUM_COLORS) {
        ^
1 error generated.

* All command lines available in /Users/coachhe/Documents/jdk/jdk11/build/macosx-x86_64-normal-server-release/make-support/failure-logs.
=== End of repeated output ===

No indication of failed target found.
Hint: Try searching the build log for '] Error'.
Hint: See doc/building.html#troubleshooting for assistance.

make[1]: *** [main] Error 2
make: *** [default] Error 2
```

从错误信息里面可以看出，我们这里需要修改`src/java.desktop/macosx/native/libawt_lwawt/awt/CSystemColors.m`这个文件，修改方式和第2个错误基本相同，也是加括号=\_=

修改前：

```java
134     if (colorIndex < (useAppleColor) ? sun_lwawt_macosx_LWCToolkit_NUM_APPLE_COLORS : java_awt_SystemColor_NUM_COLORS) {
```

修改后：

```cpp
134     if (colorIndex < ((useAppleColor) ? sun_lwawt_macosx_LWCToolkit_NUM_APPLE_COLORS : java_awt_SystemColor_NUM_COLORS)) {
```

基本没啥差别，就是加了个括号。

### 第4个错误
这时再次make，还会出现一个错误（最后一个了。。。）

```shell
$ make clean
... ...

$ make
... ...

ERROR: Build failed for target 'default (exploded-image)' in configuration 'macosx-x86_64-normal-server-release' (exit code 2)
Stopping sjavac server

=== Output from failing command(s) repeated here ===
* For target support_native_java.desktop_libjsound_PLATFORM_API_MacOSX_MidiUtils.o:
/Users/coachhe/Tools/jdk-compile/openjdk11/src/java.desktop/macosx/native/libjsound/PLATFORM_API_MacOSX_MidiUtils.c:263:31: error: cast to smaller integer type 'MIDIClientRef' (aka 'unsigned int') from 'void *' [-Werror,-Wvoid-pointer-to-int-cast]
static MIDIClientRef client = (MIDIClientRef) NULL;
                              ^~~~~~~~~~~~~~~~~~~~
/Users/coachhe/Tools/jdk-compile/openjdk11/src/java.desktop/macosx/native/libjsound/PLATFORM_API_MacOSX_MidiUtils.c:264:29: error: cast to smaller integer type 'MIDIPortRef' (aka 'unsigned int') from 'void *' [-Werror,-Wvoid-pointer-to-int-cast]
static MIDIPortRef inPort = (MIDIPortRef) NULL;
                            ^~~~~~~~~~~~~~~~~~
/Users/coachhe/Tools/jdk-compile/openjdk11/src/java.desktop/macosx/native/libjsound/PLATFORM_API_MacOSX_MidiUtils.c:265:30: error: cast to smaller integer type 'MIDIPortRef' (aka 'unsigned int') from 'void *' [-Werror,-Wvoid-pointer-to-int-cast]
static MIDIPortRef outPort = (MIDIPortRef) NULL;
                             ^~~~~~~~~~~~~~~~~~
/Users/coachhe/Tools/jdk-compile/openjdk11/src/java.desktop/macosx/native/libjsound/PLATFORM_API_MacOSX_MidiUtils.c:471:32: error: cast to smaller integer type 'MIDIEndpointRef' (aka 'unsigned int') from 'void *' [-Werror,-Wvoid-pointer-to-int-cast]
    MIDIEndpointRef endpoint = (MIDIEndpointRef) NULL;
                               ^~~~~~~~~~~~~~~~~~~~~~
   ... (rest of output omitted)

* All command lines available in /Users/coachhe/Tools/jdk-compile/openjdk11/build/macosx-x86_64-normal-server-release/make-support/failure-logs.
=== End of repeated output ===

No indication of failed target found.
Hint: Try searching the build log for '] Error'.
Hint: See doc/building.html#troubleshooting for assistance.

make[1]: *** [main] Error 2
make: *** [default] Error 2
```

可以看到，这里需要修改`src/java.desktop/macosx/native/libjsound/PLATFORM_API_MacOSX_MidiUtils.c`文件的4个地方。但是修改方式是统一的，都是将括号里的内容（`MIDIClientRef`,`MIDIPortRef`,`MIDIEndpointRef`）修改为`unsigned long`。

修改前：

```cpp
  263 static MIDIClientRef client = (MIDIClientRef) NULL;
  264 static MIDIPortRef inPort = (MIDIPortRef) NULL;
  265 static MIDIPortRef outPort = (MIDIPortRef) NULL;

  471 MIDIEndpointRef endpoint = (MIDIEndpointRef) NULL;
```

修改后：

```cpp
  263 static MIDIClientRef client = (unsigned long) NULL;
  264 static MIDIPortRef inPort = (unsigned long) NULL;
  265 static MIDIPortRef outPort = (unsigned long) NULL;

  471 MIDIEndpointRef endpoint = (unsigned long) NULL;
```

编译过程中会出现这些警告，可以忽视

```shell
注: 某些输入文件使用或覆盖了已过时的 API。
注: 有关详细信息, 请使用 -Xlint:deprecation 重新编译。
注: 某些输入文件使用或覆盖了已过时的 API。
注: 有关详细信息, 请使用 -Xlint:deprecation 重新编译。
注: 某些输入文件使用了未经检查或不安全的操作。
注: 有关详细信息, 请使用 -Xlint:unchecked 重新编译。


Warning: generation and use of skeletons and static stubs for JRMP


ld: warning: directory not found for option '-F/Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX11.3.sdk/System/Library/Frameworks/JavaVM.framework/Frameworks'
```

### 4. 编译成功
终于终于终于编译成功了。

```shell
Compiling 4 files for BUILD_JIGSAW_TOOLS
Stopping sjavac server
Finished building target 'default (exploded-image)' in configuration 'macosx-x86_64-normal-server-release'
```

我们进入对应目录：
<img src=https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/Redis/20210809202548.png height=100>

至此我们终于编译完成了，可以在IDEA中选择我们编译成功的jdk版本：

# 使用
首先在IDEA中，点击`File->Project Structure`，删除现有的SDKs
<img src=https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/Redis/20210809203004.png height=500>

然后重新添加我们目录下的SDK
<img src=https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/Redis/20210809203150.png height=500>
<img src=https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/Redis/20210809203248.png height=500>

此时源码还是只读的，无法进行更改，这是因为这里的源码我们还是默认去系统`$JAVA_HOME`目录下的src里面找的Java源代码，因为我们还需要将Sourcepath里面的src文件的目录进行修改：
<img src=https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/Redis/20210809203445.png height=500>
点击加号之后添加我们自己的openjdk目录：
<img src=https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/Redis/20210809203537.png height = 500>
添加之后确认：
<img src=https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/Redis/20210809203646.png height = 500>


此时我们就可以进入源码进行修改了，例如最基础的`System.out.println()`，我们可以进入查看：
<img src=https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/Redis/20210809204047.png height=500>

修改之后对应文件目录下的src文件就会被更改了，然后重新在源文件目录下`make images`就可以更新了
注意：`make images`会在原来的基础上增量编译，不会重新编译了
