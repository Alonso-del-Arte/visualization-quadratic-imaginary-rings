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

/**
 * This checked exception is to be thrown when an Euclidean GCD function is 
 * called on numbers that are not from an Euclidean domain. If the numbers are 
 * from two different non-Euclidean domains, AlgebraicDegreeOverflowException 
 * should be thrown instead.
 * @author Alonso del Arte
 */
public class NonEuclideanDomainException extends Exception {
    
    private static final long serialVersionUID = 1058267512;
        
    private final ImaginaryQuadraticInteger attemptedA, attemptedB;
    
    public ImaginaryQuadraticInteger[] getEuclideanGCDAttemptedNumbers() {
        return (new ImaginaryQuadraticInteger[]{attemptedA, attemptedB});
    }
    
//    public ImaginaryQuadraticInteger tryEuclideanGCDAnyway() {
//        //   (* This is going to be a function that tries to take the Euclidean GCD algorithm as far as possible.
//        //      Sometimes there will be a result, other times not. *)
//        int negRad = this.attemptedA.getRing().getNegRad();
//        if (negRad != this.attemptedB.getRing().getNegRad()) {
//            throw new AlgebraicDegreeOverflowException("euclideanGCD should have thrown AlgebraicDegreeOverflowException, not NonEuclideanDomainException.", 2, this.attemptedA, this.attemptedB);
//        }
//        ImaginaryQuadraticInteger attemptedEuclideanGCD;
//        attemptedEuclideanGCD = this.attemptedA;
//        //   (* The actual logic of attempting the Euclidean algorithm will go here *)
//        return attemptedEuclideanGCD;
//    }

    /**
     * This is an exception to be thrown by an Euclidean GCD function if called 
     * upon a and b in a ring of Q(sqrt(d)) for d other than the ones listed in 
     * {@link NumberTheoreticFunctionsCalculator#NORM_EUCLIDEAN_QUADRATIC_IMAGINARY_RINGS_D}.
     * @param message Should probably just be something like 
     * a.getRing().toString() + " is not an Euclidean domain." This message 
     * is just passed on to the superclass.
     * @param a One of the two algebraic integers for which the request to 
     * compute the Euclidean GCD was declined. If desired, the calling function 
     * may choose the number with larger norm to be a, but this is not required. 
     * However, a and b ought to be in the same ring, otherwise 
     * {@link AlgebraicDegreeOverflowException} should have been used instead.
     * @param b One of the two algebraic integers for which the request to 
     * compute the Euclidean GCD was declined. If desired, the calling function 
     * may choose the number with smaller norm to be b, but this is not 
     * required. However, b and a ought to be in the same ring, otherwise 
     * {@link AlgebraicDegreeOverflowException} should have been used instead.
     */
    public NonEuclideanDomainException(String message, ImaginaryQuadraticInteger a, ImaginaryQuadraticInteger b) {
        super(message);
        this.attemptedA = a;
        this.attemptedB = b;
    }
    
}
