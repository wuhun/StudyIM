package studyim.cn.edu.cafa.studyim.model;

import java.io.Serializable;

/**
 * ================================================
 * 作    者：悟魂(了解自己，感悟灵魂，做最好的自己)
 * 创建日期：2018/1/19 0019
 * 版    本：1.0
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class GroupFile implements Serializable {

    /**
     * FILESIZE : 1641
     * STS : A
     * FILETIME : 1515393655000
     * FILETYPE : docx
     * CLASS : photo
     * FILEID : 299
     * IOS_GROUP_FILE_ID : 299
     * FILENAME : app端bug2017.12.17---.docx
     * THUMBNAILPATH : uploadFiles/uploadImgs//IOS20180104/thumb_41681808431b4a27a0b6382fabb85347.png
     */

    private int FILESIZE;//文件大小
    private String STS;
    private long FILETIME; //文件上传时间
    private String FILEPATH; //文件地址
    private String FILETYPE; //文件类型 doc，pdf，zip等文档
    private String CLASS; //文件类型
    private int FILEID; //文件id
    private int IOS_GROUP_FILE_ID;
    private String FILENAME; //文件名
    private String THUMBNAILPATH; //缩略图地址

    public int getFILESIZE() {
        return FILESIZE;
    }

    public void setFILESIZE(int FILESIZE) {
        this.FILESIZE = FILESIZE;
    }

    public String getSTS() {
        return STS;
    }

    public void setSTS(String STS) {
        this.STS = STS;
    }

    public long getFILETIME() {
        return FILETIME;
    }

    public void setFILETIME(long FILETIME) {
        this.FILETIME = FILETIME;
    }

    public String getFILEPATH() {
        return FILEPATH;
    }

    public void setFILEPATH(String FILEPATH) {
        this.FILEPATH = FILEPATH;
    }

    public String getFILETYPE() {
        return FILETYPE;
    }

    public void setFILETYPE(String FILETYPE) {
        this.FILETYPE = FILETYPE;
    }

    public String getCLASS() {
        return CLASS;
    }

    public void setCLASS(String CLASS) {
        this.CLASS = CLASS;
    }

    public int getFILEID() {
        return FILEID;
    }

    public void setFILEID(int FILEID) {
        this.FILEID = FILEID;
    }

    public int getIOS_GROUP_FILE_ID() {
        return IOS_GROUP_FILE_ID;
    }

    public void setIOS_GROUP_FILE_ID(int IOS_GROUP_FILE_ID) {
        this.IOS_GROUP_FILE_ID = IOS_GROUP_FILE_ID;
    }

    public String getFILENAME() {
        return FILENAME;
    }

    public void setFILENAME(String FILENAME) {
        this.FILENAME = FILENAME;
    }

    public String getTHUMBNAILPATH() {
        return THUMBNAILPATH;
    }

    public void setTHUMBNAILPATH(String THUMBNAILPATH) {
        this.THUMBNAILPATH = THUMBNAILPATH;
    }

    @Override
    public String toString() {
        return "GroupFile{" +
                "FILESIZE=" + FILESIZE +
                ", STS='" + STS + '\'' +
                ", FILETIME=" + FILETIME +
                ", FILEPATH='" + FILEPATH + '\'' +
                ", FILETYPE='" + FILETYPE + '\'' +
                ", CLASS='" + CLASS + '\'' +
                ", FILEID=" + FILEID +
                ", IOS_GROUP_FILE_ID=" + IOS_GROUP_FILE_ID +
                ", FILENAME='" + FILENAME + '\'' +
                ", THUMBNAILPATH='" + THUMBNAILPATH + '\'' +
                '}';
    }
}
