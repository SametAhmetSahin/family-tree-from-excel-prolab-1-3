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

import WebServer.*;
import com.google.gson.annotations.Expose;

public class Main
{
    static String filePath = "test.xlsx";
    static Scanner input = new Scanner(System.in);

    @Expose
    public static ArrayList<PersonData> peopleList;
    public static GodotData godotData = new GodotData();
    public static ArrayList<GodotFamily> godotFamilies = new ArrayList<>();
    public static int tempGenerationCounter = 0;
    public static Person tempPerson;

    public static void main(String[] args) throws IOException
    {
        int port = 8080;

        WebServer server = new WebServer();
        new Thread(() -> {
            try {
                server.Start(port);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();

        peopleList = ExcelParser.parseAll(filePath);
        ArrayList<ArrayList<PersonData>> membersOfFamilies = new ArrayList<>();
        ArrayList<Family> families = new ArrayList<>();

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
                families.get(i).AddPerson(data, peopleList);

            families.get(i).ValidateFamily(peopleList);
            godotFamilies.get(i).AddPerson(families.get(i).rootNode);
        }

        System.out.println();

        FamilyTreeMenu(families, peopleList);
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

    public static String GetGodotPersonData()
    {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        return "" + gson.toJson(peopleList);
    }

    public static void FamilyTreeMenu(ArrayList<Family> families, ArrayList<PersonData> personData)
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
                    System.exit(0);
                }
                case "1" -> Menu_FindPeopleWithNoChildren(families, personData);
                case "3" -> Menu_FindPeopleWithSpecificBloodType(families, personData);
                case "4" -> Menu_FindContinuedProfessions(families, personData);
                case "7" -> Menu_ShowFamilyTreeOfSpecificPerson(families, personData);
                case "8" -> Menu_CalculateGenerationCount(families);
                case "9" -> Menu_CalculateGenerationsAfterPerson(families);

                default -> {
                    System.out.println(".------------------------------------------.");
                    System.out.println("| Seçiminiz hatalı, lütfen tekrar deneyin. |");
                    System.out.println("'------------------------------------------'\n");
                }
            }

            System.gc();
        }
    }

    public static void RenderMenu()
    {
        System.out.println("YILDIZsoft Soy Ağacı Sistemi\n");
        System.out.println("1) Çocuğu olmayanları bul.");   // Tamamlandı
        System.out.println("2) Üvey kardeşleri bul.");
        System.out.println("3) Belirtilen kan grubuna sahip kişileri bul.");    // Tamamlandı
        System.out.println("4) Ata mesleğini devam ettiren kişileri bul.");     // Tamamlandı
        System.out.println("5) Aynı isme sahip kişileri bul.");
        System.out.println("6) Belirtilen iki kişinin birbirine yakınlığını bul.");
        System.out.println("7) Belirtilen kişinin soy ağacını göster.");    // Tamamlandı
        System.out.println("8) Soy ağacının kaç nesilden oluştuğunu hesapla."); // Tamamlandı
        System.out.println("9) Belirtilen kişiden sonra kaç nesil geldiğini hesapla.\n");   // Tamamlandı
        System.out.println("0) Çıkış\n");
        System.out.print("Seçiminiz: ");
    }

    public static void Menu_FindContinuedProfessions(ArrayList<Family> families, ArrayList<PersonData> personData)
    {
        System.out.println("\n-----  Ata Mesleğini Devam Ettiren Kişileri Bul  -----\n");
        godotData.continuedProfessions.clear();

        for(Family family : families)
            FindContinuedProfessionsRecursive(family.rootNode);

        System.out.println("Ata mesleğini devam ettiren kişiler:");
        for(Integer[] data : godotData.continuedProfessions)
        {
            switch(data[1])
            {
                case 1 -> System.out.println(GetPersonDataFromID(data[0], personData).id + ": " + GetPersonDataFromID(data[0], personData).name + " " + GetPersonDataFromID(data[0], personData).surname + " babasının mesleğini devam ettiriyor.");
                case 2 -> System.out.println(GetPersonDataFromID(data[0], personData).id + ": " + GetPersonDataFromID(data[0], personData).name + " " + GetPersonDataFromID(data[0], personData).surname + " dedesinin mesleğini devam ettiriyor.");
                case 3 -> System.out.println(GetPersonDataFromID(data[0], personData).id + ": " + GetPersonDataFromID(data[0], personData).name + " " + GetPersonDataFromID(data[0], personData).surname + " hem babasının hem de dedesinin mesleğini devam ettiriyor.");
            }
        }

        System.out.println("\nDevam etmek için ENTER'a basın...\n----------------------------------------------------------------------------\n");
        input.nextLine();
    }

    public static void FindContinuedProfessionsRecursive(Person root)
    {
        if(root.father != null)
        {
            if (root.father.father != null)
            {
                if (root.data.profession.equalsIgnoreCase(root.father.data.profession) &&
                        root.data.profession.equalsIgnoreCase(root.father.father.data.profession))
                    godotData.continuedProfessions.add(new Integer[]{root.data.id, 3});

                else if(root.data.profession.equalsIgnoreCase(root.father.father.data.profession))
                    godotData.continuedProfessions.add(new Integer[]{root.data.id, 2});

                else if (root.data.profession.equalsIgnoreCase(root.father.data.profession))
                    godotData.continuedProfessions.add(new Integer[]{root.data.id, 1});
            }
            else
                if (root.data.profession.equalsIgnoreCase(root.father.data.profession))
                    godotData.continuedProfessions.add(new Integer[]{root.data.id, 1});
        }

        for(Person child : root.children)
            FindContinuedProfessionsRecursive(child);
    }

    public static void Menu_ShowFamilyTreeOfSpecificPerson(ArrayList<Family> families, ArrayList<PersonData> personData)
    {
        System.out.println("\n-----  Belirtilen Kişinin Soy Ağacını Göster  -----\n");

        System.out.print("Kişinin ID'sini giriniz: ");
        int wantedID = input.nextInt();

        Family wantedFamily = new Family(0);

        Person aPerson = GetPersonFromID(wantedID, families);
        Person wantedPerson = new Person(GetPersonDataFromID(wantedID, personData), aPerson.mother, aPerson.father, aPerson.spouse, aPerson.children);

        AddPersonToFamilyRecursive(wantedPerson, wantedFamily, personData);
        wantedFamily.ValidateFamily(personData);
        SetRelationOfPersonRecursive(wantedFamily.rootNode, "", false, false, true, personData);

        godotData.familyTreeOfSpecificPerson.AddPerson(wantedFamily.rootNode);

        System.out.println("\n" + wantedPerson.data.name + " " + wantedPerson.data.surname + " kişisinin soy ağacı oluşturuldu.");

        PrintTreeRecursive(wantedFamily.rootNode, 0, 0, wantedFamily.rootNode.children.size() - 1, false);
        System.out.println("\n" + GetGodotData() + "\n");

        System.out.println("\nDevam etmek için ENTER'a basın...\n----------------------------------------------------------------------------\n");
        input.nextLine();
        input.nextLine();
    }

    public static void AddPersonToFamilyRecursive(Person root, Family familyToAdded, ArrayList<PersonData> personData)
    {
        if(root == null)
            return;

        familyToAdded.AddPerson(root.data, personData);

        if(root.spouse != null)
            familyToAdded.AddPerson(root.spouse.data, personData);

        for(Person child : root.children)
            AddPersonToFamilyRecursive(child, familyToAdded, personData);
    }

    public static void SetRelationOfPersonRecursive(Person somePerson, String relation, boolean isNotStart, boolean upward, boolean checkSpouse, ArrayList<PersonData> personData)
    {
        for(PersonData deyta : personData)
            if(deyta.id == somePerson.data.id)
            {
                somePerson.data.surname = deyta.surname;
                break;
            }

        if(isNotStart)
            somePerson.data.surname += " (";
        else
            somePerson.data.surname += " ";

        somePerson.data.surname += relation;

        String temp = relation;

        if(!upward)
        {
            if (somePerson.spouse != null && checkSpouse)
            {
                if (relation.endsWith("ı") || relation.endsWith("a"))
                    relation += "nın ";
                else if (relation.endsWith("i") || relation.endsWith("e"))
                    relation += "nin ";
                else if (relation.endsWith("u") || relation.endsWith("o"))
                    relation += "nun ";
                else if (relation.endsWith("ü") || relation.endsWith("ö"))
                    relation += "nün ";

                SetRelationOfPersonRecursive(somePerson.spouse, relation + (somePerson.data.maritalStatus.equalsIgnoreCase("Evli") ? "eşi" : "eski eşi"), true, false, false, personData);
            }

            relation = temp;

            if (!somePerson.children.isEmpty() && checkSpouse)
            {
                if (relation.endsWith("ı") || relation.endsWith("a"))
                    relation += "nın ";
                else if (relation.endsWith("i") || relation.endsWith("e"))
                    relation += "nin ";
                else if (relation.endsWith("u") || relation.endsWith("o"))
                    relation += "nun ";
                else if (relation.endsWith("ü") || relation.endsWith("ö"))
                    relation += "nün ";

                for (Person child : somePerson.children)
                    SetRelationOfPersonRecursive(child, relation + (child.data.gender ? "oğlu" : "kızı"), true, false, true, personData);
            }
        }
        else
        {
            if (somePerson.spouse != null && checkSpouse)
            {
                if (relation.endsWith("ı") || relation.endsWith("a"))
                    relation += "nın ";
                else if (relation.endsWith("i") || relation.endsWith("e"))
                    relation += "nin ";
                else if (relation.endsWith("u") || relation.endsWith("o"))
                    relation += "nun ";
                else if (relation.endsWith("ü") || relation.endsWith("ö"))
                    relation += "nün ";

                SetRelationOfPersonRecursive(somePerson.spouse, relation + (somePerson.data.maritalStatus.equalsIgnoreCase("Evli") ? "eşi" : "eski eşi"), true, true, false, personData);
            }

            relation = temp;

            if (somePerson.mother != null)
            {
                if (relation.endsWith("ı") || relation.endsWith("a"))
                    relation += "nın ";
                else if (relation.endsWith("i") || relation.endsWith("e"))
                    relation += "nin ";
                else if (relation.endsWith("u") || relation.endsWith("o"))
                    relation += "nun ";
                else if (relation.endsWith("ü") || relation.endsWith("ö"))
                    relation += "nün ";

                SetRelationOfPersonRecursive(somePerson.mother, relation + "annesi", true, true, true, personData);
            }

            relation = temp;

            if (somePerson.father != null && checkSpouse)
            {
                if (relation.endsWith("ı") || relation.endsWith("a"))
                    relation += "nın ";
                else if (relation.endsWith("i") || relation.endsWith("e"))
                    relation += "nin ";
                else if (relation.endsWith("u") || relation.endsWith("o"))
                    relation += "nun ";
                else if (relation.endsWith("ü") || relation.endsWith("ö"))
                    relation += "nün ";

                SetRelationOfPersonRecursive(somePerson.father, relation + "babası", true, true, false, personData);
            }
        }

        if(isNotStart)
            somePerson.data.surname += ")";

        somePerson.data.surname = somePerson.data.surname.trim();
    }

    public static void Menu_CalculateGenerationsAfterPerson(ArrayList<Family> families)
    {
        System.out.println("\n-----  Belirtilen Kişiden Sonra Kaç Nesil Geldiğini Hesapla  -----\n");

        System.out.print("Kişinin ID'sini giriniz: ");
        int wantedID = input.nextInt();
        Person wantedPerson = GetPersonFromID(wantedID, families);

        tempGenerationCounter = 0;
        CalculateGenerationCountRecursive(wantedPerson, 0);

        godotData.generationCountAfterPerson[0] = wantedID;
        godotData.generationCountAfterPerson[1] = tempGenerationCounter;

        System.out.println(wantedPerson.data.name + " " + wantedPerson.data.surname + " kişisinden sonra " + (tempGenerationCounter == 0 ? "nesil gelmemektedir." : tempGenerationCounter + " nesil gelmektedir."));

        System.out.println("\nDevam etmek için ENTER'a basın...\n----------------------------------------------------------------------------\n");
        input.nextLine();
        input.nextLine();
    }

    public static Person GetPersonFromID(int ID, ArrayList<Family> families)
    {
        tempPerson = null;

        for(Family family : families)
            FindPersonRecursive(family.rootNode, ID);

        return tempPerson;
    }

    public static void FindPersonRecursive(Person root, int ID)
    {
        if(root.data.id == ID)
            tempPerson = root;

        else
        {
            if(root.spouse != null)
                if(root.spouse.data.id == ID)
                {
                    tempPerson = root.spouse;
                    return;
                }

            for (Person child : root.children)
                FindPersonRecursive(child, ID);
        }
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

    public static void Menu_FindPeopleWithSpecificBloodType(ArrayList<Family> families, ArrayList<PersonData> personData)
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
                System.out.println(".------------------------------------------.");
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
                System.out.println(personID + ": " + GetPersonDataFromID(personID, personData).name + " " + GetPersonDataFromID(personID, personData).surname);
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

    public static void Menu_CalculateGenerationCount(ArrayList<Family> families)
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

    public static void Menu_FindPeopleWithNoChildren(ArrayList<Family> families, ArrayList<PersonData> personData)
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
                System.out.print(godotData.peopleIDsWithNoChildren.get(j) + ": " + GetPersonDataFromID(godotData.peopleIDsWithNoChildren.get(j), personData).name + " " + GetPersonDataFromID(godotData.peopleIDsWithNoChildren.get(j), personData).surname
                        + (j == godotData.peopleIDsWithNoChildren.size() - 1 ? "]\n\n" : ", "));

            System.out.println("Sıralama başlıyor... (Sıralama için kullanılan algoritma: Selection sort)");

            for (int i = 0; i < godotData.peopleIDsWithNoChildren.size() - 1; i++)
            {
                int index = i;

                for (int j = i + 1; j < godotData.peopleIDsWithNoChildren.size(); j++)
                {
                    // Birinci kişinin doğum yılı bilgisi alınıyor.
                    PersonData thePerson = GetPersonDataFromID(godotData.peopleIDsWithNoChildren.get(index), personData);
                    String[] theData = thePerson.birthdate.split("[-/.]");
                    int theYear = Integer.parseInt(theData[theData.length - 1]);

                    // İkinci kişinin doğum yılı bilgisi alınıyor.
                    PersonData theOtherPerson = GetPersonDataFromID(godotData.peopleIDsWithNoChildren.get(j), personData);
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
                    System.out.print(godotData.peopleIDsWithNoChildren.get(j) + ": " + GetPersonDataFromID(godotData.peopleIDsWithNoChildren.get(j), personData).name + " " + GetPersonDataFromID(godotData.peopleIDsWithNoChildren.get(j), personData).surname
                            + (j == godotData.peopleIDsWithNoChildren.size() - 1 ? "]\n" : ", "));
            }
            System.out.println("Sıralama tamamlandı.");

            System.out.print("\nÇocuğu olmayan kişiler (Yaş sıralaması: büyükten küçüğe)\n");
            for (Integer personID : godotData.peopleIDsWithNoChildren)
                System.out.println(personID + ": " + GetPersonDataFromID(personID, personData).name + " " + GetPersonDataFromID(personID, personData).surname);
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

    public static PersonData GetPersonDataFromID(int ID, ArrayList<PersonData> personData)
    {
        for(PersonData data : personData)
            if(data.id == ID)
                return data;

        return null;
    }

/*    public static void PrintFamilyTreeToConsole(ArrayList<Family> families)
    {
        System.out.println("\n----------------------------------------------------------------------------------------\n");

        for(Family family : families)
        {
            PrintTreeRecursive(family.rootNode, 0, 0, family.rootNode.children.size() - 1, false);
            System.out.println("\n----------------------------------------------------------------------------------------\n");
        }
    }*/

    public static void PrintTreeRecursive(Person root, int generation, int childIndex, int childCount, boolean isFinalChild)
    {
        for(int i = 0; i < generation; i++)
        {
            if(childIndex >= childCount && i == generation - 1)
                System.out.print(" '-- ");

            else if(i == generation - 1 && childIndex < childCount)
                System.out.print(" |-- ");

            else if(i < generation - 1 && !isFinalChild)
                System.out.print(" |   ");

            else
                System.out.print("     ");
        }

        isFinalChild = childIndex >= childCount;

        System.out.print(root.data.id + ": " + root.data.name + " " + root.data.surname);
        if(root.spouse != null)
            System.out.print((root.data.maritalStatus.equalsIgnoreCase("evli") ? " === " : " =X= ") + root.spouse.data.id + ": " + root.spouse.data.name + " " + root.spouse.data.surname);

        System.out.println();

        for(int i = 0; i < root.children.size(); i++)
            PrintTreeRecursive(root.children.get(i), generation + 1, i, root.children.size() - 1, isFinalChild);
    }
}