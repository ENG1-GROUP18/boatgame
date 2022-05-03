[![Java CI with Gradle](https://github.com/ENG1-GROUP18/boatgame/actions/workflows/gradle.yml/badge.svg?branch=main)](https://github.com/ENG1-GROUP18/boatgame/actions/workflows/gradle.yml)
[![Lint Code Base](https://github.com/ENG1-GROUP18/boatgame/actions/workflows/linter.yml/badge.svg)](https://github.com/ENG1-GROUP18/boatgame/actions/workflows/linter.yml)
```
       ~
    _ )_) _
   )_))_))_)
   _!__!__!_
~~~\_______/~~~
```
# boatgame

### Project Architecture
```
settings.gradle            <- definition of sub-modules. By default core, desktop, android, html, ios
build.gradle               <- main Gradle build file, defines dependencies and plugins
gradlew                    <- local Gradle wrapper
gradlew.bat                <- script that will run Gradle on Windows
gradle                     <- script that will run Gradle on Unix systems
local.properties           <- IntelliJ only file, defines Android SDK location

core/
    build.gradle           <- Gradle build file for core project*
    src/main/java/         <- Source folder for all your game's code
    src/test/java/         <- Source folder for all tests

desktop/
    build.gradle           <- Gradle build file for desktop project*
    src/                   <- Source folder for your desktop project, contains LWJGL launcher class

```

### Core Folder Architecture

```
src/main/java/...
  entities/
  frameworks/
  screens/
  tools/
  BoatGame.java
```
