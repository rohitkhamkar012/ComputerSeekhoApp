package com.example.entities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "closure_reason")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClosureReason {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "closure_reason_id")
    private int closureReasonId;

    @Column(name = "closure_reason_desc")
    private String closureReasonDesc;

    @Column(name = "enquirer_name")
    private String enquirerName;

    public int getClosureReasonId() {
		return closureReasonId;
	}

	public void setClosureReasonId(int closureReasonId) {
		this.closureReasonId = closureReasonId;
	}

	public String getClosureReasonDesc() {
		return closureReasonDesc;
	}

	public void setClosureReasonDesc(String closureReasonDesc) {
		this.closureReasonDesc = closureReasonDesc;
	}

	public String getEnquirerName() {
		return enquirerName;
	}

	public void setEnquirerName(String enquirerName) {
		this.enquirerName = enquirerName;
	}

	public ClosureReason(String closureReasonDesc,String enquirerName){
        this.closureReasonDesc =closureReasonDesc;
        this.enquirerName = enquirerName;
    }
}