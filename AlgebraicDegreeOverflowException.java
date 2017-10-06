/*
 * Copyright (C) 2017 AL
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
 *
 * @author Alonso del Arte
 */
public class AlgebraicDegreeOverflowException extends Exception {
    
    private static final long serialVersionUID = 1;
    private final int maxExpectedAlgebraicDegree;
    private final int necessaryAlgebraicDegree;
    
    public int getMaxExpectedAlgebraicDegree() {
        return maxExpectedAlgebraicDegree;
    }
    
    public int getNecessaryAlgebraicDegree() {
        return necessaryAlgebraicDegree;
    }
    
    /**
     * This exception should be thrown when the result of an arithmetic operation on two objects implementing the AlgebraicInteger interface results in an algebraic integer of higher algebraic degree than either object can represent.
     * For instance, if the multiplication of two quadratic integers represented by the ImaginaryQuadraticInteger class results in an algebraic integer of degree 4, it would be appropriate to throw this exception.
     * @param message A message to pass on to the Exception constructor.
     * @param maxDegree The maximum algebraic degree the object can handle (e.g., 2 in the case of ImaginaryQuadraticInteger).
     * @param necessaryDegree The algebraic degree the object should be capable of handling to properly represent the result (e.g., 4 in the case of two quadratic integers that multiply to an algebraic integer of degree 4).
     */
    public AlgebraicDegreeOverflowException(String message, int maxDegree, int necessaryDegree) {
        
        super(message);
        maxExpectedAlgebraicDegree = maxDegree;
        necessaryAlgebraicDegree = necessaryDegree;
        
    }
    
}
