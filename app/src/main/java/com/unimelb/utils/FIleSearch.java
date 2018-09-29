package com.unimelb.utils;

import java.io.File;
import java.util.ArrayList;

/**
 * This class is used to find all the files/directories in certain directory
 */
public class FIleSearch {

    /**
     * Search in a specfic directory and return all the directories inside
     * @param directory directory path
     * @return the directory path list
     */
    public static ArrayList<String> getDirectoryPaths(String directory) {
        ArrayList<String> pathArray = new ArrayList<>();
        File file = new File(directory);
        File[] listFiles = file.listFiles();
        for (File subFile: listFiles) {
            if (subFile.isDirectory()) {
                pathArray.add(subFile.getAbsolutePath());
            }
        }

        return pathArray;
    }

    /**
     * Search in a specfic directory and return all the files inside
     * @param directory directory path
     * @return the file path list
     */
    public static ArrayList<String> getFilePaths(String directory) {
        ArrayList<String> pathArray = new ArrayList<>();
        File file = new File(directory);
        File[] listFiles = file.listFiles();
        for (File subFile: listFiles) {
            if (subFile.isFile()) {
                pathArray.add(subFile.getAbsolutePath());
            }
        }

        return pathArray;
    }
}
