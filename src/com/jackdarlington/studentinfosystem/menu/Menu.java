/*
 * Created by Jack Darlington | 2023
 */

package com.jackdarlington.studentinfosystem.menu;

import java.util.HashMap;
import java.util.Scanner;

/*
 * @author Jack Darlington
 * Student ID: 19082592
 * Date: 09/03/2023
 */

public class Menu {
    
    public String menuTitle;
    public String menuDescription;
    public HashMap<Integer, Option> menuOptions;
    
    private HashMap<Integer, Option> generateMenuOptions(Option... options) {
        HashMap<Integer, Option> newMenuOptions = new HashMap<Integer, Option>();
        for (int i = 1; i < options.length + 1; i++) {
            newMenuOptions.put(i, options[i-1]);
        }
        return newMenuOptions;
    }
    
    public static void printTitle(String title) {
        System.out.println();
        for (int i = 0; i < title.length() + 4; i++) {
            System.out.print("=");
        }
        System.out.println("\n  " + title);
        for (int i = 0; i < title.length() + 4; i++) {
            System.out.print("=");
        }
        System.out.println("\n");
    }
    
    public void printMenuOptions() {
        for (HashMap.Entry<Integer, Option> entry : menuOptions.entrySet()) {
            System.out.println("  (" + entry.getKey() + ") " + entry.getValue().optionTitle);
        }
        System.out.println("  (" + (menuOptions.size() + 1) + ") Back");
    }
    
    public Menu(String title) {
        this.menuTitle = title;
    }
    
    public Menu(String title, Option... options) {
        this.menuTitle = title;
        this.menuOptions = generateMenuOptions(options);
    }
    
    public Menu(String title, String description, Option... options) {
        this.menuTitle = title;
        this.menuDescription = description;
        this.menuOptions = generateMenuOptions(options);
    }
    
    public void printMenu() {
        printTitle(this.menuTitle);
        if (this.menuDescription != null) {
            System.out.println(this.menuDescription + "\n");
        }
        if (this.menuOptions != null) {
            this.printMenuOptions();
        }
    }
    
    public void show() {
        String input = "";
        while (!input.equalsIgnoreCase("q")) {
            clearConsole();
            this.printMenu();
            Scanner sc = new Scanner(System.in);
            input = sc.nextLine().trim();
            int number;
            try {
                number = Integer.parseInt(input);
                if (number > 0 && number <= this.menuOptions.size()) {
                    this.menuOptions.get(number).show();
                } else if (number == this.menuOptions.size() + 1) {
                    return;
                } else {
                    System.out.println("That is not a valid option! Try again!");
                }
            } catch (NumberFormatException ex) {
                System.out.println("That is not a valid option! Try again!");
            }
        }
    }
    
    public static void clearConsole() {  
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }
    
}
