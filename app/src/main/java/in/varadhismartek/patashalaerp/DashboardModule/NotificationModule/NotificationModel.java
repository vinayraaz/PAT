package in.varadhismartek.patashalaerp.DashboardModule.NotificationModule;

import java.io.Serializable;
import java.util.ArrayList;

public class NotificationModel implements Serializable {

    private String name;
    private int image;
    private String parentName, childName, id, mobile;

    private String message, lastName,notificationID, sendTo, firstName;
    private String title, addedDate, addedTime, attachment, sendDate,sendTime,approverStatus;


    boolean status;

    public NotificationModel(int image, String name, String parentName, String childName, String id, String mobile) {
        this.image = image;
        this.name = name;
        this.parentName = parentName;
        this.childName = childName;
        this.id = id;
        this.mobile = mobile;
    }


    private String className;
    private ArrayList<String> sectionName;

    public NotificationModel(String className, ArrayList<String> sectionName) {
        this.className = className;
        this.sectionName = sectionName;
    }

    public NotificationModel(boolean status, String notificationID, String title, String message, String addedDate,
                             String addedTime, String attachment, String sendTo, String sendDate, String sendTime, String approverStatus,
                             String firstName, String lastName){

        this.status = status;
        this.notificationID = notificationID;
        this.title = title;
        this.message = message;
        this.addedDate = addedDate;
        this.addedTime = addedTime;
        this.attachment = attachment;
        this.sendTo = sendTo;
        this.sendDate = sendDate;
        this.sendTime = sendTime;
        this.approverStatus = approverStatus;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public ArrayList<String> getSectionName() {
        return sectionName;
    }

    public String getClassName() {
        return className;
    }

    public String getName() {
        return name;
    }

    public int getImage() {
        return image;
    }

    public String getParentName() {
        return parentName;
    }

    public String getChildName() {
        return childName;
    }

    public String getId() {
        return id;
    }

    public String getMobile() {
        return mobile;
    }

    public String getMessage() {
        return message;
    }

    public String getLastName() {
        return lastName;
    }

    public String getNotificationID() {
        return notificationID;
    }

    public String getSendTo() {
        return sendTo;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getTitle() {
        return title;
    }

    public String getAddedDate() {
        return addedDate;
    }

    public String getAddedTime() {
        return addedTime;
    }

    public String getAttachment() {
        return attachment;
    }

    public String getSendDate() {
        return sendDate;
    }

    public String getSendTime() {
        return sendTime;
    }

    public String getApproverStatus() {
        return approverStatus;
    }

    public boolean isStatus() {
        return status;
    }
}
