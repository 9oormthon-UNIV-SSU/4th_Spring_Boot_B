package study.goorm.domain.history.domain.repository;

import study.goorm.domain.cloth.domain.entity.Cloth;

public interface HistoryClothRepository {
    void deleteAllByCloth(Cloth cloth);
}
