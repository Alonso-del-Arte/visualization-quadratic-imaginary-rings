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
import java.util.Random;

/**
 * A collection of number theoretic functions, including basic primality testing 
 * and the Euclidean GCD algorithm.
 * @author Alonso del Arte
 */
public class NumberTheoreticFunctionsCalculator {
    
    /**
     * The only five values d such that new ImaginaryQuadraticRing(d) represents 
     * a norm-Euclidean domain. These numbers, -11, -7, -3, -2, -1, are the 
     * first five terms listed in <a href="http://oeis.org/A048981">Sloane's 
     * A048981</a>.  numbers correspond to <b>Z</b>[i], <b>Z</b>[&radic;-2], 
     * <b>Z</b>[&omega;], <i>O</i><sub><b>Q</b>(&radic;-7)</sub> and 
     * <i>O</i><sub><b>Q</b>(&radic;-11)</sub>.
     */
    public static final int[] NORM_EUCLIDEAN_QUADRATIC_IMAGINARY_RINGS_D = {-11, -7, -3, -2, -1};
    
    /**
     * There are the only nine negative numbers d such that the ring of 
     * algebraic integers of <b>Q</b>(&radic;d) is a unique factorization domain 
     * (though not necessarily Euclidean). These numbers are, in descending 
     * order: -1, -2, -3, -7, -11, -19, -43, -67, -163 (in this constant array, 
     * they are in the reverse order). These correspond to <b>Z</b>[i], 
     * <b>Z</b>[&radic;-2], <b>Z</b>[&omega;], 
     * <i>O</i><sub><b>Q</b>(&radic;-7)</sub>, 
     * <i>O</i><sub><b>Q</b>(&radic;-11)</sub>, 
     * <i>O</i><sub><b>Q</b>(&radic;-19)</sub>, 
     * <i>O</i><sub><b>Q</b>(&radic;-43)</sub>, 
     * <i>O</i><sub><b>Q</b>(&radic;-67)</sub> and 
     * <i>O</i><sub><b>Q</b>(&radic;-163)</sub>.
     */
    public static final int[] HEEGNER_NUMBERS = {-163, -67, -43, -19, -11, -7, -3, -2, -1};
    
    public static final ImaginaryQuadraticRing RING_GAUSSIAN = new ImaginaryQuadraticRing(-1);
    public static final ImaginaryQuadraticInteger IMAG_UNIT_I = new ImaginaryQuadraticInteger(0, 1, RING_GAUSSIAN);
    public static final ImaginaryQuadraticInteger IMAG_UNIT_NEG_I = IMAG_UNIT_I.times(-1);
    
    public static final ImaginaryQuadraticRing RING_EISENSTEIN = new ImaginaryQuadraticRing(-3);
    public static final ImaginaryQuadraticInteger COMPLEX_CUBIC_ROOT_OF_UNITY = new ImaginaryQuadraticInteger(-1, 1, RING_EISENSTEIN, 2);

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
                return false;
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
     * Determines whether a given purely real number is prime or not.
     * @param num The number to be tested for primality.
     * @return True if the number is prime, false otherwise.
     */
    public static boolean isPrime(long num) {
        if (num == -1 || num == 0 || num == 1) {
            return false;
        }
        if (num == -2 || num == 2) {
            return true;
        }
        if (num % 2 == 0) {
            return false;
        } else {
            boolean primeFlag = true;
            long potentialFactor = 3;
            double numSqrt = Math.sqrt(Math.abs(num));
            while (primeFlag && potentialFactor <= numSqrt) {
                primeFlag = (num % potentialFactor != 0);
                potentialFactor += 2;
            }
            return primeFlag;
        }
    }
        
