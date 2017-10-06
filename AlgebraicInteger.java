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
public interface AlgebraicInteger {
    
    /**
     * Gives the algebraic degree of the algebraic integer
     * @return 0 if the algebraic integer is 0, a positive integer for any other algebraic integer
     */
    int algebraicDegree();
    
    /**
     * Gives the trace of the algebraic integer
     * @return The trace
     */
    int trace();
    
    /**
     * Gives the norm of the algebraic integer, useful for comparing
     * @return The norm
     */
    int norm();
    
    /**
     * Gives the coefficients for the minimal polynomial of the algebraic integer
     * @return An array of integers, in total one more than the algebraic degree
     */
    int[] minPolynomial();
    
    /**
     * Gives the minimal polynomial formatted as a string, e.g., "x^3 + x^2 - x + 1"
     * @return A string in which the variable x appears as many times as dictated by the algebraic degree
     */
    String minPolynomialString();
    
}
