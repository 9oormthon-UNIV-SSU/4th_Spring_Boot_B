package study.goorm.domain.history.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.goorm.domain.history.domain.entity.History;

import java.time.YearMonth;
import java.util.List;

public interface HistoryRepository extends JpaRepository<History, Long> {
    List<History> findByClokeyIdAndMonth(Long clokeyId, YearMonth month);
}
