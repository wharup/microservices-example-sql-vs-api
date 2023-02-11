package microservices.examples.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import microservices.examples.common.CodeService;
import microservices.examples.customer.Customer;
import microservices.examples.customer.CustomerService;
import microservices.examples.department.Department;
import microservices.examples.department.DepartmentService;
import microservices.examples.gateway.CodeGateway;
import microservices.examples.gateway.CustomerGateway;
import microservices.examples.gateway.DepartmentGateway;
import microservices.examples.gateway.UserGateway;
import microservices.examples.system.CacheProxy;
import microservices.examples.user.User;
import microservices.examples.user.UserService;

@Service
@Slf4j
public class ServiceRequestCachedService extends ServiceRequestService {
	
    private CacheProxy<Map<String, String>> codeCache;
    private CacheProxy<Department> departmentCache;
    private CacheProxy<User> userCache;
    private CacheProxy<Customer> customerCache;

	@Autowired
	public ServiceRequestCachedService(ServiceRequestDAO serviceRequestDAO,
			CustomerService customerService, UserService userService, DepartmentService departmentService,
			CodeService codeService, CustomerGateway customerGateway, UserGateway userGateway,
			DepartmentGateway departmentGateway, CodeGateway codeGateway, CacheManager cacheManager) {
		
		super(serviceRequestDAO, customerService, userService, departmentService, codeService, customerGateway,
				userGateway, departmentGateway, codeGateway);
		
	    this.departmentCache = new CacheProxy<>(cacheManager, "departments");
	    this.userCache = new CacheProxy<>(cacheManager, "users");
	    this.customerCache = new CacheProxy<>(cacheManager, "customers");
	    this.codeCache = new CacheProxy<>(cacheManager, "codes"); 
	}

	public List<ServiceRequest> findAllWithSingleRESTApi(Pageable pageable) {
		List<ServiceRequest> findAllWithSingleRESTApi = super.findAllWithSingleRESTApi(pageable);
		printCacheStatus();
		return findAllWithSingleRESTApi;
	}

	public List<ServiceRequest> findAllWithBatchRESTApi(Pageable pageable) {
		List<ServiceRequest> result = super.findAllWithBatchRESTApi(pageable);
		printCacheStatus();
		return result;
	}
	
	public Map<String, String> getDepartmentNames(Set<String> departmentIds) {
		Map<String, String> names = new HashMap<>();
		Iterator<String> iterator = departmentIds.iterator();
		while(iterator.hasNext()) {
			Department department = departmentCache.get(iterator.next());
			if (department != null) {
				names.put(department.getId(), department.getName());
				iterator.remove();
			}
		}
		List<Department> departments = departmentGateway.getDepartments(departmentIds);
		for (Department department : departments) {
			names.put(department.getId(), department.getName());
			departmentCache.put(department.getId(), department);
		}
		return names;
	}
	
	public Map<String,Map<String, String>> getCodeNamesByTypes(String... codeTypes) {
		Map<String, Map<String, String>> names = new HashMap<>();

		List<String> requestingCodeTypes = new ArrayList<>();
		for (String codeType : codeTypes) {
			Map<String, String> codeNames = codeCache.get(codeType);
			if (codeNames == null) {
				requestingCodeTypes.add(codeType);
			} else {
				names.put(codeType, codeNames);
			}
		}
		Map<String, Map<String, String>> codeNamesByTypes = super.getCodeNamesByTypes(requestingCodeTypes.toArray(new String[0]));
		for(String key : codeNamesByTypes.keySet()) {
			names.put(key,  codeNamesByTypes.get(key));
			codeCache.put(key, codeNamesByTypes.get(key));
		}
		return names;
	}
	
	public Map<String, String> getUserNames(Set<String> userIds) {
		Map<String, String> names = new HashMap<>();
		Iterator<String> iterator = userIds.iterator();
		while(iterator.hasNext()) {
			User user = userCache.get(iterator.next());
			if (user != null) {
				names.put(user.getId(), user.getName());
				iterator.remove();
			}
		}
		List<User> users = userGateway.getUsers(userIds);
		for (User user : users) {
			names.put(user.getId(), user.getName());
			userCache.put(user.getId(), user);
		}
		return names;
	}
	
  public Map<String, String> getCustomerNames(Set<String> customerIds) {
    //1. 로컬 캐시에서 데이터 조회
    Map<String, String> customerNames = new HashMap<>(customerIds.size());
    Iterator<String> iterator = customerIds.iterator();
    while (iterator.hasNext()) {
      Customer customer = customerCache.get(iterator.next());
      if (customer != null) {
        //2. 로컬 캐시에 데이터가 있으면, 리턴 결과에 추가하고
        //   REST API 요청 목록에서 삭제
        customerNames.put(customer.getId(), customer.getName());
        iterator.remove();
      }
    }
    //2. 부가 정보를 REST API로 요청
    List<Customer> customers = customerGateway.getCustomers(customerIds);
    for (Customer customer : customers) {
      customerNames.put(customer.getId(), customer.getName());
       //2.1 조회한 정보를 리턴 결과에 추가
      customerCache.put(customer.getId(), customer);
    }
    return customerNames;
  }

	private void printCacheStatus() {
		print("  > cache status:\n\t%s\n\t%s\n\t%s\n\t%s", codeCache.getStats()
				, departmentCache.getStats()
				, userCache.getStats()
				, customerCache.getStats());
	}

  private void print(String format, String... args) {
	  System.out.println(String.format(format, args));
  }
  
}
