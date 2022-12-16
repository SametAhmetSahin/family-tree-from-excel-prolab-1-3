package Main;

import Parser.ExcelParser;
import Person.*;
import Tree.Family;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import Tree.GodotFamily;
import com.google.gson.*;

public class Main
{
    static String filePath = "test.xlsx";
    static Scanner input = new Scanner(System.in);

    public static ArrayList<PersonData> peopleList = new ArrayList<>();
    public static ArrayList<ArrayList<PersonData>> membersOfFamilies = new ArrayList<>();
    public static ArrayList<Family> families = new ArrayList<>();
    public static ArrayList<GodotFamily> godotFamilies = new ArrayList<>();
    public static ArrayList<Integer> peopleWithNoChildren = new ArrayList<>();

    public static void main(String[] args) throws IOException
    {
        peopleList = ExcelParser.parseAll(filePath);

        for(int i = 0; i < ExcelParser.GetFamilyCount(filePath); i++)
        {
            families.add(new Family(i));
            godotFamilies.add(new GodotFamily(i));
            membersOfFamilies.add(new ArrayList<>());
            membersOfFamilies.get(i).addAll(ExcelParser.parse(filePath, i));
        }

        for(int i = 0; i < families.size(); i++)
        {
            for(PersonData data : membersOfFamilies.get(i))
                families.get(i).AddPerson(data);

            families.get(i).ValidateFamily();
            godotFamilies.get(i).AddPerson(families.get(i).rootNode);
        }

        System.out.println("\nSoy ağaçları oluşturuldu.");

        PrintFamilyTreeToConsole(families);

        System.out.println("Ailelerin JSON formatındaki verisi:");
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        String familiesjson = "";
        familiesjson += gson.toJson(godotFamilies);
        System.out.println(familiesjson);
        System.out.println("\n----------------------------------------------------------------------------------------\n");

        FamilyTreeMenu();
    }

    public static void FamilyTreeMenu()
    {
        while(true)
        {
            RenderMenu();
            String selection = input.nextLine();
            System.out.println();

            switch (selection)
            {
                case "0" -> {
                    System.out.println("Sistemden çıkış yapılıyor...");
                    return;
                }
                case "1" -> Menu_FindPeopleWithNoChildren();

                default -> {
                    System.out.println(",------------------------------------------,");
                    System.out.println("| Seçiminiz hatalı, lütfen tekrar deneyin. |");
                    System.out.println("'------------------------------------------'\n");
                }
            }
        }
    }

    public static void RenderMenu()
    {
        System.out.println("YILDIZsoft Soy Ağacı Sistemi\n");
        System.out.println("1) Çocuğu olmayanları bul.");
        System.out.println("2) Üvey kardeşleri bul.");
        System.out.println("3) Belirtilen kan grubuna sahip kişileri bul.");
        System.out.println("4) Ata mesleğini devam ettiren kişileri bul.");
        System.out.println("5) Aynı isme sahip kişileri bul.");
        System.out.println("6) Belirtilen iki kişinin birbirine yakınlığını bul.");
        System.out.println("7) Belirtilen kişinin soy ağacını göster.");
        System.out.println("8) Soy ağacının kaç nesilden oluştuğunu hesapla.");
        System.out.println("9) Belitilen kişiden sonra kaç nesil geldiğini hesapla.\n");
        System.out.println("0) Çıkış\n");
        System.out.print("Seçiminiz: ");
    }

    public static void Menu_FindPeopleWithNoChildren()
    {
        System.out.println("\n-----  Çocuğu Olmayanları Bul  -----\n");

        peopleWithNoChildren.clear();

        System.out.println("Mevcut liste temizlendi, yeni liste oluşturuluyor...\n");

        for(Family family : families)
            FindPeopleWithNoChildrenRecursive(family.rootNode);

        for(int i = 0; i < peopleWithNoChildren.size() - 1; i++)
        {
            int index = i;

            for(int j = i + 1; j < peopleWithNoChildren.size(); j++)
            {
                PersonData thePerson = GetPersonFromID(peopleWithNoChildren.get(index));
                String[] theData = thePerson.birthdate.split("[-/.]");
                int theYear = Integer.parseInt(theData[theData.length - 1]);

                PersonData theOtherPerson = GetPersonFromID(peopleWithNoChildren.get(j));
                String[] theOtherData = theOtherPerson.birthdate.split("[-/.]");
                int theOtherYear = Integer.parseInt(theOtherData[theOtherData.length - 1]);

                if(theYear > theOtherYear)
                    index = j;
            }

            Integer temp = peopleWithNoChildren.get(i);
            peopleWithNoChildren.set(i, peopleWithNoChildren.get(index));
            peopleWithNoChildren.set(index, temp);

            System.out.print((i + 1) + ". adım sonunda liste: [");
            for(int j = 0; j < peopleWithNoChildren.size(); j++)
                System.out.print(peopleWithNoChildren.get(j) + ": " + GetPersonFromID(peopleWithNoChildren.get(j)).name + " " + GetPersonFromID(peopleWithNoChildren.get(j)).surname
                                 + (j == peopleWithNoChildren.size() - 1 ? "]\n" : ", "));
        }

        System.out.print("\nÇocuğu olmayan kişiler (Yaş sıralaması: büyükten küçüğe)\n");
        for (Integer personID : peopleWithNoChildren)
            System.out.println(personID + ": " + GetPersonFromID(personID).name + " " + GetPersonFromID(personID).surname);

        System.out.println("\nDevam etmek için ENTER'a basın...\n----------------------------------------------------------------------------\n");
        input.nextLine();
    }

    public static void FindPeopleWithNoChildrenRecursive(Person root)
    {
        if(root.children.isEmpty() && !peopleWithNoChildren.contains(root.data.id))
            peopleWithNoChildren.add(root.data.id);

        else
            for(Person person : root.children)
                FindPeopleWithNoChildrenRecursive(person);
    }

    public static PersonData GetPersonFromID(int ID)
    {
        for(PersonData data : peopleList)
            if(data.id == ID)
                return data;

        return null;
    }

    public static void PrintFamilyTreeToConsole(ArrayList<Family> families)
    {
        System.out.println("\n----------------------------------------------------------------------------------------\n");

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
        if(root.spouse != null)
        {
            System.out.print((root.data.maritalStatus.equalsIgnoreCase("evli") ? " === " : " =X= ") + root.spouse.data.id + ": " + root.spouse.data.name + " " + root.spouse.data.surname);
        }

        System.out.println();

        for(int i = 0; i < root.children.size(); i++)
            PrintTreeRecursive(root.children.get(i), generation + 1, i, root.children.size() - 1, isFinalChild);
    }
}