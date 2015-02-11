package objects_and_parsing;

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
}
