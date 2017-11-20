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
import java.awt.event.*;
import javax.swing.*;

/**
 *
 * @author Alonso del Arte
 */
public final class RingWindowDisplay extends JPanel implements ActionListener, MouseMotionListener {
    
    /**
     * The default number of pixels per unit interval. The protected variable pixelsPerUnitInterval is initialized to this value.
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
    
    /**
     * The minimum horizontal pixel dimension for the canvas in which to draw the diagram.
     * This should be small even on moderately obsolete mobile devices.
     */
    public static final int RING_CANVAS_HORIZ_MIN = 100;
    
    /**
     * The minimum vertical pixel dimension for the canvas in which to draw the diagram.
     * This should be small even on moderately obsolete mobile devices.
     */
    public static final int RING_CANVAS_VERTIC_MIN = 178;
    
    /**
     * The default horizontal pixel dimension for the canvas in which to draw the diagram.
     * This fills up most of the screen on a 1440 by 900 (16:10 aspect ration) display.
     */
    public static final int RING_CANVAS_DEFAULT_HORIZ_MAX = 1280;
    
    /**
     * The default vertical pixel dimension for the canvas in which to draw the diagram.
     * This fills up most of the screen on a 1440 by 900 (16:10 aspect ration) display.
     */
    public static final int RING_CANVAS_DEFAULT_VERTIC_MAX = 720;
    
    public static final int DEFAULT_RING_D = -1;
    /**
     * The minimum integer the square root of which can be used to generate an imaginary quadratic integer ring for the purpose of display in this ring window.
     * Although technically an ImaginaryQuadraticRing can be defined with the square root of -2147483647 (which is a prime number), this would quickly lead to arithmetic overflow problems.
     * Hopefully this value of -67108863 = (-1) * 3 * 2371 * 8191 is "small" enough not to cause arithmetic overflow problems with the largest zoom out setting, but "large" enough to be of no interest to most users of this program.
     */
    public static final int MINIMUM_RING_D = Integer.MIN_VALUE/32 + 1;
    
    public static final int DEFAULT_DOT_RADIUS = 5;
    public static final int MINIMUM_DOT_RADIUS = 1;
    public static final int MAXIMUM_DOT_RADIUS = 128;
    
    public static final int DEFAULT_ZOOM_INTERVAL = 5;
    public static final int MINIMUM_ZOOM_INTERVAL = 1;
    public static final int MAXIMUM_ZOOM_INTERVAL = 48;
    
    public static final Color DEFAULT_CANVAS_BACKGROUND_COLOR = new Color(2107440); // A dark blue
    
    public static final Color DEFAULT_HALF_INTEGER_GRID_COLOR = Color.DARK_GRAY;
    public static final Color DEFAULT_INTEGER_GRID_COLOR = Color.BLACK;
    
    public static final Color DEFAULT_ZERO_COLOR = Color.BLACK;
    public static final Color DEFAULT_UNIT_COLOR = Color.WHITE;
    public static final Color DEFAULT_INERT_PRIME_COLOR = Color.CYAN;
    public static final Color DEFAULT_SPLIT_PRIME_COLOR = Color.BLUE;
    public static final Color DEFAULT_RAMIFIED_PRIME_COLOR = Color.GREEN;
    
    public static final int DEFAULT_READOUT_FIELD_COLUMNS = 20;
    
    /**
     * The actual pixels per unit interval setting, should be initialized to DEFAULT_PIXELS_PER_UNIT_INTERVAL in the constructor. Use setPixelsPerUnitInterval(int pixelLength) to change, making sure pixelLength is greater than or equal to MINIMUM_PIXELS_PER_UNIT_INTERVAL but less than or equal to MAXIMUM_PIXELS_PER_UNIT_INTERVAL.
     */
    protected int pixelsPerUnitInterval;
    protected ImaginaryQuadraticRing imagQuadRing;
    private ImaginaryQuadraticInteger mouseIQI;
    protected int pixelsPerBasicImaginaryInterval;
    
    /**
     * When imagQuadRing.d1mod4 is true, some users may prefer to see "half-integers" notated with theta notation rather than fractions with 2s for denominators.
     * With this preference turned on and d = -3, omega will be used rather than theta.
     * omega = -1/2 + sqrt(-3)/2.
     * theta = 1/2 + sqrt(d)/2.
     */
    private boolean preferenceForThetaNotation;
    
    private int ringCanvasHorizMax;
    private int ringCanvasVerticMax;
    
    private int dotRadius;
    private int zoomInterval;
    
    private Color backgroundColor;
    
    private Color halfIntegerGridColor;
    private Color integerGridColor;
    
    private Color zeroColor;
    private Color unitColor;
    private Color inertPrimeColor;
    private Color splitPrimeColor;
    private Color ramifiedPrimeColor;
    
    private int zeroCoordX, zeroCoordY;
    private boolean zeroCentered, zeroInView;
    
    private JFrame ringFrame;
    
    private JMenuBar ringWindowMenuBar;
    private JMenu ringWindowMenu;
    private JMenuItem ringWindowMenuItem;
    private JMenuItem chooseDMenuItem, increaseDMenuItem, decreaseDMenuItem;
    private JMenuItem zoomInMenuItem, zoomOutMenuItem;
    private JMenuItem decreaseZoomIntervalMenuItem, increaseZoomIntervalMenuItem;
    private JMenuItem decreaseDotRadiusMenuItem, increaseDotRadiusMenuItem;
    private JMenuItem resetViewDefaultsMenuItem;
    private JCheckBoxMenuItem preferThetaNotationMenuItem, toggleReadOutsEnabledMenuItem;
    private JMenuItem aboutMenuItem;
    
    private JTextField algIntReadOut, algIntTraceReadOut, algIntNormReadOut, algIntPolReadOut;
    
