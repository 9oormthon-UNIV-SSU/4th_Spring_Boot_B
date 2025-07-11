package study.goorm.domain.history.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.goorm.domain.history.domain.entity.History;
import study.goorm.domain.history.domain.entity.HistoryImage;

import java.util.List;

public interface HistoryImageRepository extends JpaRepository<HistoryImage, Long> {
    void deleteAllByHistory(History history);

    List<HistoryImage> findFirstImagesByHistory(History history);


    History history(History history);

    @Query("""
    SELECT hi FROM HistoryImage hi
    WHERE hi.id IN (
        SELECT MIN(hi2.id)
        FROM HistoryImage hi2
        WHERE hi2.history.id IN :historyIds
        GROUP BY hi2.history.id
    )
""")
    List<HistoryImage> findFirstImagesByHistoryIds(@Param("historyIds") List<Long> historyIds);
    void deleteAllByHistoryId(Long historyId);


}
