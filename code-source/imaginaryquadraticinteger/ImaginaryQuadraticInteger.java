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

import java.util.Objects;

/**
 * The main class, defines an imaginary quadratic integer. The real part, and 
 * the real number to be multiplied by an imaginary number, are held in 32-bit 
 * fields. However, some of the computations are done with 64-bit variables. 
 * There is some overflow checking; the documentation for some of the methods 
 * gives more details.
 * @author Alonso del Arte
 */
public class ImaginaryQuadraticInteger implements AlgebraicInteger {
    
    /**
     * The real part of the imaginary quadratic integer. If the denominator is 
     * 2, the real part should be odd.
     */
    protected int realPartMult;
    
    /**
     * The imaginary part of the imaginary quadratic integer. If the denominator 
     * is 2, the real part should be odd.
     */
    protected int imagPartMult;
    
    /**
     * Really this is an object that stores information about the ring that 
     * we're working in, such as whether the denominator may be 2.
     */
    protected ImaginaryQuadraticRing imagQuadRing;
    
    /**
     * If imagQuadRing.d1mod4 is true, then denominator may be 1 or 2, otherwise 
     * denominator should be 1.
     */
    protected int denominator;

    /**
     * Gives the algebraic degree of the algebraic integer. Should not be higher 
     * than 2.
     * @return 0 if the algebraic integer is 0, 1 if it's a purely real integer, 
     * 2 otherwise.
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
     * Calculates the trace of the imaginary quadratic integer (real part plus 
     * real integer times square root of a negative integer). There is no 
     * overflow checking, but this should not be a problem as long as the real 
     * part a is within the range -(2^30) < a < 2^30.
     * @return Twice the real part. For example, given 5/2 + sqrt(-7)/2, the 
     * trace would be 5.
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
     * Calculates the norm of the imaginary quadratic integer (real part plus 
     * real integer times square root of a negative integer).
     * WARNING: There is no overflow checking. That might slow things down 
     * unacceptably in 
     * {@link RingWindowDisplay#paintComponent(java.awt.Graphics)}.
     * @return Square of the real part minus square of the imaginary part. For 
     * example, given 5/2 + sqrt(-7)/2, the norm would be 8.
     * May be 0 but should never be negative. If it is negative, most likely an 
     * overflow has occurred.
     * In general, the farther away negRad is from 0, the closer the real and 
     * imaginary parts have to be to 0 to avoid overflows.
     */
    @Override
    public int norm() {
        long N;
        if (imagQuadRing.d1mod4 && denominator == 2) {
            N = (realPartMult * realPartMult + imagQuadRing.absNegRad * imagPartMult * imagPartMult)/4;
        } else {
            N = realPartMult * realPartMult + imagQuadRing.absNegRad * imagPartMult * imagPartMult;
        }
        if (N < Integer.MIN_VALUE || N > Integer.MAX_VALUE) {
            throw new ArithmeticException("Norm exceeds range of int data type. You might need long or even BigInteger.");
        }
        return (int) N;
    }
    
    /**
     * Gives the coefficients for the minimal polynomial of the algebraic 
     * integer.
     * @return An array of three integers. If the algebraic integer is of degree 
     * 2, the array will be {norm, negative trace, 1}; if of degree 1, then 
     * {number, 1, 0}, and for 0, {0, 1, 0}. For example, for 5/2 + sqrt(-7)/2, 
     * the result would be {8, -5, 1}.
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
     * @return A String. If the algebraic degree is 2, the String should start 
     * off with "x^2". For example, for 5/2 + sqrt(-7)/2, the result would be 
     * "x^2 - 5x + 8".
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
     * Computes the conjugate of the given algebraic integer.
     * Remember: an imaginary quadratic integer times its conjugate is its norm.
     * @return The conjugate. For example, given 5/2 + sqrt(-7)/2, the conjugate 
     * would be 5/2 - sqrt(-7)/2.
     */
    public ImaginaryQuadraticInteger conjugate() {
        if (this.imagPartMult == 0) {
            return this;
        } else {
            return new ImaginaryQuadraticInteger(this.realPartMult, -this.imagPartMult, this.imagQuadRing, this.denominator);
        }
    }
    
    /**
     * Gets the real part of the imaginary quadratic integer. May be half an 
     * integer.
     * @return The real part of the imaginary quadratic integer. For example, 
     * for -1/2 + sqrt(-7)/2, the result should be -0.5.
     */
    public double getRealPartMultNumeric() {
        double realPart = this.realPartMult;
        if (this.denominator == 2) {
            realPart /= 2;
        }
        return realPart;
    }
    
    /**
     * Gets the imaginary part of the imaginary quadratic integer multiplied by 
     * -i. It will most likely be the rational approximation of an irrational 
     * real number.
     * @return The imaginary part of the imaginary quadratic integer multiplied 
     * by -i. For example, for -1/2 + sqrt(-7)/2, the result should be something 
     * like 1.32287565553229529525.
     */
    public double getImagPartwRadMultNumeric() {
        double imagPartwRad = this.imagPartMult * this.imagQuadRing.absNegRadSqrt;
        if (this.denominator == 2) {
            imagPartwRad /= 2;
        }
        return imagPartwRad;
    }
    
    /**
     * Gets the real part of the imaginary quadratic integer multiplied by 2.
     * @return The real part of the imaginary quadratic integer multiplied by 2. 
     * For example, for -1/2 + sqrt(-7)/2, the result should be -1; and for -1 + 
     * sqrt(-7), the result should be -2.
     */
    public long getTwiceRealPartMult() {
        long twiceRealPartMult = this.realPartMult;
        if (this.denominator == 1) {
            twiceRealPartMult *= 2;
        }
        return twiceRealPartMult;
    }
    
