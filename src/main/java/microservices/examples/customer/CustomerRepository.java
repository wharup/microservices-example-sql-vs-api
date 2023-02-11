package microservices.examples.customer;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, String> {

	public Iterable<Customer> findAll(Pageable p);
}
