package com.banking.account.service;

import com.banking.account.dto.CustomerDto;

public interface IAccountService {
   /**
    *
    * @param customerDto = CustomerDto Object
    */
    void createAccount(CustomerDto customerDto);

    CustomerDto fetchAccount(String mobileNumber);

    boolean updateAccount(CustomerDto customerDto);

    public boolean deleteAccount(String mobileNumber);
}
