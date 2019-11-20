package sample.services;

import sample.entities.Homework;
import sample.exceptions.ValidationException;
import sample.repositories.HomeworkRepository;

import java.io.IOException;

public class ServiceHomework {
    private HomeworkRepository repo;


    public ServiceHomework(HomeworkRepository repo) {
        this.repo = repo;

    }


    /**
     *
     * @param id - the id of the homework
     * @param description - the description of the homework
     * @param startWeek - the week in which the homework has been added.
     * @param deadlineWeek - the deadline week for the homework
     *
     * @return null- the homework will be saved
     *          otherwise will return the homework(id already exists)
     *
     *
     * @throws ValidationException - the homework is not valid
     */
    public Homework addHomework(String id, String description, int startWeek, int deadlineWeek) throws ValidationException {

        Homework hw = new Homework(id,description,startWeek,deadlineWeek);
        return repo.save(hw);
    }

    /**
     *
     * @param id
     * @return null - the id doesn't exists.
     *          otherwise return the deleted homework.
     */
    public Homework deleteHomework(String id){
        return repo.delete(id);
    }

    /**
     *
     * @param id
     * @param new_description
     * @param new_deadlineWeek
     *
     * @return null - the Homework was updated
     *          otherwise return the homework( id doesn't exists)
     *
     * @throws ValidationException -- the new homework is not valid.
     */
    public Homework updateHomework(String id,String new_description, int new_deadlineWeek) throws ValidationException {

        Homework h = repo.findOne(id);
        if(h == null)
            throw new IllegalArgumentException("The homework can't be found");


        Homework hw = new Homework(id,new_description,h.getStartWeek(),new_deadlineWeek);
        return repo.update(hw);
    }

    /**
     *
     * @return all the homework
     */
    public Iterable<Homework> getAllHomework(){
        return repo.findAll();
    }

    /**
     *
     * @param id
     * @return null- the homework doesn't exists
     *          otherwise the homework with specific id.
     */
    public Homework findHomework(String id){
        return repo.findOne(id);
    }


    public void readFromFile() throws IOException {
        String file = "src/sample/files/teme.csv";
        repo.readFromFile(file);
    }

    public void writeToFile() throws IOException {
        repo.writeToFile("src/sample/files/teme.csv");
    }

    public void readFromXmlFile(){
        String file = "src/sample/files/xmlFiles/teme.xml";
        repo.readFromXmlFile(file,"tema");
    }

    public void writeToXmlFile(){
        String file = "src/sample/files/xmlFiles/teme.xml";
        repo.writeToXmlFile(file,"teme","tema");
    }

}
