package org.unitedlands.DTO;

import java.util.UUID;

public class DTOGenericNamed {
    public UUID uuid;
    public String name;

    public DTOGenericNamed(UUID uuid, String name)
    {
        this.uuid = uuid;
        this.name = name;
    }
}
