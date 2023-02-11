package microservices.examples.system.initializer;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import com.github.javafaker.Faker;

import lombok.extern.slf4j.Slf4j;
import microservices.examples.common.CodeService;
import microservices.examples.customer.Customer;
import microservices.examples.customer.CustomerRepository;

@Service
@Slf4j
public class CustomerDataGenerator {
	CustomerRepository customerRepository;
	
	CodeDataGenerator codeService;

    int count = 0;
	Map<Integer, Customer> customersByIndex = new HashMap<>();

    @Autowired
	public CustomerDataGenerator(CustomerRepository customerRepository, CodeDataGenerator codeService, CacheManager cacheManager) {
		super();
		this.customerRepository = customerRepository;
		this.codeService = codeService;
	}

	public Iterable<Customer> getAll() {
		return customerRepository.findAll();
	}
	
	public Customer getRandomCustomer() {
		if (count == 0) {
			count = (int)customerRepository.count();
			Iterable<Customer> customers = customerRepository.findAll();
			int i = 0;
			Iterator<Customer> iterator = customers.iterator();
			while(iterator.hasNext()) {
				customersByIndex.put(i++, iterator.next());
			}
		}
		int index = getRandomNumber(0, count);
		return customersByIndex.get(index);
	}
	
	private int getRandomNumber(int min, int max) {
	    return (int) ((Math.random() * (max - min)) + min);
	}

	public void deleteAll() {
		customerRepository.deleteAll();
	}

	public void createTestData(int size) {
		Faker f = new Faker(new Locale("ko"));
		List<Customer> customers = new ArrayList<>();
		for (int i = 0 ; i < size; i++) {
			customers.add(aCustomer(f));
		}
		
		try {
			log.error("{}, {}", customers.get(0).getId(), "-");
			Iterable<Customer> saveAll = customerRepository.saveAll(customers);
			log.error("{}, {}", customers.get(0).getId(), saveAll.iterator().next().getId());
		} catch (Exception e2) {
			System.out.println(e2);
		}
		
	}

	private Customer aCustomer(Faker f) {
		Customer customer = new Customer();
		customer.setName(f.name().fullName());
		customer.setBirthday(f.date().birthday().toString());
		customer.setGender(codeService.getRandomCode(CodeService.CUSTOMER_GENDER));
		customer.setAddress(f.address().fullAddress());
		customer.setPhone_number(f.phoneNumber().cellPhone());
		customer.setType(codeService.getRandomCode(CodeService.CUSTOMER_TYPE));
		customer.setCreated(Instant.now());
		customer.setUpdated(Instant.now());
		return customer;
	}

}
