Simon Walker's OOAD assignments

# Building

Using JDK 21.

**Build all,**
- If you have gradle, `gradle buildJars` and you must supply the JDK yourself.
- If you do not have gradle, but you have nix, `nix develop` and `gradle buildJars` (which will
also supply the JDK automatically)

**Each runnable project or sub-project can also be built individually:**

eg, `gradle build-project1b` to build the second program under project1.

**Run**: jars are located (after built) in `build/libs`

eg: to run task project1b, `java -jar build/libs/project1b.jar`

# Project Details


### Project1
- [Concepts.pdf](src/main/java/ooad/project1a/Concepts.pdf)
- [program 1](src/main/java/ooad/project1a)
  - [Results1.txt](src/main/java/ooad/project1a/Results1.txt)
- [program 2](src/main/java/ooad/project1b)
  - [Results2.txt](src/main/java/ooad/project1b/Results2.txt)
