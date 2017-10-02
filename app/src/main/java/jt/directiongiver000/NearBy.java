package jt.directiongiver000;

public class NearBy
{
    private location coordinates;
    private String name;
    private double distance;
    private double position;
    private double rating;
    public NearBy(location coordinates,String name,double distance,double position,double rating)
    {
        this.coordinates = coordinates;
        this.name = name;
        this.distance = distance;
        this.position = position;
        this.rating = rating;

    }
    public location getCoordinates()
    {
        return coordinates;
    }
    public void setCoordinates(location coordinates)
    {
        this.coordinates = coordinates;
    }
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public double getDistance()
    {
        return distance;
    }
    public void setDistance(double distance)
    {
        this.distance = distance;
    }
    public String toString()
    {
        String Direction = GetDirection();
        return this.name + "[" +this.coordinates.X+","+this.coordinates.Y +"]\n與查詢點距離：" + this.distance + "公尺\n相對方位：" + Direction + "\n評價：" + this.rating + "\n";
    }
    public double getPosition()
    {
        return position;
    }
    public void setPosition(double position)
    {
        this.position = position;
    }
    public String GetDirection()
    {
        if ((this.position <= 10 ) || (this.position > 350)) return "右邊";
        if ((this.position > 10) && (this.position <= 80)) return "右前方";
        if ((this.position > 80) && (this.position <= 100)) return "前面";
        if ((this.position > 100) && (this.position <= 170)) return "左前方";
        if ((this.position > 170) && (this.position <= 190)) return "左邊";
        if ((this.position > 190) && (this.position <= 260)) return "左後方";
        if ((this.position > 260) && (this.position <= 280)) return "後方";
        if ((this.position > 280) && (this.position <= 350)) return "右後方";
        return null;
    }
}