    private void drawGrids(Graphics graphicsForGrids) {
        
        int verticalGridDistance;
        int currPixelPos, currReflectPixelPos;
        boolean withinBoundaries = true;
        
        verticalGridDistance = this.pixelsPerBasicImaginaryInterval;
        if (this.imagQuadRing.d1mod4) {
            // Draw horizontal lines of half integer grid
            currPixelPos = this.zeroCoordY + verticalGridDistance;
            currReflectPixelPos = this.zeroCoordY - verticalGridDistance;
            graphicsForGrids.setColor(halfIntegerGridColor);
            verticalGridDistance *= 2;
            while (withinBoundaries) {
                withinBoundaries = (currPixelPos < this.ringCanvasVerticMax) && (currReflectPixelPos > -1);
                if (withinBoundaries) {
                    graphicsForGrids.drawLine(0, currPixelPos, this.ringCanvasHorizMax, currPixelPos);
                    graphicsForGrids.drawLine(0, currReflectPixelPos, this.ringCanvasHorizMax, currReflectPixelPos);
                    currPixelPos += verticalGridDistance;
                    currReflectPixelPos -= verticalGridDistance;
                }
            }
            // Draw vertical lines of half integer grid
            int halfHorizontalGridDistance = this.pixelsPerUnitInterval;
            if (halfHorizontalGridDistance % 2 == 1) {
                halfHorizontalGridDistance--;
            }
            halfHorizontalGridDistance /= 2;
            withinBoundaries = true;
            currPixelPos = this.zeroCoordX + halfHorizontalGridDistance;
            currReflectPixelPos = this.zeroCoordX - halfHorizontalGridDistance;
            while (withinBoundaries) {
                withinBoundaries = (currPixelPos < ringCanvasHorizMax) && (currReflectPixelPos > -1);
                if (withinBoundaries) {
                    graphicsForGrids.drawLine(currPixelPos, 0, currPixelPos, this.ringCanvasVerticMax);
                    graphicsForGrids.drawLine(currReflectPixelPos, 0, currReflectPixelPos, this.ringCanvasVerticMax);
                    currPixelPos += this.pixelsPerUnitInterval;
                    currReflectPixelPos -= this.pixelsPerUnitInterval;
                }
            }
        } 
        // Draw horizontal lines of integer grid
        withinBoundaries = true;
        graphicsForGrids.setColor(integerGridColor);
        graphicsForGrids.drawLine(0, this.zeroCoordY, this.ringCanvasHorizMax, this.zeroCoordY);
        currPixelPos = this.zeroCoordY;
        currReflectPixelPos = currPixelPos;
        while (withinBoundaries) {
            currPixelPos += verticalGridDistance;
            currReflectPixelPos -= verticalGridDistance;
            withinBoundaries = (currPixelPos < ringCanvasVerticMax) && (currReflectPixelPos > -1);
            if (withinBoundaries) {
                graphicsForGrids.drawLine(0, currPixelPos, this.ringCanvasHorizMax, currPixelPos);
                graphicsForGrids.drawLine(0, currReflectPixelPos, this.ringCanvasHorizMax, currReflectPixelPos);
            }
        }
        // Draw vertical lines of integer grid
        graphicsForGrids.drawLine(this.zeroCoordX, 0, this.zeroCoordX, this.ringCanvasVerticMax);
        currPixelPos = this.zeroCoordX;
        currReflectPixelPos = currPixelPos;
        withinBoundaries = true;
        while (withinBoundaries) {
            currPixelPos += this.pixelsPerUnitInterval;
            currReflectPixelPos -= this.pixelsPerUnitInterval;
            withinBoundaries = (currPixelPos < ringCanvasHorizMax) && (currReflectPixelPos > -1);
            if (withinBoundaries) {
                graphicsForGrids.drawLine(currPixelPos, 0, currPixelPos, this.ringCanvasVerticMax);
                graphicsForGrids.drawLine(currReflectPixelPos, 0, currReflectPixelPos, this.ringCanvasVerticMax);
            }
        }
                
    }
    
