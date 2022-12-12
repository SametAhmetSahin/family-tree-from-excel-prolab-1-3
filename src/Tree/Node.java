package Tree;

import Person.*;

public class Node
{
    public int nodeID;
    public Person rootNode;

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
            return root;
        }

        // Kökün eşi varsa ve eklenecek kişinin anne-baba adı köke ve eşine uyuyorsa çocuk olarak ekle.
        else if(root.wife != null)
        {
            if ((root.data.gender ? personDataToAdd.father_name.equals(root.data.name) : personDataToAdd.mother_name.equals(root.data.name)) &&
                    (root.wife.data.gender ? personDataToAdd.father_name.equals(root.wife.data.name) : personDataToAdd.mother_name.equals(root.wife.data.name)))
            {
                root.children.add(new Person(personDataToAdd, (root.data.gender ? root.wife : root), (root.data.gender ? root : root.wife)));
                return root;
            }
        }

        // Eklenecek kişinin bilgileri mevcut köke uymuyorsa çocukları dolaş.
        else
        {
            for(int i = 0; i < root.children.size(); i++)
                root.children.set(i, AddPersonRecursive(root.children.get(i), personDataToAdd));

            // Ayrıca IntelliJ IDEA bu for döngüsü yerine aşağıdakini öneriyor:
            // root.children.replaceAll(person -> AddPersonRecursive(person, personDataToAdd));

            return root;
        }

        return root;
    }
}
