package TFG.GameVault.videogame;
import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import TFG.GameVault.model.BaseEntity;
import TFG.GameVault.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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
    @Column(length = 6000)
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
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    public LocalDate releaseDate;
    
    @ManyToMany
    @JoinTable(
        name = "wishlist",
        joinColumns= @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "videogame_id")
    )
    public List<User> usersWhishlited;    
}
