package chatversion3.ohsoonchat.model;



import chatversion3.ohsoonchat.common.AccountRole;
import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter
@Entity
public class Member {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String name;

    private String email;

    private String profilePath;

    @Enumerated(EnumType.STRING)
    private AccountRole accountRole = AccountRole.USER;

    @Builder
    public Member( String name, String email, String profilePath) {
        this.name = name;
        this.email = email;
        this.profilePath = profilePath;
    }



}
