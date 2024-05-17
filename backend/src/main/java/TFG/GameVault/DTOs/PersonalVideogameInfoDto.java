package TFG.GameVault.DTOs;

import java.time.LocalDate;


import lombok.Data;

@Data
public class PersonalVideogameInfoDto {
    
    Integer id;
    
    VideogameDto videogame;

    Float timePlayed;

    Float mark;

    String acquiredOn;

    String completedOn;

    Float completionTime;

    String platform;

    String notes;
}
