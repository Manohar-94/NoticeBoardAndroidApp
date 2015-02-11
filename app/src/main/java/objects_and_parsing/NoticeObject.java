package objects_and_parsing;

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

}
