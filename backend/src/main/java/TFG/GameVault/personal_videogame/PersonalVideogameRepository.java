package TFG.GameVault.personal_videogame;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface PersonalVideogameRepository extends JpaRepository<PersonalVideogame, Integer>, JpaSpecificationExecutor<PersonalVideogame>{
    
}
