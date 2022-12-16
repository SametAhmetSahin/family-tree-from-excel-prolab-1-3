package Person;

import com.google.gson.annotations.Expose;
import java.util.ArrayList;

public class GodotPerson
{
    @Expose
    public PersonData data;
    @Expose
    public GodotPerson spouse;
    @Expose
    public int mother;
    @Expose
    public int father;
    @Expose
    public ArrayList<GodotPerson> children;

    public GodotPerson()
    {
        this.data = new PersonData();
        this.mother = 0;
        this.father = 0;
        this.spouse = null;
        this.children = new ArrayList<>();
    }

    public GodotPerson(Person person, boolean createNonExistentSpouse)
    {
        this.data = person.data;
        this.mother = person.mother == null ? 0 : person.mother.data.id;
        this.father = person.father == null ? 0 : person.father.data.id;
        this.spouse = person.spouse == null || createNonExistentSpouse ? null : new GodotPerson(person.spouse, true);
        this.children = new ArrayList<>();

        if(!person.children.isEmpty() && !createNonExistentSpouse)
            for(Person child : person.children)
                this.children.add(new GodotPerson(child, false));
    }
}

