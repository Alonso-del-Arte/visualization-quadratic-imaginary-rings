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
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for a collection of number theoretic functions, including basic primality testing and the Euclidean GCD algorithm.
 * Some of these tests use a finite list of small primes and another finite list of non-prime numbers.
 * @author Alonso del Arte
 */
public class NumberTheoreticFunctionsCalculatorTest {
    
    /**
     * setUp() will generate a List of the first few consecutive primes. This 
     * constant determines how long that list will be.
     */
    public static final int PRIME_LIST_SIZE = 1000;
    
    /**
     * A List of the first few prime numbers, to be used in some of the tests.
     */
    private static List<Integer> primeList;
    
    /**
     * The size of primeList
     */
    private static int primeListLength;
    
    /**
     * The composite numbers, and 1, listed in <a href="http://oeis.org/A018252">Sloane's A018252</a>.
     * A lot more of these are listed in the B-file, but these should be sufficient to test isPrime().
     */
    public static final int[] SLOANES_OEIS_A018252 = {1, 4, 6, 8, 9, 10, 12, 14, 15, 16, 18, 20, 21, 22, 24, 25, 26, 27, 28, 30, 32, 33, 34, 35, 36, 38, 39, 40, 42, 44, 45, 46, 48, 49, 50, 51, 52, 54, 55, 56, 57, 58, 60, 62, 63, 64, 65, 66, 68, 69, 70, 72, 74, 75, 76, 77, 78, 80, 81, 82, 84, 85, 86, 87, 88};
    
    /**
     * A List to be populated with the factors of 44100.
     * The prime factorization of 44100 is: 2^2 * 3^2 * 5^2 * 7^2.
     */
    public static List<Integer> factors44100;
    
    /**
     * A List to be populated with the factors of 86436000, a multiple of 44100.
     * The prime factorization of 86436000 is: 2^5 * 3^2 * 5^3 * 7^4.
     */
    public static List<Integer> factors86436000;
    
    public NumberTheoreticFunctionsCalculatorTest() {
    }
    
