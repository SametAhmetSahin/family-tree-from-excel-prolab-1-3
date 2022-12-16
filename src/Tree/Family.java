package Tree;

import Main.Main;
import Person.*;
import com.google.gson.annotations.Expose;
import java.util.ArrayList;

public class Family
{
    @Expose
    public int nodeID;
    @Expose
    public Person rootNode = null;

    public Family(int id)
    {
        this.nodeID = id;
    }

    public void AddPerson(PersonData personDataToAdd)
    {
        this.rootNode = AddPersonRecursive(this.rootNode, personDataToAdd);
    }

    Person AddPersonRecursive(Person root, PersonData personDataToAdd)
    {
        // Yorumlanmış kodlar spouse String'inde spouseID bulunması durumunda geçerli olup
        // yorumdan çıkarıldığı takdirde önceden yorumlanmamış eşdeğer kodların yorumlanmasını gerektirmektedir.

        if(root == null)
            return new Person(personDataToAdd);

        // Eş stringini bölüyoruz çünkü herkesin eşinde soyadı bulunmuyor ve bunu göz önüne almamız lazım.
        String[] divided = GetSpouseData(root.data.surname, root.data.spouse);

        // Eğer kökün eşi yoksa, kökün eşinin adı-soyadı ile eklenecek kişinin adı ve varsa soyadı uyuşuyorsa eş olarak ekle.
        //if(root.wife == null && personDataToAdd.id == Integer.parseInt(divided[0]) && personDataToAdd.name.equalsIgnoreCase(divided[1]) && personDataToAdd.surname.equalsIgnoreCase(divided[2]))
        if(root.wife == null && personDataToAdd.name.equalsIgnoreCase(divided[0]) && personDataToAdd.surname.equalsIgnoreCase(divided[1]))
        {
            root.wife = new Person(personDataToAdd, root);
            //System.out.println("Eş eklendi.");
            return root;
        }

        // Eklenecek kişinin anne-baba adı köke uyuyorsa çocuk olarak ekle.
        // Eş kontrolü kalıcı olarak kaldırıldı.
        if(root.data.gender)
        {
            if(personDataToAdd.father_name.equalsIgnoreCase(root.data.name))
            {
                root.children.add(new Person(personDataToAdd, root.wife, root));
                //System.out.println("Çocuk eklendi.");
                return root;
            }
        }
        else
        {
            if(personDataToAdd.mother_name.equalsIgnoreCase(root.data.name))
            {
                root.children.add(new Person(personDataToAdd, root, root.wife));
                //System.out.println("Çocuk eklendi.");
                return root;
            }
        }

        // Eklenecek kişinin bilgileri mevcut köke uymuyorsa çocukları dolaş.
        for(int i = 0; i < root.children.size(); i++)
            root.children.set(i, AddPersonRecursive(root.children.get(i), personDataToAdd));

        return root;
    }

    public void ValidateFamily()
    {
        this.rootNode = ValidateFamilyRecursive(this.rootNode);
    }

