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
package com.alonsodelarte.quadraticRings.imaginaryquadraticinteger.Exceptions;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.alonsodelarte.quadraticRings.imaginaryquadraticinteger.ImaginaryQuadraticInteger;
import com.alonsodelarte.quadraticRings.imaginaryquadraticinteger.ImaginaryQuadraticRing;
import com.alonsodelarte.quadraticRings.imaginaryquadraticinteger.NumberTheoreticFunctionsCalculator;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests for the NonEuclideanDomainException class. The purpose of this test 
 * class is only to make sure the exception object works as it should. Testing 
 * whether this exception is thrown for the right reasons or not is the 
 * responsibility of other test classes. A lot of the tests in this test class 
 * use the famous domain <b>Z</b>[&radic;-5], which is not an Euclidean domain 
 * nor even a principal ideal domain.
 * @author Alonso del Arte
 */
public class NonEuclideanDomainExceptionTest {
    
    /**
     * The famous ring <b>Z</b>[&radic;-5], consisting of numbers of the form a 
     * + b&radic;-5. It is not an Euclidean domain, since, for example, the 
     * Euclidean GCD algorithm fails for gcd(2, 1 + &radic;-5).
     */
    public static final ImaginaryQuadraticRing RING_ZI5 = new ImaginaryQuadraticRing(-5);
    
    /**
     * The ring <i>O</i><sub><b>Q</b>(&radic;-19)</sub>, famous for being a 
     * principal ideal domain but not Euclidean.
     */
    public static final ImaginaryQuadraticRing RING_OQI19 = new ImaginaryQuadraticRing(-19);
    
    /**
     * Just the number &radic;-5.
     */
    public static final ImaginaryQuadraticInteger ZI5_RAMIFIER = new ImaginaryQuadraticInteger(0, 1, RING_ZI5);
    
    private static NonEuclideanDomainException nonEuclExc6, nonEuclExc29, nonEuclExc143A, nonEuclExc143B, nonEuclExc700;
    
