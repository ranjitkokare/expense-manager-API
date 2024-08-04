package in.ranjitkokare.expensetrackerapi.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import in.ranjitkokare.expensetrackerapi.dto.CategoryDTO;
import in.ranjitkokare.expensetrackerapi.dto.UserDTO;
import in.ranjitkokare.expensetrackerapi.entity.CategoryEntity;
import in.ranjitkokare.expensetrackerapi.entity.User;
import in.ranjitkokare.expensetrackerapi.exceptions.ItemAlreadyExistsException;
import in.ranjitkokare.expensetrackerapi.exceptions.ResourceNotFoundException;
import in.ranjitkokare.expensetrackerapi.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor //Constructor injection
public class CategoryServiceImpl implements CategoryService{
	
	private final CategoryRepository categoryRepository;
	private final UserService userService;
	
	/**
	 * This is for reading the categories from the database
	 * @return list
	 */
	@Override
	public List<CategoryDTO> getAllCategories() {
		List<CategoryEntity> list = categoryRepository.findByUserId(userService.getLoggedInUser().getId());
		//Use of StreamAPI
		//Convert this category entity into category DTO
		//map function will convert from one object to another object
		return list.stream().map(categoryEntity -> mapToDTO(categoryEntity)).collect(Collectors.toList());
	}
	
	/**
	 * This is for creating the category
	 * @param categoryDTO
	 * @return CategoryDTO
	 */
	@Override
	public CategoryDTO saveCategory(CategoryDTO categoryDTO) {
		boolean isCategoryPresent = categoryRepository.existsByNameAndUserId(categoryDTO.getName(), 
				userService.getLoggedInUser().getId());
		if(isCategoryPresent) {
			throw new ItemAlreadyExistsException("Category is already present for the name "+categoryDTO.getName());
		}
		CategoryEntity newCategory = mapToEntity(categoryDTO);
		newCategory = categoryRepository.save(newCategory);
		return mapToDTO(newCategory);
	}

	/**
	 * This is for deleting the category from database
	 * @param categoryId
	 */
	@Override
	public void deleteCategory(String categoryId) {
		Optional<CategoryEntity> optionalCategory = categoryRepository.findByUserIdAndCategoryId(userService.getLoggedInUser().getId(), categoryId);
		if(!optionalCategory.isPresent()) {
			throw new ResourceNotFoundException("Category not found for the id "+categoryId);
		}
		categoryRepository.delete(optionalCategory.get());
	}
	
	/**
	 * Mapper method to convert the category DTO to category entity
	 * @param categoryDTO
	 * @return CategoryEntity
	 */
	private CategoryEntity mapToEntity(CategoryDTO categoryDTO) {
		return CategoryEntity.builder()
					.name(categoryDTO.getName())
					.description(categoryDTO.getDescription())
					.categoryIcon(categoryDTO.getCategoryIcon())
					.categoryId(UUID.randomUUID().toString())
					.user(userService.getLoggedInUser())
					.build();
	}

	/**
	 * Mapper method to convert Category entity to Category DTO
	 * @param categoryEntity
	 * @return CategoryDTO
	 */
	private CategoryDTO mapToDTO(CategoryEntity categoryEntity) {
		return CategoryDTO.builder()
			.categoryId(categoryEntity.getCategoryId())
			.name(categoryEntity.getName())
			.description(categoryEntity.getDescription())
			.categoryIcon(categoryEntity.getCategoryIcon())
			.createdAt(categoryEntity.getCreatedAt())
			.updatedAt(categoryEntity.getUpdatedAt())
			.user(mapToUserDTO(categoryEntity.getUser()))
			.build();
	}
	
	/**
	 * Mapper method to convert User entity to User DTO
	 * @param user
	 * @return UserDTO
	 */
	private UserDTO mapToUserDTO(User user) {
		return UserDTO.builder()
				.email(user.getEmail())
				.name(user.getName())
				.build();
	}

}
