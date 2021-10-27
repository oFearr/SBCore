package me.ofearr.sbcore.Dwarven.Commissions.MiningCommissions.Location;

import me.ofearr.sbcore.Dwarven.Commissions.DwarvenCommission;
import me.ofearr.sbcore.Dwarven.DwarvenManager;

public class UpperMinesMithrilCommission implements DwarvenCommission {
    @Override
    public String commissionName() {
        return "Upper Mines Mithril";
    }

    @Override
    public String commissionID() {
        return "upper_mines_mithril";
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
        return "&7Mine &a" + completionProgress() + "&7 Mithril in the Upper Mines.";
    }

    @Override
    public int completionProgress() {
        return 350;
    }

    @Override
    public void registerCommission() {
        DwarvenManager.putDwarvenCommissionToMap(commissionID(), this);
    }
}
