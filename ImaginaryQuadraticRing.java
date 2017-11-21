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
 *
 * @author Alonso del Arte
 */
public class ImaginaryQuadraticRing {

    /**
     * Ought to be a squarefree negative integer
     */
    protected int negRad;
    
    /**
     * A convenient holder for the absolute value of negRad
     */
    protected int absNegRad;
    
    /**
     * A convenient holder for the square root of the absNegRad
     */
    protected double absNegRadSqrt;
    
    /**
     * Should be true only if negRad is congruent to 1 modulo 4
     */
    protected boolean d1mod4;
    
    private static boolean preferenceForBlackboardBold = true;
    
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
     * A text representation of the ring. In some contexts, toTeXString() or toHTMLString() may be preferable.
     * @return A String representing the imaginary quadratic ring which can be output to the console.
     */
    @Override
    public String toString() {
        String IQRString;
        switch (this.negRad) {
            case -1:
                IQRString = "Z[i]";
                break;
            case -3:
                IQRString = "Z[\u03C9]";
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
     * I have not tested this function.
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
     * I have not tested this function.
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
     * Class constructor
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
        this.d1mod4 = (d % 4 == -3);
        this.negRad = d;
        this.absNegRad = Math.abs(negRad);
        this.absNegRadSqrt = Math.sqrt(absNegRad);
    }
    
}
