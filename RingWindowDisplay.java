/*
 * Copyright (C) 2017 Alonso del Arte
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package imaginaryquadraticinteger;

import java.awt.*;

/**
 *
 * @author Alonso del Arte
 */
public class RingWindowDisplay {
    
    /**
     * The default pixels per unit interval. Hopefully in later versions of the program there will be the capability to change pixels per unit interval.
     */
    public static final int DEFAULT_PIXELS_PER_UNIT_INTERVAL = 40;
    /**
     * The minimum pixels per unit interval. Trying to set pixels per unit interval below this value will cause an exception.
     */
    public static final int MINIMUM_PIXELS_PER_UNIT_INTERVAL = 2;
    /**
     * The maximum pixels per unit interval. Even on an 8K display, this value might be much too large. Trying to set pixels per unit interval above this value will cause an exception.
     */
    public static final int MAXIMUM_PIXELS_PER_UNIT_INTERVAL = 6400;
    
    public static final int RING_CANVAS_HORIZ_MIN = 100;
    public static final int RING_CANVAS_VERTIC_MIN = 178;
    public static final int RING_CANVAS_DEFAULT_HORIZ_MAX = 1280;
    public static final int RING_CANVAS_DEFAULT_VERTIC_MAX = 720;
    
    public static final int DEFAULT_RING_D = -1;
    
    public static final Color DEFAULT_ZERO_COLOR = Color.BLACK;
    public static final Color DEFAULT_UNIT_COLOR = Color.WHITE;
    public static final Color DEFAULT_INERT_PRIME_COLOR = Color.CYAN;
    public static final Color DEFAULT_SPLIT_PRIME_COLOR = Color.BLUE;
    public static final Color DEFAULT_RAMIFIED_PRIME_COLOR = Color.GREEN;
    
    /**
     * The actual pixels per unit interval setting, should be initialized to DEFAULT_PIXELS_PER_UNIT_INTERVAL in the constructor. Use setPixelsPerUnitInterval(int pixelLength) to change, making sure pixelLength is greater than or equal to MINIMUM_PIXELS_PER_UNIT_INTERVAL but less than or equal to MAXIMUM_PIXELS_PER_UNIT_INTERVAL.
     */
    protected int pixelsPerUnitInterval;
    protected ImaginaryQuadraticRing imagQuadRing;
    protected ImaginaryQuadraticInteger basicImaginaryInterval;
    protected int pixelsPerBasicImaginaryInterval;
    protected Canvas ringCanvas;
    protected Graphics ringCanvasGraphics;
    
    private java.util.List<ImagQuadrIntDisplayComplexPlanePoint> windowIntegers;
    private int windowIntegersLength;
    private int halfIntegersMark;
    private int ringCanvasHorizMax;
    private int ringCanvasVerticMax;
    
    private int chosenRingD;
    
    private Color zeroColor;
    private Color unitColor;
    private Color inertPrimeColor;
    private Color splitPrimeColor;
    private Color ramifiedPrimeColor;
    
    /**
     * Change how many pixels there are per unit interval.
     * @param pixelLength An integer greater than or equal to MINIMUM_PIXELS_PER_UNIT_INTERVAL but less than or equal to MAXIMUM_PIXELS_PER_UNIT_INTERVAL. A value outside of this range will cause an IllegalArgumentException.
     */
    public void setPixelsPerUnitInterval(int pixelLength) {
        if (pixelLength < MINIMUM_PIXELS_PER_UNIT_INTERVAL) {
            throw new CoordinateSystemMismatchException("Pixels per unit interval needs to be set to greater than " + (MINIMUM_PIXELS_PER_UNIT_INTERVAL - 1), false);
        }
        if (pixelLength > MAXIMUM_PIXELS_PER_UNIT_INTERVAL) {
            throw new CoordinateSystemMismatchException("Pixels per unit interval needs to be set to less than " + (MAXIMUM_PIXELS_PER_UNIT_INTERVAL + 1), false);
        }
        pixelsPerUnitInterval = pixelLength;
    }
    
