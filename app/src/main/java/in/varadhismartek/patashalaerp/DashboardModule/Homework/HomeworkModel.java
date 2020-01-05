package in.varadhismartek.patashalaerp.DashboardModule.Homework;

public class HomeworkModel {


    private String homeworkId, homeworkTitle, description, homeClass, section, subject, startDate, endDate;
    private String bookAuthor="", bookCover="", bookName="", bookPageNo="";


    private String divisionClassName;
    private String noOfHomework;
    private boolean cheked;
    private String strAttachment;

    private String urlReference="", urlDescription="";

    public HomeworkModel(String urlReference, String urlDescription) {
        this.urlReference = urlReference;
        this.urlDescription = urlDescription;
    }

    public HomeworkModel(String divisionClassName, String noOfHomework, boolean cheked) {
        this.divisionClassName = divisionClassName;
        this.noOfHomework = noOfHomework;
        this.cheked = cheked;
    }

    public HomeworkModel(String homeworkId, String homeworkTitle, String description, String homeClass, String section, String subject, String startDate, String endDate) {
        this.homeworkId = homeworkId;
        this.homeworkTitle = homeworkTitle;
        this.description = description;
        this.homeClass = homeClass;
        this.section = section;
        this.subject = subject;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public HomeworkModel(String bookAuthor, String bookCover, String bookName, String bookPageNo) {
        this.bookAuthor = bookAuthor;
        this.bookCover = bookCover;
        this.bookName = bookName;
        this.bookPageNo = bookPageNo;
    }

    public HomeworkModel(String strAttachment) {
        this.strAttachment= strAttachment;
    }

    public String getStrAttachment() {
        return strAttachment;
    }

    public void setStrAttachment(String strAttachment) {
        this.strAttachment = strAttachment;
    }

    public String getUrlReference() {
        return urlReference;
    }

    public void setUrlReference(String urlReference) {
        this.urlReference = urlReference;
    }

    public String getUrlDescription() {
        return urlDescription;
    }

    public void setUrlDescription(String urlDescription) {
        this.urlDescription = urlDescription;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public String getBookCover() {
        return bookCover;
    }

    public void setBookCover(String bookCover) {
        this.bookCover = bookCover;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookPageNo() {
        return bookPageNo;
    }

    public void setBookPageNo(String bookPageNo) {
        this.bookPageNo = bookPageNo;
    }

    public String getDivisionClassName() {
        return divisionClassName;
    }

    public void setDivisionClassName(String divisionClassName) {
        this.divisionClassName = divisionClassName;
    }

    public String getNoOfHomework() {
        return noOfHomework;
    }

    public void setNoOfHomework(String noOfHomework) {
        this.noOfHomework = noOfHomework;
    }

    public boolean isCheked() {
        return cheked;
    }

    public void setCheked(boolean cheked) {
        this.cheked = cheked;
    }

    public String getHomeworkId() {
        return homeworkId;
    }

    public void setHomeworkId(String homeworkId) {
        this.homeworkId = homeworkId;
    }

    public String getHomeworkTitle() {
        return homeworkTitle;
    }

    public void setHomeworkTitle(String homeworkTitle) {
        this.homeworkTitle = homeworkTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHomeClass() {
        return homeClass;
    }

    public void setHomeClass(String homeClass) {
        this.homeClass = homeClass;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
