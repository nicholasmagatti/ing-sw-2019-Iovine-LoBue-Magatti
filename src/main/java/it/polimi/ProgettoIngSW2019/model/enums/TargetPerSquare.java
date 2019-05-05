package it.polimi.ProgettoIngSW2019.model.enums;

public enum TargetPerSquare {
    ALL("all"),
    ONE_FOR_SQUARE("one_for_square"),
    AS_YOU_WISH("as_you_wish");

    private String stringValue;

    TargetPerSquare(String stringValue){
        this.stringValue = stringValue;
    }

    public static TargetPerSquare fromString(String value) {
        for(TargetPerSquare tps: values()){
            String tpsStringVal = tps.stringValue;
            if(tpsStringVal.equals(value)){
                return tps;
            }
        }
        return null;
    }
}
