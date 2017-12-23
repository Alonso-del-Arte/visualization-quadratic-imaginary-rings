/*
 * Copyright (C) 2017 Alonso del Arte
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
import java.util.Random;
import java.util.Scanner;
import java.util.InputMismatchException;

/**
 * A collection of number theoretic functions, including basic primality testing and the Euclidean GCD algorithm.
 * @author Alonso del Arte
 */

public class NumberTheoreticFunctionsCalculator {
    
    /**
     * Determines the prime factors of a given number.
     * Uses simple trial division with only basic optimization.
     * @param num The integer for which to determine prime factors of.
     * @return A list of the prime factors, with some factors repeated as needed.
     * For example, given num = 44100, the resulting list should be 2, 2, 3, 3, 5, 5, 7, 7.
     * The factorization of 0 is given as just 0.
     * For a negative number, the factorization starts with -1 followed by the factorization of its positive counterpart.
     * For example, given num = -44100, the resulting list should be -1, 2, 2, 3, 3, 5, 5, 7, 7.
     */
    public static List<Integer> primeFactors(int num) {
        
        int n = num;
        List<Integer> factors = new ArrayList<>();
        
        if (n == 0) {
            factors.add(0);
        } else {
            if (n < 0) {
                n *= (-1);
                factors.add(-1);
            }
            while (n % 2 == 0) {
                factors.add(2); // Treating 2 as a special case
                n /= 2;
            }
            for (int i = 3; i <= n; i += 2) {
                while (n % i == 0) {
                    factors.add(i);
                    n /= i;
                }
            }
        }
        return factors;
        
    }
    
    /**
     * Determines whether a given number is prime or not.
     * @param num The number to be tested for primality.
     * @return true if the number is prime (even if negative), false otherwise.
     * For example, -2 and 47 should each return true, -25, 0 and 91 should each return false.
     */
    public static boolean isPrime(int num) {
        
        switch (num) {
            case -1:
            case 0:
            case 1:
                return false; // break statement not needed after return
            case -2:
            case 2:
                return true;
            default:
                if (num % 2 == 0) {
                    return false;
                } else {
                    boolean primeFlag = true;
                    int potentialFactor = 3;
                    double numSqrt = Math.sqrt(Math.abs(num));
                    while (primeFlag && potentialFactor <= numSqrt) {
                        primeFlag = (num % potentialFactor != 0);
                        potentialFactor += 2;
                    }
                    return primeFlag;
                }
        }
    }
    
    /**
     * Determines whether a given number is squarefree or not.
     * @param num The number to be tested for being squarefree.
     * @return true if the number is squarefree, false otherwise.
     * For example, -3 and 7 should each return true, -4, 0 and 25 should each return false.
     * Note that 1 is considered squarefree. Therefore, for num = 1, this function should return true.
     */
    public static boolean isSquareFree(int num) {
        
        switch (num) {
            case -1:
            case 1:
                return true;
            case 0:
                return false;
            default:
                boolean dupFactorFound = false;
                List<Integer> prFacts = primeFactors(num);
                int lastToCheck = prFacts.size() - 1;
                int curr = 0;
                while (dupFactorFound == false && curr < lastToCheck) {
                    dupFactorFound = prFacts.get(curr).equals(prFacts.get(curr + 1));
                    curr++;
                }
                return !dupFactorFound;
        }
    }
    
    /**
     * Computes the M\u00F6bius function \u03BC for a given integer.
     * @param num The integer for which to compute the M\u00F6bius function.
     * @return 1 if num is squarefree with an even number of prime factors, -1 if num is squarefree with an odd number of prime factors, 0 if num is not squarefree.
     * Since -1 is a unit, not a prime, \u03BC(-n) = \u03BC(n).
     * For example, \u03BC(31) = -1, \u03BC(32) = 0 and \u03BC(33) = 1.
     */
    public static byte moebiusMu(int num) {
        
        switch (num) {
            case -1:
            case 1:
                return 1;
            default:
                if (isSquareFree(num)) {
                    List<Integer> prFacts = primeFactors(num);
                    if (prFacts.get(0) == -1) {
                        prFacts.remove(0);
                    }
                    if (prFacts.size() % 2 == 0) {
                        return 1;
                    } else {
                        return -1;
                    }
                } else {
                    return 0;
                }
        }
    }
    
