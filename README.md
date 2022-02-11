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
    src/                   <- Source folder for all your game's code

desktop/
    build.gradle           <- Gradle build file for desktop project*
    src/                   <- Source folder for your desktop project, contains LWJGL launcher class

```

Core Folder Architecture

```
src/...
  entities/
  frameworks/
  screens/
  tools/
  BoatGame.java
```
