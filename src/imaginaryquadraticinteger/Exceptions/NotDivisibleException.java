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
package imaginaryquadraticinteger.Exceptions;

import imaginaryquadraticinteger.AlgebraicInteger;
import imaginaryquadraticinteger.ImaginaryQuadraticInteger;
import imaginaryquadraticinteger.ImaginaryQuadraticRing;

/**
 * An exception to indicate when the division of one algebraic integer by 
 * another algebraic integer results in an algebraic number that is in the 
 * relevant field but not the relevant ring. For example, sqrt(-2)/3 is in 
 * Q(sqrt(-2)) but not Z[sqrt(-2)].
 * <p>This is the wrong exception to throw for division by 0. Throwing this 
 * exception implies the result of a division is an algebraic number but not an 
 * algebraic integer. Whatever we think an algebraic integer divided by 0 is, it 
 * is neither an algebraic number nor an algebraic integer. Also, throwing this 
 * exception implies the result of a division can be rounded to an algebraic 
 * integer nearby in the relevant ring.</p>
 * <p>At some point in the future I want to make this exception usable for any 
 * implementation of {@link AlgebraicInteger}, not just {@link
 * ImaginaryQuadraticInteger}.</p>
 * @author Alonso del Arte
 */
public class NotDivisibleException extends Exception {
    
    private static final long serialVersionUID = 1058274357;
    
    private final long resultingFractionRealPartNumerator;
    private final long resultingFractionImagPartNumerator;
    private final long resultingFractionDenominator;
    private final int resultingFractionNegRad;
    private final ImaginaryQuadraticRing workingRing;
    
    private final double numericRealPart;
    private final double numericImagPartMult;
    private final double numericImagPart;
    
    /**
     * Gives the numerator of the real part of the resulting fraction.
     * @return The integer for the real part supplied at the time the exception 
     * was constructed. For example, given 7/3 + 2 * sqrt(-5)/3, this would be 
     * 7.
     */
    public long getResReFractNumer() {
        return resultingFractionRealPartNumerator;
    }

    /**
     * Gives the numerator of the imaginary part of the resulting fraction.
     * @return The integer for the real part supplied at the time the exception 
     * was constructed. For example, given 7/3 + 2 * sqrt(-5)/3, this would be 
     * 3.
     */
    public long getResImFractNumer() {
        return resultingFractionImagPartNumerator;
    }
   
    /**
     * Gives the denominator of the resulting fraction.
     * @return The integer supplied at the time the exception was constructed. 
     * It may be almost any integer, but most certainly it should not be 0. 
     * Actually, it should not be -1 nor 1 either, and if the ring has 
     * "half-integers," it should not be -2 nor 2. For example, given 7/3 + 2 * 
     * sqrt(-5)/3, this would be 3.
     */
    public long getResFractDenom() {
        return resultingFractionDenominator;
    }
    
    /**
     * Gives the negative integer in the radical in the numerator of the 
     * resulting fraction.
     * @return The integer supplied at the time the exception was constructed. 
     * It ought to be a negative, squarefree integer.
     */
    public int getResFractNegRad() {
        return resultingFractionNegRad;
    }
    
    /**
     * Gives a numeric approximation of the real part of the resulting fraction.
     * @return A double with a numeric approximation of the real part of the 
     * resulting fraction. For example, for 3/4 + 7sqrt(-2)/4, this would be 
     * 0.75.
     */
    public double getNumericRealPart() {
        return numericRealPart;
    }
    
    /**
     * Gives a numeric approximation of the imaginary part of the resulting 
     * fraction divided by the square root of the parameter d of the relevant 
     * ring.
     * @return A double with a numeric approximation of the imaginary part of 
     * the resulting fraction divided by the square root of the parameter d of 
     * the relevant ring. For example, for 3/4 + 7sqrt(-2)/4, this would be 
     * 1.75.
     */
    public double getNumericImagPartMult() {
        return numericImagPartMult;
    }
    
    /**
     * Gives a numeric approximation of the imaginary part of the resulting 
     * fraction divided by the imaginary unit.
     * @return A double with a numeric approximation of the imaginary part of 
     * the resulting fraction divided by the imaginary unit. For example, for 
     * 3/4 + 7sqrt(-2)/4, this would be about 2.4748737341, which is 
     * approximately 7sqrt(2)/4.
     */
    public double getNumericImagPart() {
        return numericImagPart;
    }
    
