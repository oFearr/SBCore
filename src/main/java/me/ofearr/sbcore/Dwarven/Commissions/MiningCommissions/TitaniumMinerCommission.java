package me.ofearr.sbcore.Dwarven.Commissions.MiningCommissions;

import me.ofearr.sbcore.Dwarven.Commissions.DwarvenCommission;
import me.ofearr.sbcore.Dwarven.DwarvenManager;

public class TitaniumMinerCommission implements DwarvenCommission {
    @Override
    public String commissionName() {
        return "Titanium Miner";
    }

    @Override
    public String commissionID() {
        return "titanium_miner";
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
        return "&7Mine &a" + completionProgress() + "&7 Titanium anywhere.";
    }

    @Override
    public int completionProgress() {
        return 15;
    }

    @Override
    public void registerCommission() {
        DwarvenManager.putDwarvenCommissionToMap(commissionID(), this);
    }
}
