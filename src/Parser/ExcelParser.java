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
        FileInputStream fis = new FileInputStream(new File(filepath));
        XSSFWorkbook wb = new XSSFWorkbook(fis);

        wb.getNumberOfSheets();

        XSSFSheet sheet = wb.getSheetAt(sheet_index);
        ArrayList<PersonData> personDataArrayList = new ArrayList<>();

        for (int i = 1; i <= sheet.getLastRowNum(); i++)
        {
            Row row = sheet.getRow(i);
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

            personDataArrayList.add(data);
        }

        return personDataArrayList;
    }
}