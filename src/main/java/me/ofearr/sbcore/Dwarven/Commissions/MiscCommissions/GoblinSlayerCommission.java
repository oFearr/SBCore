package me.ofearr.sbcore.Dwarven.Commissions.MiscCommissions;

import me.ofearr.sbcore.Dwarven.Commissions.DwarvenCommission;
import me.ofearr.sbcore.Dwarven.DwarvenManager;

public class GoblinSlayerCommission implements DwarvenCommission {
    @Override
    public String commissionName() {
        return "Goblin Slayer";
    }

    @Override
    public String commissionID() {
        return "goblin_slayer";
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
        return "&7Slay &a" + completionProgress() + "&7 Goblins.";
    }

    @Override
    public int completionProgress() {
        return 50;
    }

    @Override
    public void registerCommission() {
        DwarvenManager.putDwarvenCommissionToMap(commissionID(), this);
    }
}
