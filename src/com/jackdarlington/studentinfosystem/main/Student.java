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
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Objects;
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

    static Integer NEXT_ID_NUMBER = 1000;
    
    LinkedHashMap<Field, String> userDetails;
    
    boolean isCurrentlyAttending;
    boolean isRecievingSchoolEmail;
    
    Course studentCourse;
    LinkedHashMap<Paper, Grade> studentPapers;
    
    final Integer studentID;
    final String studentEmail;
    
    public Student() {
        this(null, null, null, null);
    }
    
    public Student(Integer id) {
        this(id, null, null, null);
    }
    
    public Student(String firstName, String lastName) {
        this(null, firstName, lastName, null);
    }
    
    public Student(Integer id, String firstName, String lastName, String courseCode) {
        // If id number is supplied, use it and set the NEXT_ID_NUMBER to id+1, else use the next id number
        if (id != null) {
            this.studentID = id;
            Student.NEXT_ID_NUMBER = ++id;
        } else {
            this.studentID = NEXT_ID_NUMBER++;
        }
        
        // Generate a student school email using the unique student id as a seed
        this.studentEmail = this.generateStudentEmail();
        
        // Create a user details map using the enumerated type Field and a string for the value
        this.initialiseUserDetailsMap();
        
        // If a first and last name have been supplied add into the details map here
        this.userDetails.replace(Field.FIRST_NAME, firstName != null ? firstName : "");
        this.userDetails.replace(Field.LAST_NAME, lastName != null ? lastName : "");
        
        // If provided a courseCode set the studentCourse here and instanciate studentPapers
        this.studentCourse = courseCode != null ? InformationSystem.courses.get(courseCode) : null;
        this.studentPapers = new LinkedHashMap<>();
        
        this.isRecievingSchoolEmail = true;
    }
    
    // Generate a random student email in the format {a-z}{a-z}{a-z}{0-9}{0-9}{0-9}{0-9}@aouniversity.co.nz
    // using the studentID as a seed
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
    
    // Print a display of a student's record information
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
    
    // Print a display of a student's enrolment information
    public void printStudentEnrolmentInfo() {
        System.out.println(" " + userDetails.get(Field.FIRST_NAME) + " " + userDetails.get(Field.LAST_NAME));
        System.out.print(this.studentCourse == null ? " Student has no current enrolments!" : " " + this.studentCourse.courseCode + " - " + this.studentCourse.courseName);
        if (this.studentPapers != null) {
            System.out.println();
            for (Entry<Paper, Grade> e : this.studentPapers.entrySet()) {
                System.out.println(" " + e.getKey().paperCode + " - " + e.getKey().paperName);
            }
        }
    }
    
    // Initialise a HashMap with all the values of the Field enum as each key
    private void initialiseUserDetailsMap() {
        this.userDetails = new LinkedHashMap<>();
        for (Field f : Field.values()) {
            this.userDetails.put(f, "");
        }
    }
    
    // Initialise Students using students.txt and mapping each correct field into a students variables and details HashMap
    public static LinkedHashMap<Integer, Student> initialiseStudents() {
        LinkedHashMap<Integer, Student> students = new LinkedHashMap<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("res/students.txt")));
            String line;
            try {
                // Read in each line and use a StringTokenizer to split into tokens
                while ((line = br.readLine()) != null) {
                    StringTokenizer st = new StringTokenizer(line, ",", false);
                    int studentID = Integer.parseInt(st.nextToken());
                    
                    st.nextToken(); // throws away final studentEmail
                    
                    // Create the new student
                    Student newStudent = new Student(studentID);
                    
                    // Set the details map
                    for (Field f : Field.values()) {
                        newStudent.userDetails.replace(f, st.nextToken());
                    }
                    
                    // Sets student boolean variables
                    newStudent.isCurrentlyAttending = Boolean.parseBoolean(st.nextToken());
                    newStudent.isRecievingSchoolEmail = Boolean.parseBoolean(st.nextToken());
                    
                    // If the student has enrolment data stored, read it in here
                    if (st.hasMoreTokens()) {
                        newStudent.studentCourse = InformationSystem.courses.get(st.nextToken());
                        while (st.hasMoreElements()) {
                            newStudent.studentPapers.put(InformationSystem.papers.get(st.nextToken()), Grade.valueOf(st.nextToken()));
                        }
                    }
                    
                    // Put the student in the local scope students HashMap
                    students.put(studentID, newStudent);
                }
            } catch (IOException e) {
                System.out.println("IOException caught!");
            }
        } catch (FileNotFoundException e) {
            System.out.println("File students.txt not found!");
        }
        
        return students;
    }
    
    public static Student editStudent(Scanner sc, Student student) {
        Student editStudent = student != null ? student : new Student();
        String input = "";
        try {
            for (Field f : Field.values()) {
                System.out.print("  " + f.label + ": ");
                input = sc.nextLine().trim();
                if (input.equals("")) {
                    editStudent.userDetails.replace(f, "");
                }
                else if (f.equals(Field.EMAIL)) {
                    editStudent.userDetails.replace(f, input);
                }
                else if (f.equals(Field.STREET_NAME)) {
                    if (input.equals("")) {
                        editStudent.userDetails.replace(f, "");
                    } else {
                        String[] streetName = input.split(" ");
                        input = "";
                        for (String s : streetName) {
                            input += s.substring(0, 1).toUpperCase() + s.substring(1) + " ";
                        }
                        editStudent.userDetails.replace(f, input.trim());
                    }
                }
                else if (Character.isDigit(input.charAt(0))) {
                    editStudent.userDetails.replace(f, input);
                }
                else {
                    editStudent.userDetails.replace(f, input.equals("") ? "" : input.substring(0, 1).toUpperCase() + input.substring(1));
                }
            }
            
            System.out.print("  Currently attending? (y/n): ");
            editStudent.isCurrentlyAttending = sc.nextLine().trim().equalsIgnoreCase("y");
            
            System.out.print("  Recieving school emails? (y/n): ");
            editStudent.isRecievingSchoolEmail = sc.nextLine().trim().equalsIgnoreCase("y");
            
            System.out.println();
            editStudent.printStudentInfo();
            System.out.println();
            
            System.out.print("  Continue without editing? (y/n): ");
            input = sc.nextLine();
            
            if (input.equalsIgnoreCase("y")) {
                InformationSystem.students.put(editStudent.studentID, editStudent);
                InformationSystem.selectedStudent = InformationSystem.students.get(editStudent.studentID);
                return editStudent;
            } else if (input.equalsIgnoreCase("n")) {
                return null;
            } else {
                System.out.println("Unexpected input! Continuing without editing.");
                return editStudent;
            }
        } catch (InputMismatchException e) {
            System.out.println("Input mismatch!");
        }
        return editStudent;
    }
    
    public static void writeStudents(HashMap<Integer, Student> students) {
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new File("res/students.txt"));
            for (Entry<Integer, Student> e : students.entrySet()) {
                Student student = e.getValue();
                pw.print(student.studentID + "," + student.studentEmail);
                for (Entry<Field, String> f : student.userDetails.entrySet()) {
                    pw.print("," + f.getValue());
                }
                pw.print("," + student.isCurrentlyAttending + "," + student.isRecievingSchoolEmail);
                if (student.studentCourse != null) {
                    pw.print("," + student.studentCourse.courseCode);
                }
                if (student.studentPapers != null) {
                    if (!student.studentPapers.isEmpty()) {
                        for (Entry<Paper, Grade> f : student.studentPapers.entrySet()) {
                            pw.print("," + f.getKey().paperCode + "," + f.getValue().toString());
                        }
                    }
                }
                pw.print("\n");
            }
        } catch (FileNotFoundException e) {}
        if (pw != null) {
            pw.close();
        }
    }
    
    @Override
    public String toString() {
        return " Student Name:  " + userDetails.get(Field.FIRST_NAME) + " " + userDetails.get(Field.LAST_NAME) + "\n Student ID:    " + studentID + "\n Student Email: " + studentEmail;
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
        return Objects.equals(this.studentID, other.studentID);
    }
    
}