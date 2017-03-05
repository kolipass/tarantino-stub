package mobi.tarantino.stub.auto.model.auth.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import mobi.tarantino.stub.auto.model.ApiResponse;

/**
 * {"error":"invalid_username","error_description":"Invalid username parameter value.",
 * "error_code":"201","user_message":"Этот неловкий момент, когда приложение недоступно =(
 * Попробуйте чуть позже."}
 */

public class ErrorMobiApiAnswer implements ApiResponse, Parcelable, Serializable {
    public static final Creator<ErrorMobiApiAnswer> CREATOR = new Creator<ErrorMobiApiAnswer>() {
        @Override
        public ErrorMobiApiAnswer createFromParcel(Parcel in) {
            return new ErrorMobiApiAnswer(in);
        }

        @Override
        public ErrorMobiApiAnswer[] newArray(int size) {
            return new ErrorMobiApiAnswer[size];
        }
    };
    @SerializedName("error")
    @Expose
    String error;
    @SerializedName("error_description")
    @Expose
    String errorDescription;
    @SerializedName(value = "errorCode")
    @Expose
    String errorCode;
    @SerializedName(value = "user_message", alternate = {"message"})
    @Expose
    String userMessage;

    public ErrorMobiApiAnswer() {
    }

    public ErrorMobiApiAnswer(String error, String errorCode, String errorDescription, String
            userMessage) {
        this.error = error;
        this.errorCode = errorCode;
        this.errorDescription = errorDescription;
        this.userMessage = userMessage;
    }

    protected ErrorMobiApiAnswer(Parcel in) {
        this.error = in.readString();
        this.errorDescription = in.readString();
        this.errorCode = in.readString();
        this.userMessage = in.readString();
    }

    public ErrorMobiApiAnswer(String message, String errorCode) {
        this.error = message;
        this.errorCode = errorCode;
    }

    public ErrorMobiApiAnswer(String message, int errorCode) {
        this(message, String.valueOf(errorCode));
    }

    public String getError() {
        return error;
    }

    public ErrorMobiApiAnswer setError(String error) {
        this.error = error;
        return this;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public ErrorMobiApiAnswer setErrorCode(String errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public ErrorMobiApiAnswer setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
        return this;
    }

    public String getUserMessage() {
        return userMessage;
    }

    public ErrorMobiApiAnswer setUserMessage(String userMessage) {
        this.userMessage = userMessage;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.error);
        dest.writeString(this.errorDescription);
        dest.writeString(this.errorCode);
        dest.writeString(this.userMessage);
    }

    public boolean isEmpty() {
        return this.error == null &&
                this.errorDescription == null &&
                this.errorCode == null &&
                this.userMessage == null;
    }

}
