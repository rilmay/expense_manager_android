package com.guzov.expensemanagercompat.entity;

import java.util.Objects;

public class Sms{
    private String _id;
    private String _address;
    private String _msg;
    private String _readState; //"0" for have not read sms and "1" for have read sms
    private String _time;
    private String _folderName;

    public String getId(){
        return _id;
    }
    public String getAddress(){
        return _address;
    }
    public String getMsg(){
        return _msg;
    }
    public String getReadState(){
        return _readState;
    }
    public String getTime(){
        return _time;
    }
    public String getFolderName(){
        return _folderName;
    }


    public void setId(String id){
        _id = id;
    }
    public void setAddress(String address){
        _address = address;
    }
    public void setMsg(String msg){
        _msg = msg;
    }
    public void setReadState(String readState){
        _readState = readState;
    }
    public void setTime(String time){
        _time = time;
    }
    public void setFolderName(String folderName){
        _folderName = folderName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sms sms = (Sms) o;
        return Objects.equals(_id, sms._id) &&
                Objects.equals(_address, sms._address) &&
                Objects.equals(_msg, sms._msg) &&
                Objects.equals(_readState, sms._readState) &&
                Objects.equals(_time, sms._time) &&
                Objects.equals(_folderName, sms._folderName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id, _address, _msg, _readState, _time, _folderName);
    }

    @Override
    public String toString() {
        return "Sms{" +
                "_id='" + _id + '\'' +
                ", _address='" + _address + '\'' +
                ", _msg='" + _msg + '\'' +
                ", _readState='" + _readState + '\'' +
                ", _time='" + _time + '\'' +
                ", _folderName='" + _folderName + '\'' +
                '}';
    }
}
