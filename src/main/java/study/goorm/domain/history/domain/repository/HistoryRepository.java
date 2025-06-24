package study.goorm.domain.history.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.goorm.domain.history.domain.entity.History;

import java.time.YearMonth;
import java.util.List;

public interface HistoryRepository extends JpaRepository<History, Long> {

    @Query("SELECT h FROM History h " +
            "JOIN h.member m " +
            "WHERE m.clokeyId = :clokeyId AND " +
            "YEAR(h.historyDate) = :year AND MONTH(h.historyDate) = :month")
    List<History> findByClokeyIdAndMonth(@Param("clokeyId") String clokeyId,
                                         @Param("year") int year,
                                         @Param("month") int month);
}

