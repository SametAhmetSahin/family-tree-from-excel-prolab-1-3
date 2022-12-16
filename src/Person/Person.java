package Person;

import java.util.ArrayList;

public class Person
{
    public PersonData data;
    public Person spouse;
    public Person mother;
    public Person father;
    public ArrayList<Person> children;

    public Person()
    {
        this.data = new PersonData();
        this.mother = null;
        this.father = null;
        this.spouse = null;
        this.children = new ArrayList<>();
    }

    public Person(PersonData Data)
    {
        this.data = Data;
        this.mother = null;
        this.father = null;
        this.spouse = null;
        this.children = new ArrayList<>();
    }

    public Person(PersonData Data, Person WifeBand)
    {
        this.data = Data;
        this.mother = null;
        this.father = null;
        this.spouse = WifeBand;
        this.children = new ArrayList<>();
    }

    public Person(PersonData Data, Person WifeBand, ArrayList<Person> Children)
    {
        this.data = Data;
        this.mother = null;
        this.father = null;
        this.spouse = WifeBand;
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
        this.spouse = WifeBand;
    }

    public Person(PersonData Data, Person Mother, Person Father, Person WifeBand, ArrayList<Person> Children)
    {
        this.data = Data;
        this.mother = Mother;
        this.father = Father;
        this.spouse = WifeBand;
        this.children = Children;
    }
}

