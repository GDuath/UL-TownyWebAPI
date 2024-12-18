package org.unitedlands;

import static spark.Spark.*;

import java.util.Collection;

import org.bukkit.plugin.java.JavaPlugin;
import org.unitedlands.Endpoints.BaltopEndpoint;
import org.unitedlands.Endpoints.NationEndpoints;
import org.unitedlands.Endpoints.ResidentEndpoints;
import org.unitedlands.Endpoints.TownEndoints;

public class TownyWebAPI extends JavaPlugin {

    @Override
    public void onEnable() {

        port(4567);

        startEndpoints();

        getLogger().info("United Lands Towny Web API 0.1.1 initialized.");
    }

    @Override
    public void onDisable() {
        stop();
    }

    public void startEndpoints() {

        var residentsEndpoints = new ResidentEndpoints(this);
        var townEndoints = new TownEndoints(this);
        var nationEndpoint = new NationEndpoints(this);
        var baltopEndpoints = new BaltopEndpoint(this);
        
    }

}
