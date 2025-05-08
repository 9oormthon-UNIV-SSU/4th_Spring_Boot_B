package study.goorm.domain.history.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.goorm.domain.cloth.domain.entity.Category;

public interface HistoryClothRepository extends JpaRepository<Category, Long> {
}
