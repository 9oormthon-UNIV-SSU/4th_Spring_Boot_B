package study.goorm.domain.folder.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.goorm.domain.cloth.domain.entity.Category;

public interface FolderRepository extends JpaRepository<Category, Long> {
}
