package POJO;

import java.sql.Time;

/**
 * Created by Vanessa on 22/04/2018.
 */

public class Units {

//    id, name, start, end, hours
    String unitName;
    int unitID;
    long unitStart;
    long unitEnd;
    Time unitHours;

    public Units(String unitName, int unitID, long unitStart, long unitEnd, Time unitHours) {
        this.unitName = unitName;
        this.unitID = unitID;
        this.unitStart = unitStart;
        this.unitEnd = unitEnd;
        this.unitHours = unitHours;
    }

    public Units() {
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public int getUnitID() {
        return unitID;
    }

    public void setUnitID(int unitID) {
        this.unitID = unitID;
    }

    public long getUnitStart() {
        return unitStart;
    }

    public void setUnitStart(long unitStart) {
        this.unitStart = unitStart;
    }

    public long getUnitEnd() {
        return unitEnd;
    }

    public void setUnitEnd(long unitEnd) {
        this.unitEnd = unitEnd;
    }

    public Time getUnitHours() {
        return unitHours;
    }

    public void setUnitHours(Time unitHours) {
        this.unitHours = unitHours;
    }
}
