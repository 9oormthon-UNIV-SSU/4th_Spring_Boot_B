package study.goorm.domain.member.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import study.goorm.domain.model.Enum.MemberStatus;
import study.goorm.domain.model.Enum.SocialType;
import study.goorm.domain.model.entity.BaseEntity;

@Entity
@Getter
@Builder
@DynamicUpdate
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(nullable = false,length = 50)
    private String email;

    @Column(length = 50, unique = true)
    private String clokeyId;

    @Column(length = 20)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SocialType socialType;

    private String profileUrl;

    private String profileBackUrl;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(15) DEFAULT 'ACTIVE'", nullable = false)
    private MemberStatus status;

    @Column(length = 50)
    private String bio;
}