    /**
     * Gets the imaginary part of the imaginary quadratic integer multiplied by 
     * -2i.
     * @return The real part of the imaginary quadratic integer multiplied by 
     * -2i. For example, for -1/2 + sqrt(-7)/2, the result should be 1; and for 
     * -1 + sqrt(-7), the result should be 2.
     */
    public long getTwiceImagPartMult() {
        long twiceImagPartMult = this.imagPartMult;
        if (this.denominator == 1) {
            twiceImagPartMult *= 2;
        }
        return twiceImagPartMult;
    }
    
    public int getRealPartMult() {
        return this.realPartMult;
    }
    
    public int getImagPartMult() {
        return this.imagPartMult;
    }
    
    /**
     * Gets the imaginary quadratic ring which this imaginary quadratic integer 
     * belongs to.
     * @return An ImaginaryQuadraticRing object, which can then be queried for 
     * its negRad, absNegRad and absNegRadSqrt values, and a couple other 
     * properties.
     */
    public ImaginaryQuadraticRing getRing() {
        return this.imagQuadRing;
    }
    
    public int getDenominator() {
        return this.denominator;
    }
    
    /**
     * A text representation of the imaginary quadratic integer, with the real 
     * part first and the imaginary part second.
     * @return A String representing the imaginary quadratic integer which can 
     * be used in a JTextField.
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
     * A text representation of the imaginary quadratic integer, using theta 
     * notation when {@link #getRing()}{@link 
     * ImaginaryQuadraticRing#hasHalfIntegers() .hasHalfIntegers()} is true.
     * @return A String representing the imaginary quadratic integer which can 
     * be used in a JTextField. If {@link #getRing()}{@link 
     * ImaginaryQuadraticRing#hasHalfIntegers() .hasHalfIntegers()} is false, 
     * this just returns the same String as {@link #toString()}.
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
     * A text representation of the integer using only ASCII characters. I wrote 
     * this function only because the font used in the test suite output 
     * (NetBeans on Windows) lacks the square root character "&radic;".
     * All this function does is replace "&radic;" with "sqrt".
     * @return A String using only ASCII characters. For example, for 
     * "&radic;(-2)", the result will be "sqrt(-2)".
     */
    public String toASCIIString() {
        return this.toString().replace("\u221A", "sqrt");
    }
    
    /**
     * A text representation of the integer with theta notation when applicable, 
     * but using only ASCII characters. After writing toASCIIString, it only 
     * made sense to write this one as well.
     * @return A String using only ASCII characters. For instance, for "-1 + 
     * &theta;", the result will be "-1 + theta". If {@link #getRing()}{@link 
     * ImaginaryQuadraticRing#hasHalfIntegers() .hasHalfIntegers()} is false, 
     * this just returns the same String as {@link #toASCIIString()}.
     */
    public String toASCIIStringAlt() {
        if (this.imagQuadRing.d1mod4) {
            String intermediateString = this.toStringAlt();
            if (this.imagQuadRing.negRad == -3) {
                intermediateString = intermediateString.replace("\u03C9", "omega");
            } else {
                intermediateString = intermediateString.replace("\u03B8", "theta");
            }
            return intermediateString;
        } else {
            return this.toASCIIString();
        }
    }
    
    /**
     * A text representation of the integer suitable for use in a TeX document. 
     * If you prefer a single denominator instead of a separate denominator for 
     * the real and imaginary parts, use {@link #toTeXStringSingleDenom()}.
     * Although I have a unit test for this function, I have not tested 
     * inserting the output of this function into an actual TeX document.
     * @return A String. For example, for 1/2 + sqrt(-7)/2, the result should be 
     * "\frac{1}{2} + \frac{\sqrt{-7}}{2}".
     */
    public String toTeXString() {
        if (this.imagQuadRing.negRad == -1) {
            return this.toString();
        }
        if (this.norm() == 0) {
            return "0";
        }
        String IQIString;
        if (this.denominator == 1) {
            if (this.realPartMult == 0) {
                switch (this.imagPartMult) {
                    case -1:
                        IQIString = "-\\sqrt{" + this.imagQuadRing.negRad + "}";
                        break;
                    case 1:
                        IQIString = "\\sqrt{" + this.imagQuadRing.negRad + "}";
                        break;
                    default:
                        IQIString = this.imagPartMult + " \\sqrt{" + this.imagQuadRing.negRad + "}";
                }
            } else {
                IQIString = this.realPartMult + " + " + this.imagPartMult + " \\sqrt{" + this.imagQuadRing.negRad + "}";
                IQIString = IQIString.replace("+ -", " - ");
                IQIString = IQIString.replace(" 1 \\sqrt", " \\sqrt");
            }
        } else {
            IQIString = "\\frac{" + this.realPartMult + "}{2} + \\frac{" + this.imagPartMult + " \\sqrt{" + this.imagQuadRing.negRad + "}}{2}";
            IQIString = IQIString.replace("\\frac{-", "-\\frac{");
            IQIString = IQIString.replace("\\frac{1 \\sqrt", "\\frac{\\sqrt");
            IQIString = IQIString.replace("+ -", " - ");
        }
        return IQIString;
    }
    
