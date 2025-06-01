package study.goorm.domain.history.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.goorm.domain.history.domain.entity.HistoryImage;

import java.util.List;

public interface HistoryImageRepository extends JpaRepository<HistoryImage, Long> {
    @Query("SELECT hi FROM HistoryImage hi " +
            "WHERE hi.history.id IN :historyIds " +
            "AND hi.id IN (" +
            "    SELECT MIN(hii.id) FROM HistoryImage hii WHERE hii.history.id IN :historyIds GROUP BY hii.history.id" +
            ")")
    List<HistoryImage> findFirstImagesByHistoryIds(@Param("historyIds") List<Long> historyIds);
}
