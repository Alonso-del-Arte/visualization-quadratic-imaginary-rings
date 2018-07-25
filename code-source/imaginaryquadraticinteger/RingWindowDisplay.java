/*
 * Copyright (C) 2018 Alonso del Arte
 *
 * This program is free software: you can redistribute it and/or modify it under 
 * the terms of the GNU General Public License as published by the Free Software 
 * Foundation, either version 3 of the License, or (at your option) any later 
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT 
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS 
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more 
 * details.
 *
 * You should have received a copy of the GNU General Public License along with 
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package imaginaryquadraticinteger;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.swing.*;
import javax.imageio.ImageIO;
import javax.swing.filechooser.FileFilter;
import java.util.List;
import java.util.ArrayList;
import filefilters.PNGFileFilter;

/**
 * A Swing component in which to display diagrams of prime numbers in various 
 * quadratic integer rings.
 * @author Alonso del Arte
 */
public final class RingWindowDisplay extends JPanel implements ActionListener, MouseMotionListener {
    
    /**
     * The default number of pixels per unit interval. The protected variable 
     * pixelsPerUnitInterval is initialized to this value.
     */
    public static final int DEFAULT_PIXELS_PER_UNIT_INTERVAL = 40;
    
    /**
     * The minimum pixels per unit interval. Trying to set pixels per unit 
     * interval below this value will cause an exception.
     */
    public static final int MINIMUM_PIXELS_PER_UNIT_INTERVAL = 2;
    
    /**
     * The minimum pixels per unit interval for which the program will draw 
     * grids.
     */
    private static final int MINIMUM_PIXELS_PER_UNIT_INTERVAL_TO_DRAW_GRIDS = 5;
    
    /**
     * The maximum pixels per unit interval. Even on an 8K display, this value 
     * might be much too large. Trying to set pixels per unit interval above 
     * this value will cause an exception.
     */
    public static final int MAXIMUM_PIXELS_PER_UNIT_INTERVAL = 6400;
    
    /**
     * The minimum horizontal pixel dimension for the canvas in which to draw 
     * the diagram. This should be small even on moderately obsolete mobile 
     * devices.
     */
    public static final int RING_CANVAS_HORIZ_MIN = 100;
    
    /**
     * The minimum vertical pixel dimension for the canvas in which to draw the 
     * diagram. This should be small even on moderately obsolete mobile devices.
     */
    public static final int RING_CANVAS_VERTIC_MIN = 178;
    
    /**
     * The default horizontal pixel dimension for the canvas in which to draw 
     * the diagram. This fills up most of the screen on a 1440 by 900 (16:10 
     * aspect ration) display.
     */
    public static final int RING_CANVAS_DEFAULT_HORIZ_MAX = 1280;
    
    /**
     * The default vertical pixel dimension for the canvas in which to draw the 
     * diagram. This fills up most of the screen on a 1440 by 900 (16:10 aspect 
     * ratio) display.
     */
    public static final int RING_CANVAS_DEFAULT_VERTIC_MAX = 720;
    
    /**
     * The number to determine which imaginary quadratic integer ring diagram 
     * will be shown at start-up. This constant corresponds to the ring of 
     * Gaussian integers.
     */
    public static final int DEFAULT_RING_D = -1;
    
    /**
     * The minimum integer the square root of which can be used to generate an 
     * imaginary quadratic integer ring for the purpose of display in this ring 
     * window. Although technically an ImaginaryQuadraticRing can be defined 
     * with the square root of -2147483647 (which is a prime number), this would 
     * quickly lead to arithmetic overflow problems. Hopefully this value of 
     * 8191 (also a prime) is "small" enough not to cause arithmetic overflow 
     * problems with the largest zoom out setting, but "large" enough to be of 
     * no interest to most users of this program.
     */
    public static final int MINIMUM_RING_D = -8191;
    
    /**
     * The default pixel radius for the dots in the diagram.
     */
    public static final int DEFAULT_DOT_RADIUS = 5;
    
    /**
     * The minimum pixel radius for the dots in the diagram.
     */
    public static final int MINIMUM_DOT_RADIUS = 1;
    
    /**
     * The maximum pixel radius for the dots in the diagram.
     */
    public static final int MAXIMUM_DOT_RADIUS = 128;
    
    /**
     * The usual step by which to increment or decrement pixels by unit 
     * interval. This step can be increased or decreased, either through the 
     * menu or with a keyboard shortcut.
     */
    public static final int DEFAULT_ZOOM_INTERVAL = 5;
    
    /**
     * The smallest step by which to increment or decrement pixels by unit 
     * interval. Once this step is reached, the relevant menu item is disabled 
     * and the corresponding keyboard shortcut is ignored.
     */
    public static final int MINIMUM_ZOOM_INTERVAL = 1;
    
    /**
     * The largest step by which to increment or decrement pixels by unit 
     * interval. Once this step is reached, the relevant menu item is disabled 
     * and the corresponding keyboard shortcut is ignored.
     */
    public static final int MAXIMUM_ZOOM_INTERVAL = 48;
    
    /**
     * The default background color when the program starts. In a future 
     * version, there will be a dialog accessed through a menu that will enable 
     * the user to change this color. For now, the only way to change it is by 
     * changing this constant.
     */
    public static final Color DEFAULT_CANVAS_BACKGROUND_COLOR = new Color(2107440); // A dark blue
    
    /**
     * The default color for the "half-integer" grid. In a future version, there 
     * will be a dialog accessed through a menu that will enable the user to 
     * change this color. This setting is only relevant when the program is 
     * displaying a diagram with "half-integers" at a zoom level sufficient to 
     * cause the drawing of grid lines.
     */
    public static final Color DEFAULT_HALF_INTEGER_GRID_COLOR = Color.DARK_GRAY;
    
    /**
     * The default color for the "full" integer grid. In a future version, there 
     * will be a dialog accessed through a menu that will enable the user to 
     * change this color. This setting is only relevant when the program is 
     * displaying a diagram with at a zoom level sufficient to cause the drawing 
     * of grid lines.
     */
    public static final Color DEFAULT_INTEGER_GRID_COLOR = Color.BLACK;
    
    /**
     * The color for 0.
     */
    public static final Color DEFAULT_ZERO_COLOR = Color.BLACK;
    
    /**
     * The color for units.
     * In Z[i], this would include i and -i.
     * In Z[omega], this would include omega, omega + 1, -1 - omega and -omega.
     * In all rings, this includes 1 and -1.
     */
    public static final Color DEFAULT_UNIT_COLOR = Color.WHITE;
    
