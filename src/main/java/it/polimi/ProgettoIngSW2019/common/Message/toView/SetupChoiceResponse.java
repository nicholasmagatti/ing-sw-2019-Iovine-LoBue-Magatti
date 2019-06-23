package it.polimi.ProgettoIngSW2019.common.Message.toView;

import it.polimi.ProgettoIngSW2019.common.LightModel.MapLM;

import java.util.List;

public class SetupChoiceResponse extends MessageConnection {
    List<MapLM> mapLMList;

    public SetupChoiceResponse(String username, String hostname, List<MapLM> mapLMList){
        super(username, hostname);
        this.mapLMList = mapLMList;
    }

    public List<MapLM> getMapLMList() {
        return mapLMList;
    }
}
