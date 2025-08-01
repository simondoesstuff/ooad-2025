Simon Walker's OOAD assignments

# Design Patterns as Language Constructs

Borrowing principles from functional programming can dramatically simplify architecture -- making it often self-describing. Design patterns become the default, idiomatic way of writing code.

https://simondoesstuff.github.io/ooad-2025/

# Building Java Projects

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

### Run all tests
`gradle test`

## Utilities

PlantUML is included in the nix flake to render UML diagrams from .puml files.
`plantuml file.puml` to render it into a png, `file.png` in the same directory.

# Project Details
### Project4

**Key changes:**
- To enable multiple `Store`s, the `Bank` singleton was given multiple `Account`s
accessible by name. Thus, `Store` also had to be given a name.
- What was previously `MusicLandSimulator` is now composed of `StoreManager` which associates
a `Clerk` with a `Store` and manages the order of actions.
`MusicLandSimulator` is now dedicated to the day cycle, log management, and `Clerk` sickness.
It manages sickness because `Clerk` sickness affects both `Store`s at once which this object
manages.
- Buy/sell logic was partially extracted from `Clerk` into `RandomCustomer` to enable
`CommandLineCustomer` to extend behavior.
- The "clothing ban" feature's logic was moved from `Clerk` into `Store`.
- `ClerkPool` is an object pool without automatic object instantiation. Regulates a single list of
`Clerk`s among both `Store`s.
- OVERALL:   `Clerk`'s logic has been simplified and moved into other components + latest
assignment requirements.

**Results:**
- [Proj5 Proposal.md](md/proj5_proposal.md)
- [Output.txt](src/main/java/ooad/project4/assets/logs/output.txt)
- [Test Output.txt](src/main/java/ooad/project4/assets/TestOuput.txt)
- [logs](src/main/java/ooad/project4/assets/logs)
- The primary UML diagram was strongly simplified. It has two large sections that were omitted
from the primary diagram -- related to the item hierarchy and the event types.
   - [overallDesignUML.png](src/main/java/ooad/project4/assets/overallDesignUML.png)
   - [eventsUML.png](src/main/java/ooad/project4/assets/eventsUML.png)
   - [itemsUML.png](src/main/java/ooad/project4/assets/itemsUML.png)
 - Sequence diagram [state.png](src/main/java/ooad/project4/assets/seq.png) (of project3)

**Where to find the design patterns:**
- [Factory](src/main/java/ooad/project4/ItemFactory.java)
- [Command](src/main/java/ooad/project4/CommandHandler.java)
- [Singleton (eager init)](src/main/java/ooad/project4/events/TheEventBus.java)
- [Singleton (lazy init)](src/main/java/ooad/project4/model/Bank.java)

### Project3
This project contains tests. See above for details.

**Key changes:**
 - `StoreActions` removed in favor of splitting its logic into `Store` and `Clerk`. This allows the
   possibility of new `Clerk` subtypes later.
   - Decorator:  Per instructor's approval, I did not implement the decorator for the
   item/accessory bundling feature -- see piazza. Instead, `SoldItem` extracts the `salePrice` and
   `daySold` into the decorator. This allows these fields to be final (for good reason) and yet not
   declared upon `Item` instantiation.
   - `Item` split into `BuildableItem` to introduce a necessary interface, now `interface Item`.
   - `TheEventBus` is a publish-subscribe pattern where Events can be send and received in an
   entirely decoupled way. While not a conventional Observer, this is a superset. A series of
   `StoreEvent` classes were created to support it. This allowed me to extract nearly all logging
   from `Clerk` in a strongly typed and highly scalable way.
   - `interface Tuner` helps implement the Strategy pattern for `Clerk`
   -
**Results:**
- [Output.txt](src/main/java/ooad/project3/assets/logs/Output.txt)
- [Test Results](src/main/java/ooad/project3/assets/logs/Tests.txt)
- [logs](src/main/java/ooad/project3/assets/logs)
- The primary UML diagram was strongly simplified. It has two large sections that were omitted,
related to the item hierarchy and the event types.
   - [overallDesignUML.png](src/main/java/ooad/project3/assets/overallDesignUML.png)
   - [eventsUML.png](src/main/java/ooad/project3/assets/eventsUML.png)
   - [itemsUML.png](src/main/java/ooad/project3/assets/itemsUML.png)
 - State machine [state.png](src/main/java/ooad/project3/assets/state.png) (of project2)

**Where to find the design patterns:** (as documented in the code)
- [Observer](src/main/java/ooad/project3/events/TheEventBus.java) (publish-subscribe actually)
- [Strategy](src/main/java/ooad/project3/model/store/tuning) (the related interface and subclasses,
it applies to `Clerk`)
- [Decorator](src/main/java/ooad/project3/model/item/SoldItem.java)


### Project2

**Note on architecture**
While not apparent in the output, I implemented functionality for Clerks and Customers to
instantiate random Items with all random attributes. The ItemFactory (factory) and
hierarchical builder pattern implement this feature. The ItemFactory uses reflection
to choose a random subtype and configures it's unique attributes. It returns unbuilt
items so that the caller can override financial information.

- [Output.txt](src/main/java/ooad/project2/assets/Output.txt)
    - [source](src/main/java/ooad/project2)
- [UMLDesign-PreCode.png](src/main/java/ooad/project2/assets/UMLDesign-PreCode.png)
- [UMLDesign-PostCode.png](src/main/java/ooad/project2/assets/UMLDesign-PreCode.png)
  - Changes: (also included in the diagram, top left)
    - Simplified Clerk: eliminated a circular relationship between Clerk & Store by extracting store management logic into StoreAction; clerk is now almost only a container
    - Item, Hierarchical Builder Pattern: due to unanticipated complexity regarding the ItemFactory and the need to modify items after randomization, a very flexible Builder pattern was implemented that parallels the Item inheritance hierarchy.
    - More enums: many fields in Item subtypes (such as FluteMaterial) were changed to use enums.


### Project1
- [Concepts.pdf](src/main/java/ooad/project1a/Concepts.pdf)
- [program 1](src/main/java/ooad/project1a)
  - [Results1.txt](src/main/java/ooad/project1a/Results1.txt)
- [program 2](src/main/java/ooad/project1b)
  - [Results2.txt](src/main/java/ooad/project1b/Results2.txt)
