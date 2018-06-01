/*
 * Copyright (C) 2018 Alonso del Arte
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

import java.util.List;
import java.util.ArrayList;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for a collection of number theoretic functions, including basic 
 * primality testing and the Euclidean GCD algorithm. Some of these tests use a 
 * finite list of small primes and another finite list of non-prime numbers, as 
 * well as a small list of Fibonacci numbers.
 * 
 * The relevant entries in Sloane's On-Line Encyclopedia of Integer Sequences 
 * (OEIS) are: 
 * <ul>
 * <li><a href="http://oeis.org/A000040">A000040</a>: The prime numbers</li>
 * <li><a href="http://oeis.org/A000045">A000040</a>: The Fibonacci numbers</li>
 * <li><a href="http://oeis.org/A002808">A002808</a>: The composite numbers</li>
 * <li><a href="http://oeis.org/A018252">A018252</a>: 1 and the composite 
 * numbers</li>
 * </ul>
 * @author Alonso del Arte
 */
public class NumberTheoreticFunctionsCalculatorTest {
    
    /**
     * setUpClass() will generate a List of the first few consecutive primes. 
     * This constant determines how long that list will be. For example, if it's 
     * 1000, setUpClass() will generate a list of the primes between 1 and 1000.
     * It should not be greater than Integer.MAX_VALUE.
     */
    public static final int PRIME_LIST_THRESHOLD = 1000;
    
    /**
     * A List of the first few prime numbers, to be used in some of the tests.
     */
    private static List<Integer> primesList;
    
    /**
     * The size of primesList.
     */
    private static int primesListLength;
    
    /**
     * A List of composite numbers, which may or may not include 
     * PRIME_LIST_THRESHOLD.
     */
    private static List<Integer> compositesList;
    
    /**
     * A List of Fibonacci numbers.
     */
    private static List<Integer> fibonacciList;
    
    /**
     * There are the only nine negative numbers d such that the ring of 
     * algebraic integers of Q(sqrt(d)) is a unique factorization domain (though 
     * not necessarily Euclidean).
     */
    public static final int[] HEEGNER_NUMBERS = {-163, -67, -43, -19, -11, -7, -3, -2, -1};
    
    private static final int[] HEEGNER_COMPANION_PRIMES = new int[9];
    
