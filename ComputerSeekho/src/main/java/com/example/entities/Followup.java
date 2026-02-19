package com.example.entities;
import java.io.Serializable;
import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "followup")
@Data
public class Followup implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "followup_id")
    private int followupId;

    @ManyToOne
    @JoinColumn(name = "enquiry_id", referencedColumnName = "enquiry_id")
    private Enquiry enquiry;

    @ManyToOne
    @JoinColumn(name = "staff_id", referencedColumnName = "staff_id")
    private Staff staff;

    @Column(name = "followup_date")
    private LocalDate followupDate;

    @Column(name = "followup_msg")
    private String followupMsg;

    @Column(name = "is_active")
    private boolean isActive;

    public void setFollowupId(int followup_id){
        this.followupId = followup_id;
    }

    public int getFollowupId(){
        return followupId;
    }

    public int getEnquiryId() {
        return enquiry.getEnquiryId();
    }

    public void setEnquiryId(int enquiry_id) {
        this.enquiry.setEnquiryId(enquiry_id);
    }

    public void setFollowupDate(LocalDate followup_date){
        this.followupDate = followup_date;
    }

    public LocalDate getFollowupDate(){
        return followupDate;
    }

    public void setFollowupMsg(String followup_msg){
        this.followupMsg = followup_msg;
    }

    public String getFollowupMsg(){
        return followupMsg;
    }

    public void setActive(boolean is_active){
        this.isActive = is_active;
    }

    public Boolean getActive(){
        return isActive;
    }   
}