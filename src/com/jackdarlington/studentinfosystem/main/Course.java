/*
 * Created by Jack Darlington | 2023
 */

package com.jackdarlington.studentinfosystem.main;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

/*
 * @author Jack Darlington
 * Student ID: 19082592
 * Date: 08/03/2023
 */

public class Course {
    
    String courseCode;
    String courseName;
    ArrayList<Paper> includedPapers;
    
    public Course(String courseCode, String courseName) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.includedPapers = new ArrayList<Paper>();
    }
    
    public static HashMap<String,Course> initialiseCourses() {
        HashMap<String,Course> courses = new HashMap<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("res/courses.txt")));
            String line;
            try {
                while((line = br.readLine()) != null) {
                    StringTokenizer st = new StringTokenizer(line, ",", false);
                    String courseCode = st.nextToken();
                    Course newCourse = new Course(courseCode, st.nextToken());
                    
                    try {
                        BufferedReader brPapers = new BufferedReader(new FileReader(new File("res/papersincourses.txt")));
                        String linePapers;
                        while((linePapers = brPapers.readLine()) != null) {
                            StringTokenizer stPapers = new StringTokenizer(linePapers, ",", false);
                            String course = stPapers.nextToken();
                            if(course.equals(courseCode)) {
                                newCourse.includedPapers.add((Paper)InformationSystem.papers.get(stPapers.nextToken()));
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    
                    courses.put(courseCode, newCourse);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
        return courses;
    }
    
}
