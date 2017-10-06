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
    
    public ImaginaryQuadraticRing(int d) {
        //
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
