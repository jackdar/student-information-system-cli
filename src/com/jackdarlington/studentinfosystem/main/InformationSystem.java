/*
 * Created by Jack Darlington | 2023
 */

package com.jackdarlington.studentinfosystem.main;

import com.jackdarlington.studentinfosystem.menu.*;
import java.util.ArrayList;
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
    
    static Student student;
    
    static Scanner sc;
    
    Menu loginMenu;
    Menu mainMenu;
    
    public InformationSystem() {
        papers = Paper.initialisePapers();
        courses = Course.initialiseCourses();
        students = Student.initialiseStudents();
        
        student = new Student();
        
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
                    String input = "";
                    System.out.println("Which course should would you like to enrol into?\n");
                    ArrayList<Course> courseList = new ArrayList<>();
                    for (Entry<String, Course> e : courses.entrySet()) {
                        courseList.add(e.getValue());
                    }
                    for (int i = 0; i < courseList.size(); i++) {
                        System.out.println(" (" + (i + 1) + ") " + courseList.get(i).courseCode + " - " + courseList.get(i).courseName);
                    }
                    input = sc.nextLine().trim();
                    student.studentCourse = courseList.get(Integer.parseInt(input) - 1);
                    System.out.println("Enrolled into course " + student.studentCourse.courseCode + " - " + student.studentCourse.courseName);
                    return false;
                }
            },
            new Option("Add New Papers", false) {
                @Override
                public boolean operation() {
                    String input = "";
                    System.out.println("Which paper would you like to enrol into? (q to quit)\n");
                    for (int i = 0; i < student.studentCourse.includedPapers.size(); i++) {
                        System.out.println(" (" + (i + 1) + ") " + student.studentCourse.includedPapers.get(i).paperCode + " - " + student.studentCourse.includedPapers.get(i).paperName);
                    }
                    while (!input.equalsIgnoreCase("q")) {
                        input = sc.nextLine().trim();
                        int selection;
                        try {
                            selection = Integer.parseInt(input);
                            if (selection > 0 && selection <= student.studentCourse.includedPapers.size() && input != null) {
                                Paper selectedPaper = student.studentCourse.includedPapers.get(selection - 1);
                                student.studentPapers.put(selectedPaper, Grade.NOT_COMPLETE);
                                System.out.println("Student now enrolled in paper " + selectedPaper.paperCode + " - " + selectedPaper.paperName);
                            } else { 
                                System.out.println("That is not a valid paper!");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("That is not a valid paper!");
                        }
                    }
                    return false;
                }
            },
            new Option("Leave Course", false) {
                @Override
                public boolean operation() {
                    student.studentCourse = null;
                    student.studentPapers = new LinkedHashMap<>();
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
            } else {
                enrolmentMenu.menuOptions.get(0).visible = true;
                enrolmentMenu.menuOptions.get(1).visible = false;
                enrolmentMenu.menuOptions.get(2).visible = false;
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
                    System.out.println("Student saved to file \"students.txt\".\n");
                    Menu.anyKeyToContinue();
                    return false;
                }
            },
            new Option(enrolmentMenu),
            new Option("Grades") {
                @Override
                public boolean operation() {
                    if (student.studentPapers.isEmpty()) {
                        System.out.println(" No Papers to show!\n");
                    } else {
                        for (Entry<Paper, Grade> e : student.studentPapers.entrySet()) {
                            System.out.print(" " + e.getKey().paperCode + " - " + e.getKey().paperName);
                            for (int i = 0; i < 40 - e.getKey().paperName.length(); i++) {
                                System.out.print(" ");
                            }
                            System.out.println("GRADE: " + e.getValue().getLabel() + "\n");
                        }
                    }
                    Menu.anyKeyToContinue();
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