    /**
     * Sets up a List of the first few consecutive primes, the first few 
     * composite numbers and the first few Fibonacci numbers. This provides most 
     * of what is needed for the tests.
     */
    @BeforeClass
    public static void setUpClass() {
        /* First, to generate a list of the first few consecutive primes, using 
        the sieve of Eratosthenes. */
        int threshold, halfThreshold;
        if (PRIME_LIST_THRESHOLD < 0) {
            threshold = (-1) * PRIME_LIST_THRESHOLD;
        } else {
            threshold = PRIME_LIST_THRESHOLD;
        }
        if (threshold % 2 == 1) {
            halfThreshold = (threshold + 1)/2;
        } else {
            halfThreshold = threshold/2;
        }
        primesList = new ArrayList<>();
        primesList.add(2); // Add 2 as a special case
        boolean[] primeFlags = new boolean[halfThreshold];
        for (int i = 0; i < halfThreshold; i++) {
            primeFlags[i] = true; // Presume all odd numbers prime for now
        }
        int currPrime = 3;
        int twiceCurrPrime, currIndex;
        while (currPrime < threshold) {
            primesList.add(currPrime);
            twiceCurrPrime = 2 * currPrime;
            for (int j = currPrime * currPrime; j < threshold; j += twiceCurrPrime) {
                currIndex = (j - 3)/2;
                primeFlags[currIndex] = false;
            }
            do {
                currPrime += 2;
                currIndex = (currPrime - 3)/2;
            } while (currIndex < (halfThreshold - 1) && primeFlags[currIndex] == false);
        }
        primesListLength = primesList.size();
        /* Now to make a list of composite numbers, from 4 up to and perhaps 
           including PRIME_LIST_THRESHOLD. */
        compositesList = new ArrayList<>();
        for (int c = 4; c < PRIME_LIST_THRESHOLD; c += 2) {
            compositesList.add(c);
            if (!primeFlags[c/2 - 1]) {
                compositesList.add(c + 1);
            }
        }
        System.out.println("setUpClass() has generated a list of the first " + primesListLength + " consecutive primes.");
        System.out.println("prime(" + primesListLength + ") = " + primesList.get(primesListLength - 1));
        System.out.println("There are " + (PRIME_LIST_THRESHOLD - (primesListLength + 1)) + " composite numbers up to " + PRIME_LIST_THRESHOLD + ".");
        // And now to make a list of Fibonacci numbers
        fibonacciList = new ArrayList<>();
        fibonacciList.add(0);
        fibonacciList.add(1);
        threshold = (Integer.MAX_VALUE - 3)/4; // Repurposing this variable
        currIndex = 2; // Also repurposing this one
        int currFibo = 1;
        while (currFibo < threshold) {
            currFibo = fibonacciList.get(currIndex - 2) + fibonacciList.get(currIndex - 1);
            fibonacciList.add(currFibo);
            currIndex++;
        }
        currIndex--; // Step one back to index last added Fibonacci number
        System.out.println("setUpClass() has generated a list of the first " + fibonacciList.size() + " Fibonacci numbers.");
        System.out.println("Fibonacci(" + currIndex + ") = " + fibonacciList.get(currIndex));
        /* And last but not least, to make what I'm calling, for lack of a 
           better term, "the Heegner companion primes." */
        int absD, currDiff, currCompCand, currSqrIndex, currSqrDMult;
        boolean numNotFoundYet;
        for (int d = 0; d < HEEGNER_NUMBERS.length; d++) {
            absD = (-1) * HEEGNER_NUMBERS[d];
            currIndex = 0;
            do {
                currPrime = primesList.get(currIndex);
                currIndex++;
            } while (currPrime <= absD);
            numNotFoundYet = true;
            while (numNotFoundYet) {
                currCompCand = 4 * currPrime;
                currSqrIndex = 1;
                currDiff = absD;
                while (currDiff > 0) {
                    currSqrDMult = absD * currSqrIndex * currSqrIndex;
                    currDiff = currCompCand - currSqrDMult;
                    if (Math.sqrt(currDiff) == Math.floor(Math.sqrt(currDiff))) {
                        currDiff = 0;
                    }
                    currSqrIndex++;
                }
                if (currDiff < 0) {
                    numNotFoundYet = false;
                } else {
                    currIndex++;
                    currPrime = primesList.get(currIndex);
                }
            }
            HEEGNER_COMPANION_PRIMES[d] = currPrime;
        }
        System.out.println("setUpClass() has generated a list of \"Heegner companion primes\": ");
        for (int dReport = 0; dReport < HEEGNER_NUMBERS.length; dReport++) {
            System.out.print(HEEGNER_COMPANION_PRIMES[dReport] + " for " + HEEGNER_NUMBERS[dReport] + ", ");
        }
        System.out.println();
    }
    
    /**
     * Test of primeFactors method, of class NumberTheoreticFunctionsCalculator.
     * This test uses squares of primorials (4, 36, 900, 44100, etc.) and 
     * certain divisors of those numbers. This test also checks the 
     * factorization of the additive inverses of those numbers (e.g., -44100).
     * The expectation is that if -n is a negative number, its factorization 
     * will be the same as that of n but with a single -1 at the beginning. This
     * test does not set out any expectations for the factorization of 0.
     */
    @Test
    public void testPrimeFactors() {
        System.out.println("primeFactors");
        int num = 1;
        int primeIndex = 0;
        List<Integer> result = new ArrayList<>();
        List<Integer> expResult = new ArrayList<>();
        boolean withinRange = true;
        int lastNumTested = 1;
        while (withinRange) {
            num *= primesList.get(primeIndex);
            expResult.add(primesList.get(primeIndex));
            /* Check the number has not overflown the integer data type and that
            we're not going beyond our finite list of primes */
            withinRange = (num > 0 && num < Integer.MAX_VALUE) && (primeIndex < primesListLength - 1);
            if (withinRange) {
                result = NumberTheoreticFunctionsCalculator.primeFactors(num);
                assertEquals(result, expResult);
                num *= -1; // Make num negative
                expResult.add(0, -1);
                result = NumberTheoreticFunctionsCalculator.primeFactors(num);
                assertEquals(result, expResult);
                lastNumTested = num;
                num *= -1; // And back to positive
                expResult.remove(0);
                // Now to test factorization on the square of a primorial
                num *= primesList.get(primeIndex);
                expResult.add(primesList.get(primeIndex));
                // Checking for integer data type overflow only at this point
                withinRange = (num > 0 && num < Integer.MAX_VALUE);
                if (withinRange) {
                    result = NumberTheoreticFunctionsCalculator.primeFactors(num);
                    assertEquals(result, expResult);
                    num *= -1; // Make num negative
                    expResult.add(0, -1);
                    result = NumberTheoreticFunctionsCalculator.primeFactors(num);
                    assertEquals(result, expResult);
                    lastNumTested = num;
                    num *= -1; // And back to positive
                    expResult.remove(0);
                }
            }
            primeIndex++;
        }
        System.out.print("Last number tested was " + lastNumTested + ", which has this factorization: " + result.get(0));
        for (int i = 1; i < result.size(); i++) {
            System.out.print(" \u00D7 ");
            System.out.print(result.get(i));
        }
        System.out.println(" ");
    }

