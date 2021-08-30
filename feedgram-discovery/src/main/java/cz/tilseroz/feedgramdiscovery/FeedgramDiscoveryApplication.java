package cz.tilseroz.feedgramdiscovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * VYSVETLENI souboru YAML
 *   register-with-eureka
 *   - slouzi k tomu, zda client sam sebe zaregistruje a tim se stane "objevitelnym"
 *   - to samo o sobe ale neznamena, ze tento klient bude ziskavat informace o end-pointech jinych sluzeb a tim padem ze se k nim bude moct pripojit
 *
 *   fetch-registry
 *   - slouzi k tomu, zda se client pripoji k serverum Eureka, aby mohla stahovat informace o dalsich end-pointech. Muze tak ucinit, aniz by se zaregistroval
 *
 *  Defaultni hodnota obou je true, my nastavujeme nyní na false, protoze jsme discovery service a nechceme, aby sebe sama registrovala.
 */
@SpringBootApplication
@EnableEurekaServer
public class FeedgramDiscoveryApplication {

	public static void main(String[] args) {
		SpringApplication.run(FeedgramDiscoveryApplication.class, args);
	}

}
