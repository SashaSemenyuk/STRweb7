package com.example.servlets.model;


import java.util.ArrayList;
import java.util.List;

public class ListService {
    private static List<Person> groupList = new ArrayList();

    static {
        groupList.add(new Person("Anna", "+375291234567", "anna.1.18@gmail.com"));
        groupList.add(new Person("Ivan", "+375331114534", "ivan.1.18@gmail.com"));
        groupList.add(new Person("Nikolai", "+3752998734534", "nik.1.18@gmail.com"));
    }

    static public List<Person> retrieveList() {
        return groupList;
    }

    static public void addPerson(Person person) {
        groupList.add(new Person(person));
    }

}
