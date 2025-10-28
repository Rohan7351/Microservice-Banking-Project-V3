package com.banking.account.service.impl;

import com.banking.account.constants.AccountsConstants;
import com.banking.account.dto.AccountsDto;
import com.banking.account.dto.CustomerDto;
import com.banking.account.entity.Accounts;
import com.banking.account.entity.Customer;
import com.banking.account.exception.CustomerAlreadyExistException;
import com.banking.account.exception.ResourceNotFoundException;
import com.banking.account.mapper.AccountMapper;
import com.banking.account.mapper.CustomerMapper;
import com.banking.account.repository.AccountsRepository;
import com.banking.account.repository.CustomerRepository;
import com.banking.account.service.IAccountService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class AccounServiceImpl implements IAccountService {

    CustomerRepository customerRepository;
    AccountsRepository accountsRepository;

    @Override
    public void createAccount(CustomerDto customerDto) {
      Customer customer = CustomerMapper.mapToCustomer(customerDto, new Customer());
      Optional<Customer> optionalCustomer = customerRepository.findByMobileNumber(customer.getMobileNumber());
      if(optionalCustomer.isPresent()){
          throw new CustomerAlreadyExistException("Customer with mobile number " + customerDto.getMobileNumber() + " already exists");
      }


      Customer savedCustomer = customerRepository.save(customer);
      accountsRepository.save(createNewAccount(savedCustomer));
    }

/**
 *   @param mobileNumber - Input Mobile Number
 *   @return Accounts Details based on a given mobileNumber
 */
    @Override
    public CustomerDto fetchAccount(String mobileNumber) {

        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                ()->new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );

        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                ()->new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );

        CustomerDto customerDto = CustomerMapper.mapToCustomerDto(customer, new CustomerDto());
        customerDto.setAccountsDto(AccountMapper.mapToAccountsDto(accounts, new AccountsDto()));

        return customerDto;
    }


    /**
 *     @param customer  - Customer object
 *     @return the new Account details
 */
  private Accounts createNewAccount(Customer customer) {
      Accounts newAccounts = new Accounts();
      newAccounts.setCustomerId(customer.getCustomerId());
      long randomAccNumber = 1000000000L + new Random().nextInt(900000000);

      newAccounts.setAccountNumber(randomAccNumber);
      newAccounts.setAccountType(AccountsConstants.SAVINGS);
      newAccounts.setBranchAddress(AccountsConstants.ADDRESS);


      return newAccounts;
    }

    /**
     * @param customerDto - CustomerDto Object
     * @return boolean indicating if the update of Account details is successful or not
     */
    @Override
    public boolean updateAccount(CustomerDto customerDto) {
        boolean isUpdated = false;
        AccountsDto accountsDto = customerDto.getAccountsDto();
        if(accountsDto !=null ){
            Accounts accounts = accountsRepository.findById(accountsDto.getAccountNumber()).orElseThrow(
                    () -> new ResourceNotFoundException("Account", "AccountNumber", accountsDto.getAccountNumber().toString())
            );
            AccountMapper.mapToAccounts(accountsDto, accounts);
            accounts = accountsRepository.save(accounts);

            Long customerId = accounts.getCustomerId();
            Customer customer = customerRepository.findById(customerId).orElseThrow(
                    () -> new ResourceNotFoundException("Customer", "CustomerID", customerId.toString())
            );
            CustomerMapper.mapToCustomer(customerDto,customer);
            customerRepository.save(customer);
            isUpdated = true;
        }
        return  isUpdated;
    }

    /**
     * @param mobileNumber - Input Mobile Number
     * @return boolean indicating if the delete of Account details is successful or not
     */
    @Override
    public boolean deleteAccount(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        accountsRepository.deleteByCustomerId(customer.getCustomerId());
        customerRepository.deleteById(customer.getCustomerId());
        return true;
    }

}
