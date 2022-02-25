package me.ofearr.sbcore.Dwarven.Commissions.MiningCommissions;

import me.ofearr.sbcore.Dwarven.Commissions.DwarvenCommission;
import me.ofearr.sbcore.Dwarven.DwarvenManager;

public class MithrilMinerCommission implements DwarvenCommission {
    @Override
    public String commissionName() {
        return "Mithril Miner";
    }

    @Override
    public String commissionID() {
        return "mithril_miner";
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
        return "&7Mine &a" + requiredCountForCompletion() + "&7 Mithril anywhere.";
    }

    @Override
    public int requiredCountForCompletion() {
        return 500;
    }

    @Override
    public void registerCommission() {
        DwarvenManager.putDwarvenCommissionToMap(commissionID(), this);
    }
}
