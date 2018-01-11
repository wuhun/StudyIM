package studyim.cn.edu.cafa.studyim.model;

import java.io.Serializable;
import java.util.List;

/**
 * ================================================
 * 作    者：悟魂(了解自己，感悟灵魂，做最好的自己)
 * 创建日期：2018/1/11 0011
 * 版    本：1.0
 * 描    述：群公告列表
 * 修订历史：
 * ================================================
 */
public class NoticeListModel implements Serializable {

    /**
     * result : [{"USERID":0,"TITLE":"现代风格过过多多多多多多多多","GROUPNOTICEID":62,"AUTHOR":"队反反复复付","CONTENT":"队反反复复付付付付付付付付付付付","RELEASETIME":{"date":5,"hours":0,"seconds":0,"month":0,"nanos":0,"timezoneOffset":-480,"year":118,"minutes":0,"time":1515081600000,"day":5}},{"USERID":0,"TITLE":"放大到多多多多","GROUPNOTICEID":63,"AUTHOR":"哒哒哒","CONTENT":"队反反复复付付付付付付付付付付付付","RELEASETIME":{"date":4,"hours":0,"seconds":0,"month":0,"nanos":0,"timezoneOffset":-480,"year":118,"minutes":0,"time":1514995200000,"day":4}}]
     * msg :
     * code : 1
     */
    private String msg;
    private int code;
    private List<ResultBean> result;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * USERID : 0
         * TITLE : 现代风格过过多多多多多多多多
         * GROUPNOTICEID : 62
         * AUTHOR : 队反反复复付
         * CONTENT : 队反反复复付付付付付付付付付付付
         * RELEASETIME : {"date":5,"hours":0,"seconds":0,"month":0,"nanos":0,"timezoneOffset":-480,"year":118,"minutes":0,"time":1515081600000,"day":5}
         */

        private int USERID;
        private String TITLE;
        private int GROUPNOTICEID;
        private String AUTHOR;
        private String CONTENT;
        private RELEASETIMEBean RELEASETIME;

        public int getUSERID() {
            return USERID;
        }

        public void setUSERID(int USERID) {
            this.USERID = USERID;
        }

        public String getTITLE() {
            return TITLE;
        }

        public void setTITLE(String TITLE) {
            this.TITLE = TITLE;
        }

        public int getGROUPNOTICEID() {
            return GROUPNOTICEID;
        }

        public void setGROUPNOTICEID(int GROUPNOTICEID) {
            this.GROUPNOTICEID = GROUPNOTICEID;
        }

        public String getAUTHOR() {
            return AUTHOR;
        }

        public void setAUTHOR(String AUTHOR) {
            this.AUTHOR = AUTHOR;
        }

        public String getCONTENT() {
            return CONTENT;
        }

        public void setCONTENT(String CONTENT) {
            this.CONTENT = CONTENT;
        }

        public RELEASETIMEBean getRELEASETIME() {
            return RELEASETIME;
        }

        public void setRELEASETIME(RELEASETIMEBean RELEASETIME) {
            this.RELEASETIME = RELEASETIME;
        }

        public static class RELEASETIMEBean {
            /**
             * date : 5
             * hours : 0
             * seconds : 0
             * month : 0
             * nanos : 0
             * timezoneOffset : -480
             * year : 118
             * minutes : 0
             * time : 1515081600000
             * day : 5
             */

            private int date;
            private int hours;
            private int seconds;
            private int month;
            private int nanos;
            private int timezoneOffset;
            private int year;
            private int minutes;
            private long time;
            private int day;

            public int getDate() {
                return date;
            }

            public void setDate(int date) {
                this.date = date;
            }

            public int getHours() {
                return hours;
            }

            public void setHours(int hours) {
                this.hours = hours;
            }

            public int getSeconds() {
                return seconds;
            }

            public void setSeconds(int seconds) {
                this.seconds = seconds;
            }

            public int getMonth() {
                return month;
            }

            public void setMonth(int month) {
                this.month = month;
            }

            public int getNanos() {
                return nanos;
            }

            public void setNanos(int nanos) {
                this.nanos = nanos;
            }

            public int getTimezoneOffset() {
                return timezoneOffset;
            }

            public void setTimezoneOffset(int timezoneOffset) {
                this.timezoneOffset = timezoneOffset;
            }

            public int getYear() {
                return year;
            }

            public void setYear(int year) {
                this.year = year;
            }

            public int getMinutes() {
                return minutes;
            }

            public void setMinutes(int minutes) {
                this.minutes = minutes;
            }

            public long getTime() {
                return time;
            }

            public void setTime(long time) {
                this.time = time;
            }

            public int getDay() {
                return day;
            }

            public void setDay(int day) {
                this.day = day;
            }

            @Override
            public String toString() {
                return "RELEASETIMEBean{" +
                        "date=" + date +
                        ", hours=" + hours +
                        ", seconds=" + seconds +
                        ", month=" + month +
                        ", nanos=" + nanos +
                        ", timezoneOffset=" + timezoneOffset +
                        ", year=" + year +
                        ", minutes=" + minutes +
                        ", time=" + time +
                        ", day=" + day +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "ResultBean{" +
                    "USERID=" + USERID +
                    ", TITLE='" + TITLE + '\'' +
                    ", GROUPNOTICEID=" + GROUPNOTICEID +
                    ", AUTHOR='" + AUTHOR + '\'' +
                    ", CONTENT='" + CONTENT + '\'' +
                    ", RELEASETIME=" + RELEASETIME +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "NoticeListModel{" +
                "msg='" + msg + '\'' +
                ", code=" + code +
                ", result=" + result +
                '}';
    }
}
