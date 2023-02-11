package microservices.examples.gateway;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import microservices.examples.common.Code;

@Component
public class CodeGateway {

	private static final String commonUriPrefix = "http://localhost:8090";

	RestTemplate restTemplate;

	@Autowired
	public CodeGateway(RestTemplate restTemplate) {
		super();
	    this.restTemplate = restTemplate;
	}

	public List<Code> getCodesByType(String... codeTypes) {
		if (codeTypes == null || codeTypes.length == 0) {
			return new ArrayList<>();
		}
		String typeString = getIdString(codeTypes);
		String url = getCommonUri("/code-types/%s?batchapi", typeString);
		ResponseEntity<List<Code>> responseEntity = 
				restTemplate.exchange(
						url, HttpMethod.GET, null,
						new ParameterizedTypeReference<List<Code>>() {}
						);
		List<Code> codes = responseEntity.getBody();
		return codes;
	}
	
	public Code getCode(String codeType, String status) {
		
		String url = getCommonUri("/code-types/%s/codes/%s", codeType, status);
		
		ResponseEntity<Code> responseEntity = 
				  restTemplate.exchange(
				    url, HttpMethod.GET, null,
				    Code.class
				  );
		Code body = responseEntity.getBody();
		return body;
	}
	
	private String getIdString(String... codeTypes) {
		String typeString = "";
		for (String codeType : codeTypes) {
			typeString += codeType + ",";
		}
		return typeString;
	}
	
	private String getCommonUri(String format, Object... args) {
		return String.format(commonUriPrefix + format, args);
	}
	

	
	
}
