VÝUKOVÝ PŘÍKLAD:

TECHNOLOGIE:
- SpringBoot
- Maven
- Netflix Zuul (API gateway)
- Eureka (Discovery Service)
- Spring Cloud Stream
- Apache Kafka (Message broker)
- Zookeeper (stará se a řídí brokery v clusteru Apache Kafka)
- JWT (token authetincator)
- Docker
- FeignClient
- Lombok
- Hibernate

Popis aplikace
- ApiGateway - brána k FE (Zuul, Eureka)
- AuthService: zajišťuje autentikaci a vytvoření nového uživatele (Kafka, Eureka)
- PostService: vytváří příspěvky (Kafka, Eureka)
- FeedService: získává uživatelův feed (Kafka, Eureka)
- DiscoveryService - list servis (Spring Cloud Stream with Eureka)

CO BY BYLO DOBRÉ DODĚLAT:
- FE v Reactu
- škálovat aplikaci pomocí Kubernetes
- používat databáze Casandra a Neo4j

DATABÁZE:
- tabulka USERS
- tabulka POST

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