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

import microservices.examples.user.User;

@Component
public class UserGateway {

	private static final String commonUriPrefix = "http://localhost:8090";

	RestTemplate restTemplate;

	@Autowired
	public UserGateway(RestTemplate restTemplate) {
		super();
	    this.restTemplate = restTemplate;
	}

	public List<User> getUsers(Set<String> userIds) {
		if (userIds.isEmpty()) {
			return new ArrayList<>();
		}
		String typeString = getIdString(userIds);
		String url = getCommonUri("/users/%s?batchapi", typeString);
		ResponseEntity<List<User>> responseEntity = 
				restTemplate.exchange(
						url, HttpMethod.GET, null,
						new ParameterizedTypeReference<List<User>>() {}
						);
		List<User> users = responseEntity.getBody();
		return users;
	}

	public User getUser(String userId) {
		if (userId == null || userId.isEmpty()) {
			return null;
		}
		String url = getCommonUri("/users/%s", userId);
		
		ResponseEntity<User> responseEntity = 
				restTemplate.exchange(
						url, HttpMethod.GET, null,
						User.class
						);
		User body = responseEntity.getBody();
		return body;
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
