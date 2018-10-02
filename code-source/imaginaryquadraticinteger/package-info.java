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
 * rings.</li>
 * <li>{@link imaginaryquadraticinteger.AlgebraicDegreeOverflowException} 
 * defines an exception to be thrown when an arithmetic operation results in an 
 * algebraic integer of higher algebraic degree than the implementation of 
 * AlgebraicInteger can handle. This exception is general enough to work with 
 * any implementation of AlgebraicInteger.</li>
 * <li>{@link imaginaryquadraticinteger.NotDivisibleException} defines an 
 * exception to be thrown when the result of dividing one imaginary quadratic 
 * integer by another results in an algebraic number that is not an algebraic 
 * integer. At some point in the future I want to make this exception usable for 
 * any implementation of AlgebraicInteger, not just 
 * ImaginaryQuadraticInteger. This exception should not be used in cases when 
 * AlgebraicDegreeOverflowException would be more appropriate.</li>
 * <li>{@link imaginaryquadraticinteger.NonEuclideanDomainException} defines an 
 * exception to be thrown when an Euclidean GCD function is called on numbers 
 * that are not from an Euclidean domain. For now this exception is also limited 
 * to ImaginaryQuadraticInteger.</li>
 * <li>{@link imaginaryquadraticinteger.NonUniqueFactorizationDomainException} 
 * defines an exception to be thrown when a prime factorization function is 
 * called on a number that is not from a unique factorization domain. For now 
 * this exception is also limited to ImaginaryQuadraticInteger.</li>
 * <li>{@link imaginaryquadraticinteger.UnsupportedNumberDomainException} 
 * defines an exception to be thrown when an arithmetic operation results in an 
 * algebraic integer that no available implementation of AlgebraicInteger can 
 * handle. For example, the multiplication of two purely imaginary quadratic 
 * integers could result in a purely real quadratic integer, but for now this 
 * package lacks RealQuadraticInteger. This exception is general enough to work 
 * with any implementation of AlgebraicInteger. It should be used in situations 
 * in which AlgebraicDegreeOverflowException would be a kludge.</li>
 * </ul>
 * 
 * <p>Copyright &copy; 2018 Alonso del Arte.</p>
 */
package imaginaryquadraticinteger;
