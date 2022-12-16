package Tree;

import Person.*;
import com.google.gson.annotations.Expose;

public class GodotFamily
{
    @Expose
    public int nodeID;
    @Expose
    public GodotPerson rootNode = null;

    public GodotFamily(int id)
    {
        this.nodeID = id;
    }

    public void AddPerson(Person person)
    {
        this.rootNode = new GodotPerson(person);
    }
}
