package Main;

import Parser.ExcelParser;
import Person.*;
import Tree.Family;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import com.google.gson.*;

public class Main
{
    public static void main(String[] args) throws IOException
    {
        String filePath = "test.xlsx";

        System.out.println("YILDIZsoft Soy Ağacı Sistemi\n");
        //System.out.println(System.getProperty("user.dir"));

        ArrayList<PersonData> peopleList = ExcelParser.parseAll(filePath);
        ArrayList<ArrayList<PersonData>> membersOfFamilies = new ArrayList<>();
        ArrayList<Family> families = new ArrayList<>();

        /*for(PersonData data : peopleList)
            System.out.println(data.id + " - " + data.name + " " + data.surname);

        System.out.println();*/

        for(int i = 0; i < ExcelParser.GetFamilyCount(filePath); i++)
        {
            families.add(new Family(i));
            membersOfFamilies.add(new ArrayList<>());
            membersOfFamilies.get(i).addAll(ExcelParser.parse(filePath, i));
        }

        for(int i = 0; i < families.size(); i++)
        {
            for(PersonData data : membersOfFamilies.get(i))
                families.get(i).AddPerson(data);

            families.get(i).ValidateFamily(peopleList);
        }

        System.out.println("\nSoy ağaçları oluşturuldu.\n");

        /*
        for(Family family : families)
        {
            System.out.println("\n\n" + family.rootNode.data.surname + " ailesinin başı: " + family.rootNode.data.name + " " + family.rootNode.data.surname);
            System.out.println("Eşi: " + family.rootNode.wife.data.name + " " + family.rootNode.wife.data.surname);

            family.FindPeopleWithNoChildrenRecursive(family.rootNode);

            System.out.println("\nÇocuksuzlar:");
            for(Person childlessPerson : family.peepsWithNoChildren)
                System.out.println(childlessPerson.data.id + " - " + childlessPerson.data.name + " " + childlessPerson.data.surname);
        }*/

        System.out.println("\n----------------------------------------------------------------------------------------\n");

        PrintFamilyTreeToConsole(families);

        System.out.println("Ailelerin JSON formatındaki verisi:");
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        String familiesjson = "";
        familiesjson += gson.toJson(families);
        System.out.println(familiesjson);

        System.out.println("\n----------------------------------------------------------------------------------------\n");
    }

    public static void PrintFamilyTreeToConsole(ArrayList<Family> families)
    {
        for(Family family : families)
        {
            PrintTreeRecursive(family.rootNode, 0, 0, family.rootNode.children.size() - 1, false);
            System.out.println("\n----------------------------------------------------------------------------------------\n");
        }
    }

    public static void PrintTreeRecursive(Person root, int generation, int childIndex, int childCount, boolean isFinalChild)
    {
        for(int i = 0; i < generation; i++)
        {
            if(childIndex >= childCount && i == generation - 1)
                System.out.print(" '-- ");

            else if(i == generation - 1 && childIndex < childCount)
                System.out.print(" |-- ");

            else if(i < generation - 1 && !isFinalChild)
            {
                System.out.print(" |   ");
            }
            else
                System.out.print("     ");
        }

        isFinalChild = childIndex >= childCount;

        System.out.print(root.data.id + ": " + root.data.name + " " + root.data.surname);
        if(root.wife != null)
        {
            System.out.print((root.data.marital_status.equalsIgnoreCase("evli") ? " === " : " =X= ") + root.wife.data.id + ": " + root.wife.data.name + " " + root.wife.data.surname);
        }

        System.out.println();

        for(int i = 0; i < root.children.size(); i++)
            PrintTreeRecursive(root.children.get(i), generation + 1, i, root.children.size() - 1, isFinalChild);
    }
}