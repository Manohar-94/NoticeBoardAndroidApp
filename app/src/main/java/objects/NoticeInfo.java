package objects;

/*
Created by manohar on 5/2/15.
 */
public class NoticeInfo {
    int id;
    String reference;
    String subject;
    String username;
    String category;
    String content;
    String datetime_modified;
    public NoticeInfo(){
        this.subject="No Notices Available!";
        this.category="";
        this.datetime_modified="T";
    };
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

    public void setId(int id){this.id = id;}
    public void setReference(String reference){this.reference = reference;}
    public void setSubject(String subject){this.subject = subject;}
    public void setUsername(String username){this.username = username;}
    public void setCategory(String category){this.category = category;}
    public void setContent(String content){this.content = content;}
    public void setDatetime_modified(String datetime_modified){this.datetime_modified = datetime_modified;}
}
