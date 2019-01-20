package com.alonsodelarte.quadraticRings.helpers;

import static com.alonsodelarte.quadraticRings.constants.ImaginaryQuadraticInteger.DEFAULT_RING_D;
import static com.alonsodelarte.quadraticRings.constants.ImaginaryQuadraticInteger.MINIMUM_RING_D;
import static com.alonsodelarte.quadraticRings.imaginaryquadraticinteger.NumberTheoreticFunctionsCalculator.isSquareFree;
import static java.lang.String.format;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Sanitizers {

    private static final Logger log = LoggerFactory.getLogger(Sanitizers.class);

    private Sanitizers() {

    }

    public static int guaranteeNegative(int value) {
        int sanitizedValue = value;

        if (value > 0) {
            sanitizedValue = -1 * value;
        }

        if(sanitizedValue != value) {
            log.info(
                "{} is not negative; substituting {}",
                value, sanitizedValue
            );
        }

        return sanitizedValue;
    }

    public static int findSquareFreeValue(int value) {
        int squareFreeValue = value;

        while (!isSquareFree(squareFreeValue)) {
            squareFreeValue--;
        }

        if(squareFreeValue != value) {
            log.info(
                "{} is not square-free; substituting {}",
                value, squareFreeValue
            );
        }

        return squareFreeValue;
    }

    public static int guaranteeGreaterThanMinimum(int value) {
        int sanitizedValue = value;

        if (sanitizedValue < MINIMUM_RING_D) {
            sanitizedValue = DEFAULT_RING_D;
        }

        if(sanitizedValue != value) {
            log.info(
                "{} is less than {}, which is the minimum for this Ring Viewer program.; " +
                "Substituting {}.",
                value, MINIMUM_RING_D,
                DEFAULT_RING_D
            );
        }

        return sanitizedValue;
    }

}