    /**
     * Test of isPrime method, of class NumberTheoreticFunctionsCalculator. The 
     * numbers listed in Sloane's A000040, as well as those same numbers 
     * multiplied by -1, should all be identified as prime. Likewise, the 
     * numbers listed in Sloane's A018252, as well as those same numbers 
     * multiplied by -1, should all be identified as not prime. As for 0, I'm 
     * not sure; if you like you can uncomment the line for it and perhaps 
     * change assertFalse to assertTrue.
     * TO DO: WRITE TESTS FOR isPrime(ImaginaryQuadraticInteger)
     */
    @Test
    public void testIsPrime() {
        System.out.println("isPrime(int)");
        // assertFalse(NumberTheoreticFunctionsCalculator.isPrime(0));
        for (int i = 0; i < primesListLength; i++) {
            assertTrue(NumberTheoreticFunctionsCalculator.isPrime(primesList.get(i)));
            assertTrue(NumberTheoreticFunctionsCalculator.isPrime(-primesList.get(i)));
        }
        assertFalse(NumberTheoreticFunctionsCalculator.isPrime(1));
        assertFalse(NumberTheoreticFunctionsCalculator.isPrime(-1));
        for (int j = 0; j < compositesList.size(); j++) {
            assertFalse(NumberTheoreticFunctionsCalculator.isPrime(compositesList.get(j)));
            assertFalse(NumberTheoreticFunctionsCalculator.isPrime(-compositesList.get(j)));
        }
        /* Now we're going to test odd integers greater than the last prime 
        in our List but smaller than the square of that prime. */
        int maxNumForTest = primesList.get(primesListLength - 1) * primesList.get(primesListLength - 1);
        int primeIndex = 1; // Which of course corresponds to 3, not 2, in a zero-indexed array
        boolean possiblyPrime = true; // Presume k to be prime until finding otherwise
        for (int k = primesList.get(primesListLength - 1) + 2; k < maxNumForTest; k += 2) {
            while (primeIndex < primesListLength && possiblyPrime) {
                possiblyPrime = (k % primesList.get(primeIndex) != 0);
                primeIndex++;
            }
            if (possiblyPrime) {
                assertTrue(NumberTheoreticFunctionsCalculator.isPrime(k));
                assertTrue(NumberTheoreticFunctionsCalculator.isPrime(-k));
            } else {
                assertFalse(NumberTheoreticFunctionsCalculator.isPrime(k));
                assertFalse(NumberTheoreticFunctionsCalculator.isPrime(-k));
            }
            primeIndex = 1; // Reset for next k
            possiblyPrime = true; // Reset for next k
        }
        /* And lastly, we're going to test indices of Fibonacci primes greater 
           than 4, which corresponds to 3 */
        for  (int m = 5; m < fibonacciList.size(); m++) {
            if (NumberTheoreticFunctionsCalculator.isPrime(fibonacciList.get(m))) {
                assertTrue(NumberTheoreticFunctionsCalculator.isPrime(m));
            }
        }
        System.out.println("isPrime(ImaginaryQuadraticInteger)");
        // That does it for testing isPrime on purely real integers.
        ImaginaryQuadraticRing ufdRing;
        ImaginaryQuadraticInteger numberFromUFD;
        for (int d = 0; d < HEEGNER_NUMBERS.length; d++) {
            ufdRing = new ImaginaryQuadraticRing(HEEGNER_NUMBERS[d]);
            // d should not be prime in the ring of Q(sqrt(d))
            numberFromUFD = new ImaginaryQuadraticInteger(HEEGNER_NUMBERS[d], 0, ufdRing);
            try {
                assertFalse(NumberTheoreticFunctionsCalculator.isPrime(numberFromUFD));
            } catch (NonUniqueFactorizationDomainException nufde) {
                fail("Attempt to call isPrime(" + numberFromUFD.toASCIIString() + ") should not have caused a NonUniqueFactorizationDomainException. " + nufde.getMessage());
            }
            // Nor should d + 4 be prime even if it is prime in Z
            numberFromUFD = new ImaginaryQuadraticInteger(-HEEGNER_NUMBERS[d] + 4, 0, ufdRing);
            try {
                assertFalse(NumberTheoreticFunctionsCalculator.isPrime(numberFromUFD));
            } catch (NonUniqueFactorizationDomainException nufde) {
                fail("Attempt to call isPrime(" + numberFromUFD.toASCIIString() + ") should not have caused a NonUniqueFactorizationDomainException. " + nufde.getMessage());
            }
            // The "Heegner companion primes" should indeed be prime
            numberFromUFD = new ImaginaryQuadraticInteger(HEEGNER_COMPANION_PRIMES[d], 0, ufdRing);
            try {
                assertTrue(NumberTheoreticFunctionsCalculator.isPrime(numberFromUFD));
            } catch (NonUniqueFactorizationDomainException nufde) {
                fail("Attempt to call isPrime(" + numberFromUFD.toASCIIString() + ") should not have caused a NonUniqueFactorizationDomainException. " + nufde.getMessage());
            }
        }
        ImaginaryQuadraticRing Zi5 = new ImaginaryQuadraticRing(-5);
        ImaginaryQuadraticInteger numberFromNonUFD = new ImaginaryQuadraticInteger(7, 0, Zi5);
        try {
            boolean primality = NumberTheoreticFunctionsCalculator.isPrime(numberFromNonUFD);
            fail("Attempt to call isPrime(" + numberFromNonUFD.toASCIIString() + ") should have caused a NonUniqueFactorizationDomainException, not given the result that primality is " + primality + ".");
        } catch (NonUniqueFactorizationDomainException nufde) {
            System.out.println("Calling isPrime(" + numberFromNonUFD.toASCIIString() + ") on number from non-UFD correctly triggered NonUniqueFactorizationDomainException \"" + nufde.getMessage() + "\"");
        }
        ImaginaryQuadraticRing OQi43 = new ImaginaryQuadraticRing(-43);
        ImaginaryQuadraticInteger a43 = new ImaginaryQuadraticInteger(43, 0, OQi43);
        try {
            assertFalse(NumberTheoreticFunctionsCalculator.isPrime(a43));
        } catch (NonUniqueFactorizationDomainException nufde) {
            fail("Calling isPrime(" + a43.toASCIIString() + ") on number from UFD not have triggered NonUniqueFactorizationDomainException \"" + nufde.getMessage() + "\"");
        }
        // May 31, 2018: These will have to do for now. Will write more later.
    }
    
