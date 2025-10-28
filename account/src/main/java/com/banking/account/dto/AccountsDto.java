package com.banking.account.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Schema(name = "Account", description = "Schema to hold Account information")
public class AccountsDto {
    @Schema(description = "Account number of bank account", example = "342516789")
    @NotEmpty(message = "Account no cannot be null or empty")
    @Pattern(regexp = "(^$|[0-9]{10})", message = "Account no must be 10 digit")
    private Long accountNumber;

    @Schema(description = "Account type of the bank account", example = "Savings")
    @NotEmpty(message = "Account type cannot be null or empty")
    private String accountType;

    @Schema(description = "Branch address for bank account", example = "Whitefield, Bangalore")
    @NotEmpty(message = "Branch Address cannot be null or empty")
    private String branchAddress;

}
