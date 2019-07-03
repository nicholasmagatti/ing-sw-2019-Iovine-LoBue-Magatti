package it.polimi.ProgettoIngSW2019.common.utilities;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import it.polimi.ProgettoIngSW2019.common.LightModel.*;
import it.polimi.ProgettoIngSW2019.common.enums.SquareType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TypeAdapterSquareLM extends TypeAdapter<SquareLM>{
    @Override
    public void write(JsonWriter jsonWriter, SquareLM squareLM) throws IOException {
        if(squareLM != null) {
            if (squareLM.getSquareType().equals(SquareType.AMMO_POINT)) {
                AmmoPointLM ammoPointLM = (AmmoPointLM) squareLM;
                jsonWriter.beginObject();
                jsonWriter.name("AmmoPoint");
                jsonWriter.beginObject();
                jsonWriter.name("players").beginArray();
                for (int id : ammoPointLM.getPlayers())
                    jsonWriter.value(id);
                jsonWriter.endArray();
                jsonWriter.name("blockedAtNorth").value(ammoPointLM.isBlockedAtNorth());
                jsonWriter.name("blockedAtEast").value(ammoPointLM.isBlockedAtEast());
                jsonWriter.name("blockedAtSouth").value(ammoPointLM.isBlockedAtSouth());
                jsonWriter.name("blockedAtWest").value(ammoPointLM.isBlockedAtWest());
                jsonWriter.name("idRoom").value(ammoPointLM.getIdRoom());
                jsonWriter.name("ammoCard").value(new Gson().toJson(ammoPointLM.getAmmoCard()));
                jsonWriter.endObject();
                jsonWriter.endObject();
            } else if (squareLM.getSquareType().equals(SquareType.SPAWNING_POINT)) {
                SpawnPointLM spwanPointLM = (SpawnPointLM) squareLM;
                jsonWriter.beginObject();
                jsonWriter.name("SpawningPoint");
                jsonWriter.beginObject();
                jsonWriter.name("players").beginArray();
                for (int id : spwanPointLM.getPlayers())
                    jsonWriter.value(id);
                jsonWriter.endArray();
                jsonWriter.name("blockedAtNorth").value(spwanPointLM.isBlockedAtNorth());
                jsonWriter.name("blockedAtEast").value(spwanPointLM.isBlockedAtEast());
                jsonWriter.name("blockedAtSouth").value(spwanPointLM.isBlockedAtSouth());
                jsonWriter.name("blockedAtWest").value(spwanPointLM.isBlockedAtWest());
                jsonWriter.name("idRoom").value(spwanPointLM.getIdRoom());
                jsonWriter.name("weapons").value(new Gson().toJson(spwanPointLM.getWeapons(), new TypeToken<List<WeaponLM>>(){}.getType()));
                jsonWriter.endObject();
                jsonWriter.endObject();
            }
        }else
            jsonWriter.nullValue();
    }

    @Override
    public SquareLM read(JsonReader jsonReader) throws IOException {
        String type;
        List<Integer> players = new ArrayList<>();
        boolean blockedAtNorth = true;
        boolean blockedAtEast = true;
        boolean blockedAtSouth = true;
        boolean blockedAtWest = true;
        int idRoom = -1;

        AmmoCardLM ammoCardLM = null;
        List<WeaponLM> weaponLMList = new ArrayList<>();

        JsonToken check = jsonReader.peek();
        if(check == JsonToken.NULL) {
            type = "null";
            jsonReader.skipValue();
        }
        else {
            jsonReader.beginObject();
            type = jsonReader.nextName();
            jsonReader.beginObject();
            while (jsonReader.hasNext()) {
                switch (jsonReader.nextName()) {
                    case "players":
                        jsonReader.beginArray();
                        if(jsonReader.hasNext()) {
                            while (jsonReader.hasNext())
                                players.add(jsonReader.nextInt());
                        }else
                            players = new ArrayList<>();
                        jsonReader.endArray();
                        break;
                    case "blockedAtNorth":
                        blockedAtNorth = jsonReader.nextBoolean();
                        break;
                    case "blockedAtSouth":
                        blockedAtSouth = jsonReader.nextBoolean();
                        break;
                    case "blockedAtEast":
                        blockedAtEast = jsonReader.nextBoolean();
                        break;
                    case "blockedAtWest":
                        blockedAtWest = jsonReader.nextBoolean();
                        break;
                    case "idRoom":
                        idRoom = jsonReader.nextInt();
                        break;
                    case "ammoCard":
                        ammoCardLM = new Gson().fromJson(jsonReader.nextString(), AmmoCardLM.class);
                        break;
                    case "weapons":
                        weaponLMList = new Gson().fromJson(jsonReader.nextString(), new TypeToken<List<WeaponLM>>(){}.getType());
                        break;
                }
            }
            jsonReader.endObject();
            jsonReader.endObject();
        }

        if(type.equalsIgnoreCase("ammopoint"))
            return new AmmoPointLM(players, ammoCardLM, blockedAtNorth, blockedAtEast, blockedAtSouth, blockedAtWest, idRoom);
        else if(type.equalsIgnoreCase("spawningpoint"))
            return new SpawnPointLM(players, weaponLMList, blockedAtNorth, blockedAtEast, blockedAtSouth, blockedAtWest, idRoom);
        else
            return null;
    }
}