    /**
     * A text representation of the integer suitable for use in a TeX document, 
     * but only with a single denominator for both the real and imaginary parts, 
     * as opposed to {@link #toTeXString()}.
     * Although I have a unit test for this function, I have not tested 
     * inserting the output of this function into an actual TeX document.
     * @return A String. For example, for 1/2 + sqrt(-7)/2, the result should be 
     * "\frac{1 + \sqrt{-7}}{2}".
     */
    public String toTeXStringSingleDenom() {
        if (this.denominator == 2) {
            String IQIString;
            IQIString = "\\frac{" + this.realPartMult + " + " + this.imagPartMult + " \\sqrt{" + this.imagQuadRing.negRad + "}}{2}";
            IQIString = IQIString.replace(" 1 \\sqrt", " \\sqrt");
            IQIString = IQIString.replace("+ -", " - ");
            return IQIString;
        } else {
            return this.toTeXString();
        }
    }

    /**
     * A text representation of the integer suitable for use in a TeX document, 
     * with theta notation when applicable.
     * @return A String. For example, for "-1 + &theta;", the result will be "-1 + 
     * \theta". If {@link #getRing()}{@link 
     * ImaginaryQuadraticRing#hasHalfIntegers() .hasHalfIntegers()} is false, 
     * this just returns the same String as {@link #toTeXString()}.
     */
    public String toTeXStringAlt() {
        if (this.imagQuadRing.d1mod4) {
            String IQIString = this.toStringAlt();
            IQIString = IQIString.replace("\u03C9", "\\omega");
            IQIString = IQIString.replace("\u03B8", "\\theta");
            return IQIString;
        } else {
            return this.toTeXString();
        }
    }
    
    /**
     * A text representation of the integer suitable for use in an HTML 
     * document. Although I have a unit test for this function, I have not 
     * tested inserting the output of this function into an actual HTML 
     * document.
     * @return  A String. For example, for 1/2 + sqrt(-7)/2, the result should 
     * be "1/2 + &radic;(-7)/2". Note that a character entity is used for the 
     * square root symbol.
     */
    public String toHTMLString() {
        String IQIString = this.toString();
        IQIString = IQIString.replace("i", "<i>i</i>");
        IQIString = IQIString.replace("\u221A", "&radic;");
        IQIString = IQIString.replace("-", "&minus;");
        return IQIString;
    }

    /**
     * A text representation of the integer suitable for use in a TeX document, 
     * with theta notation when applicable.
     * @return  A String. For example, for "-1 + &theta;", the result will be 
     * "-1 + &theta;". If {@link #getRing()}{@link 
     * ImaginaryQuadraticRing#hasHalfIntegers() .hasHalfIntegers()} is false, 
     * this just returns the same String as {@link #toHTMLString()}. Note that 
     * character entities are used for the square root symbol and the Greek 
     * letters theta and omega.
     */
    public String toHTMLStringAlt() {
        if (this.imagQuadRing.d1mod4) {
            String IQIString = this.toStringAlt();
            IQIString = IQIString.replace("\u03C9", "&omega;");
            IQIString = IQIString.replace("\u03B8", "&theta;");
            IQIString = IQIString.replace("-", "&minus;");
            return IQIString;
        } else {
            return this.toHTMLString();
        }
    }
    
    /**
     * Returns a hash code value for the imaginary quadratic integer. Overriding 
     * {@link Object#hashCode} on account of needing to override 
     * {@link Object#equals}. The hash code is based on the real part 
     * (multiplied by 2 when applicable), the imaginary part (multiplied by 2 
     * when applicable), the discriminant and the denominator. However, if the 
     * imaginary part is 0, the purely real integer is treated as a Gaussian 
     * integer. This was done in the hope of satisfying the contract that two 
     * objects that evaluate as equal also hash equal.
     * @return An integer which is hopefully unique from the hash codes of 
     * algebraic integers which are different that might occur in the same 
     * execution of the program.
     */
    @Override
    public int hashCode() {
        if (this.imagPartMult == 0) {
            return Objects.hash(this.realPartMult, this.imagPartMult, -1, this.denominator);
        } else {
            return Objects.hash(this.realPartMult, this.imagPartMult, this.imagQuadRing.negRad, this.denominator);
        }
    }
    
    /**
     * Compares whether an object is arithmetically equal to this imaginary 
     * quadratic integer.
     * @param obj The object to compare this to, preferably but not necessarily 
     * an object that implements the AlgebraicInteger interface.
     * @return True if the object is an imaginary quadratic integer 
     * arithmetically equal to this imaginary quadratic integer, false 
     * otherwise. If obj is an ImaginaryQuadraticInteger representing a purely 
     * real integer and this ImaginaryQuadraticInteger represents the same 
     * purely integer, then this function will return true even if they're from 
     * different rings. For example, 3 + 0 * sqrt(-2) is the same as 3 + 0 * 
     * sqrt(-5).
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ImaginaryQuadraticInteger other = (ImaginaryQuadraticInteger) obj;
        if (this.realPartMult != other.realPartMult) {
            return false;
        }
        if (this.imagPartMult != other.imagPartMult) {
            return false;
        }
        if (this.denominator != other.denominator) {
            return false;
        }
        if (this.imagPartMult == 0) {
            return true; // negRad might be different, but its square root multiplied by 0 is still 0
        }
        return (this.imagQuadRing.negRad == other.imagQuadRing.negRad);
    }
    
    /**
     * Checks whether this imaginary quadratic integer is arithmetically equal 
     * to a purely real integer. Not to be confused with {@link #norm()}.
     * @param num The purely real integer to check against. For example, 7.
     * @return True if the imaginary quadratic integer is indeed equal to the 
     * purely real integer, false otherwise. For example, given num = 7 + 0 * 
     * sqrt(-3), num.equalsInt(7) returns true. If instead num = 5/2 + 
     * sqrt(-3)/2, num.equalsInt(7) returns false even though num.norm() = 7.
     */
    public boolean equalsInt(int num) {
        if (this.imagPartMult == 0) {
            return (this.realPartMult == num);
        } else {
            return false;
        }
    }
    
