package com.dws.challenge.service;

import com.dws.challenge.domain.Account;
import com.dws.challenge.repository.AccountsRepositoryInMemory;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest
class AccountTransferServiceTest {
    @MockBean
    private AccountsRepositoryInMemory accountRepository;
    @MockBean
    private NotificationService notificationService;

    @Autowired
    private AccountTransferService transferService;


    @Test
    void testTransferMoney() {
        // Set up test data
        Account accountFrom = new Account("1", new BigDecimal("100"));
        Account accountTo = new Account("2", new BigDecimal("50"));

        Mockito.when(accountRepository.getAccount("1")).thenReturn(accountFrom);
        Mockito.when(accountRepository.getAccount("2")).thenReturn(accountTo);
        transferService.transferMoney(accountFrom.getAccountId(), accountTo.getAccountId(), new BigDecimal("30"));

        // Verify account balances
        assertEquals(new BigDecimal(70), accountFrom.getBalance());
        assertEquals(new BigDecimal(80), accountTo.getBalance());

        // Verify notifications
        verify(notificationService, times(2)).notifyAboutTransfer(Mockito.any(),Mockito.any());
}}