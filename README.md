## Firrtl plugin for Intellij platform

### Features

- Syntax highlighting
- Code folding
- Structure View
- Navigate to corresponding Chisel code by Ctrl+Click or Ctrl+B on the Firrtl fileinfo comment

### Build

1. [learn](https://www.jetbrains.org/intellij/sdk/docs/basics/basics.html) how to develop an intellij plugin with Gradle

2. modify build.gradle configuration according to your environment

    change `ideDirectory`  to "your idea install path"

    comment or remove `jvmArgs '-Didea.platform.prefix=Chip'`

3. run gradle tasks buildPlugin or runIde directly


![](https://plugins.jetbrains.com/files/14183/screenshot_21600.png)

### Sponsor

Please check if these plugins are helpful to you (Free for students and teachers)

- [EasySoC Diagrammer](https://plugins.jetbrains.com/plugin/16255-easysoc-diagrammer)	for circuit visualization
- [EasySoC Verilog](https://plugins.jetbrains.com/plugin/14184-easysoc-verilog) 	Jump to the corresponding Chisel code

|                     Contact me by WeChat                     |
| :----------------------------------------------------------: |
| ![wechatpay_160](https://github.com/itviewer/personal/blob/main/wechat.jpg?raw=true) |