package sample.services;

import sample.entities.*;
import sample.exceptions.ValidationException;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ServiceMaster {
    private ServiceGrades serviceGrades;
    private ServiceStudents serviceStudents;
    private ServiceHomework serviceHomework;
    private YearStructure yearStructure;

    public ServiceMaster(ServiceGrades serviceGrades, ServiceStudents serviceStudents, ServiceHomework serviceHomework, YearStructure yr) {
        this.serviceGrades = serviceGrades;
        this.serviceStudents = serviceStudents;
        this.serviceHomework = serviceHomework;
        this.yearStructure = yr;
    }


    //  --STUDENT FUNCTIONS --//

    /**
     *
     * @param id - the id of the student
     * @param firstName - first name of the student
     * @param lastName - last name of the student
     * @param group - the group number of the student
     * @param email - the email of the student
     * @param guidingTeacher - the guiding teacher of the student
     * @return null-the student has been saved, otherwise return the student.
     * @throws ValidationException - student is not valid(eg. id invalid)
     */
    public Student addStudent(String id, String firstName, String lastName, int group, String email, String guidingTeacher) throws ValidationException {
        return serviceStudents.addStudent(id,firstName,lastName,group,email,guidingTeacher);
    }

    /**
     *
     * @param id - the id of the student (must be not null)
     * @return null if the student doesn't exists, otherwise the student which we deleted.
     */
    public Student removeStudent(String id){
        return serviceStudents.removeStudent(id);
    }

    /**
     *
     * @param id - the id of an existing student
     * @param newFirstName - the first name of the student
     * @param newLastName - the last name of the student
     * @param newGroup - the group number of the student
     * @param newEmail - the email of the student
     * @param newGuidingTeacher - the guiding teacher of the student
     * @return null if the student has been updated, otherwise the student.
     */
    public Student updateStudent(String id, String newFirstName, String newLastName,int newGroup, String newEmail,String newGuidingTeacher) throws ValidationException {
        Student ss = findStudent(id);
        String old_file = "";
        if (ss != null)
        {
            old_file = ss.getFirstName() + "_"+ss.getLastName();
        }

        Student s = serviceStudents.updateStudent(id,newFirstName,newLastName,newGroup,newEmail,newGuidingTeacher);

        if(s == null)
            renameFile(old_file,newFirstName + "_" + newLastName);
        return s;
    }

    /**
     *
     * @param id - id of the student
     * @return null if the student doesn't exists, otherwise the student
     */
    public Student findStudent(String id){
        return serviceStudents.findStudent(id);
    }

    /**
     *
     * @return all the students
     */
    public Iterable<Student> getAllStudents(){
        return serviceStudents.getAllStudents();
    }




//  --HOMEWORK FUNCTIONS    --//

    /**
     *
     * @param id - the id of the homework
     * @param description - the description of the homework
     * @param deadlineWeek - the deadline week of the homework
     * @return null if the homework has been saved, otherwise return the homework
     * @throws ValidationException - the homework is not valid(eg. invalid id)
     */
    public Homework addHomework(String id, String description, int deadlineWeek) throws ValidationException {
        int startWeek = yearStructure.getWeekByDate(LocalDate.now());
        return serviceHomework.addHomework(id,description,startWeek,deadlineWeek);
    }

    /**
     *
     * @param id - the id of an existing homework
     * @return null if the homework doesn't exists, otherwise return the deleted homework
     */
    public Homework removeHomework(String id){
        return serviceHomework.deleteHomework(id);
    }

    /**
     *
     * @param id - the id of the homeowrk
     * @param description - the description of the homework
     * @param deadlineWeek - the deadline week of the homework
     * @return null if the homework has been updated,otherwise return the homework
     * @throws ValidationException - if the new homework is invalid.
     */
    public Homework updateHomework(String id,String description,int deadlineWeek) throws ValidationException {
        LocalDate now = LocalDate.now();

        if(deadlineWeek < yearStructure.getWeekByDate(now))
            throw new ValidationException("Invalid deadline week!");

        return serviceHomework.updateHomework(id,description,deadlineWeek);
    }

    /**
     *
     * @param id - the id of an existing homework
     * @return null if the homework doesn't exists, otherwise return the homework with specific id
     */
    public Homework findHomework(String id){
        return serviceHomework.findHomework(id);
    }

    /**
     *
     * @return all homework.
     */
    public Iterable<Homework> getAllHomework(){
        return serviceHomework.getAllHomework();
    }




//  --GRADES FUNCTIONS--    //

    /**
     *
     * @param studentID - the id of the existing student
     * @param homeworkID - the id of the existing homework
     * @param week - the week in which the grade has been added
     * @param semester - the semester in which the grade has been added
     * @param teacher - the teacher which added the grade
     * @param value - the value of the grade
     * @param feedback - the feedback which the teacher gave it to this grade
     * @return null if the grade has been saved, otherwise return the grade
     * @throws ValidationException - the student/homework doesn't exists, or the grade is not valid
     * @throws IOException - can't open the feedback file for a student
     */
    public Grade addGrade(String studentID, String homeworkID, int week, int semester, String teacher, int value, String[] feedback) throws ValidationException, IOException {
        LocalDate date;
        if(week == -1 && semester == -1)
            date = LocalDate.now();
        else
            date = yearStructure.getDateByWeek(week,semester);

        if(date == null)
            throw new ValidationException("The date is not valid!");

        Student s = serviceStudents.findStudent(studentID);
        if(s==null)
            throw new ValidationException("The student id doesn't exists");

            Homework h = serviceHomework.findHomework(homeworkID);
        if(h == null)
            throw new ValidationException("The homework id doesn't exists");

        if(getMaximumValueForGrade(yearStructure.getWeekByDate(date),homeworkID) < 8)
            throw new IllegalArgumentException("You can't add a new grade");

        if(value > getMaximumValueForGrade(yearStructure.getWeekByDate(date),homeworkID))
            throw new IllegalArgumentException("The value for the grade is not good!");

        Grade g = serviceGrades.addGrade(s,h,date,teacher,value);
        if(g!=null)
            return g;

        String data = serviceGrades.createFeedback(homeworkID,value,yearStructure.getWeekByDate(date),h.getDeadlineWeek(),feedback);
        serviceGrades.createInfoData(s,data);
        return null;
    }

    /**
     * Calculate the maximum value for a grade
     * @param week - the week in whcih the grade has been saved
     * @param homeworkID - the id of the homework
     * @return value(between 1 and 10).
     */
    public int getMaximumValueForGrade(int week,String homeworkID){
        Homework h = serviceHomework.findHomework(homeworkID);
        if(h == null)
            throw new IllegalArgumentException("The homework doesn't exists!");

        if(week == -1){
            week = yearStructure.getWeekByDate(LocalDate.now());
        }
        if(week == -1){
            throw new IllegalArgumentException("We are outside of the semester!");
        }

        return serviceGrades.getMaximumGrade(week,h);
    }

    /**
     *
     * @param studentID - the id of an existing student
     * @param homeworkID - the id of an existing homework
     * @return null if the grade doesn't exists, otherwise return the deleted grade
     */
    public Grade removeGrade(String studentID,String homeworkID){
        return serviceGrades.removeGrade(studentID,homeworkID);
    }

    /**
     *
     * @param studentID - the id of an existing student
     * @param homeworkID - the id of an exsting homework
     * @return null if the grade doesn't exists, otherwise return the grade
     */
    public Grade findGrade(String studentID,String homeworkID){
        return serviceGrades.findGrade(studentID ,homeworkID);
    }

    /**
     *
     * @return all grades
     */
    public Iterable<Grade> getAllGrades(){
        return serviceGrades.getAllGrades();
    }




    //  --COMBINED FUNCTIONS--  //
    public YearStructure getYearStructure(){
        return yearStructure;
    }

    /**
     * load data from a file for every entity
     * @throws IOException
     */
    public void loadData() throws IOException {
        serviceStudents.readFromFile();
        serviceHomework.readFromFile();
        serviceGrades.readFromFile();
    }

    /**
     * Save data in a file for every entity
     * @throws IOException
     */
    public void saveData() throws IOException {
        serviceStudents.writeToFile();
        serviceHomework.writeToFile();
        serviceGrades.writeToFile();
    }


    public void loadDataXml(){
        serviceStudents.readFromXmlFile();
        serviceHomework.readFromXmlFile();
        serviceGrades.readFromXmlFile();
    }


    public void saveDataXml(){
        serviceStudents.writeToXmlFile();
        serviceHomework.writeToXmlFile();
        serviceGrades.writeToXmlFile();
    }

    public Student getStudentByGradeID(GradeID id){
        return serviceStudents.findStudent(serviceGrades.getStudentIDByGrade(id));
    }

    public Homework getHomeworkByGradeID(GradeID id){
        return serviceHomework.findHomework(serviceGrades.getHomeworkIDByGrade(id));
    }



    // -- FILTER FUNCTIONS -- //

    /**
     *
     * @param group_number - the group number of the students
     * @return all the students from a specific group.
     */
    public List<Student> allStudentsFromSpecificGroup(int group_number){
        List<Student> students = new ArrayList<>();
        serviceStudents.getAllStudents().forEach(students::add);

        return students.stream().filter( s -> s.getGroup() == group_number).collect(Collectors.toList());
    }

    /**
     *
     * @param homeworkID - the existing id of a homework
     * @return all the students which have a grade for this homework
     */
    public List<Student> allStudentsWithSpecificHomework(String homeworkID){
        List<Grade> grades = new ArrayList<>();
        serviceGrades.getAllGrades().forEach(grades::add);

        return grades.stream().filter(g -> g.getHomework().equals(homeworkID)).map(g -> getStudentByGradeID(g.getId())).collect(Collectors.toList());
    }

    /**
     *
     * @param homeworkID - the existing id of the homework
     * @param teacher - the name of the teacher which gave the grade
     * @return all students whcih have a grade for this homework from a specific teacher
     */
    public List<Student> allStudentsWithSpecificHomeworkAndTeacher(String homeworkID,String teacher){
        List<Grade> grades = new ArrayList<>();
        serviceGrades.getAllGrades().forEach(grades::add);

        return grades.stream().filter(g -> g.getHomework().equals(homeworkID) && g.getTeacher().equals(teacher)).map(g -> getStudentByGradeID(g.getId())).collect(Collectors.toList());
    }

    /**
     *
     * @param homeworkID - the existing id of the homework
     * @param week - the week in which the grade has been added
     * @return all the grades for a specific homework which have been added in a specific week.
     */
    public List<Grade> allGradesFromSpecificHomeworkAndWeek(String homeworkID,int week){
        List<Grade> grades = new ArrayList<>();
        serviceGrades.getAllGrades().forEach(grades::add);
        return grades.stream().filter(g -> g.getHomework().equals(homeworkID) && yearStructure.getWeekByDate(g.getDate()) == week).collect(Collectors.toList());
    }




    /**
     * @param old_name - the current name of the file
     * @param new_name - the new name of the file
     * @return true, the file has been renamed, false otherwise.
     */
    public boolean renameFile(String old_name,String new_name){
        File f1 = new File("src/sample/infoData/"+old_name +".txt");
        File f2 = new File("src/sample/infoData/"+new_name+".txt");

        return f1.renameTo(f2);
    }


    public boolean deleteFile(String filename){
        File f = new File("src/sample/infoData/"+filename +".txt");
        return f.delete();
    }

}
