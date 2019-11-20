package sample.services;

import sample.entities.Student;
import sample.exceptions.ValidationException;
import sample.repositories.StudentRepository;

import java.io.IOException;

public class ServiceStudents {
    private StudentRepository repo;

    public ServiceStudents(StudentRepository repo) {
        this.repo = repo;
    }


    /**
     * @param id - The id of the student
     * @param firstName - The first name of the student
     * @param lastName - The last name of the student
     * @param group - The number of the group
     * @param email - the email of the student
     * @param guidingTeacher - The name of the guidingTeacher
     * @return null- if the student is saved in repository
     *       student- if the student is already saved in repository
     *
     * @throws ValidationException - if the student is not valid
     */
    public Student addStudent(String id, String firstName, String lastName, int group, String email, String guidingTeacher) throws ValidationException {
        Student student = new Student(id,firstName,lastName,group,email,guidingTeacher);
        return repo.save(student);
    }


    /**
     *
     * @param id- the ID of the student.
     * @return null - if the student with specific id is not found
     *       student - the student with the specific id
     */
    public Student removeStudent(String id){
        return repo.delete(id);
    }


    /**
     *
     * @param id
     * @param new_firstName
     * @param new_lastName
     * @param new_group
     * @param new_email
     * @param new_guidingTeacher
     * @return null - if the student data is updated.
     *      student - if the student doesn't exists.
     */
    public Student updateStudent(String id, String new_firstName, String new_lastName,int new_group, String new_email, String new_guidingTeacher) throws ValidationException {
        Student student = new Student(id,new_firstName,new_lastName,new_group,new_email,new_guidingTeacher);
        return repo.update(student);
    }

    /**
     *
     * @return - a list of students
     */
    public Iterable<Student> getAllStudents(){
        return repo.findAll();
    }

    /**
     * @param id
     * @return null - the student with specific id doesn't exists
     *    student- with the specific id.
     */
    public Student findStudent(String id){
        return repo.findOne(id);
    }


    /**
     * Read the data from a file and add to memory.
     * @throws IOException
     */
    public void readFromFile() throws IOException {
        String file = "src/sample/files/studenti.csv";
        repo.readFromFile(file);
    }


    public void writeToFile() throws IOException {
        repo.writeToFile("src/sample/files/studenti.csv");
    }


    public void readFromXmlFile(){
        String file = "src/sample/files/xmlFiles/studenti.xml";
        repo.readFromXmlFile(file,"student");
    }

    public void writeToXmlFile(){
        String file = "src/sample/files/xmlFiles/studenti.xml";
        repo.writeToXmlFile(file,"studenti","student");

    }




}
