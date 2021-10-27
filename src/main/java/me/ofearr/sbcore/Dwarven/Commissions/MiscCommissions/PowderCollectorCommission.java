package me.ofearr.sbcore.Dwarven.Commissions.MiscCommissions;

import me.ofearr.sbcore.Dwarven.Commissions.DwarvenCommission;
import me.ofearr.sbcore.Dwarven.DwarvenManager;

public class PowderCollectorCommission implements DwarvenCommission {
    @Override
    public String commissionName() {
        return "Powder Collector";
    }

    @Override
    public String commissionID() {
        return "powder_collector";
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
        return "&7Collect &a" + completionProgress() + "&7 Mithril powder.";
    }

    @Override
    public int completionProgress() {
        return 2500;
    }

    @Override
    public void registerCommission() {
        DwarvenManager.putDwarvenCommissionToMap(commissionID(), this);
    }
}
