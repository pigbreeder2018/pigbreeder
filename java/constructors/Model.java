package constructors;

/**
 * Created by ALAN on 7/6/2018.
 */

public class Model {
    public  String date,female,male;

    public Model() {

    }

    //none empty constructor

    public Model(String date, String female, String male) {
        this.date = date;
        this.female = female;
        this.male = male;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFemale() {
        return female;
    }

    public void setFemale(String female) {
        this.female = female;
    }

    public String getMale() {
        return male;
    }

    public void setMale(String male) {
        this.male = male;
    }
}
