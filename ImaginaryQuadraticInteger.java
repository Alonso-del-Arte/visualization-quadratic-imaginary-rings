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

public class ImaginaryQuadraticInteger implements AlgebraicInteger {
    
    /**
     * The real part of the imaginary quadratic integer. If the denominator is 2, the real part should be odd.
     */
    protected int realPartMult;
    
    /**
     * The imaginary part of the imaginary quadratic integer. If the denominator is 2, the real part should be odd.
     */
    protected int imagPartMult;
    
    /**
     * Really this is an object that stores information about the ring that we're working in, such as whether the denominator may be 2.
     */
    protected ImaginaryQuadraticRing imagQuadRing;
    
    /**
     * If imagQuadRing.d1mod4 is true, then denominator may be 1 or 2, otherwise denominator should be 1.
     */
    protected int denominator;

    /**
     * Gives the algebraic degree of the algebraic integer. Should not be higher than 2.
     * @return 0 if the algebraic integer is 0, 1 if it's a purely real integer, 2 otherwise.
     */
    @Override
    public int algebraicDegree() {
        if (imagPartMult == 0) {
            if (realPartMult == 0) {
                return 0;
            } else {
                return 1;
            }
        } else {
            return 2;
        }
    }
    
    /**
     * Calculates the trace of the imaginary quadratic integer (real part plus real integer times square root of a negative integer)
     * @return Twice the real part
     */
    @Override
    public int trace() {
        if (imagQuadRing.d1mod4 && denominator == 2) {
            return realPartMult;
        } else {
            return 2 * realPartMult;
        }
    }
    
    /**
     * Calculates the norm of the imaginary quadratic integer (real part plus real integer times square root of a negative integer)
     * @return Square of the real part minus square of the imaginary part. May be 0 but never negative.
     */
    @Override
    public int norm() {
        int N;
        if (imagQuadRing.d1mod4 && denominator == 2) {
            N = (realPartMult * realPartMult + imagQuadRing.absNegRad * imagPartMult * imagPartMult)/4;
        } else {
            N = realPartMult * realPartMult + imagQuadRing.absNegRad * imagPartMult * imagPartMult;
        }
        return N;
    }
    
    /**
     * Gives the coefficients for the minimal polynomial of the algebraic integer
     * @return An array of three integers. If the algebraic integer is of degree 2, the array will be {norm, negative trace, 1}; if of degree 1, then {number, 1, 0}, and for 0, {0, 0, 0}.
     */
    @Override
    public int[] minPolynomial() {
        int[] coeffs = {0, 0, 0};
        switch (algebraicDegree()) {
            case 0:
                coeffs[1] = 1;
                break;
            case 1: 
                coeffs[0] = -1 * realPartMult;
                coeffs[1] = 1;
                break;
            case 2: 
                coeffs[0] = norm();
                coeffs[1] = -trace();
                coeffs[2] = 1;
                break;
        }
        return coeffs;
    }
    
    /**
     * Gives the polynomial in a format suitable for plain text or TeX.
     * @return A String. If the algebraic degree is 2, the String should start off with "x^2".
     */
    @Override
    public String minPolynomialString() {
        String polString = "";
        int[] polCoeffs = minPolynomial();
        switch (algebraicDegree()) {
            case 0:
                polString = "x";
                break;
            case 1:
                if (polCoeffs[0] < 0) {
                    polString = "x - " + ((-1) * polCoeffs[0]);
                } else {
                    polString = "x + " + polCoeffs[0];
                }
                break;
            case 2:
                polString = "x^2 ";
                if (polCoeffs[1] < -1) {
                    polString += ("- " + ((-1) * polCoeffs[1]) + "x ");
                }
                if (polCoeffs[1] == -1) {
                    polString += "- x ";
                }
                if (polCoeffs[1] == 1) {
                    polString += "+ x ";
                }
                if (polCoeffs[1] > 1) {
                    polString += ("+ " + polCoeffs[1] + "x ");
                }
                if (polCoeffs[0] < 0) {
                    polString += ("- " + ((-1) * polCoeffs[0]));
                } else {
                    polString += ("+ " + polCoeffs[0]);
                }
                break;
        }
        return polString;
    }
    
