package me.ofearr.sbcore.Dwarven.Commissions.MiscCommissions;

import me.ofearr.sbcore.Dwarven.Commissions.DwarvenCommission;
import me.ofearr.sbcore.Dwarven.DwarvenManager;

public class MonolithExaminerCommission implements DwarvenCommission {
    @Override
    public String commissionName() {
        return "Monolith Examiner";
    }

    @Override
    public String commissionID() {
        return "monolith_examiner";
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
        return "&7Discover &a" + requiredCountForCompletion() + "&7 Ancient Monolith.";
    }

    @Override
    public int requiredCountForCompletion() {
        return 1;
    }

    @Override
    public void registerCommission() {
        DwarvenManager.putDwarvenCommissionToMap(commissionID(), this);
    }
}
