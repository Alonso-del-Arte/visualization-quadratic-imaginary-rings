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

File - There is no File menu yet.

Edit -> Choose discriminant... (Ctrl-D) Brings up a text box asking user to enter a negative, squarefree integer. But you can enter a positive integer and the program will multiply it by -1. And if that number is not squarefree, the program will simply move on to the next lower squarefree number (or to -1 in the case of -67108864).

Technically "discriminant" is not the right term. What is meant is the variable usually designated d, which is equal to the discriminant only when it is congruent to 1 modulo 4 (otherwise the disciminant is 4d).

Edit -> Increment discriminant (Ctrl-Up arrow) Changes d to the next higher negative, squarefree integer. Disabled when d = -1.

Edit -> Decrement discriminant (Ctrl-Down arrow) Changes d to the next lower negative, squarefree integer. Disabled when d = -67108863 (this is a somewhat arbitrary value chosen to avoid arithmetic overflow problems).

View -> Zoom in (Ctrl-Number pad plus) Zooms in by increasing pixels per unit interval by the specified zoom interval (initially 5 pixels), drawing the dots representing numbers further apart.

View -> Zoom out (Ctrl-Number pad minus) Zooms out by decreasing pixels per unit interval by the specified zoom interval (initially 5 pixels), drawing the dots representing numbers closer together.

View -> Decrease zoom interval (Ctrl-Shift-comma) Decreases the zoom interval (initially 5 pixels) by 1 pixel. Disabled when zoom interval is 1.

View -> Increase zoom interval (Ctrl-Shift-period) Increases the zoom interval (initially 5 pixels) by 1 pixel. Disabled when zoom interval is 48.

View -> Decrease dot radius (Ctrl-comma)

View -> Increase dot radius (Ctrl-period)

View -> Reset view defaults (F7)

View -> Use theta notation in readouts (T)

View -> Update readouts (F2)

Help -> About... Shows the about box with the version number and copyright notice.

## Known issues

By issues, I mean a feature I have implemented does not work as well as expected, or it does not work correctly. This is meant to exclude features I have not implemented at all. For those, see under "feature requests" (and also under "version goals").

* The program is very sluggish when the diagram is zoomed out to 2 pixels per unit interval, especially in Z[i] and Z[omega]. I don't recommend changing MINIMUM_PIXELS_PER_UNIT_INTERVAL to 1.
* The keyboard shortcuts in Mac OS leave a lot to be desired.

## Version goals

By Version 1.0, the program ought to have:
* the ability to save diagrams in a graphics format (most likely PNG).
* the ability to copy the diagram to the clipboard in a graphics format using a menu command within the program (at least for now on Windows you can use Alt-PrintScreen, or Command-Shift-3 or Command-Shift-4 on Mac OS X).

By Version 2.0, the program ought to have:
* a way for the user to change the numerical color coding

By Version 3.0, the program ought to have:
* the ability to drag the diagram in any direction ("north," "west," "south," "east") that the user wants, like you can with Google Maps.

## Feature requests

None yet, other than what is listed under "version goals."
