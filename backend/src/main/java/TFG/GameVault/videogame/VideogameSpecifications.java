package TFG.GameVault.videogame;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

public class VideogameSpecifications {
    
    public static Specification<Videogame> hasPlatform(String platform){
        return (videogame, cq, cb) ->{
            if (platform == null || platform.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(videogame.get("platforms")), "%"+platform.toLowerCase()+"%");
        };
    }

    public static Specification<Videogame> hasPublisher(String publisher){
        return (videogame, cq, cb) ->{
            if (publisher == null || publisher.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(videogame.get("publisher")), "%"+publisher.toLowerCase()+"%");
        };
    }

    public static Specification<Videogame> filterByDate(LocalDate minReleaseDate, LocalDate maxReleaseDate){
        return (videogame, cq, cb) ->{
            if (minReleaseDate == null&& maxReleaseDate == null) {
                return cb.conjunction();
            }else if(minReleaseDate == null){
                return cb.lessThanOrEqualTo(videogame.get("releaseDate"), maxReleaseDate);
            }else if(maxReleaseDate == null){
                return cb.greaterThanOrEqualTo(videogame.get("releaseDate"), minReleaseDate);
            }else{
                return cb.between(videogame.get("releaseDate"), minReleaseDate, maxReleaseDate);
            }
        };
    }

    public static Specification<Videogame> hasGenre(String genre){
        return (videogame, cq, cb) ->{
            if (genre == null || genre.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(videogame.get("genres")), "%"+genre.toLowerCase()+"%");
        };
    }
    
    public static Specification<Videogame> search(String searchTerms){
        return (videogame, cq, cb) ->{
            if (searchTerms == null || searchTerms.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(videogame.get("name")), "%" + searchTerms.toLowerCase() + "%");
        };
    }
    
}
