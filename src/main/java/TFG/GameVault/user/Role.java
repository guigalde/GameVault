package TFG.GameVault.user;

import TFG.GameVault.model.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Entity
@Data
@Table(name="roles")
public class Role extends BaseEntity{

    @NotBlank
    public String roleName;
    
}
