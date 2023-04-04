package com.nttdata.bcp1.msaccount.service;

import com.nttdata.bcp1.msaccount.MsAccountApplication;
import com.nttdata.bcp1.msaccount.model.Account;
import com.nttdata.bcp1.msaccount.model.Customer;
import com.nttdata.bcp1.msaccount.model.DebitCard;
import com.nttdata.bcp1.msaccount.proxy.AccountProxy;
import com.nttdata.bcp1.msaccount.repository.AccountRepository;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService{
    private static final Logger logger = LogManager.getLogger(MsAccountApplication.class);
    private final AccountRepository repository;

    private final AccountProxy accountProxy = new AccountProxy();

    @Override
    public Flux<Account> getAll() {
        return repository.findAll();
    }

    @Override
    public Mono<Account> getAccountById(String id) {
        return repository.findById(id);
    }

    @Override
    public Flux<Account> getAllByIdCustomer(String idCustomer) {
        return repository.findAllByIdCustomer(idCustomer);
    }

    @Override
    public Mono<Account> save(Account account) {
        //return repository.save(account);
        logger.info("Account Type = " + account.getAccountType());
        switch (account.getAccountType()) {
            case "DEBIT":
                //return Mono.error(() -> new IllegalArgumentException("Entro a debit"));
                return createCuentaAhorro(account).flatMap(repository::save);
            case "CURRENT_ACCOUNT":
                return createCuentaCorriente(account).flatMap(repository::save);
            case "FIXED_DEPOSIT":
                return createCuentaPlazoFijo(account).flatMap(repository::save);
            default:
                return Mono.error(() -> new IllegalArgumentException("Invalid Account type"));
        }
    }

    @Override
    public void delete(String id) {
        repository.deleteById(id).subscribe();
    }

    @Override
    public Mono<Account> getAccountByIdCustomerAndAccountType(String idCustomer, String accountType) {
        return repository.findByIdCustomerAndAccountType(idCustomer, accountType);
    }

    @Override
    public Mono<Account> associateWithCard(String idAccount, String idCard){
        return getAccountById(idAccount).flatMap(resp->putCardIntoAccount(resp, idCard))
                .flatMap(this::save);
    }


    //PRODUCT VALIDATION METHODS
    public Mono<Account> createCuentaAhorro(Account account){
        return getCustomer(account).flatMap(customer -> {

            switch (customer.getCustomerType()) {
                case "PERSONAL":
                    //return Mono.error(() -> new IllegalArgumentException("Entro a PErsonal"));
                    return Mono.just(account).flatMap(this::clientHasAccountAlready);
                case "BUSINESS":
                    return Mono.error(() -> new IllegalArgumentException("Business Client can't have this account type"));
                default:
                    return Mono.error(() -> new IllegalArgumentException("Invalid Client type"));
            }

        });
    }

    public Mono<Account> createCuentaCorriente(Account account){
        return getCustomer(account).flatMap(customer -> {

            switch (customer.getCustomerType()) {
                case "PERSONAL":
                    return Mono.just(account).flatMap(this::clientHasAccountAlready);
                case "BUSINESS":
                    return Mono.just(account);
                default:
                    return Mono.error(() -> new IllegalArgumentException("Invalid Client type"));
            }

        });
    }

    public Mono<Account> createCuentaPlazoFijo(Account account){
        return getCustomer(account).flatMap(customer -> {

            switch (customer.getCustomerType()) {
                case "PERSONAL":
                    return Mono.just(account).flatMap(this::clientHasAccountAlready);
                case "BUSINESS":
                    return Mono.error(() -> new IllegalArgumentException("Business Client can't have this account type"));
                default:
                    return Mono.error(() -> new IllegalArgumentException("Invalid Client type"));
            }

        });
    }

    //PRODUCT UTIL METHODS
    public Mono<Customer> getCustomer(Account account){
        return accountProxy.getCustomer(account.getIdCustomer());
    }

    public Mono<DebitCard> getDebitCard(String idDebitCard){
        return accountProxy.getDebitCard(idDebitCard);
    }

    public Mono<Account> clientHasAccountAlready(Account account){
        logger.info("getAccountByIdCustomerAndAccountType = " + Mono.just(getAccountByIdCustomerAndAccountType(account.getIdCustomer(), account.getAccountType())));
        return getAccountByIdCustomerAndAccountType(account.getIdCustomer(), account.getAccountType())
                .switchIfEmpty(Mono.just(new Account()))
                .flatMap(resp -> {
                    if(resp.getId()==null || resp.getId().equals(account.getId())) {
                        return Mono.just(account);
                    }
                    return Mono.error(()->new IllegalArgumentException("Client has a this account type already"));
                });
    }

    public Mono<Account> putCardIntoAccount(Account account,String idCard){
        return cardExist(idCard).flatMap(resp->{
            if(resp.getIdCustomer().equals(account.getIdCustomer())) {
                account.setIdCard(idCard);
                return Mono.just(account);
            }else {
                return Mono.error(()->new IllegalArgumentException("This client is not the owner of this debit card"));
            }
        });
    }

    public Mono<DebitCard> cardExist(String idCard){
        return accountProxy.getDebitCard(idCard).switchIfEmpty(Mono.just(new DebitCard()))
                .flatMap(resp -> {
                    return resp.getId()==null ? Mono.error(()->new IllegalArgumentException("Credit card doesn't exist"))
                            : Mono.just(resp);
                });
    }
}