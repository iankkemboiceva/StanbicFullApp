package model;

/**
 * Created by deeru on 18-10-2016.
 */

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class GetServices {
    @SerializedName("responseCode")
    private String responseCode;
    @SerializedName("errors")
    private String errors;
    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private List<GetServicesData> results;

    public GetServices(String respcode, String errors, String message, List<GetServicesData> results) {
        this.responseCode = respcode;
        this.errors = errors;
        this.message = message;
        this.results = results;
    }


    public String getRespCode() {
        return responseCode;
    }

    public void setResponseCode(String accname) {
        this.responseCode = accname;
    }


    public String getErrors() {
        return errors;
    }

    public void setErrors(String accname) {
        this.errors = accname;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String accname) {
        this.message = accname;
    }


    public List<GetServicesData>  getResults() {
        return results;
    }

    public void setResults(List<GetServicesData> results) {
        this.results = results;
    }
}