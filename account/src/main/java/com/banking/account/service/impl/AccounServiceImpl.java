package com.banking.account.service.impl;

import com.banking.account.constants.AccountsConstants;
import com.banking.account.dto.AccountsDto;
import com.banking.account.dto.AccountsMsgDto;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class AccounServiceImpl implements IAccountService {

    CustomerRepository customerRepository;
    AccountsRepository accountsRepository;
    private final StreamBridge streamBridge;


    private static final Logger log = LoggerFactory.getLogger(AccounServiceImpl.class);

    @Override
    public void createAccount(CustomerDto customerDto) {
      Customer customer = CustomerMapper.mapToCustomer(customerDto, new Customer());
      Optional<Customer> optionalCustomer = customerRepository.findByMobileNumber(customer.getMobileNumber());
      if(optionalCustomer.isPresent()){
          throw new CustomerAlreadyExistException("Customer with mobile number " + customerDto.getMobileNumber() + " already exists");
      }


      Customer savedCustomer = customerRepository.save(customer);
      Accounts savedAccount = accountsRepository.save(createNewAccount(savedCustomer));
      sendCommunication(savedAccount,savedCustomer);
    }

    private void sendCommunication(Accounts account, Customer customer) {
        var accountsMsgDto = new AccountsMsgDto(account.getAccountNumber(), customer.getName(),
                customer.getEmail(), customer.getMobileNumber());
        log.info("Sending Communication request for the details: {}", accountsMsgDto);
        var result = streamBridge.send("sendCommunication-out-0", accountsMsgDto);
        log.info("Is the Communication request successfully triggered ? : {}", result);
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

    /**
     * @param accountNumber - Long
     * @return boolean indicating if the update of communication status is successful or not
     */
    @Override
    public boolean updateCommunicationStatus(Long accountNumber) {
        boolean isUpdated = false;
        if(accountNumber !=null ){
            Accounts accounts = accountsRepository.findById(accountNumber).orElseThrow(
                    () -> new ResourceNotFoundException("Account", "AccountNumber", accountNumber.toString())
            );
            accounts.setCommunicationSw(true);
            accountsRepository.save(accounts);
            isUpdated = true;
        }
        return  isUpdated;
    }
}
