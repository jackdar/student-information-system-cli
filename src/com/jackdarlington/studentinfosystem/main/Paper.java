/*
 * Created by Jack Darlington | 2023
 */

package com.jackdarlington.studentinfosystem.main;

import java.io.*;
import java.util.HashMap;
import java.util.StringTokenizer;

/*
 * @author Jack Darlington
 * Student ID: 19082592
 * Date: 08/03/2023
 */

enum Grade {
    
    NOT_COMPLETE("Not Complete"), 
    FAIL("Fail"), 
    C_MINUS("C-"), 
    C("C"),
    C_PLUS("C+"), 
    B_MINUS("B-"),
    B("B"),
    B_PLUS("B+"),
    A_MINUS("A-"),
    A("A"),
    A_PLUS("A+");
    
    public final String label;
    
    public String getLabel() {
        return this.label;
    }
    
    private Grade(String label) {
        this.label = label;
    }
    
}

public class Paper {

    String paperCode;
    String paperName;

    public String getPaperCode() {
        return paperCode;
    }

    public void setPaperCode(String paperCode) {
        this.paperCode = paperCode;
    }

    public String getPaperName() {
        return paperName;
    }

    public void setPaperName(String paperName) {
        this.paperName = paperName;
    }
    
    public Paper() {
        this("", "");
    }
    
    public Paper(String paperCode, String paperName) {
        this.paperCode = paperCode;
        this.paperName = paperName;
    }
    
    // Initialise Papers using data from papers.txt
    public static HashMap<String, Paper> initialisePapers() {
        HashMap<String, Paper> papers = new HashMap<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("res/papers.txt")));
            String line;
            try {
                // Read in each line and split using StringTokenizer and put into papers HashMap
                while((line = br.readLine()) != null) {
                    StringTokenizer st = new StringTokenizer(line, ",", false);
                    String paperCode = st.nextToken();
                    Paper newPaper = new Paper(paperCode, st.nextToken());
                    papers.put(paperCode, newPaper);
                }
            } catch (IOException e) {
                System.out.println("IOException caught!");
            }
        } catch (FileNotFoundException e) {
            System.out.println("Papers file not found!");
        }
        
        return papers;
    }
    
    @Override
    public String toString() {
        return this.paperCode;
    }

    @Override
    public boolean equals(Object o) {
        return this.paperCode.equals(((Paper) o).paperCode);
    }
    
}
