package com.example.backend.dto;

import com.example.backend.validator.ValidationMessage;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class WalletDto {
    private Long id;
    @NotNull(message = ValidationMessage.NOT_NULL)
    private String walletName;
    @NotNull(message = ValidationMessage.NOT_NULL)

    private String icon;
    @NotNull(message = ValidationMessage.NOT_NULL)

    private String walletDescription;
    @NotNull(message = ValidationMessage.NOT_NULL)

    private String currency;
    @NotNull(message = ValidationMessage.NOT_NULL)

    private BigDecimal amount;
}
