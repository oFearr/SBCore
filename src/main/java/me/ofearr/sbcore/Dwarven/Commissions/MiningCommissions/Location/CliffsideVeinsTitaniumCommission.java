package me.ofearr.sbcore.Dwarven.Commissions.MiningCommissions.Location;

import me.ofearr.sbcore.Dwarven.Commissions.DwarvenCommission;
import me.ofearr.sbcore.Dwarven.DwarvenManager;

public class CliffsideVeinsTitaniumCommission implements DwarvenCommission {
    @Override
    public String commissionName() {
        return "Cliffside Veins Titanium";
    }

    @Override
    public String commissionID() {
        return "cliffside_veins_titanium";
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
        return "&7Mine &a" + completionProgress() + " &7Titanium in the Cliffside Veins.";
    }

    @Override
    public int completionProgress() {
        return 10;
    }

    @Override
    public void registerCommission() {
        DwarvenManager.putDwarvenCommissionToMap(commissionID(), this);
    }
}
