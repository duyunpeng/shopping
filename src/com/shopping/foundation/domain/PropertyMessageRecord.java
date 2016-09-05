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
//物业短消息发送记录
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Table(name = "shopping_property_message_record")
public class PropertyMessageRecord extends IdEntity {

    //接受用户
    @OneToOne(cascade = CascadeType.ALL)
    private User account;

    //发送时间
    private Date sendTime;

    public User getAccount() {
        return account;
    }

    public void setAccount(User account) {
        this.account = account;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }
}
