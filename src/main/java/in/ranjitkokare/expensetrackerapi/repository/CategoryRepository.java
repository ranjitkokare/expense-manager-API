package in.ranjitkokare.expensetrackerapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import in.ranjitkokare.expensetrackerapi.entity.CategoryEntity;


/**
 * JPA repository for category entity
 * @author Ranjit Kokare
 */
/**
 * 
 */
/**
 * 
 */
/**
 * 
 */
/**
 * 
 */
/**
 * 
 */
/**
 * 
 */
/**
 * 
 */
/**
 * 
 */
/**
 * 
 */
/**
 * 
 */
/**
 * 
 */
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long>{
	
	/**
	 * Finder method to retrieve the categories by user id
	 * @param userId
	 * @return list
	 */
	List<CategoryEntity> findByUserId(Long userId);
	
	//finder method here 1st to delete category we have to read category
	/**
	 * Finder method to fetch the category by user id and category id
	 * @param id
	 * @param categoryId
	 * @return Optional<CategoryEntity>
	 */
	Optional<CategoryEntity> findByUserIdAndCategoryId(Long id, String categoryId); 
	//we might get category or not thats why optional
	
	
	//checking for existing name for category name
	/**
	 * It checks whether category is present or not by user id and category name
	 * @param name
	 * @param userId
	 * @return boolean
	 */
	boolean existsByNameAndUserId(String name , Long userId);
	
	
	/**
	 * It retrieves the category by name and user id
	 * @param name
	 * @param userId
	 * @return Optional<CategoryEntity>
	 */
	Optional<CategoryEntity> findByNameAndUserId(String name, Long userId);
}
