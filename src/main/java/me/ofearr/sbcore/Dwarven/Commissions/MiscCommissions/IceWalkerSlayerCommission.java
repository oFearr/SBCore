package me.ofearr.sbcore.Dwarven.Commissions.MiscCommissions;

import me.ofearr.sbcore.Dwarven.Commissions.DwarvenCommission;
import me.ofearr.sbcore.Dwarven.DwarvenManager;

public class IceWalkerSlayerCommission implements DwarvenCommission {
    @Override
    public String commissionName() {
        return "Ice Walker Slayer";
    }

    @Override
    public String commissionID() {
        return "ice_walker_slayer";
    }

    @Override
    public int commissionPool1() {
        return 2;
    }

    @Override
    public int commissionPool2() {
        return 4;
    }

    @Override
    public String description() {
        return "&7Slay &a" + completionProgress() + "&7 Ice walkers.";
    }

    @Override
    public int completionProgress() {
        return 75;
    }

    @Override
    public void registerCommission() {
        DwarvenManager.putDwarvenCommissionToMap(commissionID(), this);
    }
}