    private static String preprocessNumberString(String stringToPreprocess) {
        String str = stringToPreprocess;
        str = str.replace(" ", "");
        str = str.replace("&minus;", "-");
        str = str.replace("\\frac{", "");
        str = str.replace("}{", "/");
        str = str.replace("{", "");
        str = str.replace("}", "");
        str = str.replace("<i>i</i>", "\u221A(-1)");
        str = str.replace("<i>j</i>", "\u221A(-1)");
        str = str.replace("i", "\u221A(-1)");
        str = str.replace("j", "\u221A(-1)");
        str = str.replace("\\sqrt", "\u221A");
        str = str.replace("sqrt", "\u221A");
        str = str.replace("&radic;", "\u221A");
        str = str.replace("\\omega", "\u03C9");
        str = str.replace("&omega;", "\u03C9");
        str = str.replace("omega", "\u03C9");
        str = str.replace("\\theta", "\u03B8");
        str = str.replace("&theta;", "\u03B8");
        str = str.replace("theta", "\u03B8");
        return str;
    }
    
    private static ImaginaryQuadraticInteger parseIQI(ImaginaryQuadraticRing ring, String str) {
        return new ImaginaryQuadraticInteger(0, 0, ring);
    }
        
    public static ImaginaryQuadraticInteger parseImaginaryQuadraticInteger(ImaginaryQuadraticRing ring, String str) {
        String parsingString = preprocessNumberString(str);
        int presumedD = ring.negRad;
        return parseIQI(ring, str);
    }
    
    public static ImaginaryQuadraticInteger parseImaginaryQuadraticInteger(String str) {
        String parsingString = preprocessNumberString(str);
        char currToken = str.charAt(0);
        switch (currToken) {
            case '(':
            case '+':
            case '-':
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                //
                break;
            default:
                String exceptionMessage = currToken + " is not a valid ImaginaryQuadraticInteger starting character.";
                throw new NumberFormatException(exceptionMessage);
        }
        return new ImaginaryQuadraticInteger(0, 0, new ImaginaryQuadraticRing(-1));
    }
  
    /**
     * Addition operation, since operator+ (plus) can't be overloaded. 
     * Computations are done with 64-bit variables. Overflow checking is 
     * rudimentary.
     * @param summand The imaginary quadratic integer to be added to this 
     * quadratic integer.
     * @return A new ImaginaryQuadraticInteger object with the result of the 
     * operation.
     * @throws AlgebraicDegreeOverflowException If the algebraic integers come 
     * from different quadratic rings, the result of the sum will be an 
     * algebraic integer of degree 4 and this runtime exception will be thrown.
     * @throws ArithmeticException A runtime exception thrown if either the real 
     * part or the imaginary part of the sum exceeds the range of the int data 
     * type. You may need long or even BigInteger for the calculation.
     */
    public ImaginaryQuadraticInteger plus(ImaginaryQuadraticInteger summand) {
        if (((this.imagPartMult != 0) && (summand.imagPartMult != 0)) && (this.imagQuadRing.negRad != summand.imagQuadRing.negRad)) {
            throw new AlgebraicDegreeOverflowException("This operation would result in an algebraic integer of degree 4.", 2, 4);
        }
        long sumRealPart = 0;
        long sumImagPart = 0;
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
        if (sumRealPart < Integer.MIN_VALUE || sumRealPart > Integer.MAX_VALUE) {
            throw new ArithmeticException("Real part of sum exceeds int data type:" + sumRealPart + " + " + sumImagPart + "sqrt(" + this.imagQuadRing.negRad + ")");
        }
        if (sumImagPart < Integer.MIN_VALUE || sumImagPart > Integer.MAX_VALUE) {
            throw new ArithmeticException("Imaginary part of sum exceeds int data type:" + sumRealPart + " + " + sumImagPart + "sqrt(" + this.imagQuadRing.negRad + ")");
        }
        return new ImaginaryQuadraticInteger((int) sumRealPart, (int) sumImagPart, this.imagQuadRing, sumDenom);
    }
    
    /**
     * Addition operation, since operator+ (plus) can't be overloaded. This does 
     * computations with 64-bit variables. Overflow checking is rudimentary. 
     * Although the previous plus function can be passed an 
     * ImaginaryQuadraticInteger with imagPartMult equal to 0, this function is 
     * to be preferred if you know for sure the summand is purely real.
     * @param summand The purely real integer to be added to the real part of 
     * the ImaginaryQuadraticInteger.
     * @return A new ImaginaryQuadraticInteger object with the result of the 
     * operation.
     * @throws ArithmeticException A runtime exception thrown if the real part 
     * of the sum exceeds the range of the int data type. The imaginary part of 
     * the sum should be fine, since the summand has a tacit imaginary part of 
     * 0.
     */
    public ImaginaryQuadraticInteger plus(int summand) {
        long sumRealPart = this.realPartMult;
        if (this.denominator == 2) {
            sumRealPart += (2 * summand);
        } else {
            sumRealPart += summand;
        }
        if (sumRealPart < Integer.MIN_VALUE || sumRealPart > Integer.MAX_VALUE) {
            throw new ArithmeticException("Real part of sum exceeds int data type:" + sumRealPart + " + " + this.imagPartMult + "sqrt(" + this.imagQuadRing.negRad + ")");
        }
        return new ImaginaryQuadraticInteger((int) sumRealPart, this.imagPartMult, this.imagQuadRing, this.denominator);
    }

