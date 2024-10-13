package Engine;

import com.google.gson.*;

import java.lang.reflect.Type;

public class ComponentDeserializer implements JsonDeserializer<Component>, JsonSerializer<Component>
{
    @Override
    public Component deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException
    {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String classType = jsonObject.get("type").getAsString();
        JsonElement element = jsonObject.get("properties");
        try
        {
            return jsonDeserializationContext.deserialize(element, Class.forName(classType));
        }
        catch(ClassNotFoundException e)
        {
            throw new JsonParseException("Unknown element type: " + type, e);
        }
    }

    @Override
    public JsonElement serialize(Component component, Type type, JsonSerializationContext jsonSerializationContext)
    {
        JsonObject result = new JsonObject();
        result.add("type", new JsonPrimitive(component.getClass().getCanonicalName()));
        result.add("properties", jsonSerializationContext.serialize(component, component.getClass()));
        return result;
    }
}
