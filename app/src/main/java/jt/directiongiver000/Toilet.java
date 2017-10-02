package jt.directiongiver000;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by lp123 on 2017/9/18.
 */

public class Toilet
{
    private String name;
    private LatLng position;
    private String address;
    private String grade;
    public Toilet(String name,LatLng position,String address,String grade)
    {
        this.name = name;
        this.position = position;
        this.address = address;
        this.grade = grade;
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

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}
