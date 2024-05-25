package TFG.GameVault.DTOs;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CollectionDto {
    Integer id;
    
    String name;

    String description;

    LocalDate   creationDate;

    LocalDate   lastUpdate;

    String creationDateString;

    String lastUpdateString;

    List<PersonalVideogameInfoDto> collectionGames;
}
