package com.elastic.stack.demo.elkDemo.sms;

/**
 * Created by liyang on 2017/10/12.
 */
public class ECHead {
    /*
    * "BCode":"EMI10001",
" +
				"    \"Ack\":\"1\",\n" +
				"    \"SqId\":\"ec2017101217241110001515\",\n" +
				"    \"ECID\":\"c00280\",\n" +
				"    "Cnxt\
    * */

    private String BCode;

    private String Ack;

    private String SqId;

    private String ECID;

    private String Cnxt;

    public String getBCode() {
        return this.BCode;
    }

    public void setBCode(String BCode) {
        this.BCode = BCode;
    }

    public String getAck() {
        return this.Ack;
    }

    public void setAck(String Ack) {
        this.Ack = Ack;
    }

    public String getSqId() {
        return this.SqId;
    }

    public void setSqId(String SqId) {
        this.SqId = SqId;
    }

    public String getECID() {
        return this.ECID;
    }

    public void setECID(String ECID) {
        this.ECID = ECID;
    }

    public String getCnxt() {
        return this.Cnxt;
    }

    public void setCnxt(String Cnxt) {
        this.Cnxt = Cnxt;
    }
}
