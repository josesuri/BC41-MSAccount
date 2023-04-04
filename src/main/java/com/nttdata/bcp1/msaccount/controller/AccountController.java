package com.nttdata.bcp1.msaccount.controller;

import com.nttdata.bcp1.msaccount.exception.MyException;
import com.nttdata.bcp1.msaccount.model.Account;
import com.nttdata.bcp1.msaccount.service.AccountService;
import com.nttdata.bcp1.msaccount.service.AccountServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.webjars.NotFoundException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
@Tag(name="Controlador para cuentas", description="progbando este controlador")
public class AccountController {

    private final AccountServiceImpl accountService;

    @Tag(name="Controlador para cuentas", description="probando para el get")
    @Operation(method="Get",summary="Get Summary")
    @ApiResponse(responseCode = "404", description ="not found", content = {
            @Content(schema=@Schema(implementation = NotFoundException.class))
    })
    @ApiResponse(responseCode = "412", description ="not found", content = {
            @Content(schema=@Schema(implementation = MyException.class))
    })
    @ApiResponse(responseCode = "200", description ="Ok")
    @GetMapping
    public Flux<Account> getAccounts(){
        return accountService.getAll();
    }

    @GetMapping("/{id}")
    public Mono<Account> getAccountById(@PathVariable("id") String id){
        return accountService.getAccountById(id);
    }

    @GetMapping("/byCustomer/{id}")
    public Flux<Account> getByIdClient(@PathVariable("id") String idCustomer){
        return accountService.getAllByIdCustomer(idCustomer);
    }

    @PostMapping("/create")
    Mono<Account> postAccount(@RequestBody Account account){
        return accountService.save(account);
    }

    @PostMapping("/associate-card/{idAccount}/{idCard}")
    Mono<Account> associateCard(@PathVariable("idAccount") String idAccount,
                                @PathVariable("idCard") String idCard){
        return accountService.associateWithCard(idAccount, idCard);
    }

    @PutMapping
    Mono<Account> updAccount(@RequestBody Account account){
        return accountService.save(account);
    }

    @DeleteMapping("/{id}")
    void dltAccount(@PathVariable("id") String id){
        accountService.delete(id);
    }
}
