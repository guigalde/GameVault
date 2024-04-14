package TFG.GameVault.DTOs;

import java.time.LocalDate;
import lombok.Data;

@Data
public class PersonalVideogameDto{
    
    Integer videogameId;

    Float timePlayed;

    Float mark;

    LocalDate acquiredOn;

    LocalDate completedOn;

    Float completionTime;

    String platform;

    String notes;

    

}