    /**
     * Gets the real part of the imaginary quadratic integer. May be half an integer.
     * @return The real part of the imaginary quadratic integer. For example, for -1/2 + sqrt(-7)/2, the result should be -0.5.
     */
    public double getRealPartMult() {
        double realPart = this.realPartMult;
        if (this.denominator == 2) {
            realPart /= 2;
        }
        return realPart;
    }
    
    /**
     * Gets the imaginary part of the imaginary quadratic integer multiplied by -i. May be the rational approximation of an irrational real number.
     * @return The imaginary part of the imaginary quadratic integer multiplied by -i. For example, for -1/2 + sqrt(-7)/2, the result should be something like 1.32287565553229529525.
     */
    public double getImagPartwRadMult() {
        double imagPartwRad = this.imagPartMult * this.imagQuadRing.absNegRadSqrt;
        if (this.denominator == 2) {
            imagPartwRad /= 2;
        }
        return imagPartwRad;
    }
    
    /**
     * Gets the real part of the imaginary quadratic integer multiplied by 2.
     * @return The real part of the imaginary quadratic integer multiplied by 2. For example, for -1/2 + sqrt(-7)/2, the result should be -1; and for -1 + sqrt(-7), the result should be -2.
     */
    public long getTwiceRealPartMult() {
        long twiceRealPartMult = this.realPartMult;
        if (this.denominator == 1) {
            twiceRealPartMult *= 2;
        }
        return twiceRealPartMult;
    }
    
    /**
     * Gets the imaginary part of the imaginary quadratic integer multiplied by -2i.
     * @return The real part of the imaginary quadratic integer multiplied by -2i. For example, for -1/2 + sqrt(-7)/2, the result should be 1; and for -1 + sqrt(-7), the result should be 2.
     */
    public long getTwiceImagPartMult() {
        long twiceImagPartMult = this.imagPartMult;
        if (this.denominator == 1) {
            twiceImagPartMult *= 2;
        }
        return twiceImagPartMult;
    }
    
    /**
     * A text representation of the imaginary quadratic integer, with the real part first and the imaginary part second.
     * @return A String representing the imaginary quadratic integer which can be output to the console.
     */
    @Override
    public String toString() {
        String IQIString = "";
        if (this.denominator == 2) {
            IQIString = this.realPartMult + "/2 ";
            if (this.imagPartMult < -1) {
                IQIString += (("- " + ((-1) * this.imagPartMult)) + "\u221A(" + this.imagQuadRing.negRad + ")/2");
            }
            if (this.imagPartMult == -1) {
                IQIString += ("- \u221A(" + this.imagQuadRing.negRad + ")/2");
            }
            if (this.imagPartMult == 1) {
                IQIString += ("+ \u221A(" + this.imagQuadRing.negRad + ")/2");
            }
            if (this.imagPartMult > 1) {
                IQIString += ("+ " + this.imagPartMult + "\u221A(" + this.imagQuadRing.negRad + ")/2");
            } 
        } else {
            if (this.realPartMult == 0) {
                if (this.imagPartMult == 0) {
                    IQIString = "0";
                } else {
                    if (this.imagPartMult < -1 || this.imagPartMult > 1) {
                        IQIString = this.imagPartMult + "\u221A(" + this.imagQuadRing.negRad + ")";
                    }
                    if (this.imagPartMult == -1) {
                        IQIString = "-\u221A(" + this.imagQuadRing.negRad + ")";
                    }
                    if (this.imagPartMult == 1) {
                        IQIString = "\u221A(" + this.imagQuadRing.negRad + ")";
                    }
                }
            } else {
                IQIString = Integer.toString(this.realPartMult);
                if (this.imagPartMult < -1) {
                    IQIString += ((" - " + ((-1) * this.imagPartMult)) + "\u221A(" + this.imagQuadRing.negRad + ")");
                }
                if (this.imagPartMult == -1) {
                    IQIString += (" - \u221A(" + this.imagQuadRing.negRad + ")");
                }
                if (this.imagPartMult == 1) {
                    IQIString += (" + \u221A(" + this.imagQuadRing.negRad + ")");
                }
                if (this.imagPartMult > 1) {
                    IQIString += (" + " + this.imagPartMult + "\u221A(" + this.imagQuadRing.negRad + ")");
                }
            }
        }
        if (this.imagQuadRing.negRad == -1) {
            IQIString = IQIString.replace("\u221A(-1)", "i");
        }
        return IQIString;
    }
    