    /**
     * Subtraction operation, since operator- can't be overloaded. This does 
     * computations with 64-bit variables. Overflow checking is rudimentary.
     * @param subtrahend The imaginary quadratic integer to be subtracted from 
     * this quadratic integer.
     * @return A new ImaginaryQuadraticInteger object with the result of the 
     * operation.
     * @throws AlgebraicDegreeOverflowException If the algebraic integers come 
     * from different quadratic rings, the result of the subtraction will be an 
     * algebraic integer of degree 4 and this runtime exception will be thrown.
     * @throws ArithmeticException A runtime exception thrown if either the real 
     * part or the imaginary part of the subtraction exceeds the range of the 
     * int data type. You may need long or even BigInteger for the calculation.
     */
    public ImaginaryQuadraticInteger minus(ImaginaryQuadraticInteger subtrahend) {
        if (((this.imagPartMult != 0) && (subtrahend.imagPartMult != 0)) && (this.imagQuadRing.negRad != subtrahend.imagQuadRing.negRad)) {
            throw new AlgebraicDegreeOverflowException("This operation would result in an algebraic integer of degree 4.", 2, 4);
        }
        long subtractionRealPart = 0;
        long subtractionImagPart = 0;
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
        if (subtractionRealPart < Integer.MIN_VALUE || subtractionRealPart > Integer.MAX_VALUE) {
            throw new ArithmeticException("Real part of subtraction exceeds int data type:" + subtractionRealPart + " + " + subtractionImagPart + "sqrt(" + this.imagQuadRing.negRad + ")");
        }
        if (subtractionImagPart < Integer.MIN_VALUE || subtractionImagPart > Integer.MAX_VALUE) {
            throw new ArithmeticException("Imaginary part of subtraction exceeds int data type:" + subtractionRealPart + " + " + subtractionImagPart + "sqrt(" + this.imagQuadRing.negRad + ")");
        }
        return new ImaginaryQuadraticInteger((int) subtractionRealPart, (int) subtractionImagPart, this.imagQuadRing, subtractionDenom);
    }
    
    /**
     * Subtraction operation, since operator- can't be overloaded. Although the 
     * previous minus function can be passed an ImaginaryQuadraticInteger with 
     * imagPartMult equal to 0, this function is to be preferred if you know for 
     * sure the subtrahend is purely real. Computations are done with 64-bit 
     * variables. Overflow checking is rudimentary.
     * @param subtrahend The purely real integer to be subtracted from this 
     * quadratic integer.
     * @return A new ImaginaryQuadraticInteger object with the result of the 
     * operation.
     * @throws ArithmeticException A runtime exception thrown if the real part 
     * of the subtraction exceeds the range of the int data type. The imaginary 
     * part of the sum should be fine, since the subtrahend has a tacit 
     * imaginary part of 0.
     */
    public ImaginaryQuadraticInteger minus(int subtrahend) {
        long subtractionRealPart = this.realPartMult;
        if (this.denominator == 2) {
            subtractionRealPart -= (2 * subtrahend);
        } else {
            subtractionRealPart -= subtrahend;
        }
        if (subtractionRealPart < Integer.MIN_VALUE || subtractionRealPart > Integer.MAX_VALUE) {
            throw new ArithmeticException("Real part of subtraction exceeds int data type:" + subtractionRealPart + " + " + this.imagPartMult + "sqrt(" + this.imagQuadRing.negRad + ")");
        }
        return new ImaginaryQuadraticInteger((int) subtractionRealPart, this.imagPartMult, this.imagQuadRing, this.denominator);

    }
    
    /**
     * Multiplication operation, since operator* can't be overloaded. 
     * Computations are done with 64-bit variables. Overflow checking is 
     * rudimentary.
     * @param multiplicand The imaginary quadratic integer to be multiplied by 
     * this quadratic integer.
     * @return A new ImaginaryQuadraticInteger object with the result of the 
     * operation.
     * @throws AlgebraicDegreeOverflowException If the algebraic integers come 
     * from different quadratic rings, the product will be an algebraic integer 
     * of degree 4 and this runtime exception will be thrown.
     * @throws ArithmeticException A runtime exception thrown if either the real 
     * part or the imaginary part of the product exceeds the range of the int 
     * data type. You may need long or even BigInteger for the calculation.
     */
    public ImaginaryQuadraticInteger times(ImaginaryQuadraticInteger multiplicand) {
        if (((this.imagPartMult != 0) && (multiplicand.imagPartMult != 0)) && (this.imagQuadRing.negRad != multiplicand.imagQuadRing.negRad)) {
            throw new AlgebraicDegreeOverflowException("This operation would result in an algebraic integer of degree 4.", 2, 4);
        }
        long intermediateRealPart = this.realPartMult * multiplicand.realPartMult - this.imagPartMult * multiplicand.imagPartMult * this.imagQuadRing.absNegRad;
        long intermediateImagPart = this.realPartMult * multiplicand.imagPartMult + this.imagPartMult * multiplicand.realPartMult;
        int intermediateDenom = this.denominator * multiplicand.denominator;
        if (intermediateDenom == 4) {
            intermediateRealPart /= 2;
            intermediateImagPart /= 2;
            intermediateDenom = 2;
        }
        /* There is no need to check if intermediateDenom is equal to 2 and both 
           intermediateRealPart and intermediateImagPart are even because the 
           ImaginaryQuadraticInteger constructor will take care of halving the 
           parts and changing the denominator to 1. */
        if (intermediateRealPart < Integer.MIN_VALUE || intermediateRealPart > Integer.MAX_VALUE) {
            throw new ArithmeticException("Real part of product exceeds int data type:" + intermediateRealPart + " + " + intermediateImagPart + "sqrt(" + this.imagQuadRing.negRad + ")");
        }
        if (intermediateImagPart < Integer.MIN_VALUE || intermediateImagPart > Integer.MAX_VALUE) {
            throw new ArithmeticException("Imaginary part of product exceeds int data type:" + intermediateRealPart + " + " + intermediateImagPart + "sqrt(" + this.imagQuadRing.negRad + ")");
        }
        return new ImaginaryQuadraticInteger((int) intermediateRealPart, (int) intermediateImagPart, this.imagQuadRing, intermediateDenom);
    }
    
