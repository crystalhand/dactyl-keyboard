# Dactyl
This is a fork based on Crystalhand's modifications of the dactyl keyboard. I
have edited the settings in order to use thumb cluster with only 1u keycaps. I
will upload a photo in the future.

## Crystalhand's modifications
Customization for dactyl  
- Ergodox like layout for a wider range of supported keys  
- Curve/slope front back and left/right for alphas and thumb controlled separately  
- Thumb cluster layout  
- Thumb offset and orientation angle  
- Number of rows (5-dactyl or 4-lightcycle)  
- Profile selection (ie high low or custom)  
- Optional detachable wrist rests with customizable height and slope  

# The Dactyl Keyboard
The Dactyl is a parameterized, split-hand, concave, columnar, ergonomic keyboard.

<img src="https://raw.githubusercontent.com/adereth/dactyl-cave/master/resources/glamourshot.png"/>

There's a talk about the motivation and design of the Dactyl that helps provide context for this repo:

[![IMAGE ALT TEXT HERE](https://img.youtube.com/vi/uk3A41U0iO4/0.jpg)](https://www.youtube.com/watch?v=uk3A41U0iO4)

## Assembly

### Generating a Design

**Setting up the Clojure environment**
* [Install the Clojure runtime](https://clojure.org)
* [Install the Leiningen project manager](http://leiningen.org/)
* [Install OpenSCAD](http://www.openscad.org/)

**Generating the design**
1. Run `lein repl`  
2. Load the file `(load-file "src/dactyl_keyboard/dactyl.clj")`  
3. This will regenerate the `things/*.scad` files  
4. Use OpenSCAD to open a `.scad` file.  
5. Make changes to design, repeat `load-file`, OpenSCAD will watch for changes and rerender.  
6. Use the makefile to generate `.stl` files.  

**Tips**
* [Some other ways to evaluate the clojure design file](http://stackoverflow.com/a/28213489)
* [Example designing with clojure](http://adereth.github.io/blog/2014/04/09/3d-printing-with-clojure/)


### Printing
Pregenerated STL files are available in the [things/](things/) directory.

### Wiring
Masks for the flexible PCBs I used are available for the [left](resources/pcb-left.svg) and [right](resources/pcb-right.svg) side.

A [very rough guide for the brave is here](guide/README.org#wiring) - It will be improved over time (**TODO**)!

## License

Copyright © 2015 Matthew Adereth

The source code for generating the models (everything excluding the [things/](things/) and [resources/](resources/) directories is distributed under the [GNU AFFERO GENERAL PUBLIC LICENSE Version 3](LICENSE).  The generated models and PCB designs are distributed under the [Creative Commons Attribution-ShareAlike License Version 4.0](LICENSE-models).
