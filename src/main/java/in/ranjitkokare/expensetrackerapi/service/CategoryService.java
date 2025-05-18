package in.ranjitkokare.expensetrackerapi.service;

import in.ranjitkokare.expensetrackerapi.dto.CategoryDTO;

import java.util.List;


/**
 * Service interface for managing the categories
 * @author Ranjit Kokare
 */
public interface CategoryService {


	/**
	 * This is for reading the categories from the database
	 * @return list
	 */
	List<CategoryDTO> getAllCategories();

	/**
	 * This is for creating the category
	 * @param categoryDTO
	 * @return CategoryDTO
	 */
	CategoryDTO saveCategory(CategoryDTO categoryDTO);

	/**
	 * This is for deleting the category from database
	 * @param categoryId
	 */
	void deleteCategory(String categoryId);
}
