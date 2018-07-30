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
 * Tests for the NonEuclideanDomainException class. The purpose of this test 
 * class is only to make sure the exception object works as it should. Testing 
 * whether this exception is thrown for the right reasons or not is the 
 * responsibility of other test classes. With one exception, all the tests in 
 * this test class use the famous domain <b>Z</b>[&radic;-5], which is not an 
 * Euclidean domain.
 * @author Alonso del Arte
 */
public class NonEuclideanDomainExceptionTest {
    
    /**
     * The famous ring <b>Z</b>[&radic;-5], consisting of numbers of the form a 
     * + b&radic;-5. It is not an Euclidean domain, since, for example, the 
     * Euclidean GCD algorithm fails for gcd(2, 1 + &radic;-5).
     */
    public static final ImaginaryQuadraticRing RING_ZI5 = new ImaginaryQuadraticRing(-5);
    
    private static NonEuclideanDomainException nonEuclExc6, nonEuclExc29, nonEuclExc143;
    
    @BeforeClass
    public static void setUpClass() {
        nonEuclExc6 = new NonEuclideanDomainException("Initialization state, not the result of an actually thrown exception.", NumberTheoreticFunctionsCalculator.IMAG_UNIT_I, NumberTheoreticFunctionsCalculator.IMAG_UNIT_NEG_I);
        nonEuclExc29 = new NonEuclideanDomainException("Initialization state, not the result of an actually thrown exception.", NumberTheoreticFunctionsCalculator.IMAG_UNIT_NEG_I, NumberTheoreticFunctionsCalculator.IMAG_UNIT_I);
        nonEuclExc143 = new NonEuclideanDomainException("Initialization state, not the result of an actually thrown exception.", NumberTheoreticFunctionsCalculator.COMPLEX_CUBIC_ROOT_OF_UNITY, NumberTheoreticFunctionsCalculator.COMPLEX_CUBIC_ROOT_OF_UNITY.times(-1));
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
        iqib = new ImaginaryQuadraticInteger(13, 0, RING_ZI5);
        try {
            ImaginaryQuadraticInteger gcd = NumberTheoreticFunctionsCalculator.euclideanGCD(iqia, iqib);
            System.out.println("Found gcd(" + iqia.toASCIIString() + ", " + iqib.toASCIIString() + ") = " + gcd + " by applying the Euclidean GCD algorithm.");
        } catch (NonEuclideanDomainException nede) {
            nonEuclExc143 = nede;
        }
        System.out.println("NonEuclideanDomainException for the example gcd(" + iqia.toASCIIString() + ", " + iqib.toASCIIString() + ") example has this message: \"" + nonEuclExc143.getMessage());
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
        iqia = iqia.plus(27);
        ImaginaryQuadraticInteger addend = new ImaginaryQuadraticInteger(-8, 4, RING_ZI5);
        iqib = iqib.plus(addend);
        expResult = new ImaginaryQuadraticInteger[]{iqia, iqib};
        result = nonEuclExc29.getEuclideanGCDAttemptedNumbers();
        assertArrayEquals(expResult, result);
        addend = new ImaginaryQuadraticInteger(-29, 11, RING_ZI5);
        iqia = iqia.plus(addend);
        addend = new ImaginaryQuadraticInteger(20, -5, RING_ZI5);
        iqib = iqib.plus(addend);
        expResult = new ImaginaryQuadraticInteger[]{iqia, iqib};
        result = nonEuclExc143.getEuclideanGCDAttemptedNumbers();
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
        System.out.println("gcd(2, 1 + sqrt(-5)) = " + result.toASCIIString() + "?");
        String assertionMessage = "Real part of attempted Euclidean GCD result on gcd(2, 1 + \u221A(-5)) should be negative.";
        assertTrue(assertionMessage, result.getRealPartMult() < 0);
        ImaginaryQuadraticInteger expResult = new ImaginaryQuadraticInteger(3, 2, RING_ZI5);
        result = nonEuclExc29.tryEuclideanGCDAnyway();
        System.out.print("gcd(29, -7 + 5sqrt(-5)) = " + result.toASCIIString() + "?");
        assertEquals(expResult, result);
        System.out.println(" Confirmed.");
        expResult = new ImaginaryQuadraticInteger(1, 0, RING_ZI5);
        result = nonEuclExc143.tryEuclideanGCDAnyway();
        System.out.print("gcd(11sqrt(-5), 13) = " + result.toASCIIString() + "?");
        assertEquals(expResult, result);
        System.out.println(" Confirmed.");
    }
    
}
