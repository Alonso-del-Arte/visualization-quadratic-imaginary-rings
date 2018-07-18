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

import java.util.ArrayList;
import java.util.List;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the NonUniqueFactorizationDomainException class. The purpose of 
 * this test class is only to make sure the exception object works as it should. 
 * Testing whether this exception is thrown for the right reasons or not is the 
 * responsibility of other test classes. All the tests in this test class use 
 * the famous domain <b>Z</b>[&radic;-5], which is not a unique factorization 
 * domain.
 * @author Alonso del Arte
 */
public class NonUniqueFactorizationDomainExceptionTest {
    
    public static final ImaginaryQuadraticRing RING_ZI5 = new ImaginaryQuadraticRing(-5);
    
    private static NonUniqueFactorizationDomainException nufdeSqrti5;
    private static NonUniqueFactorizationDomainException nufde05;
    private static NonUniqueFactorizationDomainException nufde06;
    private static NonUniqueFactorizationDomainException nufde41PF;
    private static NonUniqueFactorizationDomainException nufde41;
    
    @BeforeClass
    public static void setUpClass() {
        ImaginaryQuadraticInteger init = new ImaginaryQuadraticInteger(1, 0, RING_ZI5);
        nufdeSqrti5 = new NonUniqueFactorizationDomainException("Initialization state, not the result of an actually thrown exception.", init);
        nufde05 = new NonUniqueFactorizationDomainException("Initialization state, not the result of an actually thrown exception.", init);
        nufde06 = new NonUniqueFactorizationDomainException("Initialization state, not the result of an actually thrown exception.", init);
        nufde41PF = new NonUniqueFactorizationDomainException("Initialization state, not the result of an actually thrown exception.", init);
        nufde41 = new NonUniqueFactorizationDomainException("Initialization state, not the result of an actually thrown exception.", init);
        List<ImaginaryQuadraticInteger> factorsList;
        init = new ImaginaryQuadraticInteger(0, 1, RING_ZI5);
        try {
            factorsList = NumberTheoreticFunctionsCalculator.primeFactors(init);
            System.out.println(init.toASCIIString() + " has " + factorsList.size() + " factors.");
        } catch (NonUniqueFactorizationDomainException nufde) {
            nufdeSqrti5 = nufde;
        }
        System.out.println("NonUniqueFactorizationDomainException for the " + init.toASCIIString() + " example has this message: \"" + nufdeSqrti5.getMessage() + "\"");
        init = new ImaginaryQuadraticInteger(5, 0, RING_ZI5);
        try {
            factorsList = NumberTheoreticFunctionsCalculator.primeFactors(init);
            System.out.println(init.toASCIIString() + " has " + factorsList.size() + " factors.");
        } catch (NonUniqueFactorizationDomainException nufde) {
            nufde05 = nufde;
        }
        System.out.println("NonUniqueFactorizationDomainException for the " + init.toASCIIString() + " example has this message: \"" + nufde05.getMessage() + "\"");
        init = new ImaginaryQuadraticInteger(6, 0, RING_ZI5);
        try {
            factorsList = NumberTheoreticFunctionsCalculator.primeFactors(init);
            System.out.println(init.toASCIIString() + " has " + factorsList.size() + " factors.");
        } catch (NonUniqueFactorizationDomainException nufde) {
            nufde06 = nufde;
        }
        System.out.println("NonUniqueFactorizationDomainException for the " + init.toASCIIString() + " example has this message: \"" + nufde06.getMessage() + "\"");
        init = new ImaginaryQuadraticInteger(6, 1, RING_ZI5);
        try {
            factorsList = NumberTheoreticFunctionsCalculator.primeFactors(init);
            System.out.println(init.toASCIIString() + " has " + factorsList.size() + " factors.");
        } catch (NonUniqueFactorizationDomainException nufde) {
            nufde41PF = nufde;
        }
        System.out.println("NonUniqueFactorizationDomainException for the " + init.toASCIIString() + " example has this message: \"" + nufde41PF.getMessage() + "\"");
        init = new ImaginaryQuadraticInteger(41, 0, RING_ZI5);
        try {
            factorsList = NumberTheoreticFunctionsCalculator.primeFactors(init);
            System.out.println(init.toASCIIString() + " has " + factorsList.size() + " factors.");
        } catch (NonUniqueFactorizationDomainException nufde) {
            nufde41 = nufde;
        }
        System.out.println("NonUniqueFactorizationDomainException for the " + init.toASCIIString() + " example has this message: \"" + nufde41.getMessage() + "\"");
    }
    
