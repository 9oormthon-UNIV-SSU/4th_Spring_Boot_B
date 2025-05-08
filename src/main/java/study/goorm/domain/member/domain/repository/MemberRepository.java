package study.goorm.domain.member.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.goorm.domain.cloth.domain.entity.Category;

public interface MemberRepository extends JpaRepository<Category, Long> {
}
