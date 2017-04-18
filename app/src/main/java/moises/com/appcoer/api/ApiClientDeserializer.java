package moises.com.appcoer.api;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import moises.com.appcoer.model.User;

public class ApiClientDeserializer {

    public static String TAG = ApiClientDeserializer.class.getSimpleName();

    static class GenericDeserializer implements JsonDeserializer{
        @Override
        public Object deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return json.getAsJsonObject();
        }
    }

    static class UserDeserializer implements JsonDeserializer<User>{
        @Override
        public User deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            Gson gson = new Gson();
            return gson.fromJson(json.getAsJsonObject().getAsJsonObject("data").toString(), User.class);
        }
    }

    static class UrlBillDeserializer implements JsonDeserializer<String>{
        @Override
        public String deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            return json.getAsJsonObject().has("urlbilling") ? json.getAsString() : "";
        }
    }
}
