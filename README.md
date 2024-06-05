# microservices-example-sql-vs-api


## 성능 비교를 위한 구성
![성능 비교를 위한 구성도](https://github.com/wharup/microservices-example-sql-vs-api/blob/main/%EA%B5%AC%EC%84%B1%EB%8F%84.png "성능 비교를 위한 구성도") 

* ① [실험용 상담 서비스: microservices-example-sql-vs-api](https://github.com/wharup/microservices-example-sql-vs-api/tree/main)
    - 테스트에 사용된 DB 스키마와 샘플 데이터를 생성하는 기능 포함
* ② [공통 서비스: microservices-example-common-service](https://github.com/wharup/microservices-example-common-service/tree/main)
* ③ Postgre SQL 9.4 (특별히 DB 의존적인 기능은 없음)


## 예시 코드에 대한 전반적인 안내
[예시 코드 안내](https://github.com/wharup/book-examples "예시 코드 안내")


## 실행 API
CommonService, ServiceRequestExperimentService 실행 후 다음과 같이 API 호출

	//모놀리식 - ① SQL, 1000건
	curl 'http://localhost:8081/services?type=monolith-sql&page=0&size=1000'
	//마이크로서비 - ② API 건별 요청, 100건
	curl 'http://localhost:8081/services?type=msa-singleapi&page=0&size=100'
	//마이크로서비 - ③ API 일괄 요청, 1000건
	curl 'http://localhost:8081/services?type=msa-batchapi&page=0&size=1000'
	//마이크로서비 - ④ API 일괄 요청 & 로컬 캐시, 1000건
	curl 'http://localhost:8081/services?type=msa-batchapi-cache&page=0&size=1000'