    /**
     * A text representation of the imaginary quadratic integer, using theta notation when imagQuadRing.d1mod4 is true.
     * @return A String representing the imaginary quadratic integer which can be output to the console. If imagQuadRing.d1mod4 is false, this just returns the same String as toString().
     */
    public String toStringAlt() {

        String altIQIString;
        if (this.imagQuadRing.d1mod4) {
            int nonThetaPart = this.realPartMult;
            int thetaPart = this.imagPartMult;
            char thetaLetter = '\u03B8';
            if (this.denominator == 1) {
                nonThetaPart *= 2;
                thetaPart *= 2;
            }
            if (this.imagQuadRing.negRad == -3) {
                nonThetaPart = (nonThetaPart + thetaPart)/2;
                thetaLetter = '\u03C9'; // Now this holds omega instead of theta
            } else {
                nonThetaPart = (nonThetaPart - thetaPart)/2;
            }
            altIQIString = Integer.toString(nonThetaPart);
            if (nonThetaPart == 0 && thetaPart != 0) {
                if (thetaPart < -1 || thetaPart > 1) {
                    altIQIString = Integer.toString(thetaPart) + thetaLetter;
                }
                if (thetaPart == -1) {
                    altIQIString = "-" + thetaLetter;
                }
                if (thetaPart == 1) {
                    altIQIString = Character.toString(thetaLetter);
                }
            } else {
                if (thetaPart < -1) {
                    altIQIString += (" - " + ((-1) * thetaPart) + thetaLetter);
                }
                if (thetaPart == -1) {
                    altIQIString += (" - " + thetaLetter);
                }
                if (thetaPart == 1) {
                    altIQIString += (" + " + thetaLetter);
                }
                if (thetaPart > 1) {
                    altIQIString += (" + " + thetaPart + thetaLetter);
                }
            }
        } else {
            altIQIString = this.toString();
        }
        return altIQIString;
 
    }
    
    /**
     * Addition operation, since operator+ (plus) can't be overloaded. No overflow checking as of yet.
     * @param summand The imaginary quadratic integer to be added to this quadratic integer.
     * @return A new ImaginaryQuadraticInteger object with the result of the operation.
     * @throws AlgebraicDegreeOverflowException If the algebraic integers come from different quadratic rings, the result of the sum will be an algebraic integer of degree 4 and this exception will be thrown.
     */
    public ImaginaryQuadraticInteger plus(ImaginaryQuadraticInteger summand) throws AlgebraicDegreeOverflowException {
        if (((this.imagPartMult != 0) && (summand.imagPartMult != 0)) && (this.imagQuadRing.negRad != summand.imagQuadRing.negRad)) {
            throw new AlgebraicDegreeOverflowException("This operation would result in an algebraic integer of degree 4.", 2, 4);
        }
        int sumRealPart = 0;
        int sumImagPart = 0;
        int sumDenom = 1;
        if (this.imagQuadRing.d1mod4) {
            if (this.denominator == 1 && summand.denominator == 2) {
                sumRealPart = 2 * this.realPartMult + summand.realPartMult;
                sumImagPart = 2 * this.imagPartMult + summand.imagPartMult;
                sumDenom = 2;
            }
            if (this.denominator == 2 && summand.denominator == 1) {
                sumRealPart = this.realPartMult + 2 * summand.realPartMult;
                sumImagPart = this.imagPartMult + 2 * summand.imagPartMult;
                sumDenom = 2;
            }
            if (this.denominator == summand.denominator) {
                sumRealPart = this.realPartMult + summand.realPartMult;
                sumImagPart = this.imagPartMult + summand.imagPartMult;
                sumDenom = this.denominator;
            }
        } else {
            sumRealPart = this.realPartMult + summand.realPartMult;
            sumImagPart = this.imagPartMult + summand.imagPartMult;
            sumDenom = 1;
        }
        return new ImaginaryQuadraticInteger(sumRealPart, sumImagPart, this.imagQuadRing, sumDenom);
    }
    
