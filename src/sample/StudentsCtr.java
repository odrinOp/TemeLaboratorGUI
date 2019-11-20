package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import sample.entities.MessageAlert;
import sample.entities.Student;
import sample.exceptions.ValidationException;
import sample.services.ServiceMaster;
import sample.services.ServiceStudents;

import java.util.ArrayList;
import java.util.List;

public class StudentsCtr {

    @FXML TableView<Student> table;
    @FXML TextField textID;
    @FXML TextField textFirstName;
    @FXML TextField textLastName;
    @FXML TextField textGroup;
    @FXML TextField textEmail;
    @FXML TextField textTeacher;
    private ServiceStudents serviceStudents;


    public void setService(ServiceStudents serviceStudents){
        this.serviceStudents = serviceStudents;
        updateList(serviceStudents.getAllStudents());


    }

    private void setTextFieldsNull(){
        textID.setText("");
        textTeacher.setText("");
        textGroup.setText("");
        textEmail.setText("");
        textLastName.setText("");
        textFirstName.setText("");

    }

    public void updateList(Iterable<Student> list){
        table.getItems().clear();
        list.forEach(x->table.getItems().add(x));
    }

    @FXML
    public void getDataFromTable(){
        Student s = table.getSelectionModel().getSelectedItem();
        if(s == null){
            setTextFieldsNull();
            return;
        }
        textID.setText(s.getId());
        textFirstName.setText(s.getFirstName());
        textLastName.setText(s.getLastName());
        textEmail.setText(s.getEmail());
        textGroup.setText(String.valueOf(s.getGroup()));
        textTeacher.setText(s.getGuidingTeacher());
    }

@FXML
    public void addStudent(){
        String id = textID.getText();
        String firstName = textFirstName.getText();
        String lastName = textLastName.getText();
        String email = textEmail.getText();
        int group = 0;
        try {
            group = Integer.parseInt(textGroup.getText());
        }catch(NumberFormatException e){
            MessageAlert.showMessage(null, Alert.AlertType.ERROR,"NumberFormatException","The group must be an integer!");
            return;
        }
        String teacher = textTeacher.getText();

        try {
            Student s = serviceStudents.addStudent(id,firstName,lastName,group,email,teacher);
            if(s==null){
                serviceStudents.writeToXmlFile();
                updateList(serviceStudents.getAllStudents());
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Saved","The student has been saved");
            }
            else
            {
                MessageAlert.showMessage(null, Alert.AlertType.WARNING,"Warning","The id already exists");
            }

        } catch (ValidationException e) {
            MessageAlert.showMessage(null, Alert.AlertType.ERROR,"Validation Error",e.getLocalizedMessage());
        }


    }
@FXML
    public void deleteStudent(){
        String id = textID.getText();

        Student s = serviceStudents.removeStudent(id);
        if(s == null)
            MessageAlert.showMessage(null, Alert.AlertType.WARNING,"Warning","The id doesn't exists!");
        else
        {
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Done","The student has been deleted");
            serviceStudents.writeToXmlFile();
            updateList(serviceStudents.getAllStudents());
            setTextFieldsNull();
        }
    }
@FXML
    public void updateStudent(){
        String id = textID.getText();
        String firstName = textFirstName.getText();
        String lastName = textLastName.getText();
        String email = textEmail.getText();
        String teacher = textTeacher.getText();
        int group = 0;
        try{
            group = Integer.parseInt(textGroup.getText());
        }catch (NumberFormatException e){
            MessageAlert.showMessage(null, Alert.AlertType.ERROR,"NumberFormatException","The group must be a number");
            return;
        }

        try {
            Student s = serviceStudents.updateStudent(id,firstName,lastName,group,email,teacher);
            if(s==null) {
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Succes", "The student has been updated");
                serviceStudents.writeToXmlFile();
                updateList(serviceStudents.getAllStudents());
            }
            else
                MessageAlert.showMessage(null, Alert.AlertType.WARNING,"Failed","The student hasn't been updated");
        } catch (ValidationException e) {
            MessageAlert.showMessage(null, Alert.AlertType.ERROR,"Validation Error", e.getLocalizedMessage());
        }

    }

@FXML
    public void onCancel(){
        setTextFieldsNull();
    }


}
