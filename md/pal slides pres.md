---
modified: 2025-07-23T06:32:01-06:00
theme: night
width: 2000
height: 1125
slideNumber: true
---
# Design Patterns as Language Constructs
Simon Walker  
July 21st, 2025

---
## What is good code?
### Washing Behind Your Ears: Principles of Software Hygiene
David M. Tilbrook: contributor of `QED` to unix, `vi` ancestor. And John McMullen
 $^1$

> Like personal hygiene, **software hygiene** is most conspicuous in its absence.

 > Often, quality considers only the development phase; the real picture is wider. The side of profession not usually highlighted in programming courses, is maintenance. It is widely **estimated that 70% of the cost of software is devoted to maintenance.** No discussion of software quality can be satisfactory if it neglects this aspect.

---
## The problem with design patterns
Design patterns have proven to be very useful for the design of object-oriented systems.
### But as systems grow,
(Jan Bosch)
1. The pattern can be "lost"; they can be hard to trace.
2. Reusability is difficult (the pattern is mixed with the domain class).
3. Which means there's overhead for the engineer in implementation.

which all compromise maintainability.

*Could make the design patterns more explicit?*
<!-- .slide: data-auto-animate -->

--
<!-- .slide: data-auto-animate -->
1. The pattern can be "lost"; they can be hard to trace.

This first point is crucial.

In Fundamentals of Software Architecture textbook, (Mark Richards & Neal Ford)
$$^2$$
- **First Law: Everything in software architecture is a trade-off**
- **Second Law: _Why_ is more important than _how_**
 
From the code, the **how** is apparent. The **why**?  Not so much. 

Sometimes even the **what** is unclear.
From the code, its not always clear which design patterns are present unless you have the class diagram. 
<!-- .slide: data-auto-animate -->

--
<!-- .slide: data-auto-animate -->
1. The pattern can be "lost"; they can be hard to trace.
- **Second Law: _Why_ is more important than _how_**

It is not always clear which design patterns are present without a class diagram.

$$~$$

1. And if you're missing the diagram, it's hard to iterate
2. and bring in new devs.
3. You could make sure you have the diagram.
   But as you refactor code, you'd have to maintain it.

$$~$$

But perpetually maintaining the diagram alongside the codebase, would be like maintaining two tightly coupled classes, without encapsulation, and all at the same low level of abstraction.

We could avoid the problem **if the design were apparent from within the code**.

---
Can we make design patterns more maintainable?
### Layered Object Model Approach
LayOM$^3$ solves this problem with a new language that transpiles into C++

```C++
class Subject {  
private:  
	int state;  
	// Manual list of observers  

public:  
	void setState(int newState) {  
	this->state = newState;  
	// Manually notify all observers  
	}  
};
```

Let's say we want to implement the observer pattern on this subject.
In LayOM, we could do that by appended the pattern to the class through a **Layer**.

```
class Subject
  layers
    st: Observer (notify after on setValue on aspect "value changed");

  variables
    value: Integer;

  methods
    setValue(newValue: Integer)
      begin
        value := newValue;
      end;
end;
```

<!-- .slide: data-auto-animate -->

--
<!-- .slide: data-auto-animate -->
### Layered Object Model Approach
```
class Subject
  layers
    st: Observer (notify after on setValue on aspect "value changed");

  variables
    value: Integer;

  methods
    setValue(newValue: Integer)
      begin
        value := newValue;
      end;

end;
```

`Observer` is one of many built-in **Layers**.

LayOM follows a form of separating layers, variables, and methods.

**It has it's strengths**
1. It answers the core mission -- design patterns as language constructs
2. Most common design patterns are built-in.
3. They're concise and reusable.
4. And, we can add more layer types; it's extensible.
<!-- .slide: data-auto-animate -->

--
<!-- .slide: data-auto-animate -->
### Layered Object Model Approach
**Where it struggles**
1. But you'd have to write code in their language
2. which isn't widely used
3. and only transpiles to C++.
4. The patterns need to be defined upfront
5. and it's tricky to add support for more.

In my opinion, it would be risky to commit a large project to this language.

Can we simplify it?

---
## Functional Architecture
Functional programming is a surprisingly perfect tool here.
It's well-suited for capturing architectural decisions and design patterns.

*(Scott Wlaschin)*
| Pattern or Principle            | Functional Programming  |
| ------------------------------- | ----------------------- |
| Single Responsibility Principle | Functions               |
| Open/Closed Principle           | Functions               |
| Dependency Inversion Principle  | Functions, also         |
| Interface Segregation Principle | Functions               |
| Factory Pattern                 | Yes, functions          |
| Strategy Pattern                | Oh my, functions again! |
| Decorator Pattern               | Functions               |
| Visitor Pattern                 | Functions[]             |

