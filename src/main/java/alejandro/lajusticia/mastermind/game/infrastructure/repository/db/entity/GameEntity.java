package alejandro.lajusticia.mastermind.game.infrastructure.repository.db.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@Entity(name = "GAME")
public class GameEntity {

    @Id
    private String id;

    @Column(length = 1024, nullable = false)
    private String secret;

    @Column(nullable = false)
    private int maxAttempts;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "gameId")
    private List<AttemptEntity> attempts = new ArrayList<>();

}
