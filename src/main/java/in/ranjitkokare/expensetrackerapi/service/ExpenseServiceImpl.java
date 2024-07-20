package in.ranjitkokare.expensetrackerapi.service;


import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import in.ranjitkokare.expensetrackerapi.entity.Expense;
import in.ranjitkokare.expensetrackerapi.exceptions.ResourceNotFoundException;
import in.ranjitkokare.expensetrackerapi.repository.ExpenseRepository;

@Service
public class ExpenseServiceImpl implements ExpenseService {
	
	@Autowired
	private ExpenseRepository expenseRepo;
	
	@Autowired
	private UserService userService;
	
	@Override
	public Page<Expense> getAllExpenses(Pageable page) {
		//to get expense of Specific User
		return expenseRepo.findByUserId(userService.getLoggedInUser().getId() ,page);
	}

	@Override
	public Expense getExpenseById(Long id) {
		Optional<Expense> expense = expenseRepo.findByUserIdAndId(userService.getLoggedInUser().getId() , id);
		if(expense.isPresent()) {//if expense is present then
			return expense.get();//call get method on expense object
		}
		throw  new ResourceNotFoundException("Expense is not found for the id "+id);
	}

	@Override
	public void deleteExpenseById(Long id) {
		Expense expense = getExpenseById(id);
		expenseRepo.delete(expense);
	}

	@Override
	public Expense saveExpenseDetails(Expense expense) {
		//set user to the expense entity before saving to db
		expense.setUser(userService.getLoggedInUser());
		return expenseRepo.save(expense);
	}

	@Override
	public Expense updateExpenseDetails(Long id, Expense expense) {
		//for getting existing expense 
		Expense existingEexpense = getExpenseById(id);
		//now set the fields to existingExpense object
		existingEexpense.setName(expense.getName()!=null? expense.getName() : existingEexpense.getName());
		existingEexpense.setDescription(expense.getDescription()!=null? expense.getDescription() : existingEexpense.getDescription());
		existingEexpense.setCategory(expense.getCategory()!=null? expense.getCategory() : existingEexpense.getCategory());
		existingEexpense.setDate(expense.getDate()!=null? expense.getDate() : existingEexpense.getDate());
		existingEexpense.setAmount(expense.getAmount()!=null? expense.getAmount() : existingEexpense.getAmount());
		return expenseRepo.save(existingEexpense);
	}

	//Filtering Records
	
	//Filter by Category
	@Override
	public List<Expense> readByCategory(String category, Pageable page) {//conversion to list
		return expenseRepo.findByUserIdAndCategory(userService.getLoggedInUser().getId(), category, page).toList();
	}

	//Filter by Keyword
	@Override
	public List<Expense> readByName(String name, Pageable page) {
		return expenseRepo.findByUserIdAndNameContaining(userService.getLoggedInUser().getId(), name, page).toList();
	}

	@Override
	public List<Expense> readByDate(Date startDate, Date endDate, Pageable page) {
		
		if (startDate == null) {
			startDate = new Date(0);//starting date if not provided initial date
		}
		if (endDate == null) {//if user has no entered endDate then 
			endDate = new Date(System.currentTimeMillis());//todays Date will be end Date
		}
		return expenseRepo.findByUserIdAndDateBetween(userService.getLoggedInUser().getId(),
				startDate, endDate, page).toList();
	}
	
	
	
	
}
