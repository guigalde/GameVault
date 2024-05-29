package TFG.GameVault.user;

import java.util.List;

import TFG.GameVault.collections.Collection;
import TFG.GameVault.model.BaseEntity;
import TFG.GameVault.personal_videogame.PersonalVideogame;
import TFG.GameVault.videogame.Videogame;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name="users")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class User extends BaseEntity{

    @NotBlank
    public String username;

    @NotBlank
    public String password;

    @NotBlank
    @Email
    public String email;

    @ManyToOne
    @NotNull
    public Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    public List<PersonalVideogame> personalVideogames;

    @ManyToMany(mappedBy = "usersWhishlited", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    public List<Videogame> wishlist;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    public List<Collection> collections;
    
}