    private void drawPoints(Graphics graphicsForPoints) {
        
        int currPointX, currPointY;
        int currNegPointX, currNegPointY;
        
        int dotDiameter = 2 * this.dotRadius;
        
        int maxX = (int) Math.floor((this.ringCanvasHorizMax - this.zeroCoordX)/this.pixelsPerUnitInterval);
        int maxY;
        int verticalGridDistance = this.pixelsPerBasicImaginaryInterval;
        
        ImaginaryQuadraticInteger currIQI;
        Color currColor;
        
        int currSplitPrime, currSplitPrimePointX, currNegSplitPrimePointX;
        
        if (this.imagQuadRing.d1mod4) {
            maxY = (int) Math.floor((this.ringCanvasVerticMax - this.zeroCoordY)/(2 * this.pixelsPerBasicImaginaryInterval));
            verticalGridDistance *= 2;
        } else {
            maxY = (int) Math.floor((this.ringCanvasVerticMax - this.zeroCoordY)/this.pixelsPerBasicImaginaryInterval);
        }
        
        // The central point, 0
        currPointX = this.zeroCoordX;
        currPointY = this.zeroCoordY;
        graphicsForPoints.setColor(zeroColor);
        graphicsForPoints.fillOval(currPointX - this.dotRadius, currPointY - this.dotRadius, dotDiameter, dotDiameter);
        
        // The purely real unit points, -1 and 1
        currNegPointX = currPointX - this.pixelsPerUnitInterval;
        currPointX += this.pixelsPerUnitInterval;
        graphicsForPoints.setColor(unitColor);
        graphicsForPoints.fillOval(currPointX - this.dotRadius, currPointY - this.dotRadius, dotDiameter, dotDiameter);
        graphicsForPoints.fillOval(currNegPointX - this.dotRadius, currPointY - this.dotRadius, dotDiameter, dotDiameter);
        
        // The other purely real integer points
        for (int x = 2; x <= maxX; x++) {
            currPointX += this.pixelsPerUnitInterval;
            currNegPointX -= this.pixelsPerUnitInterval;
            if (NumberTheoreticFunctionsCalculator.isPrime(x)) {
                if (NumberTheoreticFunctionsCalculator.euclideanGCD(x, this.imagQuadRing.negRad) > 1) {
                    currColor = this.ramifiedPrimeColor;
                } else {
                    currColor = this.inertPrimeColor; // Assume the prime to be inert for now
                }
                graphicsForPoints.setColor(currColor);
                graphicsForPoints.fillOval(currPointX - this.dotRadius, currPointY - this.dotRadius, dotDiameter, dotDiameter);
                graphicsForPoints.fillOval(currNegPointX - this.dotRadius, currPointY - this.dotRadius, dotDiameter, dotDiameter);
            }
        }
        
        // The purely imaginary integer points other than 0
        if (this.imagQuadRing.negRad == -1) {
            // Take care to color the units in Z[i]
            currPointX = this.zeroCoordX;
            // currPointY = this.zeroCoordY; ???currPointY should not have changed from before, right????
            currPointY = this.zeroCoordY + verticalGridDistance;
            currNegPointY = this.zeroCoordY - verticalGridDistance;
            graphicsForPoints.setColor(this.unitColor);
            graphicsForPoints.fillOval(currPointX - this.dotRadius, currPointY - this.dotRadius, dotDiameter, dotDiameter);
            graphicsForPoints.fillOval(currPointX - this.dotRadius, currNegPointY - this.dotRadius, dotDiameter, dotDiameter);
            graphicsForPoints.setColor(this.inertPrimeColor);
            for (int y = 2; y <= maxY; y++) {
                currPointY += verticalGridDistance;
                currNegPointY -= verticalGridDistance;
                if (NumberTheoreticFunctionsCalculator.isPrime(y)) {
                    graphicsForPoints.fillOval(currPointX - this.dotRadius, currPointY - this.dotRadius, dotDiameter, dotDiameter);
                    graphicsForPoints.fillOval(currPointX - this.dotRadius, currNegPointY - this.dotRadius, dotDiameter, dotDiameter);
                }
            }
        } else {
            currPointX = this.zeroCoordX;
            // currPointY = this.zeroCoordY; ???currPointY should not have changed from before, right????
            currNegPointY = currPointY;
            for (int y = 1; y <= maxY; y++) {
                currPointY += verticalGridDistance;
                currNegPointY -= verticalGridDistance;
                currIQI = new ImaginaryQuadraticInteger(0, y, this.imagQuadRing, 1);
                if (NumberTheoreticFunctionsCalculator.isPrime(currIQI.norm())) {
                    if (NumberTheoreticFunctionsCalculator.euclideanGCD(currIQI.norm(), this.imagQuadRing.negRad) > 1) {
                        currColor = this.ramifiedPrimeColor;
                    } else {
                        currColor = this.inertPrimeColor;
                    }
                    graphicsForPoints.setColor(currColor);
                    graphicsForPoints.fillOval(currPointX - this.dotRadius, currPointY - this.dotRadius, dotDiameter, dotDiameter);
                    graphicsForPoints.fillOval(currPointX - this.dotRadius, currNegPointY - this.dotRadius, dotDiameter, dotDiameter);
                }
            }
        }
        
        // Now the complex integer points, but not the "half-integers" yet
        for (int x = 1; x <= maxX; x++) {
            currPointX = this.zeroCoordX + (x * this.pixelsPerUnitInterval);
            currNegPointX = this.zeroCoordX - (x * this.pixelsPerUnitInterval);
            for (int y = 1; y <= maxY; y++) {
                currPointY = this.zeroCoordY + (y * verticalGridDistance);
                currNegPointY = this.zeroCoordY - (y * verticalGridDistance);
                currIQI = new ImaginaryQuadraticInteger(x, y, this.imagQuadRing, 1);
                if (NumberTheoreticFunctionsCalculator.isPrime(currIQI.norm())) {
                    graphicsForPoints.setColor(this.inertPrimeColor);
                    graphicsForPoints.fillOval(currPointX - this.dotRadius, currPointY - this.dotRadius, dotDiameter, dotDiameter);
                    graphicsForPoints.fillOval(currPointX - this.dotRadius, currNegPointY - this.dotRadius, dotDiameter, dotDiameter);
                    graphicsForPoints.fillOval(currNegPointX - this.dotRadius, currPointY - this.dotRadius, dotDiameter, dotDiameter);
                    graphicsForPoints.fillOval(currNegPointX - this.dotRadius, currNegPointY - this.dotRadius, dotDiameter, dotDiameter);
                    currSplitPrime = currIQI.norm();
                    if (currSplitPrime <= maxX) {
                        currSplitPrimePointX = this.zeroCoordX + (currSplitPrime * this.pixelsPerUnitInterval);
                        currNegSplitPrimePointX = this.zeroCoordX - (currSplitPrime * this.pixelsPerUnitInterval);
                        graphicsForPoints.setColor(this.splitPrimeColor);
                        graphicsForPoints.fillOval(currSplitPrimePointX - this.dotRadius, this.zeroCoordY - this.dotRadius, dotDiameter, dotDiameter);
                        graphicsForPoints.fillOval(currNegSplitPrimePointX - this.dotRadius, this.zeroCoordY - this.dotRadius, dotDiameter, dotDiameter);
                    }
                    if (currSplitPrime <= maxY && this.imagQuadRing.negRad == -1) {
                        currSplitPrimePointX = this.zeroCoordY + (currSplitPrime * this.pixelsPerUnitInterval);
                        currNegSplitPrimePointX = this.zeroCoordY - (currSplitPrime * this.pixelsPerUnitInterval);
                        graphicsForPoints.fillOval(this.zeroCoordX - this.dotRadius, currSplitPrimePointX - this.dotRadius, dotDiameter, dotDiameter);
                        graphicsForPoints.fillOval(this.zeroCoordX - this.dotRadius, currNegSplitPrimePointX - this.dotRadius, dotDiameter, dotDiameter);
                    }
                    
                }
            }
        }
        
        // Last but not least, the "half-integers"
        if (this.imagQuadRing.d1mod4) {
            int halfUnitInterval = pixelsPerUnitInterval;
            if (halfUnitInterval % 2 == 1) {
                halfUnitInterval--;
            }
            halfUnitInterval /= 2;
            int halfMaxX = 2 * maxX;
            int halfMaxY = (int) Math.floor((this.ringCanvasVerticMax - this.zeroCoordY)/this.pixelsPerBasicImaginaryInterval);
            currPointX = this.zeroCoordX + halfUnitInterval;
            currNegPointX = this.zeroCoordX - halfUnitInterval;
            currPointY = this.zeroCoordY + pixelsPerBasicImaginaryInterval;
            currNegPointY = this.zeroCoordY - pixelsPerBasicImaginaryInterval;
            // Take care of the other units among the Eisenstein integers
            if (this.imagQuadRing.negRad == -3) {
                graphicsForPoints.setColor(unitColor);
                graphicsForPoints.fillOval(currPointX - this.dotRadius, currPointY - this.dotRadius, dotDiameter, dotDiameter);
                graphicsForPoints.fillOval(currPointX - this.dotRadius, currNegPointY - this.dotRadius, dotDiameter, dotDiameter);
                graphicsForPoints.fillOval(currNegPointX - this.dotRadius, currPointY - this.dotRadius, dotDiameter, dotDiameter);
                graphicsForPoints.fillOval(currNegPointX - this.dotRadius, currNegPointY - this.dotRadius, dotDiameter, dotDiameter);        
            }
            // And now to seek the primes
            for (int x = 1; x < halfMaxX; x += 2) {
                currPointX = this.zeroCoordX + (x * halfUnitInterval);
                currNegPointX = this.zeroCoordX - (x * halfUnitInterval);
                for (int y = 1; y <= halfMaxY; y += 2) {
                    currPointY = this.zeroCoordY + (y * this.pixelsPerBasicImaginaryInterval);
                    currNegPointY = this.zeroCoordY - (y * this.pixelsPerBasicImaginaryInterval);
                    currIQI = new ImaginaryQuadraticInteger(x, y, this.imagQuadRing, 2);
                    if (NumberTheoreticFunctionsCalculator.isPrime(currIQI.norm())) {
                        graphicsForPoints.setColor(this.inertPrimeColor);
                        graphicsForPoints.fillOval(currPointX - this.dotRadius, currPointY - this.dotRadius, dotDiameter, dotDiameter);
                        graphicsForPoints.fillOval(currPointX - this.dotRadius, currNegPointY - this.dotRadius, dotDiameter, dotDiameter);
                        graphicsForPoints.fillOval(currNegPointX - this.dotRadius, currPointY - this.dotRadius, dotDiameter, dotDiameter);
                        graphicsForPoints.fillOval(currNegPointX - this.dotRadius, currNegPointY - this.dotRadius, dotDiameter, dotDiameter);
                        currSplitPrime = currIQI.norm();
                        if (currSplitPrime <= maxX) {
                            currSplitPrimePointX = this.zeroCoordX + (currSplitPrime * this.pixelsPerUnitInterval);
                            currNegSplitPrimePointX = this.zeroCoordX - (currSplitPrime * this.pixelsPerUnitInterval);
                            graphicsForPoints.setColor(this.splitPrimeColor);
                            graphicsForPoints.fillOval(currSplitPrimePointX - this.dotRadius, this.zeroCoordY - this.dotRadius, dotDiameter, dotDiameter);
                            graphicsForPoints.fillOval(currNegSplitPrimePointX - this.dotRadius, this.zeroCoordY - this.dotRadius, dotDiameter, dotDiameter);
                        }
                    }
                }
            }
        }
        
    }

