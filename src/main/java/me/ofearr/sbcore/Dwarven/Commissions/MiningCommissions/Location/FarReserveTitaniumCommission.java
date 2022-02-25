package me.ofearr.sbcore.Dwarven.Commissions.MiningCommissions.Location;

import me.ofearr.sbcore.Dwarven.Commissions.DwarvenCommission;
import me.ofearr.sbcore.Dwarven.DwarvenManager;

public class FarReserveTitaniumCommission implements DwarvenCommission {
    @Override
    public String commissionName() {
        return "Far Reserve Titanium";
    }

    @Override
    public String commissionID() {
        return "far_reserve_titanium";
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
        return "&7Mine &a" + requiredCountForCompletion() + "&7 Titanium in the Far Reserve.";
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
