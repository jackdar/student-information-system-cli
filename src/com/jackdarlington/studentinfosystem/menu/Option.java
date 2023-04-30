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
    public Menu optionMenu;
    public boolean visible;
    
    private Runnable descriptionMethod;
    
    public Option(String title) {
        this(title, null, null, true);
    }
    
    public Option(String title, boolean visible) {
        this (title, null, null, visible);
    }
    
    public Option(String title, Runnable descriptionMethod) {
        this(title, descriptionMethod, null, true);
    }
    
    public Option(String title, Runnable descriptionMethod, boolean visible) {
        this(title, descriptionMethod, null, visible);
    }
    
    public Option(Menu menu) {
        this(null, null, menu, true);
    }
    
    public Option(Menu menu, boolean visible) {
        this(null, null, menu, visible);
    }
    
    public Option(Runnable descriptionMethod, Menu menu) {
        this(null, descriptionMethod, menu, true);
    }
    
    public Option(Runnable descriptionMethod, Menu menu, boolean visible) {
        this(null, descriptionMethod, menu, visible);
    }
    
    public Option(String title, Runnable descriptionMethod, Menu menu, boolean visible) {
        this.optionMenu = menu;
        this.optionTitle = title != null ? title : menu != null ? menu.menuTitle : null;
        this.descriptionMethod = descriptionMethod;
        this.visible = visible;
    }
    
    // This is the method that is run when no nested menu is supplied.
    // This method is to be overidden with custom logic.
    public boolean operation() {
        System.out.println("@Override for custom operation! Return true for repeat, false for no repeat.");
        return false;
    }
    
    // The "main" option method. This method runs the show() method of a supplied nested menu,
    // else it runs the custom logic provided in the operation() method.
    // The returned boolean from opertaion() determines if the logic loops again or not.
    public void show() {
        if (this.optionMenu != null) {
            this.optionMenu.show();
        } else {
            boolean repeat = true;
            while (repeat) {
                Menu.clearConsole();
                Menu.printTitle(this.optionTitle);
                if (this.descriptionMethod != null) {
                    this.descriptionMethod.run();
                    System.out.println();
                }
                repeat = this.operation();
            }
        }
    }
    
}