    /**
     * Multiplication operation, since operator* can't be overloaded. Although 
     * the previous times function can be passed an ImaginaryQuadraticInteger 
     * with imagPartMult equal to 0, this function is to be preferred if you 
     * know for sure the multiplicand is purely real. Computations are done with 
     * 64-bit variables. Overflow checking is rudimentary.
     * @param multiplicand The purely real integer to be multiplied by this 
     * quadratic integer.
     * @return A new ImaginaryQuadraticInteger object with the result of the 
     * operation.
     * @throws ArithmeticException A runtime exception thrown if either the real 
     * part or the imaginary part of the product exceeds the range of the int 
     * data type.
     */
    public ImaginaryQuadraticInteger times(int multiplicand) {
        long multiplicationRealPart = this.realPartMult * multiplicand;
        long multiplicationImagPart = this.imagPartMult * multiplicand;
        // No need to worry about denominator, constructor will take care of it if necessary.
        if (multiplicationRealPart < Integer.MIN_VALUE || multiplicationRealPart > Integer.MAX_VALUE) {
            throw new ArithmeticException("Real part of product exceeds int data type:" + multiplicationRealPart + " + " + multiplicationImagPart + "sqrt(" + this.imagQuadRing.negRad + ")");
        }
        if (multiplicationImagPart < Integer.MIN_VALUE || multiplicationImagPart > Integer.MAX_VALUE) {
            throw new ArithmeticException("Imaginary part of product exceeds int data type:" + multiplicationRealPart + " + " + multiplicationImagPart + "sqrt(" + this.imagQuadRing.negRad + ")");
        }
        return new ImaginaryQuadraticInteger((int) multiplicationRealPart, (int) multiplicationImagPart, this.imagQuadRing, this.denominator);
    }
   
    /**
     * Division operation, since operator/ can't be overloaded. Computations are 
     * done with 64-bit variables. Overflow checking is rudimentary.
     * @param divisor The imaginary quadratic integer by which to divide this 
     * quadratic integer.
     * @return A new ImaginaryQuadraticInteger object with the result of the 
     * operation.
     * @throws AlgebraicDegreeOverflowException If the algebraic integers come 
     * from different quadratic rings, the result of the division will be an 
     * algebraic integer of degree 4 and this runtime exception will be thrown.
     * @throws NotDivisibleException If the imaginary quadratic integer is not 
     * divisible by the divisor, this checked exception will be thrown.
     * @throws IllegalArgumentException Division by 0 is not allowed, and will 
     * trigger this runtime exception.
     * @throws ArithmeticException A runtime exception thrown if either the real 
     * part or the imaginary part of the division exceeds the range of the int 
     * data type.
     */
    public ImaginaryQuadraticInteger divides(ImaginaryQuadraticInteger divisor) throws NotDivisibleException {
        if (((this.imagPartMult != 0) && (divisor.imagPartMult != 0)) && (this.imagQuadRing.negRad != divisor.imagQuadRing.negRad)) {
            throw new AlgebraicDegreeOverflowException("This operation would result in an algebraic integer of degree 4.", 2, 4);
        }
        if (divisor.realPartMult == 0 && divisor.imagPartMult == 0) {
            throw new IllegalArgumentException("Division by 0 is not allowed.");
        }
        long intermediateRealPart = (long) this.realPartMult * (long) divisor.realPartMult + (long) this.imagPartMult * (long) divisor.imagPartMult * (long) this.imagQuadRing.absNegRad;
        long intermediateImagPart = (long) this.imagPartMult * (long) divisor.realPartMult - (long) this.realPartMult * (long) divisor.imagPartMult;
        long intermediateDenom = (long) (divisor.norm() * (long) this.denominator * (long) divisor.denominator);
        long realCutDown = NumberTheoreticFunctionsCalculator.euclideanGCD(intermediateRealPart, intermediateDenom);
        long imagCutDown = NumberTheoreticFunctionsCalculator.euclideanGCD(intermediateImagPart, intermediateDenom);
        if (realCutDown < imagCutDown) {
            intermediateRealPart /= realCutDown;
            intermediateImagPart /= realCutDown;
            intermediateDenom /= realCutDown;
        } else {
            intermediateRealPart /= imagCutDown;
            intermediateImagPart /= imagCutDown;
            intermediateDenom /= imagCutDown;
        }
        boolean divisibleFlag;
        if (this.imagQuadRing.d1mod4) {
            divisibleFlag = (intermediateDenom == 1 || intermediateDenom == 2);
            if (intermediateDenom == 2) {
                divisibleFlag = (NumberTheoreticFunctionsCalculator.euclideanGCD(intermediateRealPart, 2) == NumberTheoreticFunctionsCalculator.euclideanGCD(intermediateImagPart, 2));
            }
        } else {
            divisibleFlag = (intermediateDenom == 1);
        }
        if (!divisibleFlag) {
            String exceptionMessage = this.toASCIIString() + " is not divisible by " + divisor.toASCIIString() + ".";
            throw new NotDivisibleException(exceptionMessage, intermediateRealPart, intermediateImagPart, intermediateDenom, this.imagQuadRing.negRad);
        }
        if (intermediateRealPart < Integer.MIN_VALUE || intermediateRealPart > Integer.MAX_VALUE) {
            throw new ArithmeticException("Real part of division exceeds int data type:" + intermediateRealPart + " + " + intermediateImagPart + "sqrt(" + this.imagQuadRing.negRad + ")");
        }
        if (intermediateImagPart < Integer.MIN_VALUE || intermediateImagPart > Integer.MAX_VALUE) {
            throw new ArithmeticException("Imaginary part of division exceeds int data type:" + intermediateRealPart + " + " + intermediateImagPart + "sqrt(" + this.imagQuadRing.negRad + ")");
        }
        return new ImaginaryQuadraticInteger((int) intermediateRealPart, (int) intermediateImagPart, this.imagQuadRing, (int) intermediateDenom);
    }
    
