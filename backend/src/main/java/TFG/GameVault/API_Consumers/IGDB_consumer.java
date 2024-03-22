package TFG.GameVault.API_Consumers;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.springframework.stereotype.Component;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import TFG.GameVault.videogame.Videogame;

@Component
public class IGDB_consumer {
    
    String userId = "tc0ofgmcuu0urgnudg7g3bgpyj1yzg";
    String userKey= "6as0obusv47dli8cd7yslwkq70a9bc";

    public HttpResponse<JsonNode> getAuthentication(){
        try{
            HttpResponse<JsonNode> response = Unirest.post("https://id.twitch.tv/oauth2/token")
            .header("Content-Type", "application/x-www-form-urlencoded")
            .field("client_id", userId)
            .field("client_secret", userKey)
            .field("grant_type", "client_credentials")
            .asJson();
            return response;
        }catch (UnirestException e){
            throw new RuntimeException("Error getting authentication from Twitch");
        }
        
    }

    public  List<Videogame> getGames(String offset){
        HttpResponse<JsonNode> authentication = getAuthentication();
        String access_token = authentication.getBody().getObject().get("access_token").toString();
        List<Videogame> res = new ArrayList<>();
        try{
            HttpResponse<JsonNode> games1 = Unirest.post("https://api.igdb.com/v4/games")
            .header("Client-ID", userId)
            .header("Authorization", "Bearer "+access_token)
            .header("Accept", "application/json")
            .body("fields name, genres, platforms, summary, first_release_date, involved_companies, cover; sort total_rating_count desc; limit 500; offset "+ offset +";")
            .asJson();
            
            JSONArray gamesArray = games1.getBody().getArray();
            for(int i = 0; i<gamesArray.length(); i++){
                try{
                    Videogame game = new Videogame();
                    game.setName(gamesArray.getJSONObject(i).getString("name"));
                    game.setDescription(gamesArray.getJSONObject(i).getString("summary"));
                    long unixTimestamp = gamesArray.getJSONObject(i).getLong("first_release_date");
                    LocalDate releaseDate = Instant.ofEpochSecond(unixTimestamp).atZone(ZoneId.systemDefault()).toLocalDate();
                    game.setReleaseDate(releaseDate);

                    JSONArray genres = gamesArray.getJSONObject(i).getJSONArray("genres");
                    String genreString = "";
                    for(int j = 0; j<genres.length(); j++){
                        HttpResponse<JsonNode> genreNames = Unirest.post("https://api.igdb.com/v4/genres")
                        .header("Client-ID", userId)
                        .header("Authorization", "Bearer "+access_token)
                        .header("Accept", "application/json")
                        .body("fields name; where id = "+genres.getLong(j)+"; limit 1;")
                        .asJson();
                        genreString = genreString + (genreString.equals("")? "":", ") + (genreNames.getBody().getArray().getJSONObject(0).getString("name"));
                    }
                    game.setGenres(genreString);

                    JSONArray platforms = gamesArray.getJSONObject(i).getJSONArray("platforms");
                    String platformString = "";
                    for(int j = 0; j<platforms.length(); j++){
                        HttpResponse<JsonNode> platformNames = Unirest.post("https://api.igdb.com/v4/platforms")
                        .header("Client-ID", userId)
                        .header("Authorization", "Bearer "+access_token)
                        .header("Accept", "application/json")
                        .body("fields name; where id = "+ platforms.getLong(j)+"; limit 1;")
                        .asJson();
                        platformString = platformString + (platformString.equals("")? "":", ") + (platformNames.getBody().getArray().getJSONObject(0).getString("name"));
                    }
                    game.setPlatforms(platformString);

                    JSONArray involvedCompanies = gamesArray.getJSONObject(i).getJSONArray("involved_companies");
                    String publishers = "";
                    String developers = "";
                    for(int j = 0; j<involvedCompanies.length(); j++){
                        HttpResponse<JsonNode> involvedCompany = Unirest.post("https://api.igdb.com/v4/involved_companies")
                        .header("Client-ID", userId)
                        .header("Authorization", "Bearer "+access_token)
                        .header("Accept", "application/json")
                        .body("fields company, developer, publisher; where id = "+involvedCompanies.getLong(j)+"; limit 1;")
                        .asJson();
                        Boolean developer = involvedCompany.getBody().getArray().getJSONObject(0).getBoolean("developer");
                        Boolean publisher = involvedCompany.getBody().getArray().getJSONObject(0).getBoolean("publisher");
                        Long id = involvedCompany.getBody().getArray().getJSONObject(0).getLong("company");

                        HttpResponse<JsonNode> companyName = Unirest.post("https://api.igdb.com/v4/companies")
                        .header("Client-ID", userId)
                        .header("Authorization", "Bearer "+access_token)
                        .header("Accept", "application/json")
                        .body("fields name; where id = "+id+"; limit 1;")
                        .asJson();
                        if(developer){
                            developers = developers + (developers.equals("")? "":", ") + companyName.getBody().getArray().getJSONObject(0).getString("name");
                        }else if (publisher){
                            publishers = publishers + (publishers.equals("")? "":", ") + companyName.getBody().getArray().getJSONObject(0).getString("name");
                        }
                    }

                    Long cover = gamesArray.getJSONObject(i).getLong("cover");
                    HttpResponse<JsonNode> coverUrl = Unirest.post("https://api.igdb.com/v4/covers")
                    .header("Client-ID", userId)
                    .header("Authorization", "Bearer "+access_token)
                    .header("Accept", "application/json")
                    .body("fields url; where id = "+ cover +"; limit 1;")
                    .asJson();
                    game.setImage(coverUrl.getBody().getArray().getJSONObject(0).getString("url"));

                    game.setDeveloper(developers);
                    game.setPublisher(publishers);
                    res.add(game);
                }catch(Exception e){
                    System.out.println("Error getting game: "+i);
                }
            }
        }catch(UnirestException e){
            throw new RuntimeException("Error getting games from IGDB");
        }
        
        return res;
     }  

     
    
}
