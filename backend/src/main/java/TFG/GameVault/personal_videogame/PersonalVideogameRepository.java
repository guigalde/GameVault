package TFG.GameVault.personal_videogame;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface PersonalVideogameRepository extends JpaRepository<PersonalVideogame, Integer>, JpaSpecificationExecutor<PersonalVideogame>{

    public List<PersonalVideogame> findAllByUser_Id(Integer user_id);

    @Query("SELECT pv FROM PersonalVideogame pv WHERE pv.user.id = :userId AND pv.videogame.id = :videogameId")
    PersonalVideogame findByUserIdAndVideogameId(@Param("userId") Integer userId, @Param("videogameId") Integer videogameId);
}
