package fun.hackathon.whereismyfun.Retrofit;

import java.util.List;

public class Sales {
    public String status;
    public List<Sale> listRows;

    public class Sale {
        public String describtion;
        public double lat;
        public double lon;
        public String titleComp;
    }
}
