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
    
    private static NonEuclideanDomainException nonEuclExc;
    
    @BeforeClass
    public static void setUpClass() {
        nonEuclExc = new NonEuclideanDomainException("Initialization state, not the result of an actually thrown exception.", NumberTheoreticFunctionsCalculator.IMAG_UNIT_I, NumberTheoreticFunctionsCalculator.IMAG_UNIT_NEG_I);
        ImaginaryQuadraticInteger two = new ImaginaryQuadraticInteger(2, 0, RING_ZI5);
        ImaginaryQuadraticInteger onePlusRam = new ImaginaryQuadraticInteger(1, 1, RING_ZI5);
        try {
            ImaginaryQuadraticInteger gcd = NumberTheoreticFunctionsCalculator.euclideanGCD(two, onePlusRam);
            System.out.println("Found gcd(" + two.toASCIIString() + ", " + onePlusRam.toASCIIString() + ") = " + gcd + " by applying the Euclidean GCD algorithm.");
        } catch (NonEuclideanDomainException nede) {
            nonEuclExc = nede;
        }
        System.out.println("NonEuclideanDomainException for the example gcd(" + two.toASCIIString() + ", " + onePlusRam.toASCIIString() + ") example has this message: \"" + nonEuclExc.getMessage());
    }
    
    /**
     * Test of getEuclideanGCDAttemptedNumbers method, of class NonEuclideanDomainException.
     */
    @Test
    public void testGetEuclideanGCDAttemptedNumbers() {
        System.out.println("getEuclideanGCDAttemptedNumbers");
        ImaginaryQuadraticInteger two = new ImaginaryQuadraticInteger(2, 0, RING_ZI5);
        ImaginaryQuadraticInteger onePlusRam = new ImaginaryQuadraticInteger(1, 1, RING_ZI5);
        ImaginaryQuadraticInteger[] expResult = new ImaginaryQuadraticInteger[]{two, onePlusRam};
        ImaginaryQuadraticInteger[] result = nonEuclExc.getEuclideanGCDAttemptedNumbers();
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of tryEuclideanGCDAnyway method, of class NonEuclideanDomainException.
     */
    @Test
    public void testTryEuclideanGCDAnyway() {
        System.out.println("tryEuclideanGCDAnyway");
        ImaginaryQuadraticInteger expResult = new ImaginaryQuadraticInteger(1, 0, RING_ZI5);
        ImaginaryQuadraticInteger result = nonEuclExc.tryEuclideanGCDAnyway();
        assertEquals(expResult, result);
    }
    
}
