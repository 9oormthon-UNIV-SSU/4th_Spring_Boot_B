package study.goorm.domain.folder.domain.repository;

import study.goorm.domain.cloth.domain.entity.Cloth;

public interface ClothFolderRepository {
    void deleteAllByCloth(Cloth cloth);
}
