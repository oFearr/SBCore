package me.ofearr.sbcore.Dwarven.Commissions.MiningCommissions.Location;

import me.ofearr.sbcore.Dwarven.Commissions.DwarvenCommission;
import me.ofearr.sbcore.Dwarven.DwarvenManager;

public class RoyalMinesTitaniumCommission implements DwarvenCommission {
    @Override
    public String commissionName() {
        return "Royal Mines Titanium";
    }

    @Override
    public String commissionID() {
        return "royal_mines_titanium";
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
        return "&7Mine &a" + requiredCountForCompletion() + "&7 Titanium in the Royal Mines.";
    }

    @Override
    public int requiredCountForCompletion() {
        return 350;
    }

    @Override
    public void registerCommission() {
        DwarvenManager.putDwarvenCommissionToMap(commissionID(), this);
    }
}
