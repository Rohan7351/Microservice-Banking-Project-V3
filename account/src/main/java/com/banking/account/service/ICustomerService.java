package com.banking.account.service;

import com.banking.account.dto.CustomerDetailsDto;


public interface ICustomerService {

    /**
     *
     *  @Param mobileNumber - Input Mobile Number
     *  @return Customer Details based on a given mobileNumber
     */
    CustomerDetailsDto fetchCustomerDetails(String mobileNumber);
}
