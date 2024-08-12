package com.example.backend.dto;

import com.example.backend.validator.ValidationMessage;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class TransactionDto {
    private Long id;

    private String note;
    @Min(value = 0)
    private BigDecimal amount;
    private LocalDate datetime;
    @NotNull(message = ValidationMessage.NOT_NULL)
    private Long categoryId;
    @NotNull(message = ValidationMessage.NOT_NULL)
    private Long walletId;

}
