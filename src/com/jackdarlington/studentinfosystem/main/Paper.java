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
    
    NOT_COMPLETE("Not Complete"), PASS("Pass"), FAIL("Fail");
    
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
        this.paperCode = "";
        this.paperName = "";
    }
    
    public Paper(String paperCode, String paperName) {
        this.paperCode = paperCode;
        this.paperName = paperName;
    }
    
    public static HashMap<String,Paper> initializePapers() {
        HashMap<String,Paper> papers = new HashMap<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("res/papers.txt")));
            String line;
            try {
                while((line = br.readLine()) != null) {
                    StringTokenizer st = new StringTokenizer(line, ",", false);
                    String paperCode = st.nextToken();
                    Paper newPaper = new Paper(paperCode, st.nextToken());
                    papers.put(paperCode, newPaper);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
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
