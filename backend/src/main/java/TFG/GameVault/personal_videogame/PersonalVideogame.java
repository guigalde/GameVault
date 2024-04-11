package TFG.GameVault.personal_videogame;

import java.time.LocalDate;

import TFG.GameVault.model.BaseEntity;
import TFG.GameVault.user.User;
import TFG.GameVault.videogame.Videogame;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonalVideogame extends BaseEntity{
    
    @OneToOne
    @NotNull
    Videogame videogame;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull
    User user;

    @NotNull
    Float timePlayed;

    Float mark;

    LocalDate aquiredOn;

    LocalDate completedOn;

    Float completionTime;

    String platform;

    @Column(length = 1000)
    String notes;

    @Transient
    Boolean completed = completionTime != null;
}