**Patterns are not special techniques, they are the default, idiomatic way of writing code**

And we can borrow a few principles for our architecture without locking into a FP language.
<!-- .slide: data-auto-animate -->

--
<!-- .slide: data-auto-animate -->
## Functional Architecture
**Patterns are not special techniques, they are the default, idiomatic way of writing code**

*(Philip Wadler)*

Use OOP if:
> you start with a fixed set of _operations_, and as your code evolves, you primarily add new *types*.
$^5$

Use FP if:
> you start with a fixed set of _types_, and as your code evolves, you primarily add new _operations_
$^5$

In my opinion, it is usually the behaviors that grow most over time (#2) -- the latter case
<!-- .slide: data-auto-animate -->

--
<!-- .slide: data-auto-animate -->
## Functional Architecture
### But what is Functional Programming?
*(Grokking Simplicity by Eric Normand)*

about "Taming Complex Software with Functional Thinking"

Normand defines FP as
> a programming style that uses only pure functions without side effects

(That is, functions that always produce the same output for the same input)

but he acknowledges some issues with the definition because all code has "side effects"
> What good is email software that doesn't send emails?

and FP is actually great at handling side effects.
<!-- .slide: data-auto-animate -->

--
<!-- .slide: data-auto-animate -->
## Functional Architecture
### But what is Functional Programming?
1. Functional programming is actually about **encapsulating what varies**
2. and minimizing what varies
3. using ironclad contracts$^6$ (pure functions).
	- When you look at a pure function, you can make strong guarantees about what it does and what it depends upon.

$state_{1} = f(event, state_{0})$

It's organized around a function that creates the next global state based on the new data and the previous state.
	

---
## A concrete example
- Let's imagine we have a music store that buys and sells items to customers each day.
- The store has an inventory and a cash register and may withdraw from a bank when it's low on funds.
- Clerks tune some items each day with different tuning strategies.
- Items have some basic attributes that remain constant and other attributes such as list price and sale price that may vary.

---

### First, we define the state

We can use type unions to avoid inheritance, although inheritance would be perfectly reasonable here too.
```ts
// Discriminated union for all items in the store
type Item =
  | { kind: "guitar", name: string, price: number, isTuned: boolean }
  | { kind: "mandolin", name: string, price: number, isTuned: boolean }
  | { kind: "hat", name: string, price: number, size: "S" | "M" | "L" };

// A record of an item including its dynamic pricing
type SoldItem = {
  item: Item;
  salePrice: number;
};

```
Notice how simple this strategy pattern is
```ts
// Clerks are defined by their name and tuning ability
type Clerk = {
  name: string;
  tuningAlgorithm: (item: Item) => Item; // A function that takes an item and returns a new, tuned item
};
```
```ts
// The complete state of the store at any given moment
type StoreState = {
  inventory: Item[];
  soldItems: SoldItem[];
  register: number;
  bank: number;
  clerkForTheDay: Clerk;
};
```
---
### Next, we define the events
```ts
// A union of all possible events that can drive the simulation forward
type Event =
  | { type: "CUSTOMER_PURCHASE", itemName: string, agreedSalePrice: number }
  | { type: "CUSTOMER_SALE", item: Item, agreedPurchasePrice: number }
  | { type: "CLERK_PERFORMS_MAINTENANCE" }
  | { type: "SECURE_LOAN_FROM_BANK", amount: number };
```

We can borrow the concept of isolating what varies from functional programming. The data that enters the system is explicitly specified -- part of the imperative shell. Notice that these "events" are just data -- since the state updates at once, there isn't a need for true reactivity.

We call this the "imperative shell, functional core" architecture. $^7$

---
### Third, pure functions
The core state transition function.
```ts
/**
 * Calculates the next state of the store based on a single event.
 * @param currentState The current, immutable state of the store.
 * @param event The event that occurred.
 * @returns A new, updated StoreState.
 */
declare function processEvent(currentState: StoreState, event: Event): StoreState;
```
This function alone defines the entire evolution of the system. It is not part of a class and there are no globals for it to modify.

All other functions that evolve the state are considered helper functions of this one.
Functions can call each other, they are **composable**.

```ts
/**
 * Applies the daily maintenance logic to the entire inventory using the day's clerk.
 */
declare function performDailyMaintenance(inventory: PricedItem[], clerk: Clerk): PricedItem[];

/**
 * Has a clerk handle a buying customer, returns the new inventory and the item that was sold to the customer.
 */
declare function handleBuyer(inventory: Item[], clerk: Clerk): (Item[], SoldItem)
```

Here, buying-customers are handled as a function of a clerk and the inventory and produce a new inventory along with a sold item.

---
### Last, we implement the imperative shell
We initialize the initial state  
and define a few tuning algorithms...
```ts
// --- 1. Define the specific strategies (tuning algorithms) ---
function standardTuning(instrument: Item): Item {
  // Simple logic: just mark as tuned
  return { ...instrument, isTuned: true }
}

function meticulousTuning(instrument: Item): Item {
  // More complex logic: maybe it takes longer or is more thorough
  return { ...instrument, isTuned: true }
}
```
When we initialize Clerks, we pass in their tuning algorithm as a function. This strategy pattern is so simple it hardly requires a name.
```ts
// --- 2. Initialize the Clerks ---
// Create a list of available clerks, injecting their tuning "strategy" at creation time.
clerk_Alice = { name: "Alice", tuningAlgorithm: standardTuning }
clerk_Bob = { name: "Bob", tuningAlgorithm: meticulousTuning }
availableClerks = [clerk_Alice, clerk_Bob]
```
```ts
// --- 3. Initialize the World State ---
// This is the starting state of our simulation.
let currentState: StoreState = {
  inventory: [
    { item: { kind: "guitar", name: "Gibson", isTuned: false }, salePrice: 1200, ... },
    { item: { kind: "mandolin", name: "Eastman", isTuned: false }, salePrice: 700, ... }
  ],
  clerkForTheDay: clerk_Alice,
  ... // other initial state properties
}
```

<!-- .slide: data-auto-animate -->

--
<!-- .slide: data-auto-animate -->
### Last, we implement the imperative shell
Note that in the example, I left out implementation details. They could be done in a non-functional programming way. We're focusing on applying these principles in the big picture, only. As long as the function has no side effects, we're happy with whatever style it utilizes internally.

```ts

// --- 4. The Main Simulation Loop ---
// This loop drives the simulation forward day by day.
for day from 1 to 5 {
  print("--- Simulation Day " + day + " ---")

  chosenClerk = random_choice(availableClerks)
  print(chosenClerk.name + " is on duty today.")

  const stateForToday = { ...currentState, clerkForTheDay: chosenClerk }
  maintenanceEvent = { type: "CLERK_PERFORMS_MAINTENANCE" }

  // IMPORTANT
  currentState = processEvent(stateForToday, maintenanceEvent)
}
```

Notice on the bottom line that we update the `currentState` in a single shot. From the perspective of `processEvent`, the system is without variables.

---

## Functional Architecture

### A God Object
This has the effect of producing what looks like a God Object; an anti-pattern -- an object with far too many responsibilities.

which has one issue, it violates:
- strong abstraction
- encapsulation
- low coupling
- high cohesion

This architecture will always produce a design with centralized state and a single master function.
<!-- .slide: data-auto-animate -->

--
<!-- .slide: data-auto-animate -->
## Functional Architecture
But functional programming is unique from object-oriented because state is not coupled to logic -- this has a drastic affect on the architectural implications.
### ~~God Object~~ Centralized State
It's not an issue in functional.
- strong abstraction

> High level functions abstract unnecessary details

- encapsulation

> There is no mutable state to encapsulate

- low coupling

> Centralized state does not necessarily imply centralized logic

- high cohesion

> The composition of functions separate responsibilities
<!-- .slide: data-auto-animate -->

--
<!-- .slide: data-auto-animate -->
## Functional Architecture
### What we've gained
- Clear state management
- which means unmatched testability
- and easier debugging.
- It's inherent parallelizable
	- because there aren't side effects, we can make strong guarantees about dependencies. Since functions are consistent for consistent parameters, we can cache their values. This enables automatic and fine-grained system-wide parallelism.
<!-- .slide: data-auto-animate -->

--
<!-- .slide: data-auto-animate -->
## Functional Architecture
### What we've gained
The reason we explored this route is because **it's more maintainable**  

We set out looking to make patterns more
1. traceable
2. reusable
3. and faster to implement

with clarity of the architecture from reading the code directly

FP code is self-describing of it's architecture. The lack of state/logic coupling makes it easier to extend and refactor behavior.

---
## Design patterns become the default, idiomatic way of writing code.
### Thank you
---

Philip Wadler quote
https://stackoverflow.com/a/2079678