    /**
     * Addition operation, since operator+ (plus) can't be overloaded. No overflow checking as of yet.
     * Although the previous plus function can be passed an ImaginaryQuadraticInteger with imagPartMult equal to 0, this function is to be preferred if you know for sure the summand is purely real.
     * With this plus, there is no need to catch an AlgebraicDegreeOverflowException.
     * @param summand The purely real integer to be added to the real part of the ImaginaryQuadraticInteger.
     * @return A new ImaginaryQuadraticInteger object with the result of the operation.
     */
    public ImaginaryQuadraticInteger plus(int summand) {
        int sumRealPart = this.realPartMult;
        if (this.denominator == 2) {
            sumRealPart += (2 * summand);
        } else {
            sumRealPart += summand;
        }
        return new ImaginaryQuadraticInteger(sumRealPart, this.imagPartMult, this.imagQuadRing, this.denominator);
    }

    /**
     * Subtraction operation, since operator- can't be overloaded.  No overflow checking as of yet.
     * @param subtrahend The imaginary quadratic integer to be subtracted from this quadratic integer.
     * @return A new ImaginaryQuadraticInteger object with the result of the operation.
     * @throws AlgebraicDegreeOverflowException If the algebraic integers come from different quadratic rings, the result of the sum will be an algebraic integer of degree 4 and this exception will be thrown.
     */
    public ImaginaryQuadraticInteger minus(ImaginaryQuadraticInteger subtrahend) throws AlgebraicDegreeOverflowException {
        if (((this.imagPartMult != 0) && (subtrahend.imagPartMult != 0)) && (this.imagQuadRing.negRad != subtrahend.imagQuadRing.negRad)) {
            throw new AlgebraicDegreeOverflowException("This operation would result in an algebraic integer of degree 4.", 2, 4);
        }
        int subtractionRealPart = 0;
        int subtractionImagPart = 0;
        int subtractionDenom = 1;
        if (this.imagQuadRing.d1mod4) {
            if (this.denominator == 1 && subtrahend.denominator == 2) {
                subtractionRealPart = 2 * this.realPartMult - subtrahend.realPartMult;
                subtractionImagPart = 2 * this.imagPartMult - subtrahend.imagPartMult;
                subtractionDenom = 2;
            }
            if (this.denominator == 2 && subtrahend.denominator == 1) {
                subtractionRealPart = this.realPartMult - 2 * subtrahend.realPartMult;
                subtractionImagPart = this.imagPartMult - 2 * subtrahend.imagPartMult;
                subtractionDenom = 2;
            }
            if (this.denominator == subtrahend.denominator) {
                subtractionRealPart = this.realPartMult - subtrahend.realPartMult;
                subtractionImagPart = this.imagPartMult - subtrahend.imagPartMult;
                subtractionDenom = this.denominator;
            }
        } else {
            subtractionRealPart = this.realPartMult - subtrahend.realPartMult;
            subtractionImagPart = this.imagPartMult - subtrahend.imagPartMult;
            subtractionDenom = 1;
        }
        return new ImaginaryQuadraticInteger(subtractionRealPart, subtractionImagPart, this.imagQuadRing, subtractionDenom);
    }
    
