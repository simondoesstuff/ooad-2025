---
modified: 2025-06-27T00:27:55-06:00
---

top
::: block

#### Header
_and_
Paragraph content
*in same block*

:::
bottom

```mermaid
classDiagram
    note "From Duck till Zebra"
    Animal <|-- Duck
    note for Duck "can fly\ncan swim\ncan dive\ncan help in debugging"
    Animal <|-- Fish
    Animal <|-- Zebra
    Animal : +int age
    Animal : +String gender
    Animal: +isMammal()
    Animal: +mate()
    class Duck{
        +String beakColor
        +swim()
        +quack()
    }
    class Fish{
        -int sizeInFeet
        -canEat()
    }
    class Zebra{
        +bool is_wild
        +run()
    }


```

note: gay?

---
```md
::: block

#### Header
_and_
Paragraph content
*in same block*

:::

---
```
# hi

> sdfs

```py
a = 3
if a:
   a = a ** 2

```

hi there
<!-- element class="red" -->
my name is,
`simon`

--

::: GOOD 
sdf
sd

::: a

---

sdf
- ds
- sdf
	- sdfsdf
		sdfsdfsdf

	s

*8sd*

**sdf**

| sdf | sdf |
| --- | --- |
| sd  | da  |
