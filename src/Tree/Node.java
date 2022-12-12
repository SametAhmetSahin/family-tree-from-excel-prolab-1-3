package Tree;

import Person.Person;
import Person.PersonData;

public class Node
{
    public int nodeID;
    public Person rootNode;

    public void AddPerson(Person root, PersonData personDataToAdd)  // TODO: Geri dönüş tipinin Person'a dönüştürülüp dönüştürülmeyeceğine karar ver.
    {
        if(root == null)
        {
            this.rootNode = new Person(personDataToAdd);
            return;
        }
        else
        {
            // Eş stringini bölüyoruz çünkü herkesin eşinde soyadı bulunmuyor ve bunu göz önüne almamız lazım.
            String[] divided = this.rootNode.data.spouse.split(" ");

            // Eğer kökün eşinin adı-soyadı ile eklenecek kişinin adı ve varsa soyadı uyuşuyorsa eş olarak ekle.
            if(personDataToAdd.name.equals(divided[0]) && (divided.length > 1 ? personDataToAdd.surname.equals(divided[divided.length - 1]) : true))
            {
                this.rootNode.wife = new Person(personDataToAdd, this.rootNode);
                return;
            }

            // Kökün eşi varsa ve eklenecek kişinin anne-baba adı köke ve eşine uyuyorsa çocuk olarak ekle.
            if(this.rootNode.wife != null)
                if((this.rootNode.data.gender ? personDataToAdd.father_name.equals(this.rootNode.data.name) : personDataToAdd.mother_name.equals(this.rootNode.data.name)) &&
                   (this.rootNode.wife.data.gender ? personDataToAdd.father_name.equals(this.rootNode.wife.data.name) : personDataToAdd.mother_name.equals(this.rootNode.wife.data.name)))
                    this.rootNode.children.add(new Person(personDataToAdd, (this.rootNode.data.gender ? this.rootNode.wife : this.rootNode), (this.rootNode.data.gender ? this.rootNode : this.rootNode.wife)));
        }
    }
}
