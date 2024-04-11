package TFG.GameVault.DTOs;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideogameDto {
    
    public String name;

    public String description;

    public String image;

    public String platforms;

    public String genres;

    public String developer;

    public String publisher;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    public LocalDate releaseDate;
}
