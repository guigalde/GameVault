package TFG.GameVault.DTOs;

import java.time.LocalDate;
import java.util.List;

import TFG.GameVault.personal_videogame.PersonalVideogame;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CollectionDto {
    
    String name;

    String description;

    LocalDate   creationDate;

    LocalDate   lastUpdate;

    List<PersonalVideogameDto> collectionGames;
}
