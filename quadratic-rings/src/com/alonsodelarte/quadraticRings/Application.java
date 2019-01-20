package com.alonsodelarte.quadraticRings;

import com.alonsodelarte.quadraticRings.imaginaryquadraticinteger.RingWindowDisplay;


/**
 * The main entry point for the package. For now, it only starts {@link
 * RingWindowDisplay}. In a later version (no later than 1.0), it will be
 * able to accept command line arguments.
 */
@SuppressWarnings("WeakerAccess")
public class Application {

    private enum LaunchOption {
        DO_NOTHING,
        VERSION,
        LAUNCH_A,
        LAUNCH_B
    }


    public static void main(String args[]) {
        launchApplication(args);
    }

    public static void launchApplication(String... arguments) {
        switch(parseArguments(arguments)) {
            case DO_NOTHING: doNothing(); break;
            case VERSION: version(); break;
            case LAUNCH_A: standardLaunch(arguments[0]); break;
            case LAUNCH_B: alternateLaunch(arguments[0], arguments[1]); break;
        }
    }


    private static LaunchOption parseArguments(String[] arguments) {
        switch(arguments.length) {
            case 0: return LaunchOption.DO_NOTHING;
            case 1: return parseSingleArgument(arguments[0]);

            // If there are more than two parameters, only the first two parameters will be processed
            default: return LaunchOption.LAUNCH_B;
        }
    }

    private static LaunchOption parseSingleArgument(String argument) {
        switch (argument) {
            case "-v":
            case "-vers":
            case "-version":
            case "--version":
            case "v":
            case "vers":
            case "version": return LaunchOption.VERSION;

            default: return LaunchOption.LAUNCH_A;
        }
    }


    public static void doNothing() {
        // RingWindowDisplay.startRingWindowDisplay(RingWindowDisplay.DEFAULT_RING_D);
    }

    public static void version() {
        String versionMessage =
            "Imaginary Quadratic Integer package\n" +
            "Version 0.95\n\u00A9 2018 Alonso del Arte";

        System.out.println(versionMessage);
    }

    public static void standardLaunch(String argument) {
        RingWindowDisplay.startRingWindowDisplay(argument);
    }

    public static void alternateLaunch(String argumentA, String argumentB) {
        RingWindowDisplay.startRingWindowDisplay(argumentA, argumentB);
    }

}
