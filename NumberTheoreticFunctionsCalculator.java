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

import java.util.*;

/**
 *
 * @author Alonso del Arte
 */

public class NumberTheoreticFunctionsCalculator {
    
    /**
     * Determines the prime factors of a given number
     * @param num The integer for which to determine prime factors of
     * @return A list of the prime factors, with some factors repeated as needed
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
            for (int i = 2; i <= n; i++) {
                while (n % i == 0) {
                    factors.add(i);
                    n /= i;
                }
            }
        }
        return factors;
        
    }
    
    /**
     * Determines whether a given number is prime or not
     * @param num The number to be tested for primality
     * @return true if the number is prime (even if negative), false otherwise
     */
    public static boolean isPrime(int num) {
        
        switch (num) {
            case -1:
            case 0:
            case 1:
                return false;
            default:
                List<Integer> prFacts = primeFactors(num);
                if (prFacts.size() == 1 && prFacts.get(0) > 0) {
                    return true;
                } else {
                    if (prFacts.size() == 2 && prFacts.get(0) == -1) {
                        return true;
                    }
                }
                return false;
        }
    }
    
    /**
     * Determines whether a given number is squarefree or not 
     * @param num The number to be tested for being squarefree
     * @return true if the number is squarefree, false otherwise
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
     * Computes the M\u00F6bius function \u03BC for a given integer
     * @param num The integer for which to compute the M\u00F6bius function
     * @return 1 if num is squarefree with an even number of prime factors, -1 if num is squarefree with an odd number of prime factors, 0 if num is not squarefree
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
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        Scanner input = new Scanner(System.in);
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
                    System.out.println(" ");
                    break;
            }
            invalidInput = true;
        }
    }
    
}