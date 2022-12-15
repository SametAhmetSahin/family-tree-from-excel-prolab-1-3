package Person;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

public class Person
{
    @Expose
    public PersonData data;

    public Person wife;

    public Person mother;
    public Person father;
    @Expose
    public ArrayList<Person> children;

    public Person()
    {
        this.data = new PersonData();
        this.mother = null;
        this.father = null;
        this.wife = null;
        this.children = new ArrayList<>();
    }

    public Person(PersonData Data)
    {
        this.data = Data;
        this.mother = null;
        this.father = null;
        this.wife = null;
        this.children = new ArrayList<>();
    }

    public Person(PersonData Data, Person WifeBand)
    {
        this.data = Data;
        this.mother = null;
        this.father = null;
        this.wife = WifeBand;
        this.children = new ArrayList<>();
    }

    public Person(PersonData Data, Person WifeBand, ArrayList<Person> Children)
    {
        this.data = Data;
        this.mother = null;
        this.father = null;
        this.wife = WifeBand;
        this.children = Children;
    }

    public Person(PersonData Data, Person Mother, Person Father)
    {
        this.data = Data;
        this.mother = Mother;
        this.father = Father;
        this.children = new ArrayList<>();
    }

    public Person(PersonData Data, Person Mother, Person Father, Person WifeBand)
    {
        this.data = Data;
        this.mother = Mother;
        this.father = Father;
        this.wife = WifeBand;
    }

    public Person(PersonData Data, Person Mother, Person Father, Person WifeBand, ArrayList<Person> Children)
    {
        this.data = Data;
        this.mother = Mother;
        this.father = Father;
        this.wife = WifeBand;
        this.children = Children;
    }
}

