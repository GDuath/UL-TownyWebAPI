package org.unitedlands.Endpoints;

import static spark.Spark.get;

import org.unitedlands.TownyWebAPI;
import org.unitedlands.TownyWebApiReponse;
import org.unitedlands.DTO.DTOBalTopEntry;

import com.Zrips.CMI.CMI;
import com.google.gson.Gson;

public class BaltopEndpoint {

    public static TownyWebAPI plugin;

    public BaltopEndpoint(TownyWebAPI plugin) {
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
    }

}
