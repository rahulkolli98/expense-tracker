package com.ex.expensetracker.Service;

import com.ex.expensetracker.Exception.ExpenseExistsException;
import com.ex.expensetracker.Exception.ExpenseNotFoundException;
import com.ex.expensetracker.Repository.ExpenseRepository;
import com.ex.expensetracker.model.Expense;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    public List<Expense> getExpenses() {
        return expenseRepository.findAll();
    }

    public Expense getExpenseById(Long id) throws ExpenseNotFoundException {
        return expenseRepository.findById(id)
                .orElseThrow(() -> new ExpenseNotFoundException("Expense does not exist..."));
    }

    public Expense getExpenseByName(String name) throws ExpenseNotFoundException {
        return expenseRepository.findExpenseByName(name)
                .orElseThrow(() -> new ExpenseNotFoundException("Expense does not exist..."));
    }

    public Expense addExpense(Expense expense) throws ExpenseExistsException {
        Optional<Expense> _expense = expenseRepository.findExpenseByName(expense.getName());
        if (_expense.isPresent()) {
            throw new ExpenseExistsException("Expense already exists");
        }

        expenseRepository.save(expense);
        return expense;
    }

    public void deleteExpenseById(Long expenseId) {
        if (expenseRepository.existsById(expenseId)) {
            log.info("Deleting expense with id: {}", expenseId);
            expenseRepository.deleteById(expenseId);
        } else {
            log.warn("Attempted to delete non-existent expense with id: {}", expenseId);
        }
    }

    public ResponseEntity<Expense> updateoldExpense(Long oldExpenseId, Expense expense) throws ExpenseNotFoundException {
        Expense existingExpense = expenseRepository.findById(oldExpenseId)
                .orElseThrow(() -> new ExpenseNotFoundException("Expense does not exist..."));

        existingExpense.setName(expense.getName());
        existingExpense.setAmount(expense.getAmount());
        existingExpense.setCategoryId(expense.getCategoryId());
        existingExpense.setComments(expense.getComments());
        existingExpense.setCreationDate(expense.getCreationDate());
        Expense updatedExpense = expenseRepository.save(existingExpense);
        return ResponseEntity.ok(updatedExpense);
    }
}
