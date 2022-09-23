package studentdata;

import java.io.IOException;
import java.util.*;
import utils.ExcelUtils;
import org.apache.poi.openxml4j.exceptions.InvalidOperationException;

import utils.ExcelUtils;

class Student {

	String name;
	int admissionNo;
	float percentage;
	int physicsMark;
	int chemistryMark;
	int mathsMark;

	Student(String name, int admissionNo, float percentage, int physicsMark, int chemistryMark, int mathsMark) {

		this.name = name;
		this.admissionNo = admissionNo;
		this.percentage = percentage;
		this.physicsMark = physicsMark;
		this.chemistryMark = chemistryMark;
		this.mathsMark = mathsMark;

	}

	public void display(String physicsGrade, String chemistryGrade, String mathsGrade, Object physicsGradePointt,
			Object chemistryGradePoint, Object mathsGradePoint) {

		System.out.println();
		System.out.println("Name: " + this.name);
		System.out.println("Admission No: " + this.admissionNo);
		System.out.println("Percentage: " + this.percentage);
		System.out.println("Physics: ");
		System.out.println("\t Mark: " + this.physicsMark);
		System.out.println("\tGrade: " + physicsGrade);
		System.out.println("\tGrade Point: " + physicsGradePointt);
		System.out.println("Chemistry: ");
		System.out.println("\tMark: " + this.chemistryMark);
		System.out.println("\tGrade: " + chemistryGrade);
		System.out.println("\tGrade Point: " + chemistryGradePoint);
		System.out.println("Maths: ");
		System.out.println("\tMark: " + this.mathsMark);
		System.out.println("\tGrade: " + mathsGrade);
		System.out.println("\tGrade Point: " + mathsGradePoint);
		System.out.println();

	}

	public static void excelToListConverter() throws IOException {

		try {
			Scanner scanner = new Scanner(System.in);
			System.out.println("type the path of excel file :");

			String path = scanner.nextLine();

			ExcelUtils excel = new ExcelUtils();
			excel.strcellData(0, 1, path);

			int rowCount = excel.rowCount(path) - 1;

			// creation of arraylist for student details

			List<Student> list = new ArrayList<Student>();

			String[] name = new String[rowCount];
			int[] admissionNo = new int[rowCount];
			float[] percentage = new float[rowCount];

			int[] physicsMark = new int[rowCount];
			String[] physicsGrade = new String[rowCount];
			Object[] physicsGradePoint = new Object[rowCount];

			int[] chemistryMark = new int[rowCount];
			String[] chemistryGrade = new String[rowCount];
			Object[] chemistryGradePoint = new Object[rowCount];

			int[] mathsMark = new int[rowCount];
			String[] mathsGrade = new String[rowCount];
			Object[] mathsGradePoint = new Object[rowCount];

			float[] total = new float[rowCount];

			// adding datas to the arraylist created

			for (int i = 0; i < rowCount; i++) {

				name[i] = excel.strcellData(i + 1, 1, path);
				admissionNo[i] = excel.numcellData(i + 1, 0, path);
				physicsMark[i] = excel.numcellData(i + 1, 2, path);
				chemistryMark[i] = excel.numcellData(i + 1, 3, path);
				mathsMark[i] = excel.numcellData(i + 1, 4, path);
				total[i] = physicsMark[i] + chemistryMark[i] + mathsMark[i];
				percentage[i] = (total[i] * 100) / 300;
				physicsGrade[i] = excel.gradeCalc(physicsMark[i]);

				if (physicsMark[i] < 32) {

					physicsGradePoint[i] = "C";

				} else {

					physicsGradePoint[i] = excel.gradePointCalc(physicsMark[i]);

				}

				chemistryGrade[i] = excel.gradeCalc(chemistryMark[i]);
				if (chemistryMark[i] < 32) {

					chemistryGradePoint[i] = "C";

				} else {

					chemistryGradePoint[i] = excel.gradePointCalc(chemistryMark[i]);

				}

				mathsGrade[i] = excel.gradeCalc(mathsMark[i]);
				if (mathsMark[i] < 32) {

					mathsGradePoint[i] = "C";

				} else {

					mathsGradePoint[i] = excel.gradePointCalc(mathsMark[i]);

				}
			}

			for (int j = 0; j < rowCount; j++) {

				Student s = new Student(name[j], admissionNo[j], percentage[j], physicsMark[j], chemistryMark[j],
						mathsMark[j]);
				list.add(s);

			}

			System.out.println("Type \"name\" to search by name or type \"adm\" to search by admission number : ");
			String chooser = scanner.nextLine();

			if (chooser.equals("name")) {

				System.out.println("Type the name you want to search :");
				String searchName = scanner.nextLine();

				for (int k = 0; k < list.size(); k++) {

					if (searchName.equals(name[k])) {

						list.get(k).display(physicsGrade[k], chemistryGrade[k], mathsGrade[k], physicsGradePoint[k],
								chemistryGradePoint[k], mathsGradePoint[k]);
					}

				}

			}

			if (chooser.equals("adm")) {

				System.out.println("Type the admission number you want to search :");
				String addmissionNumber = scanner.nextLine();
				int admissionNum = Integer.parseInt(addmissionNumber);

				for (int m = 0; m < list.size(); m++) {

					if (admissionNo[m] == admissionNum) {

						list.get(m).display(physicsGrade[m], chemistryGrade[m], mathsGrade[m], physicsGradePoint[m],
								chemistryGradePoint[m], mathsGradePoint[m]);

					}
				}
			}
		} catch (InvalidOperationException e) {

			System.out.println("The path you entered doesn't exist or the file may not be an excel");
		}

	}

}
