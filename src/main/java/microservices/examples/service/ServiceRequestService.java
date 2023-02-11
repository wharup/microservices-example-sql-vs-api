package microservices.examples.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import lombok.extern.slf4j.Slf4j;
import microservices.examples.common.Code;
import microservices.examples.common.CodeService;
import microservices.examples.customer.Customer;
import microservices.examples.customer.CustomerService;
import microservices.examples.department.Department;
import microservices.examples.department.DepartmentService;
import microservices.examples.gateway.CodeGateway;
import microservices.examples.gateway.CustomerGateway;
import microservices.examples.gateway.DepartmentGateway;
import microservices.examples.gateway.UserGateway;
import microservices.examples.system.StopWatchUtil;
import microservices.examples.user.User;
import microservices.examples.user.UserService;

@Service
@Slf4j
public class ServiceRequestService {

  protected ServiceRequestDAO serviceRequestDAO;

  protected CustomerService customerService;
  protected UserService userService;
  protected DepartmentService departmentService;
  protected CodeService codeService;

  protected CustomerGateway customerGateway;
  protected UserGateway userGateway;
  protected DepartmentGateway departmentGateway;
  protected CodeGateway codeGateway;

  public ServiceRequestService() {
    super();
  }

  @Autowired
  public ServiceRequestService(ServiceRequestDAO serviceRequestDAO,
        CustomerService customerService, UserService userService,
        DepartmentService departmentService, CodeService codeService,
        CustomerGateway customerGateway, UserGateway userGateway,
        DepartmentGateway departmentGateway, CodeGateway codeGateway) {
    super();
    this.serviceRequestDAO = serviceRequestDAO;
    this.customerService = customerService;
    this.userService = userService;
    this.departmentService = departmentService;
    this.codeService = codeService;

    this.customerGateway = customerGateway;
    this.userGateway = userGateway;
    this.departmentGateway = departmentGateway;
    this.codeGateway = codeGateway;
  }

  public List<ServiceRequest> findAllWithSingleRESTApi(Pageable pageable) {
    // 1. 상담이력 조회
    List<ServiceRequest> result = serviceRequestDAO.selectAll(pageable);

    // 2. 부가정보를 REST API로 조회하여 조합
    Iterator<ServiceRequest> itr = result.iterator();
    while (itr.hasNext()) {
      ServiceRequest next = itr.next();
      next.setStatusName(getStatusName(next.getStatus()));
      next.setTypeName(getTypeName(next.getType()));
      next.setCustomerName(getCustomerName(next.getCustomerId()));
      next.setCallAgentName(getUserName(next.getCallAgentId()));
      next.setVocAssgneeName(getUserName(next.getVocAssgneeId()));
      next.setVocAssgneeDeptName(
            getDepartmentName(next.getVocAssgneeDeptId()));
    }
    return result;
  }

  public List<ServiceRequest> findAllWithBatchRESTApi(Pageable pageable) {
    StopWatch sw = new StopWatch("findAllBySQl");
    sw.start("00 select service requests");
    // 1. 상담이력 조회
    List<ServiceRequest> result = serviceRequestDAO.selectAll(pageable);
    sw.stop();
    StopWatchUtil.logGroupByTaskName(sw);

    // 2. 부가정보 ID 취합
    Set<String> customerIds = new HashSet<>();
    Set<String> userIds = new HashSet<>();
    Set<String> departmentIds = new HashSet<>();

    Iterator<ServiceRequest> itr = result.iterator();
    while (itr.hasNext()) {
      ServiceRequest next = itr.next();
      customerIds.add(next.getCustomerId());
      userIds.add(next.getCallAgentId());
      userIds.add(next.getVocAssgneeId());
      departmentIds.add(next.getVocAssgneeDeptId());
    }

    // 3. 부가정보를 REST API로 조회
    Map<String, Map<String, String>> codeTypes = getCodeNamesByTypes(
          "SR_STATUS", "SR_TYPE");
    Map<String, String> srTypeCodes = codeTypes.get("SR_TYPE");
    Map<String, String> srStatusCodes = codeTypes.get("SR_STATUS");
    Map<String, String> customerNames = getCustomerNames(customerIds);
    Map<String, String> userNames = getUserNames(userIds);
    Map<String, String> departmentNames = getDepartmentNames(departmentIds);

    // 4. 상담이력에 부가정보 조합
    Iterator<ServiceRequest> itr2 = result.iterator();
    while (itr2.hasNext()) {
      ServiceRequest next = itr2.next();
      next.setStatusName(srStatusCodes.get(next.getStatus()));
      next.setTypeName(srTypeCodes.get(next.getType()));
      next.setCustomerName(customerNames.get(next.getCustomerId()));
      next.setCallAgentName(userNames.get(next.getCallAgentId()));
      next.setVocAssgneeName(userNames.get(next.getVocAssgneeId()));
      next.setVocAssgneeDeptName(
            departmentNames.get(next.getVocAssgneeDeptId()));
    }
    return result;
  }

  public Map<String, String> getDepartmentNames(Set<String> departmentIds) {
    Map<String, String> names = new HashMap<>();
    List<Department> departments = departmentGateway
          .getDepartments(departmentIds);

    for (Department department : departments) {
      names.put(department.getId(), department.getName());
    }
    return names;
  }

  public Map<String, String> getUserNames(Set<String> userIds) {
    Map<String, String> names = new HashMap<>();
    List<User> users = userGateway.getUsers(userIds);

    for (User user : users) {
      names.put(user.getId(), user.getName());
    }
    return names;
  }

  public Map<String, String> getCustomerNames(Set<String> customerIds) {
    Map<String, String> customerNames = new HashMap<>();
    // 1. REST API로 고객 이름 조회
    List<Customer> customers = customerGateway.getCustomers(customerIds);

    // 2. 조회한 고객 정보를 Map 형태로 변환
    for (Customer customer : customers) {
      customerNames.put(customer.getId(), customer.getName());
    }
    return customerNames;
  }

  public Map<String, Map<String, String>> getCodeNamesByTypes(
        String... codeTypes) {
    List<Code> codes = codeGateway.getCodesByType(codeTypes);

    Map<String, Map<String, String>> names = new HashMap<>();
    for (String codeType : codeTypes) {
      names.put(codeType, new HashMap<String, String>());
    }

    for (Code code : codes) {
      Map<String, String> codeType = names.get(code.getCodeType());
      codeType.put(code.getCode(), code.getValue());
    }
    return names;
  }

  public String getTypeName(String type) {
    return codeGateway.getCode("SR_TYPE", type).getValue();
  }

  public String getStatusName(String status) {
    return codeGateway.getCode("SR_STATUS", status).getValue();
  }

  public String getCustomerName(String customerId) {
    Customer body = customerGateway.getCustomer(customerId);
    return body.getName();
  }

  public String getUserName(String callAgentId) {
    return userGateway.getUser(callAgentId).getName();
  }

  public String getDepartmentName(String deptId) {
    Department department = departmentGateway.getDepartment(deptId);
    return department.getName();
  }

}
