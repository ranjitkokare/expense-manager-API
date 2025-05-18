package in.ranjitkokare.expensetrackerapi.mappers;

import in.ranjitkokare.expensetrackerapi.dto.CategoryDTO;
import in.ranjitkokare.expensetrackerapi.entity.CategoryEntity;
import in.ranjitkokare.expensetrackerapi.io.CategoryRequest;
import in.ranjitkokare.expensetrackerapi.io.CategoryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryEntity mapToCategoryEntity(CategoryDTO categoryDTO);

    CategoryDTO mapToCategoryDTO(CategoryEntity categoryEntity);

    @Mapping(target = "categoryIcon", source = "icon")
    CategoryDTO mapToCategoryDTO(CategoryRequest categoryRequest);

    CategoryResponse mapToCategoryResponse(CategoryDTO categoryDTO);
}
