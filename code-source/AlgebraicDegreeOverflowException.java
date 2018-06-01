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

/**
 * A runtime exception to indicate when the result of an arithmetical operation 
 * results in an algebraic integer of higher algebraic degree than the 
 * implementation of AlgebraicInteger was designed for. For example, the square 
 * root of 2 times the fifth root of 3 is an algebraic integer with minimal 
 * polynomial x^10 - 288. So an AlgebraicInteger implementation for quadratic 
 * integers would be ill-suited to hold the result of the operation, as would an 
 * implementation that can handle algebraic integers up to algebraic degree 5. 
 * In such a case, it is appropriate to throw this exception. Note that this was 
 * originally a checked exception; later on I decided it made more sense as a 
 * runtime exception.
 * @author Alonso del Arte
 */
public class AlgebraicDegreeOverflowException extends RuntimeException {
    
    private static final long serialVersionUID = 1058208768;
    private final int maxExpectedAlgebraicDegree;
    private final int necessaryAlgebraicDegree;
    
    /**
     * Tells what is the maximum algebraic degree the function that threw the 
     * exception was expecting.
     * @return An integer greater than 1 but less than the necessary algebraic 
     * degree. For example, this would probably be 2 if thrown by 
     * {@link ImaginaryQuadraticInteger#plus}.
     */
    public int getMaxExpectedAlgebraicDegree() {
        return this.maxExpectedAlgebraicDegree;
    }
    
    /**
     * Tells what is the algebraic degree an object implementing 
     * AlgebraicInteger would need to handle to properly represent the result of 
     * the operation.
     * @return An integer greater than the expected algebraic degree. For 
     * example, this could be 4 if thrown by 
     * {@link ImaginaryQuadraticInteger#plus}.
     */
    public int getNecessaryAlgebraicDegree() {
        return this.necessaryAlgebraicDegree;
    }
    
    /**
     * This exception should be thrown when the result of an arithmetic 
     * operation on two objects implementing the {@link AlgebraicInteger} 
     * interface results in an algebraic integer of higher algebraic degree than 
     * either object can represent. For instance, if the multiplication of two 
     * quadratic integers represented by the {@link ImaginaryQuadraticInteger} 
     * class results in an algebraic integer of degree 4, it would be 
     * appropriate to throw this exception.
     * @param message A message to pass on to the {@link Exception} constructor.
     * @param maxDegree The maximum algebraic degree the object can handle 
     * (e.g., 2 in the case of ImaginaryQuadraticInteger).
     * @param necessaryDegree The algebraic degree the object should be capable 
     * of handling to properly represent the result (e.g., 4 in the case of two 
     * quadratic integers that multiply to an algebraic integer of degree 4).
     */
    public AlgebraicDegreeOverflowException(String message, int maxDegree, int necessaryDegree) {
        super(message);
        this.maxExpectedAlgebraicDegree = maxDegree;
        this.necessaryAlgebraicDegree = necessaryDegree;
    }
    
}
