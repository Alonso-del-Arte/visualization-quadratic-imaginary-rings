NebBeans users, it would be a good idea to rename `dist-jar` just `dist`, `code-source` to `src` and `unit-testing` to `test`. IntelliJ users can simply mark the source and test folders accordingly. Whatever the IDE, you'll probably have to connect JUnit somehow.

# visualization-quadratic-imaginary-rings
WORK IN PROGRESS: A visualization of imaginary quadratic integer rings

Although not as popular as the Mandelbrot set, the diagram of prime numbers in the domain of Gaussian integers is a well known image. For example, do a Google image search for "primes in the Gaussian integers" to see a few different versions, most of them within a circle. The diagram of primes in the domain of Eisenstein integers is also fairly easy to find.

The Gaussian integers involve the square root of -1, a number usually notated as i, sometimes j. And the Eisenstein integers involve the square root of -3 in the number 1/2 + sqrt(-3)/2, often notated as the Greek lowercase omega.

But what about the square roots of -2? -5? -6? -7? etc. Might not at least a few of those numbers lead us to interesting diagrams? Hence this Java program. The negative number the square root of which will be used for the diagram will be referred to as "discriminant" (not entirely correct terminology, as I'll explain later) or just d.

This is a work in progress. When you start it up, the program shows the Gaussian integers diagram, corresponding to i = sqrt(-1), in a rectangular slice of the complex plane 1280 pixels wide by 720 pixels tall, with 40 pixels per unit interval, and 0 smack dab in the middle. This is the color coding used:

* 0 is black.
* Units are white.
* Prime factors of d are green. This is a little bit misleading because the current version of the program does not actually do anything with ideals (principal or otherwise), but it helped me orient myself before I added the readouts at the bottom of the window.
* Primes presumed inert are cyan.
* Primes confirmed split are blue.
* The "half integer" grid lines are a dark gray.
* The "regular" integer grid lines are black.
* The background is a dark blue.

By version 2.0 at the latest I'll add a way for the user to change the color coding.

It's possible to zoom out or zoom in, but the program does not yet allow the user to drag the diagram away from 0. For now, the program takes advantage of the symmetry

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

Help -> About... Shows the about box with the version number and copyright notice.

## Known issues

By issues, I mean a feature I have implemented does not work as well as expected, or it does not work correctly. This is meant to exclude features I have not implemented at all. For those, see under "feature requests" (and also under "version goals").

* The program was sluggish when the diagram is zoomed out to 2 pixels per unit interval, especially in Z[i] and Z[omega]. I don't recommend changing MINIMUM_PIXELS_PER_UNIT_INTERVAL to 1. Some basic optimization of the primeFactors() function resulted in a major performance improvement.
* The keyboard shortcuts in Mac OS leave a lot to be desired. I hope to have this issue taken care of before Version 1.0.

## Version goals

By Version 1.0, the program ought to have:
* the ability to save diagrams in a graphics format (most likely PNG). Check: added in Version 0.9.
* the ability to copy the diagram to the clipboard in a graphics format using a menu command within the program (at least for now on Windows you can use Alt-PrintScreen, or Command-Shift-3 or Command-Shift-4 on Mac OS X).
* some command-line options.

By Version 2.0, the program ought to have:
* a way for the user to change the numerical color coding.
* some internationalization.
* the ability to read in preferences from the previous session (if available). But perhaps Z[i] should always be the ring shown at start-up.

By Version 3.0, the program ought to have:
* the ability to drag the diagram in any direction ("north," "west," "south," "east") that the user wants, like you can with Google Maps.
* internationalization for the ten most spoken languages of the world.

## Feature requests

None yet, other than what is listed under "version goals."
