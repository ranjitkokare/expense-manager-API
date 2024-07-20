package in.ranjitkokare.expensetrackerapi.controller;


import java.sql.Date;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import in.ranjitkokare.expensetrackerapi.entity.Expense;
import in.ranjitkokare.expensetrackerapi.service.ExpenseService;
import javax.validation.Valid;

@RestController
public class ExpenseController {
	
	@Autowired
	private ExpenseService expenseService; 
	
	@GetMapping("/expenses")
	public List<Expense> getAllExpenses(Pageable page) {
//		int number = 1;
//		calculateFactorial(number);
		return expenseService.getAllExpenses(page).toList();
	}
	
	@GetMapping("/expenses/{id}")//path variable
	public Expense getExpenseById(@PathVariable Long id) {//argument binded both
		return expenseService.getExpenseById(id); 
	}
	
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@DeleteMapping("/expenses")
	public void deleteExpenseById(@RequestParam Long id) {
		expenseService.deleteExpenseById(id);
	}
	
	@ResponseStatus(value = HttpStatus.CREATED)//means resource has been successfully created
	@PostMapping("/expenses")
	public Expense saveExpenseDetails(@Valid @RequestBody Expense expense) {
		//here @valid checks while binding the request body to Bean
		return expenseService.saveExpenseDetails(expense);
	}
	
	@PutMapping("/expenses/{id}")
	public Expense updateExpenseDetails(@RequestBody Expense expense, @PathVariable Long id) {
		return expenseService.updateExpenseDetails(id, expense);
	}
	
	public int calculateFactorial(int number) {
		return number * calculateFactorial(number - 1);
	}
	
	@GetMapping("/expenses/category")
	public List<Expense> getExpenseByCategory(@RequestParam String category, Pageable page){
		return expenseService.readByCategory(category, page);
	}
	
	@GetMapping("/expenses/name")
	public List<Expense> getExpenseByName(@RequestParam String keyword, Pageable page){
		return expenseService.readByName(keyword, page);
	}
	
	@GetMapping("/expenses/date")
	public List<Expense> getExpenseByDate(@RequestParam(required = false) Date startDate,//optional
										  @RequestParam(required = false) Date endDate,
										  Pageable page){
		return expenseService.readByDate(startDate, endDate, page);
	}
}
