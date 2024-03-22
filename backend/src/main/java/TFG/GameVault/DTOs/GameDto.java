package TFG.GameVault.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GameDto {
    
    private Integer id;
    private String name;
    private String description;
    private String image;
    private String platforms;
    private String genres;
    private String developer;
    private String publisher;
    private String releaseDate;
}
