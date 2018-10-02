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
 * A runtime exception to indicate when the result of an arithmetic operation 
 * results in an algebraic integer which no currently available implementation 
 * of {@link AlgebraicInteger} can properly represent. However, if the result of 
 * the arithmetic operation is an algebraic integer of higher degree than any of 
 * the available implementations support, {@link 
 * AlgebraicDegreeOverflowException} should be used instead.
 * @author Alonso del Arte
 */
public class UnsupportedNumberDomainException extends RuntimeException {
    
    private static final long serialVersionUID = 1058341899;
    private final AlgebraicInteger diffRingNumberA;
    private final AlgebraicInteger diffRingNumberB;
    
    /**
     * Retrieves the two numbers that triggered this exception.
     * @return An array of two objects implementing the {@link AlgebraicInteger} 
     * interface. They could very well both be instances of the same class 
     * (e.g., both {@link ImaginaryQuadraticInteger}), but they should come from 
     * different rings. These are just the numbers that were supplied to the 
     * constructor.
     */
    public AlgebraicInteger[] getCausingNumbers() {
        return (new AlgebraicInteger[]{this.diffRingNumberA, this.diffRingNumberB});
    }
    
    /**
     * This exception should be thrown when the result of an arithmetic 
     * operation on two objects implementing the {@link AlgebraicInteger} 
     * results in an algebraic integer which no currently available 
     * implementation of AlgebraicInteer can handle.
     * @param message A message to pass on to the {@link Exception} constructor.
     * @param numberA One of the two numbers that caused the exception. It need 
     * not be smaller or larger than numberB in any sense (norm, absolute value, 
     * etc.) but it is expected to come from a different ring of algebraic 
     * integers. For example, sqrt(-2).
     * @param numberB One of the two numbers that caused the exception. It need 
     * not be larger or smaller than numberA in any sense (norm, absolute value, 
     * etc.) but it is expected to come from a different ring of algebraic 
     * integers. For example, sqrt(-7).
     */
    public UnsupportedNumberDomainException(String message, AlgebraicInteger numberA, AlgebraicInteger numberB) {
        super(message);
        diffRingNumberA = numberA;
        diffRingNumberB = numberB;
    }
    
}
