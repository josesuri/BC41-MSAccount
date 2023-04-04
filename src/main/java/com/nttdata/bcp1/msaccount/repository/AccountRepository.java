package com.nttdata.bcp1.msaccount.repository;

import com.nttdata.bcp1.msaccount.model.Account;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface AccountRepository extends ReactiveMongoRepository<Account, String> {
    public Mono<Account> findByIdCustomerAndAccountType(String idCustomer, String accountType);
    public Flux<Account> findAllByIdCustomer(String idCustomer);
}
