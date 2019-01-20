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
package imaginaryquadraticinteger.Exceptions;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import imaginaryquadraticinteger.ImaginaryQuadraticInteger;
import imaginaryquadraticinteger.ImaginaryQuadraticRing;
import imaginaryquadraticinteger.ImaginaryQuadraticRingTest;
import imaginaryquadraticinteger.NumberTheoreticFunctionsCalculator;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests for the NotDivisibleException class. The purpose of this test class is 
 * only to make sure the exception object works as it should. Testing whether 
 * this exception is thrown for the right reasons or not is the responsibility 
 * of other test classes.
 * @author Alonso del Arte
 */
public class NotDivisibleExceptionTest {
    
    private static NotDivisibleException notDivGaussian;
    private static NotDivisibleException notDivEisenstein;
    
    @BeforeClass
    public static void setUpClass() {
        notDivGaussian = new NotDivisibleException("Initialization state, not the result of an actually thrown exception.", 0, 0, 4, -1);
        notDivEisenstein = new NotDivisibleException("Initialization state, not the result of an actually thrown exception.", 0, 0, 4, -3);
        ImaginaryQuadraticInteger dividend = new ImaginaryQuadraticInteger(5, 1, NumberTheoreticFunctionsCalculator.RING_GAUSSIAN);
        ImaginaryQuadraticInteger divisor = new ImaginaryQuadraticInteger(3, 1, NumberTheoreticFunctionsCalculator.RING_GAUSSIAN);
        ImaginaryQuadraticInteger division;
        try {
            division = dividend.divides(divisor);
            System.out.println(dividend.toASCIIString() + " divided by " + divisor.toASCIIString() + " is " + division.toASCIIString());
        } catch (AlgebraicDegreeOverflowException adoe) {
            System.err.println("AlgebraicDegreeOverflowException should not have occurred in this context: " + adoe.getMessage());
            // This would merit a fail if occurred in a test.
        } catch (NotDivisibleException nde) {
            notDivGaussian = nde;
        }
        System.out.println("NotDivisibleException for the Gaussian integers example has this message: \"" + notDivGaussian.getMessage() + "\"");
        dividend = new ImaginaryQuadraticInteger(61, 0, NumberTheoreticFunctionsCalculator.RING_EISENSTEIN);
        divisor = new ImaginaryQuadraticInteger(1, 9, NumberTheoreticFunctionsCalculator.RING_EISENSTEIN);
        try {
            division = dividend.divides(divisor);
            System.out.println(dividend.toASCIIString() + " divided by " + divisor.toASCIIString() + " is " + division.toASCIIString());
        } catch (AlgebraicDegreeOverflowException adoe) {
            System.err.println("AlgebraicDegreeOverflowException should not have occurred in this context: " + adoe.getMessage());
            // This would merit a fail if occurred in a test.
        } catch (NotDivisibleException nde) {
            notDivEisenstein = nde;
        }
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
     * Test of getNumericRealPart method, of class NotDivisibleException.
     */
    @Test
    public void testGetNumericRealPart() {
        System.out.println("getNumericRealPart");
        double expResult, result;
        expResult = 1.0 / 4.0;
        result = notDivEisenstein.getNumericRealPart();
        Assert.assertEquals(expResult, result, ImaginaryQuadraticRingTest.TEST_DELTA);
        expResult = 8.0 / 5.0;
        result = notDivGaussian.getNumericRealPart();
        assertEquals(expResult, result, ImaginaryQuadraticRingTest.TEST_DELTA);
    }

    /**
     * Test of getNumericImagPartMult method, of class NotDivisibleException.
     */
    @Test
    public void testGetNumericImagPartMult() {
        System.out.println("getNumericImagPartMult");
        double expResult, result;
        expResult = -9.0 / 4.0;
        result = notDivEisenstein.getNumericImagPartMult();
        assertEquals(expResult, result, ImaginaryQuadraticRingTest.TEST_DELTA);
        expResult = -1.0 / 5.0;
        result = notDivGaussian.getNumericImagPartMult();
        assertEquals(expResult, result, ImaginaryQuadraticRingTest.TEST_DELTA);
    }

    /**
     * Test of getNumericImagPart method, of class NotDivisibleException.
     */
    @Test
    public void testGetNumericImagPart() {
        System.out.println("getNumericImagPart");
        double expResult, result;
        expResult = -9.0 * Math.sqrt(3) / 4.0;
        result = notDivEisenstein.getNumericImagPart();
        assertEquals(expResult, result, ImaginaryQuadraticRingTest.TEST_DELTA);
        expResult = -1.0 / 5.0;
        result = notDivGaussian.getNumericImagPart();
        assertEquals(expResult, result, ImaginaryQuadraticRingTest.TEST_DELTA);
    }
    
    private void sortIQIArray(ImaginaryQuadraticInteger[] iqiArray) {
        ImaginaryQuadraticInteger swapper;
        boolean swapFlag;
        do {
            swapFlag = false;
            for (int i = 0; i < iqiArray.length - 1; i++) {
                if (iqiArray[i].norm() > iqiArray[i + 1].norm()) {
                    swapper = iqiArray[i];
                    iqiArray[i] = iqiArray[i + 1];
                    iqiArray[i + 1] = swapper;
                    swapFlag = true;
                }
            }
        } while (swapFlag);
    }
    
    /**
     * Test of getBoundingIntegers method, of class NotDivisibleException. This 
     * test does not prescribe that the bounding integers should be returned in 
     * any particular order. The test will sort the result array by norm prior 
     * to array comparison.
     */
    @Test
    public void testGetBoundingIntegers() {
        System.out.println("getBoundingIntegers");
        ImaginaryQuadraticInteger[] expResult = new ImaginaryQuadraticInteger[4];
        ImaginaryQuadraticInteger[] result;
        expResult[0] = new ImaginaryQuadraticInteger(1, 0, NumberTheoreticFunctionsCalculator.RING_GAUSSIAN);
        expResult[1] = new ImaginaryQuadraticInteger(1, -1, NumberTheoreticFunctionsCalculator.RING_GAUSSIAN);
        expResult[2] = new ImaginaryQuadraticInteger(2, 0, NumberTheoreticFunctionsCalculator.RING_GAUSSIAN);
        expResult[3] = new ImaginaryQuadraticInteger(2, -1, NumberTheoreticFunctionsCalculator.RING_GAUSSIAN);
        result = notDivGaussian.getBoundingIntegers();
        sortIQIArray(result);
        assertArrayEquals(expResult, result);
        expResult[0] = new ImaginaryQuadraticInteger(0, -2, NumberTheoreticFunctionsCalculator.RING_EISENSTEIN);
        expResult[1] = new ImaginaryQuadraticInteger(-1, -5, NumberTheoreticFunctionsCalculator.RING_EISENSTEIN, 2);
        expResult[2] = new ImaginaryQuadraticInteger(1, -5, NumberTheoreticFunctionsCalculator.RING_EISENSTEIN, 2);
        expResult[3] = new ImaginaryQuadraticInteger(0, -3, NumberTheoreticFunctionsCalculator.RING_EISENSTEIN);
        result = notDivEisenstein.getBoundingIntegers();
        sortIQIArray(result);
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of roundTowardsZero method, of class NotDivisibleException. Here we 
     * get down to the nitty-gritty, what is perhaps the most important function 
     * of this exception, the one that makes the Euclidean GCD algorithm 
     * possible. First, this tests the two exception instances notDivGaussian 
     * and notDivEisenstein.
     */
    @Test
    public void testRoundTowardsZero() {
        System.out.println("roundTowardsZero");
        ImaginaryQuadraticInteger expResult = new ImaginaryQuadraticInteger(1, 0, NumberTheoreticFunctionsCalculator.RING_GAUSSIAN);
        ImaginaryQuadraticInteger result = notDivGaussian.roundTowardsZero();
        assertEquals(expResult, result);
        expResult = new ImaginaryQuadraticInteger(0, -2, NumberTheoreticFunctionsCalculator.RING_EISENSTEIN);
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
                    dividend = new ImaginaryQuadraticInteger(-7, 15, currRing, 2);
                    divisor = new ImaginaryQuadraticInteger(-3, 7, currRing, 2);
                    expResult = new ImaginaryQuadraticInteger(2, 0, currRing);
                } else {
                    dividend = new ImaginaryQuadraticInteger(2, 1, currRing);
                    divisor = new ImaginaryQuadraticInteger(1, 1, currRing);
                    expResult = new ImaginaryQuadraticInteger(1, 0, currRing);
                }
                try {
                    division = dividend.divides(divisor);
                    fail("Dividing " + dividend + " by " + divisor + " should not have given " + division);
                } catch (AlgebraicDegreeOverflowException adoe) {
                    fail("AlgebraicDegreeOverflowException should not have happened. " + adoe.getMessage());
                } catch (NotDivisibleException nde) {
                    result = nde.roundTowardsZero();
                    assertEquals(expResult, result);
                }
                /* Now to try it with conjugates, results should be the same as 
                   before */
                try {
                    division = dividend.conjugate().divides(divisor.conjugate());
                    fail("Dividing " + dividend + " by " + divisor + " should not have given " + division);
                } catch (AlgebraicDegreeOverflowException adoe) {
                    fail("AlgebraicDegreeOverflowException should not have happened. " + adoe.getMessage());
                } catch (NotDivisibleException nde) {
                    result = nde.roundTowardsZero();
                    assertEquals(expResult, result);
                }
            }
        }
    }

    /**
     * Test of roundAwayFromZero method, of class NotDivisibleException. Not 
     * planning to test this one anywhere near as thoroughly as 
     * roundTowardsZero.
     */
    @Test
    public void testRoundAwayFromZero() {
        System.out.println("roundAwayFromZero");
        ImaginaryQuadraticInteger expResult = new ImaginaryQuadraticInteger(2, -1, NumberTheoreticFunctionsCalculator.RING_GAUSSIAN);
        ImaginaryQuadraticInteger result = notDivGaussian.roundAwayFromZero();
        assertEquals(expResult, result);
        expResult = new ImaginaryQuadraticInteger(0, -3, NumberTheoreticFunctionsCalculator.RING_EISENSTEIN);
        result = notDivEisenstein.roundAwayFromZero();
        assertEquals(expResult, result);
    }
    
}
