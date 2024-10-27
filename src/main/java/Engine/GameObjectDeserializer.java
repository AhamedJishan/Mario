package Engine;

import Components.Component;
import com.google.gson.*;

import java.lang.reflect.Type;

public class GameObjectDeserializer implements JsonDeserializer<GameObject>
{
    @Override
    public GameObject deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException
    {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String name = jsonObject.get("name").getAsString();
        JsonArray componentsArray = jsonObject.getAsJsonArray("components");

        GameObject gameObject = new GameObject(name);
        for (JsonElement element : componentsArray)
        {
            Component component = jsonDeserializationContext.deserialize(element, Component.class);
            gameObject.AddComponent(component);
        }
        gameObject.transform = gameObject.GetComponent(Transform.class);
        return gameObject;
    }
}
