# microservices-example-sql-vs-api


CommonService, ServiceRequestExperimentService 실행 후 다음과 같이 API 호출

	//모놀리식 - ① SQL, 1000건
	curl 'http://localhost:8081/services?type=monolith-sql&page=0&size=1000'
	//마이크로서비 - ② API 건별 요청, 100건
	curl 'http://localhost:8081/services?type=msa-singleapi&page=0&size=100'
	//마이크로서비 - ③ API 일괄 요청, 1000건
	curl 'http://localhost:8081/services?type=msa-batchapi&page=0&size=1000'
	//마이크로서비 - ④ API 일괄 요청 & 로컬 캐시, 1000건
	curl 'http://localhost:8081/services?type=msa-batchapi-cache&page=0&size=1000'
