package Tree;

import Main.Main;
import Person.*;

import java.util.ArrayList;

public class Family
{
    public int nodeID;
    public Person rootNode = null;

    public Family(int id)
    {
        this.nodeID = id;
    }

    public void AddPerson(PersonData personDataToAdd, ArrayList<PersonData> peopleList)
    {
        this.rootNode = AddPersonRecursive(this.rootNode, personDataToAdd, peopleList);
    }

    Person AddPersonRecursive(Person root, PersonData personDataToAdd, ArrayList<PersonData> peopleList)
    {
        // Yorumlanmış kodlar spouse String'inde spouseID bulunmaması durumunda geçerli olup
        // yorumdan çıkarıldığı takdirde önceden yorumlanmamış eşdeğer kodların yorumlanmasını gerektirmektedir.

        if(root == null)
            return new Person(personDataToAdd);

        for(PersonData data : peopleList)
            if(data.id == root.data.id)
            {
                root.data.surname = data.surname;
                break;
            }
        //root.data.surname = Main.GetPersonDataFromID(root.data.id, peopleList).surname;

        // Eş stringini bölüyoruz çünkü herkesin eşinde soyadı bulunmuyor ve bunu göz önüne almamız lazım.
        String[] divided = GetSpouseData(root.data.surname, root.data.spouse);

        if(!root.data.spouse.isEmpty())
        {
            // Eğer kökün eşi yoksa, kökün eşinin adı-soyadı ile eklenecek kişinin adı ve varsa soyadı uyuşuyorsa eş olarak ekle.
            if (root.spouse == null && personDataToAdd.id == Integer.parseInt(divided[0]) && personDataToAdd.name.equalsIgnoreCase(divided[1]) && personDataToAdd.surname.equalsIgnoreCase(divided[2]))
            //if(root.spouse == null && personDataToAdd.name.equalsIgnoreCase(divided[0]) && personDataToAdd.surname.equalsIgnoreCase(divided[1]))
            {
                root.spouse = new Person(personDataToAdd, root);
                //System.out.println("Eş eklendi.");
                return root;
            }
        }

        // Eklenecek kişinin anne-baba adı köke uyuyorsa çocuk olarak ekle.
        // Eş kontrolü kalıcı olarak kaldırıldı.
        if(root.data.gender)
        {
            if(personDataToAdd.fatherName.equalsIgnoreCase(root.data.name))
            {
                root.children.add(new Person(personDataToAdd, root.spouse, root));
                //System.out.println("Çocuk eklendi.");
                return root;
            }
        }
        else
        {
            if(personDataToAdd.motherName.equalsIgnoreCase(root.data.name))
            {
                root.children.add(new Person(personDataToAdd, root, root.spouse));
                //System.out.println("Çocuk eklendi.");
                return root;
            }
        }

        // Eklenecek kişinin bilgileri mevcut köke uymuyorsa çocukları dolaş.
        for(int i = 0; i < root.children.size(); i++)
            root.children.set(i, AddPersonRecursive(root.children.get(i), personDataToAdd, peopleList));

        return root;
    }

    public void ValidateFamily(ArrayList<PersonData> peopleList)
    {
        this.rootNode = ValidateFamilyRecursive(this.rootNode, peopleList);
    }

