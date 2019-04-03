package model;

/**
 * Created by deeru on 18-10-2016.
 */

import com.google.gson.annotations.SerializedName;


public class GetCommPerfData {

    @SerializedName("txnCode")
    private String txnCode;
    @SerializedName("txndateTime")
    private String txndateTime;
    @SerializedName("agentCmsn")
    private double agentCmsn;
    @SerializedName("amount")
    private String amount;
    @SerializedName("status")
    private String status;
    @SerializedName("toAcNum")
    private String toAcNum;
    @SerializedName("refNumber")
    private String refNumber;
    @SerializedName("fromAccountNum")
    private String fromAccountNum;
    public GetCommPerfData(String txnCode, String txndateTime, double agentCmsn, String status,String amount,String toAcNum,String refNumber,String fromacnum) {
        this.txnCode = txnCode;
        this.txndateTime = txndateTime;
        this.agentCmsn = agentCmsn;
        this.status = status;
        this.amount = amount;
        this.toAcNum = toAcNum;
        this.refNumber = refNumber;
        this.fromAccountNum = fromacnum;
    }


    public String getTxnCode() {
        return txnCode;
    }

    public void SetTxnCode(String accname) {
        this.txnCode = accname;
    }

    public String getAmount() {
        return amount;
    }

    public void SetAmount(String accname) {
        this.amount = accname;
    }

    public String getTxndateTime() {
        return txndateTime;
    }

    public void setTxndateTime(String accnum) {
        this.txndateTime = accnum;
    }

    public double getAgentCmsn() {
        return agentCmsn;
    }

    public void setAgentCmsn(double accname) {
        this.agentCmsn = accname;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String accname) {
        this.status = accname;
    }



    public String gettoAcNum() {
        return toAcNum;
    }

    public void settoAcNum(String toAcNum) {
        this.toAcNum = toAcNum;
    }

    public String getrefNumber() {
        return refNumber;
    }

    public void setrefNumber(String refNumber) {
        this.refNumber = refNumber;
    }

    public String getFromAcnum() {
        return fromAccountNum;
    }

    public void setFromAcnum(String fromAccountNum) {
        this.fromAccountNum = fromAccountNum;
    }
}