    /**
     * Change how many pixels there are per unit interval. Also concomitantly changes how many pixels there are per basic imaginary interval.
     * @param pixelLength An integer greater than or equal to MINIMUM_PIXELS_PER_UNIT_INTERVAL but less than or equal to MAXIMUM_PIXELS_PER_UNIT_INTERVAL. A value outside of this range will cause an IllegalArgumentException.
     */
    public void setPixelsPerUnitInterval(int pixelLength) {
        if (pixelLength < MINIMUM_PIXELS_PER_UNIT_INTERVAL) {
            throw new IllegalArgumentException("Pixels per unit interval needs to be set to greater than " + (MINIMUM_PIXELS_PER_UNIT_INTERVAL - 1));
        }
        if (pixelLength > MAXIMUM_PIXELS_PER_UNIT_INTERVAL) {
            throw new IllegalArgumentException("Pixels per unit interval needs to be set to less than " + (MAXIMUM_PIXELS_PER_UNIT_INTERVAL + 1));
        }
        pixelsPerUnitInterval = pixelLength;
        double imagInterval = this.pixelsPerUnitInterval * this.imagQuadRing.absNegRadSqrt;
        if (this.imagQuadRing.d1mod4) {
            imagInterval /= 2;
        }
        this.pixelsPerBasicImaginaryInterval = (int) Math.floor(imagInterval);
    }
    
    /**
     * Function to change the size of the canvas on which the ring diagrams are drawn. I have not completely thought this one through, and I certainly haven't tested it.
     * @param newHorizMax The new width of the ring window. This needs to be at least equal to RING_CANVAS_HORIZ_MIN.
     * @param newVerticMax The new height of the ring window. This needs to be at least equal to RING_CANVAS_VERTIC_MIN.
     */
    public void changeRingWindowDimensions(int newHorizMax, int newVerticMax) {
        if (newHorizMax < RING_CANVAS_HORIZ_MIN || newVerticMax < RING_CANVAS_VERTIC_MIN) {
            throw new IllegalArgumentException("New window dimensions need to be equal or greater than supplied minimums.");
        }
        this.ringCanvasHorizMax = newHorizMax;
        this.ringCanvasVerticMax = newVerticMax;
    }
    
    /**
     * Function to change the background color. I have not tested this one yet.
     * @param newBackgroundColor Preferably a color that will contrast nicely with the foreground points but which the grids can blend into.
     */
    public void changeBackgroundColor(Color newBackgroundColor) {
        this.backgroundColor = newBackgroundColor;
    }
    
    /**
     * Function to change the grid colors.
     * @param newHalfIntegerGridColor Applicable only when this.imagQuadRing.d1mod4 is true. In choosing this color, keep in mind that, when applicable, the "half-integer" grid is drawn first.
     * @param newIntegerGridColor In choosing this color, keep in mind that, when applicable, the "full" integer grid is drawn second, after the "half-integer" grid.
     */
    public void changeGridColors(Color newHalfIntegerGridColor, Color newIntegerGridColor) {
        this.halfIntegerGridColor = newHalfIntegerGridColor;
        this.integerGridColor = newIntegerGridColor;
    }
     /**
     * Function to change the colors of the points.
     * @param newZeroColor The color for the point 0.
     * @param newUnitColor The color for the units. In most imaginary quadratic rings, this color will only be used for -1 and 1.
     * @param newInertPrimeColor The color for inert primes, or at least primes having no splitting or ramifying factors in view.
     * @param newSplitPrimeColor The color for confirmed split primes.
     * @param newRamifiedPrimeColor The color for primes that are factors of the discriminant.
     */
    public void changePointColors(Color newZeroColor, Color newUnitColor, Color newInertPrimeColor, Color newSplitPrimeColor, Color newRamifiedPrimeColor) {
        this.zeroColor = newZeroColor;
        this.unitColor = newUnitColor;
        this.inertPrimeColor = newInertPrimeColor;
        this.splitPrimeColor = newSplitPrimeColor;
        this.ramifiedPrimeColor = newRamifiedPrimeColor;
    }
   
    /**
     * Function to change the dot radius.
     * @param newDotRadius Needs to be at least MINIMUM_DOT_RADIUS pixels, or else an IllegalArgumentException will be triggered.
     */
    public void changeDotRadius(int newDotRadius) {
        boolean dotRadiusOutOfBounds = false;
        String exceptionMessage = "Dot radius must be ";
        if (newDotRadius < MINIMUM_DOT_RADIUS) {
            dotRadiusOutOfBounds = true;
            exceptionMessage = exceptionMessage + "at least " + MINIMUM_DOT_RADIUS + " pixel";
            if (MINIMUM_DOT_RADIUS > 1) {
                exceptionMessage += "s.";
            } else {
                exceptionMessage += ".";
            }
            throw new IllegalArgumentException(exceptionMessage);
        } else {
            if (newDotRadius > MAXIMUM_DOT_RADIUS) {
                dotRadiusOutOfBounds = true;
                exceptionMessage = exceptionMessage + "no more than " + MAXIMUM_DOT_RADIUS + " pixels.";
            }
        }
        if (dotRadiusOutOfBounds) {
            throw new IllegalArgumentException(exceptionMessage);
        }
        this.dotRadius = newDotRadius;
    }
    
    /**
     * Function to change the zoom interval.
     * @param newZoomInterval Needs to be at least MINIMUM_ZOOM_INTERVAL pixels, or else an IllegalArgumentException will be triggered.
     */
    public void changeZoomInterval(int newZoomInterval) {
        boolean zoomIntervalOutOfBounds = false;
        String exceptionMessage = "Zoom interval must be ";
        if (newZoomInterval < MINIMUM_ZOOM_INTERVAL) {
            zoomIntervalOutOfBounds = true;
            exceptionMessage = exceptionMessage + "at least " + MINIMUM_ZOOM_INTERVAL + " pixel";
            if (MINIMUM_ZOOM_INTERVAL > 1) {
                exceptionMessage += "s.";
            } else {
                exceptionMessage += ".";
            }
        } else {
            if (newZoomInterval > MAXIMUM_ZOOM_INTERVAL) {
                zoomIntervalOutOfBounds = true;
                exceptionMessage = exceptionMessage + "no more than " + MAXIMUM_ZOOM_INTERVAL + " pixels.";
            }
        }
        if (zoomIntervalOutOfBounds) {
            throw new IllegalArgumentException(exceptionMessage);
        }
        this.zoomInterval = newZoomInterval;
    }
    
    
    /**
     * Function to change the coordinates of the point 0. I have not yet implemented a meaningful use for this function.
     * @param newCoordX The new x-coordinate for 0.
     * @param newCoordY The new y-coordinate for 0.
     */
    public void changeZeroCoords(int newCoordX, int newCoordY) {
        this.zeroCoordX = newCoordX;
        this.zeroCoordY = newCoordY;
        zeroCentered = (this.zeroCoordX == (int) Math.floor(this.ringCanvasHorizMax/2) && this.zeroCoordY == (int) Math.floor(this.ringCanvasVerticMax/2));
        zeroInView = ((this.zeroCoordX > -1) && (this.zeroCoordY > -1) && (this.zeroCoordX <= this.ringCanvasHorizMax) && (this.zeroCoordY <= this.ringCanvasVerticMax));
    }
    
