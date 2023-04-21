/*
 * Created by Jack Darlington | 2023
 */

package com.jackdarlington.studentinfosystem.main;

import com.jackdarlington.studentinfosystem.menu.Menu;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map.Entry;
import java.util.Scanner;

/*
 * @author Jack Darlington
 * Student ID: 19082592
 * Date: 08/03/2023
 */

public class Main {
    
    static HashMap<String, Paper> papers;
    static HashMap<String, Course> courses;
    static HashMap<Integer, Student> students;
    
    static ArrayList<Menu> menu;
    
    public static void main(String[] args) {
        papers = Paper.initializePapers();
        courses = Course.initializeCourses();
        students = Student.initializeStudents();
        
        menu = new ArrayList<Menu>();
        
        menu.add(new Menu("Student Information System", new String[] {"Log In", "Create New Record", "Quit"}));
        menu.add(new Menu("Log In"));
        menu.add(new Menu("Create New Record"));
        menu.add(new Menu("Student Information System", new String[] {"Retrieve Record", "Edit Record", "Save Record", "Enrolments", "Grades", "Log Out"}));
        menu.add(new Menu("Retrieve Record"));
        menu.add(new Menu("Edit Record"));
        menu.add(new Menu("Save Record"));
        menu.add(new Menu("Enrolments", new String[] {"Back"}));
        menu.add(new Menu("Grades"));
        menu.add(new Menu("Log Out"));
        
        
        students.get(1000).studentCourse = courses.get("BCIS");
        
        boolean quit = false;
        while (quit == false) {
            Scanner sc = new Scanner(System.in);

            Student student = loginMenu(sc, quit);
            if (student == null) {
                quit = true;
                break;
            }
            mainMenu(sc, student);
        }
        System.out.println("Goodbye!");
        
    }
    
    public static Student loginMenu(Scanner sc, boolean quit) {
        Student student = new Student();
        Menu.clearConsole();
        menu.get(0).printMenu();
        String selection = sc.nextLine();
        switch (selection) {
            case "1":
                student = Student.selectStudent(students, sc);
                try {
                    Thread.sleep(1000);
                    Menu.clearConsole();
                } catch (InterruptedException e) {}
                break;
            case "2":
                Student.editStudent(sc, student, menu.get(2));
                try {
                    Thread.sleep(1000);
                    Menu.clearConsole();
                } catch (InterruptedException e) {}
                break;
            case "3":
                student = null;
                quit = true;
                break;
            case "q":
                student = null;
                quit = true;
                break;
            case "Q":
                student = null;
                quit = true;
                break;
        }
        
    return student;
    }
    
    public static void mainMenu(Scanner sc, Student student) {
        boolean menuLoop = true;
        while (menuLoop == true) {
            Menu.clearConsole();
            String selection = "";

            menu.get(3).setMenuDescription(student.printStudentBasicInfo());
            menu.get(3).printMenu();

            try {
                selection = sc.nextLine().trim();
            } catch (InputMismatchException e) {
                System.out.println("Incorrect input!");
            }

            switch (selection) {
                case "1":
                    retrieveRecord(sc, student);
                    break;
                case "2":
                    editRecord(sc, student);
                    break;
                case "3":
                    saveRecord(student);
                    break;
                case "4":
                    enrolments(sc, student);
                    break;
                case "6":
                    System.out.println("Goodbye!");
                    menuLoop = false;
                    break;
                case "q":
                    System.out.println("Goodbye!");
                    menuLoop = false;
                    break;
                case "Q":
                    System.out.println("Goodbye!");
                    menuLoop = false;
                    break;
            }
        }
    }
    
    public static void retrieveRecord(Scanner sc, Student student) {
        Menu.clearConsole();
        String input = "";
        System.out.println(menu.get(4).printTitle());
        System.out.println(student.printStudentInfo());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {}
        System.out.print("Press enter to continue... ");
        input = sc.nextLine();
    }
    
    public static void editRecord(Scanner sc, Student student) {
        Student.editStudent(sc, student, menu.get(5));
    }
    
    public static void saveRecord(Student student) {
        Student.writeStudents(students);
    }
    
    public static void enrolments(Scanner sc, Student student) {
        Menu.clearConsole();
        String input = "";
        
        menu.get(7).setMenuOptions(student.studentCourse == null ? new String[] {"Enrol in Course", "Back"} : new String[]{"Add New Papers", "Back"});
        menu.get(7).setMenuDescription(student.printStudentEnrolmentInfo());
        menu.get(7).printMenu();
        
        try {
            input = sc.nextLine().trim();
        } catch (InputMismatchException e) {
            System.out.println("Incorrect input!");
        }
        
        switch (input) {
            case "1":
                if (student.studentCourse == null) {
                    student.studentCourse = Student.enrolStudentInCourse(courses, student, sc);
                } else if (student.studentCourse != null) {
                    Student.enrolStudentInPapers(papers, student, sc);
                }
            case "2":
                return;
        }
        
    }
}
