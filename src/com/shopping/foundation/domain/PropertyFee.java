package com.shopping.foundation.domain;

import com.shopping.core.domain.IdEntity;
import com.shopping.core.enums.ConfirmStatus;
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
//物业费
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Table(name = "shopping_property_fee")
public class PropertyFee extends IdEntity{

    //用户ID
    @OneToOne(cascade = CascadeType.ALL)
    private User userId;

    //缴费状态
    private PayStatus payStatus;

    //创建时间
    private Date createTime;

    //创建账户
    private String account;

    //创建人
    private String createPerson;

    //总费用
    private double totalCost;

    //已交费用
    private double paidFee;

    //是否确认
    private ConfirmStatus confirmStatus;

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public PayStatus getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(PayStatus payStatus) {
        this.payStatus = payStatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getCreatePerson() {
        return createPerson;
    }

    public void setCreatePerson(String createPerson) {
        this.createPerson = createPerson;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public double getPaidFee() {
        return paidFee;
    }

    public void setPaidFee(double paidFee) {
        this.paidFee = paidFee;
    }

    public ConfirmStatus getConfirmStatus() {
        return confirmStatus;
    }

    public void setConfirmStatus(ConfirmStatus confirmStatus) {
        this.confirmStatus = confirmStatus;
    }
}
