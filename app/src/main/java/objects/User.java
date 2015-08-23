package objects;

/**
 * Created by manohar on 15/8/15.
 */
public class User {
    String name, info, enrollmentno;
    public User(String name,String info,String enrollmentno){
        this.name = name;
        this.info = info;
        this.enrollmentno = enrollmentno;
    }
    public String getName(){
        return name;
    }
    public String getInfo(){
        return info;
    }
    public String getEnrollmentno(){
        return enrollmentno;
    }
}
