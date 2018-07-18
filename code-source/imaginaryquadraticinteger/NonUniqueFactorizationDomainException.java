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

/**
 * This checked exception is to be thrown when a prime factorization function is 
 * called on a number that is not from a unique factorization domain.
 * @author Alonso del Arte
 */
public class NonUniqueFactorizationDomainException extends Exception {
    
    private static final long serialVersionUID = 1058262425;
    
    private final ImaginaryQuadraticInteger unfactorizedNumber;
    
    /**
     * Just a getter for the algebraic integer that triggered this exception.
     * @return An imaginary quadratic integer object. Calling 
     * {@link ImaginaryQuadraticInteger#getRing()} should return a ring 
     * adjoining &radic;<i>d</i> with <i>d</i> being a negative, squarefree 
     * number other than those listed in 
     * {@link NumberTheoreticFunctionsCalculator#HEEGNER_NUMBERS}.
     */
    public ImaginaryQuadraticInteger getUnfactorizedNumber() {
        return this.unfactorizedNumber;
    }
    
    /**
     * Attempts to factorize the unfactorized number that triggered this 
     * exception.
     * @return A list of imaginary quadratic integers that should multiply to 
     * the previously unfactorized number. The list will include -1  at least 
     * twice if the algorithm encountered factors that are irreducible but not 
     * prime. For example, the factorization of 6 in <b>Z</b>[&radic;-5] might 
     * be given as -1 &times; -1 &times; 2 &times; 3, on account of the famous 
     * alternate factorization (1 - &radic;-5)(1 + &radic;-5).
     */
    public List<ImaginaryQuadraticInteger> tryToFactorizeAnyway() {
        ImaginaryQuadraticInteger n = this.unfactorizedNumber;
        ImaginaryQuadraticInteger unity = new ImaginaryQuadraticInteger(1, 0, n.getRing());
        ImaginaryQuadraticInteger negativeOne = unity.times(-1);
        List<ImaginaryQuadraticInteger> factors = new ArrayList<>();
        if (n.norm() < 2) {
            factors.add(n);
            return factors;
        }
        boolean keepGoing = true;
        if (NumberTheoreticFunctionsCalculator.isIrreducible(n)) {
            factors.add(unity);
            factors.add(n);
            if (!NumberTheoreticFunctionsCalculator.isPrime(n)) {
                factors.add(negativeOne);
                factors.add(negativeOne);
            }
        } else {
            ImaginaryQuadraticInteger testDivisor = new ImaginaryQuadraticInteger(2, 0, n.getRing());
            if (NumberTheoreticFunctionsCalculator.isIrreducible(testDivisor)) {
                while (n.norm() % 4 == 0) {
                    try {
                        n = n.divides(testDivisor);
                        factors.add(testDivisor);
                        if (!NumberTheoreticFunctionsCalculator.isPrime(testDivisor)) {
                            factors.add(negativeOne);
                            factors.add(negativeOne);
                        }
                    } catch (NotDivisibleException nde) {
                        // keepGoing = ((Math.abs(nde.getNumericRealPart()) > 1) || (Math.abs(nde.getNumericImagPart()) > 1));
                    }
                }
            }
            testDivisor = testDivisor.plus(1);
            while ((n.norm() >= testDivisor.norm()) && keepGoing) {
                if (NumberTheoreticFunctionsCalculator.isIrreducible(testDivisor)) {
                    while (n.norm() % testDivisor.norm() == 0 && keepGoing) {
                        try {
                            n = n.divides(testDivisor);
                            factors.add(testDivisor);
                            if (!NumberTheoreticFunctionsCalculator.isPrime(testDivisor)) {
                                factors.add(unity.times(-1));
                                factors.add(unity.times(-1));
                            }
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
                if (NumberTheoreticFunctionsCalculator.isIrreducible(testDivisor)) {
                    keepGoing = true;
                    while (n.norm() % testDivisor.norm() == 0 && keepGoing) {
                        try {
                            n = n.divides(testDivisor.conjugate());
                            factors.add(testDivisor.conjugate());
                            if (!NumberTheoreticFunctionsCalculator.isPrime(testDivisor)) {
                                factors.add(unity.times(-1));
                                factors.add(unity.times(-1));
                            }
                        } catch (NotDivisibleException nde) {
                            keepGoing = false;
                        }
                        try {
                            n = n.divides(testDivisor);
                            factors.add(testDivisor);
                            if (!NumberTheoreticFunctionsCalculator.isPrime(testDivisor)) {
                                factors.add(unity.times(-1));
                                factors.add(unity.times(-1));
                            }
                        } catch (NotDivisibleException nde) {
                            keepGoing = false;
                        }
                    }
                }
                if (withinRange) {
                    testDivRealPartMult += 2;
                } else {
                    if (!n.getRing().hasHalfIntegers()) {
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
        factors = NumberTheoreticFunctionsCalculator.sortListIQIByNorm(factors);
        int quadrantAdjustStart = 1;
        while (quadrantAdjustStart < factors.size() && keepGoing) {
            if (factors.get(quadrantAdjustStart).norm() == 1) {
                quadrantAdjustStart++;
            } else {
                keepGoing = false;
            }
        }
        for (int i = quadrantAdjustStart; i < factors.size(); i++) {
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
     * This is an exception to be potentially thrown by a prime factorization 
     * function if called upon to operate on a number from a domain that is not 
     * a unique factorization domain (UFD), such as those adjoining the square 
     * root of a negative number other than those listed in {@link 
     * NumberTheoreticFunctionsCalculator#HEEGNER_NUMBERS}. There are many more 
     * real quadratic integer rings that are UFDs, but that's outside the scope 
     * of this documentation.
     * @param message Should probably just be something like 
     * number.getRing().toString() + " is not a unique factorizaton domain." 
     * This message is just passed on to the superclass.
     * @param number The number sent to the prime factorization function, like, 
     * for example, 1 + sqrt(-30).
     */
    public NonUniqueFactorizationDomainException(String message, ImaginaryQuadraticInteger number) {
        super(message);
        this.unfactorizedNumber = number;
    }
    
}