    /**
     * Sets up five NonEuclideanDomainException objects. First they are 
     * initialized with Gaussian or Eisenstein units, then exceptions are tried 
     * to be caught to set them with numbers from non-Euclidean domains.
     */
    @BeforeClass
    public static void setUpClass() {
        nonEuclExc6 = new NonEuclideanDomainException("Initialization state, not the result of an actually thrown exception.", NumberTheoreticFunctionsCalculator.IMAG_UNIT_I, NumberTheoreticFunctionsCalculator.IMAG_UNIT_NEG_I);
        nonEuclExc29 = new NonEuclideanDomainException("Initialization state, not the result of an actually thrown exception.", NumberTheoreticFunctionsCalculator.IMAG_UNIT_NEG_I, NumberTheoreticFunctionsCalculator.IMAG_UNIT_I);
        nonEuclExc143A = new NonEuclideanDomainException("Initialization state, not the result of an actually thrown exception.", NumberTheoreticFunctionsCalculator.COMPLEX_CUBIC_ROOT_OF_UNITY, NumberTheoreticFunctionsCalculator.COMPLEX_CUBIC_ROOT_OF_UNITY.times(-1));
        nonEuclExc143B = new NonEuclideanDomainException("Initialization state, not the result of an actually thrown exception.", NumberTheoreticFunctionsCalculator.COMPLEX_CUBIC_ROOT_OF_UNITY, NumberTheoreticFunctionsCalculator.COMPLEX_CUBIC_ROOT_OF_UNITY.times(-1));
        nonEuclExc700 = new NonEuclideanDomainException("Initialization state, not the result of an actually thrown exception.", NumberTheoreticFunctionsCalculator.COMPLEX_CUBIC_ROOT_OF_UNITY, NumberTheoreticFunctionsCalculator.COMPLEX_CUBIC_ROOT_OF_UNITY.times(-1));
        ImaginaryQuadraticInteger iqia = new ImaginaryQuadraticInteger(2, 0, RING_ZI5);
        ImaginaryQuadraticInteger iqib = new ImaginaryQuadraticInteger(1, 1, RING_ZI5);
        try {
            ImaginaryQuadraticInteger gcd = NumberTheoreticFunctionsCalculator.euclideanGCD(iqia, iqib);
            System.out.println("Found gcd(" + iqia.toASCIIString() + ", " + iqib.toASCIIString() + ") = " + gcd + " by applying the Euclidean GCD algorithm.");
        } catch (NonEuclideanDomainException nede) {
            nonEuclExc6 = nede;
        }
        System.out.println("NonEuclideanDomainException for the example gcd(" + iqia.toASCIIString() + ", " + iqib.toASCIIString() + ") example has this message: \"" + nonEuclExc6.getMessage());
        iqia = new ImaginaryQuadraticInteger(29, 0, RING_ZI5);
        iqib = new ImaginaryQuadraticInteger(-7, 5, RING_ZI5);
        try {
            ImaginaryQuadraticInteger gcd = NumberTheoreticFunctionsCalculator.euclideanGCD(iqia, iqib);
            System.out.println("Found gcd(" + iqia.toASCIIString() + ", " + iqib.toASCIIString() + ") = " + gcd + " by applying the Euclidean GCD algorithm.");
        } catch (NonEuclideanDomainException nede) {
            nonEuclExc29 = nede;
        }
        System.out.println("NonEuclideanDomainException for the example gcd(" + iqia.toASCIIString() + ", " + iqib.toASCIIString() + ") example has this message: \"" + nonEuclExc29.getMessage());
        iqia = new ImaginaryQuadraticInteger(0, 11, RING_ZI5);
        iqib = new ImaginaryQuadraticInteger(0, 13, RING_ZI5);
        try {
            ImaginaryQuadraticInteger gcd = NumberTheoreticFunctionsCalculator.euclideanGCD(iqia, iqib);
            System.out.println("Found gcd(" + iqia.toASCIIString() + ", " + iqib.toASCIIString() + ") = " + gcd + " by applying the Euclidean GCD algorithm.");
        } catch (NonEuclideanDomainException nede) {
            nonEuclExc143A = nede;
        }
        System.out.println("NonEuclideanDomainException for the example gcd(" + iqia.toASCIIString() + ", " + iqib.toASCIIString() + ") example has this message: \"" + nonEuclExc143A.getMessage());
        iqia = new ImaginaryQuadraticInteger(0, 11, RING_ZI5);
        iqib = new ImaginaryQuadraticInteger(13, 0, RING_ZI5);
        try {
            ImaginaryQuadraticInteger gcd = NumberTheoreticFunctionsCalculator.euclideanGCD(iqia, iqib);
            System.out.println("Found gcd(" + iqia.toASCIIString() + ", " + iqib.toASCIIString() + ") = " + gcd + " by applying the Euclidean GCD algorithm.");
        } catch (NonEuclideanDomainException nede) {
            nonEuclExc143B = nede;
        }
        System.out.println("NonEuclideanDomainException for the example gcd(" + iqia.toASCIIString() + ", " + iqib.toASCIIString() + ") example has this message: \"" + nonEuclExc143B.getMessage());
        iqia = new ImaginaryQuadraticInteger(10, 0, RING_OQI19);
        iqib = new ImaginaryQuadraticInteger(3, 1, RING_OQI19, 2);
        try {
            ImaginaryQuadraticInteger gcd = NumberTheoreticFunctionsCalculator.euclideanGCD(iqia, iqib);
            System.out.println("Found gcd(" + iqia.toASCIIString() + ", " + iqib.toASCIIString() + ") = " + gcd + " by applying the Euclidean GCD algorithm.");
        } catch (NonEuclideanDomainException nede) {
            nonEuclExc700 = nede;
        }
        System.out.println("NonEuclideanDomainException for the example gcd(" + iqia.toASCIIString() + ", " + iqib.toASCIIString() + ") example has this message: \"" + nonEuclExc700.getMessage());
    }
    
