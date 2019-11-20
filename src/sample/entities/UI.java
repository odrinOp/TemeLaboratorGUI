package sample.entities;

import sample.exceptions.ValidationException;
import sample.services.ServiceMaster;

import java.io.IOException;
import java.util.Scanner;

public class UI {

    private ServiceMaster serviceMaster;

    public UI(ServiceMaster serviceMaster) {

        this.serviceMaster = serviceMaster;
    }

    private void mainMenu(){
        System.out.println("1.Students");
        System.out.println("2.Homework");
        System.out.println("3.Grades");
        System.out.println("4.Filters");
        System.out.println("0. Exit");
    }

    private void gradeMenu(){
        System.out.println("1.Add");
        System.out.println("2.Delete");
        System.out.println("3.Find grade");
        System.out.println("4.Print all grades");
        System.out.println("0.Back");
    }

    private void studentMenu(){
        System.out.println("1.Add Student");
        System.out.println("2.Delete Student");
        System.out.println("3.Update Student");
        System.out.println("4.Find Student by ID");
        System.out.println("5.Show all students");
        System.out.println("0.Back");
    }

    private void homeworkMenu(){
        System.out.println("1.Add Homework");
        System.out.println("2.Delete Homework");
        System.out.println("3.Update Homework");
        System.out.println("4.Find Homework by ID");
        System.out.println("5.Show all homework");
        System.out.println("0.Back");
    }

    private void filterMenu(){
        System.out.println("1.Show all the students from a specific group.");
        System.out.println("2.Show all the students which have a grade at a specific homework");
        System.out.println("3.Show all the students which have a grade at a specific homework which was evaluated by a teacher");
        System.out.println("4.Show all the grades for a specific homework which were added on a specific week");
        System.out.println("0.Back");
    }


    //Student MENU
    private void uiAddStudent() throws ValidationException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the id");
        String id = sc.next();
        System.out.println("Enter the first name");
        String firstName = sc.next();
        System.out.println("Enter the last name");
        String lastName = sc.next();
        System.out.println("Enter the group number");
        int group = sc.nextInt();

        System.out.println("Enter the email (eg. example@scs.ubbcluj.ro)");
        String email = sc.next();
        System.out.println("Enter the guiding teacher name");
        String guidingTeacher = sc.next();


