package org.unitedlands.Endpoints;

import static spark.Spark.get;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.unitedlands.TownyWebAPI;
import org.unitedlands.TownyWebApiReponse;
import org.unitedlands.DTO.DTOBalTopEntry;

import com.Zrips.CMI.CMI;
import com.google.gson.Gson;
import com.palmergames.bukkit.towny.TownyAPI;

public class LeaderboardsEndpoint {

    public static TownyWebAPI plugin;

    public LeaderboardsEndpoint(TownyWebAPI plugin) {
        this.plugin = plugin;
        Start();
    }

    public void Start() {

        get("/api/baltop", (req, res) -> {
            TownyWebApiReponse response = new TownyWebApiReponse();
            try {
                var em = CMI.getInstance().getEconomyManager();
                var pm = CMI.getInstance().getPlayerManager();

                em.recalculateBalTop();

                var baltop = em.getBalTopMap();
                var bals = new DTOBalTopEntry[10];
                var counter = 0;

                for (double bal : baltop.keySet()) {

                    bals[counter] = new DTOBalTopEntry();
                    bals[counter].balance = bal;
                    bals[counter].name = pm.getUser(baltop.get(bal)).getName();

                    counter++;
                    if (counter > 9)
                        break;
                }

                response.success = true;
                response.data = bals;
            } catch (Exception ex) {
                response.errorMessage = ex.getMessage();
            }
            return new Gson().toJson(response);
        });

        get("/api/baltoptown", (req, res) -> {
            TownyWebApiReponse response = new TownyWebApiReponse();
            try {
                var towns = TownyAPI.getInstance().getTowns();

                List<DTOBalTopEntry> bals = towns.stream().map(t -> new DTOBalTopEntry(t.getName(), t.getAccount().getHoldingBalance())).collect(Collectors.toList());
                bals.sort(Comparator.comparingDouble((DTOBalTopEntry t) -> t.balance).reversed());
                bals = bals.subList(0, Math.min(bals.size(), 10));

                response.success = true;
                response.data = bals;
            } catch (Exception ex) {
                response.errorMessage = ex.getMessage();
            }
            return new Gson().toJson(response);
        });

        get("/api/baltopnation", (req, res) -> {
            TownyWebApiReponse response = new TownyWebApiReponse();
            try {
                var nations = TownyAPI.getInstance().getNations();

                List<DTOBalTopEntry> bals = nations.stream().map(t -> new DTOBalTopEntry(t.getName(), t.getAccount().getHoldingBalance())).collect(Collectors.toList());
                bals.sort(Comparator.comparingDouble((DTOBalTopEntry t) -> t.balance).reversed());
                bals = bals.subList(0, Math.min(bals.size(), 10));

                response.success = true;
                response.data = bals;
            } catch (Exception ex) {
                response.errorMessage = ex.getMessage();
            }
            return new Gson().toJson(response);
        });
    }

}
