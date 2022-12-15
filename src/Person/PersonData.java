package Person;

import com.google.gson.annotations.Expose;

public class PersonData
{
    @Expose
    public int id;
    @Expose
    public String name;
    @Expose
    public String surname;
    @Expose
    public String birthdate;
    @Expose
    public String spouse;
    @Expose
    public String mother_name;
    @Expose
    public String father_name;
    @Expose
    public String blood_type;
    @Expose
    public String profession;
    @Expose
    public String marital_status;
    @Expose
    public String maiden_name;
    @Expose
    public Boolean gender;

    public PersonData()
    {
        this.id = 0;
        this.name = "Bilmemkim";
        this.surname = "Bilinmeyengiloğlu";
        this.birthdate = "Bilinmiyor";
        this.spouse = "Bilinmiyor";
        this.mother_name = "Bilinmiyor";
        this.father_name = "Bilinmiyor";
        this.blood_type = "Bilinmiyor";
        this.profession = "Bilinmiyor";
        this.marital_status = "Bilinmiyor";
        this.maiden_name = "Bilinmiyor";
        this.gender = false;
    }

    public PersonData(int ID, String Name, String Surname, String Marital_Status, boolean Gender)
    {
        this.id = ID;
        this.name = Name;
        this.surname = Surname;
        this.birthdate = "Bilinmiyor";
        this.spouse = "Bilinmiyor";
        this.mother_name = "Bilinmiyor";
        this.father_name = "Bilinmiyor";
        this.blood_type = "Bilinmiyor";
        this.profession = "Bilinmiyor";
        this.marital_status = Marital_Status;
        this.maiden_name = "Bilinmiyor";
        this.gender = Gender;
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