    /**
     * The color for primes believed to be inert. If a prime does split but its 
     * prime factors are not in the current diagram view, the program is unaware 
     * of them. Though I have yet to think of a single example where it might 
     * actually be the case that the program erroneously identifies a prime as 
     * inert.
     */
    public static final Color DEFAULT_INERT_PRIME_COLOR = Color.CYAN;
    
    /**
     * The primes confirmed split.
     * If a prime's splitting factors are in the current diagram view, the 
     * program uses this color for the split prime.
     */
    public static final Color DEFAULT_SPLIT_PRIME_COLOR = Color.BLUE;
    
    /**
     * The primes that are ramified.
     * Regardless of current diagram view, the program checks if gcd(p, d) > 1.
     */
    public static final Color DEFAULT_RAMIFIED_PRIME_COLOR = Color.GREEN;
    
    /**
     * How wide to make the readouts.
     */
    public static final int DEFAULT_READOUT_FIELD_COLUMNS = 20;
    
    /**
     * The maximum number of previous discriminants the program will remember 
     * for history in any given run.
     */
    public static final int MAXIMUM_HISTORY_ITEMS = 128;
    
    /**
     * The actual pixels per unit interval setting, should be initialized to 
     * DEFAULT_PIXELS_PER_UNIT_INTERVAL in the constructor. Use 
     * setPixelsPerUnitInterval(int pixelLength) to change, making sure 
     * pixelLength is greater than or equal to MINIMUM_PIXELS_PER_UNIT_INTERVAL 
     * but less than or equal to MAXIMUM_PIXELS_PER_UNIT_INTERVAL.
     */
    protected int pixelsPerUnitInterval;
    
    /**
     * The actual pixels per basic imaginary interval setting. This setting 
     * depends on pixelsPerUnitInterval.
     */
    protected int pixelsPerBasicImaginaryInterval;
    
    protected ImaginaryQuadraticRing imagQuadRing;
    
    private ImaginaryQuadraticInteger mouseIQI;
        
    /**
     * When imagQuadRing.d1mod4 is true, some users may prefer to see 
     * "half-integers" notated with theta notation rather than fractions with 2s 
     * for denominators. With this preference turned on and d = -3, omega will 
     * be used rather than theta. Remember that omega = -1/2 + sqrt(-3)/2 and 
     * theta = 1/2 + sqrt(d)/2.
     */
    private boolean preferenceForThetaNotation;
    
    private int ringCanvasHorizMax;
    private int ringCanvasVerticMax;
    
    private int dotRadius;
    private int zoomInterval;
    
    private Color backgroundColor, halfIntegerGridColor, integerGridColor;
    private Color zeroColor, unitColor, inertPrimeColor, splitPrimeColor, ramifiedPrimeColor;
    
    private int zeroCoordX, zeroCoordY;
    private boolean zeroCentered, zeroInView;
    
    private JFrame ringFrame;
    
    private JMenuItem increaseDMenuItem, decreaseDMenuItem;
    private JMenuItem prevDMenuItem, nextDMenuItem;
    private JMenuItem zoomInMenuItem, zoomOutMenuItem;
    private JMenuItem decreaseZoomIntervalMenuItem, increaseZoomIntervalMenuItem;
    private JMenuItem decreaseDotRadiusMenuItem, increaseDotRadiusMenuItem;
    private JCheckBoxMenuItem preferThetaNotationMenuItem, toggleReadOutsEnabledMenuItem;
    
    private JTextField algIntReadOut, algIntTraceReadOut, algIntNormReadOut, algIntPolReadOut;
    
    /**
     * Keeps track of whether or not the user has saved a diagram before. 
     * Applies only during the current session.
     */
    private boolean haveSavedBefore;
    
    /**
     * Points to the directory where the user has previously saved a diagram to 
     * in the current session. Probably empty or null if haveSavedBefore is 
     * false.
     */
    private String prevSavePathname;

    /**
     * The history list, with which to enable to user to view previous diagrams.
     */
    private final List<Integer> discrHistory;
    
    /**
     * Where we are at in the history list.
     */
    private short currHistoryIndex;
    
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
        
        // The even primes, -2 and 2
        currNegPointX -= this.pixelsPerUnitInterval;
        currPointX += this.pixelsPerUnitInterval;
        byte symbol = NumberTheoreticFunctionsCalculator.symbolKronecker(this.imagQuadRing.negRad, 2);
        if (this.imagQuadRing.negRad % 4 == -1) {
            symbol = 0;
        }
        if (this.imagQuadRing.negRad == -3) {
            symbol = -1;
        }
        if (this.imagQuadRing.negRad == -1) {
            symbol = 1;
        }
        switch (symbol) {
            case -1:
                graphicsForPoints.setColor(this.inertPrimeColor);
                graphicsForPoints.fillOval(currPointX - this.dotRadius, currPointY - this.dotRadius, dotDiameter, dotDiameter);
                graphicsForPoints.fillOval(currNegPointX - this.dotRadius, currPointY - this.dotRadius, dotDiameter, dotDiameter);
                break;
            case 0:
                graphicsForPoints.setColor(this.ramifiedPrimeColor);
                graphicsForPoints.drawOval(currPointX - this.dotRadius, currPointY - this.dotRadius, dotDiameter, dotDiameter);
                graphicsForPoints.drawOval(currNegPointX - this.dotRadius, currPointY - this.dotRadius, dotDiameter, dotDiameter);
                break;
            case 1:
                graphicsForPoints.setColor(this.splitPrimeColor);
                graphicsForPoints.drawOval(currPointX - this.dotRadius, currPointY - this.dotRadius, dotDiameter, dotDiameter);
                graphicsForPoints.drawOval(currNegPointX - this.dotRadius, currPointY - this.dotRadius, dotDiameter, dotDiameter);
                break;
            default:
                throw new RuntimeException("Unexpected problem computing symbolKronecker(" + imagQuadRing.negRad + ", " + 2 + ") = " + symbol);
        }
        
        // Place "nibs" back at -1 and 1
        currNegPointX += this.pixelsPerUnitInterval;
        currPointX -= this.pixelsPerUnitInterval;

