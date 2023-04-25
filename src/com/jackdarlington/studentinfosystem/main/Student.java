/*
 * Created by Jack Darlington | 2023
 */

package com.jackdarlington.studentinfosystem.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Scanner;
import java.util.StringTokenizer;

/*
 * @author Jack Darlington
 * Student ID: 19082592
 * Date: 08/03/2023
 */

enum Field {
    
    FIRST_NAME  ("First Name"),
    LAST_NAME   ("Last Name"),
    EMAIL       ("Personal Email"),
    PHONE_NUM   ("Phone Number"),
    AGE         ("Age"),
    STREET_NUM  ("Street Number"),
    STREET_NAME ("Street Name"),
    SUBURB      ("Suburb"),
    CITY        ("City"),
    POST_CODE   ("Post Code");
    
    public final String label;
    
    private Field(String label) {
        this.label = label;
    }
    
}

public class Student {

    static int NEXT_ID_NUMBER = 1000;
    
    HashMap<Field, String> userDetails;
    
    boolean isCurrentlyAttending;
    boolean isRecievingSchoolEmail;
    
    Course studentCourse;
    HashMap<Paper, Grade> studentPapers;
    
    final int studentID;
    final String studentEmail;
    
    public Student(int id) {
        this.studentID = id;
        NEXT_ID_NUMBER = id++;
        
        this.studentEmail = this.generateStudentEmail();
        
        this.initialiseUserDetailsMap();
    }
    
    public Student() {
        this.studentID = NEXT_ID_NUMBER++;
        this.studentEmail = this.generateStudentEmail();
        
        this.initialiseUserDetailsMap();
        
        this.isRecievingSchoolEmail = true;
    }
    
    public Student(String firstName, String lastName) {
        this.studentID = NEXT_ID_NUMBER++;
        this.studentEmail = this.generateStudentEmail();
        
        this.initialiseUserDetailsMap();
        
        this.userDetails.replace(Field.FIRST_NAME, firstName);
        this.userDetails.replace(Field.LAST_NAME, lastName);
        
        this.isRecievingSchoolEmail = true;
    }
    
    public Student(String firstName, String lastName, String courseCode) {
        this.studentID = NEXT_ID_NUMBER++;
        this.studentEmail = this.generateStudentEmail();
        
        this.initialiseUserDetailsMap();
        
        this.userDetails.replace(Field.FIRST_NAME, firstName);
        this.userDetails.replace(Field.LAST_NAME, lastName);
        
        this.studentCourse = InformationSystem.courses.get(courseCode);
        this.studentPapers = new HashMap<Paper, Grade>();
        
        this.isRecievingSchoolEmail = true;
    }
    
    private String generateStudentEmail() {
        Random newRand = new Random(this.studentID);
        String newEmail = "";
        for (int i = 0; i < 3; i++) {
            newEmail += (char)(newRand.nextInt(25) + 97);
        }
        for (int i = 0; i < 4; i++) {
            newEmail += Integer.toString(newRand.nextInt(10));
        }
        newEmail += "@aouniversity.ac.nz";
        
        return newEmail;
    }
    
    public void printStudentInfo() {
        System.out.println(" Student Name:   " + userDetails.get(Field.FIRST_NAME) + " " + userDetails.get(Field.LAST_NAME));
        System.out.println(" Student ID:     " + studentID);
        System.out.println(" Student Email:  " + studentEmail);
        System.out.println(" Personal Email: " + userDetails.get(Field.EMAIL));
        System.out.println(" Phone Number:   " + userDetails.get(Field.PHONE_NUM));
        System.out.println(" Age:            " + userDetails.get(Field.AGE));
        System.out.println(" Address:        " + userDetails.get(Field.STREET_NUM) + " " + userDetails.get(Field.STREET_NAME));
        System.out.println("                 " + userDetails.get(Field.SUBURB));
        System.out.println("                 " + userDetails.get(Field.CITY) + " " + userDetails.get(Field.POST_CODE));
        System.out.println(" Currently attending?     " + isCurrentlyAttending);
        System.out.println(" Recieving school emails? " + isRecievingSchoolEmail);
    }
    
    public String printStudentEnrolmentInfo() {
        String studentEnrolments = this.studentCourse == null ? "Student has no current enrolments!" : this.studentCourse.courseCode + " - " + this.studentCourse.courseName;
        String studentPapers = "";
        if (this.studentPapers != null) {
            for (Entry e : this.studentPapers.entrySet()) {
                studentPapers += "\n  - " + this.studentPapers.get(e.getKey()).name();
            }
        }
        return " " + userDetails.get(Field.FIRST_NAME) + " " + userDetails.get(Field.LAST_NAME)+ "\n " + studentEnrolments + studentPapers + "\n";
    }
    
    private void initialiseUserDetailsMap() {
        this.userDetails = new HashMap<Field, String>();
        for (Field f : Field.values()) {
            this.userDetails.put(f, "");
        }
    }
    