    /**
     * The Legendre symbol, a number theoretic function which tells if a given 
     * number is a quadratic residue modulo an odd prime. There is no overflow 
     * checking, but hopefully that's only a problem for numbers that are very 
     * close to {@link Integer#MIN_VALUE} or {@link Integer#MAX_VALUE}.
     * @param a The number to test for being a quadratic residue modulo an odd 
     * prime. For example, 10.
     * @param p The odd prime to test a for being a quadratic residue modulo of. 
     * For example, 7. This parameter may be negative; the function will quietly 
     * change it to a positive number; this behavior is not guaranteed for 
     * future versions of this program.
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
    
    /**
     * The Jacobi symbol, a number theoretic function. This implementation is 
     * almost entirely dependent on the Legendre symbol.
     * @param n Parameter n, for example, 8.
     * @param m Parameter m, for example, 15.
     * @return The result, for example, 1.
     * @throws IllegalArgumentException If m is even or negative (or both). Note 
     * that this is a runtime exception.
     */
    public static byte symbolJacobi(int n, int m) {
        if (m % 2 == 0) {
            throw new IllegalArgumentException(m + " is not an odd number. Consider using the Kronecker symbol instead.");        }
        if (m < 0) {
            throw new IllegalArgumentException(m + " is not a positive number. Consider using the Kronecker symbol instead.");
        }
        if (m == 1) {
            return 1;
        }
        if (euclideanGCD(n, m) > 1) {
            return 0;
        }
        List<Integer> mFactors = primeFactors(m);
        byte symbol = 1;
        for (Integer mFactor : mFactors) {
            symbol *= symbolLegendre(n, mFactor);
        }
        return symbol;
    }
    
    private static byte symbolKroneckerNegOne(int n) {
        if (n < 0) {
            return -1;
        } else {
            return 1;
        }
    }
    
    private static byte symbolKroneckerTwo(int n) {
        int nMod8 = n % 8;
        switch (nMod8) {
            case -7:
            case -1:
            case 1:
            case 7:
                return 1;
            case -5:
            case -3:
            case 3:
            case 5:
                return -1;
            default:
                return 0;
        }
    }
    
    /**
     * The Kronecker symbol, a number theoretic function. This implementation 
     * relies in great part on the Legendre symbol, but not at all on the Jacobi 
     * symbol.
     * @param n Parameter n, for example, 3.
     * @param m Parameter m, for example, 2.
     * @return The result, for example, -1.
     */
    public static byte symbolKronecker(int n, int m) {
        if (euclideanGCD(n, m) > 1) {
            return 0;
        }
        if (m == 1) {
            return 1;
        }
        if (m == 0) {
            if (n == -1 || n == 1) {
                return 1;
            } else {
                return 0;
            }
        }
        List<Integer> mFactors = primeFactors(m);
        int currMFactorIndex = 0;
        int kindaOmega = mFactors.size();
        byte symbol = 1;
        if (mFactors.get(currMFactorIndex) == -1) {
            symbol *= symbolKroneckerNegOne(n);
            currMFactorIndex++;
        }
        int currFactor;
        boolean keepGoing = true; // Keep going with Kronecker(n, 2)?
        while (currMFactorIndex < kindaOmega && keepGoing) {
            currFactor = mFactors.get(currMFactorIndex);
            keepGoing = (currFactor == 2);
            if (keepGoing) {
                symbol *= symbolKroneckerTwo(n);
                currMFactorIndex++;
            }
        }
        while (currMFactorIndex < kindaOmega) {
            currFactor = mFactors.get(currMFactorIndex);
            symbol *= symbolLegendre(n, currFactor);
            currMFactorIndex++;
        }
        return symbol;
    }
    
