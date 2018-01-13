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
     * The prime numbers listed in <a href="http://oeis.org/A000040">Sloane's A000040</a>.
     * A lot more primes are listed in the B-file, but these should be sufficient to test isPrime().
     */
    public static final int[] SLOANES_OEIS_A000040 = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101, 103, 107, 109, 113, 127, 131, 137, 139, 149, 151, 157, 163, 167, 173, 179, 181, 191, 193, 197, 199, 211, 223, 227, 229, 233, 239, 241, 251, 257, 263, 269, 271};
    
    /**
     * The composite numbers, and 1, listed in <a href="http://oeis.org/A018252">Sloane's A018252</a>.
     * A lot more of these are listed in the B-file, but these should be sufficient to test isPrime().
     */
    public static final int[] SLOANES_OEIS_A018252 = {1, 4, 6, 8, 9, 10, 12, 14, 15, 16, 18, 20, 21, 22, 24, 25, 26, 27, 28, 30, 32, 33, 34, 35, 36, 38, 39, 40, 42, 44, 45, 46, 48, 49, 50, 51, 52, 54, 55, 56, 57, 58, 60, 62, 63, 64, 65, 66, 68, 69, 70, 72, 74, 75, 76, 77, 78, 80, 81, 82, 84, 85, 86, 87, 88};
    
    public static List<Integer> factors44100;
    public static List<Integer> factors86436000;
    
    public NumberTheoreticFunctionsCalculatorTest() {
    }
    
    /**
     * Sets up an array list of the factors of 44100 and another array list with the factors of 86436000.
     * The static final fields provide most of everything else needed for the tests.
     */
    @BeforeClass
    public static void setUpClass() {
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
        int A40Len = SLOANES_OEIS_A000040.length;
        for (int i = 0; i < A40Len; i++) {
            assertTrue(NumberTheoreticFunctionsCalculator.isPrime(SLOANES_OEIS_A000040[i]));
            assertTrue(NumberTheoreticFunctionsCalculator.isPrime(-SLOANES_OEIS_A000040[i]));
        }
        for (int j = 0; j < SLOANES_OEIS_A018252.length; j++) {
            assertFalse(NumberTheoreticFunctionsCalculator.isPrime(SLOANES_OEIS_A018252[j]));
            assertFalse(NumberTheoreticFunctionsCalculator.isPrime(-SLOANES_OEIS_A018252[j]));
        }
        // Now we're going to test odd integers greater than the last prime listed in the SLOANES_OEIS_A000040 array but smaller than the square root of that prime.
        int maxNumForTest = SLOANES_OEIS_A000040[A40Len - 1] * SLOANES_OEIS_A000040[A40Len - 1];
        int primeIndex = 1; // Which of course corresponds to 3, not 2, in a zero-indexed array
        boolean possiblyPrime = true; // Presume k to be prime until finding otherwise
        for (int k = SLOANES_OEIS_A000040[A40Len - 1] + 2; k < maxNumForTest; k += 2) {
            while (primeIndex < A40Len && possiblyPrime) {
                possiblyPrime = (k % SLOANES_OEIS_A000040[primeIndex] != 0);
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
        for (int i = 0; i < SLOANES_OEIS_A000040.length - 1; i++) {
            num = SLOANES_OEIS_A000040[i] * SLOANES_OEIS_A000040[i + 1]; // A squarefree semiprime, pq
            assertTrue(NumberTheoreticFunctionsCalculator.isSquareFree(num));
            num *= SLOANES_OEIS_A000040[i]; // Repeat one prime factor, (p^2)q
            assertFalse(NumberTheoreticFunctionsCalculator.isSquareFree(num));
        }
    }

    /**
     * Test of moebiusMu method, of class NumberTheoreticFunctionsCalculator.
     */
    @Test
    public void testMoebiusMu() {
        System.out.println("moebiusMu");
        int num = 1074;
        byte expResult = -1;
        byte result = NumberTheoreticFunctionsCalculator.moebiusMu(num);
        assertEquals(expResult, result);
        // The primes p should all have mu(p) = -1
        for (int i = 0; i < SLOANES_OEIS_A000040.length; i++) {
            assertEquals(expResult, NumberTheoreticFunctionsCalculator.moebiusMu(SLOANES_OEIS_A000040[i]));
            assertEquals(NumberTheoreticFunctionsCalculator.moebiusMu(SLOANES_OEIS_A000040[i]), NumberTheoreticFunctionsCalculator.moebiusMu(-SLOANES_OEIS_A000040[i]));
        }
        // Now to test mu(n) = 0 with n being a multiple of 4
        expResult = 0;
        for (int j = 0; j < 97; j += 4) {
            assertEquals(expResult, NumberTheoreticFunctionsCalculator.moebiusMu(j));
            assertEquals(NumberTheoreticFunctionsCalculator.moebiusMu(j), NumberTheoreticFunctionsCalculator.moebiusMu(-j));
        }
        // And lastly, the products of two distinct primes p and q should give mu(pq) = 1
        expResult = 1;
        for (int k = 0; k < SLOANES_OEIS_A000040.length - 1; k++) {
            num = SLOANES_OEIS_A000040[k] * SLOANES_OEIS_A000040[k + 1];
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
        int testBound = SLOANES_OEIS_A000040[SLOANES_OEIS_A000040.length - 1] * SLOANES_OEIS_A000040[SLOANES_OEIS_A000040.length - 1];
        int potentialNegRanSqFreeNum = NumberTheoreticFunctionsCalculator.randomNegativeSquarefreeNumber(testBound);
        System.out.println("Function came up with this pseudorandom number: " + potentialNegRanSqFreeNum);
        // Check that the pseudorandom number is indeed squarefree
        double squaredPrime, ranNumDiv, flooredRanNumDiv;
        for (int i = 0; i < SLOANES_OEIS_A000040.length; i++) {
            squaredPrime = SLOANES_OEIS_A000040[i] * SLOANES_OEIS_A000040[i];
            ranNumDiv = potentialNegRanSqFreeNum/squaredPrime;
            flooredRanNumDiv = (int) Math.floor(ranNumDiv);
            System.out.print(squaredPrime + ", " + ranNumDiv + ", " + flooredRanNumDiv + "; ");
            assertFalse(ranNumDiv == flooredRanNumDiv);
        }
        System.out.println();
        // And lastly, check that it is at least the negated test bound but not more than or equal to 0
        assertFalse(potentialNegRanSqFreeNum < (-1) * testBound);
        assertTrue(potentialNegRanSqFreeNum < 0);
    }
    
}