    /**
     * Division operation, since operator/ can't be overloaded. Although the 
     * previous divides function can be passed an ImaginaryQuadraticInteger with 
     * imagPartMult equal to 0, this function is to be preferred if you know for 
     * sure the divisor is purely real. Computations are done with 64-bit 
     * variables. Overflow checking is rudimentary.
     * @param divisor The purely real integer by which to divide this quadratic 
     * integer.
     * @return A new ImaginaryQuadraticInteger object with the result of the 
     * operation.
     * @throws NotDivisibleException If the imaginary quadratic integer is not 
     * divisible by the divisor, this exception will be thrown.
     * @throws IllegalArgumentException Division by 0 is not allowed, and will 
     * trigger this runtime exception.
     * @throws ArithmeticException A runtime exception thrown if either the real 
     * part or the imaginary part of the division exceeds the range of the int 
     * data type.
     */
    public ImaginaryQuadraticInteger divides(int divisor) throws NotDivisibleException {
        if (divisor == 0) {
            throw new IllegalArgumentException("Division by 0 is not allowed.");
        }
        long intermediateRealPart = this.realPartMult;
        long intermediateImagPart = this.imagPartMult;
        long intermediateDenom = this.denominator * divisor;
        long realCutDown = NumberTheoreticFunctionsCalculator.euclideanGCD(intermediateRealPart, intermediateDenom);
        long imagCutDown = NumberTheoreticFunctionsCalculator.euclideanGCD(intermediateImagPart, intermediateDenom);
        if (realCutDown < imagCutDown) {
            intermediateRealPart /= realCutDown;
            intermediateImagPart /= realCutDown;
            intermediateDenom /= realCutDown;
        } else {
            intermediateRealPart /= imagCutDown;
            intermediateImagPart /= imagCutDown;
            intermediateDenom /= imagCutDown;
        }
        if (intermediateDenom < 0) {
            intermediateRealPart *= -1;
            intermediateImagPart *= -1;
            intermediateDenom *= -1;
        }
        boolean divisibleFlag;
        if (this.imagQuadRing.d1mod4) {
            divisibleFlag = (intermediateDenom == 1 || intermediateDenom == 2);
            if (intermediateDenom == 2) {
                divisibleFlag = (NumberTheoreticFunctionsCalculator.euclideanGCD(intermediateRealPart, 2) == NumberTheoreticFunctionsCalculator.euclideanGCD(intermediateImagPart, 2));
            }
        } else {
            divisibleFlag = (intermediateDenom == 1);
        }
        if (!divisibleFlag) {
            String exceptionMessage = this.toASCIIString() + " is not divisible by " + divisor + ".";
            throw new NotDivisibleException(exceptionMessage, intermediateRealPart, intermediateImagPart, intermediateDenom, this.imagQuadRing.negRad);
        }
        if (intermediateRealPart < Integer.MIN_VALUE || intermediateRealPart > Integer.MAX_VALUE) {
            throw new ArithmeticException("Real part of division exceeds int data type:" + intermediateRealPart + " + " + intermediateImagPart + "sqrt(" + this.imagQuadRing.negRad + ")");
        }
        if (intermediateImagPart < Integer.MIN_VALUE || intermediateImagPart > Integer.MAX_VALUE) {
            throw new ArithmeticException("Imaginary part of division exceeds int data type:" + intermediateRealPart + " + " + intermediateImagPart + "sqrt(" + this.imagQuadRing.negRad + ")");
        }
        return new ImaginaryQuadraticInteger((int) intermediateRealPart, (int) intermediateImagPart, this.imagQuadRing, (int) intermediateDenom);
    }
    
    /**
     * Alternative object constructor, may be used when the denominator is known 
     * to be 1.
     * @param a The real part of the imaginary quadratic integer. For example, 
     * for 5 + &radic;-3, this parameter would be 5.
     * @param b The part to be multiplied by sqrt(d). For example, for 5 + 
     * &radic;-3, this parameter would be 1.
     * @param R The ring to which this algebraic integer belongs to. For 
     * example, for 5 + &radic;-3, this parameter could be <code>new 
     * ImaginaryQuadraticRing(-3)</code>.
     */
    public ImaginaryQuadraticInteger(int a, int b, ImaginaryQuadraticRing R) {
        this.realPartMult = a;
        this.imagPartMult = b;
        this.imagQuadRing = R;
        this.denominator = 1;
    }
    
