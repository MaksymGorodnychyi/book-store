package mate.academy.bookstore.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import mate.academy.bookstore.dto.CategoryDto;
import mate.academy.bookstore.dto.CreateCategoryRequestDto;
import mate.academy.bookstore.exception.EntityNotFoundException;
import mate.academy.bookstore.mapper.CategoryMapper;
import mate.academy.bookstore.model.Category;
import mate.academy.bookstore.repository.CategoryRepository;
import mate.academy.bookstore.util.TestUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    void getById_existingId_returnsDto() {
        Category category = TestUtil.createCategory(4L, "Keto");
        CategoryDto expected = TestUtil.createCategoryDto(category);

        when(categoryRepository.findById(4L)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(expected);

        CategoryDto actual = categoryService.getById(4L);

        assertEquals(expected, actual);
    }

    @Test
    void getById_missingId_throwsException() {
        when(categoryRepository.findById(7L)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> categoryService.getById(7L));
        assertEquals("Can't find category by id: 7", ex.getMessage());
    }

    @Test
    void findAll_withPageable_returnsPageOfDtos() {
        Pageable pageable = PageRequest.of(0, 10);
        Category category = TestUtil.createCategory(4L, "Keto");
        CategoryDto dto = TestUtil.createCategoryDto(category);
        Page<Category> categoryPage = new PageImpl<>(List.of(category));

        when(categoryRepository.findAll(pageable)).thenReturn(categoryPage);
        when(categoryMapper.toDto(category)).thenReturn(dto);

        Page<CategoryDto> categoryDtoPage = categoryService.findAll(pageable);

        assertEquals(1, categoryDtoPage.getTotalElements());
        assertEquals(dto, categoryDtoPage.getContent().get(0));
    }

    @Test
    void save_validRequest_returnsDto() {
        CreateCategoryRequestDto categoryRequestDto = new CreateCategoryRequestDto();
        Category category = TestUtil.createCategory(4L, "Keto");
        CategoryDto expected = TestUtil.createCategoryDto(category);

        when(categoryMapper.toModel(categoryRequestDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(expected);

        CategoryDto actual = categoryService.save(categoryRequestDto);

        assertEquals(expected, actual);
    }

    @Test
    void update_existingId_returnsUpdatedDto() {
        Category category = TestUtil.createCategory(4L, "Keto");
        CategoryDto expected = TestUtil.createCategoryDto(category);
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto();

        when(categoryRepository.findById(3L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(expected);

        CategoryDto actual = categoryService.update(3L, requestDto);

        assertEquals(expected, actual);
        verify(categoryMapper).updateEntityFromDto(category, requestDto);
    }

    @Test
    void deleteById_anyId_callsRepositoryOnce() {
        categoryService.deleteById(6L);

        verify(categoryRepository).deleteById(6L);
    }

    @Test
    void update_missingId_throwsAndNeverSaves() {
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> categoryService.update(99L, new CreateCategoryRequestDto()));
        verify(categoryRepository, never()).save(any());
    }
}
