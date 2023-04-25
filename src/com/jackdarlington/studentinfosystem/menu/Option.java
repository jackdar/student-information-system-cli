/*
 * Created by Jack Darlington | 2023
 */

package com.jackdarlington.studentinfosystem.menu;

/*
 * @author Jack Darlington
 * Student ID: 19082592
 * Date: 24/04/2023
 */

public class Option {
    
    public String optionTitle;
    public String optionDescription;
    public Menu optionMenu;
    
    public Option(String title) {
        this.optionTitle = title;
    }
    
    public Option(String title, String description) {
        this.optionTitle = title;
        this.optionDescription = description;
    }
    
    public Option(Menu menu) {
        this.optionTitle = menu.menuTitle;
        this.optionMenu = menu;
    }
    
    public boolean operation() {
        System.out.println("@Override for custom operation! Return true for repeat, false for no repeat.");
        return false;
    }
    
    public void show() {
        if (this.optionMenu != null) {
            this.optionMenu.show();
        } else {
            boolean repeat = true;
            while (repeat) {
                Menu.clearConsole();
                Menu.printTitle(this.optionTitle);
                if (this.optionDescription != null) {
                    System.out.println(this.optionDescription + "\n");
                }
                repeat = this.operation();
            }
        }
    }
}