    /**
     * Test of isIrreducible method, of class 
     * NumberTheoreticFunctionsCalculator.
     */
    @Test
    public void testIsIrreducible() {
        System.out.println("isIrreducible");
        ImaginaryQuadraticRing currRing;
        ImaginaryQuadraticInteger currQuadrInt;
        /* The number 1 + sqrt(d) should be irreducible but not prime in each
           domain Z[sqrt(d)] for squarefree negative d = 3 mod 4. But the 
           conjugate of (1 + sqrt(d))^2 should not be. */
        for (int iterDiscr = -5; iterDiscr > -200; iterDiscr -= 4) {
            if (NumberTheoreticFunctionsCalculator.isSquareFree(iterDiscr)) {
                currRing = new ImaginaryQuadraticRing(iterDiscr);
                currQuadrInt = new ImaginaryQuadraticInteger(1, 1, currRing);
                try {
                    assertFalse(NumberTheoreticFunctionsCalculator.isPrime(currQuadrInt));
                } catch (NonUniqueFactorizationDomainException nufde) {
                    fail("NonUniqueFactorizationDomainException should not have happened in this context: " + nufde.getMessage());
                }
                assertTrue(NumberTheoreticFunctionsCalculator.isIrreducible(currQuadrInt));
                try {
                    currQuadrInt = currQuadrInt.times(currQuadrInt); // Squaring currQuadrInt
                } catch (AlgebraicDegreeOverflowException adoe) {
                    fail("AlgebraicDegreeOverflowException should not have happened when multiplying an algebraic integer by itself.");
                }
                currQuadrInt = currQuadrInt.conjugate();
                assertFalse(NumberTheoreticFunctionsCalculator.isIrreducible(currQuadrInt));
            }
        }
    }
    
