package microservices.examples.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceRequestRepository extends CrudRepository<ServiceRequest, String> {

	@Query(value="select max(tsr.id) from tb_service_request tsr", nativeQuery = true)
	public long getMaxId();
	
	public List<ServiceRequest> findAll(Pageable pageable);

}
