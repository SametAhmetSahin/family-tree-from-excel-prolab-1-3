package Person;

public class PersonData
{
    public int id;
    public String name;
    public String surname;
    public String birthdate;
    public String spouse;
    public String mother_name;
    public String father_name;
    public String blood_type;
    public String profession;
    public String marital_status;
    public String maiden_name;
    public Boolean gender;

    public PersonData()
    {
        this.id = 0;
        this.name = "";
        this.surname = "";
        this.birthdate = "";
        this.spouse = "";
        this.mother_name = "";
        this.father_name = "";
        this.blood_type = "";
        this.profession = "";
        this.marital_status = "";
        this.maiden_name = "";
        this.gender = false;
    }

    public PersonData(int ID, String Name, String Surname, String Birthday, String Spouse, String MotherName, String FatherName, String BloodType, String Profession, String MaritalStatus, String MaidenName, boolean Gender)
    {
        this.id = ID;
        this.name = Name;
        this.surname = Surname;
        this.birthdate = Birthday;
        this.spouse = Spouse;
        this.mother_name = MotherName;
        this.father_name = FatherName;
        this.blood_type = BloodType;
        this.profession = Profession;
        this.marital_status = MaritalStatus;
        this.maiden_name = MaidenName;
        this.gender = Gender;
    }
}
