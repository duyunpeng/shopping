package com.shopping.foundation.domain;

import com.shopping.core.domain.IdEntity;
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
//物业消息
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Table(name = "shopping_property_information")
public class PropertyInformation extends IdEntity{

    //用户id
    @OneToOne(cascade = CascadeType.ALL)
    private User userId;

    //物业名称
    @OneToOne(cascade = CascadeType.ALL)
    private PropertySheet informationName;

    //消息标题
    private String messageHeader;

    //物业公告内容
    private String propertyAnnouncementContent;

    //公告时间
    private Date informationTime;


    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public PropertySheet getInformationName() {
        return informationName;
    }

    public void setInformationName(PropertySheet informationName) {
        this.informationName = informationName;
    }

    public String getMessageHeader() {
        return messageHeader;
    }

    public void setMessageHeader(String messageHeader) {
        this.messageHeader = messageHeader;
    }

    public String getPropertyAnnouncementContent() {
        return propertyAnnouncementContent;
    }

    public void setPropertyAnnouncementContent(String propertyAnnouncementContent) {
        this.propertyAnnouncementContent = propertyAnnouncementContent;
    }

    public Date getInformationTime() {
        return informationTime;
    }

    public void setInformationTime(Date informationTime) {
        this.informationTime = informationTime;
    }
}
