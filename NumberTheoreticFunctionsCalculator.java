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

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * A collection of number theoretic functions, including basic primality testing 
 * and the Euclidean GCD algorithm.
 * @author Alonso del Arte
 */
public class NumberTheoreticFunctionsCalculator {
    
    /**
     * Determines the prime factors of a given number. Uses simple trial 
     * division with only basic optimization.
     * @param num The integer for which to determine prime factors of.
     * @return A list of the prime factors, with some factors repeated as 
     * needed. For example, given num = 44100, the resulting list should be 2, 
     * 2, 3, 3, 5, 5, 7, 7. The factorization of 0 is given as just 0. For a 
     * negative number, the factorization starts with -1 followed by the 
     * factorization of its positive counterpart. For example, given num = 
     * -44100, the resulting list should be -1, 2, 2, 3, 3, 5, 5, 7, 7.
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
     * Determines whether a given purely real number is prime or not. The 
     * numbers 0, -1, 1, -2, 2 are treated as special cases. For all others, the 
     * function searches only for the least prime factor. If the least prime 
     * factor is found to be unequal to the absolute value of the number, the 
     * function reports the number as composite and returns to the caller. 
     * Still, the function is open to optimization.
     * @param num The number to be tested for primality.
     * @return true if the number is prime (even if negative), false otherwise.
     * For example, -2 and 47 should each return true, -25, 0 and 91 should each 
     * return false.
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
     * An implementation of the Legendre symbol, which tells if a given number 
     * is a quadratic residue modulo an odd prime. There is no overflow 
     * checking, but hopefully that's only a problem for numbers that are very 
     * close to Integer.MIN_VALUE or Integer.MAX_VALUE.
     * @param a The number to test for being a quadratic residue modulo an odd 
     * prime. For example, 10.
     * @param p The odd prime to test a for being a quadratic residue modulo of. 
     * For example, 7. This parameter may be negative; the function will simply 
     * change it to a positive number.
     * @return -1 if a is quadratic residue modulo p, 0 if gcd(a, p) > 1, 1 if a 
     * is a quadratic residue modulo p. An example of each: Legendre(10, 7) = -1 
     * since there are no solutions to x^2 = 10 mod 7; Legendre(10, 5) = 0 since 
     * 10 is a multiple of 5; and Legendre(10, 3) = 1 since x^2 = 10 mod 3 does 
     * have solutions, such as x = 4.
     * @throws IllegalArgumentException If p is not an odd prime. Note that this 
     * is a runtime exception.
     */
    public static byte symbolLegendre(int a, int p) {
        if (!isPrime(p)) {
            throw new IllegalArgumentException(p + " is not a prime number. Consider using the Jacobi symbol instead.");
        }
        if (p == -2 || p == 2) {
            throw new IllegalArgumentException(p + " is not an odd prime. Consider using the Kronecker symbol instead.");
        }
        if (euclideanGCD(a, p) > 1) {
            return 0;
        }
        int oddPrime = Math.abs(p); // Making sure p is positive
        int exponent = (oddPrime - 1)/2;
        int modStop = oddPrime - 2;
        int adjA = a;
        if (adjA > (oddPrime - 1)) {
            adjA %= oddPrime;
        }
        if (adjA == (oddPrime - 1)) {
            adjA = -1;
        }
        while (adjA < -1) {
            adjA += oddPrime;
        }
        int power = adjA;
        for (int i = 1; i < exponent; i++) {
            power *= adjA;
            while (power > modStop) {
                power -= oddPrime;
            }
            while (power < -1) {
                power += oddPrime;
            }
        }
        return (byte) power;
    }
    
    // PLACEHOLDER FOR symbolJacobi
    // Uncomment the next three lines to get failing first test.
//    public static byte symbolJacobi(int a, int m) {
//        return -3;
//    }
    
    // PLACEHOLDER FOR symbolKronecker
    // Uncomment the next three lines to get failing first test.
//    public static byte symbolKronecker(int a, int m) {
//        return -4;
//    }
    
