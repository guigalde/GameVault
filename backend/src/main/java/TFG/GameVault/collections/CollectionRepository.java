package TFG.GameVault.collections;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import TFG.GameVault.user.User;

@Repository
public interface CollectionRepository extends JpaRepository<Collection, Integer>, JpaSpecificationExecutor<Collection>{
    
    public List<Collection> findAllByUser(User user);

    public List<Collection> findAllByUser_Id(Integer userId);

    public Collection findByName(String name);
}