    /**
     * Computes the greatest common divisor (GCD) of two purely real integers by using the Euclidean algorithm.
     * @param a One of the two integers. May be negative, need not be greater than the other.
     * @param b One of the two integers. May be negative, need not be greater than the other.
     * @return The GCD as an integer.
     * If one of a or b is 0 and the other is nonzero, the result will be the nonzero number.
     * If both a and b are 0, then the result will be 0, which is technically wrong, but I think it's good enough for the purpose here.
     */
    public static int euclideanGCD(int a, int b) {
        int currA, currB, currRemainder;
        if (a < b) {
            currA = b;
            currB = a;
        } else {
            currA = a;
            currB = b;
        }
        while (currB != 0) {
            currRemainder = currA % currB;
            currA = currB;
            currB = currRemainder;
        }
        if (currA < 0) {
            currA *= -1;
        }
        return currA;
    }
    
    /**
     * Provides a pseudorandom negative squarefree integer.
     * @param bound The lowest number desired (but may use a positive integer). For example, for a pseudorandom squarefree number between -97 and -1, you can pass -100 or 100.
     * @return A pseudorandom negative squarefree integer.
     */
    public static int randomNegativeSquarefreeNumber(int bound) {
        if (bound < 0) {
            bound *= -1;
        }
        Random ranNumGen = new Random();
        int randomNumber = ranNumGen.nextInt(bound);
        randomNumber *= -1;
        while (!isSquareFree(randomNumber)) {
            randomNumber--;
        }
        return randomNumber;
    }
    
    /**
     * A console program for testing the number theoretic functions with user inputs.
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        Scanner input = new Scanner(System.in);
        int prevInteger = 0;
        int enteredInteger = 1;
        List<Integer> prFacts;
        boolean invalidInput = true;
        
        while (enteredInteger != 0) {
            System.out.print("Please enter an integer to factor (or 0 to quit): ");
            while (invalidInput) {
                try {
                    enteredInteger = input.nextInt();
                    invalidInput = false;
                } catch (InputMismatchException inputMismatch) {
                    System.out.println("Please enter an integer.");
                    input.nextLine();
                }
            }
            switch (enteredInteger) {
                case -1:
                case 1:
                    System.out.println(enteredInteger + " is a unit.");
                    System.out.println(enteredInteger + " is squarefree.");
                    System.out.println("\u03BC(" + enteredInteger + ") = 1.");
                    System.out.println(" ");
                    break;
                case 0:
                    System.out.println("0 is not prime, nor squarefree.");
                    System.out.println("\u03BC(0) = 0.");
                    System.out.println("gcd(0, " + prevInteger + ") = " + euclideanGCD(0, prevInteger));
                    System.out.println(" ");
                    break;
                default:
                    if (isPrime(enteredInteger)) {
                        System.out.println(enteredInteger + " is prime.");
                    } else {
                        prFacts = primeFactors(enteredInteger);
                        System.out.print("The prime factorization of " + enteredInteger + " is " + prFacts.get(0));
                        for (int i = 1; i < prFacts.size(); i++) {
                            System.out.print(" \u00D7 ");
                            System.out.print(prFacts.get(i));
                        }
                        System.out.println(" ");
                    }
                    if (isSquareFree(enteredInteger)) {
                        System.out.println(enteredInteger + " is squarefree.");
                    } else {
                        System.out.println(enteredInteger + " is not squarefree.");
                    }
                    System.out.println("\u03BC(" + enteredInteger + ") = " + moebiusMu(enteredInteger));
                    System.out.println(enteredInteger + " is congruent to " + (enteredInteger % 4) + " modulo 4.");
                    System.out.println("gcd(" + enteredInteger + ", " + prevInteger + ") = " + euclideanGCD(enteredInteger, prevInteger));
                    System.out.println(" ");
                    break;
            }
            invalidInput = true;
            prevInteger = enteredInteger;
        }
    }
    
}