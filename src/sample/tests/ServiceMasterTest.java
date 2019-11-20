package sample.tests;

import org.junit.Test;

import org.junit.jupiter.api.BeforeEach;
import sample.entities.Grade;
import sample.entities.Homework;
import sample.entities.Student;
import sample.entities.YearStructure;
import sample.exceptions.ValidationException;
import sample.repositories.GradeRepository;
import sample.repositories.HomeworkRepository;
import sample.repositories.StudentRepository;
import sample.services.ServiceGrades;
import sample.services.ServiceHomework;
import sample.services.ServiceMaster;
import sample.services.ServiceStudents;
import sample.validations.GradeValidation;
import sample.validations.HomeworkValidation;
import sample.validations.StudentValidation;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;



class ServiceMasterTest {
    private ServiceMaster serviceMaster;
    @BeforeEach
    void setUp() {
        YearStructure ys = new YearStructure("2019");

        StudentValidation sv = new StudentValidation();
        HomeworkValidation hv = new HomeworkValidation();
        GradeValidation gv = new GradeValidation();

        StudentRepository sr = new StudentRepository(sv);
        HomeworkRepository hs = new HomeworkRepository(hv);
        GradeRepository gr = new GradeRepository(gv);

        ServiceStudents ss = new ServiceStudents(sr);
        ServiceHomework sh = new ServiceHomework(hs);
        ServiceGrades sg = new ServiceGrades(gr);

        serviceMaster = new ServiceMaster(sg,ss,sh,ys);


        //adding some elements to the list
        try {
            serviceMaster.addStudent("1","Odrin","Traian",225,"odrin123@gmail.com","Alina");
            serviceMaster.addStudent("2","Andrei","Miron",226,"odrin123@gmail.com","Alina");
            serviceMaster.addStudent("3","Ovidiu","Catalin",237,"odrin123@gmail.com","Alina");
            serviceMaster.addStudent("4","Ana","Maftei",325,"odrin123@gmail.com","Alina");
            serviceMaster.addStudent("5","test1","test1",220,"test1.test1@gmail.com","Alina");
            serviceMaster.addStudent("6","test2","test2",220,"test1.test1@gmail.com","Alina");


            int deadlineWeek = ys.getWeekByDate(LocalDate.now())+1;
            if(deadlineWeek != 0) {
                serviceMaster.addHomework("1", "abc", deadlineWeek);
                serviceMaster.addHomework("2", "ccc", deadlineWeek);
            }


            String[] feedback = new String[100];
            feedback[0] = "good";
            if(deadlineWeek != 0) {


                serviceMaster.addGrade("5", "1", deadlineWeek - 1, ys.getSemesterByDate(LocalDate.now()), "Alina", serviceMaster.getMaximumValueForGrade(deadlineWeek - 1, "1"), feedback);
                serviceMaster.addGrade("5", "2", deadlineWeek - 1, ys.getSemesterByDate(LocalDate.now()), "Ion", serviceMaster.getMaximumValueForGrade(deadlineWeek - 1, "1"), feedback);
                serviceMaster.addGrade("4", "1", deadlineWeek - 1, ys.getSemesterByDate(LocalDate.now()), "Alina", serviceMaster.getMaximumValueForGrade(deadlineWeek - 1, "1"), feedback);

            }
        } catch ( IOException | ValidationException e) {}

    }

    @Test
    void addStudent() {
        try {
            assert serviceMaster.addStudent("7", "ok", "ok", 225, "ok@gmail.com", "alina") == null;
            Student s = serviceMaster.addStudent("7", "ok", "ok", 225, "ok@gmail.com", "alina");
            assert s!=null;
            assert s.getId().equals("7");
            assert s.getFirstName().equals("ok");
        }catch (ValidationException e){}
    }

    @Test
    void removeStudent() {
        Student s = serviceMaster.removeStudent("1");
        assert s!=null;
        assert s.getFirstName().equals("Odrin");

        assert serviceMaster.removeStudent("1") == null;
    }

    @Test
    void updateStudent() throws ValidationException {
        assert serviceMaster.updateStudent("1","odrin","traian",123,"ggg","aaa")==null;
        Student s = serviceMaster.updateStudent("147","odrin","traian",123,"ggg","aaa");
        assert s!=null;
        assert s.getId().equals("147");
    }

    @Test
    void findStudent() {
        Student s = serviceMaster.findStudent("1");
        assert s!=null;
        assert s.getFirstName().equals("Odrin");

        assert serviceMaster.findStudent("147") == null;
    }

    @Test
    void getAllStudents() {
        int i = 1;
        for(Student student:serviceMaster.getAllStudents()){
            assert student.getId().equals(String.valueOf(i));
            i++;
        }
    }



    @Test
    void addHomework() {

        int deadlineWeek = serviceMaster.getYearStructure().getWeekByDate(LocalDate.now()) + 1;

        try {
            if(deadlineWeek == 0) {
                serviceMaster.addHomework("25", "abc", deadlineWeek);
            }
            else
                assert true;
        }catch (ValidationException e){assert  true;}

        try{
            if(deadlineWeek!= 0){
                assert  serviceMaster.addHomework("25","abc",deadlineWeek) == null;
                Homework h =  serviceMaster.addHomework("25","abc",deadlineWeek);
                assert h!=null;
                assert h.getStartWeek() == deadlineWeek-1;
            }
        }catch (ValidationException e){assert  false;}



    }

