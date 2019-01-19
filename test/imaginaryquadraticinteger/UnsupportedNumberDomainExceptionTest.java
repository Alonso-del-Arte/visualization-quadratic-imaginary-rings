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
 * Tests for the UnsupportedNumberDomainException class.
 * @author Alonso del Arte
 */
public class UnsupportedNumberDomainExceptionTest {
    
    private static UnsupportedNumberDomainException unsDomExc;
       
    @BeforeClass
    public static void setUpClass() {
        unsDomExc = new UnsupportedNumberDomainException("Initialization state, not the result of an actually thrown exception.", NumberTheoreticFunctionsCalculator.IMAG_UNIT_I, NumberTheoreticFunctionsCalculator.IMAG_UNIT_NEG_I);
        ImaginaryQuadraticRing ring = new ImaginaryQuadraticRing(-2);
        ImaginaryQuadraticInteger numberA = new ImaginaryQuadraticInteger(0, 1, ring);
        ring = new ImaginaryQuadraticRing(-7);
        ImaginaryQuadraticInteger numberB = new ImaginaryQuadraticInteger(0, 1, ring);
        ImaginaryQuadraticInteger product;
        try {
            product = numberA.times(numberB);
            System.out.println(numberA.toASCIIString() + " \u00D7 " + numberB.toASCIIString() + " = " + product.toASCIIString());
        } catch (UnsupportedNumberDomainException unde) {
            unsDomExc = unde;
        } catch (Exception e) {
            System.out.println("Exception " + e.getClass().getName() + " encountered.");
            System.out.println("\"" + e.getMessage() + "\"");
            System.out.println("Probably all the tests will fail now.");
        }
    }
    
    /**
     * Test of getCausingNumbers method, of class 
     * UnsupportedNumberDomainException.
     */
    @Test
    public void testGetCausingNumbers() {
        System.out.println("getCausingNumbers");
        ImaginaryQuadraticRing ring = new ImaginaryQuadraticRing(-2);
        ImaginaryQuadraticInteger numberA = new ImaginaryQuadraticInteger(0, 1, ring);
        ring = new ImaginaryQuadraticRing(-7);
        ImaginaryQuadraticInteger numberB = new ImaginaryQuadraticInteger(0, 1, ring);
        AlgebraicInteger[] expResult = new AlgebraicInteger[]{numberA, numberB};
        AlgebraicInteger[] result = unsDomExc.getCausingNumbers();
        assertArrayEquals(expResult, result);
    }
    
}
