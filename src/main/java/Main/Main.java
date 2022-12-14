package Main;

import Parser.ExcelParser;
import Person.*;
import Tree.Family;

import java.io.IOException;
import java.util.ArrayList;
import com.google.gson.*;
public class Main
{
    public static void main(String[] args) throws IOException
    {
        String filePath = "test.xlsx";

        System.out.println("Hello world!");
        System.out.println(System.getProperty("user.dir"));

        //ArrayList<PersonData> peopleList = ExcelParser.parse(filePath, 0);
        ArrayList<PersonData> peopleList = ExcelParser.parseAll(filePath);
        ArrayList<PersonData> familyRootList = ExcelParser.GetFamilyRoots(filePath);
        ArrayList<ArrayList<PersonData>> membersOfFamilies = new ArrayList<>();
        ArrayList<Family> families = new ArrayList<>();

        for(PersonData data : peopleList)
            System.out.println(data.id + " - " + data.name + " " + data.surname);

        System.out.println();

        for(int i = 0; i < ExcelParser.GetFamilyCount(filePath); i++)
        {
            families.add(new Family());
            membersOfFamilies.add(new ArrayList<>());
            membersOfFamilies.get(i).addAll(ExcelParser.parse(filePath, i));
        }

        for(int i = 0; i < families.size(); i++)
        {
            for(PersonData data : membersOfFamilies.get(i))
                families.get(i).AddPerson(data);
        }

        System.out.println("\nAğaçlar konstıraktır edildi.\n");

        for(Family family : families)
        {
            System.out.println("\n\n" + family.rootNode.data.surname + " ailesinin başı: " + family.rootNode.data.name + " " + family.rootNode.data.surname);
            //System.out.println("Eşi: " + family.rootNode.wife.data.name + " " + family.rootNode.wife.data.surname);

            family.FindPeopleWithNoChildrenRecursive(family.rootNode);

            System.out.println("\nÇocuksuzlar:");
            for(Person childlessPerson : family.peepsWithNoChildren)
                System.out.println(childlessPerson.data.id + " - " + childlessPerson.data.name + " " + childlessPerson.data.surname);
        }

        System.out.println("\n\n");

        PrintFamilyTree(families);

        System.out.println("----------------");
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        String familiesjson = "";
        familiesjson += gson.toJson(families);
        System.out.println(familiesjson);

        //PrintTreeRecursive(families.get(0).rootNode, 0);
    }

    public static void PrintFamilyTree(ArrayList<Family> families)
    {
        for(Family family : families)
        {
            PrintTreeRecursive(family.rootNode, 0);
            System.out.println();
        }
    }

    public static void PrintTreeRecursive(Person root, int generation)
    {
        for(int i = 0; i < generation; i++)
            System.out.print(i == generation - 1 ? "|-- " : i == 0 ? "|   " : "    ");

        System.out.print(root.data.id + ": " + root.data.name + " " + root.data.surname);
        if(root.wife != null)
            System.out.print(" == " + root.wife.data.id + ": " + root.wife.data.name + " " + root.wife.data.surname);
        System.out.println();

        for(Person child : root.children)
            PrintTreeRecursive(child, generation + 1);
    }
}