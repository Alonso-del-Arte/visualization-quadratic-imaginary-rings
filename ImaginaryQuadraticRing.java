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

/**
 * An object to represent an imaginary quadratic integer ring.
 * A ring of quadratic integers contains infinitely many numbers.
 * But, for the sake of this program, objects of type ImaginaryQuadraticRing are held by objects of type ImaginaryQuadraticInteger rather than the other way around.
 * @author Alonso del Arte
 */
public class ImaginaryQuadraticRing {

    /**
     * Ought to be a squarefree negative integer.
     */
    protected int negRad;
    
    /**
     * A convenient holder for the absolute value of negRad.
     */
    protected int absNegRad;
    
    /**
     * A convenient holder for the square root of the absNegRad.
     */
    protected double absNegRadSqrt;
    
    /**
     * Should be true only if negRad is congruent to 1 modulo 4.
     */
    protected boolean d1mod4;
    
    private static boolean preferenceForBlackboardBold = true;
    
    /**
     * Gets the value of the discriminant, divided by 4 when necessary.
     * @return The negative integer d for Q(sqrt(d)). For instance, for Z[i], returns -1, for Z[sqrt(-2)] returns -2, for Z[omega] returns -3, etc.
     */
    public int getNegRad() {
        return this.negRad;
    }
    
    /**
     * Gets the absolute value of the discriminant, divided by 4 when necessary, but always multiplied by -1.
     * @return The absolute value of the negative integer d for Q(sqrt(d)). For instance, for Z[i], returns 1, for Z[sqrt(-2)] returns 2, for Z[omega] returns 3, etc.
     */
    public int getAbsNegRad() {
        return this.absNegRad;
    }

    /**
     * Gets the square root of the absolute value of the discriminant, divided by 4 when necessary, but always multiplied by -1.
     * @return sqrt(abs(d)). For example, for Z[i] this would 1, for Z[sqrt(-2)] this would be approximately 1.414213562373...
     */
    public double getAbsNegRadSqrt() {
        return this.absNegRadSqrt;
    }
    
    /**
     * Tells whether the ring has what are imprecisely called "half-integers."
     * These are numbers of the form a/2 + b sqrt(d)/2, with both a and b odd integers.
     * @return True if d is congruent to 1 modulo 4, false otherwise.
     */
    public boolean hasHalfIntegers() {
        return this.d1mod4;
    }
    
    /**
     * Query the setting of the preference for blackboard bold.
     * @return true if blackboard bold is preferred, false if plain bold is preferred.
     */
    public static boolean preferBlackboardBold() {
        return preferenceForBlackboardBold;
    }
    
    /**
     * Set preference for blackboard bold or plain bold. This is only relevant for the functions toTeXString() and toHTMLString().
     * @param preferenceForBB true if blackboard bold is preferred, false if plain bold is preferred.
     */
    public static void preferBlackboardBold(boolean preferenceForBB) {
        preferenceForBlackboardBold = preferenceForBB;
    }
    
    /**
     * A text representation of the ring's label.
     * In some contexts, toTeXString(), toHTMLString() or toFilenameString may be preferable.
     * @return A String representing the imaginary quadratic ring which can be output to the console.
     */
    @Override
    public String toString() {
        String IQRString;
        switch (this.negRad) {
            case -1:
                IQRString = "Z[i]"; // i is the imaginary unit, sqrt(-1)
                break;
            case -3:
                IQRString = "Z[\u03C9]"; // omega = -1/2 + sqrt(-3)/2 is a complex cubic root of 1
                break;
            default:
                if (this.d1mod4) {
                    IQRString = "O_(Q(\u221A" + this.negRad + "))";
                } else {
                    IQRString = "Z[\u221A" + this.negRad + "]";
                }
        }
        return IQRString;
    }

    /**
     * A text representation of the ring's label suitable for use in a TeX document.
     * I have not tested this function in the context of outputting to a TeX document.
     * @return A String suitable for use in a TeX document, if I haven't made any mistakes.
     */
    public String toTeXString() {
        String IQRString;
        String QChar;
        String ZChar;
        if (preferenceForBlackboardBold) {
            QChar = "\\mathbb Q";
            ZChar = "\\mathbb Z";
        } else {
            QChar = "\\textbf Q";
            ZChar = "\\textbf Z";
        }
        switch (this.negRad) {
            case -1:
                IQRString = ZChar + "[i]";
                break;
            case -3:
                IQRString = ZChar + "[\\omega]";
                break;
            default:
                if (this.d1mod4) {
                    IQRString = "\\mathcal O_{" + QChar + "(\\sqrt{" + this.negRad + "})}";
                } else {
                    IQRString = ZChar + "[\\sqrt{" + this.negRad + "}]";
            }
        }
        return IQRString;
    }
    
    /**
     * A text representation of the ring's label suitable for use in an HTML document.
     * I have not tested this function in the context of outputting to an HTML document.
     * @return A String suitable for use in an HTML document, if I haven't made any mistakes. If preferenceForBlackboardBold is true, this also assumes the font has the relevant Unicode characters.
     */
    public String toHTMLString() {
        String IQRString;
        String QChar;
        String ZChar;
        if (preferenceForBlackboardBold) {
            QChar = "\u211A"; // Double-struck capital Q
            ZChar = "\u2124"; // Double-struck capital Z
        } else {
            QChar = "<b>Q</b>";
            ZChar = "<b>Z</b>";
        }
        switch (this.negRad) {
            case -1:
                IQRString = ZChar + "[<i>i</i>]";
                break;
            case -3:
                IQRString = ZChar + "[\u03C9]";
                break;
            default:
                if (this.d1mod4) {
                    IQRString = "<i>O</i><sub>" + QChar + "(&radic;(" + this.negRad + ")</sub>";
                } else {
                    IQRString = ZChar + "[&radic;" + this.negRad + "]";
                }
        }
        return IQRString;
    }
    
    /**
     * A text representation of the ring's label suitable for use in a filename.
     * With our modern operating systems, it may be unnecessary to worry about illegal characters in toString().
     * But just in case, here is a function whose output will hopefully help the calling program conform to the old MS-DOS 8.3 standard.
     * There is one context in which I have definitely found this function useful, and that is in the output of the test suite, for which the font seems to lack Greek letters.
     * @return A string suitable for use in a filename.
     */
    public String toFilenameString() {
        String IQRString;
        switch (this.negRad) {
            case -1:
                IQRString = "ZI";
                break;
            case -3:
                IQRString = "ZW"; // It is frowned upon to use "w" as a makeshift omega, but for the purpose here it is acceptable.
                break;
            default:
                if (this.d1mod4) {
                    IQRString = "OQI" + this.absNegRad;
                } else {
                    IQRString = "ZI" + this.absNegRad;
                }
        }
        return IQRString;
    }
    
    /**
     * Class constructor.
     * @param d A negative, squarefree integer.
     * @throws IllegalArgumentException If d is 0 or any positive integer, or if d is negative but not squarefree.
     */
    public ImaginaryQuadraticRing(int d) {
        if (d > -1) {
            throw new IllegalArgumentException("Negative integer required for parameter d.");
        }
        if (!NumberTheoreticFunctionsCalculator.isSquareFree(d)) {
            throw new IllegalArgumentException("Squarefree integer required for parameter d.");
        }
        this.d1mod4 = (d % 4 == -3); // For whatever reason, odd negative numbers modulo 4 are given as -3 and -1 rather than 1 and 3.
        this.negRad = d;
        this.absNegRad = Math.abs(negRad);
        this.absNegRadSqrt = Math.sqrt(absNegRad);
    }
    
}