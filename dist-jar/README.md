Version 0.95 of the overall program, Version 0.97 of RingWindowDisplay. To execute this program, download the executable JAR file to a computer that has Java on it (at least the Java Runtime Environment. To compile the program from the source code, you will need the Java Development Kit

Note: this JAR does not include the Scala classes `ImagQuadInt`, `ImagQuadRing` nor `NTFC`.

# User Manual

With the executable JAR file downloaded to a computer with the necessary Java components installed, the easiest way to run the program is simply to double-click on the JAR file. Alternatively you may load the source code into an integrated development environment (IDE). A third option, if you have the Scala binaries on your system, is to load the JAR file into the standard Scala REPL and then use the command `imaginaryquadraticinteger.RingWindowDisplay.startRingWindowDisplay(-1)`.

The program should then start, displaying a window with a menu bar and a diagram of Gaussian primes at 40 pixels per unit interval.

## Menu commands

Keyboard shortcuts using the Ctrl key are understood to be Windows keyboard shortcuts, while keyboard shortcuts using the Command key are understood to be Mac OS X keyboard shortcuts. The operating system will be specified only when absolutely necessary for clarity. I have not tested this program on Mac OS 9 nor earlier, nor Linux nor any other operating system.

File -> Save diagram as... (Ctrl-Shift-S or Command-Shift-S) Saves the currently displayed diagram as a Portable Network Graphics (PNG) file. The initially suggested directory is probably the user's Documents folder; the program tries to keep track of where a file was last saved but this will be forgotten if the program is quit and restarted. The suggested filename consists of an ASCII label for the displayed ring (e.g., "ZI" for the ring of Gaussian integers, "ZW" for the ring of Eisenstein integers), the letters "pxui" (which stand for "pixels per unit interval") and a number like 5 or 40 (meaning 5 pixels per unit interval and 40 pixels per unit interval, respectively) and the ".png" extension. This feature was added in Version 0.9.

File -> Quit (Ctrl-Q or Command-Q) Exits the program. There is no request for confirmation.

Edit -> Choose discriminant... (Ctrl-D or Command-D) Brings up a text box asking user to enter a negative, squarefree integer. But you can enter a positive integer and the program will quietly multiply it by -1. And if that number is not squarefree, the program will simply move on to the next lower squarefree number (or to -1 in the case of -67108864).

Technically "discriminant" is not the right term. What is meant is the variable usually designated d, which is equal to the discriminant only when it is congruent to 1 modulo 4 (otherwise the disciminant is actually 4d).

Edit -> Increment discriminant (Ctrl-Up arrow or Command-Y). Changes d to the next higher negative, squarefree integer. Disabled when d = -1.

Edit -> Decrement discriminant (Ctrl-Down arrow or Command-B). Changes d to the next lower negative, squarefree integer. Disabled when d = -67108863 (this is a somewhat arbitrary value chosen to avoid arithmetic overflow problems).

Edit -> Copy readouts to clipboard (Ctrl-Shift-C or Command-Shift-C). This copies the readouts about a given imaginary quadratic integer to the clipboard.

Edit -> Copy diagram to clipboard (Ctrl-Alt-C or Command-Option-C) NOT YET IMPLEMENTED AS OF VERSION 0.9.

View -> Zoom in (Ctrl-Number pad plus) Zooms in by increasing pixels per unit interval by the specified zoom interval (initially 5 pixels), drawing the dots representing numbers further apart.

View -> Zoom out (Ctrl-Number pad minus) Zooms out by decreasing pixels per unit interval by the specified zoom interval (initially 5 pixels), drawing the dots representing numbers closer together.

View -> Decrease zoom interval (Ctrl-Shift-comma or Command-Shift-comma) Decreases the zoom interval (initially 5 pixels) by 1 pixel. Disabled when zoom interval is 1.

View -> Increase zoom interval (Ctrl-Shift-period or Command-Shift-period) Increases the zoom interval (initially 5 pixels) by 1 pixel. Disabled when zoom interval is 48.

View -> Decrease dot radius (Ctrl-comma or Command-comma) Decreases the radius of the dots for 0, units and prime numbers. Disabled when dot radius is 1; in a future version I might change this so that it depends on the zoom level.

View -> Increase dot radius (Ctrl-period or Command-period) Increases the radius of the dots for 0, units and prime numbers. Disabled when dot radius is 128; in a future version I might change this so that it depends on the zoom level.

View -> Reset view defaults (F7) Resets pixels per unit interval, dot radius and zoom interval. This does not change the discriminant, nor whether or not readouts are updated, nor the preference for theta notation.

View -> Use theta notation in readouts (T) Unchecked by default.

View -> Update readouts (F2 on Windows or R on Mac OS X) Unchecked by default; in a future version I might change it so it's checked by default.

Help -> User Manual Shows this page on your default Web browser

Help -> About... Shows the about box with the version number and copyright notice.
