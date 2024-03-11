package TFG.GameVault.game;
import org.springframework.format.annotation.DateTimeFormat;

import TFG.GameVault.model.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Game extends BaseEntity{

    @NotEmpty
    public String name;

    @NotEmpty
    public String description;

    @NotEmpty
    public String image;

    @NotEmpty
    public String platforms;

    @NotEmpty
    public String genres;

    @NotEmpty
    public String developer;

    @NotEmpty
    public String publisher;

    @NotEmpty
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    public String releaseDate;
    
}
