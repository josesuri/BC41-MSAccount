package com.nttdata.bcp1.msaccount.service;

import com.nttdata.bcp1.msaccount.model.Account;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountService {
    Flux<Account> getAll();
    Flux<Account> getAllByIdCustomer(String idCustomer);
    Mono<Account> associateWithCard(String idAccount, String idCard);
    Mono<Account> getAccountById(String id);
    Mono<Account> getAccountByIdCustomerAndAccountType(String idCustomer, String accountType);
    Mono<Account> save(Account account);
    void delete(String id);
}
