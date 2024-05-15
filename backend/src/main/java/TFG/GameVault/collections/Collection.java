package TFG.GameVault.collections;

import java.time.LocalDate;
import java.util.List;

import TFG.GameVault.model.BaseEntity;
import TFG.GameVault.personal_videogame.PersonalVideogame;
import TFG.GameVault.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Collection extends BaseEntity{
    
    @NotNull
    String name;

    @Size(max = 1000)
    String description;
    
    @NotNull
    LocalDate creationDate;

    @NotNull
    LocalDate lastUpdate;

    @NotNull
    @ManyToMany
    @JoinTable(
        name = "collection_games",
        joinColumns= @JoinColumn(name = "personalgame_id"),
        inverseJoinColumns = @JoinColumn(name = "collection_id")
    )
    List<PersonalVideogame> collectionGames;

    @NotNull
    @ManyToOne
    User user;
}
