package org.unitedlands.Endpoints;

import static spark.Spark.*;

import java.util.Collection;
import java.util.stream.Collectors;

import org.unitedlands.TownyWebAPI;
import org.unitedlands.TownyWebApiReponse;
import org.unitedlands.DTO.DTOResident;

import com.google.gson.Gson;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.object.Resident;

public class ResidentEndpoints {

    private final TownyWebAPI plugin;

    private long lastCallTime = 0;
    private Collection<Resident> cachedResidents;

    public ResidentEndpoints(TownyWebAPI plugin) {
        this.plugin = plugin;
        Start();
    }

    public void Start() {

        get("/api/resident/:name", (req, res) -> {
            TownyWebApiReponse response = new TownyWebApiReponse();
            try {
                var name = req.params(":name");
                Resident resident = TownyUniverse.getInstance().getResident(name);

                if (resident != null) {
                    response.data = new DTOResident(resident);
                    response.success = true;
                } else {
                    response.errorMessage = "Player " + name + " not found.";
                }
            } catch (Exception ex) {
                response.errorMessage = ex.getMessage();
            }
            return new Gson().toJson(response);
        });

        get("/api/residents", (req, res) -> {
            TownyWebApiReponse response = new TownyWebApiReponse();
            try {

                long currentTime = System.currentTimeMillis();
                long elapsedTime = currentTime - lastCallTime;
                
                if (elapsedTime > 60000) {
                    cachedResidents = TownyAPI.getInstance().getResidents();
                    lastCallTime = System.currentTimeMillis();
                }

                var dtoresidents = cachedResidents.stream().map(t -> t.getName()).collect(Collectors.toList());
                response.data = dtoresidents;
                response.success = true;
            } catch (Exception ex) {
                response.errorMessage = ex.getMessage();
            }
            return new Gson().toJson(response);
        });

    }
}