        Student s = serviceMaster.addStudent(id,firstName,lastName,group,email,guidingTeacher);
        if(s == null)
            System.out.println("Student added!");
        else
            System.out.println("Id already used!");

    }


    private void uiDeleteStudent(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the id");
        String id = sc.next();

        Student s = serviceMaster.removeStudent(id);
        if(s == null)
            System.out.println("ID doesn't exists!");

        else
            System.out.println("Student : " + s + "deleted!");
    }

    private void uiFindStudent(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the id");
        String id = sc.next();

        Student s = serviceMaster.findStudent(id);
        if(s == null)
            System.out.println("The ID doesn't exists!");

        else
            System.out.println(s);
    }


    private void uiUpdateStudent() throws ValidationException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the id");
        String id = sc.next();
        System.out.println("Enter the new first name");
        String firstName = sc.next();
        System.out.println("Enter the new last name");
        String lastName = sc.next();

        System.out.println("Enter the new group number");
        int group = sc.nextInt();

        System.out.println("Enter the new email (eg. example@scs.ubbcluj.ro)");
        String email = sc.next();

        System.out.println("Enter the new guiding teacher name");
        String guidingTeacher = sc.next();

        Student s = serviceMaster.updateStudent(id,firstName,lastName,group,email,guidingTeacher);
        if(s == null)
            System.out.println("Updated student");
        else
            System.out.println("ID doesn't exists!");
    }


    private void uiPrintAllStudents(){
        for(Student s:serviceMaster.getAllStudents())
            System.out.println(s);
    }


    //HOMEWORK MENU
    private void uiAddHomework() throws ValidationException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the id");
        String id = sc.next();
        System.out.println("Enter the description");
        String description = sc.next();
        System.out.println("Enter the deadline week");
        int deadlineWeek = sc.nextInt();

        Homework h = serviceMaster.addHomework(id,description,deadlineWeek);
        if(h==null)
            System.out.println("Homework added");
        else
            System.out.println("ID already exists");
    }

    private void uiDeleteHomework(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the id");
        String id = sc.next();

        Homework h = serviceMaster.removeHomework(id);
        if(h!=null)
            System.out.println("Homework: "+ h + " added");
        else
            System.out.println("ID doesn't exists");
    }

    private void uiUpdateHomework() throws ValidationException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the id");
        String id = sc.next();
        System.out.println("Enter the new description");
        String description = sc.next();
        System.out.println("Enter the new deadline week");
        int deadlineWeek = sc.nextInt();

        Homework h =serviceMaster.updateHomework(id,description,deadlineWeek);
        if(h==null)
            System.out.println("Homework updated");
        else
            System.out.println("ID doesn't exists");
    }

    private void uiFindHomework(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the id");
        String id = sc.next();

        Homework h = serviceMaster.findHomework(id);
        if(h==null)
            System.out.println("Homework doesn't exists");
        else
            System.out.println(h);
    }

    private void uiPrintHomework(){
        for(Homework homework: serviceMaster.getAllHomework())
            System.out.println(homework);
    }


    //GRADES MENU
    private void uiAddGrade() throws ValidationException, IOException {

        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the id of the homework");
        String homeworkID = sc.next();

        System.out.println("Enter the id of the student");
        String studentID = sc.next();

        System.out.println("Do you want to select manually the week and semester?");
        String cmd = sc.next();
        cmd.toLowerCase();
        int week = -1;
        int semester = -1;

        if(cmd.equals("yes")){
            System.out.println("Enter the week");
            week = sc.nextInt();
            semester = sc.nextInt();
        }

        int maxValue = serviceMaster.getMaximumValueForGrade(week,homeworkID);

        if(maxValue < 0 || maxValue > 10){
            System.out.println("The week doesn't exists!");
        }

        if(maxValue < 8)
        {
            System.out.println("You can't add a grade because the deadline week has been exceeded!");
            return;
        }

        System.out.println("Enter the teacher's name");
        String teacher = sc.next();

        System.out.println("Enter the value of the grade(between 0 and " + maxValue + ")");
        int value = sc.nextInt();

        System.out.println("Enter the feedback(enter 0 if you want to stop)");
        String[] feedback = new String[100];
        int i = 0;
        String data = sc.next();
        while(!data.equals("0")){
            feedback[i++] = data;
            data = sc.next();
        }

        Grade g = serviceMaster.addGrade(studentID,homeworkID,week,semester,teacher,value,feedback);
        if(g == null)
            System.out.println("The grade has been added!");
        else
            System.out.println("The grade for this student at this homework already exists!");

    }

    private void uiDeleteGrade(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the student ID");
        String studentID = sc.next();
        System.out.println("Enter the homework ID");
        String homeworkID = sc.next();

        Grade g = serviceMaster.removeGrade(studentID,homeworkID);
        if(g == null)
            System.out.println("The grade doesn't exists!");
        else
            System.out.println("The grade with the id [" + g.getId() + "] has been deleted!");
    }

    private void uiFindGrade(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the student ID");
        String studentID = sc.next();
        System.out.println("Enter the homework ID");
        String homeworkID = sc.next();

        Grade g = serviceMaster.removeGrade(studentID,homeworkID);
        if(g == null)
            System.out.println("The grade doesn't exists!");
        else
            System.out.println(g);


    }

    private void uiAllGrades(){
        for(Grade g: serviceMaster.getAllGrades())
            System.out.println(g);
    }


    //  FILTER FUNCTIONS    //
    private void uiShowStudentsGroup(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the group number");
        int group = sc.nextInt();

        for(Student s: serviceMaster.allStudentsFromSpecificGroup(group))
            System.out.println(s);
    }

    private void uiShowStudentsHomework(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the homework id");
        String homeworkID = sc.next();

        for (Student s: serviceMaster.allStudentsWithSpecificHomework(homeworkID))
            System.out.println(s);
    }

    private void uiShowStudentsHomeworkTeacher(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the id of the homework");
        String homeworkID = sc.next();
        System.out.println("Enter the teacher name");
        String teacher = sc.next();

        for(Student s:serviceMaster.allStudentsWithSpecificHomeworkAndTeacher(homeworkID,teacher))
            System.out.println(s);
    }

    private void uiShowGradesForHomework(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the id of the homework");
        String homeworkID = sc.next();
        System.out.println("Enter the week number(between 1 and 14)");
        int week = sc.nextInt();

        for(Grade g: serviceMaster.allGradesFromSpecificHomeworkAndWeek(homeworkID,week))
            System.out.println(g);
    }



    //LOAD/SAVE FUNCTIONS//
    private void loadData() throws IOException {
       serviceMaster.loadData();
    }

    private void saveData() throws IOException {
        serviceMaster.saveData();
    }

    private void loadDataXml(){
        serviceMaster.loadDataXml();
    }

    private void saveDataXml(){
        serviceMaster.saveDataXml();
    }


    public void run(){
            Scanner sc = new Scanner(System.in);
            String cmd = "";
            loadDataXml();
        while(true){
                try {
                    mainMenu();
                    System.out.println(">>");
                    cmd = sc.next();
                    if (cmd.equals("0"))
                        return;

                    if (!(cmd.equals("2") || cmd.equals("1") || cmd.equals("3") || cmd.equals("4")))
                        System.out.println("Invalid command!");
                    if (cmd.equals("1")) {
                        studentMenu();
                        Scanner sc1 = new Scanner(System.in);
                        String cmd1 = sc1.next();
                        switch (cmd1) {
                            case "1":
                                uiAddStudent();
                                break;
                            case "2":
                                uiDeleteStudent();
                                break;
                            case "3":
                                uiUpdateStudent();
                                break;
                            case "4":
                                uiFindStudent();
                                break;
                            case "5":
                                uiPrintAllStudents();
                                break;
                            default:
                                System.out.println("Invalid command");
                                break;
                        }

                    }
                    if (cmd.equals("2")) {
                        homeworkMenu();
                        Scanner sc1 = new Scanner(System.in);
                        String cmd1 = sc1.next();
                        switch (cmd1) {
                            case "1":
                                uiAddHomework();
                                break;
                            case "2":
                                uiDeleteHomework();
                                break;
                            case "3":
                                uiUpdateHomework();
                                break;
                            case "4":
                                uiFindHomework();
                                break;
                            case "5":
                                uiPrintHomework();
                                break;
                            default:
                                System.out.println("Invalid command");
                                break;
                        }
                    }
                    if(cmd.equals("3")){
                        gradeMenu();
                        Scanner sc1 = new Scanner(System.in);
                        String cmd1 = sc1.next();
                        switch (cmd1){
                            case "0":
                                break;
                            case "1":
                                uiAddGrade();
                                break;
                            case "2":
                                uiDeleteGrade();
                                break;
                            case "3":
                                uiFindGrade();
                                break;
                            case "4":
                                uiAllGrades();
                                break;
                            default:
                                System.out.println("Invalid command");
                                break;
                        }

                        }
                    if(cmd.equals("4")){
                        filterMenu();
                        Scanner sc1 = new Scanner(System.in);
                        String cmd1 = sc1.next();
                        switch (cmd1){
                            case "0":
                                break;
                            case "1":
                                uiShowStudentsGroup();
                                break;
                            case "2":
                                uiShowStudentsHomework();
                                break;
                            case "3":
                                uiShowStudentsHomeworkTeacher();
                                break;
                            case "4":
                                uiShowGradesForHomework();
                                break;
                            default:
                                System.out.println("Invalid command");
                                break;
                        }

                    }

                saveDataXml();
                }catch (ValidationException e){
                    e.printStackTrace();
                }
                catch (IOException e){
                    e.printStackTrace();
                }
                catch (IllegalArgumentException e){
                    e.printStackTrace();
                }
            }

    }
}
