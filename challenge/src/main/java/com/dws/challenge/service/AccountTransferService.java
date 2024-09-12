package com.dws.challenge.service;

import com.dws.challenge.domain.Account;
import com.dws.challenge.repository.AccountsRepositoryInMemory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AccountTransferService {

        private AccountsRepositoryInMemory accountRepository;

        private NotificationService notificationService;

    public AccountTransferService(AccountsRepositoryInMemory accountRepository, NotificationService notificationService) {
        this.accountRepository = accountRepository;
        this.notificationService = notificationService;
    }

    public void transferMoney(String accountFromId, String accountToId, BigDecimal amount) {

                if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                    throw new IllegalArgumentException("Transfer amount must be positive.");
                }

            Account accountFrom = accountRepository.getAccount(accountFromId);
            Account accountTo = accountRepository.getAccount(accountToId);
            synchronized (accountFrom) {
                synchronized (accountTo) {
                    if (accountFrom.getBalance().compareTo(amount) < 0) {
                        throw new IllegalArgumentException("Insufficient balance in account " + accountFromId);
                    }

                    accountFrom.debit(amount);
                    accountTo.credit(amount);

                    notificationService.notifyAboutTransfer(accountFrom, "Transferred the amount of Rs " + amount +
                            " successfully to account with ID " +accountTo.getAccountId());
                    notificationService.notifyAboutTransfer(accountTo, "Received the amount of Rs " +amount+
                            " successfully from account with ID" +accountFrom.getAccountId());
                }
            }
        }
    }

