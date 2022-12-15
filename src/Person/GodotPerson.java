package Person;

import com.google.gson.annotations.Expose;
import java.util.ArrayList;

public class GodotPerson
{
    @Expose
    public PersonData data;
    @Expose
    public int wife = 0;
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
        this.wife = 0;
        this.children = new ArrayList<>();
    }

    public GodotPerson(PersonData Data)
    {
        this.data = Data;
        this.mother = 0;
        this.father = 0;
        this.wife = 0;
        this.children = new ArrayList<>();
    }

    public GodotPerson(PersonData Data, int WifeBand)
    {
        this.data = Data;
        this.mother = 0;
        this.father = 0;
        this.wife = WifeBand;
        this.children = new ArrayList<>();
    }

    public GodotPerson(PersonData Data, int WifeBand, ArrayList<GodotPerson> Children)
    {
        this.data = Data;
        this.mother = 0;
        this.father = 0;
        this.wife = WifeBand;
        this.children = Children;
    }

    public GodotPerson(PersonData Data, int Mother, int Father)
    {
        this.data = Data;
        this.mother = Mother;
        this.father = Father;
        this.children = new ArrayList<>();
    }

    public GodotPerson(PersonData Data, int Mother, int Father, int WifeBand)
    {
        this.data = Data;
        this.mother = Mother;
        this.father = Father;
        this.wife = WifeBand;
    }

    public GodotPerson(PersonData Data, int Mother, int Father, int WifeBand, ArrayList<GodotPerson> Children)
    {
        this.data = Data;
        this.mother = Mother;
        this.father = Father;
        this.wife = WifeBand;
        this.children = Children;
    }
}

