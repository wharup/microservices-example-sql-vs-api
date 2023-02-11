package microservices.examples.system.initializer;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.StopWatch;

import com.github.javafaker.Book;
import com.github.javafaker.Faker;

import lombok.extern.slf4j.Slf4j;
import microservices.examples.common.CodeService;
import microservices.examples.customer.Customer;
import microservices.examples.department.Department;
import microservices.examples.department.DepartmentService;
import microservices.examples.service.ServiceRequest;
import microservices.examples.service.ServiceRequestRepository;
import microservices.examples.system.StopWatchUtil;

@Service
@Slf4j
public class ServiceRequestDataGenerator {

	ServiceRequestRepository serviceRequestRepository;

	CustomerDataGenerator customerGenerator;
	
	UserDataGenerator userGenerator;
	
	DepartmentDataGenerator departmentGenerator;
	
	DepartmentService departmentService;
	
	CodeDataGenerator codeGenerator;

	@Autowired
	public ServiceRequestDataGenerator(ServiceRequestRepository serviceRequestRepository,
			CustomerDataGenerator customerService, UserDataGenerator userService, 
			DepartmentService departmentService, DepartmentDataGenerator departmentDataGenerator,
			CodeDataGenerator codeService) {
		super();
		this.serviceRequestRepository = serviceRequestRepository;
		this.customerGenerator = customerService;
		this.userGenerator = userService;
		this.departmentGenerator = departmentDataGenerator;
		this.departmentService = departmentService;
		this.codeGenerator = codeService;
	}

	public void createTestData(int size) {
		Faker f = new Faker(new Locale("ko"));
		List<ServiceRequest> services = new ArrayList<>();
		StopWatch sw = new StopWatch();
		for (int i = 0 ; i < size; i++) {
			services.add(aServiceRequest(f, sw));
		}
		
		try {
			sw.start("10: save all");
			log.error("{}, {}", services.get(0).getId(), "-");
			Iterable<ServiceRequest> saveAll = serviceRequestRepository.saveAll(services);
			log.error("{}, {}", services.get(0).getId(), saveAll.iterator().next().getId());
			sw.stop();
		} catch (Exception e2) {
			System.out.println(e2);
		}
		StopWatchUtil.logGroupByTaskName(sw);
		
	}

    @Autowired
    PlatformTransactionManager transactionManager;

//    @Transactional
	public void createTestData2(int size) {
        log.error("0: method started");
//		TransactionStatus tx = TransactionAspectSupport.currentTransactionStatus();
        TransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        TransactionStatus tx = transactionManager.getTransaction(transactionDefinition);
        
        log.error("1: completed:{}, new:{}, rollbackonly:{}", tx.isCompleted(), tx.isNewTransaction(), tx.isRollbackOnly());
//        tx.setRollbackOnly();
		Faker f = new Faker(new Locale("ko"));
		List<ServiceRequest> services = new ArrayList<>();
		for (int i = 0 ; i < size; i++) {
			services.add(aServiceRequest(f, new StopWatch()));
		}
		
		try {
			Iterable<ServiceRequest> saveAll = serviceRequestRepository.saveAll(services);
		} catch (Exception e2) {
			System.out.println(e2);
		}
		
		
		log.error("2: completed:{}, new:{}, rollbackonly:{}", tx.isCompleted(), tx.isNewTransaction(), tx.isRollbackOnly());
		transactionManager.rollback(tx);
//		transactionManager.commit(tx);
		log.error("3: completed:{}, new:{}, rollbackonly:{}", tx.isCompleted(), tx.isNewTransaction(), tx.isRollbackOnly());
        log.error("4: method ended");

	}

	int index = 0;
	
	private ServiceRequest aServiceRequest(Faker f, StopWatch sw) {
		Book book = f.book();
		
		ServiceRequest e = new ServiceRequest();
		e.setId(String.format("%013d", index++));
		e.setTitle(book.title());
		sw.start("0: customer");
		Customer customer = customerGenerator.getRandomCustomer();
		e.setCustomerId(customer.getId());
		sw.stop();
		sw.start("1: code");
		e.setType(codeGenerator.getRandomCode(CodeService.SR_TYPE));
		sw.stop();
		e.setDetail(f.shakespeare().hamletQuote());
		sw.start("2: code");
		e.setStatus(codeGenerator.getRandomCode(CodeService.SR_STATUS));
		sw.stop();
		sw.start("3: user");
		e.setCallAgentId(getACallCenterUser());
		sw.stop();
		sw.start("4: user");
		e.setVocAssgneeId(getAVocUser());
		sw.stop();
		sw.start("4: deprt");
		e.setVocAssgneeDeptId(departmentGenerator.getRandomDepartment().getId());
		sw.stop();
		e.setCreated(Instant.now());
		e.setUpdated(Instant.now());
		return e;
	}

	public void deleteAll() {
		serviceRequestRepository.deleteAll();
	}
	
	private String getAVocUser() {
		Department department = departmentService.findByName("영업팀");
		return userGenerator.getRandomUserByDepartmentId(department.getId()).getId();
	}

	private String getACallCenterUser() {
		Department department = departmentService.findByName("콜센터");
		return userGenerator.getRandomUserByDepartmentId(department.getId()).getId();
	}

	public void setIndex(int index) {
		this.index = index;
	}

	
}