    /**
     * Test of getUnfactorizedNumber method, of class 
     * NonUniqueFactorizationDomainException. This is a simple getter, so it's a 
     * fairly straightforward test.
     */
    @Test
    public void testGetUnfactorizedNumber() {
        System.out.println("getUnfactorizedNumber");
        ImaginaryQuadraticInteger expResult = new ImaginaryQuadraticInteger(0, 1, RING_ZI5);
        ImaginaryQuadraticInteger result = nufdeSqrti5.getUnfactorizedNumber();
        assertEquals(expResult, result);
        expResult = new ImaginaryQuadraticInteger(0, 1, RING_ZI5);
        result = nufdeSqrti5.getUnfactorizedNumber();
        assertEquals(expResult, result);
        expResult = new ImaginaryQuadraticInteger(5, 0, RING_ZI5);
        result = nufde05.getUnfactorizedNumber();
        assertEquals(expResult, result);
        expResult = new ImaginaryQuadraticInteger(6, 0, RING_ZI5);
        result = nufde06.getUnfactorizedNumber();
        assertEquals(expResult, result);
        expResult = new ImaginaryQuadraticInteger(6, 1, RING_ZI5);
        result = nufde41PF.getUnfactorizedNumber();
        assertEquals(expResult, result);
        expResult = new ImaginaryQuadraticInteger(41, 0, RING_ZI5);
        result = nufde41.getUnfactorizedNumber();
        assertEquals(expResult, result);
    }

    /**
     * Test of tryToFactorizeAnyway method, of class 
     * NonUniqueFactorizationDomainException. The expectation is that when a 
     * number in a non-UFD can be properly factorized into primes, 
     * tryToFactorizeAnyway() will return a list of those primes. But when a 
     * number is the product of irreducible numbers that are not prime, 
     * tryToFactorizeAnyway() will return a list of factors that includes -1 as 
     * a factor at least twice, so as to alert the recipient of the information 
     * that another factorization might be possible.
     */
    @Test
    public void testTryToFactorizeAnyway() {
        System.out.println("tryToFactorizeAnyway");
        List<ImaginaryQuadraticInteger> expResult = new ArrayList<>();
        ImaginaryQuadraticInteger number = new ImaginaryQuadraticInteger(0, 1, RING_ZI5);
        expResult.add(number); // Add sqrt(-5) to the list of expected factors
        System.out.println("Try to factorize " + nufdeSqrti5.getUnfactorizedNumber().toASCIIString());
        List<ImaginaryQuadraticInteger> result = nufdeSqrti5.tryToFactorizeAnyway();
        assertEquals(expResult, result);
        expResult.add(number); // Add sqrt(-5) to the list of expected factors again
        number = new ImaginaryQuadraticInteger(-1, 0, RING_ZI5); // Good old -1, a unit
        expResult.add(0, number);
        System.out.println("Try to factorize " + nufde05.getUnfactorizedNumber().toASCIIString());
        result = nufde05.tryToFactorizeAnyway();
        assertEquals(expResult, result);
        expResult.clear();
        expResult.add(number);
        expResult.add(number); // Add -1 to the list twice
        System.out.println("Try to factorize " + nufde06.getUnfactorizedNumber().toASCIIString());
        result = nufde06.tryToFactorizeAnyway();
        String assertionMessage = "Factorization of " + nufde06.getUnfactorizedNumber().toASCIIString() + " should include -1 twice.";
        assertTrue(assertionMessage, result.containsAll(expResult));
        number = number.times(-1); // Number is now 1
        for (ImaginaryQuadraticInteger iqi : result) {
            number = number.times(iqi);
        }
        assertionMessage = "Numbers in factorization of " + nufde06.getUnfactorizedNumber().toASCIIString() + " should multiply to said number.";
        assertTrue(assertionMessage, number.equals(nufde06.getUnfactorizedNumber()));
        expResult.clear();
        number = new ImaginaryQuadraticInteger(6, -1, RING_ZI5); // 6 - sqrt(-5)
        expResult.add(number);
        expResult.add(number.conjugate()); // 6 + sqrt(-5)
        System.out.println("Try to factorize " + nufde41.getUnfactorizedNumber().toASCIIString());
        result = nufde41.tryToFactorizeAnyway();
        assertEquals(expResult, result);
        expResult.remove(number);
        System.out.println("Try to factorize " + nufde41PF.getUnfactorizedNumber().toASCIIString());
        result = nufde41PF.tryToFactorizeAnyway();
        assertEquals(expResult, result);
    }
    
}