    /**
     * Test of symbolLegendre method, of class 
     * NumberTheoreticFunctionsCalculator. Per quadratic reciprocity, 
     * Legendre(p, q) = Legendre(q, p) if p and q are both primes and either one 
     * or both of them are congruent to 1 mod 4. But if both are congruent to 3 
     * mod 4, then Legendre(p, q) = -Legendre(q, p). And of course Legendre(p, 
     * p) = 0. Some of this assumes that both p and q are positive. In the case 
     * of Legendre(p, -q) with q being positive, reckon the congruence of q mod 
     * 4 rather than -q.
     * <p>Another property to test for is that Legendre(ab, p) = Legendre(a, p) 
     * Legendre(b, p). That is to say that this is a multiplicative function. So 
     * here this is tested with Legendre(2p, q) = Legendre(2, q) Legendre(p, q).
     * </p>
     * <p>Of course it's entirely possible that in implementing symbolLegendre 
     * the programmer could get dyslexic and produce an implementation that 
     * always gives the wrong result when gcd(a, p) = 1, meaning that it always 
     * returns -1 when it should return 1, and vice-versa, and yet it passes the 
     * tests.</p>
     * <p>For that reason one should not rely only on the identities pertaining 
     * to multiplicativity and quadratic reciprocity. Therefore these tests also 
     * include some computations of actual squares modulo p to check some of the 
     * answers.</p>
     * <p>I chose to use the Fibonacci numbers for this purpose, since they are 
     * already being used in some of the other tests and they contain a good mix 
     * of prime numbers (including the even prime 2) and composite numbers.</p>
     */
    @Test
    public void testLegendreSymbol() {
        System.out.println("symbolLegendre");
        byte expResult, result;
        int p, q;
        // First to test Legendre(Fibonacci(n), p)
        for (int i = 3; i < fibonacciList.size(); i++) {
            for (int j = 1; j < primesListLength; j++) {
                p = primesList.get(j);
                int fiboM = fibonacciList.get(i) % p;
                if (fiboM == 0) {
                    expResult = 0;
                } else {
                    int halfPmark = (p + 1)/2;
                    int[] modSquares = new int[halfPmark];
                    boolean noSolutionFound = true;
                    int currModSqIndex = 0;
                    for (int n = 0; n < halfPmark; n++) {
                        modSquares[n] = (n * n) % p;
                    }
                    while (noSolutionFound && (currModSqIndex < halfPmark)) {
                        noSolutionFound = !(fiboM == modSquares[currModSqIndex]);
                        currModSqIndex++;
                    }
                    if (noSolutionFound) {
                        expResult = -1;
                    } else {
                        expResult = 1;
                    }
                }
                result = NumberTheoreticFunctionsCalculator.symbolLegendre(fibonacciList.get(i), p);
                assertEquals(expResult, result);
            }
        }
        // Now to test with both p and q being odd primes
        for (int pindex = 1; pindex < primesListLength; pindex++) {
            p = primesList.get(pindex);
            expResult = 0;
            result = NumberTheoreticFunctionsCalculator.symbolLegendre(p, p);
            assertEquals(expResult, result);
            for (int qindex = pindex + 1; qindex < primesListLength; qindex++) {
                q = primesList.get(qindex);
                expResult = NumberTheoreticFunctionsCalculator.symbolLegendre(q, p);
                if (p % 4 == 3 && q % 4 == 3) {
                    expResult *= -1;
                }
                result = NumberTheoreticFunctionsCalculator.symbolLegendre(p, q);
                assertEquals(expResult, result);
                result = NumberTheoreticFunctionsCalculator.symbolLegendre(p, -q);
                assertEquals(expResult, result);
                /* And lastly, to test that Legendre(2p, q) = Legendre(2, q) 
                   Legendre(p, q). */
                expResult = NumberTheoreticFunctionsCalculator.symbolLegendre(2, q);
                expResult *= NumberTheoreticFunctionsCalculator.symbolLegendre(p, q);
                result = NumberTheoreticFunctionsCalculator.symbolLegendre(2 * p, q);
                assertEquals(expResult, result);
            }
        }
        // And lastly to check for exceptions for bad arguments.
        try {
            byte attempt = NumberTheoreticFunctionsCalculator.symbolLegendre(7, 2);
            fail("Calling Legendre(7, 2) should have triggered an exception, not given result " + attempt + ".");
        } catch (IllegalArgumentException iae) {
            System.out.println("Calling Legendre(7, 2) correctly triggered IllegalArgumentException. " + iae.getMessage());
        }
    }

