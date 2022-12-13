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

        //System.out.println(test.get(0).id + " " + test.get(0).mother_name);

        for(PersonData data : test)
            System.out.println(data.id + " - " + data.name + " " + data.surname);

        System.out.println();

        Node tree = new Node();

        for(PersonData data : test)
            tree.AddPerson(data);

        System.out.println("\nTree constructed.\n");

        System.out.println("Ailenin başı:\n" + tree.rootNode.data.id + " - " + tree.rootNode.data.name + " " + tree.rootNode.data.surname);
        System.out.println("Eşi: " + tree.rootNode.wife.data.id + " - " + tree.rootNode.wife.data.name + " " + tree.rootNode.wife.data.surname);

        tree.FindPeopleWithNoChildrenRecursive(tree.rootNode);

        System.out.println("\nÇocuksuzlar:");
        for(Person childlessPerson : tree.peepsWithNoChildren)
            System.out.println(childlessPerson.data.id + " - " + childlessPerson.data.name + " " + childlessPerson.data.surname);
    }
}