package objects;

/*
Created by manohar on 5/2/15.
 */
public class NoticeInfo {
    public int id;
    public String reference;
    public String subject;
    public String username;
    public String category;
    public String content;
    public String datetime_modified;
    public NoticeInfo(){};
    public NoticeInfo(int id, String reference, String subject, String username, String category,
                      String content, String datetime_modified){
        this.id=id;
        this.reference=reference;
        this.subject=subject;
        this.username=username;
        this.category=category;
        this.content=content;
        this.datetime_modified=datetime_modified;
    }
    public String getSubject(){return subject;}
    public String getCategory(){return category;}
    public String getDatetime_modified(){return datetime_modified;}
    public String getContent(){return content;}
}
