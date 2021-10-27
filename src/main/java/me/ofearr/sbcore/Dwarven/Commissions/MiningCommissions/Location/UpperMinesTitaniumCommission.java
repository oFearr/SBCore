package me.ofearr.sbcore.Dwarven.Commissions.MiningCommissions.Location;

import me.ofearr.sbcore.Dwarven.Commissions.DwarvenCommission;
import me.ofearr.sbcore.Dwarven.DwarvenManager;

public class UpperMinesTitaniumCommission implements DwarvenCommission {
    @Override
    public String commissionName() {
        return "Upper Mines Titanium";
    }

    @Override
    public String commissionID() {
        return "upper_mines_titanium";
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
        return "&7Mine &a" + completionProgress() + "&7 Titanium in the Upper Mines.";
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
