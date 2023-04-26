/*
 * Created by Jack Darlington | 2023
 */

package com.jackdarlington.studentinfosystem.main;

import com.jackdarlington.studentinfosystem.menu.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Scanner;

/*
 * @author Jack Darlington
 * Student ID: 19082592
 * Date: 08/03/2023
 */

public class InformationSystem {
    
    static HashMap<String, Paper> papers;
    static HashMap<String, Course> courses;
    static LinkedHashMap<Integer, Student> students;
    
    static Student student = new Student();
    
    static Scanner sc;
    
    Menu loginMenu;
    Menu mainMenu;
    
    public InformationSystem() {
        papers = Paper.initializePapers();
        courses = Course.initializeCourses();
        students = Student.initialiseStudents();
        
        sc = new Scanner(System.in);
        
        loginMenu = new Menu("Student Information System", "Quit", 
            new Option("Log In") {
                @Override
                public boolean operation() {
                    System.out.print("Please enter a Student ID to log in: ");
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
            new Option("Create New Record", () -> System.out.println("Creating new Student record (" + student.studentID + "), type info or leave blank for none.")) {
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

        Menu enrolmentMenu = new Menu("Enrolments", 
            new Option("Enrol In Course") {
                @Override
                public boolean operation() {
                    Student.enrolStudentInCourse(courses, student, sc);
                    return false;
                }
            },
            new Option("Add New Papers", false) {
                @Override
                public boolean operation() {
                    Student.enrolStudentInPapers(papers, student, sc);
                    return false;
                }
            },
            new Option("Leave Course", false) {
                @Override
                public boolean operation() {
                    student.studentCourse = null;
                    student.studentPapers = new HashMap<>();
                    return false;
                }
            }
          
        );
        enrolmentMenu.setDescriptionMethod(() -> {
            student.printStudentEnrolmentInfo();
            if (student.studentCourse != null) {
                enrolmentMenu.menuOptions.get(0).visible = false;
                enrolmentMenu.menuOptions.get(1).visible = true;
                enrolmentMenu.menuOptions.get(2).visible = true;
            }
        });
        
        mainMenu = new Menu("Student Information System", () -> System.out.println(student.toString()), "Log Out",
            new Option("Retrieve Record") {
                @Override
                public boolean operation() {
                    student.printStudentInfo();
                    Menu.anyKeyToContinue();
                    return false;
                }
            },
            new Option("Edit Record", () -> System.out.println(" Editing record " + student.userDetails.get(Field.FIRST_NAME) + " " + 
                    student.userDetails.get(Field.LAST_NAME) + " (" + student.studentID + "), type info or leave blank for none.")) {
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
                    System.out.println("Student saved to file \"students.txt\".");
                    Menu.anyKeyToContinue();
                    return false;
                }
            },
            new Option(enrolmentMenu),
            new Option("Grades") {
                @Override
                public boolean operation() {
                    if (student.studentPapers.isEmpty()) {
                        System.out.println("No Papers to show!");
                    } else {
                        for (Entry<Paper, Grade> e : student.studentPapers.entrySet()) {
                            System.out.println(" " + e.getKey().paperCode + " - " + e.getKey().paperName + ": " + e.getValue().getLabel());
                        }
                        System.out.println();
                    }
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
