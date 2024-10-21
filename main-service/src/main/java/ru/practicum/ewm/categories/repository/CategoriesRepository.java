package ru.practicum.ewm.categories.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.ewm.categories.model.Category;
import ru.practicum.ewm.compilation.model.Compilation;

public interface CategoriesRepository extends JpaRepository<Category, Long> {

    @Query("select c from category c where c.id = :id or :id is null")
    Page<Compilation> findAllByPinned(@Param("id") Long id, Long page);

}
