package TFG.GameVault.personal_videogame;

import java.time.LocalDate;
import java.util.List;

import TFG.GameVault.collections.Collection;
import TFG.GameVault.model.BaseEntity;
import TFG.GameVault.user.User;
import TFG.GameVault.videogame.Videogame;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
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
    
    @ManyToOne
    @NotNull
    Videogame videogame;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull
    User user;

    @ManyToMany(mappedBy = "collectionGames", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    List<Collection> collections;

    @NotNull
    Float timePlayed;

    Float mark;

    LocalDate acquiredOn;

    LocalDate completedOn;

    Float completionTime;

    String platform;

    @Column(length = 1000)
    String notes;

    @Transient
    Boolean completed = completionTime != null;

    Integer steamId;
}
