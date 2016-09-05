package com.shopping.foundation.domain;

import com.shopping.core.domain.IdEntity;
import com.shopping.core.enums.PayStatus;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by dyp on 2016/8/26.
 */
//用户物业缴费记录
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Table(name = "shopping_property_record")
public class PropertyRecord extends IdEntity {

    //缴费时间
    private Date paymentTime;

    //缴费状态
    private PayStatus payStatus;

    //缴费详细
    private String detailedPayment;

    //创建时间
    private Date createTime;

    //总费用
    private double totalCost;

    //用户ID
    @OneToOne(cascade = CascadeType.ALL)
    private User userId;

    //用户账户
    private String account;

    public Date getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(Date paymentTime) {
        this.paymentTime = paymentTime;
    }

    public PayStatus getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(PayStatus payStatus) {
        this.payStatus = payStatus;
    }

    public String getDetailedPayment() {
        return detailedPayment;
    }

    public void setDetailedPayment(String detailedPayment) {
        this.detailedPayment = detailedPayment;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
