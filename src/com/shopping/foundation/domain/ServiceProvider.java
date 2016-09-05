package com.shopping.foundation.domain;

import com.shopping.core.domain.IdEntity;
import com.shopping.core.enums.DeleteStatus;
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
//维修商
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Table(name = "shopping_service_provider")
public class ServiceProvider extends IdEntity{

    //用户
    @OneToOne(cascade = CascadeType.ALL)
    private User userId;

    //小区ID
    @OneToOne(cascade = CascadeType.ALL)
    private Area areaId;

    //维修商名称
    private String serviceName;

    //维修内容
    private String serviceContent;

    //创建时间
    private Date createTime;

    //是否删除
    private DeleteStatus serviceProviderStatus;

    //删除账户
    private String deleteAccount;

    //删除时间
    private Date deleteTime;

    //创建账户
    private String createAccount;


    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public Area getAreaId() {
        return areaId;
    }

    public void setAreaId(Area areaId) {
        this.areaId = areaId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceContent() {
        return serviceContent;
    }

    public void setServiceContent(String serviceContent) {
        this.serviceContent = serviceContent;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public DeleteStatus getServiceProviderStatus() {
        return serviceProviderStatus;
    }

    public void setServiceProviderStatus(DeleteStatus serviceProviderStatus) {
        this.serviceProviderStatus = serviceProviderStatus;
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

    public String getCreateAccount() {
        return createAccount;
    }

    public void setCreateAccount(String createAccount) {
        this.createAccount = createAccount;
    }
}
