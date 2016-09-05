package com.shopping.foundation.domain;

import com.shopping.core.domain.IdEntity;
import com.shopping.core.enums.DealStatus;
import com.shopping.core.enums.DeleteStatus;
import com.shopping.core.enums.RepairStatus;
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
//物业维修
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Table(name = "shopping_property_maintenance")
public class PropertyMaintenance extends IdEntity{

    //用户
    @OneToOne(cascade = CascadeType.ALL)
    private User userId;

    //维修类别
    private RepairStatus repairStatus;

    //标题
    private String title;

    //创建时间
    private Date createTime;

    //处理时间
    private Date dealTime;

    //处理状态
    private DealStatus dealStatus;

    //是否删除
    private DeleteStatus maintenanceStatus;

    //创建账户
    private String createAccount;

    //删除账户
    private String deleteAccount;

    //删除时间
    private Date deleteTime;


    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public RepairStatus getRepairStatus() {
        return repairStatus;
    }

    public void setRepairStatus(RepairStatus repairStatus) {
        this.repairStatus = repairStatus;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getDealTime() {
        return dealTime;
    }

    public void setDealTime(Date dealTime) {
        this.dealTime = dealTime;
    }

    public DealStatus getDealStatus() {
        return dealStatus;
    }

    public void setDealStatus(DealStatus dealStatus) {
        this.dealStatus = dealStatus;
    }

    public DeleteStatus getMaintenanceStatus() {
        return maintenanceStatus;
    }

    public void setMaintenanceStatus(DeleteStatus maintenanceStatus) {
        this.maintenanceStatus = maintenanceStatus;
    }

    public String getCreateAccount() {
        return createAccount;
    }

    public void setCreateAccount(String createAccount) {
        this.createAccount = createAccount;
    }

    public String getDeleteAccount() {
        return deleteAccount;
    }

    public void setDeleteAccount(String deleteAccount) {
        this.deleteAccount = deleteAccount;
    }

    public Date getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(Date deleteTime) {
        this.deleteTime = deleteTime;
    }
}