        // The other purely real integer points
        for (int x = 3; x <= maxX; x += 2) {
            currPointX += (2 * this.pixelsPerUnitInterval);
            currNegPointX -= (2 * this.pixelsPerUnitInterval);
            if (NumberTheoreticFunctionsCalculator.isPrime(x)) {
                symbol = NumberTheoreticFunctionsCalculator.symbolLegendre(imagQuadRing.negRad, x);
                switch (symbol) {
                    case -1:
                        graphicsForPoints.setColor(this.inertPrimeColor);
                        graphicsForPoints.fillOval(currPointX - this.dotRadius, currPointY - this.dotRadius, dotDiameter, dotDiameter);
                        graphicsForPoints.fillOval(currNegPointX - this.dotRadius, currPointY - this.dotRadius, dotDiameter, dotDiameter);
                        break;
                    case 0:
                        graphicsForPoints.setColor(this.ramifiedPrimeColor);
                        graphicsForPoints.drawOval(currPointX - this.dotRadius, currPointY - this.dotRadius, dotDiameter, dotDiameter);
                        graphicsForPoints.drawOval(currNegPointX - this.dotRadius, currPointY - this.dotRadius, dotDiameter, dotDiameter);
                        break;
                    case 1:
                        graphicsForPoints.setColor(this.splitPrimeColor);
                        graphicsForPoints.drawOval(currPointX - this.dotRadius + 1, currPointY - this.dotRadius + 1, dotDiameter, dotDiameter);
                        graphicsForPoints.drawOval(currNegPointX - this.dotRadius + 1, currPointY - this.dotRadius + 1, dotDiameter, dotDiameter);
                        break;
                    default:
                        throw new RuntimeException("Unexpected problem computing symbolLegendre(" + imagQuadRing.negRad + ", " + x + ") = " + symbol);
                }
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
                currIQI = new ImaginaryQuadraticInteger(0, y, this.imagQuadRing);
                if (NumberTheoreticFunctionsCalculator.isPrime(currIQI.norm())) {
                    if (NumberTheoreticFunctionsCalculator.euclideanGCD(currIQI.norm(), this.imagQuadRing.negRad) > 1) {
                        int ramifyPoint = this.zeroCoordX + (int) currIQI.norm() * this.pixelsPerUnitInterval;
                        int negRamifyPoint = this.zeroCoordX - (int) currIQI.norm() * this.pixelsPerUnitInterval;
                        graphicsForPoints.setColor(this.ramifiedPrimeColor);
                        graphicsForPoints.fillOval(ramifyPoint - this.dotRadius, this.zeroCoordY - this.dotRadius, dotDiameter, dotDiameter);
                        graphicsForPoints.fillOval(negRamifyPoint - this.dotRadius, this.zeroCoordY - this.dotRadius, dotDiameter, dotDiameter);
                    } else {
                        graphicsForPoints.setColor(this.inertPrimeColor);
                    }
                    graphicsForPoints.fillOval(currPointX - this.dotRadius, currPointY - this.dotRadius, dotDiameter, dotDiameter);
                    graphicsForPoints.fillOval(currPointX - this.dotRadius, currNegPointY - this.dotRadius, dotDiameter, dotDiameter);
                }
            }
        }
        
