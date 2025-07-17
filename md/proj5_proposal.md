---
modified: 2025-07-09T06:55:53-06:00
---
## Transformer toy

**Interactive, realtime learning Transformer (AI) demo**

Proposal

> Simon Walker
> June 30th, 2025

The user draws an image and as they do, the machine learning model attempts to discern the pattern
in realtime and predict the next moves. This is done by modeling the drawing as a sequence of steps
and applying sequence-to-sequence models to infer the next moves before the user makes them. These
next moves would then be rendered for the user as a "pictorial auto-complete".

For feasibility, the drawing space will be very small (probably 16x16). This scale eases the
computational issue to the point where an advanced model can be re-trained every few seconds. This
serves as an intriguing view into the nature of what such advanced models are "thinking".

### Implementation

"Back-end" logic to be baked into the front-end for a fully standalone tool in the browser. It is
best to think of the project as composed of a front/middle/back as it would be in a production
environment, but in this case, there is no server.

**UI stack:**
- TypeScript (NodeJS runtime)
- Svelte
- TailwindCSS

**"Back-end" stack (core logic):**
- Python
- PyTorch *(define model in Python)*
- ONNX wasm runtime *(execute model on TS)*

**Major components:**
- UI
  - Draw grid -- interactive, boolean 16x16 grid of toggle-able cells.
  - Ghost effects -- model inferences are rendered as "ghost" changes to the grid.
  - General utility -- reset button, `ctrl-z` support, status/loading indicators.
- Machine-learning Model $\to$ UI, "middle-end" -- `ONNX` will be used on both the python and TS side to compile from
Python and bridge to TS. The model, **defined** in python, but **trainable** in TS. This is what enables browser
support.
- Transformer Model
  - Architected in python using PyTorch.
  - Will require finetuning/experimentation regarding architecture & hyperparameters for this purpose.
