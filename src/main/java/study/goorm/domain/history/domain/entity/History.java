package study.goorm.domain.history.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import study.goorm.domain.member.domain.entity.Member;
import study.goorm.domain.model.entity.BaseEntity;
import study.goorm.domain.model.enums.Visibility;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@DynamicUpdate
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class History extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate historyDate;

    @Min(0)
    @Column(nullable = false, columnDefinition = "integer default 0")
    private int likes;

    @Column(length = 200)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false, columnDefinition = "varchar(20) default 'PUBLIC'")
    private Visibility visibility; // PUBLIC 또는 PRIVATE

    public void updateHistory(String content, Visibility visibility) {
        if (content != null && !content.trim().isEmpty()) {
            this.content = content.trim();
        }

        if (visibility != null) {
            this.visibility = visibility;
        }
    }
}
