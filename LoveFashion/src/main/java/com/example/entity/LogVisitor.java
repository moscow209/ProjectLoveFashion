package com.example.entity;
// Generated Dec 19, 2015 11:20:12 PM by Hibernate Tools 4.3.1

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * LogVisitor generated by hbm2java
 */
@Entity
@Table(name = "log_visitor", catalog = "lovefashion")
public class LogVisitor implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long visitorId;
	private int customerId;
	private String sessionId;
	private Date lastVisitAt;

	public LogVisitor() {
	}

	public LogVisitor(int customerId, Date lastVisitAt) {
		this.customerId = customerId;
		this.lastVisitAt = lastVisitAt;
	}

	public LogVisitor(int customerId, String sessionId, Date lastVisitAt) {
		this.customerId = customerId;
		this.sessionId = sessionId;
		this.lastVisitAt = lastVisitAt;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "visitor_id", unique = true, nullable = false)
	public Long getVisitorId() {
		return this.visitorId;
	}

	public void setVisitorId(Long visitorId) {
		this.visitorId = visitorId;
	}

	@Column(name = "customer_id", nullable = false)
	public int getCustomerId() {
		return this.customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	@Column(name = "session_id", length = 64)
	public String getSessionId() {
		return this.sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "last_visit_at", nullable = false, length = 19)
	public Date getLastVisitAt() {
		return this.lastVisitAt;
	}

	public void setLastVisitAt(Date lastVisitAt) {
		this.lastVisitAt = lastVisitAt;
	}

}