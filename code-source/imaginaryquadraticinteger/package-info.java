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
/**
 * <b>ImaginaryQuadraticInteger</b>, a package for visualizing algebraic
 * integers of degree 2 in the complex plane.
 * 
 * <p>The package consists of the following classes:</p>
 * 
 * <ul>
 * <li>{@link imaginaryquadraticinteger.AlgebraicInteger}, an interface for 
 * defining algebraic integer classes.</li>
 * <li>{@link imaginaryquadraticinteger.ImaginaryQuadraticRing}, defines objects 
 * to represent imaginary quadratic integer rings.</li>
 * <li>{@link imaginaryquadraticinteger.ImaginaryQuadraticInteger} is the main 
 * class, defines objects representing imaginary quadratic integers.</li>
 * <li>{@link imaginaryquadraticinteger.NumberTheoreticFunctionsCalculator} is a 
 * collection of number theoretic functions, including basic primality testing 
 * and the Euclidean GCD algorithm.</li>
 * <li>{@link imaginaryquadraticinteger.RingWindowDisplay} is a Swing component 
 * in which to display diagrams of prime numbers in various quadratic integer 
 * rings. Note that this class is final due to a leaky constructor problem.</li>
 * <li>{@link imaginaryquadraticinteger.AlgebraicDegreeOverflowException}</li>
 * <li>{@link imaginaryquadraticinteger.NotDivisibleException}</li>
 * <li>{@link imaginaryquadraticinteger.NonEuclideanDomainException}</li>
 * <li>{@link imaginaryquadraticinteger.NonUniqueFactorizationDomainException}</li>
 * </ul>
 * 
 * <p>Copyright &copy; 2018 Alonso del Arte.</p>
 */
package imaginaryquadraticinteger;