    private void collectWindowIntegers() {
        
        int minX, maxX, minY, maxY;
        int halfIntMinX, halfIntMaxX, halfIntMinY, halfIntMaxY;
        int currX, currY;
        ImagQuadrIntDisplayComplexPlanePoint currAlgInteger;
        
        currX = 0;
        currY = 0;
        
        maxX = (int) Math.floor((ringCanvasHorizMax/pixelsPerUnitInterval)/2);
        minX = (-1) * maxX;
        if (imagQuadRing.d1mod4) {
            maxY = (int) Math.floor(ringCanvasVerticMax/pixelsPerBasicImaginaryInterval);
        } else {
            maxY = (int) Math.floor((ringCanvasVerticMax/pixelsPerBasicImaginaryInterval)/2);
        }
        minY = (-1) * maxY;
        for (int x = minX; x < maxX; x++) {
            for (int y = minY; y < minY; y++) {
                currAlgInteger = new ImagQuadrIntDisplayComplexPlanePoint(x, y, imagQuadRing, 1, currX, currY);
                windowIntegers.add(currAlgInteger);
            }
        }
        halfIntegersMark = windowIntegers.size();
        if (imagQuadRing.d1mod4) {
            // TODO: Collect the "half-integers"
        }
        windowIntegersLength = windowIntegers.size();
        
    }
    
    private ImaginaryQuadraticInteger integerLookUp(int coordX, int coordY) {
        int currIndex = 0;
        if (coordX < 0 || coordX > ringCanvasHorizMax || coordY < 0 || coordY > ringCanvasVerticMax) {
            throw new IllegalArgumentException("Coordinates with negative values are not allowed.");
        }
        if (coordX < 0 || coordX > ringCanvasHorizMax || coordY < 0 || coordY > ringCanvasVerticMax) {
            throw new IllegalArgumentException("Coordinates exceed given canvas size.");
        }
        // TODO: Lookup logic goes here
        return windowIntegers.get(currIndex);
    }
    
    public void changeRingWindowDimensions(int newHorizMax, int newVerticMax) {
        if (newHorizMax < RING_CANVAS_HORIZ_MIN || newVerticMax < RING_CANVAS_VERTIC_MIN) {
            throw new IllegalArgumentException("New window dimensions need to be equal or greater than supplied minimums.");
        }
        this.ringCanvasHorizMax = newHorizMax;
        this.ringCanvasVerticMax = newVerticMax;
    }
    
    public void changePointColorCodes(Color newZeroColor, Color newUnitColor, Color newInertPrimeColor, Color newSplitPrimeColor, Color newRamifiedPrimeColor) {
        this.zeroColor = newZeroColor;
        this.unitColor = newUnitColor;
        this.inertPrimeColor = newInertPrimeColor;
        this.splitPrimeColor = newSplitPrimeColor;
        this.ramifiedPrimeColor = newRamifiedPrimeColor;
    }
    
    /**
     * Set the imaginary quadratic ring for which to draw a window of
     * @param iR The imaginary quadratic integer ring to work in
     */
    private void setRing(ImaginaryQuadraticRing iR) {
        double imagInterval;
        this.imagQuadRing = iR;
        imagInterval = this.pixelsPerUnitInterval * this.imagQuadRing.absNegRadSqrt;
        if (imagQuadRing.d1mod4) {
            imagInterval /= 2;
        }
        this.pixelsPerBasicImaginaryInterval = (int) Math.floor(imagInterval);
    }
    
    public RingWindowDisplay(int ringChoice) {
        
        ImaginaryQuadraticRing imR;
        
        this.pixelsPerUnitInterval = DEFAULT_PIXELS_PER_UNIT_INTERVAL;
        this.ringCanvas = new Canvas();
        this.ringCanvasHorizMax = RING_CANVAS_DEFAULT_HORIZ_MAX;
        this.ringCanvasVerticMax = RING_CANVAS_DEFAULT_VERTIC_MAX;
        this.zeroColor = DEFAULT_ZERO_COLOR;
        this.unitColor = DEFAULT_UNIT_COLOR;
        this.inertPrimeColor = DEFAULT_INERT_PRIME_COLOR;
        this.splitPrimeColor = DEFAULT_SPLIT_PRIME_COLOR;
        this.ramifiedPrimeColor = DEFAULT_RAMIFIED_PRIME_COLOR;
        
        if (ringChoice > 0) {
            ringChoice *= -1;
        }
        if (NumberTheoreticFunctionsCalculator.isSquareFree(ringChoice)) {
            imR = new ImaginaryQuadraticRing(ringChoice);
        } else {
            imR = new ImaginaryQuadraticRing(DEFAULT_RING_D);
        }
        this.setRing(imR);
        
    }
    
    public static void main(String[] args) {
        
        RingWindowDisplay rwd = new RingWindowDisplay(DEFAULT_RING_D);
        
        
        
        
    }

    
}