    /**
     * Gets the algebraic integers which surround the algebraic number 
     * represented by ({@link #getResReFractNumer()} + 
     * {@link #getResImFractNumer()} * sqrt({@link #getResFractNegRad()})) /
     * {@link #getResFractDenom()}. WARNING: There is no overflow checking.
     * @return An array of ImaginaryQuadraticInteger. Do not expect the integers 
     * to be in any particular order: I or anyone else working on this project 
     * in the future is free to change the implementation in the interest of 
     * efficiency. If you need the bounding integers in a specific order, sort 
     * the array returned by this function prior to the point where you need the 
     * contents to be in a specific order. For example, for 1/2 + <i>i</i>/2, 
     * this function should return {0, <i>i</i>, 1 + <i>i</i>, 1}, not 
     * necessarily in that order.
     */
    public ImaginaryQuadraticInteger[] getBoundingIntegers() {
        ImaginaryQuadraticInteger zeroIQI = new ImaginaryQuadraticInteger(0, 0, workingRing);
        ImaginaryQuadraticInteger[] algIntArray = {zeroIQI, zeroIQI, zeroIQI, zeroIQI};
        if (workingRing.hasHalfIntegers()) {
            int topPointA, topPointB;
            topPointA = (int) Math.ceil(numericRealPart * 2);
            topPointB = (int) Math.ceil(numericImagPartMult * 2);
            if ((topPointA % 2 == 0 && topPointB % 2 != 0) || (topPointA % 2 != 0 && topPointB % 2 == 0)) {
                topPointA--;
            }
            algIntArray[0] = new ImaginaryQuadraticInteger(topPointA, topPointB, workingRing, 2);
            algIntArray[1] = new ImaginaryQuadraticInteger(topPointA - 1, topPointB - 1, workingRing, 2);
            algIntArray[2] = new ImaginaryQuadraticInteger(topPointA + 1, topPointB - 1, workingRing, 2);
            algIntArray[3] = new ImaginaryQuadraticInteger(topPointA, topPointB - 2, workingRing, 2);
        } else {
            int floorA, floorB, ceilA, ceilB;
            floorA = (int) Math.floor(numericRealPart);
            floorB = (int) Math.floor(numericImagPartMult);
            ceilA = (int) Math.ceil(numericRealPart);
            ceilB = (int) Math.ceil(numericImagPartMult);
            algIntArray[0] = new ImaginaryQuadraticInteger(floorA, floorB, workingRing);
            algIntArray[1] = new ImaginaryQuadraticInteger(ceilA, floorB, workingRing);
            algIntArray[2] = new ImaginaryQuadraticInteger(floorA, ceilB, workingRing);
            algIntArray[3] = new ImaginaryQuadraticInteger(ceilA, ceilB, workingRing);
        }
        return algIntArray;
    }
    
    /**
     * Rounds the imaginary quadratic number to the imaginary quadratic integer 
     * in the relevant ring that is closest to 0. However, no guarantee is made 
     * as to which result will be returned if two potential results are the same 
     * distance from 0.
     * @return An ImaginaryQuadraticInteger object representing the imaginary 
     * quadratic integer that is as close if not closer to 0 than the other 
     * integers bounding the imaginary quadratic number. For example, for 7/3 + 
     * 4 * sqrt(-5)/3 this would be 2 + sqrt(-5). Now, given, for example, 7/8 + 
     * 7i/8, no guarantee is made as to whether this function would return i or 
     * 1.
     */
    public ImaginaryQuadraticInteger roundTowardsZero() {
        if (workingRing.hasHalfIntegers()) {
            ImaginaryQuadraticInteger[] bounds = this.getBoundingIntegers();
            double currAbs = bounds[0].abs();
            double closestSoFar = currAbs;
            int currIndex = 1;
            int bestIndex = 0;
            while (currIndex < bounds.length) {
                currAbs = bounds[currIndex].abs();
                if (currAbs < closestSoFar) {
                    closestSoFar = currAbs;
                    bestIndex = currIndex;
                }
                currIndex++;
            }
            return bounds[bestIndex];
        } else {
            double intermediateRealPart = (double) resultingFractionRealPartNumerator / (double) resultingFractionDenominator;
            double intermediateImagPart = (double) resultingFractionImagPartNumerator / (double) resultingFractionDenominator;
            if (intermediateRealPart < 0) {
                intermediateRealPart = Math.ceil(intermediateRealPart);
            } else {
                intermediateRealPart = Math.floor(intermediateRealPart);
            }
            if (intermediateImagPart < 0) {
                intermediateImagPart = Math.ceil(intermediateImagPart);
            } else {
                intermediateImagPart = Math.floor(intermediateImagPart);
            }
            boolean overflowFlag = (intermediateRealPart < Integer.MIN_VALUE) || (intermediateRealPart > Integer.MAX_VALUE);
            overflowFlag = overflowFlag || ((intermediateImagPart < Integer.MIN_VALUE) || (intermediateImagPart > Integer.MAX_VALUE));
            if (overflowFlag) {
                throw new ArithmeticException("Real part " + intermediateRealPart + ", imaginary part " + intermediateImagPart + " times sqrt" + resultingFractionNegRad + " is outside the range of this implmentation of ImaginaryQuadraticInteger, which uses 32-bit signed integers.");
            }
            return new ImaginaryQuadraticInteger((int) intermediateRealPart, (int) intermediateImagPart, workingRing);
        }
    }
    
