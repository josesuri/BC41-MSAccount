package com.nttdata.bcp1.msaccount.proxy;

import com.nttdata.bcp1.msaccount.model.Customer;
import com.nttdata.bcp1.msaccount.model.DebitCard;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class AccountProxy {

    private final WebClient.Builder webClientBuilder = WebClient.builder();

    public Mono<Customer> getCustomer(String idCustomer){
        return webClientBuilder.build()
                .get()
                .uri("http://localhost:9020/customer/{idCustomer}", idCustomer)
                .retrieve()
                .bodyToMono(Customer.class);
    }

    public Mono<DebitCard> getDebitCard(String idDebitCard){
        return webClientBuilder.build()
                .get()
                .uri("http://localhost:9020/debit-card/{id}", idDebitCard)
                .retrieve()
                .bodyToMono(DebitCard.class);
    }
}
