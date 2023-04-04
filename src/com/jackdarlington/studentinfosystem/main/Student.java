/*
 * Created by Jack Darlington | 2023
 */

package com.jackdarlington.studentinfosystem.main;

import static com.jackdarlington.studentinfosystem.main.Main.menu;
import static com.jackdarlington.studentinfosystem.main.Main.students;
import com.jackdarlington.studentinfosystem.menu.Menu;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Scanner;
import java.util.StringTokenizer;

/*
 * @author Jack Darlington
 * Student ID: 19082592
 * Date: 08/03/2023
 */

public class Student {

    static int NEXT_ID_NUMBER = 1000;
    
    String firstName;
    String lastName;
    
    String personalEmail;
    String phoneNum;
    
    String age;
    
    String streetNum;
    String streetName;
    String suburb;
    String city;
    String postCode;
    
    boolean isCurrentlyAttending;
    boolean isRecievingSchoolEmail;
    
    Course studentCourse;
    HashMap<Paper, Grade> studentPapers;
    
    final int studentID;
    final String studentEmail;
    
    public Student() {
        this.studentID = NEXT_ID_NUMBER++;
        this.studentEmail = this.generateStudentEmail();
        
        this.isRecievingSchoolEmail = true;
    }
    
    public Student(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        
        this.studentID = NEXT_ID_NUMBER++;
        this.studentEmail = this.generateStudentEmail();
        
        this.isRecievingSchoolEmail = true;
    }
    
    public Student(String firstName, String lastName, String courseCode) {
        this.firstName = firstName;
        this.lastName = lastName;
        
        this.studentCourse = Main.courses.get(courseCode);
        this.studentPapers = new HashMap<Paper, Grade>();
        
        this.studentID = NEXT_ID_NUMBER++;
        this.studentEmail = this.generateStudentEmail();
        
        this.isRecievingSchoolEmail = true;
    }
    
    private String generateStudentEmail() {
        Random newRand = new Random(this.studentID);
        String newEmail = "";
        
        newEmail += (char)(newRand.nextInt(25) + 97);
        newEmail += (char)(newRand.nextInt(25) + 97);
        newEmail += (char)(newRand.nextInt(25) + 97);
        newEmail += Integer.toString(newRand.nextInt(10));
        newEmail += Integer.toString(newRand.nextInt(10));
        newEmail += Integer.toString(newRand.nextInt(10));
        newEmail += Integer.toString(newRand.nextInt(10));
        
        newEmail += "@aouniversity.ac.nz";
        
        return newEmail;
    }
    
    public String printStudentBasicInfo() {
        return " " + this.firstName + " " + this.lastName + "\n Student ID: " + 
                this.studentID + "\n Student Email: " + this.studentEmail + "\n";
    }
    
    public String printStudentInfo() {
        return  "\nName:            " + this.firstName + " " + this.lastName +
                "\nStudent ID:      " + this.studentID +
                "\nStudent Email:   " + this.studentEmail +
                "\nAge:             " + this.age +
                "\nPersonal Email:  " + this.personalEmail +
                "\nPhone Number:    " + this.phoneNum +
                "\nAddress:         " + this.streetNum + " " + this.streetName +
                "\n                 " + this.suburb +
                "\n                 " + this.city + "    " + this.postCode +
                "\nCurrently attending?     " + this.isCurrentlyAttending +
                "\nRecieving school emails? " + this.isRecievingSchoolEmail + "\n";
    }
    
    public String printStudentEnrolmentInfo() {
        String studentEnrolments = this.studentCourse == null ? "Student has no current enrolments!" : this.studentCourse.courseCode + " - " + this.studentCourse.courseName;
        String studentPapers = "";
        if (!(this.studentPapers == null)) {
            for (Entry e : this.studentPapers.entrySet()) {
                studentPapers += "\n  - " + this.studentPapers.get(e.getKey()).name();
            }
        }
        return " " + this.firstName + " " + this.lastName + "\n " + studentEnrolments + studentPapers + "\n";
    }
    
