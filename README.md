# KafkaApplication

### Introduction
Kafka Cluster를 중심으로, E-Commerce 서비스에서 고객의 결제 직후에 일어날 수 있는 데이터 플로우를 가정한 프로젝트입니다.

해당 프로젝트는 패스트캠퍼스의 "대용량 비동기 프로세스를 위한 Kafka 활용" 강의 내용을 기반으로 진행한 클론 코딩 실습으로 Kafka에 대한 이해를 높이기 위해 진행했습니다.


### Architecture
![image](https://github.com/Seung-IL-Bang/KafkaApplication/assets/87510898/6bd87da8-c107-4a2f-97c0-dab46c5b124e)



### 진행한 프로젝트

- **CheckOut Web Spring-Boot(8080)**
    - 상품 Id, 배송 주소 등을 입력받아 고객이 구매할 상품의 결제가 일어나는 시스템입니다.
    - JPA 를 이용하여 결제 데이터를 H2 DB에 저장합니다.
    - 결제 완료 이벤트 발생 시 Producer를 통해 Kafka Cluster에 이를 Publishing 합니다.
    - 학습을 위한 In-memory DB 인 H2를 사용합니다.

- **Shipment System Spring-Boot(8090)**
    - Kafka Cluster로 부터 결제 이벤트 메시지를 Consume하여 배송 프로세스를 처리하는 시스템입니다.
    - 배송 처리에 대한 정보를 자체 DB에 저장하기 위해 H2 를 사용합니다.
    
- **Kafka Streams (실시간 집계)**
    - kafka cluster로 발행된 메시지들을 실시간으로 집계하는 시스템입니다.
    - Product Id를 key로 하여, 1 minute TimeWindow를 적용하여 key 별 합산 금액을 계산합니다.
    - 1분 합산 금액에 대한 정보를 다시 별도의 topic으로 재발행합니다.

- **Kafka Connect (ElasticSearch 에 저장)**
    - kafka cluster 의 메시지들을 consume 하여 ElasticSearch에 저장합니다.
    - ElasticSearch와 이를 위한 웹 UI (=kibana)를 설치했습니다.
    - kafka Connect 구동을 통해 ElasticSearch의 Index에 데이터를 저장합니다.
 
- **JMeter**
    - 임의의 트래픽을 발생시켜 전체 프로젝트를 시연해봤습니다.