        // Now the complex integer points, but not the "half-integers" yet
        long currSplitPrime;
        int currSplitPrimePointX, currNegSplitPrimePointX;
        for (int x = 1; x <= maxX; x++) {
            currPointX = this.zeroCoordX + (x * this.pixelsPerUnitInterval);
            currNegPointX = this.zeroCoordX - (x * this.pixelsPerUnitInterval);
            for (int y = 1; y <= maxY; y++) {
                currPointY = this.zeroCoordY + (y * verticalGridDistance);
                currNegPointY = this.zeroCoordY - (y * verticalGridDistance);
                currIQI = new ImaginaryQuadraticInteger(x, y, this.imagQuadRing, 1);
                if (NumberTheoreticFunctionsCalculator.isPrime(currIQI)) {
                    graphicsForPoints.setColor(this.inertPrimeColor);
                    graphicsForPoints.fillOval(currPointX - this.dotRadius, currPointY - this.dotRadius, dotDiameter, dotDiameter);
                    graphicsForPoints.fillOval(currPointX - this.dotRadius, currNegPointY - this.dotRadius, dotDiameter, dotDiameter);
                    graphicsForPoints.fillOval(currNegPointX - this.dotRadius, currPointY - this.dotRadius, dotDiameter, dotDiameter);
                    graphicsForPoints.fillOval(currNegPointX - this.dotRadius, currNegPointY - this.dotRadius, dotDiameter, dotDiameter);
                    currSplitPrime = currIQI.norm();
                    if (currSplitPrime <= maxX) {
                        currSplitPrimePointX = this.zeroCoordX + ((int) currSplitPrime * this.pixelsPerUnitInterval);
                        currNegSplitPrimePointX = this.zeroCoordX - ((int) currSplitPrime * this.pixelsPerUnitInterval);
                        graphicsForPoints.setColor(this.splitPrimeColor);
                        graphicsForPoints.fillOval(currSplitPrimePointX - this.dotRadius, this.zeroCoordY - this.dotRadius, dotDiameter, dotDiameter);
                        graphicsForPoints.fillOval(currNegSplitPrimePointX - this.dotRadius, this.zeroCoordY - this.dotRadius, dotDiameter, dotDiameter);
                    }
                    if (currSplitPrime <= maxY && this.imagQuadRing.negRad == -1) {
                        currSplitPrimePointX = this.zeroCoordY + ((int) currSplitPrime * this.pixelsPerUnitInterval);
                        currNegSplitPrimePointX = this.zeroCoordY - ((int) currSplitPrime * this.pixelsPerUnitInterval);
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
                    if (NumberTheoreticFunctionsCalculator.isPrime(currIQI)) {
                        graphicsForPoints.setColor(this.inertPrimeColor);
                        graphicsForPoints.fillOval(currPointX - this.dotRadius, currPointY - this.dotRadius, dotDiameter, dotDiameter);
                        graphicsForPoints.fillOval(currPointX - this.dotRadius, currNegPointY - this.dotRadius, dotDiameter, dotDiameter);
                        graphicsForPoints.fillOval(currNegPointX - this.dotRadius, currPointY - this.dotRadius, dotDiameter, dotDiameter);
                        graphicsForPoints.fillOval(currNegPointX - this.dotRadius, currNegPointY - this.dotRadius, dotDiameter, dotDiameter);
                        currSplitPrime = currIQI.norm();
                        if (currSplitPrime <= maxX) {
                            currSplitPrimePointX = this.zeroCoordX + ((int) currSplitPrime * this.pixelsPerUnitInterval);
                            currNegSplitPrimePointX = this.zeroCoordX - ((int) currSplitPrime * this.pixelsPerUnitInterval);
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
     * Change how many pixels there are per unit interval. Also concomitantly 
     * changes how many pixels there are per basic imaginary interval.
     * @param pixelLength An integer greater than or equal to 
     * MINIMUM_PIXELS_PER_UNIT_INTERVAL but less than or equal to 
     * MAXIMUM_PIXELS_PER_UNIT_INTERVAL.
     * @throws IllegalArgumentException If pixelLength is outside the range 
     * specified above.
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
     * Function to change the size of the canvas on which the ring diagrams are 
     * drawn. I have not completely thought this one through, and I certainly 
     * haven't tested it.
     * @param newHorizMax The new width of the ring window. This needs to be at 
     * least equal to RING_CANVAS_HORIZ_MIN.
     * @param newVerticMax The new height of the ring window. This needs to be 
     * at least equal to RING_CANVAS_VERTIC_MIN.
     * @throws IllegalArgumentException If either newHorizMax is less than 
     * RING_CANVAS_HORIZ_MIN or newVerticMax is less than 
     * RING_CANVAS_VERTIC_MIN.
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
     * @param newBackgroundColor Preferably a color that will contrast nicely 
     * with the foreground points but which the grids can blend into.
     */
    public void changeBackgroundColor(Color newBackgroundColor) {
        this.backgroundColor = newBackgroundColor;
    }
    
    /**
     * Function to change the grid colors.
     * @param newHalfIntegerGridColor Applicable only when 
     * this.imagQuadRing.d1mod4 is true. In choosing this color, keep in mind 
     * that, when applicable, the "half-integer" grid is drawn first.
     * @param newIntegerGridColor In choosing this color, keep in mind that, 
     * when applicable, the "full" integer grid is drawn second, after the 
     * "half-integer" grid.
     */
    public void changeGridColors(Color newHalfIntegerGridColor, Color newIntegerGridColor) {
        this.halfIntegerGridColor = newHalfIntegerGridColor;
        this.integerGridColor = newIntegerGridColor;
    }
     /**
     * Function to change the colors of the points.
     * @param newZeroColor The color for the point 0.
     * @param newUnitColor The color for the units. In most imaginary quadratic 
     * rings, this color will only be used for -1 and 1.
     * @param newInertPrimeColor The color for inert primes, or at least primes 
     * having no splitting or ramifying factors in view.
     * @param newSplitPrimeColor The color for confirmed split primes.
     * @param newRamifiedPrimeColor The color for primes that are factors of the 
     * discriminant.
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
     * @param newDotRadius Needs to be at least MINIMUM_DOT_RADIUS pixels.
     * @throws IllegalArgumentException If newDotRadius is less than 
     * MINIMUM_DOT_RADIUS.
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
     * @param newZoomInterval Needs to be at least MINIMUM_ZOOM_INTERVAL pixels. 
     * @throws IllegalArgumentException If newZoomInterval is less than 
     * MINIMUM_ZOOM_INTERVAL.
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
     * Function to change the coordinates of the point 0. I have not yet 
     * implemented a meaningful use for this function.
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
     * Paints the canvas, by delegating to private functions to draw the grids 
     * and the points.
     * @param gr The graphics object supplied by the caller.
     */
    @Override
    public void paintComponent(Graphics gr) {
        super.paintComponent(gr);
        if (this.pixelsPerUnitInterval > MINIMUM_PIXELS_PER_UNIT_INTERVAL_TO_DRAW_GRIDS) {
            drawGrids(gr);
        }
        drawPoints(gr);
    }
    
    /**
     * Function to determine mouse position on the diagram and update readouts 
     * accordingly.
     * @param mauv A MouseEvent object with the relevant information.
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
            algIntTraceReadOut.setText(Long.toString(mouseIQI.trace()));
            algIntNormReadOut.setText(Long.toString(mouseIQI.norm()));
            algIntPolReadOut.setText(mouseIQI.minPolynomialString());
        }
    }
    
    /**
     * No implementation for time being. Hope to add the capability to drag the 
     * diagram in a future version.
     * @param mauv Mouse event to respond to.
     */
    @Override
    public void mouseDragged(MouseEvent mauv) {
        // IMPLEMENTATION PLACEHOLDER
    }
    
    /**
     * Prompts the user for a filename and saves the currently displayed diagram 
     * with that filename as a Portable Network Graphics (PNG) file.
     */
    public void saveDiagramAs() {
        BufferedImage diagram = new BufferedImage(this.ringCanvasHorizMax, this.ringCanvasVerticMax, BufferedImage.TYPE_INT_RGB);
        Graphics2D graph = diagram.createGraphics();
        this.paint(graph);
        String suggestedFilename = this.imagQuadRing.toFilenameString() + "pxui" + this.pixelsPerUnitInterval + ".png";
        File diagramFile = new File(suggestedFilename);
        JFileChooser fileChooser = new JFileChooser();
        FileFilter pngFilter = new PNGFileFilter();
        fileChooser.addChoosableFileFilter(pngFilter);
        if (haveSavedBefore) {
            fileChooser.setCurrentDirectory(new File(prevSavePathname));
        }
        fileChooser.setSelectedFile(diagramFile);
        int fcRet = fileChooser.showSaveDialog(this);
        String notificationString;
        switch (fcRet) {
            case JFileChooser.APPROVE_OPTION:
                diagramFile = fileChooser.getSelectedFile();
                String filePath = diagramFile.getAbsolutePath();
                prevSavePathname = filePath.substring(0, filePath.lastIndexOf(File.separator));
                haveSavedBefore = true;
                try {
                    ImageIO.write(diagram, "PNG", diagramFile);
                } catch (IOException ioe) {
                    notificationString = "Image input/output exception occurred:\n " + ioe.getMessage();
                    JOptionPane.showMessageDialog(ringFrame, notificationString);
                }
                break;
            case JFileChooser.CANCEL_OPTION:
                notificationString = "File save canceled.";
                JOptionPane.showMessageDialog(ringFrame, notificationString);
                break;
            case JFileChooser.ERROR_OPTION:
                notificationString = "An error occurred trying to choose a file to save to.";
                JOptionPane.showMessageDialog(ringFrame, notificationString);
                break;
            default:
                notificationString = "Unexpected option " + fcRet + " from file chooser.";
                JOptionPane.showMessageDialog(ringFrame, notificationString);
        }
    }
    
    private void switchToRing(int d) {
        ImaginaryQuadraticRing imagRing = new ImaginaryQuadraticRing(d);
        ringFrame.setTitle("Ring Diagram for " + imagRing.toString());
        setRing(imagRing);
        repaint();
    }
    
    /**
     * Function to update the history of previously viewed diagrams.
     * @param d The discriminant to add to the history list.
     */
    private void updateDiscriminantHistory(int d) {
        if (currHistoryIndex == discrHistory.size() - 1) {
            discrHistory.add(d);
            currHistoryIndex++;
            if (!prevDMenuItem.isEnabled()) {
                prevDMenuItem.setEnabled(true);
            }
        } else {
            currHistoryIndex++;
            discrHistory.add(currHistoryIndex, d);
            while (currHistoryIndex < discrHistory.size() - 1) {
                discrHistory.remove(currHistoryIndex + 1); // Remove the "forward arrow" history
            }
            nextDMenuItem.setEnabled(false);
        }
        if (currHistoryIndex > MAXIMUM_HISTORY_ITEMS) {
            discrHistory.remove(0); // Remove the first item
        }
    }
    
    /**
     * Function to ask user to enter a new, negative, squarefree integer for the 
     * discriminant of a ring to diagram primes in. If the user enters a 
     * positive integer, the number will be multiplied by -1. And if that number 
     * is not squarefree, the function looks for the next lower squarefree 
     * number, taking care not to go below MINIMUM_RING_D.
     */
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
            updateDiscriminantHistory(discr);
        }

    }

    /**
     * Function to choose for discriminant the next higher negative squarefree 
     * integer. If this brings us up to -1, then the "Increase discriminant" 
     * menu item is disabled.
     */
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
        updateDiscriminantHistory(discr);
    }

    /**
     * Function to choose for discriminant the next lower negative squarefree 
     * integer. If this brings us down to MINIMUM_RING_D, then the "Decrease 
     * discriminant" menu item is disabled.
     */
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
        updateDiscriminantHistory(discr);
    }
    
    /**
     * Bring up the previously displayed diagram.
     */
    public void previousDiscriminant() {
        currHistoryIndex--;
        switchToRing(discrHistory.get(currHistoryIndex));
        if (currHistoryIndex == 0) {
            prevDMenuItem.setEnabled(false);
        }
        if (!nextDMenuItem.isEnabled()) {
            nextDMenuItem.setEnabled(true);
        }
    }

    /**
     * Bring up the next displayed diagram.
     */
    public void nextDiscriminant() {
        currHistoryIndex++;
        switchToRing(discrHistory.get(currHistoryIndex));
        if (currHistoryIndex == discrHistory.size() - 1) {
            nextDMenuItem.setEnabled(false);
        }
        if (!prevDMenuItem.isEnabled()) {
            prevDMenuItem.setEnabled(true);
        }
    }
    
    /**
     * Copies the readouts of the algebraic integer, trace, norm and polynomial 
     * to the clipboard.
     */
    public void copyReadoutsToClipboard() {
        String agregReadouts = mouseIQI.toString();
        if (this.imagQuadRing.d1mod4) {
            agregReadouts = agregReadouts + " = " + mouseIQI.toStringAlt();
        }
        agregReadouts = agregReadouts + ", Trace: " + mouseIQI.trace() + ", Norm: " + mouseIQI.norm() + ", Polynomial: " + mouseIQI.minPolynomialString();
        StringSelection ss = new StringSelection(agregReadouts);
        this.getToolkit().getSystemClipboard().setContents(ss, ss);
    }
    
    /**
     * Copies the currently displayed diagram to the clipboard.
     * FEATURE NOT YET IMPLEMENTED*****************************
     */
    public void copyDiagramToClipboard() {
        System.out.println("FEATURE NOT YET IMPLEMENTED");
    }
    
    /**
     * Checks whether the Zoom in and Zoom out menu items are enabled or not, 
     * and whether they should be, enabling them or disabling them as needed.
     */
    private void checkViewMenuEnablements() {
        if (this.zoomInMenuItem.isEnabled() && (this.pixelsPerUnitInterval > (MAXIMUM_PIXELS_PER_UNIT_INTERVAL - zoomInterval))) {
            this.zoomInMenuItem.setEnabled(false);
        }
        if (!this.zoomInMenuItem.isEnabled() && (this.pixelsPerUnitInterval <= (MAXIMUM_PIXELS_PER_UNIT_INTERVAL - zoomInterval))) {
            this.zoomInMenuItem.setEnabled(true);
        }
        if (this.zoomOutMenuItem.isEnabled() && (this.pixelsPerUnitInterval < (MINIMUM_PIXELS_PER_UNIT_INTERVAL + zoomInterval))) {
            this.zoomOutMenuItem.setEnabled(false);
        }
        if (!this.zoomOutMenuItem.isEnabled() && (this.pixelsPerUnitInterval >= (MINIMUM_PIXELS_PER_UNIT_INTERVAL + zoomInterval))) {
            this.zoomOutMenuItem.setEnabled(true);
        }
    }
    
    /**
     * Zooms in on the diagram. This is done by reducing pixelsPerUnitInterval 
     * by zoomInterval and calling repaint().
     */
    public void zoomIn() {
        int newPixelsPerUnitInterval = this.pixelsPerUnitInterval + this.zoomInterval;
        if (newPixelsPerUnitInterval <= MAXIMUM_PIXELS_PER_UNIT_INTERVAL) {
            setPixelsPerUnitInterval(newPixelsPerUnitInterval);
            repaint();
            if ((newPixelsPerUnitInterval + this.zoomInterval) > MAXIMUM_PIXELS_PER_UNIT_INTERVAL) {
                this.zoomInMenuItem.setEnabled(false);
            }
        }
        if (!this.zoomOutMenuItem.isEnabled() && (newPixelsPerUnitInterval >= (MINIMUM_PIXELS_PER_UNIT_INTERVAL + this.zoomInterval))) {
            this.zoomOutMenuItem.setEnabled(true);
        }
    }
    
    /**
     * Zooms out on the diagram. This is done by increasing 
     * pixelsPerUnitInterval by zoomInterval and calling repaint().
     */
    public void zoomOut() {
        int newPixelsPerUnitInterval = this.pixelsPerUnitInterval - this.zoomInterval;
        if (newPixelsPerUnitInterval >= MINIMUM_PIXELS_PER_UNIT_INTERVAL) {
            setPixelsPerUnitInterval(newPixelsPerUnitInterval);
            repaint();
            if ((newPixelsPerUnitInterval - zoomInterval) < MINIMUM_PIXELS_PER_UNIT_INTERVAL) {
                this.zoomOutMenuItem.setEnabled(false);
            }
        }
        if (!this.zoomInMenuItem.isEnabled() && (newPixelsPerUnitInterval <= (MAXIMUM_PIXELS_PER_UNIT_INTERVAL - this.zoomInterval))) {
            this.zoomInMenuItem.setEnabled(true);
        }
    }
    
    /**
     * Just a little message dialog to let the user know what the zoom interval 
     * is now.
     */
    private void informZoomIntervalChange() {
        String notificationString = "Zoom interval is now " + this.zoomInterval + ".\nThere are " + this.pixelsPerUnitInterval + " pixels per unit interval.";
        JOptionPane.showMessageDialog(ringFrame, notificationString);
    }
    
    /**
     * Decreases the zoom interval. The zoom interval is decremented by 1, 
     * taking care that it does not become less than {@link 
     * #MINIMUM_ZOOM_INTERVAL MINIMUM_ZOOM_INTERVAL}.
     */
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
        if (newZoomIntervalFlag) {
            informZoomIntervalChange();
        }
        if (!this.increaseZoomIntervalMenuItem.isEnabled() && (newZoomInterval < MAXIMUM_ZOOM_INTERVAL)) {
            this.increaseZoomIntervalMenuItem.setEnabled(true);
        }
        checkViewMenuEnablements();
    }

    /**
     * Increases the zoom interval. The zoom interval is incremented by 1, 
     * taking care that it does not become more than {@link 
     * #MAXIMUM_ZOOM_INTERVAL MAXIMUM_ZOOM_INTERVAL}.
     */
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
        if (newZoomIntervalFlag) {
            informZoomIntervalChange();
        }
        if (!this.decreaseZoomIntervalMenuItem.isEnabled() && (newZoomInterval > MINIMUM_ZOOM_INTERVAL)) {
            this.decreaseZoomIntervalMenuItem.setEnabled(true);
        }
        checkViewMenuEnablements();
    }

