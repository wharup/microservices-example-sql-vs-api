package microservices.examples.ServiceService;

import static org.mockito.Mockito.after;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.javafaker.Faker;
import com.github.javafaker.service.RandomService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class TempTest {

	Faker f = new Faker(new Locale("ko"));

	@BeforeEach
	void setup() {
	}

	@Test
	void checkMemorySize_10000000() {
		getAndPrintSize(1000 * 1000 * 10);
	}
	
	
	@Test
	void checkMemorySize_1000000() {
		getAndPrintSize(1000 * 1000);
	}
	@Test
	void checkMemorySize_100000() {
		getAndPrintSize(1000 * 100);
	}
	@Test
	void checkMemorySize_10000() {
		int count = 1000 * 10;
		getAndPrintSize(1000 * 10);
	}
	
	@Test
	void checkMemorySize_1000() {
		getAndPrintSize(1000);
	}
	private void getAndPrintSize(int count) {
		long beforeUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
		Map<String, Map<String, String>> data = new HashMap<>(count/5);
		
		String lastKey = "";
		for (int i = 0; i < count; i++) {
			Code code = aCode();
			if (i%5 == 0) {
				lastKey = code.codeType;
				Map<String, String> names = new HashMap<>();
				data.put(lastKey, names);
				names.put(code.code, code.value);
			} else {
				Map<String, String> names = data.get(lastKey);
				names.put(code.code, code.value);
			}
		}
		long afterUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
		log.error("	{}:	{}	-	{}=	{}", count, beforeUsedMem/1024/1024, afterUsedMem/1024/1024, (afterUsedMem - beforeUsedMem)/1024/1024);
	}
	

	Code aCode() {
		RandomService r = f.random();
		return new Code(r.hex(10), r.hex(10), r.hex(10));
	}
	
	class Code {
		private String codeType;
		private String code;
		private String value;
		public Code(String codeType, String code, String value) {
			super();
			this.codeType = codeType;
			this.code = code;
			this.value = value;
		}
	}

	
}
