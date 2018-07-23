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
 * <p>WORK IN PROGRESS. At this stage, only RingWindowDisplay has a GUI, which 
 * ImaginaryQuadraticInteger.main() calls upon starting.</p>
 * 
 * <p>Also, I have thought that perhaps this project ought to be split among two 
 * or three packages. Then this package would consist of pretty much the same 
 * classes it currently has but perhaps RingWindowDisplay would be shipped out 
 * to a view package and NumberTheoreticFunctionsCalculator would go to a 
 * calculators package. The exceptions would stay in this package.</p>
 * 
 * <p>In late May 2018, I did decide to make a separate package for the file 
 * filters which RingWindowDisplay.saveDiagramAs() uses (or might use) to get 
 * the user to select a place where to save diagrams produced by the program. 
 * That package is called, appropriately enough, filefilters.</p>
 * 
 * <p>Copyright &copy; 2018 Alonso del Arte.</p>
 */
package imaginaryquadraticinteger;
