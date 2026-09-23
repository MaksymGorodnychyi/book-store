package mate.academy.bookstore.mapper;

import mate.academy.bookstore.dto.CategoryDto;
import mate.academy.bookstore.dto.CreateCategoryRequestDto;
import mate.academy.bookstore.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDto toDto(Category category);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    Category toModel(CreateCategoryRequestDto requestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    void updateEntityFromDto(@MappingTarget Category category, CreateCategoryRequestDto requestDto);
}
