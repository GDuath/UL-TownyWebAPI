package org.unitedlands.DTO;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.palmergames.bukkit.towny.TownyEconomyHandler;
import com.palmergames.bukkit.towny.TownySettings;
import com.palmergames.bukkit.towny.object.Town;

public class DTOTown {
    public UUID uuid;
    public String name;
    public long founded;
    public DTOResident mayor;
    public DTONation nation;
    public boolean is_public;
    public Integer loc_x;
    public Integer loc_z;
    public double tax;
    public Boolean is_tax_percent;
    public double upkeep;
    public Integer resident_count;
    public Integer claim_count;
    public double balance;

    public List<DTOCoordinate> blocks;

    public DTOTown() {
    }

    public DTOTown(Town town) {
        this.uuid = town.getUUID();
        this.name = town.getName();
        this.founded = town.getRegistered();

        var mayor = town.getMayor();
        this.mayor = new DTOResident();
        this.mayor.uuid = mayor.getUUID();
        this.mayor.name = mayor.getName();

        var nation = town.getNationOrNull();
        if (nation != null)
            this.nation = new DTONation(nation);

        this.is_public = town.isPublic();

        var homeblock = town.getHomeBlockOrNull();
        if (homeblock != null) {
            this.loc_x = homeblock.getX();
            this.loc_z = homeblock.getZ();
        }

        this.tax = town.getTaxes();
        this.is_tax_percent = town.isTaxPercentage();

        if (TownyEconomyHandler.isActive() && TownySettings.isUsingEconomy())
        {
            this.upkeep = TownySettings.getTownUpkeepCost(town);
        }

        this.resident_count = town.getNumResidents();
        this.claim_count = town.getNumTownBlocks();

        this.balance = town.getAccount() != null ? town.getAccount().getHoldingBalance() : 0;

        var blockMap = town.getTownBlockMap();
        this.blocks = blockMap.keySet().stream().map(c -> new DTOCoordinate(c.getX(), c.getZ())).collect(Collectors.toList());
    }
}