    /**
     * Test of getEuclideanGCDAttemptedNumbers method, of class 
     * NonEuclideanDomainException.
     */
    @Test
    public void testGetEuclideanGCDAttemptedNumbers() {
        System.out.println("getEuclideanGCDAttemptedNumbers");
        ImaginaryQuadraticInteger iqia = new ImaginaryQuadraticInteger(2, 0, RING_ZI5);
        ImaginaryQuadraticInteger iqib = new ImaginaryQuadraticInteger(1, 1, RING_ZI5);
        ImaginaryQuadraticInteger[] expResult = new ImaginaryQuadraticInteger[]{iqia, iqib};
        ImaginaryQuadraticInteger[] result = nonEuclExc6.getEuclideanGCDAttemptedNumbers();
        assertArrayEquals(expResult, result);
        iqia = new ImaginaryQuadraticInteger(29, 0, RING_ZI5);
        iqib = new ImaginaryQuadraticInteger(-7, 5, RING_ZI5);
        expResult = new ImaginaryQuadraticInteger[]{iqia, iqib};
        result = nonEuclExc29.getEuclideanGCDAttemptedNumbers();
        assertArrayEquals(expResult, result);
        iqia = new ImaginaryQuadraticInteger(0, 11, RING_ZI5);
        iqib = new ImaginaryQuadraticInteger(0, 13, RING_ZI5);
        expResult = new ImaginaryQuadraticInteger[]{iqia, iqib};
        result = nonEuclExc143A.getEuclideanGCDAttemptedNumbers();
        assertArrayEquals(expResult, result);
        iqia = new ImaginaryQuadraticInteger(0, 11, RING_ZI5);
        try {
            iqib = iqib.divides(ZI5_RAMIFIER);
        } catch (NotDivisibleException nde) {
            System.out.println("NotDivisibleException \"" + nde.getMessage() + "\" should not have occurred.");
            fail("Tests for NotDivisibleException may need review.");
        }
        expResult = new ImaginaryQuadraticInteger[]{iqia, iqib};
        result = nonEuclExc143B.getEuclideanGCDAttemptedNumbers();
        assertArrayEquals(expResult, result);
        iqia = new ImaginaryQuadraticInteger(10, 0, RING_OQI19);
        iqib = new ImaginaryQuadraticInteger(3, 1, RING_OQI19, 2);
        expResult = new ImaginaryQuadraticInteger[]{iqia, iqib};
        result = nonEuclExc700.getEuclideanGCDAttemptedNumbers();
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of tryEuclideanGCDAnyway method, of class 
     * NonEuclideanDomainException. The expectation is that when the Euclidean 
     * algorithm can complete successfully, the correct result will be obtained. 
     * But when the Euclidean algorithm fails, the only requirement on the 
     * result is that its real part be negative, so that the caller can be aware 
     * that the result is unreliable.
     */
    @Test
    public void testTryEuclideanGCDAnyway() {
        System.out.println("tryEuclideanGCDAnyway");
        ImaginaryQuadraticInteger result = nonEuclExc6.tryEuclideanGCDAnyway();
        System.out.print("gcd(2, 1 + sqrt(-5)) = " + result.toASCIIString() + "?");
        String assertionMessage = "Real part of attempted Euclidean GCD result on gcd(2, 1 + \u221A(-5)) should be negative.";
        assertTrue(assertionMessage, result.getRealPartMult() < 0);
        System.out.println("????");
        ImaginaryQuadraticInteger expResult = new ImaginaryQuadraticInteger(3, 2, RING_ZI5);
        result = nonEuclExc29.tryEuclideanGCDAnyway();
        System.out.print("gcd(29, -7 + 5sqrt(-5)) = " + result.toASCIIString() + "?");
        assertEquals(expResult, result);
        System.out.println(" Confirmed.");
        expResult = new ImaginaryQuadraticInteger(0, 1, RING_ZI5);
        result = nonEuclExc143A.tryEuclideanGCDAnyway();
        System.out.print("gcd(11sqrt(-5), 13sqrt(-5)) = " + result.toASCIIString() + "?");
        assertEquals(expResult, result);
        System.out.println(" Confirmed.");
        result = nonEuclExc143B.tryEuclideanGCDAnyway();
        System.out.print("gcd(11sqrt(-5), 13) = " + result.toASCIIString() + "?");
        assertionMessage = "Real part of attempted Euclidean GCD result on gcd(10, 3/2 + \u221A(-7)/2) should be negative.";
        assertTrue(assertionMessage, result.getRealPartMult() < 0);
        System.out.println("????");
        result = nonEuclExc700.tryEuclideanGCDAnyway();
        System.out.print("gcd(10, 3/2 + sqrt(-7)/2) = " + result.toASCIIString() + "?");
        assertionMessage = "Real part of attempted Euclidean GCD result on gcd(10, 3/2 + \u221A(-7)/2) should be negative.";
        assertTrue(assertionMessage, result.getRealPartMult() < 0);
        System.out.println("????");
    }
    
    /**
     * Another test of tryEuclideanGCDAnyway, of class 
     * NonEuclideanDomainException. This test uses the following facts: if 
     * <i>d</i> is even, then the norms of 2 and &radic;<i>d</i> are both even 
     * but the two numbers are actually coprime and the Euclidean algorithm is 
     * unable to make that determination (except of course for 
     * <b>Z</b>[&radic;-2]); if <i>d</i> = 1 mod 4, then gcd(2, 1 + 
     * &radic;<i>d</i>) = 2 and the Euclidean algorithm should be able to make 
     * that determination even in a non-Euclidean domain, but if <i>d</i> = 3 
     * mod 4, the Euclidean algorithm will fail to find gcd(2, 1 + 
     * &radic;<i>d</i>) = 1 except in the case <i>d</i> = -1, in which it 
     * instead finds that gcd(2, 1 + <i>i</i>) = 1 + <i>i</i>.
     */
    @Test
    public void testTryEuclideanGCDInSeveralRings() {
        System.out.println("tryEuclideanGCDAnyway in several different rings");
        ImaginaryQuadraticRing currRing;
        ImaginaryQuadraticInteger two, onePlusRam, ramifier, gcd, negNorm;
        // Three initializations so as to avoid error message on last report out
        onePlusRam = new ImaginaryQuadraticInteger(0, 0, RING_ZI5);
        negNorm = new ImaginaryQuadraticInteger(0, 0, RING_ZI5);
        gcd =  new ImaginaryQuadraticInteger(0, 0, RING_ZI5);
        String assertionMessage; // This one can be initialized right before use
        for (int iterDiscr = -1; iterDiscr > -200; iterDiscr--) {
            if (NumberTheoreticFunctionsCalculator.isSquareFree(iterDiscr)) {
                currRing = new ImaginaryQuadraticRing(iterDiscr);
                two = new ImaginaryQuadraticInteger(2, 0, currRing);
                onePlusRam = new ImaginaryQuadraticInteger(1, 1, currRing);
                ramifier = onePlusRam.minus(1);
                try {
                    if (iterDiscr % 2 == 0) {
                        gcd = NumberTheoreticFunctionsCalculator.euclideanGCD(two, ramifier);
                        assertEquals(ramifier, gcd);
                    } else {
                        gcd = NumberTheoreticFunctionsCalculator.euclideanGCD(two, onePlusRam);
                        if (iterDiscr == -1 || iterDiscr == -3) {
                            assertEquals(onePlusRam, gcd);
                        } else {
                            assertEquals(two, gcd);
                        }
                    }
                } catch (NonEuclideanDomainException nede) {
                    gcd = nede.tryEuclideanGCDAnyway();
                    if (iterDiscr % 4 == -3) {
                        assertionMessage = "gcd(2, " + onePlusRam.toASCIIString() + ") should be 2.";
                        assertEquals(assertionMessage, two, gcd);
                    } else {
                        assertionMessage = "Real part of attempted Euclidean GCD should be negative.";
                        assertTrue(assertionMessage, gcd.getRealPartMult() < 0);
                    }
                }
                /* Also gcd(-1 - sqrt(d), -norm(1 + sqrt(d))) should be 1 + 
                   sqrt(d). */
                negNorm = onePlusRam.times(onePlusRam.conjugate());
                negNorm = negNorm.times(-1);
                try {
                    gcd = NumberTheoreticFunctionsCalculator.euclideanGCD(onePlusRam.times(-1), negNorm);
                } catch (NonEuclideanDomainException nede) {
                    gcd = nede.tryEuclideanGCDAnyway();
                }
                assertEquals(onePlusRam, gcd);
            }
        }
        System.out.println("gcd(" + onePlusRam.times(-1).toASCIIString() + ", " + negNorm.toASCIIString() + ") = " + gcd.toASCIIString() + "? Confirmed.");
    }
    
}