    /**
     * Paints the canvas, by delegating to drawGrids() and drawPoints().
     * @param gr The graphics object supplied by the caller.
     */
    @Override
    public void paintComponent(Graphics gr) {
        super.paintComponent(gr);
        drawGrids(gr);
        drawPoints(gr);
    }
    
    /**
     * 
     * @param mauv 
     */
    @Override
    public void mouseMoved(MouseEvent mauv) {
        boolean algIntFound;
        int horizCoord, verticCoord;
        String stringForAlgIntReadOut;
        if (this.imagQuadRing.d1mod4) {
            double horizIntermediate = 4 * (mauv.getX() - this.zeroCoordX)/this.pixelsPerUnitInterval;
            horizCoord = (int) Math.round(horizIntermediate/2);
            verticCoord = (int) Math.round((-mauv.getY() + this.zeroCoordY)/this.pixelsPerBasicImaginaryInterval);
            algIntFound = (Math.abs(horizCoord % 2) == Math.abs(verticCoord % 2));
            if (algIntFound) {
                mouseIQI = new ImaginaryQuadraticInteger(horizCoord, verticCoord, this.imagQuadRing, 2);
            }
        } else {
            horizCoord = (int) Math.round((mauv.getX() - this.zeroCoordX)/this.pixelsPerUnitInterval);
            verticCoord = (int) Math.round((-mauv.getY() + this.zeroCoordY)/this.pixelsPerBasicImaginaryInterval);
            mouseIQI = new ImaginaryQuadraticInteger(horizCoord, verticCoord, this.imagQuadRing, 1);
            algIntFound = true;
        }
        if (algIntFound) {
            if (preferenceForThetaNotation) {
                stringForAlgIntReadOut = mouseIQI.toStringAlt();
            } else {
                stringForAlgIntReadOut = mouseIQI.toString();
            }
            algIntReadOut.setText(stringForAlgIntReadOut);
            algIntTraceReadOut.setText(Integer.toString(mouseIQI.trace()));
            algIntNormReadOut.setText(Integer.toString(mouseIQI.norm()));
            algIntPolReadOut.setText(mouseIQI.minPolynomialString());
        }
    }
    
    /**
     * No implementation for time being. Hope to add the capability to drag the diagram in a future version.
     * @param mauv Mouse event to respond to.
     */
    @Override
    public void mouseDragged(MouseEvent mauv) {
        // Implementation placeholder.
    }
    
    private void switchToRing(int d) {
        ImaginaryQuadraticRing imagRing = new ImaginaryQuadraticRing(d);
        this.ringFrame.setTitle("Ring Diagram for " + imagRing.toString());
        setRing(imagRing);
        repaint();
    }
    
    public void chooseDiscriminant() {
        String discrString = Integer.toString(this.imagQuadRing.negRad);
        String userChoice = (String) JOptionPane.showInputDialog(ringFrame, "Please enter a negative, squarefree integer:", discrString);
        int discr;
        boolean repaintNeeded;
        try {
            discr = Integer.parseInt(userChoice);
        } catch (NumberFormatException nfe) {
            discr = this.imagQuadRing.negRad;
        }
        if (discr > 0) {
            discr *= -1;
        }
        if (discr < MINIMUM_RING_D) {
            discr = MINIMUM_RING_D;
        }
        while (!NumberTheoreticFunctionsCalculator.isSquareFree(discr) && discr > MINIMUM_RING_D) {
            discr--;
        }
        repaintNeeded = (discr != this.imagQuadRing.negRad);
        if (repaintNeeded) {
            if (discr == MINIMUM_RING_D) {
                this.decreaseDMenuItem.setEnabled(false);
            }
            if (discr > MINIMUM_RING_D && !this.decreaseDMenuItem.isEnabled()) {
                this.decreaseDMenuItem.setEnabled(true);
            }
            if (discr == -1) {
                this.increaseDMenuItem.setEnabled(false);
            }
            if (discr < -1 && !this.increaseDMenuItem.isEnabled()) {
                this.increaseDMenuItem.setEnabled(true);
            }
            switchToRing(discr);
        }

    }

    public void incrementDiscriminant() {
        int discr = this.imagQuadRing.negRad + 1;
        while (!NumberTheoreticFunctionsCalculator.isSquareFree(discr) && discr < -1) {
            discr++;
        }
        if (discr == -1) {
            increaseDMenuItem.setEnabled(false);
        }
        if (discr == (MINIMUM_RING_D + 1)) {
            decreaseDMenuItem.setEnabled(true);
        }
        switchToRing(discr);
    }

    public void decrementDiscriminant() {
        int discr = this.imagQuadRing.negRad - 1;
        while (!NumberTheoreticFunctionsCalculator.isSquareFree(discr) && discr > (Integer.MIN_VALUE + 1)) {
            discr--;
        }
        if (discr == MINIMUM_RING_D) {
            this.decreaseDMenuItem.setEnabled(false);
        }
        if (discr == -2) {
            this.increaseDMenuItem.setEnabled(true);
        }
        switchToRing(discr);
    }
    
    public void zoomIn() {
        int newPixelsPerUnitInterval = this.pixelsPerUnitInterval + zoomInterval;
        if (newPixelsPerUnitInterval <= MAXIMUM_PIXELS_PER_UNIT_INTERVAL) {
            setPixelsPerUnitInterval(newPixelsPerUnitInterval);
            repaint();
            if ((newPixelsPerUnitInterval + zoomInterval) > MAXIMUM_PIXELS_PER_UNIT_INTERVAL) {
                this.zoomInMenuItem.setEnabled(false);
            }
        }
        if (!this.zoomOutMenuItem.isEnabled() && (newPixelsPerUnitInterval >= (MINIMUM_PIXELS_PER_UNIT_INTERVAL + zoomInterval))) {
            this.zoomOutMenuItem.setEnabled(true);
        }
    }
    
    public void zoomOut() {
        int newPixelsPerUnitInterval = this.pixelsPerUnitInterval - zoomInterval;
        if (newPixelsPerUnitInterval >= MINIMUM_PIXELS_PER_UNIT_INTERVAL) {
            setPixelsPerUnitInterval(newPixelsPerUnitInterval);
            repaint();
            if ((newPixelsPerUnitInterval - zoomInterval) < MINIMUM_PIXELS_PER_UNIT_INTERVAL) {
                this.zoomOutMenuItem.setEnabled(false);
            }
        }
        if (!this.zoomInMenuItem.isEnabled() && (newPixelsPerUnitInterval <= (MAXIMUM_PIXELS_PER_UNIT_INTERVAL - zoomInterval))) {
            this.zoomInMenuItem.setEnabled(true);
        }
    }
    
