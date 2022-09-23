package studentdata;

import java.io.IOException;
import utils.ExcelUtils;

import org.apache.poi.openxml4j.exceptions.InvalidOperationException;

public class Studentdata {

	public static void main(String[] args) throws IOException {

		Student s1 = new Student(null, 0, 0, 0, 0, 0);
		s1.excelToListConverter();
//path = C:\Users\ecs\Desktop\ECS training\resources\book1.xlsx
	}

}
