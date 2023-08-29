package vn.com.ids.myachef.business.zalo.article.response;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleTokenResponse {

    private int error;

    private String message;

    @SerializedName("data")
    @JsonAdapter(TokenDeserializer.class)
    private String token;

    public static class TokenDeserializer implements JsonDeserializer<String> {

        @Override
        public String deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
            JsonElement tokenElement = json.getAsJsonObject().get("token");
            if (tokenElement != null) {
                return tokenElement.getAsString();
            }
            return "";
        }
    }
}
