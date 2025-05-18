package in.ranjitkokare.expensetrackerapi.controller;

import in.ranjitkokare.expensetrackerapi.dto.CategoryDTO;
import in.ranjitkokare.expensetrackerapi.io.CategoryRequest;
import in.ranjitkokare.expensetrackerapi.io.CategoryResponse;
import in.ranjitkokare.expensetrackerapi.mappers.CategoryMapper;
import in.ranjitkokare.expensetrackerapi.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


/**
 * This controller is for managing the categories
 * @author Ranjit Kokare
 */
@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

	private final CategoryService categoryService;
	private final CategoryMapper categoryMapper;

	/**
	 * API for the creating category
	 * @param categoryRequest
	 * @return CategoryResponse
	 */
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
	public CategoryResponse createCategory(@RequestBody CategoryRequest categoryRequest) {
		CategoryDTO categoryDTO = categoryMapper.mapToCategoryDTO(categoryRequest);
		categoryDTO = categoryService.saveCategory(categoryDTO);
		//convert this DTO object to response object
		return categoryMapper.mapToCategoryResponse(categoryDTO);
	}

	/**
	 * API for reading the categories
	 * @return List
	 */
	@GetMapping
	public List<CategoryResponse> readCategories(){
		List<CategoryDTO> listOfCategories = categoryService.getAllCategories();
		return listOfCategories.stream().map(categoryDTO -> categoryMapper.mapToCategoryResponse(categoryDTO)).collect(Collectors.toList());
	}

	/**
	 * API for deleting the category
	 * @param categoryId
	 *
	 */
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{categoryId}")//pass id using path variable
	public void deleteCategory(@PathVariable String categoryId) {
		categoryService.deleteCategory(categoryId);
	}
}
