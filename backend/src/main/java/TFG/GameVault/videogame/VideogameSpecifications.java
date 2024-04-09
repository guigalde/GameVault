package TFG.GameVault.videogame;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

public class VideogameSpecifications {
    
    public static Specification<Videogame> hasPlatform(String platform){
        return (videogame, cq, cb) ->{
            if (platform == null || platform.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(videogame.get("platforms"), "%"+platform+"%");
        };
    }

    public static Specification<Videogame> hasPublisher(String publisher){
        return (videogame, cq, cb) ->{
            if (publisher == null || publisher.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(videogame.get("publisher"), "%"+publisher+"%");
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

    public static Specification<Videogame> search(String searchTerms){
        return (videogame, cq, cb) ->{
            if (searchTerms == null || searchTerms.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(videogame.get("name"), "%"+searchTerms+"%");
        };
    }
}
