package POJO;

import java.sql.Date;
import java.sql.Time;

/**
 * Created by Vanessa on 22/04/2018.
 */

public class Students {

    String student_class_email, student_class_name;
    Time student_class_period;
    Date student_class_date;
    int percentage;

    public Students() {
    }

    public Students(String student_class_email, String student_class_name, Time student_class_period, Date student_class_date, int percentage) {
        this.student_class_email = student_class_email;
        this.student_class_name = student_class_name;
        this.student_class_period = student_class_period;
        this.student_class_date = student_class_date;
        this.percentage = percentage;
    }

    public String getStudent_class_email() {
        return student_class_email;
    }

    public void setStudent_class_email(String student_class_email) {
        this.student_class_email = student_class_email;

    }

    public Time getStudent_class_period() {
        return student_class_period;
    }

    public void setStudent_class_period(Time student_class_period) {
        this.student_class_period = student_class_period;
    }

    public String getStudent_class_name() {
        return student_class_name;
    }

    public void setStudent_class_name(String student_class_name) {
        this.student_class_name = student_class_name;
    }

    public Date getStudent_class_date() {
        return student_class_date;
    }

    public void setStudent_class_date(Date student_class_date) {
        this.student_class_date = student_class_date;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }
}
