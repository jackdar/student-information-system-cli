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
    
    static Student selectedStudent;
    
    static Scanner sc;
    
    Menu loginMenu;
    Menu mainMenu;
    
    public InformationSystem() {
        
        // Initialise all data HashMaps
        papers = Paper.initialisePapers();
        courses = Course.initialiseCourses();
        students = Student.initialiseStudents();
        
        sc = new Scanner(System.in);
        
        // Menu Logic
        loginMenu = new Menu("Student Information System", "Quit", 
            new Option("Log In") {
                @Override
                public boolean operation() {
                    System.out.print("Please enter a Student ID to log in (q to return): ");
                    String input = sc.nextLine().trim();
                    try {
                        selectedStudent = students.get(Integer.valueOf(input));
                        System.out.print("Do you want to log in as " + selectedStudent.userDetails.get(Field.FIRST_NAME) + 
                                " " + selectedStudent.userDetails.get(Field.LAST_NAME) + " (" + selectedStudent.studentID + ")? (y/n) ");
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
            new Option("Create New Record", () -> System.out.println("Creating new Student record (" + Student.NEXT_ID_NUMBER + "), type info or leave blank for none.")) {
                @Override
                public boolean operation() {
                    selectedStudent = Student.editStudent(sc, null);
                    if (selectedStudent != null) {
                        mainMenu.show();
                    }
                    return false;
                }
            }
        );
        
        // Create nested menus enrolmentMenu and gradeMenu
        Menu enrolmentMenu = new Menu("Enrolments", 
            new Option("Enrol In Course") {
                @Override
                public boolean operation() {
                    String input = "";
                    System.out.println("Which course would you like to enrol into?\n");
                    ArrayList<Course> courseList = new ArrayList<>();
                    for (Entry<String, Course> e : courses.entrySet()) {
                        courseList.add(e.getValue());
                    }
                    for (int i = 0; i < courseList.size(); i++) {
                        System.out.println(" (" + (i + 1) + ") " + courseList.get(i).courseCode + " - " + courseList.get(i).courseName);
                    }
                    input = sc.nextLine().trim();
                    selectedStudent.studentCourse = courseList.get(Integer.parseInt(input) - 1);
                    System.out.println("Enrolled into course " + selectedStudent.studentCourse.courseCode + " - " + selectedStudent.studentCourse.courseName);
                    return false;
                }
            },
            new Option("Add New Papers", false) {
                @Override
                public boolean operation() {
                    String input = "";
                    System.out.println("Which paper would you like to enrol into? (q to quit)\n");
                    for (int i = 0; i < selectedStudent.studentCourse.includedPapers.size(); i++) {
                        System.out.println(" (" + (i + 1) + ") " + selectedStudent.studentCourse.includedPapers.get(i).paperCode + " - " + selectedStudent.studentCourse.includedPapers.get(i).paperName);
                    }
                    while (!input.equalsIgnoreCase("q")) {
                        input = sc.nextLine().trim();
                        int selection;
                        try {
                            selection = Integer.parseInt(input);
                            if (selection > 0 && selection <= selectedStudent.studentCourse.includedPapers.size() && input != null) {
                                Paper selectedPaper = selectedStudent.studentCourse.includedPapers.get(selection - 1);
                                selectedStudent.studentPapers.put(selectedPaper, Grade.NOT_COMPLETE);
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
                    selectedStudent.studentCourse = null;
                    selectedStudent.studentPapers = new LinkedHashMap<>();
                    return false;
                }
            }
          
        );
        // Set enrolmentMenu description to print up to date student info and set the correct visible options
        enrolmentMenu.setDescriptionMethod(() -> {
            selectedStudent.printStudentEnrolmentInfo();
            if (selectedStudent.studentCourse != null) {
                enrolmentMenu.menuOptions.get(0).visible = false;
                enrolmentMenu.menuOptions.get(1).visible = true;
                enrolmentMenu.menuOptions.get(2).visible = true;
            } else {
                enrolmentMenu.menuOptions.get(0).visible = true;
                enrolmentMenu.menuOptions.get(1).visible = false;
                enrolmentMenu.menuOptions.get(2).visible = false;
            }            
        });
        
        Menu gradeMenu = new Menu("Grades",
            new Option("Set Grade", false) {
                @Override
                public boolean operation() {
                    String input = "";
                    System.out.println("Which paper would you like to change your grade? (q to quit)\n");
                    ArrayList<Paper> studentEnrolledPapers = new ArrayList<>();
                    for (Entry<Paper, Grade> e : selectedStudent.studentPapers.entrySet()) {
                        studentEnrolledPapers.add(e.getKey());
                    }
                    for (int i = 0; i < studentEnrolledPapers.size(); i++) {
                        System.out.print(" (" + (i + 1) + ") " + studentEnrolledPapers.get(i).paperCode + " - " + studentEnrolledPapers.get(i).paperName);
                        for (int j = 0; j < 40 - studentEnrolledPapers.get(i).paperName.length(); j++) {
                            System.out.print(" ");
                        }
                        System.out.print("GRADE: " + selectedStudent.studentPapers.get(studentEnrolledPapers.get(i)).getLabel() + "\n");
                    }
                    input = sc.nextLine().trim();
                    if (input.equalsIgnoreCase("q")) {
                        return false;
                    } else {
                        int selection;
                        try {
                            selection = Integer.parseInt(input);
                            if (selection > 0 && selection <= studentEnrolledPapers.size() && input != null) {
                                Paper selectedPaper = studentEnrolledPapers.get(selection - 1);
                                System.out.print("Which grade would you like to assign to " + selectedPaper.paperCode + " - " + selectedPaper.paperName + "?\n(NOT_COMPLETE, FAIL, {A, B, C}{,_MINUS,_PLUS} e.g. \"A_MINUS\"): ");
                                input = sc.nextLine().trim();
                                try {
                                selectedStudent.studentPapers.replace(selectedPaper, Grade.valueOf(input));
                                System.out.println(selectedPaper.paperCode + " - " + selectedPaper.paperName + " is now graded " + Grade.valueOf(input).getLabel());
                                } catch (IllegalArgumentException e) {
                                    System.out.println("\nThat is not a valid grade!");
                                }
                                Menu.sleep(500);
                            } else { 
                                System.out.println("\nThat is not a valid paper!");
                                Menu.sleep(500);
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("That is not a valid paper!");
                            Menu.sleep(500);
                        }
                        return true;
                    }
                }
            }
        );
        // Set gradeMenu descriptionMethod depending on the state of studentPapers and show the correct relevant menuOptions
        gradeMenu.setDescriptionMethod(() -> {
            if (selectedStudent.studentPapers.isEmpty()) {
                gradeMenu.menuOptions.get(0).visible = false;
                System.out.println(" No Papers to show!");
            } else {
                gradeMenu.menuOptions.get(0).visible = true;
                for (Entry<Paper, Grade> e : selectedStudent.studentPapers.entrySet()) {
                    System.out.print(" " + e.getKey().paperCode + " - " + e.getKey().paperName);
                    for (int i = 0; i < 40 - e.getKey().paperName.length(); i++) {
                        System.out.print(" ");
                    }
                    System.out.println("GRADE: " + e.getValue().getLabel());
                }
            }
        });
        
        mainMenu = new Menu("Student Information System", () -> System.out.println(selectedStudent.toString()), "Log Out",
            new Option("Retrieve Record") {
                @Override
                public boolean operation() {
                    selectedStudent.printStudentInfo();
                    Menu.anyKeyToContinue();
                    return false;
                }
            },
            new Option("Edit Record", () -> System.out.println(" Editing record " + selectedStudent.userDetails.get(Field.FIRST_NAME) + " " + 
                    selectedStudent.userDetails.get(Field.LAST_NAME) + " (" + selectedStudent.studentID + "), type info or leave blank for none.")) {
                @Override
                public boolean operation() {
                    Student.editStudent(sc, selectedStudent);
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
            new Option(gradeMenu)
        );
    }
    
    // Main method instanciating InformationSystem
    public static void main(String[] args) {
        new InformationSystem().loginMenu.show();
        
        System.out.println("Goodbye!");
    }
    
}
