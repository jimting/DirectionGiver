package jt.directiongiver000;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by lp123 on 2017/10/2.
 */

public class ConvenienceStore
{
    private String name;
    private LatLng position;
    private String address;

    public ConvenienceStore(String name,LatLng position,String address)
    {
        this.name = name;
        this.position = position;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LatLng getPosition() {
        return position;
    }

    public void setPosition(LatLng position) {
        this.position = position;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
