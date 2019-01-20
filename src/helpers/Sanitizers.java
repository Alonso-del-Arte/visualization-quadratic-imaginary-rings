package helpers;

import static constants.ImaginaryQuadraticInteger.DEFAULT_RING_D;
import static constants.ImaginaryQuadraticInteger.MINIMUM_RING_D;
import static imaginaryquadraticinteger.NumberTheoreticFunctionsCalculator.isSquareFree;
import static java.lang.String.format;

public class Sanitizers {

    private Sanitizers() {

    }

    public static int guaranteeNegative(int value) {
        int sanitizedValue = value;

        if (value > 0) {
            sanitizedValue = -1 * value;
        }

        if(sanitizedValue != value) {
            System.out.print(format(
                "%d is not negative; substituting %d",
                value, sanitizedValue
            ));
        }

        return sanitizedValue;
    }

    public static int findSquareFreeValue(int value) {
        int squareFreeValue = value;

        while (!isSquareFree(squareFreeValue)) {
            squareFreeValue--;
        }

        if(squareFreeValue != value) {
            System.out.println(format(
                "%d is not square-free; substituting %s",
                value, squareFreeValue
            ));
        }

        return squareFreeValue;
    }

    public static int guaranteeGreaterThanMinimum(int value) {
        int sanitizedValue = value;

        if (sanitizedValue < DEFAULT_RING_D) {
            sanitizedValue = DEFAULT_RING_D;
        }

        if(sanitizedValue != value) {
            System.out.print(String.format(
                "%s is less than %s, which is the minimum for this Ring Viewer program.;\n" +
                    "Substituting %s.",
                value, MINIMUM_RING_D,
                DEFAULT_RING_D
            ));
        }

        return sanitizedValue;
    }

}
