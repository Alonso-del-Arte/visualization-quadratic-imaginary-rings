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
package filefilters;

import java.io.File;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Just a straightforward test of the PNGFileFilter for the JFileChooser.
 * @author Alonso del Arte
 */
public class PNGFileFilterTest {
    
    /**
     * Test of accept method, of class PNGFileFilter.
     */
    @Test
    public void testAccept() {
        System.out.println("accept");
        File file = new File("image.png");
        PNGFileFilter filter = new PNGFileFilter();
        String assertionMessage = "PNGFileFilter should accept image.png";
        assertTrue(assertionMessage, filter.accept(file));
        file = new File("document.doc");
        assertionMessage = "PNGFileFilter should reject document.doc";
        assertFalse(assertionMessage, filter.accept(file));
//        File dir = file.getParentFile();
//        assertionMessage = "PNGFileFilter should accept directory " + dir.getAbsolutePath();
//        assertTrue(assertionMessage, filter.accept(file));
    }

    /**
     * Test of getDescription method, of class PNGFileFilter.
     */
    @Test
    public void testGetDescription() {
        System.out.println("getDescription");
        PNGFileFilter filter = new PNGFileFilter();
        String description = filter.getDescription();
        String assertionMessage = "Filter description should include \"Portable Network Graphics\"";
        assertTrue(assertionMessage, description.contains("Portable Network Graphics"));
        assertionMessage = "Filter description should include \"png\" or \"PNG\"";
        assertTrue(assertionMessage, description.contains("png") || description.contains("PNG"));
    }
    
}
