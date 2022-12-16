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
    public String motherName;
    @Expose
    public String fatherName;
    @Expose
    public String bloodType;
    @Expose
    public String profession;
    @Expose
    public String maritalStatus;
    @Expose
    public String maidenName;
    @Expose
    public Boolean gender;

    public PersonData()
    {
        this.id = 0;
        this.name = "Bilmemkim";
        this.surname = "Bilinmeyengiloğlu";
        this.birthdate = "Bilinmiyor";
        this.spouse = "Bilinmiyor";
        this.motherName = "Bilinmiyor";
        this.fatherName = "Bilinmiyor";
        this.bloodType = "Bilinmiyor";
        this.profession = "Bilinmiyor";
        this.maritalStatus = "Bilinmiyor";
        this.maidenName = "Bilinmiyor";
        this.gender = false;
    }

    public PersonData(int ID, PersonData data)
    {
        this.id = ID;
        this.name = data.name;
        this.surname = data.surname;
        this.birthdate = data.birthdate;
        this.spouse = data.spouse;
        this.motherName = data.motherName;
        this.fatherName = data.fatherName;
        this.bloodType = data.bloodType;
        this.profession = data.profession;
        this.maritalStatus = data.maritalStatus;
        this.maidenName = data.maidenName;
        this.gender = data.gender;
    }

    public PersonData(int ID, String Name, String Surname, String Marital_Status, boolean Gender)
    {
        this.id = ID;
        this.name = Name;
        this.surname = Surname;
        this.birthdate = "Bilinmiyor";
        this.spouse = "Bilinmiyor";
        this.motherName = "Bilinmiyor";
        this.fatherName = "Bilinmiyor";
        this.bloodType = "Bilinmiyor";
        this.profession = "Bilinmiyor";
        this.maritalStatus = Marital_Status;
        this.maidenName = "Bilinmiyor";
        this.gender = Gender;
    }

    public PersonData(int ID, String Name, String Surname, String Birthday, String Spouse, String MotherName, String FatherName, String BloodType, String Profession, String MaritalStatus, String MaidenName, boolean Gender)
    {
        this.id = ID;
        this.name = Name;
        this.surname = Surname;
        this.birthdate = Birthday;
        this.spouse = Spouse;
        this.motherName = MotherName;
        this.fatherName = FatherName;
        this.bloodType = BloodType;
        this.profession = Profession;
        this.maritalStatus = MaritalStatus;
        this.maidenName = MaidenName;
        this.gender = Gender;
    }
}
