package fun.hackathon.whereismyfun.ForAR;

public class FunCompany {
    private String mName;
    private double mLatitude ;
    private double mLongitude ;

    public FunCompany(String newName, double newLatitude, double newLongitude) {
        this. mName = newName;
        this. mLatitude = newLatitude;
        this. mLongitude = newLongitude;
    }

    public String getPoiName() {
        return mName;
    }
    public double getPoiLatitude() {
        return mLatitude;
    }
    public double getPoiLongitude() {
        return mLongitude;
    }
}
