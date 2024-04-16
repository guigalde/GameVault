package TFG.GameVault.personal_videogame;

import TFG.GameVault.videogame.GamesFilter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonalVideogameFilter extends GamesFilter{
    
    private Float minMark;

    private Float maxMark;

    private Float maxTimePlayed;

    private Float minTimePlayed;

    private Boolean completed;

    //Sort atributes are null if theres no sorting, true if its ascending and false if its descending
    private Boolean timePlayedSort;

    private Boolean markSort;
}
