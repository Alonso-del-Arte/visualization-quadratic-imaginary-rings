"pxui" means "pixels per unit interval"

The files with "Diagram" in the filename were made by taking a screenshot and then processing them in Adobe Photoshop Elements.

That was before I added file save capability to the program, though that still needs some work (I need to program it so it asks before overwriting an existing file). To build the filename, it uses ImaginaryQuadraticRing.toFilenameString(), followed by "pxui" and the number of pixels per unit interval, ending with the PNG extension.

The program has no caching capability. When you tell it to draw a diagram for another ring, even if it's a ring it has previously drawn in the same session, all calculations are performed anew.
