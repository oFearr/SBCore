package me.ofearr.sbcore.Dwarven.Commissions.MiningCommissions.Location;

import me.ofearr.sbcore.Dwarven.Commissions.DwarvenCommission;
import me.ofearr.sbcore.Dwarven.DwarvenManager;

public class LavaSpringsTitaniumCommission implements DwarvenCommission {
    @Override
    public String commissionName() {
        return "Lava Springs Titanium";
    }

    @Override
    public String commissionID() {
        return "lava_springs_titanium";
    }

    @Override
    public int commissionPool1() {
        return 1;
    }

    @Override
    public int commissionPool2() {
        return 3;
    }

    @Override
    public String description() {
        return "&7Mine &a" + requiredCountForCompletion() + "&7 Titanium in the Lava Springs.";
    }

    @Override
    public int requiredCountForCompletion() {
        return 10;
    }

    @Override
    public void registerCommission() {
        DwarvenManager.putDwarvenCommissionToMap(commissionID(), this);
    }
}
