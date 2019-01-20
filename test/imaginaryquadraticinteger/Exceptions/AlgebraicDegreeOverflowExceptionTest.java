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

import imaginaryquadraticinteger.AlgebraicInteger;
import imaginaryquadraticinteger.Exceptions.AlgebraicDegreeOverflowException;
import imaginaryquadraticinteger.ImaginaryQuadraticInteger;
import imaginaryquadraticinteger.ImaginaryQuadraticRing;
import imaginaryquadraticinteger.NumberTheoreticFunctionsCalculator;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the AlgebraicDegreeOverflowException class.
 * @author Alonso del Arte
 */
public class AlgebraicDegreeOverflowExceptionTest {
    
    private static AlgebraicDegreeOverflowException algDegOvflExc;
    
    @BeforeClass
    public static void setUpClass() {
        algDegOvflExc = new AlgebraicDegreeOverflowException("Initialization state, not the result of an actually thrown exception.", 2, NumberTheoreticFunctionsCalculator.IMAG_UNIT_I, NumberTheoreticFunctionsCalculator.IMAG_UNIT_NEG_I);
        ImaginaryQuadraticRing ring = new ImaginaryQuadraticRing(-2);
        ImaginaryQuadraticInteger numberA = new ImaginaryQuadraticInteger(14, 7, ring);
        ring = new ImaginaryQuadraticRing(-7);
        ImaginaryQuadraticInteger numberB = new ImaginaryQuadraticInteger(7, 1, ring);
        ImaginaryQuadraticInteger sum;
        try {
            sum = numberA.plus(numberB);
            System.out.println(numberA.toASCIIString() + " + " + numberB.toASCIIString() + " = " + sum.toASCIIString());
        } catch (AlgebraicDegreeOverflowException adoe) {
            algDegOvflExc = adoe;
        } catch (Exception e) {
            System.out.println("Exception " + e.getClass().getName() + " encountered.");
            System.out.println("\"" + e.getMessage() + "\"");
            System.out.println("Probably all the tests will fail now.");
        }
    }
    
    /**
     * Test of getMaxExpectedAlgebraicDegree method, of class 
     * AlgebraicDegreeOverflowException.
     */
    @Test
    public void testGetMaxExpectedAlgebraicDegree() {
        System.out.println("getMaxExpectedAlgebraicDegree");
        assertEquals(2, algDegOvflExc.getMaxExpectedAlgebraicDegree());
    }

    /**
     * Test of getNecessaryAlgebraicDegree method, of class 
     * AlgebraicDegreeOverflowException.
     */
    @Test
    public void testGetNecessaryAlgebraicDegree() {
        System.out.println("getNecessaryAlgebraicDegree");
        assertEquals(4, algDegOvflExc.getNecessaryAlgebraicDegree());
    }

    /**
     * Test of getCausingNumbers method, of class 
     * AlgebraicDegreeOverflowException.
     */
    @Test
    public void testGetCausingNumbers() {
        System.out.println("getCausingNumbers");
        ImaginaryQuadraticRing ring = new ImaginaryQuadraticRing(-2);
        ImaginaryQuadraticInteger numberA = new ImaginaryQuadraticInteger(14, 7, ring);
        ring = new ImaginaryQuadraticRing(-7);
        ImaginaryQuadraticInteger numberB = new ImaginaryQuadraticInteger(7, 1, ring);
        AlgebraicInteger[] expResult = new AlgebraicInteger[]{numberA, numberB};
        AlgebraicInteger[] result = algDegOvflExc.getCausingNumbers();
        assertArrayEquals(expResult, result);
    }
    
}
