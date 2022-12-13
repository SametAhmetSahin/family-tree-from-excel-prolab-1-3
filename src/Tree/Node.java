package Tree;

import Person.*;

import java.util.ArrayList;

public class Node
{
    public int nodeID;
    public Person rootNode;
    public ArrayList<Person> peepsWithNoChildren = new ArrayList<>();

    public void AddPerson(PersonData personDataToAdd)
    {
        this.rootNode = AddPersonRecursive(this.rootNode, personDataToAdd);
    }

    public Person AddPersonRecursive(Person root, PersonData personDataToAdd)
    {
        if(root == null)
            return new Person(personDataToAdd);

        // Eş stringini bölüyoruz çünkü herkesin eşinde soyadı bulunmuyor ve bunu göz önüne almamız lazım.
        String[] divided = root.data.spouse.split(" ");

        // Eğer kökün eşi yoksa, kökün eşinin adı-soyadı ile eklenecek kişinin adı ve varsa soyadı uyuşuyorsa eş olarak ekle.
        if(root.wife == null && personDataToAdd.name.equals(divided[0]) && (divided.length > 1 ? personDataToAdd.surname.equals(divided[divided.length - 1]) : true))
        {
            root.wife = new Person(personDataToAdd, root);
            System.out.println("Eş eklendi.");
            return root;
        }

        // Kökün eşi varsa ve eklenecek kişinin anne-baba adı köke ve eşine uyuyorsa çocuk olarak ekle.
        // Eş kontrolü geçici olarak kaldırıldı.
        if(/*root.wife != null*/true)
        {
            if(root.data.gender)
            {
                if(personDataToAdd.father_name.equals(root.data.name) /*&& personDataToAdd.mother_name.equals(root.wife.data.name)*/)
                {
                    root.children.add(new Person(personDataToAdd, root.wife, root));
                    System.out.println("Çocuk eklendi.");
                    return root;
                }
            }
            else
            {
                if(/*personDataToAdd.father_name.equals(root.wife.data.name) &&*/ personDataToAdd.mother_name.equals(root.data.name))
                {
                    root.children.add(new Person(personDataToAdd, root, root.wife));
                    System.out.println("Çocuk eklendi.");
                    return root;
                }
            }
        }

        // Eklenecek kişinin bilgileri mevcut köke uymuyorsa çocukları dolaş.
        for(int i = 0; i < root.children.size(); i++)
        {
            Person pers = AddPersonRecursive(root.children.get(i), personDataToAdd);
            root.children.set(i, pers);
        }

        // Ayrıca IntelliJ IDEA bu for döngüsü yerine aşağıdakini öneriyor:
        // root.children.replaceAll(person -> AddPersonRecursive(person, personDataToAdd));

        return root;
    }

    // Depth first mantığıyla çocuksuzları bulup ArrayListe ekliyoruz.
    public void FindPeopleWithNoChildrenRecursive(Person root)
    {
        if(root.children.isEmpty())
            peepsWithNoChildren.add(root);

        else
        {
            for(Person person : root.children)
                FindPeopleWithNoChildrenRecursive(person);
        }
    }
}
