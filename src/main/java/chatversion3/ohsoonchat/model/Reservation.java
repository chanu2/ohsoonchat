package chatversion3.ohsoonchat.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class Reservation {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "reservation_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "reservation",cascade = CascadeType.ALL)
    private List<Participation> participations = new ArrayList<>();

    private String title;

    private Integer passengerNum;

    private Integer currentNum;

    @Builder
    public Reservation(Member member,
                       String title,
                       Integer passengerNum,
                       Integer currentNum
                       ) {

        this.member = member;
        this.title = title;
        this.passengerNum = passengerNum;
        this.currentNum = currentNum;

    }





}