    private void informZoomIntervalChange(boolean changeFlag) {
        String notificationString = "Zoom interval is now " + this.zoomInterval + ".\nThere are " + this.pixelsPerUnitInterval + " pixels per unit interval.";
        JOptionPane.showMessageDialog(ringFrame, notificationString);
    }
    
    public void decreaseZoomInterval() {
        int newZoomInterval = this.zoomInterval - 1;
        boolean newZoomIntervalFlag = false;
        if (newZoomInterval >= MINIMUM_ZOOM_INTERVAL) {
            changeZoomInterval(newZoomInterval);
            newZoomIntervalFlag = true;
            if (this.zoomInterval == MINIMUM_ZOOM_INTERVAL) {
                this.decreaseZoomIntervalMenuItem.setEnabled(false);
            }
        }
        informZoomIntervalChange(newZoomIntervalFlag);
        if (!this.increaseZoomIntervalMenuItem.isEnabled() && (newZoomInterval < MAXIMUM_ZOOM_INTERVAL)) {
            this.increaseZoomIntervalMenuItem.setEnabled(true);
        }
    }

    public void increaseZoomInterval() {
        int newZoomInterval = this.zoomInterval + 1;
        boolean newZoomIntervalFlag = false;
        if (newZoomInterval <= MAXIMUM_ZOOM_INTERVAL) {
            changeZoomInterval(newZoomInterval);
            newZoomIntervalFlag = true;
            if (this.zoomInterval == MAXIMUM_ZOOM_INTERVAL) {
                this.increaseZoomIntervalMenuItem.setEnabled(false);
            }
        }
        informZoomIntervalChange(newZoomIntervalFlag);
        if (!this.decreaseZoomIntervalMenuItem.isEnabled() && (newZoomInterval > MINIMUM_ZOOM_INTERVAL)) {
            this.decreaseZoomIntervalMenuItem.setEnabled(true);
        }
    }

    public void decreaseDotRadius() {
        int newDotRadius = this.dotRadius - 1;
        if (newDotRadius >= MINIMUM_DOT_RADIUS) {
            changeDotRadius(newDotRadius);
            repaint();
            if (this.dotRadius == MINIMUM_DOT_RADIUS) {
                this.decreaseDotRadiusMenuItem.setEnabled(false);
            }
        }
        if (!this.increaseDotRadiusMenuItem.isEnabled() && (newDotRadius < MAXIMUM_DOT_RADIUS)) {
            this.increaseDotRadiusMenuItem.setEnabled(true);
        }
    }

    public void increaseDotRadius() {
        int newDotRadius = this.dotRadius + 1;
        if (newDotRadius <= MAXIMUM_DOT_RADIUS) {
            changeDotRadius(newDotRadius);
            repaint();
            if (this.dotRadius == MAXIMUM_DOT_RADIUS) {
                this.increaseDotRadiusMenuItem.setEnabled(false);
            }
        }
        if (!this.decreaseDotRadiusMenuItem.isEnabled() && (newDotRadius > MINIMUM_DOT_RADIUS)) {
            this.decreaseDotRadiusMenuItem.setEnabled(true);
        }
    }
    
    public void resetViewDefaults() {
        /* Since the program does not yet allow the user to change colors, the following three lines are for now unnecessary.
        changeBackgroundColor(DEFAULT_CANVAS_BACKGROUND_COLOR);
        changeGridColors(DEFAULT_HALF_INTEGER_GRID_COLOR, DEFAULT_INTEGER_GRID_COLOR);
        changePointColors(DEFAULT_ZERO_COLOR, DEFAULT_UNIT_COLOR, DEFAULT_INERT_PRIME_COLOR, DEFAULT_SPLIT_PRIME_COLOR, DEFAULT_RAMIFIED_PRIME_COLOR);
        */
        setPixelsPerUnitInterval(DEFAULT_PIXELS_PER_UNIT_INTERVAL);
        changeZoomInterval(DEFAULT_ZOOM_INTERVAL);
        changeDotRadius(DEFAULT_DOT_RADIUS);
        repaint();
        // Now to check if any menu items need to be re-enabled
        if (!this.zoomInMenuItem.isEnabled() && (this.pixelsPerUnitInterval < (MAXIMUM_PIXELS_PER_UNIT_INTERVAL - zoomInterval))) {
            this.zoomInMenuItem.setEnabled(true);
        }
        if (!this.zoomOutMenuItem.isEnabled() && (this.pixelsPerUnitInterval > (MINIMUM_PIXELS_PER_UNIT_INTERVAL + zoomInterval))) {
            this.zoomOutMenuItem.setEnabled(true);
        }
        if (!this.increaseZoomIntervalMenuItem.isEnabled() && (this.zoomInterval < MAXIMUM_ZOOM_INTERVAL)) {
            this.increaseZoomIntervalMenuItem.setEnabled(true);
        }
        if (!this.decreaseZoomIntervalMenuItem.isEnabled() && (this.zoomInterval > MINIMUM_ZOOM_INTERVAL)) {
            this.decreaseZoomIntervalMenuItem.setEnabled(true);
        }
        if (!this.increaseDotRadiusMenuItem.isEnabled() && (this.dotRadius < MAXIMUM_DOT_RADIUS)) {
            this.increaseDotRadiusMenuItem.setEnabled(true);
        }
        if (!this.decreaseDotRadiusMenuItem.isEnabled() && (this.dotRadius > MINIMUM_DOT_RADIUS)) {
            this.decreaseDotRadiusMenuItem.setEnabled(true);
        }
    }
    
    /**
     * Function to enable or disable the use of theta notation in the readout field for integer when imagQuadRing.d1mod4 is true.
     * Of course updating of readouts has to be enabled.
     */
    public void toggleThetaNotation() {
        this.preferenceForThetaNotation = this.preferThetaNotationMenuItem.isSelected();
    }
    
    /**
     * Function to enable or disable updating of the readout fields for integer, trace, norm and polynomial.
     * I don't know what it is that I forgot to do, but after enabling the readouts, the menu becomes hard to reach.
     */
    public void toggleReadOutsEnabled() {
        if (this.toggleReadOutsEnabledMenuItem.isSelected()) {
            this.addMouseMotionListener(this);
        } else {
            this.removeMouseMotionListener(this);
        }
    }
    
