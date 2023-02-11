package microservices.examples.common;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import microservices.examples.system.CacheProxy;

@Service
@Slf4j
public class CodeService {
	public static final String SR_STATUS = "SR_STATUS";
	public static final String SR_TYPE = "SR_TYPE";
	public static final String CUSTOMER_GENDER = "GENDER";
	public static final String CUSTOMER_TYPE = "CUST_TYPE";

	CodeRepository codeRepository;

    private CacheProxy<String> codeCache;

    @Autowired
	public CodeService(CodeRepository codeRepository, CacheManager cacheManager) {
		super();
		this.codeRepository = codeRepository;
		this.codeCache = new CacheProxy<>(cacheManager, "codes");
	}

	public Map<String, Code> findByCodeType(String codeType) {
		List<Code> codes = codeRepository.findByCodeType(codeType);
		Map<String, Code> result = new HashMap<>();

		Iterator<Code> iterator = codes.iterator();
		while (iterator.hasNext()) {
			Code code = iterator.next();
			result.put(code.getCode(), code);
		}
		return result;
	}

	public String getCodeValue(String codeType, String type) {

		Map<String, Code> typeCodes = findByCodeType(codeType);
		Code code = typeCodes.get(type);
		if (code == null) {
			log.debug("coudn't get code value for {}:{}", codeType, type);
			return "";
		}
		return code.getValue();
	}

	public String getCachedCodeValue(String codeType, String type) {
		String key = codeType + "^" + type;
		String value = codeCache.get(key);
		if (value != null) {
			return value;
		}
		value = getCodeValue(codeType, type);
		if (isEmpty(value)) {
			log.info("CANNOT FIND code value for {}", codeType);
			return "";
		}
		codeCache.put(key, value);
		return value;
	}
	
	private boolean isEmpty(String value) {
		if (value == null) return true;
		if (value.isEmpty()) return true;
		return false;
	}

	public Iterable<Code> findAll() {
		return codeRepository.findAll();
	}

	public Code find(String codeType, String code) {
		return codeRepository.findByCodeTypeAndCode(codeType, code);
	}

	public Iterable<Code> findByCodeTypes(List<String> codeTypes) {
		return codeRepository.findByCodeTypeIn(codeTypes);
	}

}
