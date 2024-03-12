package LambdasAndStreamAPI.hw;

/**
 * Реалзиовать методы, описанные ниже:
 */

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class Homework {

    public static void main(String[] args) {

        List<Department> departments = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            departments.add(new Department("Department #" + i));
        }

        List<Person> persons = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            persons.add(new Person(
                    "Person #" + i,
                    ThreadLocalRandom.current().nextInt(20, 61),
                    ThreadLocalRandom.current().nextInt(20_000, 100_000) * 1.0,
                    departments.get(ThreadLocalRandom.current().nextInt(departments.size()))
            ));
        }

        printNamesOrdered(persons);
        System.out.println("\n\n" + printDepartmentOldestPerson(persons));
        System.out.println("\n" + findFirstPersons(persons));
        System.out.println("\n" + findTopDepartment(persons));

    }

    /**
     * Вывести на консоль отсортированные (по алфавиту) имена персонов
     */
    public static void printNamesOrdered(List<Person> persons) {
        persons.stream()
                .map(Person::getName)
                .sorted(Comparator.naturalOrder())
                .forEach(s -> System.out.print(s + ", "));
    }

    /**
     * В каждом департаменте найти самого взрослого сотрудника.
     * Вывести на консоль мапипнг department -> personName
     * Map<Department, Person>
     */
    public static Map<Department, Person> printDepartmentOldestPerson(List<Person> persons) {
        return persons.stream()
                .collect(Collectors.groupingBy(
                        Person::getDepartment,
                        Collectors.collectingAndThen(
                                Collectors.maxBy(Comparator.comparing(Person::getAge)),
                                person -> person.orElse(null))
                ));
    }

    /**
     * Найти 10 первых сотрудников, младше 30 лет, у которых зарплата выше 50_000
     */
    public static List<Person> findFirstPersons(List<Person> persons) {
        return persons.stream()
                .filter(person -> person.getAge() < 30 && person.getSalary() > 50000)
                .sorted(Comparator.comparingDouble(Person::getSalary))
                .limit(10)
                .collect(Collectors.toList());
    }

    /**
     * Найти депаратмент, чья суммарная зарплата всех сотрудников максимальна
     */
    public static Optional<Department> findTopDepartment(List<Person> persons) {
        return persons.stream()
                .collect(Collectors.groupingBy(
                        Person::getDepartment,
                        Collectors.summingDouble(Person::getSalary)))
                .entrySet()
                .stream()
                .max(Comparator.comparingDouble(Map.Entry::getValue))
                .map(Map.Entry::getKey);
    }

}