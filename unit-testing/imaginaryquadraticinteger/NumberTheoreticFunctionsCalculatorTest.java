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
        halfThreshold = threshold/2;
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
            } while (currIndex < (halfThreshold - 1) && !primeFlags[currIndex]);
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
        for (int d = 0; d < NumberTheoreticFunctionsCalculator.HEEGNER_NUMBERS.length; d++) {
            absD = (-1) * NumberTheoreticFunctionsCalculator.HEEGNER_NUMBERS[d];
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
        for (int dReport = 0; dReport < NumberTheoreticFunctionsCalculator.HEEGNER_NUMBERS.length; dReport++) {
            System.out.print(HEEGNER_COMPANION_PRIMES[dReport] + " for " + NumberTheoreticFunctionsCalculator.HEEGNER_NUMBERS[dReport] + ", ");
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
     * <p>As for testing primeFactors(ImaginaryQuadraticInteger), some purely 
     * real primes of <b>Z</b> are tested in the context of the imaginary 
     * quadratic rings that are unique factorization domains, but just that 
     * their lists of factors in the particular ring have one or two factors. 
     * This is followed by a more comprehensive test in which a few small 
     * complex primes are multiplied together and the expected and actual factor 
     * lists are compared. I believe that particular test does not prescribe a 
     * particular order, though I am assuming java.util.List.containsAll() does 
     * not require the order to be the same.</p>
     * <p>However, if an order is required, I suggest this one: first a unit (if 
     * not 1), followed by primes in order by norm, and a prime with negative 
     * imaginary part should precede a prime with positive imaginary part of the 
     * same norm, e.g., 1 - i precedes 1 + i.</p>
     */
    @Test
    public void testPrimeFactors() {
        System.out.println("primeFactors(int)");
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
        System.out.print("Last integer in Z tested was " + lastNumTested + ", which has this factorization: " + result.get(0));
        for (int i = 1; i < result.size(); i++) {
            System.out.print(" \u00D7 ");
            System.out.print(result.get(i));
        }
        System.out.println(" ");
        // Now to test primeFactors() on imaginary quadratic integers
        System.out.println("primeFactors(ImaginaryQuadraticInteger)");
        ImaginaryQuadraticRing r;
        ImaginaryQuadraticInteger z;
        List<ImaginaryQuadraticInteger> factorsList;
        int facLen;
        String assertionMessage;
        for (Integer d : NumberTheoreticFunctionsCalculator.HEEGNER_NUMBERS) {
            r = new ImaginaryQuadraticRing(d);
            /* First to test purely real integers that are prime in Z in the 
               context of a particular ring r, e.g., 5 in Z[sqrt(-2)] */
            for (Integer p : primesList) {
                z = new ImaginaryQuadraticInteger(p, 0, r);
                try {
                    factorsList = NumberTheoreticFunctionsCalculator.primeFactors(z);
                    facLen = factorsList.size();
                } catch (NonUniqueFactorizationDomainException nufde) {
                    facLen = 0;
                    fail("NonUniqueFactorizationDomainException should not have happened in this context: " + nufde.getMessage());
                }
                if (NumberTheoreticFunctionsCalculator.isPrime(z)) {
                    assertionMessage = "Factor list of " + z.toString() + " in " + z.getRing().toString() + " should contain just one prime factor.";
                    assertEquals(assertionMessage, 1, facLen);
                } else {
                    assertionMessage = "Factor list of " + z.toString() + " in " + z.getRing().toString() + " should contain two or three factors.";
                    assertTrue(assertionMessage, facLen > 1);
                }
            }
            // Lastly, to look at some consecutive algebraic integers
            int expFacLen;
            for (int a = -4; a < 5; a++) {
                for (int b = 3; b > -1; b--) {
                    z = new ImaginaryQuadraticInteger(a, b, r);
                    try {
                        factorsList = NumberTheoreticFunctionsCalculator.primeFactors(z);
                        facLen = factorsList.size();
                        expFacLen = 0;
                        if ((factorsList.get(0).norm() == 1) && (factorsList.size() > 1)) {
                            expFacLen = 1;
                        }
                        if (z.norm() > 1) {
                            expFacLen++;
                        }
                        if (NumberTheoreticFunctionsCalculator.isPrime(z)) {
                            assertionMessage = z.toString() + " is expected to have " + expFacLen + " factor(s).";
                            assertEquals(assertionMessage, expFacLen, facLen);
                        } else {
                            expFacLen++;
                            assertionMessage = z.toString() + " is expected to have at least " + expFacLen + " factors.";
                            assertTrue(assertionMessage, facLen >= expFacLen);
                        }
                    } catch (NonUniqueFactorizationDomainException nufde) {
                        fail("NonUniqueFactorizationDomainException should not have happened in this context: " + nufde.getMessage());
                    }
                }
            }
        }
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
        for (Integer compositeNum : compositesList) {
            assertFalse(NumberTheoreticFunctionsCalculator.isPrime(compositeNum));
            assertFalse(NumberTheoreticFunctionsCalculator.isPrime(-compositeNum));
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
        /* One more thing before moving on to complex UFDs: testing 
           isPrime(long) */
        long longNum = Integer.MAX_VALUE;
        String assertionMessage = "2^31 - 1 should be found to be prime.";
        assertTrue(assertionMessage, NumberTheoreticFunctionsCalculator.isPrime(longNum));
        longNum++;
        assertionMessage = "2^31 should not be found to be prime.";
        assertFalse(assertionMessage, NumberTheoreticFunctionsCalculator.isPrime(longNum));
        longNum += 11;
        assertionMessage = "2^31 + 11 should be found to be prime.";
        assertTrue(assertionMessage, NumberTheoreticFunctionsCalculator.isPrime(longNum));
        int castNum = (int) longNum;
        assertionMessage = "-(2^31) + 11 should not be found to be prime.";
        assertFalse(assertionMessage, NumberTheoreticFunctionsCalculator.isPrime(castNum));
        // That does it for testing isPrime in the context of Z.
        System.out.println("isPrime(ImaginaryQuadraticInteger)");
        ImaginaryQuadraticRing ufdRing;
        ImaginaryQuadraticInteger numberFromUFD;
        for (int d = 0; d < NumberTheoreticFunctionsCalculator.HEEGNER_NUMBERS.length; d++) {
            ufdRing = new ImaginaryQuadraticRing(NumberTheoreticFunctionsCalculator.HEEGNER_NUMBERS[d]);
            // d should not be prime in the ring of Q(sqrt(d))
            numberFromUFD = new ImaginaryQuadraticInteger(NumberTheoreticFunctionsCalculator.HEEGNER_NUMBERS[d], 0, ufdRing);
            assertFalse(NumberTheoreticFunctionsCalculator.isPrime(numberFromUFD));
            // Nor should d + 4 be prime even if it is prime in Z
            numberFromUFD = new ImaginaryQuadraticInteger(-NumberTheoreticFunctionsCalculator.HEEGNER_NUMBERS[d] + 4, 0, ufdRing);
            assertFalse(NumberTheoreticFunctionsCalculator.isPrime(numberFromUFD));
            // The "Heegner companion primes" should indeed be prime
            numberFromUFD = new ImaginaryQuadraticInteger(HEEGNER_COMPANION_PRIMES[d], 0, ufdRing);
            assertionMessage = numberFromUFD.toString() + " should have been identified as prime in " + ufdRing.toString();
            assertTrue(assertionMessage, NumberTheoreticFunctionsCalculator.isPrime(numberFromUFD));
        }
        // There are some special cases to test in the Gaussian integers
        ImaginaryQuadraticInteger gaussianInteger;
        ImaginaryQuadraticInteger twoStepImagIncr = new ImaginaryQuadraticInteger(0, 2, NumberTheoreticFunctionsCalculator.RING_GAUSSIAN);
        for (int g = 3; g < 256; g += 4) {
            if (NumberTheoreticFunctionsCalculator.isPrime(g)) {
                gaussianInteger = new ImaginaryQuadraticInteger(0, g, NumberTheoreticFunctionsCalculator.RING_GAUSSIAN);
                assertionMessage = gaussianInteger.toString() + " should have been identified as prime in Z[i]";
                assertTrue(assertionMessage, NumberTheoreticFunctionsCalculator.isPrime(gaussianInteger));
                gaussianInteger = gaussianInteger.plus(twoStepImagIncr);
                assertionMessage = gaussianInteger.toString() + " should not have been identified as prime in Z[i]";
                assertFalse(assertionMessage, NumberTheoreticFunctionsCalculator.isPrime(gaussianInteger));
            }
        }
        /* There are also special cases in the Eisenstein integers, thanks to 
           the units. If p is a purely real integer that is prime among the 
           Eisenstein integers, then omega * p, omega^2 * p, -omega * p and 
           -omega^2 * p should all be prime also. */
        ImaginaryQuadraticInteger eisensteinInteger;
        for (int eisen = -29; eisen < 30; eisen++) {
            eisensteinInteger = new ImaginaryQuadraticInteger(eisen, 0, NumberTheoreticFunctionsCalculator.RING_EISENSTEIN);
            if (NumberTheoreticFunctionsCalculator.isPrime(eisensteinInteger)) {
                eisensteinInteger = eisensteinInteger.times(NumberTheoreticFunctionsCalculator.COMPLEX_CUBIC_ROOT_OF_UNITY);
                assertionMessage = eisensteinInteger.toString() + " should be found to be prime.";
                assertTrue(assertionMessage, NumberTheoreticFunctionsCalculator.isPrime(eisensteinInteger));
                eisensteinInteger = eisensteinInteger.times(NumberTheoreticFunctionsCalculator.COMPLEX_CUBIC_ROOT_OF_UNITY);
                assertionMessage = eisensteinInteger.toString() + " should be found to be prime.";
                assertTrue(assertionMessage, NumberTheoreticFunctionsCalculator.isPrime(eisensteinInteger));
                eisensteinInteger = eisensteinInteger.times(NumberTheoreticFunctionsCalculator.COMPLEX_CUBIC_ROOT_OF_UNITY);
                eisensteinInteger = eisensteinInteger.times(-1); // This should bring us to -eisen
                assertionMessage = eisensteinInteger.toString() + " should be found to be prime.";
                assertTrue(assertionMessage, NumberTheoreticFunctionsCalculator.isPrime(eisensteinInteger));
                eisensteinInteger = eisensteinInteger.times(NumberTheoreticFunctionsCalculator.COMPLEX_CUBIC_ROOT_OF_UNITY);
                assertionMessage = eisensteinInteger.toString() + " should be found to be prime.";
                assertTrue(assertionMessage, NumberTheoreticFunctionsCalculator.isPrime(eisensteinInteger));
                eisensteinInteger = eisensteinInteger.times(NumberTheoreticFunctionsCalculator.COMPLEX_CUBIC_ROOT_OF_UNITY);
                assertionMessage = eisensteinInteger.toString() + " should be found to be prime.";
                assertTrue(assertionMessage, NumberTheoreticFunctionsCalculator.isPrime(eisensteinInteger));
            }
        }
        // Now to test some complex numbers in Z[sqrt(-5)] and O_Q(sqrt(-31))
        ImaginaryQuadraticRing Zi5 = new ImaginaryQuadraticRing(-5);
        ImaginaryQuadraticRing OQi31 = new ImaginaryQuadraticRing(-31);
        ImaginaryQuadraticInteger numberFromNonUFD;
        int norm;
        for (int a = -1; a > -10; a--) {
            for (int b = 1; b < 10; b++) {
                numberFromNonUFD = new ImaginaryQuadraticInteger(a, b, Zi5);
                norm = a * a + 5 * b * b;
                if (NumberTheoreticFunctionsCalculator.isPrime(norm)) {
                    assertionMessage = numberFromNonUFD.toString() + " should have been identified as prime in " + Zi5.toString();
                    assertTrue(assertionMessage, NumberTheoreticFunctionsCalculator.isPrime(numberFromNonUFD));
                } else {
                    assertionMessage = numberFromNonUFD.toString() + " should not have been identified as prime in " + Zi5.toString();
                    assertFalse(assertionMessage, NumberTheoreticFunctionsCalculator.isPrime(numberFromNonUFD));
                }
                numberFromNonUFD = new ImaginaryQuadraticInteger(a, b, OQi31);
                norm = a * a + 31 * b * b;
                if (NumberTheoreticFunctionsCalculator.isPrime(norm)) {
                    assertionMessage = numberFromNonUFD.toString() + " should have been identified as prime in " + OQi31.toString();
                    assertTrue(assertionMessage, NumberTheoreticFunctionsCalculator.isPrime(numberFromNonUFD));
                } else {
                    assertionMessage = numberFromNonUFD.toString() + " should not have been identified as prime in " + OQi31.toString();
                    assertFalse(assertionMessage, NumberTheoreticFunctionsCalculator.isPrime(numberFromNonUFD));
                }
            }
        }
        /* And lastly, to check some purely real integers in the context of a 
           few non-UFDs */
        ImaginaryQuadraticRing r;
        int re;
        ImaginaryQuadraticInteger z;
        for (int iterDiscr = -6; iterDiscr > -200; iterDiscr--) {
            if (NumberTheoreticFunctionsCalculator.isSquareFree(iterDiscr)) {
                r = new ImaginaryQuadraticRing(iterDiscr);
                re = -iterDiscr + 1;
                z = new ImaginaryQuadraticInteger(re, 0, r);
                assertionMessage = re + " in " + r.toString() + " should not have been identified as prime.";
                assertFalse(assertionMessage, NumberTheoreticFunctionsCalculator.isPrime(z));
//                re += 2;
//                z = z.plus(2);
//                if (primesList.contains(re)) {
//                    assertionMessage = FIGURE IT OUT IN THE MORNING
//                }
            }
        }
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
        String assertionMessage;
        /* The number 1 + sqrt(d) should be irreducible but not prime in each
           domain Z[sqrt(d)] for squarefree negative d = 3 mod 4. But (1 + 
           sqrt(d))^2 should not be, nor the conjugate of that number. */
        for (int iterDiscr = -5; iterDiscr > -200; iterDiscr -= 4) {
            if (NumberTheoreticFunctionsCalculator.isSquareFree(iterDiscr)) {
                currRing = new ImaginaryQuadraticRing(iterDiscr);
                currQuadrInt = new ImaginaryQuadraticInteger(1, 1, currRing);
                assertionMessage = currQuadrInt.toASCIIString() + " should have been found to not be prime.";
                assertFalse(assertionMessage, NumberTheoreticFunctionsCalculator.isPrime(currQuadrInt));
                assertionMessage = assertionMessage + "\nBut it should have been found to be irreducible.";
                assertTrue(assertionMessage, NumberTheoreticFunctionsCalculator.isIrreducible(currQuadrInt));
                try {
                    currQuadrInt = currQuadrInt.times(currQuadrInt); // Squaring currQuadrInt
                } catch (AlgebraicDegreeOverflowException adoe) {
                    fail("AlgebraicDegreeOverflowException should not have happened when multiplying an algebraic integer by itself: " + adoe.getMessage());
                }
                assertionMessage = currQuadrInt.toASCIIString() + " should not have been found to be irreducible.";
                assertFalse(assertionMessage, NumberTheoreticFunctionsCalculator.isIrreducible(currQuadrInt));
                currQuadrInt = currQuadrInt.conjugate();
                assertionMessage = currQuadrInt.toASCIIString() + " should not have been found to be irreducible.";
                assertFalse(assertionMessage, NumberTheoreticFunctionsCalculator.isIrreducible(currQuadrInt));
            }
        }
        for (int iterDiscrOQ = -7; iterDiscrOQ > -200; iterDiscrOQ -= 4) {
            if (NumberTheoreticFunctionsCalculator.isSquareFree(iterDiscrOQ)) {
                currRing = new ImaginaryQuadraticRing(iterDiscrOQ);
                currQuadrInt = new ImaginaryQuadraticInteger(1, 1, currRing, 2);
                assertionMessage = currQuadrInt.toASCIIString() + " should have been found to be irreducible.";
                assertTrue(assertionMessage, NumberTheoreticFunctionsCalculator.isIrreducible(currQuadrInt));
                try {
                    currQuadrInt = currQuadrInt.times(currQuadrInt);
                } catch (AlgebraicDegreeOverflowException adoe) {
                    fail("AlgebraicDegreeOverflowException should not have happened when multiplying an algebraic integer by itself: " + adoe.getMessage());
                }
                assertionMessage = currQuadrInt.toASCIIString() + " should not have been found to be irreducible.";
                assertFalse(assertionMessage, NumberTheoreticFunctionsCalculator.isIrreducible(currQuadrInt));
                currQuadrInt = new ImaginaryQuadraticInteger(3, 1, currRing, 2);
                currQuadrInt = currQuadrInt.times(currQuadrInt);
                assertionMessage = currQuadrInt.toASCIIString() + " should not have been found to be irreducible.";
                assertFalse(assertionMessage, NumberTheoreticFunctionsCalculator.isIrreducible(currQuadrInt));
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
     * q). If the Legendre symbol test fails, the result of this test is 
     * meaningless. Then follows the actual business of checking Jacobi(n, m).
     */
    @Test
    public void testJacobiSymbol() {
        System.out.println("symbolJacobi");
        System.out.println("Checking overlap with Legendre symbol...");
        byte expResult, result;
        for (int i = 1; i < primesListLength; i++) {
            for (int a = 5; a < 13; a++) {
                expResult = NumberTheoreticFunctionsCalculator.symbolLegendre(a, primesList.get(i));
                result = NumberTheoreticFunctionsCalculator.symbolJacobi(a, primesList.get(i));
                assertEquals(expResult, result);
            }
        }
        System.out.println("Now checking Jacobi symbol per se...");
        int p, q, m;
        for (int pindex = 1; pindex < primesListLength; pindex++) {
            p = primesList.get(pindex);
            for (int qindex = pindex + 1; qindex < primesListLength; qindex++) {
                q = primesList.get(qindex);
                m = p * q;
                for (int n = 15; n < 20; n++) {
                    expResult = NumberTheoreticFunctionsCalculator.symbolLegendre(n, p);
                    expResult *= NumberTheoreticFunctionsCalculator.symbolLegendre(n, q);
                    result = NumberTheoreticFunctionsCalculator.symbolJacobi(n, m);
                    assertEquals(expResult, result);
                }
            }
        }
        // And lastly to check for exceptions for bad arguments.
        try {
            byte attempt = NumberTheoreticFunctionsCalculator.symbolJacobi(7, 2);
            fail("Calling Jacobi(7, 2) should have triggered an exception, not given result " + attempt + ".");
        } catch (IllegalArgumentException iae) {
            System.out.println("Calling Jacobi(7, 2) correctly triggered IllegalArgumentException. " + iae.getMessage());
        }
    }

    /**
     * Test of symbolKronecker method, of class 
     * NumberTheoreticFunctionsCalculator. First, it checks that Legendre(a, p) 
     * = Kronecker(a, p), where p is an odd prime. Next, it checks that 
     * Jacobi(n, m) = Kronecker(n, m). If either the Legendre symbol test or the 
     * Jacobi symbol test fails, the result of this test is meaningless. Then 
     * follows the actual business of checking Kronecker(n, -2), Kronecker(n, 
     * -1) and Kronecker(n, 2). On another occasion I might add a few 
     * multiplicative tests.
     */
    @Test
    public void testKroneckerSymbol() {
        System.out.println("symbolKronecker");
        byte expResult, result;
        System.out.println("Checking overlap with Legendre symbol...");
        for (int i = 1; i < primesListLength; i++) {
            for (int a = 7; a < 11; a++) {
                expResult = NumberTheoreticFunctionsCalculator.symbolLegendre(a, primesList.get(i));
                result = NumberTheoreticFunctionsCalculator.symbolKronecker(a, primesList.get(i));
                assertEquals(expResult, result);
            }
        }
        System.out.println("Checking overlap with Jacobi symbol...");
        for (int j = -10; j < 10; j++) {
            for (int b = 5; b < 15; b += 2) {
                expResult = NumberTheoreticFunctionsCalculator.symbolJacobi(j, b);
                result = NumberTheoreticFunctionsCalculator.symbolKronecker(j, b);
                assertEquals(expResult, result);
            }
        }
        System.out.println("Now checking Kronecker symbol per se...");
        for (int n = 1; n < 50; n++) {
            expResult = -1;
            result = NumberTheoreticFunctionsCalculator.symbolKronecker(-n, -1);
            assertEquals(expResult, result);
            expResult = 1;
            result = NumberTheoreticFunctionsCalculator.symbolKronecker(n, -1);
            assertEquals(expResult, result);
        }
        for (int m = -24; m < 25; m += 8) {
            expResult = -1;
            result = NumberTheoreticFunctionsCalculator.symbolKronecker(m + 3, 2);
            assertEquals(expResult, result);
            result = NumberTheoreticFunctionsCalculator.symbolKronecker(m + 5, 2);
            assertEquals(expResult, result);
            if (m < 0) {
                result = NumberTheoreticFunctionsCalculator.symbolKronecker(m + 1, -2);
                assertEquals(expResult, result);
                result = NumberTheoreticFunctionsCalculator.symbolKronecker(m + 7, -2);
                assertEquals(expResult, result);
            } else {
                result = NumberTheoreticFunctionsCalculator.symbolKronecker(m + 3, -2);
                assertEquals(expResult, result);
                result = NumberTheoreticFunctionsCalculator.symbolKronecker(m + 5, -2);
                assertEquals(expResult, result);
            }
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
            if (m < 0) {
                result = NumberTheoreticFunctionsCalculator.symbolKronecker(m + 3, -2);
                assertEquals(expResult, result);
                result = NumberTheoreticFunctionsCalculator.symbolKronecker(m + 5, -2);
                assertEquals(expResult, result);
            } else {
                result = NumberTheoreticFunctionsCalculator.symbolKronecker(m + 1, -2);
                assertEquals(expResult, result);
                result = NumberTheoreticFunctionsCalculator.symbolKronecker(m + 7, -2);
                assertEquals(expResult, result);
            }
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
        for (int m = -30; m < 31; m += 2) {
            result = NumberTheoreticFunctionsCalculator.euclideanGCD(m, m + 2);
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
        /* TO DO: Write tests for euclideanGCD(a, IQI). */
        /* TO DO: Write tests for euclideanGCD(IQI, b). */
        /* Last but not least, euclideanGCD(ImaginaryQuadraticInteger, 
           ImaginaryQuadraticInteger). */
        ImaginaryQuadraticRing r;
        ImaginaryQuadraticInteger iqia, iqib, expResultIQI, resultIQI;
        // sqrt(d) and 1 + sqrt(d) should be coprime in any ring
        for (Integer iterDiscr : NumberTheoreticFunctionsCalculator.NORM_EUCLIDEAN_QUADRATIC_IMAGINARY_RINGS_D) {
            r = new ImaginaryQuadraticRing(iterDiscr);
            iqia = new ImaginaryQuadraticInteger(0, 1, r);
            iqib = new ImaginaryQuadraticInteger(1, 1, r);
            expResultIQI = new ImaginaryQuadraticInteger(1, 0, r);
            try {
                resultIQI = NumberTheoreticFunctionsCalculator.euclideanGCD(iqia, iqib);
            } catch (AlgebraicDegreeOverflowException adoe) {
                resultIQI = iqia; // Just to avoid "may not have been initialized" warning
                fail("Attempting to calculate gcd(" + iqia.toASCIIString() + ", " + iqib.toASCIIString() + ") should not have triggered AlgebraicDegreeOverflowException" + adoe.getMessage());
            } catch (NonEuclideanDomainException nede) {
                resultIQI = iqib; // Same reason as previous
                fail("Attempting to calculate gcd(" + iqia.toASCIIString() + ", " + iqib.toASCIIString() + ") should not have triggered NonEuclideanDomainException" + nede.getMessage());
            } catch (NullPointerException npe) {
                resultIQI = iqia;
                System.out.println("NullPointerException encountered: " + npe.getMessage());
                System.out.println("This could indicate a problem with NotDivisibleException.getBoundingIntegers().");
            }
            assertEquals(expResultIQI, resultIQI);
        }
        // In most cases, gcd(sqrt(d), -d) = sqrt(d)
        for (int iterDiscrA = 0; iterDiscrA < NumberTheoreticFunctionsCalculator.NORM_EUCLIDEAN_QUADRATIC_IMAGINARY_RINGS_D.length - 1; iterDiscrA++) {
            r = new ImaginaryQuadraticRing(NumberTheoreticFunctionsCalculator.NORM_EUCLIDEAN_QUADRATIC_IMAGINARY_RINGS_D[iterDiscrA]);
            iqia = new ImaginaryQuadraticInteger(0, 1, r);
            iqib = new ImaginaryQuadraticInteger(NumberTheoreticFunctionsCalculator.NORM_EUCLIDEAN_QUADRATIC_IMAGINARY_RINGS_D[iterDiscrA], 0, r);
            expResultIQI = iqia;
            try {
                resultIQI = NumberTheoreticFunctionsCalculator.euclideanGCD(iqia, iqib);
            } catch (AlgebraicDegreeOverflowException adoe) {
                resultIQI = NumberTheoreticFunctionsCalculator.IMAG_UNIT_I; // Just to avoid "may not have been initialized" warning
                fail("Attempting to calculate gcd(" + iqia.toASCIIString() + ", " + iqib.toASCIIString() + ") should not have triggered AlgebraicDegreeOverflowException" + adoe.getMessage());
            } catch (NonEuclideanDomainException nede) {
                resultIQI = NumberTheoreticFunctionsCalculator.IMAG_UNIT_NEG_I; // Same reason as previous
                fail("Attempting to calculate gcd(" + iqia.toASCIIString() + ", " + iqib.toASCIIString() + ") should not have triggered NonEuclideanDomainException" + nede.getMessage());
            } catch (ArrayIndexOutOfBoundsException aioobe) {
                resultIQI = iqib;
                System.out.println("ArrayIndexOutOfBoundsException encountered: " + aioobe.getMessage());
                System.out.println("This could indicate either a flaw in the implementation of the Euclidean algorithm or a problem with NotDivisibleException.getBoundingIntegers().");
                fail("Attempting to calculate gcd(" + iqia.toASCIIString() + ", " + iqib.toASCIIString() + ") should not have triggered ArrayIndexOutOfBoundsException: " + aioobe.getMessage());
            }
            assertEquals(expResultIQI, resultIQI);
        }
        int b;
        for (int iterDiscrOQ = 0; iterDiscrOQ < NumberTheoreticFunctionsCalculator.NORM_EUCLIDEAN_QUADRATIC_IMAGINARY_RINGS_D.length - 3; iterDiscrOQ++) {
            r = new ImaginaryQuadraticRing(NumberTheoreticFunctionsCalculator.NORM_EUCLIDEAN_QUADRATIC_IMAGINARY_RINGS_D[iterDiscrOQ]);
            iqia = new ImaginaryQuadraticInteger(1, 1, r, 2);
            b = (int) iqia.norm() * HEEGNER_COMPANION_PRIMES[iterDiscrOQ + 4];
            expResultIQI = iqia;
            iqia = iqia.times(iqia);
            try {
                resultIQI = NumberTheoreticFunctionsCalculator.euclideanGCD(iqia, b);
            } catch (AlgebraicDegreeOverflowException adoe) {
                resultIQI = NumberTheoreticFunctionsCalculator.IMAG_UNIT_I; // Just to avoid "may not have been initialized" warning
                fail("Attempting to calculate gcd(" + iqia.toASCIIString() + ", " + b + ") should not have triggered AlgebraicDegreeOverflowException" + adoe.getMessage());
            } catch (NonEuclideanDomainException nede) {
                resultIQI = NumberTheoreticFunctionsCalculator.IMAG_UNIT_NEG_I; // Same reason as previous
                fail("Attempting to calculate gcd(" + iqia.toASCIIString() + ", " + b + ") should not have triggered NonEuclideanDomainException" + nede.getMessage());
            } catch (ArrayIndexOutOfBoundsException aioobe) {
                resultIQI = new ImaginaryQuadraticInteger(0, 0, r);
                System.out.println("ArrayIndexOutOfBoundsException encountered: " + aioobe.getMessage());
                System.out.println("This could indicate either a flaw in the implementation of the Euclidean algorithm or a problem with NotDivisibleException.getBoundingIntegers().");
                fail("Attempting to calculate gcd(" + iqia.toASCIIString() + ", " + b + ") should not have triggered ArrayIndexOutOfBoundsException: " + aioobe.getMessage());
            }
            assertEquals(expResultIQI, resultIQI);
            try {
                resultIQI = NumberTheoreticFunctionsCalculator.euclideanGCD(b, iqia);
            } catch (NonEuclideanDomainException nede) {
                resultIQI = NumberTheoreticFunctionsCalculator.IMAG_UNIT_NEG_I; // Same reason as previous
                fail("Attempting to calculate gcd(" + iqia.toASCIIString() + ", " + b + ") should not have triggered NonEuclideanDomainException" + nede.getMessage());
            }
            assertEquals(expResultIQI, resultIQI);
        }
        r = NumberTheoreticFunctionsCalculator.RING_GAUSSIAN;
        iqia = new ImaginaryQuadraticInteger(-2, 2, r);
        iqib = new ImaginaryQuadraticInteger(-2, 4, r);
        expResultIQI = new ImaginaryQuadraticInteger(2, 0, r);
        try {
            resultIQI = NumberTheoreticFunctionsCalculator.euclideanGCD(iqia, iqib);
        } catch (AlgebraicDegreeOverflowException adoe) {
            resultIQI = iqia;
            fail("Attempting to calculate gcd(" + iqia.toASCIIString() + ", " + iqib.toASCIIString() + ") should not have triggered AlgebraicDegreeOverflowException" + adoe.getMessage());
        } catch (NonEuclideanDomainException nede) {
            resultIQI = iqib;
            fail("Attempting to calculate gcd(" + iqia.toASCIIString() + ", " + iqib.toASCIIString() + ") should not have triggered NonEuclideanDomainException" + nede.getMessage());
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
        System.out.println("Function came up with this pseudorandom negative squarefree number: " + potentialNegRanSqFreeNum);
        // Check that the pseudorandom number is indeed squarefree
        double squaredPrime, ranNumDiv, flooredRanNumDiv;
        for (int i = 0; i < primesListLength; i++) {
            squaredPrime = primesList.get(i) * primesList.get(i);
            ranNumDiv = potentialNegRanSqFreeNum/squaredPrime;
            flooredRanNumDiv = (int) Math.floor(ranNumDiv);
            assertNotEquals(ranNumDiv, flooredRanNumDiv, ImaginaryQuadraticRingTest.TEST_DELTA);
        }
        /* And lastly, check that it is at least the negated test bound but not 
        more than or equal to 0. */
        assertFalse(potentialNegRanSqFreeNum < (-1) * testBound);
        assertTrue(potentialNegRanSqFreeNum < 0);
    }
    
}