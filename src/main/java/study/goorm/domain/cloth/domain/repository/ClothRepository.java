package study.goorm.domain.cloth.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import study.goorm.domain.cloth.domain.entity.Cloth;
import study.goorm.domain.member.domain.entity.Member;


public interface ClothRepository extends JpaRepository<Cloth, Long> {
    Page<Cloth> findByMemberOrderByWearNumAsc(Member member, Pageable pageable);
    Page<Cloth> findByMemberOrderByWearNumDesc(Member member, Pageable pageable);
    Page<Cloth> findByMemberOrderByCreatedAtAsc(Member member, Pageable pageable);
    Page<Cloth> findByMemberOrderByCreatedAtDesc(Member member, Pageable pageable);

}