    private void showAboutBox() {
        JOptionPane.showMessageDialog(ringFrame, "Imaginary Quadratic Integer Ring Viewer\nVersion 0.8\n\u00A9 2017 Alonso del Arte");
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
    
    /**
     * Function to handle menu events
     * @param ae Object giving information about the menu item selected
     */
    @Override
    public void actionPerformed(ActionEvent ae) {
        switch (ae.getActionCommand()) {
            case "chooseD":
                chooseDiscriminant();
                break;
            case "incrD":
                incrementDiscriminant();
                break;
            case "decrD":
                decrementDiscriminant();
                break;
            case "zoomIn":
                zoomIn();
                break;
            case "zoomOut":
                zoomOut();
                break;
            case "decrZoomInterval":
                decreaseZoomInterval();
                break;
            case "incrZoomInterval":
                increaseZoomInterval();
                break;
            case "decrDotRadius":
                decreaseDotRadius();
                break;
            case "incrDotRadius":
                increaseDotRadius();
                break;
            case "defaultView":
                String messageForOKCancel = "This will reset pixels per unit interval, dot radius and zoom interval,\nbut not discriminant nor whether readouts are updated.";
                String titleForOKCancel = "Reset view defaults?";
                int okCancelReply = JOptionPane.showConfirmDialog(ringFrame, messageForOKCancel, titleForOKCancel, JOptionPane.OK_CANCEL_OPTION);
                if (okCancelReply == JOptionPane.OK_OPTION) {
                    resetViewDefaults();
                }
                break;
            case "toggleTheta":
                toggleThetaNotation();
                break;
            case "toggleReadOuts":
                toggleReadOutsEnabled();
                break;
            case "about":
                showAboutBox();
                break;
            default:
                System.out.println("Command not recognized.");
        }
    }
    
    private void setUpRingFrame() {
        
        ringFrame = new JFrame("Ring Diagram for " + this.imagQuadRing.toString());  
        
        ringWindowMenuBar = new JMenuBar();
        ringWindowMenu = new JMenu("Edit");
        ringWindowMenu.setMnemonic(KeyEvent.VK_E);
        ringWindowMenu.getAccessibleContext().setAccessibleDescription("Menu to change certain parameters");
        ringWindowMenuBar.add(ringWindowMenu);
        ringWindowMenuItem = new JMenuItem("Choose discriminant...");
        ringWindowMenuItem.getAccessibleContext().setAccessibleDescription("Let user enter new choice for ring discriminant");
        chooseDMenuItem = ringWindowMenu.add(ringWindowMenuItem);
        chooseDMenuItem.setActionCommand("chooseD");
        chooseDMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, Event.CTRL_MASK));
        chooseDMenuItem.addActionListener(this);
        ringWindowMenuItem = new JMenuItem("Increment discriminant");
        ringWindowMenuItem.getAccessibleContext().setAccessibleDescription("Increment the discriminant to choose another ring");
        increaseDMenuItem = ringWindowMenu.add(ringWindowMenuItem);
        increaseDMenuItem.setActionCommand("incrD");
        increaseDMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_UP, Event.CTRL_MASK));
        increaseDMenuItem.addActionListener(this);
        if (this.imagQuadRing.negRad == -1) {
            increaseDMenuItem.setEnabled(false);
        }
        ringWindowMenuItem = new JMenuItem("Decrement discriminant");
        ringWindowMenuItem.getAccessibleContext().setAccessibleDescription("Decrement the discriminant to choose another ring");
        decreaseDMenuItem = ringWindowMenu.add(ringWindowMenuItem);
        decreaseDMenuItem.setActionCommand("decrD");
        decreaseDMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, Event.CTRL_MASK));
        decreaseDMenuItem.addActionListener(this);
        if (this.imagQuadRing.negRad == Integer.MIN_VALUE + 1) {
            decreaseDMenuItem.setEnabled(false);
        }
        
    /*    ringWindowMenu.addSeparator();
        ringWindowMenuItem = new JMenuItem("Preferences...");
        ringWindowMenuItem.getAccessibleContext().setAccessibleDescription("Bring up a dialogue to adjust preferences");
        ringWindowMenu.add(ringWindowMenuItem); 
    */

        ringWindowMenu = new JMenu("View");
        ringWindowMenu.setMnemonic(KeyEvent.VK_V);
        ringWindowMenu.getAccessibleContext().setAccessibleDescription("Menu to zoom in or zoom out");
        ringWindowMenuBar.add(ringWindowMenu);
        ringWindowMenuItem = new JMenuItem("Zoom in");
        ringWindowMenuItem.getAccessibleContext().setAccessibleDescription("Zoom in, by increasing pixels per unit interval");
        zoomInMenuItem = ringWindowMenu.add(ringWindowMenuItem);
        zoomInMenuItem.setActionCommand("zoomIn");
        zoomInMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ADD, Event.CTRL_MASK));
        zoomInMenuItem.addActionListener(this);
        if (this.pixelsPerUnitInterval > (MAXIMUM_PIXELS_PER_UNIT_INTERVAL - zoomInterval)) {
            zoomInMenuItem.setEnabled(false);
        }
        ringWindowMenuItem = new JMenuItem("Zoom out");
        ringWindowMenuItem.getAccessibleContext().setAccessibleDescription("Zoom out, by decreasing pixels per unit interval");
        zoomOutMenuItem = ringWindowMenu.add(ringWindowMenuItem);
        zoomOutMenuItem.setActionCommand("zoomOut");
        zoomOutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_SUBTRACT, Event.CTRL_MASK));
        zoomOutMenuItem.addActionListener(this);
        if (this.pixelsPerUnitInterval < (MINIMUM_PIXELS_PER_UNIT_INTERVAL + zoomInterval)) {
            zoomInMenuItem.setEnabled(false);
        }
        ringWindowMenu.addSeparator();
        ringWindowMenuItem = new JMenuItem("Decrease zoom interval");
        ringWindowMenuItem.getAccessibleContext().setAccessibleDescription("Decrease the zoom interval used by the zoom in and zoom out functions");
        decreaseZoomIntervalMenuItem = ringWindowMenu.add(ringWindowMenuItem);
        decreaseZoomIntervalMenuItem.setActionCommand("decrZoomInterval");
        decreaseZoomIntervalMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_COMMA, Event.CTRL_MASK + Event.SHIFT_MASK));
        decreaseZoomIntervalMenuItem.addActionListener(this);
        if (this.zoomInterval == MINIMUM_ZOOM_INTERVAL) {
            decreaseZoomIntervalMenuItem.setEnabled(false);
        }
        ringWindowMenuItem = new JMenuItem("Increase zoom interval");
        ringWindowMenuItem.getAccessibleContext().setAccessibleDescription("Increase the zoom interval used by the zoom in and zoom out functions");
        increaseZoomIntervalMenuItem = ringWindowMenu.add(ringWindowMenuItem);
        increaseZoomIntervalMenuItem.setActionCommand("incrZoomInterval");
        increaseZoomIntervalMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_PERIOD, Event.CTRL_MASK + Event.SHIFT_MASK));
        increaseZoomIntervalMenuItem.addActionListener(this);
        if (this.zoomInterval == MAXIMUM_ZOOM_INTERVAL) {
            increaseZoomIntervalMenuItem.setEnabled(false);
        }
        ringWindowMenu.addSeparator();
        ringWindowMenuItem = new JMenuItem("Decrease dot radius");
        ringWindowMenuItem.getAccessibleContext().setAccessibleDescription("Decrease the dot radius used to draw the points on the grids");
        decreaseDotRadiusMenuItem = ringWindowMenu.add(ringWindowMenuItem);
        decreaseDotRadiusMenuItem.setActionCommand("decrDotRadius");
        decreaseDotRadiusMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_COMMA, Event.CTRL_MASK));
        decreaseDotRadiusMenuItem.addActionListener(this);
        if (this.dotRadius == MINIMUM_DOT_RADIUS) {
            decreaseDotRadiusMenuItem.setEnabled(false);
        }
        ringWindowMenuItem = new JMenuItem("Increase dot radius");
        ringWindowMenuItem.getAccessibleContext().setAccessibleDescription("Increase the dot radius used to draw the points on the grids");
        increaseDotRadiusMenuItem = ringWindowMenu.add(ringWindowMenuItem);
        increaseDotRadiusMenuItem.setActionCommand("incrDotRadius");
        increaseDotRadiusMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_PERIOD, Event.CTRL_MASK));
        increaseDotRadiusMenuItem.addActionListener(this);
        if (this.dotRadius == MAXIMUM_DOT_RADIUS) {
            increaseDotRadiusMenuItem.setEnabled(false);
        }
        ringWindowMenu.addSeparator();
        ringWindowMenuItem = new JMenuItem("Reset view defaults");
        ringWindowMenuItem.getAccessibleContext().setAccessibleDescription("Reset defaults for zoom level, zoom interval and dot radius");
        resetViewDefaultsMenuItem = ringWindowMenu.add(ringWindowMenuItem);
        resetViewDefaultsMenuItem.setActionCommand("defaultView");
        resetViewDefaultsMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F7, 0));
        resetViewDefaultsMenuItem.addActionListener(this);
        ringWindowMenu.addSeparator();
        preferThetaNotationMenuItem = new JCheckBoxMenuItem("Use theta notation in readouts", false);
        preferThetaNotationMenuItem.getAccessibleContext().setAccessibleDescription("Toggle whether theta notation is used or not in the integer readout.");
        preferThetaNotationMenuItem.setActionCommand("toggleTheta");
        preferThetaNotationMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, 0));
        preferThetaNotationMenuItem.addActionListener(this);
        ringWindowMenu.add(preferThetaNotationMenuItem);
        toggleReadOutsEnabledMenuItem = new JCheckBoxMenuItem("Update readouts", false);
        toggleReadOutsEnabledMenuItem.getAccessibleContext().setAccessibleDescription("Toggle whether the trace, norm and polynomial readouts are updated.");
        toggleReadOutsEnabledMenuItem.setActionCommand("toggleReadOuts");
        toggleReadOutsEnabledMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0)); // Decided Ctrl-F2 is too uncomfortable, so changed it to just F2.
        toggleReadOutsEnabledMenuItem.addActionListener(this);
        ringWindowMenu.add(toggleReadOutsEnabledMenuItem);
        ringWindowMenu = new JMenu("Help");
        ringWindowMenu.setMnemonic(KeyEvent.VK_H);
        ringWindowMenu.getAccessibleContext().setAccessibleDescription("Menu to provide help and documentation");
        ringWindowMenuBar.add(ringWindowMenu);
        ringWindowMenuItem = new JMenuItem("About...");
        ringWindowMenuItem.getAccessibleContext().setAccessibleDescription("Information about this program");
        aboutMenuItem = ringWindowMenu.add(ringWindowMenuItem);
        aboutMenuItem.setActionCommand("about");
        aboutMenuItem.addActionListener(this);

        ringFrame.setJMenuBar(ringWindowMenuBar);
                
        JPanel readOutsPane = new JPanel();
        algIntReadOut = new JTextField(DEFAULT_READOUT_FIELD_COLUMNS);
        algIntReadOut.setText("0");
        algIntReadOut.setEditable(false);
        readOutsPane.add(algIntReadOut);
        readOutsPane.add(new JLabel("Trace: "));
        algIntTraceReadOut = new JTextField(DEFAULT_READOUT_FIELD_COLUMNS);
        algIntTraceReadOut.setText("0");
        algIntTraceReadOut.setEditable(false);
        readOutsPane.add(algIntTraceReadOut);
        readOutsPane.add(new JLabel("Norm: "));
        algIntNormReadOut = new JTextField(DEFAULT_READOUT_FIELD_COLUMNS);
        algIntNormReadOut.setText("0");
        algIntNormReadOut.setEditable(false);
        readOutsPane.add(algIntNormReadOut);
        readOutsPane.add(new JLabel("Polynomial: "));
        algIntPolReadOut = new JTextField(DEFAULT_READOUT_FIELD_COLUMNS);
        algIntPolReadOut.setText("x");
        algIntPolReadOut.setEditable(false);
        readOutsPane.add(algIntPolReadOut);
        
        ringFrame.add(readOutsPane, BorderLayout.PAGE_END);
        ringFrame.add(this, BorderLayout.CENTER);
        ringFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        ringFrame.pack();
        ringFrame.setVisible(true);

    }
    
    public RingWindowDisplay(int ringChoice) {
        
        ImaginaryQuadraticRing imR;
        
        this.pixelsPerUnitInterval = DEFAULT_PIXELS_PER_UNIT_INTERVAL;
        this.ringCanvasHorizMax = RING_CANVAS_DEFAULT_HORIZ_MAX;
        this.ringCanvasVerticMax = RING_CANVAS_DEFAULT_VERTIC_MAX;
        this.backgroundColor = DEFAULT_CANVAS_BACKGROUND_COLOR;
        this.halfIntegerGridColor = DEFAULT_HALF_INTEGER_GRID_COLOR;
        this.integerGridColor = DEFAULT_INTEGER_GRID_COLOR;
        this.zeroColor = DEFAULT_ZERO_COLOR;
        this.unitColor = DEFAULT_UNIT_COLOR;
        this.inertPrimeColor = DEFAULT_INERT_PRIME_COLOR;
        this.splitPrimeColor = DEFAULT_SPLIT_PRIME_COLOR;
        this.ramifiedPrimeColor = DEFAULT_RAMIFIED_PRIME_COLOR;
        
        this.zeroCoordX = (int) Math.floor(this.ringCanvasHorizMax/2);
        this.zeroCoordY = (int) Math.floor(this.ringCanvasVerticMax/2);
        this.zeroCentered = true;
        this.zeroInView = true;
        
        this.dotRadius = DEFAULT_DOT_RADIUS;
        this.zoomInterval = DEFAULT_ZOOM_INTERVAL;
        this.preferenceForThetaNotation = false;
        
        if (ringChoice > 0) {
            ringChoice *= -1;
        }
        if (NumberTheoreticFunctionsCalculator.isSquareFree(ringChoice)) {
            imR = new ImaginaryQuadraticRing(ringChoice);
        } else {
            imR = new ImaginaryQuadraticRing(DEFAULT_RING_D);
        }
        this.setRing(imR);
        this.mouseIQI = new ImaginaryQuadraticInteger(0, 0, imR, 1);
        this.setBackground(this.backgroundColor);
        this.setPreferredSize(new Dimension(this.ringCanvasHorizMax, this.ringCanvasVerticMax)); 
                               
    }
    
    public static void startRingWindowDisplay(int ringChoice) {
        
        if (ringChoice > -1) {
            ringChoice *= -1;
        }
        if (!NumberTheoreticFunctionsCalculator.isSquareFree(ringChoice)) {
            ringChoice = DEFAULT_RING_D;
        }
        
        RingWindowDisplay rwd = new RingWindowDisplay(ringChoice);
        rwd.setUpRingFrame();
        
    }
    
}