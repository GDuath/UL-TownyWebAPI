package org.unitedlands.Endpoints;

import static spark.Spark.*;

import java.util.List;
import java.util.stream.Collectors;

import org.unitedlands.TownyWebAPI;
import org.unitedlands.TownyWebApiReponse;
import org.unitedlands.DTO.DTOTown;

import com.google.gson.Gson;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Town;

public class TownEndoints {

    private final TownyWebAPI plugin;

    private long lastCallTime = 0;
    private List<Town> cachedTowns;

    public TownEndoints(TownyWebAPI plugin) {
        this.plugin = plugin;
        Start();
    }

    public void Start() {

        get("/api/towns", (req, res) -> {

            TownyWebApiReponse response = new TownyWebApiReponse();
            try {

                long currentTime = System.currentTimeMillis();
                long elapsedTime = currentTime - lastCallTime;
                
                if (elapsedTime > 60000) {
                    cachedTowns = TownyAPI.getInstance().getTowns();
                    lastCallTime = System.currentTimeMillis();
                }

                var dtotowns = cachedTowns.stream().map(t -> t.getName()).collect(Collectors.toList());

                response.data = dtotowns;
                response.success = true;
            } catch (Exception ex) {
                response.errorMessage = ex.getMessage();
            }
            return new Gson().toJson(response);
        });

        get("/api/town/:name", (req, res) -> {
            TownyWebApiReponse response = new TownyWebApiReponse();
            try {
                var name = req.params(":name");
                var town = TownyAPI.getInstance().getTown(name);
                if (town != null) {
                    response.data = new DTOTown(town);
                    response.success = true;
                } else {
                    response.errorMessage = "No town with name " + name + " found.";
                }
            } catch (Exception ex) {
                response.errorMessage = ex.getMessage();
            }
            return new Gson().toJson(response);
        });
    }
}
