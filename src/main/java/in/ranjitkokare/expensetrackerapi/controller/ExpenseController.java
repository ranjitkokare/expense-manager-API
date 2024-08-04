package in.ranjitkokare.expensetrackerapi.controller;


import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

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

import in.ranjitkokare.expensetrackerapi.dto.CategoryDTO;
import in.ranjitkokare.expensetrackerapi.dto.ExpenseDTO;
import in.ranjitkokare.expensetrackerapi.entity.Expense;
import in.ranjitkokare.expensetrackerapi.io.CategoryResponse;
import in.ranjitkokare.expensetrackerapi.io.ExpenseRequest;
import in.ranjitkokare.expensetrackerapi.io.ExpenseResponse;
import in.ranjitkokare.expensetrackerapi.service.ExpenseService;
import jakarta.validation.Valid;

@RestController
public class ExpenseController {
	
	@Autowired
	private ExpenseService expenseService; 
	
	@GetMapping("/expenses")
	public List<ExpenseResponse> getAllExpenses(Pageable page) {
//		int number = 1;
//		calculateFactorial(number);
		List<ExpenseDTO> listOfExpenses = expenseService.getAllExpenses(page);
		return listOfExpenses.stream().map(expenseDTO -> mapToResponse(expenseDTO)).collect(Collectors.toList());
	}
	
	@GetMapping("/expenses/{expenseId}")//path variable
	public ExpenseResponse getExpenseById(@PathVariable String expenseId) {//argument binded both
		ExpenseDTO expenseDTO = expenseService.getExpenseById(expenseId); 
		return mapToResponse(expenseDTO);
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
		ExpenseDTO expenseDTO = mapToDTO(expenseRequest);
		expenseDTO = expenseService.saveExpenseDetails(expenseDTO);
		return mapToResponse(expenseDTO);
	}
	
	private ExpenseResponse mapToResponse(ExpenseDTO expenseDTO) {
		return ExpenseResponse.builder() // convert DTO to response
					.expenseId(expenseDTO.getExpenseId())
					.name(expenseDTO.getName())
					.description(expenseDTO.getDescription())
					.amount(expenseDTO.getAmount())
					.date(expenseDTO.getDate())
					.createdAt(expenseDTO.getCreatedAt())
					.updatedAt(expenseDTO.getUpdatedAt())
					.category(mapToCategoryResponse(expenseDTO.getCategoryDTO()))
					.build();
	}

	private CategoryResponse mapToCategoryResponse(CategoryDTO categoryDTO) {
		return CategoryResponse.builder()
					.categoryId(categoryDTO.getCategoryId())
					.name(categoryDTO.getName())
					.build();
	}

	private ExpenseDTO mapToDTO(ExpenseRequest expenseRequest) {
		return ExpenseDTO.builder()//convert request to DTO
				.name(expenseRequest.getName())
				.description(expenseRequest.getDescription())
				.amount(expenseRequest.getAmount())
				.date(expenseRequest.getDate())
				.categoryId(expenseRequest.getCategoryId())
				.build();
	}

	@PutMapping("/expenses/{expenseId}")
	public ExpenseResponse updateExpenseDetails(@RequestBody ExpenseRequest expenseRequest, @PathVariable String expenseId) {
		ExpenseDTO updatedExpense = mapToDTO(expenseRequest);
		updatedExpense = expenseService.updateExpenseDetails(expenseId, updatedExpense);
		return mapToResponse(updatedExpense);
	}
	
	public int calculateFactorial(int number) {
		return number * calculateFactorial(number - 1);
	}
	
	@GetMapping("/expenses/category")
	public List<ExpenseResponse> getExpenseByCategory(@RequestParam String category, Pageable page){
		List<ExpenseDTO> list = expenseService.readByCategory(category, page);
		return list.stream().map(expenseDTO -> mapToResponse(expenseDTO)).collect(Collectors.toList());
	}
	
	@GetMapping("/expenses/name")
	public List<ExpenseResponse> getExpenseByName(@RequestParam String keyword, Pageable page){
		List<ExpenseDTO> list = expenseService.readByName(keyword, page);
		return list.stream().map(expenseDTO -> mapToResponse(expenseDTO)).collect(Collectors.toList());
	}
	
	@GetMapping("/expenses/date")
	public List<ExpenseResponse> getExpenseByDate(@RequestParam(required = false) Date startDate,//optional
										  @RequestParam(required = false) Date endDate,
										  Pageable page){
		List<ExpenseDTO> list = expenseService.readByDate(startDate, endDate, page);
		return list.stream().map(expenseDTO -> mapToResponse(expenseDTO)).collect(Collectors.toList());
	}
}
