package microservices.examples.system.initializer;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import microservices.examples.common.Code;
import microservices.examples.common.CodeRepository;
import microservices.examples.system.CacheProxy;

@Service
@Slf4j
public class CodeDataGenerator {
	public static final String SR_STATUS = "SR_STATUS";
	public static final String SR_TYPE = "SR_TYPE";
	public static final String CUSTOMER_GENDER = "GENDER";
	public static final String CUSTOMER_TYPE = "CUST_TYPE";

	CodeRepository codeRepository;

    Map<String, List<Code>> codesByType = new HashMap<>();

    @Autowired
	public CodeDataGenerator(CodeRepository codeRepository) {
		super();
		this.codeRepository = codeRepository;
	}

	
	public String getRandomCode(String codeType) {
		List<Code> codes = codesByType.get(codeType);
		if (codes == null) {
			codes = codeRepository.findByCodeType(codeType);
			codesByType.put(codeType, codes);
		}
		int size = codes.size();
		int index = getRandomNumber(0, size);
		return codes.get(index).getCode();
	}

	private int getRandomNumber(int min, int max) {
		return (int) ((Math.random() * (max - min)) + min);
	}

	public void deleteAll() {
		codeRepository.deleteAll();
	}

	public void createBasicData() {

		List<Code> services = new ArrayList<>();
		services.add(aCode("SR_STATUS", "CREATED", "접수"));
		services.add(aCode("SR_STATUS", "PROGRESS", "진행중"));
		services.add(aCode("SR_STATUS", "COMPLETED", "완료"));
		services.add(aCode("SR_STATUS", "DELAYED", "지연"));

		services.add(aCode("SR_TYPE", "SIMPLE_QUESTION", "단순문의"));
		services.add(aCode("SR_TYPE", "COMPLAIN", "불만"));
		services.add(aCode("SR_TYPE", "SERVICE_REQUEST", "서비스요청"));

		services.add(aCode("GENDER", "MALE", "남자"));
		services.add(aCode("GENDER", "FEMAIL", "여자"));

		services.add(aCode("CUST_TYPE", "LOCAL", "LOCAL"));
		services.add(aCode("CUST_TYPE", "FOREIGN", "FOREIGN"));

		Iterable<Code> saveAll = codeRepository.saveAll(services);
	}

	private Code aCode(String codeType, String code, String value) {
		Code c = new Code();
		c.setCodeType(codeType);
		c.setActive("Y");
		c.setCode(code);
		c.setValue(value);
		c.setCreated(Instant.now());
		c.setUpdated(Instant.now());
		return c;
	}

	public Iterable<Code> findAll() {
		return codeRepository.findAll();
	}

}
