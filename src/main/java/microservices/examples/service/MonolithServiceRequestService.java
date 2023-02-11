package microservices.examples.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import lombok.extern.slf4j.Slf4j;
import microservices.examples.system.StopWatchUtil;

@Service
@Slf4j
public class MonolithServiceRequestService {
	
	@Autowired
	ServiceRequestDAO serviceRequestDao;
	
	@Autowired
	ServiceRequestRepository serviceRequestRepository;
	
	public List<ServiceRequest> findAllBySQL(Pageable pageable) {
		StopWatch sw = new StopWatch("findAllBySQl");
		sw.start("1. 상담이력 조회 & 조합");
		List<ServiceRequest> result = serviceRequestDao.selectAllWithJoin(pageable);
		sw.stop();
		StopWatchUtil.logGroupByTaskName(sw);
		return result;
	}
	
}
