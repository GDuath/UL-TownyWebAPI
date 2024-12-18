package org.unitedlands.DTO;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.palmergames.bukkit.towny.TownyEconomyHandler;
import com.palmergames.bukkit.towny.TownySettings;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.metadata.BooleanDataField;
import com.palmergames.bukkit.towny.utils.MetaDataUtil;

public class DTONation {
    public UUID uuid;
    public String name;
    public long founded;
    public String type; 
    public DTOResident leader;
    public DTOTown capital;
    public int resident_count;
    public int claim_count;
    public int town_count;
    public List<String> towns;
    public double balance;
    public double upkeep;

    public DTONation(Nation nation) {
        this.uuid = nation.getUUID();
        this.name = nation.getName();
        this.founded = nation.getRegistered(); 

        var major = new BooleanDataField("official_major_nation", true);
        var minor = new BooleanDataField("official_minor_nation", true);

        if (MetaDataUtil.getBoolean(nation, major))
            this.type = "Major Power";
        if (MetaDataUtil.getBoolean(nation, minor))
            this.type = "Minor Power";

        var leader = nation.getKing();
        this.leader = new DTOResident();
        this.leader.uuid = leader.getUUID();
        this.leader.name = leader.getName();

        var capital = nation.getCapital();
        this.capital = new DTOTown();
        this.capital.uuid = capital.getUUID();
        this.capital.name = capital.getName();

        this.resident_count = nation.getNumResidents();
        this.claim_count = nation.getNumTownblocks();
        this.town_count = nation.getNumTowns();
        this.towns = nation.getTowns().stream().map(t -> t.getName()).collect(Collectors.toList());
        this.balance = nation.getAccount().getHoldingBalance();
        if (TownyEconomyHandler.isActive() && TownySettings.isUsingEconomy())
        {
            this.upkeep = TownySettings.getNationUpkeepCost(nation);
        }
    }
}
