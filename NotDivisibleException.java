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
 * An exception to indicate when the division of one algebraic integer by another algebraic integer results in an algebraic number that is in the relevant field but not the relevant ring.
 * For example, sqrt(-2)/3 is in Q(sqrt(-2)) but not Z[sqrt(-2)].
 * This is the wrong exception to throw for division by 0.
 * Throwing this exception implies the result of a division is an algebraic number but not an algebraic integer.
 * Whatever we think an algebraic integer divided by 0 is, it is neither algebraic number nor algebraic integer.
 * Also, throwing this exception implies the result of a division can be rounded to an algebraic integer nearby in the relevant ring.
 * @author Alonso del Arte
 */
public class NotDivisibleException extends Exception {
    
    private static final long serialVersionUID = 1;
    private final long resultingFractionRealPartNumerator;
    private final long resultingFractionImagPartNumerator;
    private final long resultingFractionDenominator;
    private final int resultingFractionNegRad;
    
    /**
     * Gives the numerator of the real part of the resulting fraction.
     * @return The integer for the real part supplied at the time the exception was constructed.
     */
    public long getResReFractNumer() {
        return resultingFractionRealPartNumerator;
    }

    /**
     * Gives the numerator of the imaginary part of the resulting fraction.
     * @return The integer for the real part supplied at the time the exception was constructed.
     */
    public long getResImFractNumer() {
        return resultingFractionImagPartNumerator;
    }
   
    /**
     * Gives the denominator of the resulting fraction.
     * @return The integer supplied at the time the exception was constructed. It may be almost any integer, but most certainly it should not be 0.
     */
    public long getResFractDenom() {
        return resultingFractionDenominator;
    }
    
    /**
     * Gives the negative integer in the radical in the numerator of the resulting fraction.
     * @return The integer supplied at the time the exception was constructed. It ought to be a negative, squarefree integer.
     */
    public int getResFractNegRad() {
        return resultingFractionNegRad;
    }
    
    // TODO: REPLACE PLACEHOLDER FUNCTION
    public ImaginaryQuadraticInteger roundTowardsZero() {
        return new ImaginaryQuadraticInteger(0, 0, new ImaginaryQuadraticRing(-1), 1);
    }
    
    // I'M THINKING OF INCLUDING FOUR OR SIX ROUNDING FUNCTIONS.
    // So part of what is holding me back is figuring out what to call these functions.

    /**
     * This exception should be thrown when a division operation takes the resulting number out of the ring, to the larger field.
     * If the result is an algebraic number of degree 4, perhaps AlgebraicDegreeOverflowException should be thrown instead.
     * And if there is an attempt to divide by 0, the appropriate exception to throw would be IllegalArgumentException.
     * @param message A message to pass on to the Exception constructor.
     * @param resFractReNumer The numerator of the real part of the resulting fraction. For example, given 7/3 + 2 * sqrt(-5)/3, this parameter would be 7.
     * @param resFractImNumer The numerator of the imaginary part of the resulting fraction. For example, given 7/3 + 2 * sqrt(-5)/3, this parameter would be 2.
     * @param resFractDenom The denominator of the resulting fraction. For example, given 7/3 + 2 * sqrt(-5)/3, this parameter would be 3.
     * @param resFractNegRad The negative integer in the radical in the numerator of the resulting fraction. For example, given 7/3 + 2 * sqrt(-5)/3, this parameter would be -5.
     */
    public NotDivisibleException(String message, long resFractReNumer, long resFractImNumer, long resFractDenom, int resFractNegRad) {
        super(message);
        resultingFractionRealPartNumerator = resFractReNumer;
        resultingFractionImagPartNumerator = resFractImNumer;
        resultingFractionDenominator = resFractDenom;
        resultingFractionNegRad = resFractNegRad;
    }
}
