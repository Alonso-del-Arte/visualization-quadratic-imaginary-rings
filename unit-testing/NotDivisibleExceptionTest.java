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

import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Alonso del Arte
 */
public class NotDivisibleExceptionTest {
    
    private static final ImaginaryQuadraticRing RING_GAUSSIAN = new ImaginaryQuadraticRing(-1);
    private static final ImaginaryQuadraticRing RING_EISENSTEIN = new ImaginaryQuadraticRing(-3);
    
    private static NotDivisibleException notDivGaussian;
    private static NotDivisibleException notDivEisenstein;
    
    @BeforeClass
    public static void setUpClass() {
        notDivGaussian = new NotDivisibleException("Initialization state, not the result of an actually thrown exception.", 0, 0, 4, -1);
        notDivEisenstein = new NotDivisibleException("Initialization state, not the result of an actually thrown exception.", 0, 0, 4, -3);
        ImaginaryQuadraticInteger dividend = new ImaginaryQuadraticInteger(5, 1, RING_GAUSSIAN);
        ImaginaryQuadraticInteger divisor = new ImaginaryQuadraticInteger(3, 1, RING_GAUSSIAN);
        ImaginaryQuadraticInteger division;
        try {
            division = dividend.divides(divisor);
            System.out.println(dividend.toASCIIString() + " divided by " + divisor.toASCIIString() + " is " + division.toASCIIString());
        } catch (AlgebraicDegreeOverflowException adoe) {
            System.err.println("AlgebraicDegreeOverflowException should not have occurred in this context.");
            // This would merit a fail if occurred in a test.
        } catch (NotDivisibleException nde) {
            notDivGaussian = nde;
        }
        dividend = new ImaginaryQuadraticInteger(61, 0, RING_EISENSTEIN);
        divisor = new ImaginaryQuadraticInteger(1, 9, RING_EISENSTEIN);
        try {
            division = dividend.divides(divisor);
            System.out.println(dividend.toASCIIString() + " divided by " + divisor.toASCIIString() + " is " + division.toASCIIString());
        } catch (AlgebraicDegreeOverflowException adoe) {
            System.err.println("AlgebraicDegreeOverflowException should not have occurred in this context.");
            // This would merit a fail if occurred in a test.
        } catch (NotDivisibleException nde) {
            notDivEisenstein = nde;
        }
        System.out.println("NotDivisibleException for the Gaussian integers example has this message: \"" + notDivGaussian.getMessage() + "\"");
        System.out.println("NotDivisibleException for the Eisenstein integers example has this message: \"" + notDivEisenstein.getMessage() + "\"");
    }
    
    /**
     * Test of getResReFractNumer method, of class NotDivisibleException. I'm 
     * not too concerned with these getters, so the tests for these are almost 
     * perfunctory.
     */
    @Test
    public void testGetResReFractNumer() {
        System.out.println("getResReFractNumer");
        assertEquals(8, notDivGaussian.getResReFractNumer());
        assertEquals(1, notDivEisenstein.getResReFractNumer());
    }

    /**
     * Test of getResImFractNumer method, of class NotDivisibleException.
     */
    @Test
    public void testGetResImFractNumer() {
        System.out.println("getResImFractNumer");
        assertEquals(-1, notDivGaussian.getResImFractNumer());
        assertEquals(-9, notDivEisenstein.getResImFractNumer());
    }

    /**
     * Test of getResFractDenom method, of class NotDivisibleException.
     */
    @Test
    public void testGetResFractDenom() {
        System.out.println("getResFractDenom");
        assertEquals(5, notDivGaussian.getResFractDenom());
        assertEquals(4, notDivEisenstein.getResFractDenom());
    }

    /**
     * Test of getResFractNegRad method, of class NotDivisibleException.
     */
    @Test
    public void testGetResFractNegRad() {
        System.out.println("getResFractNegRad");
        assertEquals(-1, notDivGaussian.getResFractNegRad());
        assertEquals(-3, notDivEisenstein.getResFractNegRad());
    }

    /**
     * Test of roundTowardsZero method, of class NotDivisibleException. Here we 
     * get down to the nitty-gritty, what is perhaps the most important function 
     * of this exception, the one that makes the Euclidean GCD algorithm 
     * possible.
     */
    @Test
    public void testRoundTowardsZero() {
        System.out.println("roundTowardsZero");
        ImaginaryQuadraticInteger expResult = new ImaginaryQuadraticInteger(1, -1, RING_GAUSSIAN);
        ImaginaryQuadraticInteger result = notDivGaussian.roundTowardsZero();
        assertEquals(expResult, result);
        expResult = new ImaginaryQuadraticInteger(0, -2, RING_EISENSTEIN);
        result = notDivEisenstein.roundTowardsZero();
        assertEquals(expResult, result);
        ImaginaryQuadraticRing currRing;
        ImaginaryQuadraticInteger dividend, divisor, division;
        /* For this next test, must skip over iterDiscr = -3, because 1/2 + 
           sqrt(-3) is a unit, as is its conjugate. */
        for (int iterDiscr = -5; iterDiscr > -200; iterDiscr--) {
            if (NumberTheoreticFunctionsCalculator.isSquareFree(iterDiscr)) {
                currRing = new ImaginaryQuadraticRing(iterDiscr);
                if (currRing.hasHalfIntegers()) {
                    dividend = new ImaginaryQuadraticInteger(1, 0, currRing);
                    divisor = new ImaginaryQuadraticInteger(1, 1, currRing, 2);
                } else {
                    dividend = new ImaginaryQuadraticInteger(2, 1, currRing);
                    divisor = new ImaginaryQuadraticInteger(1, 1, currRing);
                }
                try {
                    division = dividend.divides(divisor);
                    fail("Dividing " + dividend + " by " + divisor + " should not have given " + division);
                } catch (AlgebraicDegreeOverflowException adoe) {
                    fail("AlgebraicDegreeOverflowException should not have happened. " + adoe.getMessage());
                } catch (NotDivisibleException nde) {
                    expResult = divisor.conjugate();
                    result = nde.roundTowardsZero();
                    System.out.println(dividend.toASCIIString() + " divided by " + divisor.toASCIIString() + " rounds to " + result.toASCIIString());
                    assertEquals(expResult, result);
                }
            }
        }
    }
    
}
