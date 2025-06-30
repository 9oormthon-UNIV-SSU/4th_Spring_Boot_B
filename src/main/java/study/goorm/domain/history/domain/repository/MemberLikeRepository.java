package study.goorm.domain.history.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.goorm.domain.history.domain.entity.MemberLike;

public interface MemberLikeRepository extends JpaRepository<MemberLike, Long> {
    void deleteAllByHistoryId(Long historyId);
    boolean existsByMemberIdAndHistoryId(Long memberId, Long historyId);
    void deleteByMemberIdAndHistoryId(Long memberId, Long historyId);
}
