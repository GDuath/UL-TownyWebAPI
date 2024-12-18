package org.unitedlands.Endpoints;

import static spark.Spark.*;

import java.util.List;
import java.util.stream.Collectors;

import org.unitedlands.TownyWebAPI;
import org.unitedlands.TownyWebApiReponse;
import org.unitedlands.DTO.DTONation;
import org.unitedlands.DTO.DTOTown;

import com.google.gson.Gson;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Town;

public class NationEndpoints {

    private final TownyWebAPI plugin;

    private long lastCallTime = 0;
    private List<Nation> cachedNations;

    public NationEndpoints(TownyWebAPI plugin) {
        this.plugin = plugin;
        Start();
    }

    public void Start() {

        get("/api/nations", (req, res) -> {
            TownyWebApiReponse response = new TownyWebApiReponse();
            try {
                long currentTime = System.currentTimeMillis();
                long elapsedTime = currentTime - lastCallTime;

                if (elapsedTime > 60000) {
                    cachedNations = TownyAPI.getInstance().getNations();
                    lastCallTime = System.currentTimeMillis();
                }

                var dtotowns = cachedNations.stream().map(t -> t.getName()).collect(Collectors.toList());

                response.data = dtotowns;
                response.success = true;
            } catch (Exception ex) {
                response.errorMessage = ex.getMessage();
            }
            return new Gson().toJson(response);
        });

        get("/api/nation/:name", (req, res) -> {
            TownyWebApiReponse response = new TownyWebApiReponse();
            try {
                var name = req.params(":name");
                var nation = TownyAPI.getInstance().getNation(name);
                if (nation != null) {
                    response.data = new DTONation(nation);
                    response.success = true;
                } else {
                    response.errorMessage = "No nation with name " + name + " found.";
                }
            } catch (Exception ex) {
                response.errorMessage = ex.getMessage();
            }
            return new Gson().toJson(response);
        });
    }

}
