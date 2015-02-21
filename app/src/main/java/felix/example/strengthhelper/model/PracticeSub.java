package felix.example.strengthhelper.model;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class PracticeSub extends BaseModel implements Serializable {
    private int mNum;
    private UUID mId;
    private Date mDate;



    public PracticeSub() {
        mId = UUID.randomUUID();

    }


    public int getNum() {
        return mNum;
    }

    public void setNum(int num) {
        mNum = num;
    }

    public UUID getId() {
        return mId;
    }

    public void setId(UUID id) {
        mId = id;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

}
