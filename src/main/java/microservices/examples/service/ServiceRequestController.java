package microservices.examples.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import microservices.examples.system.StopWatchUtil;

@RestController
@RequiredArgsConstructor
@RequestMapping("services")
@Slf4j
public class ServiceRequestController {
	
	@Autowired
	ServiceRequestService serviceRequestService;
	
	@Autowired
	ServiceRequestCachedService cachedServiceRequestService;
	
	@Autowired
	MonolithServiceRequestService monolithServiceRequestService;
	
	@GetMapping("")
	public Page<ServiceRequest> search( @RequestParam(required = false) String type,  
									    Pageable pageable){
		log.error("\n\n\n===== ===== 상담이력 조회 시작: {} ===== =====", type);
		StopWatch sw = new StopWatch("전체 상담이력 조회 속도: " + type);
		sw.start();

		List<ServiceRequest> result = null;
		//1. 모노리틱 : 순수 SQL JOIN
		if ("monolith-sql".equalsIgnoreCase(type)) {
			result = monolithServiceRequestService.findAllBySQL(pageable);
		}
		//4. MSA : JAVA에서 SingleAPI JOIN
		else if ("msa-singleapi".equalsIgnoreCase(type)) {
			result = serviceRequestService.findAllWithSingleRESTApi(pageable);
		}
		//5. MSA : JAVA에서 Batch API JOIN
		else if ("msa-batchapi".equalsIgnoreCase(type)) {
			result = serviceRequestService.findAllWithBatchRESTApi(pageable);
		}
		//6. MSA : JAVA에서 Batch API w/ Cache
		else if ("msa-batchapi-cache".equalsIgnoreCase(type)) {
			result = cachedServiceRequestService.findAllWithBatchRESTApi(pageable);
		}
		//7. MSA : JAVA에서 SingleAPI w/ Cache
		else if ("msa-singleapi-cache".equalsIgnoreCase(type)) {
			result = cachedServiceRequestService.findAllWithSingleRESTApi(pageable);
		}
		else {
			String msg = String.format("ERROR! REQUEST TYPE IS INCORRECT! %s", type);
			log.error(msg);
			return new PageImpl<ServiceRequest>(new ArrayList<ServiceRequest>(), pageable, 0);
		}
		sw.stop();
		StopWatchUtil.log(sw);
		log.error("\n===== ===== 상담이력 조회 완료 ===== =====");
		return new PageImpl<>(result, pageable, result.size());
		
	}
}
