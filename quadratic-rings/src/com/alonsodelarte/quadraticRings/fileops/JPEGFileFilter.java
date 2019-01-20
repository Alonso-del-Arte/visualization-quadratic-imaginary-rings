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
 * FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
 * details.
 *
 * You should have received a copy of the GNU General Public License along with 
 * this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.alonsodelarte.quadraticRings.fileops;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 * File filter for selecting Joint Photographic Experts Group (JPEG) files in a 
 * JFileChooser dialog box.
 * @author Alonso del Arte, based on the tutorial at 
 * <a href="http://www.codejava.net/java-se/swing/add-file-filter-for-jfilechooser-dialog">CodeJava</a>.
 */
public class JPEGFileFilter extends FileFilter {
    
    /**
     * Tells JFileChooser whether it should accept or reject a given file.
     * @param file The file to accept or reject.
     * @return True if the finename has any of these four extensions: ".jpg", 
     * ".jpeg", ".JPG", ".JPEG"; false otherwise.
     */
    @Override
    public boolean accept(File file) {
        if (file.isDirectory()) {
            return true;
        }
        return (file.getName().toLowerCase().endsWith(".jpg") || file.getName().toLowerCase().endsWith(".jpeg"));
    }
    
    /**
     * The description of this filter.
     * @return The String "Joint Photographic Experts Group image files (*.jpg 
     * or *.jpeg)"
     */
    @Override
    public String getDescription() {
        return "Joint Photographic Experts Group image files (*.jpg or *.jpeg)";
    }
        
}
