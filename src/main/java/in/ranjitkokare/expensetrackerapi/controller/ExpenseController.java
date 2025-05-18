package in.ranjitkokare.expensetrackerapi.controller;


import in.ranjitkokare.expensetrackerapi.dto.ExpenseDTO;
import in.ranjitkokare.expensetrackerapi.io.ExpenseRequest;
import in.ranjitkokare.expensetrackerapi.io.ExpenseResponse;
import in.ranjitkokare.expensetrackerapi.mappers.ExpenseMapper;
import in.ranjitkokare.expensetrackerapi.service.ExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ExpenseController {

	private final ExpenseService expenseService;
	private final ExpenseMapper expenseMapper;

	@GetMapping("/expenses")
	public List<ExpenseResponse> getAllExpenses(Pageable page) {
//		int number = 1;
//		calculateFactorial(number);
		List<ExpenseDTO> listOfExpenses = expenseService.getAllExpenses(page);
		return listOfExpenses.stream().map(expenseDTO -> expenseMapper.mapToExpenseResponse(expenseDTO)).collect(Collectors.toList());
	}

	@GetMapping("/expenses/{expenseId}")//path variable
	public ExpenseResponse getExpenseById(@PathVariable String expenseId) {//argument binded both
		ExpenseDTO expenseDTO = expenseService.getExpenseById(expenseId);
		return expenseMapper.mapToExpenseResponse(expenseDTO);
	}

	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@DeleteMapping("/expenses")
	public void deleteExpenseById(@RequestParam String expenseId) {
		expenseService.deleteExpenseById(expenseId);
	}

	@ResponseStatus(value = HttpStatus.CREATED)//means resource has been successfully created
	@PostMapping("/expenses")
	public ExpenseResponse saveExpenseDetails(@Valid @RequestBody ExpenseRequest expenseRequest) {
		//here @valid checks while binding the request body to Bean
		//convert request object to DTO object
		ExpenseDTO expenseDTO = expenseMapper.mapToExpenseDTO(expenseRequest);
		expenseDTO = expenseService.saveExpenseDetails(expenseDTO);
		return expenseMapper.mapToExpenseResponse(expenseDTO);
	}

	@PutMapping("/expenses/{expenseId}")
	public ExpenseResponse updateExpenseDetails(@RequestBody ExpenseRequest expenseRequest, @PathVariable String expenseId) {
		ExpenseDTO updatedExpense = expenseMapper.mapToExpenseDTO(expenseRequest);
		updatedExpense = expenseService.updateExpenseDetails(expenseId, updatedExpense);
		return expenseMapper.mapToExpenseResponse(updatedExpense);
	}

	public int calculateFactorial(int number) {
		return number * calculateFactorial(number - 1);
	}

	@GetMapping("/expenses/category")
	public List<ExpenseResponse> getExpenseByCategory(@RequestParam String category, Pageable page){
		List<ExpenseDTO> list = expenseService.readByCategory(category, page);
		return list.stream().map(expenseDTO -> expenseMapper.mapToExpenseResponse(expenseDTO)).toList();
	}

	@GetMapping("/expenses/name")
	public List<ExpenseResponse> getExpenseByName(@RequestParam String keyword, Pageable page){
		List<ExpenseDTO> list = expenseService.readByName(keyword, page);
		return list.stream().map(expenseDTO -> expenseMapper.mapToExpenseResponse(expenseDTO)).collect(Collectors.toList());
	}

	@GetMapping("/expenses/date")
	public List<ExpenseResponse> getExpenseByDate(@RequestParam(required = false) Date startDate,//optional
												  @RequestParam(required = false) Date endDate,
												  Pageable page){
		List<ExpenseDTO> list = expenseService.readByDate(startDate, endDate, page);
		return list.stream().map(expenseDTO -> expenseMapper.mapToExpenseResponse(expenseDTO)).collect(Collectors.toList());
	}
}
