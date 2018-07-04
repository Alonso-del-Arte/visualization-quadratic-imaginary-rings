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

/**
 * This checked exception is to be thrown when a prime factorization function is 
 * called on a number that is not from a unique factorization domain.
 * @author Alonso del Arte
 */
public class NonUniqueFactorizationDomainException extends Exception {
    
    private static final long serialVersionUID = 1058246933;
    
    private final ImaginaryQuadraticInteger unfactorizedNumber;
    
    public ImaginaryQuadraticInteger getUnfactorizedNumber() {
        return this.unfactorizedNumber;
    }
    
    // PLACEHOLDER for tryToFactorizeAnyway()
    
    /**
     * This is an exception to be potentially thrown by a prime factorization 
     * function if called upon to operate on a number from a domain other than 
     * those listed in UNIQUE_FACTORIZATON_QUADRATIC_IMAGINARY_RINGS_D.
     * @param message Should probably just be something like 
     * number.getRing().toString() + " is not a unique factorizaton domain." 
     * This message is just passed on to the superclass.
     * @param number The number sent to the prime factorization function, like, 
     * for example, 1 + sqrt(-30).
     */
    public NonUniqueFactorizationDomainException(String message, ImaginaryQuadraticInteger number) {
        super(message);
        this.unfactorizedNumber = number;
    }
    
}