    /**
     * Sets up a List of the first few consecutive primes, a List of the factors 
     * of 44100 and another List with the factors of 86436000. The static final 
     * fields provide most of everything else needed for the tests.
     */
    @BeforeClass
    public static void setUpClass() {
        /* First, to generate a list of the first few consecutive primes, using 
        the sieve of Eratosthenes. */
        int threshold, halfThreshold;
        if (PRIME_LIST_SIZE < 0) {
            threshold = (-1) * PRIME_LIST_SIZE;
        } else {
            threshold = PRIME_LIST_SIZE;
        }
        if (threshold % 2 == 1) {
            halfThreshold = (threshold + 1)/2;
        } else {
            halfThreshold = threshold/2;
        }
        primeList = new ArrayList<>();
        primeList.add(2); // Add 2 as a special case
        boolean[] primeFlags = new boolean[halfThreshold];
        for (int i = 0; i < halfThreshold; i++) {
            primeFlags[i] = true; // Presume all odd numbers prime for now
        }
        int currPrime = 3;
        int twiceCurrPrime, currIndex;
        while (currPrime < threshold) {
            primeList.add(currPrime);
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
        primeListLength = primeList.size();
        System.out.println("setUpClass() has generated a list of the first " + primeListLength + " consecutive primes.");
        System.out.println("prime(" + primeListLength + ") = " + primeList.get(primeListLength - 1));
        // 44100 = 2^2 * 3^2 * 5^2 * 7^2
        factors44100 = new ArrayList<>();
        factors44100.add(2);
        factors44100.add(2); // 4
        factors44100.add(3);
        factors44100.add(3); // 9
        factors44100.add(5);
        factors44100.add(5); // 25
        factors44100.add(7);
        factors44100.add(7); // 49
        // 86436000 = 2^5 * 3^2 * 5^3 * 7^4
        factors86436000 = new ArrayList<>();
        factors86436000.add(2);
        factors86436000.add(2);
        factors86436000.add(2);
        factors86436000.add(2);
        factors86436000.add(2); // 32
        factors86436000.add(3);
        factors86436000.add(3); // 9
        factors86436000.add(5);
        factors86436000.add(5);
        factors86436000.add(5); // 125
        factors86436000.add(7);
        factors86436000.add(7);
        factors86436000.add(7);
        factors86436000.add(7); // 2401
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of primeFactors method, of class NumberTheoreticFunctionsCalculator.
     */
    @Test
    public void testPrimeFactors() {
        System.out.println("primeFactors");
        int num = 32;
        List<Integer> expResult = new ArrayList<>();
        expResult.add(2);
        expResult.add(2); // 4
        expResult.add(2); // 8
        expResult.add(2); // 16
        expResult.add(2); // 32
        List<Integer> result = NumberTheoreticFunctionsCalculator.primeFactors(num);
        assertEquals(expResult, result);
        num = 44100;
        expResult = factors44100;
        result = NumberTheoreticFunctionsCalculator.primeFactors(num);
        assertEquals(expResult, result);
        num = 86436000;
        expResult = factors86436000;
        result = NumberTheoreticFunctionsCalculator.primeFactors(num);
        assertEquals(expResult, result);
    }

    /**
     * Test of isPrime method, of class NumberTheoreticFunctionsCalculator.
     * The numbers listed in Sloane's A000040, as well as those same numbers multiplied by -1, should all be identified as prime.
     * Likewise, the numbers listed in Sloane's A018252, as well as those same numbers multiplied by -1, should all be identified as not prime.
     * As for 0, I'm not sure; if you like you can uncomment the line for it and perhaps change assertFalse to assertTrue.
     */
    @Test
    public void testIsPrime() {
        System.out.println("isPrime");
        // assertFalse(NumberTheoreticFunctionsCalculator.isPrime(0));
        for (int i = 0; i < primeListLength; i++) {
            assertTrue(NumberTheoreticFunctionsCalculator.isPrime(primeList.get(i)));
            assertTrue(NumberTheoreticFunctionsCalculator.isPrime(-primeList.get(i)));
        }
        for (int j = 0; j < SLOANES_OEIS_A018252.length; j++) {
            assertFalse(NumberTheoreticFunctionsCalculator.isPrime(SLOANES_OEIS_A018252[j]));
            assertFalse(NumberTheoreticFunctionsCalculator.isPrime(-SLOANES_OEIS_A018252[j]));
        }
        /* Now we're going to test odd integers greater than the last prime 
        in our List but smaller than the square of that prime. */
        int maxNumForTest = primeList.get(primeListLength - 1) * primeList.get(primeListLength - 1);
        int primeIndex = 1; // Which of course corresponds to 3, not 2, in a zero-indexed array
        boolean possiblyPrime = true; // Presume k to be prime until finding otherwise
        for (int k = primeList.get(primeListLength - 1) + 2; k < maxNumForTest; k += 2) {
            while (primeIndex < primeListLength && possiblyPrime) {
                possiblyPrime = (k % primeList.get(primeIndex) != 0);
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
    }
    
    /**
     * Test of isSquareFree method, of class NumberTheoreticFunctionsCalculator.
     */
    @Test
    public void testIsSquareFree() {
        System.out.println("isSquareFree");
        int num = 1024;
        boolean expResult = false;
        boolean result = NumberTheoreticFunctionsCalculator.isSquareFree(num);
        assertEquals(expResult, result);
        for (int i = 0; i < primeListLength - 1; i++) {
            num = primeList.get(i) * primeList.get(i + 1); // A squarefree semiprime, pq
            assertTrue(NumberTheoreticFunctionsCalculator.isSquareFree(num));
            num *= primeList.get(i); // Repeat one prime factor, (p^2)q
            assertFalse(NumberTheoreticFunctionsCalculator.isSquareFree(num));
        }
    }

    /**
     * Test of moebiusMu method, of class NumberTheoreticFunctionsCalculator.
     * I expect that mu(-n) = mu(n), so this test checks for that.
     * If you wish to limit the test to positive integers, you can just remove some of the assertions.
     */
    @Test
    public void testMoebiusMu() {
        System.out.println("moebiusMu");
        int num = 1074;
        byte expResult = -1;
        byte result = NumberTheoreticFunctionsCalculator.moebiusMu(num);
        assertEquals(expResult, result);
        // The primes p should all have mu(p) = -1
        for (int i = 0; i < primeListLength; i++) {
            assertEquals(expResult, NumberTheoreticFunctionsCalculator.moebiusMu(primeList.get(i)));
            assertEquals(NumberTheoreticFunctionsCalculator.moebiusMu(primeList.get(i)), NumberTheoreticFunctionsCalculator.moebiusMu(-primeList.get(i)));
        }
        // Now to test mu(n) = 0 with n being a multiple of 4
        expResult = 0;
        for (int j = 0; j < 97; j += 4) {
            assertEquals(expResult, NumberTheoreticFunctionsCalculator.moebiusMu(j));
            assertEquals(NumberTheoreticFunctionsCalculator.moebiusMu(j), NumberTheoreticFunctionsCalculator.moebiusMu(-j));
        }
        // And lastly, the products of two distinct primes p and q should give mu(pq) = 1
        expResult = 1;
        for (int k = 0; k < primeListLength - 1; k++) {
            num = primeList.get(k) * primeList.get(k + 1);
            assertEquals(expResult, NumberTheoreticFunctionsCalculator.moebiusMu(num));
            assertEquals(NumberTheoreticFunctionsCalculator.moebiusMu(num), NumberTheoreticFunctionsCalculator.moebiusMu(-num));
        }
    }

    /**
     * Test of euclideanGCD method, of class NumberTheoreticFunctionsCalculator.
     * TO DO: Write test for euclideanGCD(IQI, IQI).
     * At this time, I choose not to test the case gcd(0, 0).
     * The value of such a test would be philosophical rather than practical.
     */
    @Test
    public void testEuclideanGCD() {
        System.out.println("euclideanGCD");
        int a = -1925;
        int b = 44100;
        int expResult = 175;
        int result = NumberTheoreticFunctionsCalculator.euclideanGCD(a, b);
        assertEquals(expResult, result);
        expResult = 1; // Going to test with consecutive integers, expect the result to be 1 each time
        for (int i = -30; i < 31; i++) {
            result = NumberTheoreticFunctionsCalculator.euclideanGCD(i, i + 1);
            assertEquals(expResult, result);
        }
        // Now test with consecutive odd numbers, result should also be 1 each time as well
        for (int i = -29; i < 31; i += 2) {
            result = NumberTheoreticFunctionsCalculator.euclideanGCD(i, i + 2);
            assertEquals(expResult, result);
        }
        expResult = 2; // Now to test with consecutive even integers, result should be 2 each time
        for (int i = -30; i < 31; i += 2) {
            result = NumberTheoreticFunctionsCalculator.euclideanGCD(i, i + 2);
            assertEquals(expResult, result);
        }
        // And now some of the same tests again but with the long data type
        long expResultLong = 1;
        long resultLong;
        for (long j = Integer.MAX_VALUE; j < Integer.MAX_VALUE + 32; j++) {
            resultLong = NumberTheoreticFunctionsCalculator.euclideanGCD(j, j + 1);
            assertEquals(expResultLong, resultLong);
        }
        expResultLong = 2;
        for (long j = Integer.MAX_VALUE; j < Integer.MAX_VALUE + 32; j += 2) {
            resultLong = NumberTheoreticFunctionsCalculator.euclideanGCD(j, j + 2);
            assertEquals(expResultLong, resultLong);
        }
    }
    
    /**
     * Test of randomNegativeSquarefreeNumber method, of class NumberTheoreticFunctionsCalculator.
     */
    @Test
    public void testRandomNegativeSquarefreeNumber() {
        System.out.println("randomNegativeSquarefreeNumber");
        // Our test bound will be the square of the largest prime in our finite list
        int testBound = primeList.get(primeListLength - 1) * primeList.get(primeListLength - 1);
        int potentialNegRanSqFreeNum = NumberTheoreticFunctionsCalculator.randomNegativeSquarefreeNumber(testBound);
        System.out.println("Function came up with this pseudorandom number: " + potentialNegRanSqFreeNum);
        // Check that the pseudorandom number is indeed squarefree
        double squaredPrime, ranNumDiv, flooredRanNumDiv;
        for (int i = 0; i < primeListLength; i++) {
            squaredPrime = primeList.get(i) * primeList.get(i);
            ranNumDiv = potentialNegRanSqFreeNum/squaredPrime;
            flooredRanNumDiv = (int) Math.floor(ranNumDiv);
            // System.out.print(squaredPrime + ", " + ranNumDiv + ", " + flooredRanNumDiv + "; ");
            assertFalse(ranNumDiv == flooredRanNumDiv);
        }
        System.out.println();
        // And lastly, check that it is at least the negated test bound but not more than or equal to 0
        assertFalse(potentialNegRanSqFreeNum < (-1) * testBound);
        assertTrue(potentialNegRanSqFreeNum < 0);
    }
    
}