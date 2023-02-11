package microservices.examples.gateway;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import microservices.examples.department.Department;

@Component
public class DepartmentGateway {

	private static final String commonUriPrefix = "http://localhost:8090";

	RestTemplate restTemplate;

	@Autowired
	public DepartmentGateway(RestTemplate restTemplate) {
		super();
	    this.restTemplate = restTemplate;
	}

	public Department getDepartment(String deptId) {
		if (deptId == null || deptId.isEmpty()) {
			return null;
		}
		String url = getCommonUri("/departments/%s", deptId);
		
		ResponseEntity<Department> responseEntity = 
				restTemplate.exchange(
						url, HttpMethod.GET, null,
						Department.class
						);
		Department body = responseEntity.getBody();
		return body;
	}
	
	public List<Department> getDepartments(Set<String> departmentIds) {
		if (departmentIds.isEmpty()) {
			return new ArrayList<>();
		}
		String typeString = getIdString(departmentIds);
		String url = getCommonUri("/departments/%s?batchapi", typeString);
		ResponseEntity<List<Department>> responseEntity = 
				restTemplate.exchange(
						url, HttpMethod.GET, null,
						new ParameterizedTypeReference<List<Department>>() {}
						);
		List<Department> departments = responseEntity.getBody();
		return departments;
	}
	
	
	private String getIdString(Set<String> ids) {
		String typeString = "";
		for (String codeType : ids) {
			typeString += codeType + ",";
		}
		return typeString;
	}
	
	private String getCommonUri(String format, Object... args) {
		return String.format(commonUriPrefix + format, args);
	}
	

	
	
}
