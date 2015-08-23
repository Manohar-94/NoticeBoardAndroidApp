package objects;

/*
Created by manohar on 4/2/15.
 */
public class NoticeObject {
    public int id;
    public String subject;
    public String datetime_modified;
    public String username;
    public String category;
    public String main_category;
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
}
