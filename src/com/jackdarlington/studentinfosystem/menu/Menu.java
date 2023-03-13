/*
 * Created by Jack Darlington | 2023
 */

package com.jackdarlington.studentinfosystem.menu;

import java.util.HashMap;

/*
 * @author Jack Darlington
 * Student ID: 19082592
 * Date: 09/03/2023
 */

public class Menu {
    
    String menuTitle;
    String menuDescription;
    HashMap<Integer, String> menuOptions;
    
    public String getMenuTitle() {
        return this.menuTitle;
    }
    
    public String getMenuDescription() {
        return this.menuDescription;
    }
    
    public void setMenuDescription(String description) {
        this.menuDescription = description;
    }
    
    private HashMap<Integer, String> generateMenuOptions(String[] options) {
        HashMap<Integer, String> newMenuOptions = new HashMap<Integer, String>();
        for (int i = 1; i < options.length + 1; i++) {
            newMenuOptions.put(i, options[i-1]);
        }
        return newMenuOptions;
    }
    
    public String printTitle() {
        String out = "\n";
        for (int i = 0; i < this.getMenuTitle().length() + 4; i++) {
            out += "=";
        }
        out += "\n  " + this.getMenuTitle() + "\n";
        for (int i = 0; i < this.getMenuTitle().length() + 4; i++) {
            out += "=";
        }
        return out + "\n";
    }
    
    public String printMenuOptions() {
        String out = "";
        for (HashMap.Entry<Integer, String> entry : menuOptions.entrySet()) {
            out += "  (" + entry.getKey() + ") " + entry.getValue() + "\n";
        }
        return out;
    }
    
    public Menu(String title) {
        this.menuTitle = title;
    }
    
    public Menu(String title, String[] options) {
        this.menuTitle = title;
        this.menuOptions = generateMenuOptions(options);
    }
    
    public Menu(String title, String description, String[] options) {
        this.menuTitle = title;
        this.menuDescription = description;
        this.menuOptions = generateMenuOptions(options);
    }
    
    public void printMenu() {
        System.out.println(this.printTitle());
        if (this.menuDescription != null) {
            System.out.println(this.getMenuDescription());
        }
        if (this.menuOptions != null) {
            System.out.println(this.printMenuOptions());
        }
    }
    
    public static void clearConsole() {  
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }
    
}
