package com.goorm.tricountapi.controller;

import com.goorm.tricountapi.dto.ExpenseRequest;
import com.goorm.tricountapi.dto.ExpenseResult;
import com.goorm.tricountapi.model.ApiResponse;
import com.goorm.tricountapi.model.Settlement;
import com.goorm.tricountapi.repository.ExpenseRepository;
import com.goorm.tricountapi.service.ExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ExpenseController {
    private final ExpenseService expenseService;

    @PostMapping("/expenses/add")
    public ApiResponse<ExpenseResult> addExpenseToSettlement(
            @RequestBody @Valid ExpenseRequest expenseRequest
    ) {
        return new ApiResponse<ExpenseResult>().ok(expenseService.addExpense(expenseRequest));
    }
}
