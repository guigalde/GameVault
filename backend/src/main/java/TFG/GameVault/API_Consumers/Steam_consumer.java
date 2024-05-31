package TFG.GameVault.API_Consumers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;


@Component
public class Steam_consumer {

    @Value("${steam.api.key}")
    String apiKey;

    public List<Map<String,?>> getGames(String steamId){
        String url = "http://api.steampowered.com/IPlayerService/GetOwnedGames/v0001/?key="+apiKey+"&steamid="+steamId+"&format=json&include_appinfo=true";
        try{
            HttpResponse<JsonNode> response = Unirest.post(url).asJson();
            JSONArray games = response.getBody().getObject().getJSONObject("response").getJSONArray("games");
            List<Map<String,?>> gamesMap = new ArrayList<>();
            for(int i = 0; i<games.length(); i++){
                Map<String,Object> gameMap = new HashMap<>();
                gameMap.put("appid", Integer.valueOf(games.getJSONObject(i).get("appid").toString()));
                gameMap.put("name", games.getJSONObject(i).get("name"));
                gameMap.put("playtime", Integer.valueOf(games.getJSONObject(i).get("playtime_forever").toString()));
                gamesMap.add(gameMap);
            }
            return gamesMap;
        }catch(Exception e){
            throw new RuntimeException("Error getting games from Steam");
        }
    }
    
}
