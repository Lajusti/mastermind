package alejandro.lajusticia.mastermind.game.infrastructure.repository.db.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@EqualsAndHashCode(exclude = {"id", "game"})
@Entity(name = "ATTEMPT")
public class AttemptEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;

    @Column(length = 1024, nullable = false)
    private String input;

    @Column(length = 1024, nullable = false)
    private String feedback;

    @ManyToOne
    @JoinColumn(name = "gameId", nullable = false)
    private GameEntity game;

}
