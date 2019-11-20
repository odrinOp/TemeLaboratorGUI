package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import sample.entities.Student;
import sample.entities.UI;
import sample.entities.YearStructure;
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

import javax.swing.*;
import java.io.IOException;

public class Main extends Application {

    StudentValidation studentValidation;
    StudentRepository studentRepository;
    ServiceStudents serviceStudents;
    @Override
    public void start(Stage primaryStage) throws Exception{

        studentValidation = new StudentValidation();
        studentRepository = new StudentRepository(studentValidation);
        serviceStudents = new ServiceStudents(studentRepository);
        serviceStudents.readFromXmlFile();

        init(primaryStage);
        primaryStage.show();

    }


    public void init(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("sample.fxml"));
        BorderPane rootLayout = loader.load();

        primaryStage.setTitle("TemeLaborator");
        primaryStage.setScene(new Scene(rootLayout, 800, 600));

        StudentsCtr controller = loader.getController();
        controller.setService(serviceStudents);


    }

    public static void initUI(){
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

        ServiceMaster serviceMaster = new ServiceMaster(sg,ss,sh,ys);


        UI ui = new UI(serviceMaster);
        ui.run();

    }
    public static void main(String[] args) {

        //initUI();
        launch(args);
    }
}
