package TFG.GameVault.videogame;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideogameRepository extends JpaRepository<Videogame, Integer>{
    public Optional<Videogame> findByName(String name);
}