    /**
     * Subtraction operation, since operator- can't be overloaded.  No overflow checking as of yet.
     * Although the previous minus function can be passed an ImaginaryQuadraticInteger with imagPartMult equal to 0, this function is to be preferred if you know for sure the subtrahend is purely real.
     * With this minus, there is no need to catch an AlgebraicDegreeOverflowException.
     * @param subtrahend The purely real integer to be subtracted from this quadratic integer.
     * @return A new ImaginaryQuadraticInteger object with the result of the operation.
     */
    public ImaginaryQuadraticInteger minus(int subtrahend) {
        int subtractionRealPart = this.realPartMult;
        if (this.denominator == 2) {
            subtractionRealPart -= (2 * subtrahend);
        } else {
            subtractionRealPart -= subtrahend;
        }
        return new ImaginaryQuadraticInteger(subtractionRealPart, this.imagPartMult, this.imagQuadRing, this.denominator);

    }
    
    /**
     * Multiplication operation, since operator* can't be overloaded. No overflow checking as of yet.
     * @param multiplicand The imaginary quadratic integer to be multiplied by this quadratic integer.
     * @return A new ImaginaryQuadraticInteger object with the result of the operation.
     * @throws AlgebraicDegreeOverflowException If the algebraic integers come from different quadratic rings, the result of the sum will be an algebraic integer of degree 4 and this exception will be thrown.
     */
    public ImaginaryQuadraticInteger times(ImaginaryQuadraticInteger multiplicand) throws AlgebraicDegreeOverflowException {
        if (((this.imagPartMult != 0) && (multiplicand.imagPartMult != 0)) && (this.imagQuadRing.negRad != multiplicand.imagQuadRing.negRad)) {
            throw new AlgebraicDegreeOverflowException("This operation would result in an algebraic integer of degree 4.", 2, 4);
        }
        int intermediateRealPart = this.realPartMult * multiplicand.realPartMult - this.imagPartMult * multiplicand.realPartMult * this.imagQuadRing.absNegRad;
        int intermediateImagPart = this.realPartMult * multiplicand.imagPartMult + this.imagPartMult * multiplicand.realPartMult;
        int intermediateDenom = this.denominator * multiplicand.denominator;
        if (intermediateDenom == 4) {
            intermediateRealPart /= 2;
            intermediateImagPart /= 2;
            intermediateDenom = 2;
        }
        // There is no need to check if intermediateDenom is equal to 2 and both intermediateRealPart and intermediateImagPart are even because the ImaginaryQuadraticInteger constructor will take care of halving the parts and changing the denominator to 1.
        ImaginaryQuadraticInteger result = new ImaginaryQuadraticInteger(intermediateRealPart, intermediateImagPart, this.imagQuadRing, intermediateDenom);
        return result;
    }
   
    // PLACEHOLDER FOR divides(IQI)
    
    // PLACEHOLDER FOR divides(int)
    
