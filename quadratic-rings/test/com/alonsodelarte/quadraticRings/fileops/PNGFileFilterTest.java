/*
 * Copyright (C) 2018 Alonso del Arte
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
package com.alonsodelarte.quadraticRings.fileops;

import java.io.File;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Just a straightforward test of the PNGFileFilter for the JFileChooser.
 * @author Alonso del Arte
 */
public class PNGFileFilterTest {
    
    private static PNGFileFilter filter;

    @BeforeClass
    public static void setUpClass() {
        filter = new PNGFileFilter();
    }
    
    /**
     * Test of accept method, of class PNGFileFilter. The file filter should 
     * accept PNG files and reject all other files, even if they are graphics 
     * files like JPEG.
     */
    @Test
    public void testAccept() {
        System.out.println("accept");
        File file = new File("image.png");
        String assertionMessage = "PNGFileFilter should accept " + file.getName();
        System.out.println(assertionMessage);
        assertTrue(assertionMessage, filter.accept(file));
        file = new File("image.PNG");
        assertionMessage = "PNGFileFilter should accept " + file.getName();
        System.out.println(assertionMessage);
        assertTrue(assertionMessage, filter.accept(file));
        file = new File("image.jpeg");
        assertionMessage = "PNGFileFilter should reject " + file.getName();
        System.out.println(assertionMessage);
        assertFalse(assertionMessage, filter.accept(file));
        file = new File("document.doc");
        assertionMessage = "PNGFileFilter should reject " + file.getName();
        System.out.println(assertionMessage);
        assertFalse(assertionMessage, filter.accept(file));
        String homeDir = System.getProperty("user.home");
        File dir = new File(homeDir);
        assertionMessage = "PNGFileFilter should accept directory " + homeDir;
        System.out.println(assertionMessage);
        assertTrue(assertionMessage, filter.accept(dir));
    }

    /**
     * Test of getDescription method, of class PNGFileFilter. The description 
     * provided to JFileChooser should include the file extension *.png or 
     *.PNG.
     */
    @Test
    public void testGetDescription() {
        System.out.println("getDescription");
        String description = filter.getDescription();
        System.out.println("PNGFileFilter description is \"" + description + "\"");
        String assertionMessage = "Filter description should include \"Portable Network Graphics\"";
        assertTrue(assertionMessage, description.contains("Portable Network Graphics"));
        assertionMessage = "Filter description should include \"png\" or \"PNG\"";
        assertTrue(assertionMessage, description.toLowerCase().contains("png"));
    }
    
}
