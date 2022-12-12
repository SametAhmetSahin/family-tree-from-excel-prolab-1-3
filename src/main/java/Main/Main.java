package Main;

import Parser.ExcelParser;
import Person.*;
import Tree.Node;

import java.io.IOException;
import java.util.ArrayList;

public class Main
{
    public static void main(String[] args) throws IOException
    {
        System.out.println("Hello world!");
        System.out.println(System.getProperty("user.dir"));

        ArrayList<PersonData> test = ExcelParser.parse("test.xlsx", 0);

        System.out.println(test.get(0).id + " " + test.get(0).mother_name);

        ArrayList<Person> peeps = new ArrayList<>();

        for(PersonData data : test)
            peeps.add(new Person(data));

        Node tree = new Node();

        for(Person person : peeps)
            tree.AddPerson(person.data);

        System.out.println("Tree constructed.");
    }
}