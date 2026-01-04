package com.banking.account.service.impl;

import com.banking.account.dto.AccountsDto;
import com.banking.account.dto.CardsDto;
import com.banking.account.dto.CustomerDetailsDto;
import com.banking.account.dto.LoansDto;
import com.banking.account.entity.Accounts;
import com.banking.account.entity.Customer;
import com.banking.account.exception.ResourceNotFoundException;
import com.banking.account.mapper.AccountMapper;
import com.banking.account.mapper.CustomerMapper;
import com.banking.account.repository.AccountsRepository;
import com.banking.account.repository.CustomerRepository;
import com.banking.account.service.ICustomerService;
import com.banking.account.service.client.CardsFeignCilent;
import com.banking.account.service.client.LoansFeignCilent;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements ICustomerService {


    private CardsFeignCilent cardsFeignCilent;
    private LoansFeignCilent loansFeignCilent;
    private AccountsRepository accountsRepository;
    private CustomerRepository customerRepository;
    /**
     *
     *  @Param mobileNumber - Input Mobile Number
     *  @return Customer Details based on a given mobileNumber
     */
    @Override
    public CustomerDetailsDto fetchCustomerDetails(String correlationId,String mobileNumber) {

        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                ()->new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );

        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                ()->new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );

        CustomerDetailsDto customerDetailsDto = CustomerMapper.mapToCustomerDetailsDto(customer, new CustomerDetailsDto());
        customerDetailsDto.setAccountsDto(AccountMapper.mapToAccountsDto(accounts, new AccountsDto()));

        ResponseEntity<LoansDto> loansDtoResponseEntity = loansFeignCilent.fetchLoanDetails(correlationId, mobileNumber);
        customerDetailsDto.setLoansDto(loansDtoResponseEntity.getBody());

        ResponseEntity<CardsDto> cardsDtoResponseEntity = cardsFeignCilent.fetchCardDetails(correlationId, mobileNumber);
        customerDetailsDto.setCardsDto(cardsDtoResponseEntity.getBody());

        return customerDetailsDto;
    }
}
