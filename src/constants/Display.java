package constants;

import java.awt.Color;

public class Display {

    private Display() {

    }

    /**
     * The default number of pixels per unit interval. The protected variable
     * pixelsPerUnitInterval is initialized to this value.
     */
    public static final int DEFAULT_PIXELS_PER_UNIT_INTERVAL = 40;

    /**
     * The minimum pixels per unit interval. Trying to set pixels per unit
     * interval below this value will cause an exception.
     */
    public static final int MINIMUM_PIXELS_PER_UNIT_INTERVAL = 2;

    /**
     * The minimum pixels per unit interval for which the program will draw
     * grids.
     */
    public static final int MINIMUM_PIXELS_PER_UNIT_INTERVAL_TO_DRAW_GRIDS = 5;

    /**
     * The maximum pixels per unit interval. Even on an 8K display, this value
     * might be much too large. Trying to set pixels per unit interval above
     * this value will cause an exception.
     */
    public static final int MAXIMUM_PIXELS_PER_UNIT_INTERVAL = 6400;

    /**
     * The minimum horizontal pixel dimension for the canvas in which to draw
     * the diagram. This should be small even on moderately obsolete mobile
     * devices.
     */
    public static final int RING_CANVAS_HORIZ_MIN = 100;

    /**
     * The minimum vertical pixel dimension for the canvas in which to draw the
     * diagram. This should be small even on moderately obsolete mobile devices.
     */
    public static final int RING_CANVAS_VERTIC_MIN = 178;

    /**
     * The default horizontal pixel dimension for the canvas in which to draw
     * the diagram. This fills up most of the screen on a 1440 by 900 (16:10
     * aspect ration) display.
     */
    public static final int RING_CANVAS_DEFAULT_HORIZ_MAX = 1280;

    /**
     * The default vertical pixel dimension for the canvas in which to draw the
     * diagram. This fills up most of the screen on a 1440 by 900 (16:10 aspect
     * ratio) display.
     */
    public static final int RING_CANVAS_DEFAULT_VERTIC_MAX = 720;

    /**
     * The default pixel radius for the dots in the diagram.
     */
    public static final int DEFAULT_DOT_RADIUS = 5;

    /**
     * The minimum pixel radius for the dots in the diagram.
     */
    public static final int MINIMUM_DOT_RADIUS = 1;

    /**
     * The maximum pixel radius for the dots in the diagram.
     */
    public static final int MAXIMUM_DOT_RADIUS = 128;

    /**
     * The usual step by which to increment or decrement pixels by unit
     * interval. This step can be increased or decreased, either through the
     * menu or with a keyboard shortcut.
     */
    public static final int DEFAULT_ZOOM_INTERVAL = 5;

    /**
     * The smallest step by which to increment or decrement pixels by unit
     * interval. Once this step is reached, the relevant menu item is disabled
     * and the corresponding keyboard shortcut is ignored.
     */
    public static final int MINIMUM_ZOOM_INTERVAL = 1;

    /**
     * The largest step by which to increment or decrement pixels by unit
     * interval. Once this step is reached, the relevant menu item is disabled
     * and the corresponding keyboard shortcut is ignored.
     */
    public static final int MAXIMUM_ZOOM_INTERVAL = 48;

    /**
     * The default background color when the program starts. In a future
     * version, there will be a dialog accessed through a menu that will enable
     * the user to change this color. For now, the only way to change it is by
     * changing this constant.
     */
    public static final Color DEFAULT_CANVAS_BACKGROUND_COLOR = new Color(2107440); // A dark blue

    /**
     * The default color for the "half-integer" grid. In a future version, there
     * will be a dialog accessed through a menu that will enable the user to
     * change this color. This setting is only relevant when the program is
     * displaying a diagram with "half-integers" at a zoom level sufficient to
     * cause the drawing of grid lines.
     */
    public static final Color DEFAULT_HALF_INTEGER_GRID_COLOR = Color.DARK_GRAY;

    /**
     * The default color for the "full" integer grid. In a future version, there
     * will be a dialog accessed through a menu that will enable the user to
     * change this color. This setting is only relevant when the program is
     * displaying a diagram with at a zoom level sufficient to cause the drawing
     * of grid lines.
     */
    public static final Color DEFAULT_INTEGER_GRID_COLOR = Color.BLACK;

    /**
     * The default color for 0.
     */
    public static final Color DEFAULT_ZERO_COLOR = Color.BLACK;

    /**
     * The default color for units. In <b>Z</b>[<i>i</i>], this would include
     * <i>i</i> and -<i>i</i>. In <b>Z</b>[&omega;], this would include &omega;,
     * 1 + &omega;, -1 - &omega; and -&omega;. In all rings, this includes 1 and
     * -1.
     */
    public static final Color DEFAULT_UNIT_COLOR = Color.WHITE;

    /**
     * The default color for primes believed to be inert. If a prime does split
     * but its prime factors are not in the current diagram view, the program is
     * unaware of them. Though I have yet to think of a single example where it
     * might actually be the case that the program erroneously identifies a
     * prime as inert.
     */
    public static final Color DEFAULT_INERT_PRIME_COLOR = Color.CYAN;

    /**
     * The default color for primes confirmed split. If a prime's splitting
     * factors are in the current diagram view, the program uses this color for
     * the split prime. For example, if the program is displaying a diagram of
     * <b>Z</b>[&omega;] and -7/2 + (&radic;-3)/2 (which has norm 13) is in
     * view, then 13 is confirmed split and colored accordingly if it's in view.
     * Note, however, that, given a prime <i>p</i> &equiv; 2 mod 3, the number
     * -<i>p</i>/2 + (<i>p</i>&radic;-3)/2 is not a splitting factor of
     * <i>p</i>, since it is <i>p</i>&omega; and &omega; is a unit. Therefore
     * <i>p</i> should be colored with the inert prime color.
     */
    public static final Color DEFAULT_SPLIT_PRIME_COLOR = Color.BLUE;

    /**
     * The default color for primes that are ramified. Regardless of the current
     * diagram view, the program checks if gcd(<i>p</i>, <i>d</i>) > 1. So, for
     * example, if the program is displaying a diagram of <b>Z</b>[&radic;-5],
     * then 5 will be shown in this color if it's in view.
     */
    public static final Color DEFAULT_RAMIFIED_PRIME_COLOR = Color.GREEN;

}
