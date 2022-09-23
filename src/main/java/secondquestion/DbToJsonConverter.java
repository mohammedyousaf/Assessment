package secondquestion;


import java.sql.ResultSet;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class DbToJsonConverter {
	
	public static void main(String args[]) throws Exception {

		CommonUtils utils = new CommonUtils();
		JSONObject jsonObject = new JSONObject();
		JSONArray array = new JSONArray();
		ResultSet result = utils.retrieveData();

		while (result.next()) {
			JSONObject studentRecord = new JSONObject();

			studentRecord.put("admission no", result.getInt("admission_no"));
			studentRecord.put("name", result.getString("name"));
			studentRecord.put("physics mark", result.getInt("physics"));
			studentRecord.put("chemistry mark", result.getInt("chemistry"));
			studentRecord.put("maths mark", result.getInt("maths"));
			array.add(studentRecord);

		}
		jsonObject.put("student_data", array);
		System.out.println(jsonObject.toJSONString());

	}
}