    /**
     * Test of symbolJacobi method, of class NumberTheoreticFunctionsCalculator. 
     * First, it checks that Legendre(a, p) = Jacobi(a, p), where p is an odd 
     * prime. Next, it checks that Jacobi(n, pq) = Legendre(n, p) Legendre(n, 
     * q). If the Legendre symbol test fails, then the result of this test is 
     * meaningless.
     */
    @Test
    public void testJacobiSymbol() {
        System.out.println("symbolJacobi");
        byte expResult, result;
        for (int i = 1; i < primesListLength; i++) {
            for (int a = 2; a < 100; a++) {
                expResult = NumberTheoreticFunctionsCalculator.symbolLegendre(a, primesList.get(i));
                result = NumberTheoreticFunctionsCalculator.symbolJacobi(a, primesList.get(i));
                assertEquals(expResult, result);
            }
        }
        int p, q, m;
        for (int pindex = 1; pindex < primesListLength; pindex++) {
            p = primesList.get(pindex);
            for (int qindex = pindex + 1; qindex < primesListLength; qindex++) {
                q = primesList.get(qindex);
                m = p * q;
                for (int n = 2; n < 100; n++) {
                    expResult = NumberTheoreticFunctionsCalculator.symbolLegendre(n, p);
                    expResult *= NumberTheoreticFunctionsCalculator.symbolLegendre(n, q);
                    result = NumberTheoreticFunctionsCalculator.symbolJacobi(n, m);
                    assertEquals(expResult, result);
                }
            }
        }
    }

    // This is nowhere near a complete test, but it should be enought to give a 
    // failing first test, per the test-driven development methodology.
    /**
     * Test of symbolKronecker method, of class 
     * NumberTheoreticFunctionsCalculator.
     */
    @Test
    public void testKroneckerSymbol() {
        System.out.println("symbolKronecker");
        byte expResult, result;
        for (int n = 1; n < 50; n++) {
            expResult = -1;
            result = NumberTheoreticFunctionsCalculator.symbolKronecker(-n, -1);
            assertEquals(expResult, result);
            expResult = 1;
            result = NumberTheoreticFunctionsCalculator.symbolKronecker(n, -1);
            assertEquals(expResult, result);
        }
        for (int m = 0; m < 50; m += 8) {
            expResult = -1;
            result = NumberTheoreticFunctionsCalculator.symbolKronecker(m + 3, 2);
            assertEquals(expResult, result);
            result = NumberTheoreticFunctionsCalculator.symbolKronecker(m + 5, 2);
            assertEquals(expResult, result);
            result = NumberTheoreticFunctionsCalculator.symbolKronecker(m + 1, -2);
            assertEquals(expResult, result);
            result = NumberTheoreticFunctionsCalculator.symbolKronecker(m + 7, -2);
            assertEquals(expResult, result);
            expResult = 0;
            result = NumberTheoreticFunctionsCalculator.symbolKronecker(m, 2);
            assertEquals(expResult, result);
            result = NumberTheoreticFunctionsCalculator.symbolKronecker(m + 2, 2);
            assertEquals(expResult, result);
            result = NumberTheoreticFunctionsCalculator.symbolKronecker(m + 4, 2);
            assertEquals(expResult, result);
            result = NumberTheoreticFunctionsCalculator.symbolKronecker(m + 6, 2);
            assertEquals(expResult, result);
            expResult = 1;
            result = NumberTheoreticFunctionsCalculator.symbolKronecker(m + 1, 2);
            assertEquals(expResult, result);
            result = NumberTheoreticFunctionsCalculator.symbolKronecker(m + 7, 2);
            assertEquals(expResult, result);
            result = NumberTheoreticFunctionsCalculator.symbolKronecker(m + 3, -2);
            assertEquals(expResult, result);
            result = NumberTheoreticFunctionsCalculator.symbolKronecker(m + 5, -2);
            assertEquals(expResult, result);
        }
    }
    
    /**
     * Test of isSquareFree method, of class NumberTheoreticFunctionsCalculator.
     */
    @Test
    public void testIsSquareFree() {
        System.out.println("isSquareFree");
        int num;
        for (int i = 0; i < primesListLength - 1; i++) {
            num = primesList.get(i) * primesList.get(i + 1); // A squarefree semiprime, pq
            assertTrue(NumberTheoreticFunctionsCalculator.isSquareFree(num));
            num *= primesList.get(i); // Repeat one prime factor, (p^2)q
            assertFalse(NumberTheoreticFunctionsCalculator.isSquareFree(num));
        }
    }

