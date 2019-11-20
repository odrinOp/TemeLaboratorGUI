package sample.interfaces;


import sample.exceptions.ValidationException;

public interface Validator<E> {
    void validate(E entity) throws ValidationException;
}