    /**
     * Determines whether a given number, not necessarily purely real, is prime 
     * or not.
     * @param num The number for which to make the determination.
     * @return true if the number is prime, false otherwise. For example, 1 + i,
     * which has a norm of 2, is prime.
     * @throws NonUniqueFactorizationDomainException If called upon to determine 
     * the primality of a number from a domain that is not a unique 
     * factorization domain. Generally this should only be a problem for purely 
     * real numbers that are prime in <b>Z</b>.
     * @throws ArithmeticException If a norm computation error occurs (this is a 
     * runtime exception).
     */
    public static boolean isPrime(ImaginaryQuadraticInteger num) throws NonUniqueFactorizationDomainException {
        if (num.norm() < 0) {
            String exceptionMessage = "Overflow has occurred for the computation of the norm of " + num.toASCIIString();
            throw new ArithmeticException(exceptionMessage);
        }
        if (isPrime(num.norm())) {
            return true;
        } else {
            if (num.imagPartMult == 0) {
                int absRealPartMult = Math.abs(num.realPartMult);
                if (isPrime(absRealPartMult)) {
                    switch (num.imagQuadRing.negRad) {
                        case -1:
                            return (absRealPartMult % 4 == 3);
                        case -2:
                            return (absRealPartMult % 8 == 5 || absRealPartMult % 8 == 7);
                        case -3:
                            return (absRealPartMult % 3 == 2);
                        case -7:
                            return (absRealPartMult != 2 || absRealPartMult != 7);
                        case -11:
                        case -19:
                        case -43:
                        case -67:
                        case -163:
                            return symbolLegendre(absRealPartMult, num.imagQuadRing.negRad) == -1;
                        default:
                            String exceptionMessage = num.imagQuadRing.toASCIIString() + " is not a unique factorization domain.";
                            throw new NonUniqueFactorizationDomainException(exceptionMessage, num);
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
    }
    
    /**
     * Determines whether a given number is irreducible, not necessarily prime.
     * @param num The number for which to make the determination.
     * @return true if num is irreducible, false if not. For example, 1 + 
     * sqrt(-5) is famously irreducible but not prime.
     * @throws ArithmeticException If a norm computation error occurs (this is a 
     * runtime exception).
     */
    public static boolean isIrreducible(ImaginaryQuadraticInteger num) {
        if (num.norm() < 0) {
            String exceptionMessage = "Overflow has occurred for the computation of the norm of " + num.toASCIIString();
            throw new ArithmeticException(exceptionMessage);
        }
        if (isPrime(num.norm())) {
            return true;
        } else {
            if (num.norm() < 2) {
                return true;
            } else {
                switch (num.imagQuadRing.negRad) {
                    case -1:
                    case -2:
                    case -3:
                    case -7:
                    case -11:
                    case -19:
                    case -43:
                    case -67:
                    case -163:
                        try {
                            return isPrime(num);
                        } catch (NonUniqueFactorizationDomainException nufde) {
                            System.err.println("NonUniqueFactorizationDomainException should not have happened in this context: " + nufde.getMessage());
                            System.exit(-1);
                        }
                    default:
                        boolean withinRange = true;
                        boolean presumedIrreducible = true;
                        ImaginaryQuadraticInteger testDivisor, currDivision;
                        int maxTestDivNorm = num.norm() - 1;
                        int currTestDivNorm;
                        int testDivRealPartMult = 2;
                        int testDivImagPartMult = 0;
                        while (withinRange && presumedIrreducible) {
                            testDivisor = new ImaginaryQuadraticInteger(testDivRealPartMult, testDivImagPartMult, num.imagQuadRing);
                            currTestDivNorm = testDivisor.norm();
                            withinRange = (currTestDivNorm < maxTestDivNorm);
                            while (withinRange && presumedIrreducible) {
                                try {
                                    currDivision = num.divides(testDivisor);
                                    presumedIrreducible = false;
                                } catch (AlgebraicDegreeOverflowException adoe) {
                                    System.err.println("AlgebraicDegreeOverflowException should not have happened in this context. " + adoe.getMessage());
                                    System.exit(-1);
                                } catch (NotDivisibleException nde) {
                                    testDivRealPartMult++;
                                    testDivisor = new ImaginaryQuadraticInteger(testDivRealPartMult, testDivImagPartMult, num.imagQuadRing);
                                    currTestDivNorm = testDivisor.norm();
                                    withinRange = (currTestDivNorm < maxTestDivNorm);
                                }
                            }
                            testDivRealPartMult = 0;
                            testDivImagPartMult++;
                        }
                        /* If the number is from a domain with "half-integers," 
                           we should try dividing by some of those, too */
                        if (presumedIrreducible && num.imagQuadRing.d1mod4) {
                            withinRange = true;
                            testDivRealPartMult = 1;
                            testDivImagPartMult = 1;
                            while (withinRange && presumedIrreducible) {
                                testDivisor = new ImaginaryQuadraticInteger(testDivRealPartMult, testDivImagPartMult, num.imagQuadRing, 2);
                                currTestDivNorm = testDivisor.norm();
                                withinRange = (currTestDivNorm < maxTestDivNorm);
                                while (withinRange && presumedIrreducible) {
                                    try {
                                        currDivision = num.divides(testDivisor);
                                        presumedIrreducible = false;
                                    } catch (AlgebraicDegreeOverflowException adoe) {
                                        System.err.println("AlgebraicDegreeOverflowException should not have happened in this context. " + adoe.getMessage());
                                        System.exit(-1);
                                    } catch (NotDivisibleException nde) {
                                        testDivImagPartMult += 2;
                                        testDivisor = new ImaginaryQuadraticInteger(testDivRealPartMult, testDivImagPartMult, num.imagQuadRing, 2);
                                        currTestDivNorm = testDivisor.norm();
                                        withinRange = (currTestDivNorm < maxTestDivNorm);
                                    }
                                }
                                testDivRealPartMult = 1;
                                testDivImagPartMult += 2;
                            }
                        }
                        return presumedIrreducible;
                }
            }
        }
    }
    
    /**
     * Determines whether a given number is squarefree or not.
     * @param num The number to be tested for being squarefree.
     * @return true if the number is squarefree, false otherwise.
     * For example, -3 and 7 should each return true, -4, 0 and 25 should each 
     * return false.
     * Note that 1 is considered squarefree. Therefore, for num = 1, this 
     * function should return true.
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
     * @return 1 if num is squarefree with an even number of prime factors, -1 
     * if num is squarefree with an odd number of prime factors, 0 if num is not 
     * squarefree. Since -1 is a unit, not a prime, \u03BC(-n) = \u03BC(n). For 
     * example, \u03BC(31) = -1, \u03BC(32) = 0 and \u03BC(33) = 1.
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
     * Computes the greatest common divisor (GCD) of two purely real integers by 
     * using the Euclidean algorithm.
     * @param a One of the two integers. May be negative, need not be greater 
     * than the other.
     * @param b One of the two integers. May be negative, need not be smaller 
     * than the other.
     * @return The GCD as an integer.
     * If one of a or b is 0 and the other is nonzero, the result will be the 
     * nonzero number.
     * If both a and b are 0, then the result will be 0, which is perhaps 
     * technically wrong, but I think it's good enough for the purpose here.
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
     * Computes the greatest common divisor (GCD) of two purely real integers by 
     * using the Euclidean algorithm.
     * @param a One of the two integers. May be negative, need not be greater 
     * than the other.
     * @param b One of the two integers. May be negative, need not be smaller 
     * than the other.
     * @return The GCD as an integer. If one of a or b is 0 and the other is 
     * nonzero, the result will be the nonzero number. If both a and b are 0, 
     * then the result will be 0, which is perhaps technically wrong, but I 
     * think it's good enough for the purpose here.
     */
    public static long euclideanGCD(long a, long b) {
        long currA, currB, currRemainder;
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
     * Computes the greatest common divisor (GCD) of two imaginary quadratic 
     * integers by using the Euclidean algorithm. WARNING: I have not yet 
     * written a test for this function yet, so for now I can't guarantee that 
     * it works correctly, or at all.
     * @param a One of the two imaginary quadratic integers. Need not have 
     * greater norm than the other.
     * @param b One of the two imaginary quadratic integers. Need not have 
     * smaller norm than the other.
     * @return The GCD.
     * @throws AlgebraicDegreeOverflowException If the algebraic integers come 
     * from different quadratic rings, the GCD might be a number from a ring of 
     * degree 4 or higher. This may or may not be the case (quite likely the two 
     * algebraic integers will be coprime and so the answer is just good old 1); 
     * the function assumes that the GCD can't be calculated using the Euclidean 
     * algorithm and throws this checked exception.
     * @throws NonEuclideanDomainException If the algebraic integers come from 
     * any imaginary quadratic ring other than Z[i], Z[sqrt(-2)], Z[omega], 
     * O_Q(sqrt(-7)) or O_Q(sqrt(-11)), the function assumes the Euclidean GCD 
     * algorithm will fail without even trying, and throws this checked 
     * exception. However, for some pairs drawn from a non-Euclidean domain, the 
     * Euclidean GCD algorithm might nevertheless work. For this reason, the 
     * exception has (will have) the method tryEuclideanGCDAnyway().
     */
    public static ImaginaryQuadraticInteger euclideanGCD(ImaginaryQuadraticInteger a, ImaginaryQuadraticInteger b) throws AlgebraicDegreeOverflowException, NonEuclideanDomainException {
        if (((a.imagPartMult != 0) && (b.imagPartMult != 0)) && (a.imagQuadRing.negRad != b.imagQuadRing.negRad)) {
            throw new AlgebraicDegreeOverflowException("This operation would result in an algebraic integer of degree 4.", 2, 4);
        }
        if (a.imagQuadRing.negRad < -11 || a.imagQuadRing.negRad == -10 || a.imagQuadRing.negRad == -6 || a.imagQuadRing.negRad == -5) {
            String exceptionMessage = a.toASCIIString() + " and " + b.toASCIIString() + " are in non-Euclidean domain " + a.imagQuadRing.toFilenameString() + ".";
            throw new NonEuclideanDomainException(exceptionMessage, a, b);
        }
        ImaginaryQuadraticInteger currA, currB, tempMultiple, currRemainder;
        if (a.norm() < b.norm()) {
            currA = b;
            currB = a;
        } else {
            currA = a;
            currB = b;
        }
        while (!currB.equalsInt(0)) {
            try {
                tempMultiple = currA.divides(currB);
            } catch (AlgebraicDegreeOverflowException adoe) {
                tempMultiple = currA; // This is just to avoid "variable might not be initialized" error
            } catch (NotDivisibleException nde) {
                tempMultiple = nde.roundTowardsZero();
            }
            try {
                tempMultiple = tempMultiple.times(currB);
                currRemainder = currA.minus(tempMultiple);
            } catch (AlgebraicDegreeOverflowException adoe) {
                currRemainder = currB; // Avoiding "variable might not be initialized" error
            }
            currA = currB;
            currB = currRemainder;
        }
        // Make sure both real and imaginary parts of currA are positive or 0, that is, not negative
        if (currA.realPartMult < 0 && currA.imagPartMult < 0) {
            currA = currA.times(-1);
        }
        if (currA.realPartMult < 0 || currA.imagPartMult < 0) {
            int absRealPart = Math.abs(currA.realPartMult);
            int absImagPart = Math.abs(currA.imagPartMult);
            int currDenom = currA.denominator;
            currA = new ImaginaryQuadraticInteger(absRealPart, absImagPart, currB.imagQuadRing, currDenom);
        }
        return currA;
    }
    
    /**
     * Provides a pseudorandom negative squarefree integer.
     * @param bound The lowest number desired (but may use a positive integer). 
     * For example, for a pseudorandom squarefree number between -97 and -1, you 
     * can pass -100 or 100.
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
            randomNumber++;
        }
        return randomNumber;
    }
    
    /**
     * A console program for testing the number theoretic functions with user 
     * inputs.
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