    /**
     * Class constructor
     * @param a The real part of the imaginary quadratic integer, multiplied by 2 when applicable
     * @param b The part to be multiplied by sqrt(d), multiplied by 2 when applicable
     * @param R The ring to which this algebraic integer belongs to
     * @param denom In most cases 1, but may be 2 if a and b have the same parity and d = 1 mod 4
     * @throws IllegalArgumentException If denom is anything other than 1 or 2, or if denom is 2 but a and b don't match parity.
     */
    public ImaginaryQuadraticInteger(int a, int b, ImaginaryQuadraticRing R, int denom) {
        
        boolean abParityMatch;
        
        if (denom < 1 || denom > 2) {
            throw new IllegalArgumentException("Parameter denom must be 1 or 2.");
        }
        if (denom == 2) {
            abParityMatch = Math.abs(a % 2) == Math.abs(b % 2);
            if (!abParityMatch) {
                throw new IllegalArgumentException("Parity of parameter a must match parity of parameter b.");
            }
            if (a % 2 == 0) {
                this.realPartMult = a/2;
                this.imagPartMult = b/2;
                this.denominator = 1;
            } else {
                if (R.d1mod4) {
                    this.realPartMult = a;
                    this.imagPartMult = b;
                    this.denominator = 2;
                } else {
                    throw new IllegalArgumentException("Either parameter a and parameter b need to both be even, or parameter denom needs to be 1.");
                }
            }
        } else {
            this.realPartMult = a;
            this.imagPartMult = b;
            this.denominator = 1;
        }
        this.imagQuadRing = R;
        
    }
    
    private static int getIntFromConsole(Scanner input) {
        int enteredInteger = 0;
        boolean invalidInput = true;
        
        while (invalidInput) {
            try {
                enteredInteger = input.nextInt();
                invalidInput = false;
            } catch (InputMismatchException inputMismatch) {
                System.out.println("Please enter an integer.");
                input.nextLine();
            }
        }
        return enteredInteger;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        Scanner inputScanner = new Scanner(System.in);
        int chosenRingD = RingWindowDisplay.DEFAULT_RING_D;
        int chosenRealPartMult;
        int chosenImagPartMult;
        int chosenDenom;
        
        ImaginaryQuadraticRing imR;
        ImaginaryQuadraticInteger currIQI;
        int[] currPolCoeffs;
        
        /*
        The idea here is that the user will be able to choose between a graphical interface and a text interface.
        
        if (args.length > 0) {
            if (args[0] == "-gui" || args[0] == "-GUI") {
                RingWindowDisplay(chosenRingD)
            }
            if (args[0] == "-text" || args[0] == "-TEXT") {
                text stuff
            }
        }
        */    
       
        RingWindowDisplay.startRingWindowDisplay(-1);
        
/*
        while (chosenRingD != 0) {
            System.out.print("Please enter a negative squarefree integer d for the ring discriminant (or 0 to quit): ");
            chosenRingD = getIntFromConsole(inputScanner);
            if (chosenRingD > 0) {
                System.out.print("Taking " + chosenRingD + " to be ");
                chosenRingD *= -1;
                System.out.println(chosenRingD);
            }
            if (NumberTheoreticFunctionsCalculator.isSquareFree(chosenRingD)) {
                imR = new ImaginaryQuadraticRing(chosenRingD);
                if (imR.d1mod4) {
                    System.out.println("Given that " + chosenRingD + " is congruent to 1 mod 4, please enter real and imaginary parts multiplied by 2.");
                    chosenDenom = 2;
                } else {
                    chosenDenom = 1;
                }
                chosenImagPartMult = 1;
                while (chosenImagPartMult != 0) {
                    System.out.print("Please enter real part of quadratic integer: ");
                    chosenRealPartMult = getIntFromConsole(inputScanner);
                    System.out.print("Please enter imaginary part of quadratic integer (or 0 to change ring): ");
                    chosenImagPartMult = getIntFromConsole(inputScanner);
                    if (imR.d1mod4 && (Math.abs(chosenRealPartMult % 2) != Math.abs(chosenImagPartMult % 2))) {
                        chosenImagPartMult++;
                    }
                    currIQI = new ImaginaryQuadraticInteger(chosenRealPartMult, chosenImagPartMult, imR, chosenDenom);
                    System.out.println(currIQI.toString());
                    System.out.println("Algebraic degree is " + currIQI.algebraicDegree());
                    System.out.println("Trace is " + currIQI.trace());
                    System.out.println("Norm is " + currIQI.norm());
                    System.out.println("Minimal polynomial is " + currIQI.minPolynomialString());
                    System.out.println(" ");
                }
            } 
        } */
    } 
    
}