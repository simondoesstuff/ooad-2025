Simon Walker's OOAD assignments

# Building

Using JDK 21.

**Build all,**
- If you have gradle, `gradle buildJars` and you must supply the JDK yourself.
- If you have nix, `nix develop` (which will supply all necessary
dependencies including gradle & the JDK)
- If you have nix & direnv, `direnv allow` (automatic env)

**Each runnable project or sub-project can also be built individually:**  
eg, `gradle build-project1b` to build the second program under project1.

**Run**: jars are located (after built) in `build/libs`  
eg: to run task project1b, `java -jar build/libs/project1b.jar`

## Utilities

PlantUML is included in the nix flake to render UML diagrams from .puml files.  
`plantuml file.puml` to render it into a png, `file.png` in the same directory.

# Project Details

### Project1
- [Concepts.pdf](src/main/java/ooad/project1a/Concepts.pdf)
- [program 1](src/main/java/ooad/project1a)
  - [Results1.txt](src/main/java/ooad/project1a/Results1.txt)
- [program 2](src/main/java/ooad/project1b)
  - [Results2.txt](src/main/java/ooad/project1b/Results2.txt)
