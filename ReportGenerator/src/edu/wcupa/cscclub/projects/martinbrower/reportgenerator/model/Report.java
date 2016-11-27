/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wcupa.cscclub.projects.martinbrower.reportgenerator.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Adrian
 */

/*
* -- UML Diagram (Report) --
* - Sections: ArrayList <Page>
* + Report (page : Page)
* + getSection(): ArrayList<Page>
* + toString(): String
* + save (filename: String) void
* + exportToExcel(fileName: String) void
 */
public class Report {

    private ArrayList<Page> sections;
    private ArrayList page;
    private String fileName;
    File file = new File (fileName);
    
    
    // + Report (page : Page)
    public Report(ArrayList Page) {
        Page = page;
    }

    // + getSection(): ArrayList<Page>
    public ArrayList<Page> getSections() {
        return null;
    }

    // + toString(): String
    @Override
    public String toString() {
        //return "Report{" + page + '}';
        return fileName+".doc";
    }

    // + save (filename: String) void
    public void save(String fileName) throws IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter(file));
        out.write(fileName);
        out.close();
    }

    // + exportToExcel(fileName: String) void
    public void exportToExcel(String fileName) throws IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter(file));
        out.write(fileName+".csv");
        out.close();
    }

}
