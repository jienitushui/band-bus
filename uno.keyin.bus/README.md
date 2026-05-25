# 弦电子书

> 本项目使用AGPL协议开源，如果您使用了任何本项目的源码都**必须开源**。

这是一款为小米 VelaOS 开发的，适用于 小米手环/REDMI Watch 的电子书小程序，旨在为 Vela 实现其所能支持的最强大的电子书阅读器。
本项目已实现以下功能:
- [x] 滑动式无缝加载阅读器
- [x] 怀旧模式分页阅读器
- [x] 完整的章节系统
- [x] 滑动式阅读器的书签功能
- [x] 定时自动翻页
- [x] 书籍封面的传输与显示能力
- [x] 双端的书籍分类能力
- [x] 双端阅读时长记录
- [x] 双端阅读进度传输同步能力
- [x] 手环端多翻页方式支持
- [x] 手机端的独立阅读书籍能力

## 使用教程

由于 `聪明猫` 已为Vela用户提供详细的教程文档，故此次引用其的文档链接。
[点击前往语雀文档](https://www.yuque.com/yulimfish/congmingmao/xiaomi-hyper-document#C9VqS)

## 编译教程

> 你所查看的当前README所处分支为 `小米手环8Pro/9Pro(n67)` 分支。

### 配置IDE

前往[小米 Vela 开发者文档](https://iot.mi.com/vela/quickapp/zh/guide/start/use-ide.html)下载`AIot-IDE`并参照文档进行配置。

### 打开项目

使用 `AIot-IDE` 打开本仓库中的 **`uno.keyin.bus`** 目录（快应用工程根目录）。`src/manifest.json` 里的 **`package`** 为 **`uno.keyin.bus`**，与仓库内 **`android/app`** 配套应用的 **`applicationId`** 一致；互联前请按文档对齐两端签名。参照 IDE 引导安装项目依赖。

### 编译项目

点击顶部的`打包`按钮并配置证书，打包一个自己的版本。

## 相关资源

### 弦电子书安卓同步器（上游历史仓库，包名与当前工程无关）

https://github.com/youshen2/com.bandbbs.ebook-android

### 小米 VelaJS 开发者文档

https://iot.mi.com/vela/quickapp/zh/guide/

> &nbsp;
> # 本项目由米坛社区开源项目支持计划提供支持
> &nbsp;
> ### 米坛社区发布贴
> ![](https://github.com/youshen2/com.bandbbs.ebook/blob/n67/badge.png?raw=true)
> &nbsp;
> [小米手环9](https://www.bandbbs.cn/resources/5045/)
> [小米手环10](https://www.bandbbs.cn/resources/4604/)
> [小米手环9Pro](https://www.bandbbs.cn/resources/4312/)
> [小米手环8Pro](https://www.bandbbs.cn/resources/4311/)
> [REDMI Watch5/6](https://www.bandbbs.cn/resources/4533/)
> &nbsp;