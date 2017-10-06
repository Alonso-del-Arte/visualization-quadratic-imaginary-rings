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
public class CoordinateSystemMismatchException extends IllegalArgumentException {
    
    private static final long serialVersionUID = 1;
    private final String expectedCoordinateSystemExplanation;
    
    /**
     * Just to clarify where the coordinate system expected (0, 0) to be at
     * @return A string saying either "Expected coordinate system has (0, 0) at the center" or "Expected coordinate system has (0, 0) at the top left corner."
     */
    public String getExpectedCoordinateSystemExplanation() {
        return expectedCoordinateSystemExplanation;
    }
    
    /**
     * An exception to be thrown when a function receives coordinates which are outside the expected range.
     * @param message A message to pass on to IllegalArgumentException.
     * @param centeredZeroFlag A boolean specifying whether (0, 0) is at the center of the coordinate system (true) or not (false). expectedCoordinateSystemExplanation is set accordingly.
     */
    public CoordinateSystemMismatchException(String message, boolean centeredZeroFlag) {
        
        super(message);
        if (centeredZeroFlag) {
            expectedCoordinateSystemExplanation = "Expected coordinate system has (0, 0) at the center.";
        } else {
            expectedCoordinateSystemExplanation = "Expected coordinate system has (0, 0) at the top left corner.";
        }
        
    }
    
}
