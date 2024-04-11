package TFG.GameVault.videogame;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GamesFilter {
    public String platform;

    public String publisher;
    
    public String genre;
    
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    public LocalDate minReleaseDate;
    
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    public LocalDate maxReleaseDate;

    public String searchTerms;
}
