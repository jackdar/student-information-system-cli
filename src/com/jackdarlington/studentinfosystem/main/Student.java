/*
 * Created by Jack Darlington | 2023
 */

package com.jackdarlington.studentinfosystem.main;

import static com.jackdarlington.studentinfosystem.main.Main.menu;
import com.jackdarlington.studentinfosystem.menu.Menu;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
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
    
    int age;
    
    int streetNum;
    String streetName;
    String suburb;
    String postCode;
    String city;
    
    boolean isCurrentlyAttending;
    boolean isRecievingSchoolEmail;
    
    Course course;
    ArrayList<Paper> papers;
    
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
        
        this.course = Main.courses.get(courseCode);
        this.papers = new ArrayList<>();
        
        this.studentID = NEXT_ID_NUMBER++;
        this.studentEmail = this.generateStudentEmail();
        
        this.isRecievingSchoolEmail = true;
    }
    
    private String generateStudentEmail() {
        Random newRand = new Random(this.studentID);
        String newEmail = "";
        
        newEmail += (char)(newRand.nextInt(97, 122));
        newEmail += (char)(newRand.nextInt(97, 122));
        newEmail += (char)(newRand.nextInt(97, 122));
        newEmail += Integer.toString(newRand.nextInt(0, 9));
        newEmail += Integer.toString(newRand.nextInt(0, 9));
        newEmail += Integer.toString(newRand.nextInt(0, 9));
        newEmail += Integer.toString(newRand.nextInt(0, 9));
        
        newEmail += "@aouniversity.ac.nz";
        
        return newEmail;
    }
    
    public String printStudentBasicInfo() {
        return " " + this.firstName + " " + this.lastName + "\n Student ID: " + 
                this.studentID + "\n Student Email: " + this.studentEmail + "\n";
    }
    
    public String printStudentInfo() {
        return  "\nName:            " + this.firstName + " " + this.lastName +
                "\nAge:             " + this.age +
                "\nStudent ID:      " + this.studentID +
                "\nStudent Email:   " + this.studentEmail +
                "\nPersonal Email:  " + this.personalEmail +
                "\nPhone Number:    " + this.phoneNum +
                "\nAddress:         " + this.streetNum + " " + this.streetName +
                "\n                 " + this.suburb +
                "\n                 " + this.city + "    " + this.postCode +
                "\nCurrently attending?     " + this.isCurrentlyAttending +
                "\nRecieving school emails? " + this.isRecievingSchoolEmail + "\n";
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
                    student = students.get(Integer.parseInt(input));
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
                input = sc.nextLine().trim();
                student.firstName = input.substring(0, 1).toUpperCase() + input.substring(1);

                System.out.print(" Last Name: ");
                input = sc.nextLine().trim();
                student.lastName = input.substring(0, 1).toUpperCase() + input.substring(1);

                System.out.print(" Personal Email: ");
                student.personalEmail = sc.nextLine().trim();

                System.out.print(" Phone Number: ");
                student.phoneNum = sc.nextLine().trim();

                System.out.print(" Age: ");
                input = sc.nextLine().trim();
                if (input.equals("")) {
                    student.age = 0;
                } else {
                    student.age = Integer.parseInt(input);
                }

                System.out.print(" Street Address: ");
                input = sc.nextLine();
                StringTokenizer st = new StringTokenizer(input, " ", false);
                student.streetNum = Integer.parseInt(st.nextToken());
                student.streetName = "";
                while (st.hasMoreTokens()) {
                    student.streetName += st.nextToken() + " ";
                }

                System.out.print(" Suburb: ");
                student.suburb = sc.nextLine().trim();

                System.out.print(" Postcode: ");
                student.postCode = sc.nextLine().trim();

                System.out.print(" City: ");
                student.city = sc.nextLine().trim();

                System.out.print(" Currently attending? (y/n): ");
                student.isCurrentlyAttending = (sc.nextLine().trim()).equalsIgnoreCase("y") ? true : false;

                System.out.print(" Recieving school emails? (y/n): ");
                student.isRecievingSchoolEmail = (sc.nextLine().trim()).equalsIgnoreCase("y") ? true : false;

                System.out.println(student.printStudentInfo());

                System.out.println("Continue without editing? (y/n)");

                input = sc.nextLine();
                if (input.equalsIgnoreCase("y")) {
                    edit = false;
                } else if (input.equalsIgnoreCase("n")) {
                    Menu.clearConsole();
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
    
}
