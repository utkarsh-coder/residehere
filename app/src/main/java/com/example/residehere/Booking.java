package com.example.residehere;

import com.google.firebase.Timestamp;

public class Booking {

    private String bookingId;
    private String bookingStatus;
    private String roomName;
    private String roomId;
    private String pgId;
    private String customerNo;
    private String ownerNo;
    private String pgName;
    private Timestamp bookingTimestamp;
    private String endingTimestamp;
    private Timestamp createdAt;
    private String currentStatus;
    private Timestamp paidTill;
    private String residerName;
    private String residerContact;

    public Booking(String bookingId, String bookingStatus, String roomName, String roomId, String pgId, String customerNo, String ownerNo, String pgName, Timestamp bookingTimestamp, String endingTimestamp, Timestamp createdAt, String currentStatus, Timestamp paidTill, String residerName, String residerContact) {
        this.bookingId = bookingId;
        this.bookingStatus = bookingStatus;
        this.roomName = roomName;
        this.roomId = roomId;
        this.pgId = pgId;
        this.customerNo = customerNo;
        this.ownerNo = ownerNo;
        this.pgName = pgName;
        this.bookingTimestamp = bookingTimestamp;
        this.endingTimestamp = endingTimestamp;
        this.createdAt = createdAt;
        this.currentStatus = currentStatus;
        this.paidTill = paidTill;
        this.residerName = residerName;
        this.residerContact = residerContact;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getPgId() {
        return pgId;
    }

    public void setPgId(String pgId) {
        this.pgId = pgId;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public String getOwnerNo() {
        return ownerNo;
    }

    public void setOwnerNo(String ownerNo) {
        this.ownerNo = ownerNo;
    }

    public String getPgName() {
        return pgName;
    }

    public void setPgName(String pgName) {
        this.pgName = pgName;
    }

    public Timestamp getBookingTimestamp() {
        return bookingTimestamp;
    }

    public void setBookingTimestamp(Timestamp bookingTimestamp) {
        this.bookingTimestamp = bookingTimestamp;
    }

    public String getEndingTimestamp() {
        return endingTimestamp;
    }

    public void setEndingTimestamp(String endingTimestamp) {
        this.endingTimestamp = endingTimestamp;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public Timestamp getPaidTill() {
        return paidTill;
    }

    public void setPaidTill(Timestamp paidTill) {
        this.paidTill = paidTill;
    }

    public String getResiderName() {
        return residerName;
    }

    public void setResiderName(String residerName) {
        this.residerName = residerName;
    }

    public String getResiderContact() {
        return residerContact;
    }

    public void setResiderContact(String residerContact) {
        this.residerContact = residerContact;
    }
}