    public static HashMap<Integer, Student> initializeStudents() {
        HashMap<Integer, Student> students = new HashMap<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("res/students.txt")));
            String line;
            try {
                while((line = br.readLine()) != null) {
                    StringTokenizer st = new StringTokenizer(line, ",", false);
                    int studentID = Integer.parseInt(st.nextToken());
                    Student newStudent = new Student();
                    
                    newStudent.firstName = st.nextToken();
                    newStudent.lastName = st.nextToken();
                    newStudent.age = st.nextToken();
                    st.nextToken(); // throws away final studentEmail
                    newStudent.personalEmail = st.nextToken();
                    newStudent.phoneNum = st.nextToken();
                    newStudent.streetNum = st.nextToken();
                    newStudent.streetName = st.nextToken();
                    newStudent.suburb = st.nextToken();
                    newStudent.city = st.nextToken();
                    newStudent.postCode = st.nextToken();
                    newStudent.isCurrentlyAttending = Boolean.parseBoolean(st.nextToken());
                    newStudent.isRecievingSchoolEmail = Boolean.parseBoolean(st.nextToken());
                    
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
    
    public static Student selectStudent(HashMap<Integer, Student> students, Scanner sc) {
        Student student = null;
        String input = "";
        boolean edit = true;
        try {
            while (edit == true) {
                Menu.clearConsole();
                System.out.println(menu.get(1).printTitle());
                System.out.println("Please enter a Student ID to log in: ");
                input = sc.nextLine();
                try {
                    student = students.get(Integer.valueOf(input));
                    System.out.print("Do you want to log in as " + student.firstName + " " + student.lastName + " (" + student.studentID + ")? (y/n) ");
                    input = sc.nextLine().trim();
                    if (input.equalsIgnoreCase("y")) {
                        System.out.println("Logging in...");
                        edit = false;
                        return student;
                    }
                } catch (NumberFormatException e) {
                    if (input.equalsIgnoreCase("q")) {
                        System.out.println("Going back!");
                        edit = false;
                    } else {
                        System.out.println("Incorrect Input");
                    }
                } catch (NullPointerException e) {
                    System.out.println("That is not a valid Student ID!");
                    try {
                        Thread.sleep(1000);
                        
                    } catch (InterruptedException ex) {}
                }
            }
        } catch (InputMismatchException e) {
            System.out.println("Incorrect Input!");
        }
        return null;
    }
    
    public static void editStudent(Scanner sc, Student student, Menu menu) {
        String input = "";
        boolean edit = true;
        try {
            while (edit == true) {
                Menu.clearConsole();
                System.out.println(menu.printTitle());
                String menuPrefixText = menu.equals(Main.menu.get(2)) ? "Creating new Student record" : "Editing record " +
                        student.firstName + " " + student.lastName + " (" + student.studentID + ")";
                System.out.println(menuPrefixText + ", type info or leave blank for none.\n");
                
                System.out.print(" First Name: ");
                if ((input = sc.nextLine()).trim().equals("")) {
                    student.firstName = "null";
                } else {
                    student.firstName = input.substring(0, 1).toUpperCase() + input.substring(1);
                }
                
                System.out.print(" Last Name: ");
                if ((input = sc.nextLine().trim()).equals("")) {
                    student.firstName = "null";
                } else {
                    student.lastName = input.substring(0, 1).toUpperCase() + input.substring(1);
                }
                
                System.out.print(" Personal Email: ");
                if ((input = sc.nextLine().trim()).equals("")) {
                    student.personalEmail = "null";
                } else {
                    student.personalEmail = input;
                }

                System.out.print(" Phone Number: ");
                if ((input = sc.nextLine().trim()).equals("")) {
                    student.phoneNum = "null";
                } else {
                    student.phoneNum = input;
                }

                System.out.print(" Age: ");
                if ((input = sc.nextLine().trim()).equals("")) {
                    student.age = "null";
                } else {
                    student.age = input;
                }

                System.out.print(" Street Address: ");
                if ((input = sc.nextLine().trim()).equals("")) {
                    student.streetNum = "null";
                    student.streetName = "null";
                } else {
                    StringTokenizer st = new StringTokenizer(input, " ", false);
                    student.streetNum = st.nextToken();
                    student.streetName = "";
                    while (st.hasMoreTokens()) {
                        String token = st.nextToken();
                        token = token.substring(0, 1).toUpperCase() + token.substring(1) + " ";
                        student.streetName += token;
                    }
                    student.streetName = student.streetName.trim();
                }

                System.out.print(" Suburb: ");
                if ((input = sc.nextLine().trim()).equals("")) {
                    student.suburb = "null";
                } else {
                    student.suburb = input.substring(0, 1).toUpperCase() + input.substring(1);;
                }

                System.out.print(" City: ");
                if ((input = sc.nextLine().trim()).equals("")) {
                    student.city = "null";
                } else {
                    student.city = input.substring(0, 1).toUpperCase() + input.substring(1);;
                }
                
                System.out.print(" Postcode: ");
                if ((input = sc.nextLine().trim()).equals("")) {
                    student.postCode = "null";
                } else {
                    student.postCode = input;
                }
                
                System.out.print(" Currently attending? (y/n): ");
                if ((input = sc.nextLine().trim()).equals("")) {
                    student.isCurrentlyAttending = false;
                } else {
                    student.isCurrentlyAttending = input.equalsIgnoreCase("y");
                }

                System.out.print(" Recieving school emails? (y/n): ");
                if ((input = sc.nextLine().trim()).equals("")) {
                    student.isRecievingSchoolEmail = false;
                } else {
                    student.isRecievingSchoolEmail = input.equalsIgnoreCase("y");
                }

                System.out.println(student.printStudentInfo());

                System.out.println("Continue without editing? (y/n)");

                input = sc.nextLine();
                if (input.equalsIgnoreCase("y")) {
                    students.put(student.studentID, student);
                    edit = false;
                } else if (input.equalsIgnoreCase("n")) {
                    edit = true;
                } else {
                    System.out.println("Unexpected input! Continuing without editing.");
                    edit = false;
                }
            }
        } catch (InputMismatchException e) {
            System.out.println("Input mismatch!");
        }
    }
    
    public static void writeStudents(HashMap<Integer, Student> students) {
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new File("res/students.txt"));
            for (Map.Entry e : students.entrySet()) {
                pw.println(e.getKey() + "," + students.get(e.getKey()).firstName + "," + students.get(e.getKey()).lastName + "," +
                    students.get(e.getKey()).age + "," + students.get(e.getKey()).studentEmail + "," + students.get(e.getKey()).personalEmail + "," +
                    students.get(e.getKey()).phoneNum + "," + students.get(e.getKey()).streetNum + "," + students.get(e.getKey()).streetName + "," +
                    students.get(e.getKey()).suburb + "," + students.get(e.getKey()).city + "," + students.get( e.getKey()).postCode + "," +
                    students.get(e.getKey()).isCurrentlyAttending +"," + students.get(e.getKey()).isRecievingSchoolEmail);
            }
        } catch (FileNotFoundException e) {}
        pw.close();
    }
    
}