    public static HashMap<Integer, Student> initialiseStudents() {
        HashMap<Integer, Student> students = new HashMap<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("res/students.txt")));
            String line;
            try {
                while ((line = br.readLine()) != null) {
                    StringTokenizer st = new StringTokenizer(line, ",", false);
                    int studentID = Integer.parseInt(st.nextToken());
                    st.nextToken(); // throws away final studentEmail
                    Student newStudent = new Student(studentID);
                    
                    for (Field f : Field.values()) {
                        newStudent.userDetails.replace(f, st.nextToken());
                    }
                    
                    newStudent.isCurrentlyAttending = Boolean.parseBoolean(st.nextToken());
                    newStudent.isRecievingSchoolEmail = Boolean.parseBoolean(st.nextToken());
                    
                    if (st.hasMoreTokens()) {
                        newStudent.studentCourse = InformationSystem.courses.get(st.nextToken());
                        while (st.hasMoreTokens()) {
                            newStudent.studentPapers.put(InformationSystem.papers.get(st.nextToken()), Grade.valueOf(st.nextToken()));
                        }
                    }
                    
                    students.put(studentID, newStudent);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return students;
    }
    
    public static boolean editStudent(Scanner sc, Student student) {
        String input = "";
        try {
            for (Field f : Field.values()) {
                System.out.print(" " + f.label + ": ");
                input = sc.nextLine().trim();
                if (f.equals(Field.EMAIL)) {
                    student.userDetails.replace(f, input);
                }
                else if (f.equals(Field.STREET_NAME)) {
                    String[] streetName = input.split(" ");
                    input = "";
                    for (int i = 0; i < streetName.length; i++) {
                        input += streetName[i].substring(0, 1).toUpperCase() + streetName[i].substring(1) + " ";
                    }
                    student.userDetails.replace(f, input.trim());
                }
                else if (input.equals("")) {
                    student.userDetails.replace(f, "");
                }
                else if (Character.isDigit(input.charAt(0))) {
                    student.userDetails.replace(f, input);
                }
                else {
                    student.userDetails.replace(f, input.substring(0, 1).toUpperCase() + input.substring(1));
                }
            }
            
            System.out.print(" Currently attending? (y/n): ");
            student.isCurrentlyAttending = sc.nextLine().trim().equalsIgnoreCase("y");
            
            System.out.print(" Recieving school emails? (y/n): ");
            student.isRecievingSchoolEmail = sc.nextLine().trim().equalsIgnoreCase("y");
            
            System.out.println();
            student.printStudentInfo();
            System.out.println();
            
            System.out.print(" Continue without editing? (y/n): ");
            input = sc.nextLine();
            
            if (input.equalsIgnoreCase("y")) {
                InformationSystem.students.put(student.studentID, student);
                InformationSystem.student = InformationSystem.students.get(student.studentID);
                return true;
            } else if (input.equalsIgnoreCase("n")) {
                return false;
            } else {
                System.out.println("Unexpected input! Continuing without editing.");
                return true;
            }
        } catch (InputMismatchException e) {
            System.out.println("Input mismatch!");
        }
        return true;
    }
    
    public static void writeStudents(HashMap<Integer, Student> students) {
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new File("res/students.txt"));
            for (Entry e : students.entrySet()) {
                Student student = students.get(e.getKey());
                pw.print(student.studentID + "," + student.studentEmail);
                for (Entry f : student.userDetails.entrySet()) {
                    pw.print("," + f.getValue());
                }
                pw.print("," + student.isCurrentlyAttending + "," + student.isRecievingSchoolEmail);
                if (student.studentCourse != null) {
                    pw.print("," + student.studentCourse.courseCode);
                }
                if (!student.studentPapers.isEmpty()) {
                    for (Entry f : student.studentPapers.entrySet()) {
                        pw.print("," + e.getKey().toString() + "," + e.getValue().toString());
                    }
                }
            }
        } catch (FileNotFoundException e) {}
        pw.close();
    }
    
    public static Course enrolStudentInCourse(HashMap<String, Course> courses, Student student, Scanner sc) {     
        String input = "";
        System.out.println("Which course should would you like to enrol into?\n");
        int i = 1;
        for (Entry e : courses.entrySet()) {
            Course course = courses.get(e.getKey());
            System.out.println(" (" + i + ") " + course.courseCode + " - " + course.courseName);
            i++;
        }
        input = sc.nextLine().trim();
        return courses.get(Integer.parseInt(input) - 1);
    }
    
    public static void enrolStudentInPapers(HashMap<String, Paper> papers, Student student, Scanner sc) {
        String input = "";
        System.out.println("Which paper would you like to enrol into? (q to quit)\n");
        int i = 1;
        for (Paper p : student.studentCourse.includedPapers) {
            System.out.println(" (" + i + ") " + p.paperCode + " - " + p.paperName);
            i++;
        }
        while (input != "q") {
            input = sc.nextLine().trim();
            if (Integer.parseInt(input) > 0 && Integer.parseInt(input) < student.studentCourse.includedPapers.size() + 1 && input != null) {
                Paper selectedPaper = papers.get(Integer.parseInt(input) - 1);
                student.studentPapers.put(selectedPaper, Grade.NOT_COMPLETE);
                System.out.println("Student now enrolled in paper " + selectedPaper.paperCode + " - " + selectedPaper.paperName);
            } else { 
                System.out.println("That is not a valid paper!");
            }
        }
    }
    
    @Override
    public String toString() {
        return " " + userDetails.get(Field.FIRST_NAME) + " " + userDetails.get(Field.LAST_NAME) + "\n Student ID:    " + studentID + "\n Student Email: " + studentEmail;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Student)) {
            return false;
        }
        Student other = (Student) o;
        return this.studentID == other.studentID;
    }
    
}
