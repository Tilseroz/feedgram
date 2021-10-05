VÝUKOVÝ PŘÍKLAD (zatím nedokončeno):

TECHNOLOGIE:
- SpringBoot
- Spring Security
- Maven
- Netflix Zuul (API gateway)
- Eureka (Discovery Service)
- Ribbon
- Spring Cloud Stream
- Apache Kafka (Message broker)
- Zookeeper (stará se a řídí brokery v clusteru Apache Kafka)
- JWT (token authetincator)
- Docker
- FeignClient
- Hystrix
- Lombok
- Hibernate
- Neo4j
- Casandra

Popis aplikace
- ApiGateway - brána k FE
- AuthService: zajišťuje autentikaci a vytvoření nového uživatele
- PostService: vytváří příspěvky
- FeedService: získává uživatelův feed
- DiscoveryService - list servis
- GraphService: ukládá vazby mezi users

CO BY BYLO DOBRÉ DODĚLAT:
- FE v Reactu
- škálovat aplikaci pomocí Kubernetes

Zuul a Eureka

Máme api gateway, na kterou jde request z FE. Ten přijme Zuul. Request vyžaduje servisu A, takže Eureka dodá všechny instance service A
a poskytne je Ribbonu. Ribbon pak vybere kterou instanci použijeme (load balancing).

Apache Kafka

Slouží pro interní komunikaci mezi services. Je to styl publish-subscribe. Máme producery, který pošlou zprávu do nějakého topicu a partiony.
Máme vlatně cluster, v něm brokery, které manažuje Zookeeper. Dále máme consumery, kteří si postupně přebírají zprávy které jsou pro ně.
Drží si hodnotu OFFSET, podle toho ví co si vzít dalšího.

V bodech Kafka:
- je to publish-subscribe messaging systém
- topic (v překladu to je kategorie)
- do topicu se posílá nějaké zpráva, ta má 4 atributy: key, value jsou povinné a dva nepovinné timestamp a headers
  -- value může být cokoliv
- máme 4 hlavní části Kafky:
  Broker: Handles all requests from clients (produce, consume, and metadata) and keeps data replicated within the cluster. There can be one or more brokers in a cluster.
  Zookeeper: Keeps the state of the cluster (brokers, topics, users).
  Producer: Sends records to a broker.
  Consumer: Consumes batches of records from the broker.