/*
 * Created by Jack Darlington | 2023
 */

package com.jackdarlington.studentinfosystem.menu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/*
 * @author Jack Darlington
 * Student ID: 19082592
 * Date: 09/03/2023
 */

public class Menu {
    
    public String menuTitle;
    public ArrayList<Option> menuOptions;
    
    private Runnable descriptionMethod;
    private String returnOptionText;
    private ArrayList<Option> visibleOptions;
    
    public void setDescriptionMethod(Runnable descriptionMethod) {
        this.descriptionMethod = descriptionMethod;
    }
    
    public Menu(String title, Option... options) {
        this(title, null, null, options);
    }

    public Menu(String title, String returnOptionText, Option... options) {
        this(title, null, returnOptionText, options);
    }

    public Menu(String title, Runnable descriptionMethod, Option... options) {
        this(title, descriptionMethod, null, options);
    }

    public Menu(String title, Runnable descriptionMethod, String returnOptionText, Option... options) {
        this.menuTitle = title;
        this.descriptionMethod = descriptionMethod;
        this.menuOptions = generateMenuOptions(options);
        this.returnOptionText = returnOptionText != null ? returnOptionText : "Back";
    }
    
    private ArrayList<Option> generateMenuOptions(Option... options) {
        ArrayList<Option> newMenuOptions = new ArrayList<>();
        newMenuOptions.addAll(Arrays.asList(options));
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
        this.visibleOptions = new ArrayList<>();
        for (Option o : menuOptions) {
            if (o.visible) {
                visibleOptions.add(o);
            }
        }
        for (int i = 1; i < visibleOptions.size() + 1; i++) {
            System.out.println("  (" + i + ") " + visibleOptions.get(i - 1).optionTitle);
        }
        System.out.println("  (" + (visibleOptions.size() + 1) + ") " + this.returnOptionText);
    }
    
    public void printMenu() {
        printTitle(this.menuTitle);
        if (this.descriptionMethod != null) {
            this.descriptionMethod.run();
            System.out.println();
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
                if (number > 0 && number <= this.visibleOptions.size()) {
                    this.visibleOptions.get(number - 1).show();
                } else if (number == this.visibleOptions.size() + 1) {
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
    
    public static void anyKeyToContinue() {
        Scanner sc = new Scanner(System.in);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {}
        System.out.print("\nPress enter to continue... ");
        sc.nextLine();
    }
    
}
