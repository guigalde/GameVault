package TFG.GameVault.videogame;
import java.time.LocalDate;
import java.util.List;


import TFG.GameVault.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Videogame extends BaseEntity{

    @NotEmpty
    public String name;

    @NotEmpty
    @Column(length = 3000)
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
    public LocalDate releaseDate;
    
}