    /**
     * Test of moebiusMu method, of class NumberTheoreticFunctionsCalculator.
     * I expect that mu(-n) = mu(n), so this test checks for that. If you wish 
     * to limit the test to positive integers, you can just remove some of the 
     * assertions.
     */
    @Test
    public void testMoebiusMu() {
        System.out.println("moebiusMu");
        byte expResult = -1;
        byte result;
        // The primes p should all have mu(p) = -1
        for (int i = 0; i < primesListLength; i++) {
            result = NumberTheoreticFunctionsCalculator.moebiusMu(primesList.get(i));
            assertEquals(expResult, result);
            assertEquals(result, NumberTheoreticFunctionsCalculator.moebiusMu(-primesList.get(i)));
        }
        // Now to test mu(n) = 0 with n being a multiple of 4
        expResult = 0;
        for (int j = 0; j < 97; j += 4) {
            result = NumberTheoreticFunctionsCalculator.moebiusMu(j);
            assertEquals(expResult, result);
            assertEquals(result, NumberTheoreticFunctionsCalculator.moebiusMu(-j));
        }
        // And lastly, the products of two distinct primes p and q should give mu(pq) = 1
        expResult = 1;
        int num;
        for (int k = 0; k < primesListLength - 1; k++) {
            num = primesList.get(k) * primesList.get(k + 1);
            result = NumberTheoreticFunctionsCalculator.moebiusMu(num);
            assertEquals(expResult, result);
            assertEquals(result, NumberTheoreticFunctionsCalculator.moebiusMu(-num));
        }
    }

