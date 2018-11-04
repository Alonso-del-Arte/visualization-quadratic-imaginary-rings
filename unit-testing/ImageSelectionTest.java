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

package clipboardops;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests of the ImageSelection class.
 * @author Alonso del Arte
 */
public class ImageSelectionTest {
    
    private ImageSelection imgSel;
    
    public ImageSelectionTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        // Check contents of clipboard
        String initClipMsg = "This message was placed by ImageSelectionTest.setUpClass()";
        StringSelection strSel = new StringSelection(initClipMsg);
        // Place strSel on clipboard
        // Create image
        // Put image in imgSel
        // Put image on the clipboard
    }
    
    @AfterClass
    public static void tearDownClass() {
        // Check contents of clipboard
    }
    
    @Before
    public void setUp() {
        // Put image in imgSel
        // Put image on the clipboard
    }
    
    @After
    public void tearDown() {
        // Report current clipboard data flavor
    }

    /**
     * Test of getTransferDataFlavors method, of class ImageSelection.
     */
    @Test
    public void testGetTransferDataFlavors() {
        System.out.println("getTransferDataFlavors");
        DataFlavor[] expResult = {DataFlavor.imageFlavor};
        DataFlavor[] result = imgSel.getTransferDataFlavors();
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of isDataFlavorSupported method, of class ImageSelection.
     */
    @Test
    public void testIsDataFlavorSupported() {
        System.out.println("isDataFlavorSupported");
        assertTrue(imgSel.isDataFlavorSupported(DataFlavor.imageFlavor));
        assertFalse(imgSel.isDataFlavorSupported(DataFlavor.allHtmlFlavor));
        assertFalse(imgSel.isDataFlavorSupported(DataFlavor.fragmentHtmlFlavor));
        assertFalse(imgSel.isDataFlavorSupported(DataFlavor.javaFileListFlavor));
        assertFalse(imgSel.isDataFlavorSupported(DataFlavor.selectionHtmlFlavor));
        assertFalse(imgSel.isDataFlavorSupported(DataFlavor.stringFlavor));
    }

    /**
     * Test of getTransferData method, of class ImageSelection.
     * @throws java.lang.Exception
     */
    @Test
    public void testGetTransferData() throws Exception {
        System.out.println("getTransferData");
        fail("Haven't written test yet.");
    }

    /**
     * Test of lostOwnership method, of class ImageSelection.
     */
    @Ignore
    @Test
    public void testLostOwnership() {
        System.out.println("lostOwnership");
        fail("Haven't written test yet.");
    }
    
}
