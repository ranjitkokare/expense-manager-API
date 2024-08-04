package in.ranjitkokare.expensetrackerapi.service;


import java.sql.Date;


import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import in.ranjitkokare.expensetrackerapi.dto.CategoryDTO;
import in.ranjitkokare.expensetrackerapi.dto.ExpenseDTO;
import in.ranjitkokare.expensetrackerapi.entity.CategoryEntity;
import in.ranjitkokare.expensetrackerapi.entity.Expense;
import in.ranjitkokare.expensetrackerapi.exceptions.ResourceNotFoundException;
import in.ranjitkokare.expensetrackerapi.repository.CategoryRepository;
import in.ranjitkokare.expensetrackerapi.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {
	
	
	private final ExpenseRepository expenseRepo;
	
	
	private final UserService userService;
	private final CategoryRepository categoryRepository;
	
	@Override
	public List<ExpenseDTO> getAllExpenses(Pageable page) {
		//to get expense of Specific User
		List<Expense> listOfExpenses = expenseRepo.findByUserId(userService.getLoggedInUser().getId(),page).toList();
		return listOfExpenses.stream().map(expense -> mapToDTO(expense)).collect(Collectors.toList());
	}

	@Override
	public ExpenseDTO getExpenseById(String expenseId) {
		Expense existingExpense = getExpenseEntity(expenseId);
		return mapToDTO(existingExpense);
	}

	private Expense getExpenseEntity(String expenseId) {
		Optional<Expense> expense = expenseRepo.findByUserIdAndExpenseId(userService.getLoggedInUser().getId() , expenseId);
		if(!expense.isPresent()) {//if expense is not present then
			throw  new ResourceNotFoundException("Expense is not found for the id "+expenseId);
		}
		return expense.get();//call get method on expense object
	}

	@Override
	public void deleteExpenseById(String expenseId) {
		Expense expense = getExpenseEntity(expenseId);
		expenseRepo.delete(expense);
	}

	@Override
	public ExpenseDTO saveExpenseDetails(ExpenseDTO expenseDTO) {
		/*
		 * //set user to the expense entity before saving to db
		 * expense.setUser(userService.getLoggedInUser()); return
		 * expenseRepo.save(expense);
		 */
		
		//check the existence of category
		Optional<CategoryEntity> optionalCategory = categoryRepository.findByUserIdAndCategoryId(userService.getLoggedInUser().getId(), expenseDTO.getCategoryId());
		if (!optionalCategory.isPresent()) {
			throw new ResourceNotFoundException("Category not found for the id "+expenseDTO.getCategoryId());
		}
		expenseDTO.setExpenseId(UUID.randomUUID().toString());
		//map to entity object
		Expense newExpense =  mapToEntity(expenseDTO);
		//save to the database
		newExpense.setCategory(optionalCategory.get());
		newExpense.setUser(userService.getLoggedInUser());
		newExpense = expenseRepo.save(newExpense);
		//map to response object
		return mapToDTO(newExpense);
	}

	private ExpenseDTO mapToDTO(Expense newExpense) {
		return ExpenseDTO.builder()
				.expenseId(newExpense.getExpenseId())
				.name(newExpense.getName())
				.description(newExpense.getDescription())
				.amount(newExpense.getAmount())
				.date(newExpense.getDate())
				.createdAt(newExpense.getCreatedAt())
				.updatedAt(newExpense.getUpdatedAt())
				.categoryDTO(mapToCategoryDTO(newExpense.getCategory()))
				.build();
	}

	private CategoryDTO mapToCategoryDTO(CategoryEntity category) {
		return CategoryDTO.builder()
					.name(category.getName())
					.categoryId(category.getCategoryId())
					.build();
	}

	private Expense mapToEntity(ExpenseDTO expenseDTO) {
		return Expense.builder()
				.expenseId(expenseDTO.getExpenseId())
				.name(expenseDTO.getName())
				.description(expenseDTO.getDescription())
				.date(expenseDTO.getDate())
				.amount(expenseDTO.getAmount())
				.build();
	}

	@Override
	public ExpenseDTO updateExpenseDetails(String expenseId, ExpenseDTO expenseDTO) {
		//for getting existing expense 
		Expense existingEexpense = getExpenseEntity(expenseId);
		
		if(expenseDTO.getCategoryId() != null) {
			Optional<CategoryEntity> optionalCategory = categoryRepository.findByUserIdAndCategoryId(userService.getLoggedInUser().getId(), expenseDTO.getCategoryId());
			if (!optionalCategory.isPresent()) {
				throw new ResourceNotFoundException("Category not found for the id "+expenseDTO.getCategoryId());
			}
			existingEexpense.setCategory(optionalCategory.get());
		}
		//now set the fields to existingExpense object
		existingEexpense.setName(expenseDTO.getName()!=null? expenseDTO.getName() : existingEexpense.getName());
		existingEexpense.setDescription(expenseDTO.getDescription()!=null? expenseDTO.getDescription() : existingEexpense.getDescription());
		existingEexpense.setDate(expenseDTO.getDate()!=null? expenseDTO.getDate() : existingEexpense.getDate());
		existingEexpense.setAmount(expenseDTO.getAmount()!=null? expenseDTO.getAmount() : existingEexpense.getAmount());
		existingEexpense = expenseRepo.save(existingEexpense);
		return mapToDTO(existingEexpense);
	}

	//Filtering Records
	
	
	//Filter by Category
	@Override
	public List<ExpenseDTO> readByCategory(String category, Pageable page) {//conversion to list
		Optional<CategoryEntity> optionalCategory = categoryRepository.findByNameAndUserId(category, userService.getLoggedInUser().getId());
		if(!optionalCategory.isPresent()) {
			throw new ResourceNotFoundException("Category not found for the name "+category);
		}
		List<Expense> list = expenseRepo.findByUserIdAndCategoryId(userService.getLoggedInUser().getId(), optionalCategory.get().getId(), page).toList();
		return list.stream().map(expense -> mapToDTO(expense)).collect(Collectors.toList());
	}

	//Filter by Keyword
	@Override
	public List<ExpenseDTO> readByName(String name, Pageable page) {
		List<Expense> list = expenseRepo.findByUserIdAndNameContaining(userService.getLoggedInUser().getId(), name, page).toList();
		return list.stream().map(expense -> mapToDTO(expense)).collect(Collectors.toList());
	}

	@Override
	public List<ExpenseDTO> readByDate(Date startDate, Date endDate, Pageable page) {
		
		if (startDate == null) {
			startDate = new Date(0);//starting date if not provided initial date
		}
		if (endDate == null) {//if user has no entered endDate then 
			endDate = new Date(System.currentTimeMillis());//todays Date will be end Date
		}
		List<Expense> list = expenseRepo.findByUserIdAndDateBetween(userService.getLoggedInUser().getId(),
				startDate, endDate, page).toList();
		return list.stream().map(expense -> mapToDTO(expense)).collect(Collectors.toList());
	}
	
	
	
	
}
