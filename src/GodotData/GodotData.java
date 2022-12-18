package GodotData;

import Tree.GodotFamily;
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
    @Expose
    public int[] generationCountAfterPerson = new int[2];   // İlk eleman kişi ID'si, ikinci eleman kişiden sonra gelen nesil sayısı
    @Expose
    public GodotFamily familyTreeOfSpecificPerson = new GodotFamily(0);
}
