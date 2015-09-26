package objects;

/*
Created by manohar on 4/2/15.
 */
public class NoticeObject {
    int id;
    String subject;
    String datetime_modified;
    String username;
    String category;
    String main_category;
    public NoticeObject(){};
    public NoticeObject(int id,String subject, String datetime_modified,String username,
                        String category, String main_category){
        this.id=id;
        this.subject=subject;
        this.datetime_modified=datetime_modified;
        this.username=username;
        this.category=category;
        this.main_category=main_category;
    }

    public String getSubject(){return this.subject;}
    public int getId(){return this.id;}
    public String getCategory(){return this.category;}
    public String getDatetime_modified(){return this.datetime_modified;}

    public void setId(int id){this.id = id;}
    public void setSubject(String subject){this.subject = subject;}
    public void setDatetime_modified(String datetime_modified){this.datetime_modified = datetime_modified;}
    public void setUsername(String username){this.username = username;}
    public void setCategory(String category){this.category = category;}
    public void setMain_category(String main_category){this.main_category = main_category;}
}
