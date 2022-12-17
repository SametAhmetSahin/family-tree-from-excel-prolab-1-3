package GodotData;

import com.google.gson.annotations.Expose;
import java.util.ArrayList;

public class GodotData
{
    @Expose
    public ArrayList<Integer> peopleWithNoChildren = new ArrayList<>();
    @Expose
    public ArrayList<Integer> peopleWithSpecificBlood = new ArrayList<>();
    @Expose
    public ArrayList<Integer> generationCounts = new ArrayList<>();
}
