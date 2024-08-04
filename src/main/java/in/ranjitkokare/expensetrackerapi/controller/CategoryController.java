package in.ranjitkokare.expensetrackerapi.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import in.ranjitkokare.expensetrackerapi.dto.CategoryDTO;
import in.ranjitkokare.expensetrackerapi.io.CategoryRequest;
import in.ranjitkokare.expensetrackerapi.io.CategoryResponse;
import in.ranjitkokare.expensetrackerapi.service.CategoryService;
import lombok.RequiredArgsConstructor;


/**
 * This controller is for managing the categories
 * @author Ranjit Kokare
 */
@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
	
	private final CategoryService categoryService;
	
	
	/**
	 * API for the creating category
	 * @param categoryRequest
	 * @return CategoryResponse
	 */
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
	public CategoryResponse createCategory(@RequestBody CategoryRequest categoryRequest) {
		CategoryDTO categoryDTO = mapToDTO(categoryRequest);
		categoryDTO = categoryService.saveCategory(categoryDTO);
		//convert this DTO object to response object
		return mapToResponse(categoryDTO);
	}
	
	/**
	 * API for reading the categories
	 * @return List
	 */
	@GetMapping
	public List<CategoryResponse> readCategories(){
		List<CategoryDTO> listOfCategories = categoryService.getAllCategories();
		return listOfCategories.stream().map(categoryDTO -> mapToResponse(categoryDTO)).collect(Collectors.toList());
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
	
	/**
	 * Mapper method for converting DTO object to Response object
	 * @param categoryDTO
	 * @return CategoryResponse
	 */
	private CategoryResponse mapToResponse(CategoryDTO categoryDTO) {
		return CategoryResponse.builder()
					.categoryId(categoryDTO.getCategoryId())
					.name(categoryDTO.getName())
					.description(categoryDTO.getDescription())
					.categoryIcon(categoryDTO.getCategoryIcon())
					.createdAt(categoryDTO.getCreatedAt())
					.updatedAt(categoryDTO.getUpdatedAt())
					.build();
	}

	/**
	 * Mapper method for converting Request object to DTO object
	 * @param categoryRequest
	 * @return CategoryDTO
	 */
	private CategoryDTO mapToDTO(CategoryRequest categoryRequest) {
		return CategoryDTO.builder()
					.name(categoryRequest.getName())
					.description(categoryRequest.getDescription())
					.categoryIcon(categoryRequest.getIcon())
					.build();
	}
}
