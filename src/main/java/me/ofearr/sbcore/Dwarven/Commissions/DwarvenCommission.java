package me.ofearr.sbcore.Dwarven.Commissions;

public interface DwarvenCommission {
    String commissionName();
    String commissionID();
    int commissionPool1();
    int commissionPool2();

    String description();
    int requiredCountForCompletion();

    void registerCommission();

}
