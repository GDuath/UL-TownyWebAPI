package org.unitedlands.DTO;

import java.util.UUID;
import com.Zrips.CMI.CMI;
import com.palmergames.bukkit.towny.object.Resident;

public class DTOResident {
    public UUID uuid;
    public String name;
    public double balance;
    public DTOTown town;
    public long joindate;
    public long lastonline;
    public String playtime;

    public DTOResident() {
    }

    public DTOResident(Resident resident) {

        if (resident != null) {
            this.uuid = resident.getUUID();
            this.name = resident.getName();
            this.balance = resident.getAccount() != null ? resident.getAccount().getHoldingBalance() : 0;
            this.joindate = resident.getRegistered();
            this.lastonline = resident.getLastOnline();

            var town = resident.getTownOrNull();
            if (town != null) {
                this.town = new DTOTown();
                this.town.uuid = town.getUUID();
                this.town.name = town.getName();
            }

            var user = CMI.getInstance().getPlayerManager().getUser(resident.getUUID());

            if (user != null) {
                this.playtime = formatPlaytime(user.getTotalPlayTime(false));
            } 
        }

    }

    public static String formatPlaytime(long playtimeMillis) {
        // Convert milliseconds to seconds
        long totalSeconds = playtimeMillis / 1000;

        // Calculate time components
        long days = totalSeconds / (24 * 3600);
        long hours = (totalSeconds % (24 * 3600)) / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;

        // Build the formatted string
        StringBuilder playtimeString = new StringBuilder();

        if (days > 0) {
            playtimeString.append(days).append("d ");
        }
        if (hours > 0 || days > 0) { // Show hours if there are days
            playtimeString.append(hours).append("h ");
        }
        playtimeString.append(minutes).append("min ");
        playtimeString.append(seconds).append("s");
    
        return playtimeString.toString();
    }
}
