## Firrtl plugin for Intellij platform

### Features

- Syntax highlighting
- Structure View
- Navigate to corresponding Chisel code by Ctrl+Click the Firrtl fileinfo comment

### Build

1. [learn](https://www.jetbrains.org/intellij/sdk/docs/basics/basics.html) how to develop an intellij plugin with Gradle

2. modify build.gradle configuration according to your environment

    change `ideDirectory`  to "your idea install path"

    comment or remove `jvmArgs '-Didea.platform.prefix=Chip'`

3. run gradle tasks buildPlugin or runIde directly

