package imaginaryquadraticinteger;

import static constants.ImaginaryQuadraticInteger.DEFAULT_RING_D;
import static helpers.Sanitizers.findSquareFreeValue;
import static helpers.Sanitizers.guaranteeGreaterThanMinimum;
import static helpers.Sanitizers.guaranteeNegative;
import static java.lang.Integer.parseInt;

class RingWindowDisplayOption {

    static int parse(String argument) {
        int argumentValue = DEFAULT_RING_D;

        try {
            argumentValue = parseInt(argument);
        } catch(NumberFormatException exception) {
            System.out.println(String.format(
                "%s is formatted badly.\n%s\nSubstituting %s with %s.",
                argumentValue,
                exception.getMessage(),
                argumentValue, DEFAULT_RING_D
            ));
        }

        return argumentValue;
    }

    static int sanitizeValue(int value) {
        int sanitizedValue = value;

        sanitizedValue = guaranteeNegative(sanitizedValue);
        sanitizedValue = findSquareFreeValue(sanitizedValue);
        sanitizedValue = guaranteeGreaterThanMinimum(sanitizedValue);

        return sanitizedValue;
    }

}
