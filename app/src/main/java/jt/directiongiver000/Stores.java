package jt.directiongiver000;

/**
 * Created by lp123 on 2017/10/16.
 */

public class Stores
{
    private String name;
    private location location;
    private String ID;
    private String address;
    private String kind;
    private String description;
    private String rating;
    public Stores(String name,location location,String ID,String address,String kind,String description,String rating)
    {
        this.name = name;
        this.location = location;
        this.ID = ID;
        this.address = address;
        this.kind = kind;
        this.description = description;
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public jt.directiongiver000.location getLocation() {
        return location;
    }

    public String getID() {
        return ID;
    }

    public String getAddress() {
        return address;
    }

    public String getKind() {
        return kind;
    }

    public String getDescription() {
        return description;
    }

    public String getRating() {
        return rating;
    }
}
