package TFG.GameVault.personal_videogame;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import TFG.GameVault.user.User;

@Repository
public interface PersonalVideogameRepository extends JpaRepository<PersonalVideogame, Integer>, JpaSpecificationExecutor<PersonalVideogame>{

    @Query("SELECT p FROM PersonalVideogame p WHERE p.user = :user")
    List<PersonalVideogame> findByUser(@Param("user") User user);
    
}
