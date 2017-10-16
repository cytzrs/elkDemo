package com.elastic.stack.demo.elkDemo.sms;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import java.util.List;

/**
 * Created by liyang on 2017/10/12.
 */
public class ECBody {

    /**
     * ECOrderId": "104343399812",
     " +
     "    \"OrderList\": [\n" +
     "        {\n" +
     "            \"PID\": \"10021\",\n" +
     "            \"Phones\": \"18621077935\"\n" +
     "        }\n" +
     "    ]\n" +
     * */

    private String ECOrderId;

    //@JsonUnwrapped
    private ECOrder OrderList;

    public String getECOrderId() {
        return this.ECOrderId;
    }

    public void setECOrderId(String ECOrderId) {
        this.ECOrderId = ECOrderId;
    }

    public ECOrder getOrderList() {
        return this.OrderList;
    }

    public void setOrderList(ECOrder OrderList) {
        this.OrderList = OrderList;
    }
}

class ECOrder {

    private String PID;

    private List<String> Phones;

    public String getPID() {
        return this.PID;
    }

    public void setPID(String PID) {
        this.PID = PID;
    }

    public List<String> getPhones() {
        return this.Phones;
    }

    public void setPhones(List<String> Phones) {
        this.Phones = Phones;
    }
}
