/*
 * Created by Jack Darlington | 2023
 */

package com.jackdarlington.studentinfosystem.main;

import com.jackdarlington.studentinfosystem.menu.Menu;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;

/*
 * @author Jack Darlington
 * Student ID: 19082592
 * Date: 08/03/2023
 */

public class Main {
    
    static HashMap<String,Paper> papers;
    static HashMap<String,Course> courses;
    
    static HashMap<Integer, Student> students;
    
    static ArrayList<Menu> menu;
    
    public static void main(String[] args) {
        papers = Paper.initializePapers();
        courses = Course.initializeCourses();
        
        students = new HashMap<Integer, Student>();
        
        menu = new ArrayList<Menu>();
        
        menu.add(new Menu("Student Information System", new String[] {"Log In", "Create New Record"}));
        menu.add(new Menu("Log In"));
        menu.add(new Menu("Create New Record"));
        menu.add(new Menu("Student Information System", new String[] {"Retrieve Record", "Edit Record", "Save Record", "Enrolments", "Grades", "Log Out"}));
        menu.add(new Menu("Retrieve Record"));
        menu.add(new Menu("Edit Record"));
        menu.add(new Menu("Save Record"));
        menu.add(new Menu("Enrolments"));
        menu.add(new Menu("Grades"));
        menu.add(new Menu("Log Out"));
        
        Student jack = new Student("Jack", "Darlington", "BCIS");
        students.put(jack.studentID, jack);
        
        /*
        System.out.println(jack.course.courseCode + " " + jack.course.courseName);
        System.out.println("================\nPossible Papers to Take:");
        for(Paper p:jack.course.includedPapers) {
            System.out.println(" - " + p.paperCode + " " + p.paperName);
        }
        */
        
        Scanner sc = new Scanner(System.in);
        
        Student student = loginMenu(sc);
        if (student == null) {
            System.out.println("Goodbye!");
        }
        
        mainMenu(sc, student);
        
        
        
        
        
    }
    
    public static Student loginMenu(Scanner sc) {
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
            case "q":
                student = null;
                break;
            case "Q":
                student = null;
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
                case "q":
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
        System.out.print("Press any key to continue... ");
        input = sc.nextLine();
    }
    
    public static void editRecord(Scanner sc, Student student) {
        Student.editStudent(sc, student, menu.get(5));
    }
    
    public static void saveRecord(Student student) {
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new File("res/students.txt"));
        } catch (IOException e) {}
        pw.print(student.firstName + "," + student.lastName + "," + student.age + ",");
        pw.close();
    }
}
