package microservices.examples.user;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String> {

	public Iterable<User> findAll(Pageable of);

	public long countByDepartmentId(String departmentId);
	public List<User> findAllByDepartmentId(String departmentId, Pageable of);

}
