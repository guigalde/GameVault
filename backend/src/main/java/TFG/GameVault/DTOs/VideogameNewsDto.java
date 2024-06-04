package TFG.GameVault.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideogameNewsDto {

    public String gameName;

    public String newsTitle;

    public String newsContent;

    public String newsUrl;
    
}
