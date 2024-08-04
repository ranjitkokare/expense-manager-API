
package in.ranjitkokare.expensetrackerapi.service;

import java.sql.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import in.ranjitkokare.expensetrackerapi.dto.ExpenseDTO;
import in.ranjitkokare.expensetrackerapi.entity.Expense;

public interface ExpenseService {
	
	List<ExpenseDTO> getAllExpenses(Pageable page);
	
	ExpenseDTO getExpenseById(String expenseId);
	
	void deleteExpenseById(String expenseId);
	
	ExpenseDTO saveExpenseDetails(ExpenseDTO expenseDTO);
	
	ExpenseDTO updateExpenseDetails(String expenseId, ExpenseDTO expenseDTO);
	
	List<ExpenseDTO> readByCategory(String category, Pageable page);
	
	List<ExpenseDTO> readByName(String name, Pageable page);
	
	List<ExpenseDTO> readByDate(Date startDate, Date endDate, Pageable page);
}