    /**
     * Decreases the dot radius for 0, units and primes on the diagram. The dot 
     * radius is decreased by 1 pixel, taking care that it does not become less 
     * than {@link #MINIMUM_DOT_RADIUS MINIMUM_DOT_RADIUS}.
     */
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

    /**
     * Increases the dot radius for 0, units and primes on the diagram. The dot 
     * radius is increased by 1 pixel, taking care that it does not become more 
     * than {@link #MAXIMUM_DOT_RADIUS MAXIMUM_DOT_RADIUS}.
     */
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
    
    /**
     * Resets pixels per unit interval, dot radius and zoom interval. This does 
     * not change the discriminant, nor whether or not readouts are updated, nor 
     * the preference for theta notation.
     */
    public void resetViewDefaults() {
        /* Since the program does not yet allow the user to change colors, the 
           following three lines are for now unnecessary.
        changeBackgroundColor(DEFAULT_CANVAS_BACKGROUND_COLOR);
        changeGridColors(DEFAULT_HALF_INTEGER_GRID_COLOR, DEFAULT_INTEGER_GRID_COLOR);
        changePointColors(DEFAULT_ZERO_COLOR, DEFAULT_UNIT_COLOR, DEFAULT_INERT_PRIME_COLOR, DEFAULT_SPLIT_PRIME_COLOR, DEFAULT_RAMIFIED_PRIME_COLOR);
        */
        setPixelsPerUnitInterval(DEFAULT_PIXELS_PER_UNIT_INTERVAL);
        changeZoomInterval(DEFAULT_ZOOM_INTERVAL);
        changeDotRadius(DEFAULT_DOT_RADIUS);
        repaint();
        // Now to check if any menu items need to be re-enabled
        checkViewMenuEnablements(); // This takes care of the Zoom in and Zoom out menu items
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
     * Enable or disable the use of theta notation in the readout field for 
     * integer when the discriminant is congruent to 1 modulo 4. Of course the
     * updating of readouts has to be enabled for this to be any of consequence.
     */
    public void toggleThetaNotation() {
        this.preferenceForThetaNotation = this.preferThetaNotationMenuItem.isSelected();
    }
    
    /**
     * Enable or disable updating of the readout fields for integer, trace, norm 
     * and polynomial.
     */
    public void toggleReadOutsEnabled() {
        if (this.toggleReadOutsEnabledMenuItem.isSelected()) {
            this.addMouseMotionListener(this);
        } else {
            this.removeMouseMotionListener(this);
        }
    }
    
    /**
     * Show the About box, a simple MessageDialog from JOptionPage.
     */
    public void showAboutBox() {
        JOptionPane.showMessageDialog(ringFrame, "Imaginary Quadratic Integer Ring Viewer\nVersion 0.941\n\u00A9 2018 Alonso del Arte");
    }
    
    /**
     * Function to handle menu events. Unrecognized commands will be printed to 
     * the console with a message to that effect.
     * @param ae Object giving information about the menu item selected.
     */
    @Override
    public void actionPerformed(ActionEvent ae) {
        String cmd = ae.getActionCommand();
        switch (cmd) {
            case "saveDiagramAs":
                saveDiagramAs();
                break;
            case "quit":
                System.exit(0);
                break;
            case "chooseD":
                chooseDiscriminant();
                break;
            case "incrD":
                incrementDiscriminant();
                break;
            case "decrD":
                decrementDiscriminant();
                break;
            case "prevD":
                previousDiscriminant();
                break;
            case "nextD":
                nextDiscriminant();
                break;
            case "copyReadouts":
                copyReadoutsToClipboard();
                break;
            case "copyDiagram":
                copyDiagramToClipboard();
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
                System.out.println("Command " + cmd + " not recognized.");
        }
    }

    /**
     * Sets the imaginary quadratic ring for which to draw a window of.
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
     * Sets up the JFrame in which the various ring diagrams will be drawn.
     */
    private void setUpRingFrame() {
        
        ringFrame = new JFrame("Ring Diagram for " + this.imagQuadRing.toString());
        haveSavedBefore = false;
        JMenuBar ringWindowMenuBar;
        JMenu ringWindowMenu;
        JMenuItem ringWindowMenuItem, saveFileMenuItem, quitMenuItem;
        JMenuItem chooseDMenuItem, copyReadOutsToClipboardMenuItem, copyDiagramToClipboardMenuItem, resetViewDefaultsMenuItem, aboutMenuItem;

        boolean macOSFlag;
        int maskCtrlCommand;
        String operSysName = System.getProperty("os.name");
        macOSFlag = operSysName.equals("Mac OS X");
        if (macOSFlag) {
            maskCtrlCommand = Event.META_MASK;
            // maskCtrlAltCommandOption = Event.META_MASK + Event.ALT_MASK;
        } else {
            maskCtrlCommand = Event.CTRL_MASK;
            // maskCtrlAltCommandOption = Event.CTRL_MASK + Event.ALT_MASK;
        }
        
        ringWindowMenuBar = new JMenuBar();
        ringWindowMenu = new JMenu("File");
        ringWindowMenu.setMnemonic(KeyEvent.VK_F);
        ringWindowMenu.getAccessibleContext().setAccessibleDescription("Menu for file operations");
        ringWindowMenuBar.add(ringWindowMenu);
        ringWindowMenuItem = new JMenuItem("Save diagram as...");
        ringWindowMenuItem.getAccessibleContext().setAccessibleDescription("Save currently displayed diagram to a PNG file");
        saveFileMenuItem = ringWindowMenu.add(ringWindowMenuItem);
        saveFileMenuItem.setActionCommand("saveDiagramAs");
        saveFileMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, maskCtrlCommand + Event.SHIFT_MASK));
        saveFileMenuItem.addActionListener(this);
        ringWindowMenu.addSeparator();
        ringWindowMenuItem = new JMenuItem("Quit");
        ringWindowMenuItem.getAccessibleContext().setAccessibleDescription("Exit the program");
        quitMenuItem = ringWindowMenu.add(ringWindowMenuItem);
        quitMenuItem.setActionCommand("quit");
        quitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, maskCtrlCommand));
        quitMenuItem.addActionListener(this);
        ringWindowMenu = new JMenu("Edit");
        ringWindowMenu.setMnemonic(KeyEvent.VK_E);
        ringWindowMenu.getAccessibleContext().setAccessibleDescription("Menu to change certain parameters");
        ringWindowMenuBar.add(ringWindowMenu);
        ringWindowMenuItem = new JMenuItem("Choose discriminant...");
        ringWindowMenuItem.getAccessibleContext().setAccessibleDescription("Let user enter new choice for ring discriminant");
        chooseDMenuItem = ringWindowMenu.add(ringWindowMenuItem);
        chooseDMenuItem.setActionCommand("chooseD");
        chooseDMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, maskCtrlCommand));
        chooseDMenuItem.addActionListener(this);
        ringWindowMenu.addSeparator();
        ringWindowMenuItem = new JMenuItem("Increment discriminant");
        ringWindowMenuItem.getAccessibleContext().setAccessibleDescription("Increment the discriminant to choose another ring");
        increaseDMenuItem = ringWindowMenu.add(ringWindowMenuItem);
        increaseDMenuItem.setActionCommand("incrD");
        if (macOSFlag) {
            increaseDMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, maskCtrlCommand));
        } else {
            increaseDMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_UP, maskCtrlCommand));
        }
        increaseDMenuItem.addActionListener(this);
        if (this.imagQuadRing.negRad == -1) {
            increaseDMenuItem.setEnabled(false);
        }
        ringWindowMenuItem = new JMenuItem("Decrement discriminant");
        ringWindowMenuItem.getAccessibleContext().setAccessibleDescription("Decrement the discriminant to choose another ring");
        decreaseDMenuItem = ringWindowMenu.add(ringWindowMenuItem);
        decreaseDMenuItem.setActionCommand("decrD");
        if (macOSFlag) {
            decreaseDMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, maskCtrlCommand));
        } else {
            decreaseDMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, maskCtrlCommand));
        }
        decreaseDMenuItem.addActionListener(this);
        if (this.imagQuadRing.negRad == Integer.MIN_VALUE + 1) {
            decreaseDMenuItem.setEnabled(false);
        }
        ringWindowMenu.addSeparator();
        ringWindowMenuItem = new JMenuItem("Copy readouts to clipboard");
        ringWindowMenuItem.getAccessibleContext().setAccessibleDescription("Copy the readouts (integer, trace, norm, polynomial) to the clipboard");
        copyReadOutsToClipboardMenuItem = ringWindowMenu.add(ringWindowMenuItem);
        copyReadOutsToClipboardMenuItem.setActionCommand("copyReadouts");
        copyReadOutsToClipboardMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, maskCtrlCommand + Event.SHIFT_MASK));
        copyReadOutsToClipboardMenuItem.addActionListener(this);
        ringWindowMenuItem = new JMenuItem("Copy diagram to clipboard");
        ringWindowMenuItem.getAccessibleContext().setAccessibleDescription("Copy the currently displayed diagram to the clipboard so that it's accessible to other applications");
        copyDiagramToClipboardMenuItem = ringWindowMenu.add(ringWindowMenuItem);
        copyDiagramToClipboardMenuItem.setActionCommand("copyDiagram");
        copyDiagramToClipboardMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, maskCtrlCommand + Event.ALT_MASK));
        copyDiagramToClipboardMenuItem.setEnabled(false); // Once implmented, uncomment next line and delete this line 
        // copyDiagramToClipboardMenuItem.addActionListener(this);
        
    /*    ringWindowMenu.addSeparator();
        THIS IS FOR WHEN I GET AROUND TO ADDING THE CAPABILITY TO CHANGE GRID, POINT COLORS
        ringWindowMenuItem = new JMenuItem("Preferences...");
        ringWindowMenuItem.getAccessibleContext().setAccessibleDescription("Bring up a dialogue to adjust preferences");
        ringWindowMenu.add(ringWindowMenuItem); 
    */
    
        ringWindowMenu = new JMenu("View");
        ringWindowMenu.setMnemonic(KeyEvent.VK_V);
        ringWindowMenu.getAccessibleContext().setAccessibleDescription("Menu to zoom in or zoom out");
        ringWindowMenuBar.add(ringWindowMenu);
        ringWindowMenuItem = new JMenuItem("Previous discriminant");
        ringWindowMenuItem.getAccessibleContext().setAccessibleDescription("View the diagram for the previous discriminant");
        prevDMenuItem = ringWindowMenu.add(ringWindowMenuItem);
        prevDMenuItem.setActionCommand("prevD");
        if (macOSFlag) {
            prevDMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, maskCtrlCommand));
        } else {
            prevDMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, maskCtrlCommand));
        }
        prevDMenuItem.addActionListener(this);
        prevDMenuItem.setEnabled(false);
        ringWindowMenuItem = new JMenuItem("Next discriminant");
        ringWindowMenuItem.getAccessibleContext().setAccessibleDescription("View the diagram for the next discriminant");
        nextDMenuItem = ringWindowMenu.add(ringWindowMenuItem);
        nextDMenuItem.setActionCommand("nextD");
        if (macOSFlag) {
            nextDMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, maskCtrlCommand));
        } else {
            nextDMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, maskCtrlCommand));
        }
        nextDMenuItem.addActionListener(this);
        nextDMenuItem.setEnabled(false);
        ringWindowMenu.addSeparator();
        ringWindowMenuItem = new JMenuItem("Zoom in");
        ringWindowMenuItem.getAccessibleContext().setAccessibleDescription("Zoom in, by increasing pixels per unit interval");
        zoomInMenuItem = ringWindowMenu.add(ringWindowMenuItem);
        zoomInMenuItem.setActionCommand("zoomIn");
        if (macOSFlag) {
            zoomInMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, Event.SHIFT_MASK));
        } else {
            zoomInMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ADD, Event.CTRL_MASK));
        }
        zoomInMenuItem.addActionListener(this);
        if (this.pixelsPerUnitInterval > (MAXIMUM_PIXELS_PER_UNIT_INTERVAL - zoomInterval)) {
            zoomInMenuItem.setEnabled(false);
        }
        ringWindowMenuItem = new JMenuItem("Zoom out");
        ringWindowMenuItem.getAccessibleContext().setAccessibleDescription("Zoom out, by decreasing pixels per unit interval");
        zoomOutMenuItem = ringWindowMenu.add(ringWindowMenuItem);
        zoomOutMenuItem.setActionCommand("zoomOut");
        if (macOSFlag) {
            zoomOutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, 0));
        } else {
            zoomOutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_SUBTRACT, Event.CTRL_MASK));
        }
        zoomOutMenuItem.addActionListener(this);
        if (this.pixelsPerUnitInterval < (MINIMUM_PIXELS_PER_UNIT_INTERVAL + zoomInterval)) {
            zoomInMenuItem.setEnabled(false);
        }
        ringWindowMenu.addSeparator();
        ringWindowMenuItem = new JMenuItem("Decrease zoom interval");
        ringWindowMenuItem.getAccessibleContext().setAccessibleDescription("Decrease the zoom interval used by the zoom in and zoom out functions");
        decreaseZoomIntervalMenuItem = ringWindowMenu.add(ringWindowMenuItem);
        decreaseZoomIntervalMenuItem.setActionCommand("decrZoomInterval");
        decreaseZoomIntervalMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_COMMA, maskCtrlCommand + Event.SHIFT_MASK));
        decreaseZoomIntervalMenuItem.addActionListener(this);
        if (this.zoomInterval == MINIMUM_ZOOM_INTERVAL) {
            decreaseZoomIntervalMenuItem.setEnabled(false);
        }
        ringWindowMenuItem = new JMenuItem("Increase zoom interval");
        ringWindowMenuItem.getAccessibleContext().setAccessibleDescription("Increase the zoom interval used by the zoom in and zoom out functions");
        increaseZoomIntervalMenuItem = ringWindowMenu.add(ringWindowMenuItem);
        increaseZoomIntervalMenuItem.setActionCommand("incrZoomInterval");
        increaseZoomIntervalMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_PERIOD, maskCtrlCommand + Event.SHIFT_MASK));
        increaseZoomIntervalMenuItem.addActionListener(this);
        if (this.zoomInterval == MAXIMUM_ZOOM_INTERVAL) {
            increaseZoomIntervalMenuItem.setEnabled(false);
        }
        ringWindowMenu.addSeparator();
        ringWindowMenuItem = new JMenuItem("Decrease dot radius");
        ringWindowMenuItem.getAccessibleContext().setAccessibleDescription("Decrease the dot radius used to draw the points on the grids");
        decreaseDotRadiusMenuItem = ringWindowMenu.add(ringWindowMenuItem);
        decreaseDotRadiusMenuItem.setActionCommand("decrDotRadius");
        decreaseDotRadiusMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_COMMA, maskCtrlCommand));
        decreaseDotRadiusMenuItem.addActionListener(this);
        if (this.dotRadius == MINIMUM_DOT_RADIUS) {
            decreaseDotRadiusMenuItem.setEnabled(false);
        }
        ringWindowMenuItem = new JMenuItem("Increase dot radius");
        ringWindowMenuItem.getAccessibleContext().setAccessibleDescription("Increase the dot radius used to draw the points on the grids");
        increaseDotRadiusMenuItem = ringWindowMenu.add(ringWindowMenuItem);
        increaseDotRadiusMenuItem.setActionCommand("incrDotRadius");
        increaseDotRadiusMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_PERIOD, maskCtrlCommand));
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
        if (macOSFlag) {
            toggleReadOutsEnabledMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, 0)); // On the Mac, fn-F2 is too much of a hassle, in my opinion.
        } else {
            toggleReadOutsEnabledMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0)); // Decided Ctrl-F2 is too uncomfortable, so changed it to just F2.
        }
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
    
    /**
     * Constructor. Most of the work is setting the various instance fields to 
     * their default values. paintComponent() is not explicitly called.
     * @param ringChoice The negative squarefree number to determine which 
     * diagram will be drawn first. If positive, it will quietly be changed to 
     * its additive inverse (e.g., 163 gets turned to -163), but if it's not 
     * squarefree,
     */
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
        this.discrHistory = new ArrayList<>();
        
        if (ringChoice > 0) {
            ringChoice *= -1;
        }
        if (NumberTheoreticFunctionsCalculator.isSquareFree(ringChoice)) {
            imR = new ImaginaryQuadraticRing(ringChoice);
            discrHistory.add(ringChoice);
        } else {
            imR = new ImaginaryQuadraticRing(DEFAULT_RING_D);
            discrHistory.add(DEFAULT_RING_D);
        }
        this.currHistoryIndex = 0;
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