    /**
     * Rounds the imaginary quadratic number to the imaginary quadratic integer 
     * in the relevant ring that is farthest from 0. However, no guarantee is 
     * made as to which result will be returned if two potential results are the 
     * same distance from 0.
     * @return An ImaginaryQuadraticInteger object representing the imaginary 
     * quadratic integer that is as far if not farther from 0 than the other 
     * integers bounding the imaginary quadratic number. For example, for 7/3 + 
     * 4 * sqrt(-5)/3 this would be 3 + 2 * sqrt(-5).
     */
    public ImaginaryQuadraticInteger roundAwayFromZero() {
        if (workingRing.hasHalfIntegers()) {
            ImaginaryQuadraticInteger[] bounds = this.getBoundingIntegers();
            double currAbs = bounds[0].abs();
            double farthestSoFar = currAbs;
            int currIndex = 1;
            int bestIndex = 0;
            while (currIndex < bounds.length) {
                currAbs = bounds[currIndex].abs();
                if (currAbs > farthestSoFar) {
                    farthestSoFar = currAbs;
                    bestIndex = currIndex;
                }
                currIndex++;
            }
            return bounds[bestIndex];
        } else {
            double intermediateRealPart = (double) resultingFractionRealPartNumerator / (double) resultingFractionDenominator;
            double intermediateImagPart = (double) resultingFractionImagPartNumerator / (double) resultingFractionDenominator;
            if (intermediateRealPart < 0) {
                intermediateRealPart = Math.floor(intermediateRealPart);
            } else {
                intermediateRealPart = Math.ceil(intermediateRealPart);
            }
            if (intermediateImagPart < 0) {
                intermediateImagPart = Math.floor(intermediateImagPart);
            } else {
                intermediateImagPart = Math.ceil(intermediateImagPart);
            }
            boolean overflowFlag = (intermediateRealPart < Integer.MIN_VALUE) || (intermediateRealPart > Integer.MAX_VALUE);
            overflowFlag = overflowFlag || ((intermediateImagPart < Integer.MIN_VALUE) || (intermediateImagPart > Integer.MAX_VALUE));
            if (overflowFlag) {
                throw new ArithmeticException("Real part " + intermediateRealPart + ", imaginary part " + intermediateImagPart + " times sqrt" + resultingFractionNegRad + " is outside the range of this implmentation of ImaginaryQuadraticInteger, which uses 32-bit signed integers.");
            }
            return new ImaginaryQuadraticInteger((int) intermediateRealPart, (int) intermediateImagPart, workingRing);
        }
    }
    
    /**
     * This exception should be thrown when a division operation takes the 
     * resulting number out of the ring, to the larger field. If the result is 
     * an algebraic number of degree 4, perhaps AlgebraicDegreeOverflowException 
     * should be thrown instead. And if there is an attempt to divide by 0, the 
     * appropriate exception to throw would perhaps be IllegalArgumentException.
     * @param message A message to pass on to the Exception constructor.
     * @param resFractReNumer The numerator of the real part of the resulting 
     * fraction. For example, given 7/3 + 2 * sqrt(-5)/3, this parameter would 
     * be 7.
     * @param resFractImNumer The numerator of the imaginary part of the 
     * resulting fraction. For example, given 7/3 + 2 * sqrt(-5)/3, this 
     * parameter would be 2.
     * @param resFractDenom The denominator of the resulting fraction. For 
     * example, given 7/3 + 2 * sqrt(-5)/3, this parameter would be 3.
     * @param resFractNegRad The negative integer in the radical in the 
     * numerator of the resulting fraction. For example, given 7/3 + 2 * 
     * sqrt(-5)/3, this parameter would be -5.
     */
    public NotDivisibleException(String message, long resFractReNumer, long resFractImNumer, long resFractDenom, int resFractNegRad) {
        super(message);
        resultingFractionRealPartNumerator = resFractReNumer;
        resultingFractionImagPartNumerator = resFractImNumer;
        resultingFractionDenominator = resFractDenom;
        resultingFractionNegRad = resFractNegRad;
        workingRing = new ImaginaryQuadraticRing(resFractNegRad);
        numericRealPart = (double) resultingFractionRealPartNumerator / (double) resultingFractionDenominator;
        numericImagPartMult = (double) resultingFractionImagPartNumerator / (double) resultingFractionDenominator;
        numericImagPart = numericImagPartMult * workingRing.getAbsNegRadSqrt();
    }
}
