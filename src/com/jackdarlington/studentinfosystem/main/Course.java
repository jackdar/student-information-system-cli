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
        this.includedPapers = new ArrayList<>();
    }
    
    // Initialise Courses using courses.txt and map each relevant Paper to the correct Course using papersincourses.txt
    public static HashMap<String, Course> initialiseCourses() {
        HashMap<String, Course> courses = new HashMap<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("res/courses.txt")));
            String line;
            try {
                // Read in each line and use a StringTokenizer to split into tokens
                while((line = br.readLine()) != null) {
                    StringTokenizer st = new StringTokenizer(line, ",", false);
                    String courseCode = st.nextToken();
                    Course newCourse = new Course(courseCode, st.nextToken());
                    
                    try {
                        // Read in each line of papers in courses and if the courseCode matches the current line being read add that paper to current course
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
                        System.out.println("Failed to read Papers in Courses file!");
                    }
                    
                    courses.put(courseCode, newCourse);
                }
            } catch (IOException e) {
                System.out.println("Failed to read Courses file!");
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        }
        
        return courses;
    }
    
}