    /**
     * Primary object constructor.
     * @param a The real part of the imaginary quadratic integer, multiplied by 
     * 2 when applicable. For example, for 5/2 + (&radic;-3)/2, this parameter 
     * would be 5.
     * @param b The part to be multiplied by sqrt(d), multiplied by 2 when 
     * applicable. For example, for 5/2 + (&radic;-3)/2, this parameter would be 
     * 1.
     * @param R The ring to which this algebraic integer belongs to. For 
     * example, for 5 + &radic;-3, this parameter could be <code>new 
     * ImaginaryQuadraticRing(-3)</code>.
     * @param denom In most cases 1, but may be 2 if a and b have the same 
     * parity and d = 1 mod 4. In the 5/2 + (&radic;-3)/2 example, this would be 
     * 2.
     * @throws IllegalArgumentException If denom is anything other than 1 or 2, 
     * or if denom is 2 but a and b don't match parity. However, if passed denom 
     * of -1 or -2, the constructor will quietly change it to 1 or 2, and 
     * multiply a and b by -1. Also, if d is not 1 mod 4 but a and b are both 
     * even, this constructor will quietly divide a and b by 2, otherwise this 
     * exception will be thrown.
     */
    public ImaginaryQuadraticInteger(int a, int b, ImaginaryQuadraticRing R, int denom) {
        boolean abParityMatch;
        if (denom == -1 || denom == -2) {
            a *= -1;
            b *= -1;
            denom *= -1;
        }
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
    
    /**
     * The main entry point for the package. For now, it only starts {@link 
     * RingWindowDisplay}. In a later version (no later than 1.0), it will be 
     * able to accept command line arguments.
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        RingWindowDisplay.startRingWindowDisplay(-1);
        switch (args.length) {
            case 0:
                RingWindowDisplay.startRingWindowDisplay(RingWindowDisplay.DEFAULT_RING_D);
                break;
            case 1:
                switch (args[0]) {
                    case "-v":
                    case "-vers":
                    case "-version":
                    case "v":
                    case "vers":
                    case "version":
                        System.out.println("Imaginary Quadratic Integer package\nVersion 0.9\n\u00A9 2018 Alonso del Arte");
                        break;
                    default:
                        int ringChoice = RingWindowDisplay.DEFAULT_RING_D;
                        try {
                            ringChoice = Integer.parseInt(args[0]);
                            if (ringChoice > 0) {
                                System.out.print(ringChoice + " is not negative.");
                                ringChoice *= -1;
                                System.out.println(" Substituting " + ringChoice + ".");
                            }
                            while (!NumberTheoreticFunctionsCalculator.isSquareFree(ringChoice)) {
                                System.out.print(ringChoice + " is not squarefree.");
                                ringChoice--;
                                System.out.println(" Substituting " + ringChoice + "...");
                            }
                            if (ringChoice < RingWindowDisplay.MINIMUM_RING_D) {
                                System.out.print(ringChoice + " is less than " + RingWindowDisplay.MINIMUM_RING_D + ", which is the minimum for the Ring Viewer program.");
                                ringChoice = RingWindowDisplay.DEFAULT_RING_D;
                                System.out.println(" Substituting " + ringChoice + ".");
                            }
                        } catch (NumberFormatException nfe) {
                            System.out.println(nfe.getMessage());
                            System.out.println("Substituting " + ringChoice + ".");
                        }
                        RingWindowDisplay.startRingWindowDisplay(ringChoice);
                }
            /* If there are more than two parameters, only the first two 
               parameters will be processed */
            default:
                int ringDiscr = RingWindowDisplay.DEFAULT_RING_D;
                ImaginaryQuadraticRing ring;
                ImaginaryQuadraticInteger number;
                try {
                    ringDiscr = Integer.parseInt(args[0]);
                    while (!NumberTheoreticFunctionsCalculator.isSquareFree(ringDiscr)) {
                        System.out.print(ringDiscr + " is not squarefree.");
                        ringDiscr--;
                        System.out.println(" Substituting " + ringDiscr + "...");
                    }
                    if (ringDiscr > 0) {
                        System.out.println(ringDiscr + " is not negative.");
                        ringDiscr *= -1;
                        System.out.println(" Substituting " + ringDiscr + ".");
                    }
                } catch (NumberFormatException nfe) {
                    System.out.println(nfe.getMessage());
                    System.out.println("Substituting " + ringDiscr);
                }
                ring = new ImaginaryQuadraticRing(ringDiscr);
                number = ImaginaryQuadraticInteger.parseImaginaryQuadraticInteger(ring, args[1]);
                System.out.print(number.toASCIIString());
                if (ring.hasHalfIntegers()) {
                    System.out.print(" = " + number.toASCIIStringAlt());
                }
                if (NumberTheoreticFunctionsCalculator.isIrreducible(number)) {
                    System.out.print(" is irreducible ");
                    if (NumberTheoreticFunctionsCalculator.isPrime(number)) {
                        System.out.println(" and prime.");
                    } else {
                        System.out.println(" but not prime.");
                    }
                }
                System.out.print("Conjugate is " + number.conjugate().toASCIIString());
                if (ring.hasHalfIntegers()) {
                    System.out.println(" = " + number.conjugate().toASCIIString() + ".");
                } else {
                    System.out.println(".");
                }
                System.out.println("Trace is " + number.trace());
                System.out.println("Norm is " + number.norm());
                System.out.println("Minimal polynomial is " + number.minPolynomialString());
        }
    
    } 
    
}