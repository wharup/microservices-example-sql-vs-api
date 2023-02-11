package microservices.examples.gateway;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import microservices.examples.customer.Customer;

@Component
public class CustomerGateway {

	private static final String commonUriPrefix = "http://localhost:8090";

	
	RestTemplate restTemplate;

	@Autowired
	public CustomerGateway() {
		super();
	    this.restTemplate = createRestTemplate();
	}

	private RestTemplate createRestTemplate(){
		SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
		requestFactory.setConnectTimeout(5000);
		requestFactory.setReadTimeout(5000);
		return new RestTemplate(requestFactory);
	}

	public List<Customer> getCustomers(Set<String> customerIds) {
		if (customerIds.isEmpty()) {
			return new ArrayList<>();
		}
		String typeString = getIdString(customerIds);
		
		String url = getCommonUri("/customers/%s?batchapi", typeString);
		ResponseEntity<List<Customer>> responseEntity = 
				restTemplate.exchange(
						url, HttpMethod.GET, null,
						new ParameterizedTypeReference<List<Customer>>() {}
						);
		List<Customer> customers = responseEntity.getBody();
		return customers;
	}
	
	public Customer getCustomer(String customerId) {
		if (customerId == null || customerId.isEmpty()) {
			return null;
		}
		String url = getCommonUri("/customers/%s", customerId);

		ResponseEntity<Customer> responseEntity = 
				restTemplate.exchange(
						url, HttpMethod.GET, null,
						Customer.class
						);
		Customer body = responseEntity.getBody();
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
