package TFG.GameVault.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RoleRepository extends CrudRepository<Role, Integer>{

    Role findByRoleName(String roleName);

    
}
