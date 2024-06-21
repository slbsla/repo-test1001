import java.util.*;
import java.util.stream.*;

class Contact {
    private String name;

    public Contact(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

class Person {
    private String name;
    private List<Contact> contacts;

    public Person(String name, List<Contact> contacts) {
        this.name = name;
        this.contacts = contacts;
    }

    public String getName() {
        return name;
    }

    public List<Contact> getContacts() {
        return contacts;
    }
}

public class Main {
    public static void main(String[] args) {
        List<Person> persons = Arrays.asList(
            new Person("Alice", Arrays.asList(new Contact("Bob"), new Contact("Charlie"))),
            new Person("Dave", Arrays.asList(new Contact("Eve"), new Contact("Frank"))),
            new Person("Alice", Arrays.asList(new Contact("George")))
        );

        Set<String> allNames = persons.stream()
            .flatMap(person -> {
                Stream<String> contactNames = person.getContacts().stream().map(Contact::getName);
                return Stream.concat(Stream.of(person.getName()), contactNames);
            })
            .collect(Collectors.toSet());

        System.out.println(allNames);
    }
}