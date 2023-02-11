package microservices.examples.department;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface DepartmentRepository extends CrudRepository<Department, String>{

	public Iterable<Department> findAll(Pageable p);

	public Department findByName(String name);

}