    /**
     * Test of euclideanGCD method, of class NumberTheoreticFunctionsCalculator.
     * At this time, I choose not to test the case gcd(0, 0). The value of such 
     * a test would be philosophical rather than practical.
     */
    @Test
    public void testEuclideanGCD() {
        System.out.println("euclideanGCD");
        int result;
        int expResult = 1; /* Going to test with consecutive integers, expect 
                              the result to be 1 each time */
        for (int i = -30; i < 31; i++) {
            result = NumberTheoreticFunctionsCalculator.euclideanGCD(i, i + 1);
            assertEquals(expResult, result);
        }
        /* Now test with consecutive odd numbers, result should also be 1 each 
           time as well */
        for (int j = -29; j < 31; j += 2) {
            result = NumberTheoreticFunctionsCalculator.euclideanGCD(j, j + 2);
            assertEquals(expResult, result);
        }
        /* And now consecutive Fibonacci numbers before moving on to even 
           numbers. This will probably be the longest part of the test. */
        for (int k = 1; k < fibonacciList.size(); k++) {
            result = NumberTheoreticFunctionsCalculator.euclideanGCD(fibonacciList.get(k - 1), fibonacciList.get(k));
            assertEquals(expResult, result);
        }
        expResult = 2; /* Now to test with consecutive even integers, result 
                          should be 2 each time */
        for (int i = -30; i < 31; i += 2) {
            result = NumberTheoreticFunctionsCalculator.euclideanGCD(i, i + 2);
            assertEquals(expResult, result);
        }
        // And now some of the same tests again but with the long data type
        long expResultLong = 1;
        long resultLong;
        for (long j = (long) Integer.MAX_VALUE; j < ((long) Integer.MAX_VALUE + 32); j++) {
            resultLong = NumberTheoreticFunctionsCalculator.euclideanGCD(j, j + 1);
            assertEquals(expResultLong, resultLong);
        }
        expResultLong = 2;
        for (long k = ((long) Integer.MAX_VALUE - 1); k < ((long) Integer.MAX_VALUE + 32); k += 2) {
            resultLong = NumberTheoreticFunctionsCalculator.euclideanGCD(k, k + 2);
            assertEquals(expResultLong, resultLong);
        }
        /* TO DO: Write more tests for euclideanGCD(IQI, IQI). */
        /* Last but not least, euclideanGCD(ImaginaryQuadraticInteger, 
           ImaginaryQuadraticInteger). */
        ImaginaryQuadraticRing r = new ImaginaryQuadraticRing(-1);
        ImaginaryQuadraticInteger iqia = new ImaginaryQuadraticInteger(-2, 2, r);
        ImaginaryQuadraticInteger iqib = new ImaginaryQuadraticInteger(-2, 4, r);
        ImaginaryQuadraticInteger expResultIQI = new ImaginaryQuadraticInteger(0, 2, r);
        ImaginaryQuadraticInteger resultIQI;
        try {
            resultIQI = NumberTheoreticFunctionsCalculator.euclideanGCD(iqia, iqib);
        } catch (AlgebraicDegreeOverflowException adoe) {
            resultIQI = iqia; // Just to avoid "may not have been initialized" warning
            fail("Attempting to calculate gcd(" + iqia.toASCIIString() + ", " + iqib.toASCIIString() + ") should not have triggered AlgebraicDegreeOverflowException" + adoe.getMessage());
        } catch (NonEuclideanDomainException nde) {
            resultIQI = iqib; // Same reason as previous
            fail("Attempting to calculate gcd(" + iqia.toASCIIString() + ", " + iqib.toASCIIString() + ") should not have triggered NonEuclideanDomainException" + nde.getMessage());
        }
        assertEquals(expResultIQI, resultIQI);
        // Now to check the appropriate exceptions are thrown
        r = new ImaginaryQuadraticRing(-2);
        iqib = new ImaginaryQuadraticInteger(-2, 4, r);
        try {
            resultIQI = NumberTheoreticFunctionsCalculator.euclideanGCD(iqia, iqib);
            fail("Attempting to calculate gcd(" + iqia.toASCIIString() + ", " + iqib.toASCIIString() + ") should have triggered AlgebraicDegreeOverflowException, not given result " + resultIQI.toASCIIString());
        } catch (AlgebraicDegreeOverflowException adoe) {
            System.out.println("Attempting to calculate gcd(" + iqia.toASCIIString() + ", " + iqib.toASCIIString() + ") correctly triggered AlgebraicDegreeOverflowException " + adoe.getMessage());
        } catch (NonEuclideanDomainException nde) {
            fail("Attempting to calculate gcd(" + iqia.toASCIIString() + ", " + iqib.toASCIIString() + ") should not have triggered NonEuclideanDomainException " + nde.getMessage());
        }
        r = new ImaginaryQuadraticRing(-5);
        iqia = new ImaginaryQuadraticInteger(-2, 2, r);
        iqib = new ImaginaryQuadraticInteger(-2, 4, r);
        try {
            resultIQI = NumberTheoreticFunctionsCalculator.euclideanGCD(iqia, iqib);
            fail("Attempting to calculate gcd(" + iqia.toASCIIString() + ", " + iqib.toASCIIString() + ") should have triggered NonEuclideanDomainException, not given result " + resultIQI.toASCIIString());
        } catch (AlgebraicDegreeOverflowException adoe) {
            fail("Attempting to calculate gcd(" + iqia.toASCIIString() + ", " + iqib.toASCIIString() + ") should not have triggered AlgebraicDegreeOverflowException " + adoe.getMessage());
        } catch (NonEuclideanDomainException nde) {
            System.out.println("Attempting to calculate gcd(" + iqia.toASCIIString() + ", " + iqib.toASCIIString() + ") correctly triggered NonEuclideanDomainException " + nde.getMessage());
        }
    }
    
    /**
     * Test of randomNegativeSquarefreeNumber method, of class 
     * NumberTheoreticFunctionsCalculator.
     */
    @Test
    public void testRandomNegativeSquarefreeNumber() {
        System.out.println("randomNegativeSquarefreeNumber");
        // Our test bound will be the square of the largest prime in our finite list
        int testBound = primesList.get(primesListLength - 1) * primesList.get(primesListLength - 1);
        int potentialNegRanSqFreeNum = NumberTheoreticFunctionsCalculator.randomNegativeSquarefreeNumber(testBound);
        System.out.println("Function came up with this pseudorandom number: " + potentialNegRanSqFreeNum);
        // Check that the pseudorandom number is indeed squarefree
        double squaredPrime, ranNumDiv, flooredRanNumDiv;
        for (int i = 0; i < primesListLength; i++) {
            squaredPrime = primesList.get(i) * primesList.get(i);
            ranNumDiv = potentialNegRanSqFreeNum/squaredPrime;
            flooredRanNumDiv = (int) Math.floor(ranNumDiv);
            assertFalse(ranNumDiv == flooredRanNumDiv);
        }
        /* And lastly, check that it is at least the negated test bound but not 
        more than or equal to 0. */
        assertFalse(potentialNegRanSqFreeNum < (-1) * testBound);
        assertTrue(potentialNegRanSqFreeNum < 0);
    }
    
}