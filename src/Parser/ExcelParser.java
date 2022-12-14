package Parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.ArrayList;

import Person.Person;
import Person.PersonData;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;

public class ExcelParser
{
    public static ArrayList<PersonData> parse(String filepath, int sheet_index) throws IOException
    {
        FileInputStream fis = new FileInputStream(filepath);
        XSSFWorkbook wb = new XSSFWorkbook(fis);

        wb.getNumberOfSheets();

        XSSFSheet sheet = wb.getSheetAt(sheet_index);
        ArrayList<PersonData> personDataArrayList = new ArrayList<>();

        for (int i = 1; i <= sheet.getLastRowNum(); i++)
        {
            Row row = sheet.getRow(i);
            PersonData data = new PersonData();

            data.id = row.getCell(0) != null ? (int)row.getCell(0).getNumericCellValue() : 0;

            data.name = row.getCell(1) != null ? row.getCell(1).getStringCellValue() : "";
            data.surname = row.getCell(2) != null ? row.getCell(2).getStringCellValue() : "";
            data.birthdate = row.getCell(3) != null ? row.getCell(3).toString() : "";
            data.spouse = row.getCell(4) != null ? row.getCell(4).getStringCellValue() : "";
            data.mother_name = row.getCell(5) != null ? row.getCell(5).getStringCellValue() : "";
            data.father_name = row.getCell(6) != null ? row.getCell(6).getStringCellValue() : "";
            data.blood_type = row.getCell(7) != null ? row.getCell(7).getStringCellValue() : "";
            data.profession = row.getCell(8) != null ? row.getCell(8).getStringCellValue() : "";
            data.marital_status = row.getCell(9) != null ? row.getCell(9).getStringCellValue() : "";
            data.maiden_name = row.getCell(10) != null ? row.getCell(10).getStringCellValue() : "";

            data.gender = row.getCell(11) != null ? row.getCell(11).getStringCellValue().equals("Erkek") : false;

            personDataArrayList.add(data);
        }

        return personDataArrayList;
    }

    public static ArrayList<PersonData> parseAll(String filepath) throws IOException
    {
        FileInputStream fis = new FileInputStream(filepath);
        XSSFWorkbook wb = new XSSFWorkbook(fis);

        int noOfSheets = wb.getNumberOfSheets();

        ArrayList<PersonData> personDataArrayList = new ArrayList<>();

        for(int j = 0; j < noOfSheets; j++)
        {
            XSSFSheet sheet = wb.getSheetAt(j);

            for (int i = 1; i <= sheet.getLastRowNum(); i++)
            {
                Row row = sheet.getRow(i);
                PersonData data = new PersonData();

                data.id = row.getCell(0) != null ? (int)row.getCell(0).getNumericCellValue() : 0;

                data.name = row.getCell(1) != null ? row.getCell(1).getStringCellValue() : "";
                data.surname = row.getCell(2) != null ? row.getCell(2).getStringCellValue() : "";
                data.birthdate = row.getCell(3) != null ? row.getCell(3).toString() : "";
                data.spouse = row.getCell(4) != null ? row.getCell(4).getStringCellValue() : "";
                data.mother_name = row.getCell(5) != null ? row.getCell(5).getStringCellValue() : "";
                data.father_name = row.getCell(6) != null ? row.getCell(6).getStringCellValue() : "";
                data.blood_type = row.getCell(7) != null ? row.getCell(7).getStringCellValue() : "";
                data.profession = row.getCell(8) != null ? row.getCell(8).getStringCellValue() : "";
                data.marital_status = row.getCell(9) != null ? row.getCell(9).getStringCellValue() : "";
                data.maiden_name = row.getCell(10) != null ? row.getCell(10).getStringCellValue() : "";

                data.gender = row.getCell(11) != null ? row.getCell(11).getStringCellValue().equals("Erkek") : false;

                boolean checkIdentity = false;

                for(PersonData deyta : personDataArrayList)
                    if (deyta.id == data.id)
                    {
                        checkIdentity = true;
                        break;
                    }

                if(!checkIdentity)
                    personDataArrayList.add(data);
            }
        }

        return personDataArrayList;
    }

    public static ArrayList<PersonData> GetFamilyRoots(String filepath) throws IOException
    {
        ArrayList<PersonData> familyRoots = new ArrayList<>();

        FileInputStream fis = new FileInputStream(filepath);
        XSSFWorkbook wb = new XSSFWorkbook(fis);

        int noOfSheets = wb.getNumberOfSheets();

        for(int i = 0; i < noOfSheets; i++)
        {
            XSSFSheet sheet = wb.getSheetAt(i);

            Row row = sheet.getRow(1);
            PersonData data = new PersonData();

            data.id = (int) row.getCell(0).getNumericCellValue();

            data.name = row.getCell(1).getStringCellValue();
            data.surname = row.getCell(2).getStringCellValue();
            data.birthdate = "" + row.getCell(3).getDateCellValue();
            data.spouse = row.getCell(4).getStringCellValue();
            data.mother_name = row.getCell(5).getStringCellValue();
            data.father_name = row.getCell(6).getStringCellValue();
            data.blood_type = row.getCell(7).getStringCellValue();
            data.profession = row.getCell(8).getStringCellValue();
            data.marital_status = row.getCell(9).getStringCellValue();
            data.maiden_name = row.getCell(10).getStringCellValue();

            data.gender = (row.getCell(11).getStringCellValue().equals("Erkek"));

            familyRoots.add(data);
        }

        return familyRoots;
    }

    public static int GetFamilyCount(String filepath) throws IOException
    {
        FileInputStream fis = new FileInputStream(filepath);
        XSSFWorkbook wb = new XSSFWorkbook(fis);

        return wb.getNumberOfSheets();
    }
}