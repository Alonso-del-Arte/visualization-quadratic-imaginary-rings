/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
    public int algebraicDegree();
    
    /**
     * Gives the trace of the algebraic integer
     * @return Trace
     */
    public int trace();
    
    /**
     * Gives the norm of the algebraic integer, useful for comparing
     * @return 
     */
    public int norm();
    
    /**
     * Gives the coefficients for the minimal polynomial of the algebraic integer
     * @return An array of integers, in total one more than the algebraic degree
     */
    public int[] minPolynomial();
    
}
