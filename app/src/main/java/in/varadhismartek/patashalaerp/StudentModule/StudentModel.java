package in.varadhismartek.patashalaerp.StudentModule;

public class StudentModel {
    private String strDivision,strClass,strSection,strFirstName,strLastName,strDOB,strStudentID,strCertificateNo,
            strStatus,strdeleted,strPhoto;

    public StudentModel(String strDivision, String strClass, String strSection, String strFirstName, String strLastName, String strDOB, String strStudentID, String strCertificateNo, String strStatus, String strdeleted, String strPhoto) {
        this.strDivision = strDivision;
        this.strClass = strClass;
        this.strSection = strSection;
        this.strFirstName = strFirstName;
        this.strLastName = strLastName;
        this.strDOB = strDOB;
        this.strStudentID = strStudentID;
        this.strCertificateNo = strCertificateNo;
        this.strStatus = strStatus;
        this.strdeleted = strdeleted;
        this.strPhoto = strPhoto;
    }

    public String getStrDivision() {
        return strDivision;
    }

    public void setStrDivision(String strDivision) {
        this.strDivision = strDivision;
    }

    public String getStrClass() {
        return strClass;
    }

    public void setStrClass(String strClass) {
        this.strClass = strClass;
    }

    public String getStrSection() {
        return strSection;
    }

    public void setStrSection(String strSection) {
        this.strSection = strSection;
    }

    public String getStrFirstName() {
        return strFirstName;
    }

    public void setStrFirstName(String strFirstName) {
        this.strFirstName = strFirstName;
    }

    public String getStrLastName() {
        return strLastName;
    }

    public void setStrLastName(String strLastName) {
        this.strLastName = strLastName;
    }

    public String getStrDOB() {
        return strDOB;
    }

    public void setStrDOB(String strDOB) {
        this.strDOB = strDOB;
    }

    public String getStrStudentID() {
        return strStudentID;
    }

    public void setStrStudentID(String strStudentID) {
        this.strStudentID = strStudentID;
    }

    public String getStrCertificateNo() {
        return strCertificateNo;
    }

    public void setStrCertificateNo(String strCertificateNo) {
        this.strCertificateNo = strCertificateNo;
    }

    public String getStrStatus() {
        return strStatus;
    }

    public void setStrStatus(String strStatus) {
        this.strStatus = strStatus;
    }

    public String getStrdeleted() {
        return strdeleted;
    }

    public void setStrdeleted(String strdeleted) {
        this.strdeleted = strdeleted;
    }

    public String getStrPhoto() {
        return strPhoto;
    }

    public void setStrPhoto(String strPhoto) {
        this.strPhoto = strPhoto;
    }
}
