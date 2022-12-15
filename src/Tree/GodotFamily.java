package Tree;

import Person.*;
import com.google.gson.annotations.Expose;
import java.util.ArrayList;

public class GodotFamily
{
    @Expose
    public int nodeID;
    @Expose
    public GodotPerson rootNode = null;

    @Expose
    public ArrayList<GodotPerson> peepsWithNoChildren = new ArrayList<>();

    public GodotFamily(int id)
    {
        this.nodeID = id;
    }

    public void AddPerson(PersonData personDataToAdd)
    {
        this.rootNode = AddPersonRecursive(this.rootNode, personDataToAdd);
    }

    static GodotPerson AddPersonRecursive(GodotPerson root, PersonData personDataToAdd)
    {
        // Yorumlanmış kodlar spouse String'inde spouseID bulunması durumunda geçerli olup
        // yorumdan çıkarıldığı takdirde önceden yorumlanmamış eşdeğer kodların yorumlanmasını gerektirmektedir.

        if(root == null)
            return new GodotPerson(personDataToAdd);

        // Eş stringini bölüyoruz çünkü herkesin eşinde soyadı bulunmuyor ve bunu göz önüne almamız lazım.
        String[] divided = GetSpouseData(root.data.surname, root.data.spouse);

        // Eğer kökün eşi yoksa, kökün eşinin adı-soyadı ile eklenecek kişinin adı ve varsa soyadı uyuşuyorsa eş olarak ekle.
        //if(root.wife == null && personDataToAdd.id == Integer.parseInt(divided[0]) && personDataToAdd.name.equals(divided[1]) && personDataToAdd.surname.equals(divided[2]))
        if(root.wife == 0 && personDataToAdd.name.equals(divided[0]) && personDataToAdd.surname.equals(divided[1]))
        {
            root.wife = personDataToAdd.id;
            //System.out.println("Eş eklendi.");
            return root;
        }

        // Eklenecek kişinin anne-baba adı köke uyuyorsa çocuk olarak ekle.
        // Eş kontrolü kalıcı olarak kaldırıldı.
        if(root.data.gender)
        {
            if(personDataToAdd.father_name.equals(root.data.name))
            {
                root.children.add(new GodotPerson(personDataToAdd, root.wife, root.data.id));
                //System.out.println("Çocuk eklendi.");
                return root;
            }
        }
        else
        {
            if(personDataToAdd.mother_name.equals(root.data.name))
            {
                root.children.add(new GodotPerson(personDataToAdd, root.data.id, root.wife));
                //System.out.println("Çocuk eklendi.");
                return root;
            }
        }

        // Eklenecek kişinin bilgileri mevcut köke uymuyorsa çocukları dolaş.
        for(int i = 0; i < root.children.size(); i++)
            root.children.set(i, GodotFamily.AddPersonRecursive(root.children.get(i), personDataToAdd));

        return root;
    }

    public void ValidateFamily(ArrayList<PersonData> peopleList)
    {
        this.rootNode = ValidateFamilyRecursive(this.rootNode, peopleList);
    }

    GodotPerson ValidateFamilyRecursive(GodotPerson root, ArrayList<PersonData> peopleList)
    {
        // Yorumlanmış kodlar spouse String'inde spouseID bulunması durumunda geçerli olup
        // yorumdan çıkarıldığı takdirde önceden yorumlanmamış eşdeğer kodların yorumlanmasını gerektirmektedir.

        if(root.data.marital_status.equalsIgnoreCase("Evli") && root.wife == 0)
        {
            String[] spouseData = GetSpouseData(root.data.surname, root.data.spouse);

            for(PersonData person : peopleList)
            {
                //if(person.id == Integer.parseInt(spouseData[0]) && person.name.equalsIgnoreCase(spouseData[1]) && person.surname.equalsIgnoreCase(spouseData[2]))
                if(person.name.equalsIgnoreCase(spouseData[0]) && person.surname.equalsIgnoreCase(spouseData[1]))
                {
                    root.wife = person.id;
                    break;
                }
            }
        }

        for(int i = 0; i < root.children.size(); i++)
        {
            GodotPerson child = root.children.get(i);

            if(!root.data.marital_status.equalsIgnoreCase("Evli") &&
                    ((!child.data.father_name.isEmpty() && child.father == 0) ||
                            (!child.data.mother_name.isEmpty() && child.mother == 0)))
            {
                if(child.father == 0)
                {
                    root.wife = peopleList.size() + 1;
                    child.father = peopleList.size() + 1;
                }
                if(child.mother == 0)
                {
                    root.wife = peopleList.size() + 1;
                    child.mother = peopleList.size() + 1;
                }
            }

            if(root.data.marital_status.equalsIgnoreCase("Evli") &&
                    ((!child.data.father_name.isEmpty() && child.father == 0) ||
                            (!child.data.mother_name.isEmpty() && child.mother == 0)))
            {
                for(PersonData person : peopleList)
                {
                    //if(child.father == 0)  // Bozuk versiyon
                    if(child.father == 0 && person.name.equalsIgnoreCase(child.data.father_name) && person.surname.equalsIgnoreCase(child.data.surname))
                    {
                        root.wife = person.id;
                        child.father = person.id;
                        break;
                    }

                    //if(child.mother == 0)  // Bozuk versiyon
                    if(child.mother == 0 && person.name.equalsIgnoreCase(child.data.mother_name) /*&& person.surname.equalsIgnoreCase(child.data.surname)*/)
                    {
                        root.wife = person.id;
                        child.mother = person.id;
                        break;
                    }
                }

                if(child.father == 0)
                {
                    root.wife = peopleList.size() + 1;
                    child.father = peopleList.size() + 1;
                }
                if(child.mother == 0)
                {
                    root.wife = peopleList.size() + 1;
                    child.mother = peopleList.size() + 1;
                }
            }

            root.children.set(i, child);
            root.children.set(i, ValidateFamilyRecursive(root.children.get(i), peopleList));
        }

        return root;
    }

    public static String[] GetSpouseData(String surnameOfHusband, String spouse)
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

    // Depth first mantığıyla çocuksuzları bulup ArrayListe ekliyoruz.
    public void FindPeopleWithNoChildrenRecursive(GodotPerson root)
    {
        if(root.children.isEmpty())
            peepsWithNoChildren.add(root);

        else
        {
            for(GodotPerson person : root.children)
                FindPeopleWithNoChildrenRecursive(person);
        }
    }
}