    @Test
    void removeHomework() {
        int startWeek = serviceMaster.getYearStructure().getWeekByDate(LocalDate.now());
        if(startWeek!=-1){
            Homework h = serviceMaster.removeHomework("1");
            assert h!=null;
            assert h.getDeadlineWeek() == startWeek+1;

            assert serviceMaster.removeHomework("1") == null;

        }
        assert true;
    }

    @Test
    void updateHomework() {
        int deadlineWeek = serviceMaster.getYearStructure().getWeekByDate(LocalDate.now())+1;
        if(deadlineWeek != 0){
            try {
                assert serviceMaster.updateHomework("1", "new_desc", deadlineWeek) == null;
                Homework h = serviceMaster.updateHomework("175","bad_desc",deadlineWeek);
            }catch (ValidationException | IllegalArgumentException e){assert true;}
        }
        assert true;
    }

    @Test
    void findHomework() {
        int startWeek = serviceMaster.getYearStructure().getWeekByDate(LocalDate.now());
        if(startWeek != -1)
        {
            assert serviceMaster.findHomework("170") == null;
            Homework h = serviceMaster.findHomework("1");
            assert h!=null;
            assert h.getDescription().equals("abc");
        }
    }

    @Test
    void getAllHomework() {
        int startWeek = serviceMaster.getYearStructure().getWeekByDate(LocalDate.now());
        if(startWeek != -1){
            int i = 1;
            for(Homework homework: serviceMaster.getAllHomework()){
                assert homework.getId().equals(String.valueOf(i));
                i++;
            }
        }
    }

    @Test
    void addGrade() {
        int deadlineWeek = serviceMaster.getYearStructure().getWeekByDate(LocalDate.now());
        String[] feedback = new String[1];
        feedback[0] = "Hello";
        if(deadlineWeek != 0) {
            try {
                assert serviceMaster.addGrade("6", "1", deadlineWeek - 1, serviceMaster.getYearStructure().getSemesterByDate(LocalDate.now()), "Alina", serviceMaster.getMaximumValueForGrade(deadlineWeek - 1, "1"), feedback) == null;
                Grade g = serviceMaster.addGrade("6", "1", deadlineWeek - 1, serviceMaster.getYearStructure().getSemesterByDate(LocalDate.now()), "Alina", serviceMaster.getMaximumValueForGrade(deadlineWeek - 1, "1"), feedback);
                assert g!=null;
                assert g.getStudent().equals("6");
            } catch (ValidationException e) {
                assert false;
            } catch (IOException e) {}
        }
        assert true;
    }


    @Test
    void removeGrade() {
        Grade g = serviceMaster.removeGrade("5","1");
        assert g!=null;
        assert g.getTeacher().equals("Alina");

        assert serviceMaster.removeGrade("5","1") == null;
    }

    @Test
    void findGrade() {
        assert serviceMaster.findGrade("127","130") == null;
        Grade g = serviceMaster.findGrade("5","1");
        assert g!=null;
        assert g.getTeacher().equals("Alina");
    }

    @Test
    void getAllGrades() {
        for(Grade g:serviceMaster.getAllGrades()){
            assert g.getTeacher().equals("Alina") || g.getTeacher().equals("Ion");
        }

    }

    @Test
    void allStudentsFromSpecificGroup(){
        try {
            serviceMaster.addStudent("7", "ok", "ok", 225, "ok@gmail.com", "alina");
        } catch (ValidationException e) { assert false; }

        List<Student> students = serviceMaster.allStudentsFromSpecificGroup(225);
        assert students.size() == 2;
        for(Student s:students){
            assert s.getGroup() == 225;
        }

        students = serviceMaster.allStudentsFromSpecificGroup(1);
        assert students.size() == 0;
    }

    @Test
    void allStudentsWithSpecificHomework(){
        List<Student> students = serviceMaster.allStudentsWithSpecificHomework("1");
        assert students != null;
        assert students.size() == 2;
        for(Student s: students){
            assert s.getGuidingTeacher().equals("Alina");
        }

        students = serviceMaster.allStudentsWithSpecificHomework("170");
        assert students != null;
        assert students.size() == 0;

    }

    @Test
    void allStudentsWithSpecificHomeworkAndTeacher(){
        List<Student> students = serviceMaster.allStudentsWithSpecificHomeworkAndTeacher("1","Alina");
        assert students != null;
        assert students.size() == 2;

        students = serviceMaster.allStudentsWithSpecificHomeworkAndTeacher("1","Ion");
        assert students != null;
        assert students.size() == 0;

        students = serviceMaster.allStudentsWithSpecificHomeworkAndTeacher("2","Ion");
        assert students != null;
        assert students.size() == 1;
    }

    @Test
    void allGradesFromSpecificHomeworkAndWeek(){
        List<Grade> grades = serviceMaster.allGradesFromSpecificHomeworkAndWeek("1",serviceMaster.getYearStructure().getWeekByDate(LocalDate.now()));
        assert grades != null;
        assert grades.size() == 2;

        grades = serviceMaster.allGradesFromSpecificHomeworkAndWeek("2",serviceMaster.getYearStructure().getWeekByDate(LocalDate.now()));
        assert grades != null;
        assert grades.size() == 1;

        grades = serviceMaster.allGradesFromSpecificHomeworkAndWeek("190",serviceMaster.getYearStructure().getWeekByDate(LocalDate.now()));
        assert grades != null;
        assert grades.size() == 0;
    }
}