# Student Information System

The Student Information System is an all-in-one solution for managing student records. The program aims to be simple and easy to use, while also highly functional.

## Log In Menu

The log in menu is the first menu displayed to the user on start up. By this point the program has already successfully loaded all the Papers, Courses, and Student data files.

### Log In

Using this option the user can log in as a pre-existing student, using a **Student ID**. The user must know the student ID, in future versions a user query function is in the works.

### Create New Record

This allows the user to create a brand new student record. All fields have a small amount of formatting they maintain, however general data tidiness relies on user input. The user fields as follows:

 - First Name (String)
 - Last Name (String)
 - Personal Email (String)
 - Phone Number (Integer)
 - Age (Integer)
 - Street Number (Integer)
 - Street Name (String)
 - Suburb (String)
 - City (String)
 - Post Code (String)
 - Currently Attending (Boolean)
 - Receiving Emails (Boolean)

This new record will be created and will be stored in memory until the program is exited. The record must be saved using **`SAVE RECORD`**.

> **NOTE**: Some fields are not user editable such as **Student ID** and **Student Email** which is generated on creation of a new student record.

## Main Menu

The main menu is where the user can edit and save records. The main menu displays the **Student Name**, **ID**, and **Student Email** in the description, along with all the relevant menu options below.

### Retrieve Record

Using retrieve record the user is displayed a more comprehensive list of the record details including personal email, phone number, age, and address. This is the same information that is displayed after creating or editing a record.

### Edit Record

The edit record option is used to edit a user record, it displays the exact same set of field entries as **`CREATE NEW RECORD`**, except rather than create a new student record, edit record applies the field entries to the existing student record.

> **NOTE**: No changes here are saved until **`SAVE RECORD`** is used.

### Save Record

Using save record the user can save all working changes on a file to record. The program saves the student data in “res/students.txt”. All student data is written including:

 - Unique fields: Student ID, Student Email
 - Data Map fields: First Name, Last Name, etc.
 - Boolean values: Receiving emails, etc.
 - Enrolment Information: Course & Paper codes, Grades

## Enrolment Menu

The enrolment menu is where the user can enrol a student into a course and relevant papers.

> **NOTE**: No changes here are saved until **`SAVE RECORD`** is used.

### Enrol In Course

When Enrol In Course is selected the user is provided with a list to be able to select from the available courses. The course that is selected will determine what papers the student can then be enrolled into.

### Add New Papers

When a student has enrolment data in their record, or a student has just been enrolled in a course, that information is now visible in the enrolments menu,

The available options have now changed to **`ADD NEW PAPERS`** and **`LEAVE COURSE`**.

This option displays a list of the available papers to the student. This information is pulled from *“res/papersincourses.txt”* file which maps the corresponding papers to the correct course.

The user can then select, one or multiple papers one after the other until they return using **`q`** to go back.

### Leave Course

This option removes a student from their current course. This also removes any papers related to that course from their record.

## Grades Menu

The grades menu is where the user can find the current grades for a student. The user can also set the grades that the student got for each paper.

> **NOTE**: No changes here are saved until **`SAVE RECORD`** is used.

### Set Grade

Using set grade the user can set a grade for each paper a student is enrolled into. The option asks for which grade the user would like to edit from the list of current student enrolled papers, and then the enumerated type Grade can be applied. The list of grades is as follows:

- NOT_COMPLETED
- FAIL
- C_MINUS, C, C_PLUS
- B_MINUS, B, B_PLUS
- A_MINUS, A, A_PLUS
    
Once a grade has been set, it is reflected in all areas of the Grade Menu.
    
## Handling Text Files

All the text files provided with this build of the Student Information System have been correctly formatted for use with the program.
    
While the program should have no issue with new Courses or Papers being added, this is an untested feature. New student records being manually entered as also technically fine as long as the formatting and field order is kept the same, however this is also untested.
    
## Student Data

The Student data provided with this build of the Student Information System is simply test data and can be used with the program to test program functionality.
