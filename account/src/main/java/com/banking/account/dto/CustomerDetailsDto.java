package com.banking.account.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Schema(name = "CustomerDetails", description = "Schema to hold Customer, Account, Cards and Loans information")
public class CustomerDetailsDto {
    @Schema(description = "Name of the customer", example = "Rohan Rana")
    @NotEmpty(message="Name cannot be null or empty")
    private String name;

    @Schema(description = "Email address of the customer", example = "rohan.rana.7351@gmail.com")
    @NotEmpty(message = "Email address cannot be null or empty")
    @Email(message = "Email address should be at valid value")
    private String email;

    @Schema(description = "Mobile number of customer", example = "9410361120")
    @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digit")
    private String mobileNumber;

    @Schema(description = "Account details of customer")
    private AccountsDto accountsDto;

    @Schema(description = "Cards details of customer")
    private CardsDto cardsDto;

    @Schema(description = "Loans details of customer")
    private LoansDto loansDto;


}
