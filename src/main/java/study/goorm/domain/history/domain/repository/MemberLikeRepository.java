package study.goorm.domain.history.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.goorm.domain.history.domain.entity.History;
import study.goorm.domain.history.domain.entity.MemberLike;
import study.goorm.domain.member.domain.entity.Member;

import java.util.List;
import java.util.Optional;

public interface MemberLikeRepository extends JpaRepository<MemberLike, Long> {
    boolean existsByMemberAndHistory(Member member, History history);
    Optional<MemberLike> findByMemberAndHistory(Member member, History history);
    Long countByHistory(History history);

    List<MemberLike> findAllByHistoryId(Long historyId);

    List<MemberLike> findAllByHistory(History history);
}
