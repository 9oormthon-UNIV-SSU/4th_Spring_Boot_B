package study.goorm.domain.history.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.goorm.domain.cloth.domain.entity.Cloth;
import study.goorm.domain.history.domain.entity.HistoryCloth;

import java.util.List;

public interface HistoryClothRepository extends JpaRepository<HistoryCloth, Long> {

    List<HistoryCloth> findAllByHistoryId(Long historyId);

    void deleteAllByCloth(Cloth cloth);

    void deleteAllByHistoryId(Long historyId);
}
