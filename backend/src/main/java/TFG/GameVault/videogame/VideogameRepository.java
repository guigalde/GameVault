package TFG.GameVault.videogame;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface VideogameRepository extends JpaRepository<Videogame, Integer>, JpaSpecificationExecutor<Videogame>{
    
    public Optional<Videogame> findByName(String name);

    @Query("SELECT DISTINCT genres FROM Videogame")
    public Set<String> getGenres();

    @Query("SELECT DISTINCT platforms FROM Videogame")
    public Set<String> getPlatforms();

    @Query("SELECT DISTINCT publisher FROM Videogame")
    public Set<String> getPublishers();
}
