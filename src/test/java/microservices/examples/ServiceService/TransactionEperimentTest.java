package microservices.examples.ServiceService;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.StopWatch;
import org.springframework.util.StopWatch.TaskInfo;

import lombok.extern.slf4j.Slf4j;
import microservices.examples.Application;
import microservices.examples.service.ServiceRequestRepository;
import microservices.examples.system.initializer.CodeDataGenerator;
import microservices.examples.system.initializer.CustomerDataGenerator;
import microservices.examples.system.initializer.DepartmentDataGenerator;
import microservices.examples.system.initializer.ServiceRequestDataGenerator;
import microservices.examples.system.initializer.UserDataGenerator;

@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
@Slf4j
class TransactionEperimentTest {

	@Autowired
	CodeDataGenerator codeGenerator;
	
	@Autowired
	DepartmentDataGenerator departmentGenerator;
	
	@Autowired
	UserDataGenerator userGenerator;
	
	@Autowired
	CustomerDataGenerator customerGenerator;
	
	@Autowired
	ServiceRequestDataGenerator serviceRequestService;

	@Autowired
	ServiceRequestRepository serviceRequestRepository;

	@Test
	public void 서비스데이터추가() {
		long maxId = serviceRequestRepository.getMaxId() + 1;
		serviceRequestService.setIndex((int)maxId);
		
        log.error("-1: method call starting");
		serviceRequestService.createTestData2(1);
		log.error("-2: method call ended");
	}
}










