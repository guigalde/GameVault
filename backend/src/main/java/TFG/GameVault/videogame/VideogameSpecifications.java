package TFG.GameVault.videogame;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

import TFG.GameVault.user.User;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

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

    public static Specification<Videogame> isWhishlistedBy(Integer userId){
        return (videogame, cq, cb) ->{
            if (userId == null) {
                return cb.conjunction();
            }
            Subquery<Integer> subquery = cq.subquery(Integer.class);
            Root<User> userRoot = subquery.from(User.class);
            Join<User, Videogame> join = userRoot.join("wishlist");
            subquery.select(join.get("id"));
            subquery.where(cb.equal(userRoot.get("id"), userId));
    
            return cb.in(videogame.get("id")).value(subquery);
        };
    }
    
}
