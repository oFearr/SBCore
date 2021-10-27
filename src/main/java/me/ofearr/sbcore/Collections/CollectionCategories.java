package me.ofearr.sbcore.Collections;

import java.util.Arrays;
import java.util.List;

public enum CollectionCategories {
    FARMING,MINING,COMBAT,FORAGING,FISHING,BOSS;

    public static List<CollectionCategories> getCollectionCategories(){

        return Arrays.asList(values());
    }

    @Override
    public String toString(){

        switch (this){

            case FARMING: return "Farming";
            case MINING: return "Mining";
            case COMBAT: return "Combat";
            case FORAGING: return "Foraging";
            case FISHING: return "Fishing";
            case BOSS: return "Boss";

        }

        return this.toString().toLowerCase();
    }
}
