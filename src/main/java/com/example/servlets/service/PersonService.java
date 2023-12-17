package com.example.servlets.service;

import com.example.servlets.exception.RepositoryException;
import com.example.servlets.exception.ServiceException;
import com.example.servlets.model.Person;
import com.example.servlets.repository.specification.PersonRepository;
import com.example.servlets.repository.specification.RepositoryCreator;

import java.util.List;


public class PersonService {
    public List<Person> findAll() throws ServiceException {
        try (RepositoryCreator repositoryCreator = new RepositoryCreator()) {
            PersonRepository personRepository = repositoryCreator.getPersonRepository();
            return personRepository.findAll();
        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    public void save(Person person) throws ServiceException {
        try (RepositoryCreator repositoryCreator = new RepositoryCreator()) {
            PersonRepository personRepository = repositoryCreator.getPersonRepository();
            personRepository.save(person);
        } catch (RepositoryException exception) {
            throw new ServiceException(exception.getMessage(), exception);
        }
    }
}
