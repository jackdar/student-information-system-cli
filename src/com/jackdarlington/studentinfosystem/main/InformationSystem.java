/*
 * Created by Jack Darlington | 2023
 */

package com.jackdarlington.studentinfosystem.main;

import com.jackdarlington.studentinfosystem.menu.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/*
 * @author Jack Darlington
 * Student ID: 19082592
 * Date: 08/03/2023
 */

public class InformationSystem {
    
    static HashMap<String, Paper> papers;
    static HashMap<String, Course> courses;
    static HashMap<Integer, Student> students;
    
    static Student student = new Student();
    
    static Scanner sc;
    
    Menu loginMenu;
    Menu mainMenu;
    
    public InformationSystem() {
        papers = Paper.initializePapers();
        courses = Course.initializeCourses();
        students = Student.initialiseStudents();
        
        sc = new Scanner(System.in);
        
        loginMenu = new Menu("Student Information System", 
            new Option("Log In") {
                @Override
                public boolean operation() {
                    System.out.println("Please enter a Student ID to log in: ");
                    String input = sc.nextLine().trim();
                    try {
                        student = students.get(Integer.valueOf(input));
                        System.out.print("Do you want to log in as " + student.userDetails.get(Field.FIRST_NAME) + 
                                " " + student.userDetails.get(Field.LAST_NAME) + " (" + student.studentID + ")? (y/n) ");
                        input = sc.nextLine().trim();
                        if (input.equalsIgnoreCase("y")) {
                            System.out.println("Logging in...");
                            mainMenu.show();
                            return false;
                        }
                    } catch (NumberFormatException e) {
                        if (input.equalsIgnoreCase("q")) {
                            return false;
                        } else {
                            System.out.println("Incorrect Input");
                        }
                    } catch (NullPointerException e) {
                        System.out.println("That is not a valid Student ID!");
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ex) {
                            System.out.println("Interrupted exception caught!");
                        }
                    }
                    return true;
                }
            },
            new Option("Create New Record", "Creating new Student record (" + student.studentID + "), type info or leave blank for none.") {
                @Override
                public boolean operation() {
                    boolean login = Student.editStudent(sc, student);
                    if (login) {
                        mainMenu.show();
                    }
                    return false;
                }
            }
        );

        mainMenu = new Menu("Student Information System", student.toString(),
            new Option("Retrieve Record") {
                @Override
                public boolean operation() {
                    student.printStudentInfo();
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {}
                    System.out.print("\nPress enter to continue... ");
                    String input = sc.nextLine();
                    return false;
                }
            },
            new Option("Edit Record", "Editing record " + student.userDetails.get(Field.FIRST_NAME) + " " + 
                    student.userDetails.get(Field.LAST_NAME) + " (" + student.studentID + "), type info or leave blank for none.") {
                @Override
                public boolean operation() {
                    Student.editStudent(sc, student);
                    return false;
                }
            },
            new Option("Save Record") {
                @Override
                public boolean operation() {
                    Student.writeStudents(students);
                    return false;
                }
            },
            new Option(
                new Menu("Enrolments", student.printStudentEnrolmentInfo(),
                    new Option(student.studentCourse == null ? "Enrol In Course" : "Add New Papers") {
                        @Override
                        public boolean operation() {
                            if (student.studentCourse == null) {
                                student.studentCourse = Student.enrolStudentInCourse(courses, student, sc);
                            } else if (student.studentCourse != null) {
                                Student.enrolStudentInPapers(papers, student, sc);
                            }
                            return false;
                        }
                    }
                )
            ),
            new Option("Grades") {
                @Override
                public boolean operation() {
                    // TODO: Show student grades
                    return false;
                }
            }
        );
    }
    
    public static void main(String[] args) {
        new InformationSystem().loginMenu.show();
        
        System.out.println("Goodbye!");
    }
    
}
