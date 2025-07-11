package study.goorm.domain.history.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.goorm.domain.cloth.domain.entity.Cloth;
import study.goorm.domain.history.domain.entity.History;
import study.goorm.domain.history.domain.entity.HistoryCloth;

import java.util.List;
import java.util.Optional;

public interface HistoryClothRepository extends JpaRepository<HistoryCloth, Long> {
    void deleteAllByCloth(Cloth cloth);

    Optional<Object> findAllByHistory_Id(Long historyId);

    List<HistoryCloth> findAllByHistory(History history);

    void deleteAllByHistory(History history);

    void deleteAllByHistoryId(Long historyId);
}
