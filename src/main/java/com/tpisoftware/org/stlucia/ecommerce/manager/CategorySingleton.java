package com.tpisoftware.org.stlucia.ecommerce.manager;

import com.tpisoftware.org.stlucia.ecommerce.exception.ExceptionMessages;
import com.tpisoftware.org.stlucia.ecommerce.model.Category;
import com.tpisoftware.org.stlucia.ecommerce.repository.CategoryRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CategorySingleton {

    @Autowired
    private CategoryRepository categoryRepository;

    private final ConcurrentHashMap<Long, Category> categoryMap = new ConcurrentHashMap<>();

    @PostConstruct
    private void init() {
        categoryRepository.findAll().stream()
                .sorted(Comparator.comparing(Category::getId))
                .forEach(category -> categoryMap.put(category.getId(), category));
    }

    public List<Category> getCategories() {
        return List.copyOf(categoryMap.values());
    }

    public void save(Category category) {
        categoryRepository.save(category);
        categoryMap.put(category.getId(), category);
    }

    public Optional<Category> get(Long id) {
        return Optional.ofNullable(categoryMap.get(id));
    }

    public void remove(Long id) {
        Category category = categoryMap.remove(id);
        if (category == null) {
            throw new NoSuchElementException(String.format(ExceptionMessages.ENTITY_NOT_FOUND_WITH_ID, "category", id));
        }

        try {
            categoryRepository.deleteById(category.getId());
        } catch (Exception e) {
            resetCategories();
            throw new RuntimeException(String.format(ExceptionMessages.DELETE_FAILED_MESSAGE, "category", id));
        }
    }

    public void resetCategories() {
        categoryMap.clear();
        init(); // No need for locks, as ConcurrentHashMap is thread-safe
    }

}