    /**
     * Determines whether a given number, not necessarily purely real, is prime 
     * or not. Note that an early version of this function would throw 
     * {@link NonUniqueFactorizationDomainException} if called upon a purely 
     * real integer in a non-UFD. That is no longer the case.
     * @param num The number for which to make the determination.
     * @return true if the number is prime, false otherwise. For example, 1 + i,
     * which has a norm of 2, is prime.
     * @throws ArithmeticException If a norm computation error occurs (this is a 
     * runtime exception).
     */
    public static boolean isPrime(ImaginaryQuadraticInteger num) {
        if (num.norm() < 0) {
            String exceptionMessage = "Overflow has occurred for the computation of the norm of " + num.toASCIIString();
            throw new ArithmeticException(exceptionMessage);
        }
        if (isPrime(num.norm())) {
            return true;
        } else {
            if (num.imagQuadRing.negRad == -1 && num.realPartMult == 0) {
                return ((Math.abs(num.imagPartMult)) % 4 == 3);
            }
            if (num.imagQuadRing.negRad == -3 && num.imagPartMult != 0) {
                ImaginaryQuadraticInteger pureReal = num.times(COMPLEX_CUBIC_ROOT_OF_UNITY);
                if (pureReal.imagPartMult != 0) {
                    pureReal = pureReal.times(COMPLEX_CUBIC_ROOT_OF_UNITY);
                }
                if (pureReal.imagPartMult == 0) {
                    if (pureReal.realPartMult < 0) {
                        pureReal = pureReal.times(-1);
                    }
                    if (pureReal.realPartMult % 3 == 2) {
                        return true;
                    }
                }
            }
            if (num.imagPartMult == 0) {
                int absRealPartMult = Math.abs(num.realPartMult);
                if (absRealPartMult == 2) {
                    return (symbolKronecker(num.imagQuadRing.negRad, 2) == -1);
                }
                if (isPrime(absRealPartMult)) {
                    switch (num.imagQuadRing.negRad) {
                        case -1:
                            return (absRealPartMult % 4 == 3);
                        case -2:
                            return (absRealPartMult % 8 == 5 || absRealPartMult % 8 == 7);
                        case -3:
                            return (absRealPartMult % 3 == 2);
//                        case -7:
//                        case -11:
//                        case -19:
//                        case -43:
//                        case -67:
//                        case -163:
//                            return (symbolLegendre(num.imagQuadRing.negRad, absRealPartMult) == -1);
                        default:
                            return (symbolLegendre(num.imagQuadRing.negRad, absRealPartMult) == -1);
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
     * Sorts a list of imaginary quadratic integers in ascending order by norm. 
     * Do note that integers of the same might come back in the same order 
     * relative to each other that they were originally in. The sorting is done 
     * with a rudimentary bubble sort algorithm, so do not use this if speed or 
     * efficiency are required.
     * @param listIQI A list of imaginary quadratic integers, which may be in 
     * any order whatsoever. For example: -1 + i, 4 + i, -i, 1 - i.
     * @return A list of the imaginary quadratic integers sorted by norm. For 
     * example: -i, -1 + i, 1 - i, 4 + i. Note that there is no checking of norm 
     * overflows, so imaginary quadratic integer objects with erroneously 
     * negative norms would be erroneously sorted before units.
     */
    static List<ImaginaryQuadraticInteger> sortListIQIByNorm(List<ImaginaryQuadraticInteger> listIQI) {
        boolean swapFlag;
        ImaginaryQuadraticInteger a, b;
        List<ImaginaryQuadraticInteger> nums = new ArrayList<>();
        for (ImaginaryQuadraticInteger iqi : listIQI) {
            nums.add(iqi);
        }
        int opLen = listIQI.size() - 1;
        if (opLen > 0) {
            do {
                swapFlag = false;
                for (int counter = 0; counter < opLen; counter++) {
                    a = nums.get(counter);
                    b = nums.get(counter + 1);
                    if (a.norm() > b.norm()) {
                        nums.set(counter, b);
                        nums.set(counter + 1, a);
                        swapFlag = true;
                    }
                }
                a = nums.get(opLen);
                b = nums.get(0);
                if (a.norm() < b.norm()) {
                    nums.set(opLen, b);
                    nums.set(0, a);
                    swapFlag = true;
                }
            } while (swapFlag);
        }
        return nums;
    }
    
    /**
     * Computes the prime factors, and unit factors when applicable, of an 
     * imaginary quadratic integer from a unique factorization domain (UFD).
     * @param num The imaginary quadratic integer to find the factors of. For 
     * example, -4 + 3sqrt(-19).
     * @return A list of imaginary quadratic integers, with the first possibly 
     * being a unit, the rest should be primes. For example, -1, 5/2 - 
     * sqrt(-19)/2, 7/2 - sqrt(-19)/2, which multiply to -4 + 3sqrt(-19).
     * @throws NonUniqueFactorizationDomainException If called upon to compute 
     * the prime factors of a number from a non-UFD, even if a complete 
     * factorization into primes is possible in the given domain, e.g., 5 and 41 
     * in Z[sqrt(-5)].
     */
    public static List<ImaginaryQuadraticInteger> primeFactors(ImaginaryQuadraticInteger num) throws NonUniqueFactorizationDomainException {
        int d = num.getRing().getNegRad();
        boolean notUFDFlag = true;
        if (d > -164) {
            for (int heegNum : HEEGNER_NUMBERS) {
                if (d == heegNum) {
                    notUFDFlag = false;
                }
            }
        }
        if (notUFDFlag) {
            String exceptionMessage = num.getRing().toASCIIString() + " is not a unique factorization domain.";
            throw new NonUniqueFactorizationDomainException(exceptionMessage, num);
        }
        if (num.norm() < 0) {
            String exceptionMessage = "A norm computation error occurred for " + num.toASCIIString() + ", which should not have norm " + num.norm();
            throw new ArithmeticException(exceptionMessage);
        }
        ImaginaryQuadraticInteger n = num;
        List<ImaginaryQuadraticInteger> factors = new ArrayList<>();
        if (n.norm() < 2) {
            factors.add(n);
            return factors;
        }
        if (isPrime(n)) {
            factors.add(new ImaginaryQuadraticInteger(1, 0, num.getRing()));
            factors.add(n);
        } else {
            ImaginaryQuadraticInteger testDivisor = new ImaginaryQuadraticInteger(2, 0, n.getRing());
            boolean keepGoing = true;
            if (isPrime(testDivisor)) {
                while (n.norm() % 4 == 0) {
                    try {
                        n = n.divides(testDivisor);
                        factors.add(testDivisor);
                    } catch (NotDivisibleException nde) {
                        keepGoing = ((Math.abs(nde.getNumericRealPart()) > 1) || (Math.abs(nde.getNumericImagPart()) > 1));
                    }
                }
            }
            testDivisor = testDivisor.plus(1);
            while ((n.norm() > testDivisor.norm()) && keepGoing) {
                if (isPrime(testDivisor)) {
                    while (n.norm() % testDivisor.norm() == 0) {
                        try {
                            n = n.divides(testDivisor);
                            factors.add(testDivisor);
                        } catch (NotDivisibleException nde) {
                            keepGoing = ((Math.abs(nde.getNumericRealPart()) > 1) || (Math.abs(nde.getNumericImagPart()) > 1));
                        }
                    }
                }
                testDivisor = testDivisor.plus(2);
            }
            int testDivRealPartMult = 0;
            int testDivImagPartMult = 2;
            if (n.getRing().hasHalfIntegers()) {
                testDivRealPartMult = 1;
                testDivImagPartMult = 1;
            }
            boolean withinRange;
            while (n.norm() > 1) {
                testDivisor = new ImaginaryQuadraticInteger(testDivRealPartMult, testDivImagPartMult, n.getRing(), 2);
                withinRange = (testDivisor.norm() < n.norm());
                if (isPrime(testDivisor)) {
                    while (n.norm() % testDivisor.norm() == 0) {
                        try {
                            n = n.divides(testDivisor.conjugate());
                            factors.add(testDivisor.conjugate());
                        } catch (NotDivisibleException nde) {
                            // withinRange = withinRange && ((Math.abs(nde.getNumericRealPart()) > 1) || (Math.abs(nde.getNumericImagPart()) > 1));
                        }
                        try {
                            n = n.divides(testDivisor);
                            factors.add(testDivisor);
                        } catch (NotDivisibleException nde) {
                            // withinRange = withinRange && ((Math.abs(nde.getNumericRealPart()) > 1) || (Math.abs(nde.getNumericImagPart()) > 1));
                        }
                    }
                }
                if (withinRange) {
                    testDivRealPartMult += 2;
                } else {
                    if (!num.getRing().hasHalfIntegers()) {
                        testDivRealPartMult = 0;
                        testDivImagPartMult += 2;
                    } else {
                        if (testDivImagPartMult % 2 == 0) {
                            testDivRealPartMult = 1;
                        } else {
                            testDivRealPartMult = 0;
                        }
                        testDivImagPartMult++;
                    }
                }
            }
            factors.add(n); // This should be a unit, most likely -1 or 1
        }
        factors = sortListIQIByNorm(factors);
        for (int i = 1; i < factors.size(); i++) {
            if (factors.get(i).getRealPartMult() < 0 || (factors.get(i).getRealPartMult() == 0 && factors.get(i).getImagPartMult() < 0)) {
                factors.set(i, factors.get(i).times(-1));
                factors.set(0, factors.get(0).times(-1));
            }
        }
        if (factors.get(0).equalsInt(1)) {
            factors.remove(0);
        }
        return factors;
    }
    
    /**
     * Determines whether a given number is irreducible, not necessarily prime.
     * @param num The number for which to make the determination.
     * @return true if num is irreducible, false if not. For example, 1 + 
     * sqrt(-5) is famously irreducible but not prime. Also, units are 
     * considered irreducible by this function.
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
                        return isPrime(num);
                    default:
                        boolean withinRange = true;
                        boolean presumedIrreducible = true;
                        ImaginaryQuadraticInteger testDivisor, currDivision;
                        int testDivRealPartMult = 4;
                        int testDivImagPartMult = 0;
                        boolean testDivisorChanged;
                        while (withinRange && presumedIrreducible) {
                            testDivisor = new ImaginaryQuadraticInteger(testDivRealPartMult, testDivImagPartMult, num.imagQuadRing, 2);
                            testDivisorChanged = false;
                            withinRange = (testDivisor.norm() < num.norm());
                            while (withinRange && presumedIrreducible) {
                                try {
                                    currDivision = num.divides(testDivisor);
                                    if (currDivision.norm() > 1) {
                                        presumedIrreducible = false;
                                    } else {
                                        withinRange = false;
                                    }
                                } catch (NotDivisibleException nde) {
                                    withinRange = (Math.abs(nde.getNumericRealPart()) > 1) || (Math.abs(nde.getNumericImagPart()) > 1);
                                    testDivRealPartMult += 2;
                                    testDivisor = new ImaginaryQuadraticInteger(testDivRealPartMult, testDivImagPartMult, num.imagQuadRing, 2);
                                    testDivisorChanged = true;
                                }
                            }
                            if (!num.imagQuadRing.d1mod4) {
                                testDivRealPartMult = 0;
                                testDivImagPartMult += 2;
                            } else {
                                if (testDivImagPartMult % 2 == 0) {
                                    testDivRealPartMult = 1;
                                } else {
                                    testDivRealPartMult = 0;
                                }
                                testDivImagPartMult++;
                            }
                            if (testDivisorChanged) {
                                withinRange = true;
                            }
                        }
                        return presumedIrreducible;
                }
            }
        }
    }
    
    /**
     * Determines whether a given number is squarefree or not. The original 
     * implementation depended on {@link #primeFactors(int)}. For version 0.95, 
     * this was optimized to try the number modulo 4, and if it's not divisible 
     * by 4, to try dividing it by odd squares. Although this includes odd 
     * squares like 9 and 81, it still makes for a performance improvement over 
     * relying on primeFactors(int).
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
                boolean noDupFactorFound = (num % 4 != 0);
                if (noDupFactorFound) {
                    double threshold = Math.sqrt(Math.abs(num));
                    int currRoot = 3;
                    int currSquare;
                    do {
                        currSquare = currRoot * currRoot;
                        noDupFactorFound = (num % currSquare != 0);
                        currRoot += 2;
                    } while (noDupFactorFound && currRoot <= threshold);
                }
                return noDupFactorFound;
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
     * integers by using the Euclidean algorithm. WARNING: Although I have 
     * written some tests for this function, more testing is still needed before 
     * I can guarantee it gives the correct result in a reasonable majority of 
     * cases.
     * @param a One of the two imaginary quadratic integers. Need not have 
     * greater norm than the other.
     * @param b One of the two imaginary quadratic integers. Need not have 
     * smaller norm than the other.
     * @return The GCD.
     * @throws AlgebraicDegreeOverflowException If the imaginary quadratic 
     * integers come from different quadratic rings, the GCD might be a number 
     * from a ring of degree 4 or higher. This may or may not be the case (quite 
     * likely the two imaginary quadratic integers will be coprime and so the 
     * answer is just good old 1); the function assumes that the GCD can't be 
     * calculated using the Euclidean algorithm and throws this runtime 
     * exception.
     * @throws NonEuclideanDomainException If the imaginary quadratic integers 
     * come from any imaginary quadratic ring other than <b>Z</b>[<i>i</i>], 
     * <b>Z</b>[&radic;-2], <b>Z</b>[&omega;], 
     * <i>O</i><sub><b>Q</b>(&radic;-7)</sub> or 
     * <i>O</i><sub><b>Q</b>(&radic;-11)</sub>, the function assumes the 
     * Euclidean GCD algorithm will fail without even trying, and throws this 
     * checked exception. However, for some pairs drawn from a non-Euclidean 
     * domain, the Euclidean GCD algorithm might nevertheless work. For this 
     * reason, the exception has (will have) the method tryEuclideanGCDAnyway().
     */
    public static ImaginaryQuadraticInteger euclideanGCD(ImaginaryQuadraticInteger a, ImaginaryQuadraticInteger b) throws NonEuclideanDomainException {
        int d = a.getRing().getNegRad();
        if (((a.getImagPartMult() != 0) && (b.getImagPartMult() != 0)) && (d != b.getRing().getNegRad())) {
            String exceptionMessage = "This operation would result in an algebraic integer of degree 4.";
            throw new AlgebraicDegreeOverflowException(exceptionMessage, 2, a, b);
        }
        if (d < -11 || d == -10 || d == -6 || d == -5) {
            String exceptionMessage = a.toASCIIString() + " and " + b.toASCIIString() + " are in non-Euclidean domain " + a.getRing().toASCIIString() + ".";
            throw new NonEuclideanDomainException(exceptionMessage, a, b);
        }
        ImaginaryQuadraticInteger currA, currB, tempMultiple, currRemainder;
        ImaginaryQuadraticInteger[] bounds;
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
                tempMultiple = tempMultiple.times(currB);
                currRemainder = currA.minus(tempMultiple);
            } catch (NotDivisibleException nde) {
                bounds = nde.getBoundingIntegers();
                boolean notFound;
                int counter = 0;
                do {
                    tempMultiple = bounds[counter].times(currB);
                    currRemainder = currA.minus(tempMultiple);
                    notFound = currRemainder.norm() >= currB.norm();
                    counter++;
                } while (notFound);
            }
            currA = currB;
            currB = currRemainder;
        }
        if (d == -1 && currA.getRealPartMult() == 0) {
            currA = currA.times(IMAG_UNIT_NEG_I);
        }
        if (currA.getRealPartMult() < 0) {
            currA = currA.times(-1);
        }
        return currA;
    }

    /**
     * Computes the greatest common divisor (GCD) of a purely real integer 
     * passed in as an int and an imaginary quadratic integer which may or may 
     * not have nonzero imaginary part, passed in as an 
     * ImaginaryQuadraticInteger. WARNING: Although I have written some tests 
     * for this function, more testing is still needed before I can guarantee it 
     * gives the correct result in a reasonable majority of cases.
     * @param a A purely real integer passed in as an int. For example, 4.
     * @param b An imaginary quadratic integer, which may be purely real, purely 
     * imaginary, or complex. For example, 3sqrt(-2).
     * @return The GCD. For example, sqrt(-2).
     * @throws NonEuclideanDomainException If the algebraic integers come from 
     * any imaginary quadratic ring other than Z[i], Z[&radic;-2], Z[\u03C9], 
     * O_Q(&radic;-7) or O_Q(&radic;-11), the function assumes the Euclidean GCD 
     * algorithm will fail without even trying, and throws this checked 
     * exception.
     */
    public static ImaginaryQuadraticInteger euclideanGCD(int a, ImaginaryQuadraticInteger b) throws NonEuclideanDomainException {
        ImaginaryQuadraticInteger wrappedA = new ImaginaryQuadraticInteger(a, 0, b.imagQuadRing);
        return euclideanGCD(wrappedA, b);
    }

    /**
     * Computes the greatest common divisor (GCD) of an imaginary quadratic 
     * integer which may or may not have nonzero imaginary part, passed in as an 
     * ImaginaryQuadraticInteger and a purely real integer passed in as an int. 
     * WARNING: Although I have written some tests for this function, more 
     * testing is still needed before I can guarantee it gives the correct 
     * result in a reasonable majority of cases.
     * @param a An imaginary quadratic integer, which may be purely real, purely 
     * imaginary, or complex. For example, 3sqrt(-2).
     * @param b A purely real integer passed in as an int. For example, 4.
     * @return The GCD. For example, sqrt(-2).
     * @throws NonEuclideanDomainException If the algebraic integers come from 
     * any imaginary quadratic ring other than Z[i], Z[&radic;-2], Z[\u03C9], 
     * O_Q(&radic;-7) or O_Q(&radic;-11), the function assumes the Euclidean GCD 
     * algorithm will fail without even trying, and throws this checked 
     * exception.
     */
    public static ImaginaryQuadraticInteger euclideanGCD(ImaginaryQuadraticInteger a, int b) throws NonEuclideanDomainException {
        ImaginaryQuadraticInteger wrappedB = new ImaginaryQuadraticInteger(b, 0, a.imagQuadRing);
        return euclideanGCD(a, wrappedB);
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
    
}