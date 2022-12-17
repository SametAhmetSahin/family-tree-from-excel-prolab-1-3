package Main;

import Parser.ExcelParser;
import Person.*;
import Tree.*;
import GodotData.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import Tree.GodotFamily;
import com.google.gson.*;

public class Main
{
    static String filePath = "test.xlsx";
    static Scanner input = new Scanner(System.in);

    public static GodotData godotData = new GodotData();
    public static ArrayList<PersonData> peopleList = new ArrayList<>();
    public static ArrayList<ArrayList<PersonData>> membersOfFamilies = new ArrayList<>();
    public static ArrayList<Family> families = new ArrayList<>();
    public static ArrayList<GodotFamily> godotFamilies = new ArrayList<>();
    public static int tempGenerationCounter = 0;

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

        //System.out.println("\nSoy ağaçları oluşturuldu.");

        //PrintFamilyTreeToConsole(families);

        /*
        System.out.println("Ailelerin JSON formatındaki verisi:");
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        String familiesjson = "";
        familiesjson += gson.toJson(godotFamilies);
        System.out.println(familiesjson);
        System.out.println("\n----------------------------------------------------------------------------------------\n");*/

        FamilyTreeMenu();

        //System.out.println(GetGodotData());
    }

    public static String GetGodotTree()
    {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        return "" + gson.toJson(godotFamilies);
    }

    public static String GetGodotData()
    {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        return "" + gson.toJson(godotData);
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
                case "3" -> Menu_FindPeopleWithSpecificBloodType();
                case "8" -> Menu_CalculateGenerationCount();

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

    public static void Menu_RenderBloodTypeMenu()
    {
        System.out.println("Aramak istediğiniz kan grubunu seçin.\n");
        System.out.println("1) 0(-)");
        System.out.println("2) 0(+)");
        System.out.println("3) A(-)");
        System.out.println("4) A(+)");
        System.out.println("5) B(-)");
        System.out.println("6) B(+)");
        System.out.println("7) AB(-)");
        System.out.println("8) AB(+)\n");
        System.out.println("0) İptal\n");
        System.out.print("Seçiminiz: ");
    }

    public static void Menu_FindPeopleWithSpecificBloodType()
    {
        System.out.println("\n-----  Belirtilen Kan Grubuna Sahip Kişileri Bul  -----\n");

        String selection, wantedBloodType = "";
        boolean illegal;

        do
        {
            Menu_RenderBloodTypeMenu();
            selection = input.nextLine();

            illegal = !(selection.equals("1") || selection.equals("2") || selection.equals("3") || selection.equals("4") || selection.equals("5") || selection.equals("6") || selection.equals("7") || selection.equals("8") || selection.equals("0"));

            if(illegal)
            {
                System.out.println(",------------------------------------------,");
                System.out.println("| Seçiminiz hatalı, lütfen tekrar deneyin. |");
                System.out.println("'------------------------------------------'\n");
            }
        } while (illegal);

        switch(selection)
        {
            case "0" -> {
                System.out.println("\nİşlem iptal edildi, ana menüye dönülüyor...\n----------------------------------------------------------------------------\n");
                return;
            }
            case "1" -> wantedBloodType = "0(-)";
            case "2" -> wantedBloodType = "0(+)";
            case "3" -> wantedBloodType = "A(-)";
            case "4" -> wantedBloodType = "A(+)";
            case "5" -> wantedBloodType = "B(-)";
            case "6" -> wantedBloodType = "B(+)";
            case "7" -> wantedBloodType = "AB(-)";
            case "8" -> wantedBloodType = "AB(+)";
        }

        godotData.peopleIDsWithSpecificBlood.clear();

        for(Family family : families)
            FindPeopleWithSpecificBloodTypeRecursive(family.rootNode, wantedBloodType);

        System.out.println();

        if(godotData.peopleIDsWithSpecificBlood.isEmpty())
            System.out.println("Kan grubu " + wantedBloodType + " olan herhangi bir kişi yoktur.");
        else
        {
            System.out.println("Kan grubu " + wantedBloodType + " olan kişiler:");
            for(Integer personID : godotData.peopleIDsWithSpecificBlood)
                System.out.println(personID + ": " + GetPersonFromID(personID).name + " " + GetPersonFromID(personID).surname);
        }

        System.out.println("\nDevam etmek için ENTER'a basın...\n----------------------------------------------------------------------------\n");
        input.nextLine();
    }

    public static void FindPeopleWithSpecificBloodTypeRecursive(Person root, String wantedBloodType)
    {
        if(root.data.bloodType.equalsIgnoreCase(wantedBloodType) && !godotData.peopleIDsWithSpecificBlood.contains(root.data.id))
            godotData.peopleIDsWithSpecificBlood.add(root.data.id);

        for(Person person : root.children)
            FindPeopleWithSpecificBloodTypeRecursive(person, wantedBloodType);
    }

    public static void Menu_CalculateGenerationCount()
    {
        System.out.println("\n-----  Soy Ağacının Kaç Nesilden Oluştuğunu Hesapla  -----\n");

        godotData.generationCounts.clear();

        for(Family family : families)
        {
            tempGenerationCounter = 0;
            CalculateGenerationCountRecursive(family.rootNode, 1);
            godotData.generationCounts.add(tempGenerationCounter);
        }

        for(int i = 0; i < godotData.generationCounts.size(); i++)
            System.out.println((i + 1) + ". soy ağacı " + godotData.generationCounts.get(i) + " nesilden oluşmaktadır.");

        System.out.println("\nDevam etmek için ENTER'a basın...\n----------------------------------------------------------------------------\n");
        input.nextLine();
    }

    public static void CalculateGenerationCountRecursive(Person root, int currentGen)
    {
        if(currentGen > tempGenerationCounter)
            tempGenerationCounter = currentGen;

        for(Person person : root.children)
            CalculateGenerationCountRecursive(person, currentGen + 1);
    }

    public static void Menu_FindPeopleWithNoChildren()
    {
        System.out.println("\n-----  Çocuğu Olmayanları Bul  -----\n");

        godotData.peopleIDsWithNoChildren.clear();

        System.out.println("Mevcut liste temizlendi, yeni liste oluşturuluyor...");

        for(Family family : families)
            FindPeopleWithNoChildrenRecursive(family.rootNode);

        System.out.println("Liste oluşturuldu.\n");

        if(godotData.peopleIDsWithNoChildren.isEmpty())
            System.out.println("Çocuğu olmayan herhangi bir kişi yoktur.");
        else
        {
            System.out.print("Sıralanmamış liste: [");
            for (int j = 0; j < godotData.peopleIDsWithNoChildren.size(); j++)
                System.out.print(godotData.peopleIDsWithNoChildren.get(j) + ": " + GetPersonFromID(godotData.peopleIDsWithNoChildren.get(j)).name + " " + GetPersonFromID(godotData.peopleIDsWithNoChildren.get(j)).surname
                        + (j == godotData.peopleIDsWithNoChildren.size() - 1 ? "]\n\n" : ", "));

            System.out.println("Sıralama başlıyor... (Sıralama için kullanılan algoritma: Selection sort)");

            for (int i = 0; i < godotData.peopleIDsWithNoChildren.size() - 1; i++) {
                int index = i;

                for (int j = i + 1; j < godotData.peopleIDsWithNoChildren.size(); j++) {
                    // Birinci kişinin doğum yılı bilgisi alınıyor.
                    PersonData thePerson = GetPersonFromID(godotData.peopleIDsWithNoChildren.get(index));
                    String[] theData = thePerson.birthdate.split("[-/.]");
                    int theYear = Integer.parseInt(theData[theData.length - 1]);

                    // İkinci kişinin doğum yılı bilgisi alınıyor.
                    PersonData theOtherPerson = GetPersonFromID(godotData.peopleIDsWithNoChildren.get(j));
                    String[] theOtherData = theOtherPerson.birthdate.split("[-/.]");
                    int theOtherYear = Integer.parseInt(theOtherData[theOtherData.length - 1]);

                    if (theYear > theOtherYear)
                        index = j;
                }

                Integer temp = godotData.peopleIDsWithNoChildren.get(i);
                godotData.peopleIDsWithNoChildren.set(i, godotData.peopleIDsWithNoChildren.get(index));
                godotData.peopleIDsWithNoChildren.set(index, temp);

                System.out.print((i + 1) + ". adım sonunda liste: [");
                for (int j = 0; j < godotData.peopleIDsWithNoChildren.size(); j++)
                    System.out.print(godotData.peopleIDsWithNoChildren.get(j) + ": " + GetPersonFromID(godotData.peopleIDsWithNoChildren.get(j)).name + " " + GetPersonFromID(godotData.peopleIDsWithNoChildren.get(j)).surname
                            + (j == godotData.peopleIDsWithNoChildren.size() - 1 ? "]\n" : ", "));
            }
            System.out.println("Sıralama tamamlandı.");

            System.out.print("\nÇocuğu olmayan kişiler (Yaş sıralaması: büyükten küçüğe)\n");
            for (Integer personID : godotData.peopleIDsWithNoChildren)
                System.out.println(personID + ": " + GetPersonFromID(personID).name + " " + GetPersonFromID(personID).surname);
        }

        System.out.println("\nDevam etmek için ENTER'a basın...\n----------------------------------------------------------------------------\n");
        input.nextLine();
    }

    public static void FindPeopleWithNoChildrenRecursive(Person root)
    {
        if(root.children.isEmpty() && !godotData.peopleIDsWithNoChildren.contains(root.data.id))
            godotData.peopleIDsWithNoChildren.add(root.data.id);

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