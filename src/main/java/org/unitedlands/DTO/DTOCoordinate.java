package org.unitedlands.DTO;

public class DTOCoordinate {
    public int x;
    public int z;
    public int index;

    public DTOCoordinate(int x, int z)
    {
        this.x = x;
        this.z = z;
    }

    public DTOCoordinate(int x, int z, int index)
    {
        this.x = x;
        this.z = z;
        this.index = index;
    }
}
