package study.goorm.domain.history.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import study.goorm.domain.history.domain.entity.Comment;
import study.goorm.domain.history.domain.entity.History;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    void deleteAllByHistoryId(Long historyId);
    Optional<Comment> findById(Long id);
    Page<Comment> findAllByHistoryAndCommentIsNull(History history, Pageable pageable);
    List<Comment> findAllByCommentIn(List<Comment> parents);
    void deleteAllByComment(Comment comment);
}
