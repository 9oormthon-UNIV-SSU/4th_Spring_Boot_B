package study.goorm.domain.history.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import study.goorm.domain.history.domain.entity.History;
import study.goorm.domain.history.dto.HistoryResponseDTO;

import java.awt.print.Pageable;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

public interface HistoryRepository extends JpaRepository<History, Long> {


    List<History> findByMemberIdAndHistoryDateBetween(Long memberId, LocalDate historyDateAfter, LocalDate historyDateBefore);

    @Query("SELECT h FROM History h JOIN FETCH h.member WHERE h.id=:historyId")
    Optional<History> findByIdWithMember(@Param("historyId") Long historyId);










}