    Person ValidateFamilyRecursive(Person root, ArrayList<PersonData> peopleList)
    {
        // Yorumlanmış kodlar spouse String'inde spouseID bulunmaması durumunda geçerli olup
        // yorumdan çıkarıldığı takdirde önceden yorumlanmamış eşdeğer kodların yorumlanmasını gerektirmektedir.

        for(PersonData data : peopleList)
            if(data.id == root.data.id)
            {
                root.data.surname = data.surname;
                break;
            }
        //root.data.surname = Main.GetPersonDataFromID(root.data.id, peopleList).surname;

        if(root.data.maritalStatus.equalsIgnoreCase("Evli") && root.spouse == null)
        {
            String[] spouseData = GetSpouseData(root.data.surname, root.data.spouse);

            for(PersonData person : peopleList)
            {
                if(!spouseData[0].isBlank())
                {
                    if (person.id == Integer.parseInt(spouseData[0]) && person.name.equalsIgnoreCase(spouseData[1]) && person.surname.equalsIgnoreCase(spouseData[2]))
                    //if(person.name.equalsIgnoreCase(spouseData[0]) && person.surname.equalsIgnoreCase(spouseData[1]))
                    {
                        root.spouse = new Person(person);
                        break;
                    }
                }
            }
        }

        for(int i = 0; i < root.children.size(); i++)
        {
            Person child = root.children.get(i);

            if(!root.data.maritalStatus.equalsIgnoreCase("Evli") &&
                    ((!child.data.fatherName.isEmpty() && child.father == null) ||
                    (!child.data.motherName.isEmpty() && child.mother == null)))
            {
                if(child.father == null)
                {
                    peopleList.add(new PersonData(peopleList.size(), child.data.fatherName, child.data.surname, "Bekar", true));
                    Person father = new Person(peopleList.get(peopleList.size() - 1), root, root.children);
                    root.spouse = father;
                    child.father = father;
                }
                if(child.mother == null)
                {
                    peopleList.add(new PersonData(peopleList.size(), child.data.motherName, "Bilinmeyengilkızı", "Dul", false));
                    Person mother = new Person(peopleList.get(peopleList.size() - 1), root, root.children);
                    root.spouse = mother;
                    child.mother = mother;
                }
            }

            if(root.data.maritalStatus.equalsIgnoreCase("Evli") &&
                    ((!child.data.fatherName.isEmpty() && child.father == null) ||
                            (!child.data.motherName.isEmpty() && child.mother == null)))
            {
                for(PersonData person : peopleList)
                {
                    //if(child.father == null)  // Bozuk versiyon
                    if(child.father == null && person.id == Integer.parseInt(GetSpouseData(child.data.surname, root.data.spouse)[0]) && person.name.equalsIgnoreCase(child.data.fatherName) && person.surname.equalsIgnoreCase(child.data.surname))
                    {
                        Person father = new Person(person);
                        root.spouse = father;
                        child.father = father;
                        break;
                    }

                    //if(child.mother == null)  // Bozuk versiyon
                    if(child.mother == null && person.id == Integer.parseInt(GetSpouseData(child.data.surname, root.data.spouse)[0]) && person.name.equalsIgnoreCase(child.data.motherName) /*&& person.surname.equalsIgnoreCase(child.data.surname)*/)
                    {
                        Person mother = new Person(person);
                        root.spouse = mother;
                        child.mother = mother;
                        break;
                    }
                }

                if(child.father == null)
                {
                    peopleList.add(new PersonData(Integer.parseInt(GetSpouseData(child.data.surname, root.data.spouse)[0]), child.data.fatherName, child.data.surname, "Evli", true));
                    Person father = new Person(peopleList.get(peopleList.size() - 1), root, root.children);
                    root.spouse = father;
                    child.father = father;
                }
                if(child.mother == null)
                {
                    peopleList.add(new PersonData(Integer.parseInt(GetSpouseData(child.data.surname, root.data.spouse)[0]), child.data.motherName, child.data.surname, "Evli", false));
                    Person mother = new Person(peopleList.get(peopleList.size() - 1), root, root.children);
                    root.spouse = mother;
                    child.mother = mother;
                }
            }

            root.children.set(i, child);
            root.children.set(i, ValidateFamilyRecursive(root.children.get(i), peopleList));
        }

        return root;
    }

    public String[] GetSpouseData(String surnameOfHusband, String spouse)
    {
        // Yorumlanmış kodlar spouse String'inde spouseID bulunmaması durumunda geçerli olup
        // yorumdan çıkarıldığı takdirde önceden yorumlanmamış eşdeğer kodların yorumlanmasını gerektirmektedir.
        // İlgili kodlar eğik çizgiler ('/') arasında bulunmaktadır.

        String[] data = { "", "", "" }; //{ "", "" };

        if(!spouse.isBlank())
        {
            String[] splitted = spouse.split(" ");

            String name = "";
            for (int i = 0; i < splitted.length - 1; i++)
                name += i == splitted.length - 2 ? splitted[i] : splitted[i] + " ";

            ////////////////////////////////////////////////////////////////////////////
            /*if(!splitted[splitted.length - 1].equalsIgnoreCase(surnameOfHusband))
            {
                name += " " + splitted[splitted.length - 1];
                data[0] = name;
                data[1] = surnameOfHusband;
            }
            else
            {
                data[0] = name;
                data[1] = splitted[splitted.length - 1];
            }*/

            data[0] = splitted[0];

            if (splitted[splitted.length - 1].equalsIgnoreCase(surnameOfHusband))
            {
                name += " " + splitted[splitted.length - 1];
                data[1] = name;
                data[2] = surnameOfHusband;
            }
            else
            {
                data[1] = name;
                data[2] = splitted[splitted.length - 1];
            }
            ////////////////////////////////////////////////////////////////////////////
        }

        return data;
    }
}
