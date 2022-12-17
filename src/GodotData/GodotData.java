package GodotData;

import com.google.gson.annotations.Expose;
import java.util.ArrayList;

public class GodotData
{
    @Expose
    public ArrayList<Integer> peopleIDsWithNoChildren = new ArrayList<>();
    @Expose
    public ArrayList<Integer> peopleIDsWithSpecificBlood = new ArrayList<>();
    @Expose
    public ArrayList<Integer> generationCounts = new ArrayList<>();
}