    Person ValidateFamilyRecursive(Person root)
    {
        // Yorumlanmış kodlar spouse String'inde spouseID bulunması durumunda geçerli olup
        // yorumdan çıkarıldığı takdirde önceden yorumlanmamış eşdeğer kodların yorumlanmasını gerektirmektedir.

        if(root.data.marital_status.equalsIgnoreCase("Evli") && root.wife == null)
        {
            String[] spouseData = GetSpouseData(root.data.surname, root.data.spouse);

            for(PersonData person : Main.peopleList)
            {
                //if(person.id == Integer.parseInt(spouseData[0]) && person.name.equalsIgnoreCase(spouseData[1]) && person.surname.equalsIgnoreCase(spouseData[2]))
                if(person.name.equalsIgnoreCase(spouseData[0]) && person.surname.equalsIgnoreCase(spouseData[1]))
                {
                    root.wife = new Person(person);
                    break;
                }
            }
        }

        for(int i = 0; i < root.children.size(); i++)
        {
            Person child = root.children.get(i);

            if(!root.data.marital_status.equalsIgnoreCase("Evli") &&
                    ((!child.data.father_name.isEmpty() && child.father == null) ||
                    (!child.data.mother_name.isEmpty() && child.mother == null)))
            {
                if(child.father == null)
                {
                    Main.peopleList.add(new PersonData(Main.peopleList.size(), child.data.father_name, child.data.surname, "Bekar", true));
                    Person father = new Person(Main.peopleList.get(Main.peopleList.size() - 1), root, root.children);
                    root.wife = father;
                    child.father = father;
                }
                if(child.mother == null)
                {
                    Main.peopleList.add(new PersonData(Main.peopleList.size(), child.data.mother_name, "Bilinmeyengilkızı", "Dul", false));
                    Person mother = new Person(Main.peopleList.get(Main.peopleList.size() - 1), root, root.children);
                    root.wife = mother;
                    child.mother = mother;
                }
            }

            if(root.data.marital_status.equalsIgnoreCase("Evli") &&
                    ((!child.data.father_name.isEmpty() && child.father == null) ||
                            (!child.data.mother_name.isEmpty() && child.mother == null)))
            {
                for(PersonData person : Main.peopleList)
                {
                    //if(child.father == null)  // Bozuk versiyon
                    if(child.father == null && person.name.equalsIgnoreCase(child.data.father_name) && person.surname.equalsIgnoreCase(child.data.surname))
                    {
                        Person father = new Person(person);
                        root.wife = father;
                        child.father = father;
                        break;
                    }

                    //if(child.mother == null)  // Bozuk versiyon
                    if(child.mother == null && person.name.equalsIgnoreCase(child.data.mother_name) /*&& person.surname.equalsIgnoreCase(child.data.surname)*/)
                    {
                        Person mother = new Person(person);
                        root.wife = mother;
                        child.mother = mother;
                        break;
                    }
                }

                if(child.father == null)
                {
                    Main.peopleList.add(new PersonData(Main.peopleList.size(), child.data.father_name, child.data.surname, "Evli", true));
                    Person father = new Person(Main.peopleList.get(Main.peopleList.size() - 1), root, root.children);root.wife = father;
                    child.father = father;
                }
                if(child.mother == null)
                {
                    Main.peopleList.add(new PersonData(Main.peopleList.size(), child.data.mother_name, child.data.surname, "Evli", false));
                    Person mother = new Person(Main.peopleList.get(Main.peopleList.size() - 1), root, root.children);
                    root.wife = mother;
                    child.mother = mother;
                }
            }

            root.children.set(i, child);
            root.children.set(i, ValidateFamilyRecursive(root.children.get(i)));
        }

        return root;
    }

    public String[] GetSpouseData(String surnameOfHusband, String spouse)
    {
        // Yorumlanmış kodlar spouse String'inde spouseID bulunması durumunda geçerli olup
        // yorumdan çıkarıldığı takdirde önceden yorumlanmamış eşdeğer kodların yorumlanmasını gerektirmektedir.
        // İlgili kodlar eğik çizgiler ('/') arasında bulunmaktadır.

        String[] data = new String[2]; //new String[3];
        String[] splitted = spouse.split(" ");

        String name = "";
        for(int i = 0; i < splitted.length - 1; i++)
            name += i == splitted.length - 2 ? splitted[i] : splitted[i] + " ";

        ////////////////////////////////////////////////////////////////////////////
        if(!splitted[splitted.length - 1].equalsIgnoreCase(surnameOfHusband))
        {
            name += " " + splitted[splitted.length - 1];
            data[0] = name;
            data[1] = surnameOfHusband;
        }
        else
        {
            data[0] = name;
            data[1] = splitted[splitted.length - 1];
        }

        /*data[0] = splitted[0];

        if(splitted[splitted.length - 1].equalsIgnoreCase(surnameOfHusband))
        {
            name += " " + surnameOfHusband;
            data[1] = name;
            data[2] = surnameOfHusband;
        }
        else
        {
            data[1] = name;
            data[2] = splitted[splitted.length - 1];
        }*/
        ////////////////////////////////////////////////////////////////////////////

        return data;
    }
}
