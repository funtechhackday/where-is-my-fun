package fun.hackathon.whereismyfun.Retrofit;

import java.util.List;

public class Company {
    public String status;
    public List<Comp> listRows;

    public class Comp {
        public String addr;
        public String desc;
        public double lat;
        public double lon;
        public String title;
    }
}
