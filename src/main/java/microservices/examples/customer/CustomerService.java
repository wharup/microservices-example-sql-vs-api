package microservices.examples.customer;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import microservices.examples.system.CacheProxy;
import microservices.examples.system.NotFoundException;

@Service
@Slf4j
public class CustomerService {
	CustomerRepository customerRepository;
	
    private CacheProxy<Customer> customerCache;

    @Autowired
	public CustomerService(CustomerRepository customerRepository, CacheManager cacheManager) {
		super();
		this.customerRepository = customerRepository;
		this.customerCache = new CacheProxy<>(cacheManager, "customers");
	}

	public Iterable<Customer> getAll() {
		return customerRepository.findAll();
	}
	
	public Customer findById(String customerId) {
		Optional<Customer> result = customerRepository.findById(customerId);
		if (result.isPresent()) {
			return result.get();
		}
		throw new NotFoundException(customerId);
	}

	public Iterable<Customer> findAll() {
		return customerRepository.findAll();
	}

	public Customer findCachedById(String customerId) {
		Customer customer = customerCache.get(customerId);
		if (customer != null) {
			return customer;
		}
		customer = findById(customerId);
		if (customer == null) {
			log.info("CANNOT FIND customer for {}", customerId);
		}
		customerCache.put(customerId, customer);
		return customer;
	}

	public Iterable<Customer> findByIds(List<String> customerIds) {
		return customerRepository.findAllById(customerIds);
	}
	
}
