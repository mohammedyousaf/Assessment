package secondquestion;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import utils.ExcelUtils;

public class CommonUtils {

	public ResultSet retrieveData() throws Exception {

		Scanner scanner = new Scanner(System.in);
		System.out.println("Type the name of database :");
		String dbName = scanner.nextLine();
		String url = "jdbc:mysql://localhost:3306/";
		String userName = "root";
		String password = "abc123!@#";
		Connection con = DriverManager.getConnection(url + dbName, userName, password);
		System.out.println("Connection established");
		Statement stmt = con.createStatement();
		System.out.println("Type \"name\" to search by name or type \"adm\" to search by admission number : ");
		String chooser = scanner.nextLine();
		String searchName = "";
		String admissionNumber = "";
		int admissionNum = 0;

		if (chooser.equals("name")) {

			System.out.println("Type the name you want to search :");
			searchName = scanner.nextLine();
		}

		if (chooser.equals("adm")) {

			System.out.println("Type the admission number you want to search :");
			admissionNumber = scanner.nextLine();
			admissionNum = Integer.parseInt(admissionNumber);
		}
		System.out.println("SELECT admission_no,name,physics,chemistry,maths\r\n" + " FROM students WHERE admission_no="
				+ admissionNum + " OR name=" + searchName);
		ResultSet result = stmt.executeQuery("SELECT admission_no,name,physics,chemistry,maths\r\n"
				+ " FROM students WHERE admission_no=" + admissionNum + " OR name= \"" + searchName + "\"");

		return result;

	}

	public void dataInserter(String path, String dbName, String userName, String password)
			throws SQLException, IOException {

		ExcelUtils exut = new ExcelUtils();
		Connection conn = null;
		String url = "jdbc:mysql://localhost:3306/";
		conn = DriverManager.getConnection(url + dbName, userName, password);
		System.out.println("Connected to the database");
		String myName;
		int admissionNumber;
		int physics;
		int chemistry;
		int maths;
		PreparedStatement preparedStatement = conn.prepareStatement("insert into students values(?,?,?,?,?)");
		Statement statement = conn.createStatement();

		for (int i = 1; i < exut.rowCount(path); i++) {

			admissionNumber = exut.numcellData(i, 0, path);
			myName = exut.strcellData(i, 1, path);
			physics = exut.numcellData(i, 2, path);
			chemistry = exut.numcellData(i, 3, path);
			maths = exut.numcellData(i, 4, path);

			preparedStatement.setInt(1, admissionNumber);
			preparedStatement.setString(2, myName);
			preparedStatement.setInt(3, physics);
			preparedStatement.setInt(4, chemistry);
			preparedStatement.setInt(5, maths);
			int recordCount = preparedStatement.executeUpdate();
			System.out.println(recordCount + " records added");

		}

		conn.close();
		statement.close();
		preparedStatement.close();
		System.out.println("Connection closed");

	}

	public JsonNode excelToJson(File excel) {

		ObjectMapper mapper = new ObjectMapper();
		// hold the excel data sheet wise
		ObjectNode excelData = mapper.createObjectNode();
		FileInputStream file = null;
		Workbook workbook = null;
		try {

			file = new FileInputStream(excel);
			String filename = excel.getName().toLowerCase();
			workbook = new XSSFWorkbook(file);

			for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
				Sheet sheet = workbook.getSheetAt(i);
				String sheetName = sheet.getSheetName();

				List<String> headers = new ArrayList<String>();
				ArrayNode sheetData = mapper.createArrayNode();
				for (int j = 0; j <= sheet.getLastRowNum(); j++) {
					Row row = sheet.getRow(j);
					if (j == 0) {

						for (int k = 0; k < row.getLastCellNum(); k++) {
							headers.add(row.getCell(k).getStringCellValue());
						}
					} else {
						// reading work sheet data
						ObjectNode rowData = mapper.createObjectNode();
						for (int k = 0; k < headers.size(); k++) {
							Cell cell = row.getCell(k);
							String headerName = headers.get(k);
							if (cell != null) {
								switch (cell.getCellType()) {
								case FORMULA:
									rowData.put(headerName, cell.getCellFormula());
									break;
								case BOOLEAN:
									rowData.put(headerName, cell.getBooleanCellValue());
									break;
								case NUMERIC:
									rowData.put(headerName, cell.getNumericCellValue());
									break;
								case BLANK:
									rowData.put(headerName, "");
									break;
								default:
									rowData.put(headerName, cell.getStringCellValue());
									break;
								}
							} else {
								rowData.put(headerName, "");
							}
						}
						sheetData.add(rowData);
					}
				}
				excelData.set(sheetName, sheetData);
			}
			return excelData;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (workbook != null) {
				try {
					workbook.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (file != null) {
				try {
					file.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
		return null;
	}

}
