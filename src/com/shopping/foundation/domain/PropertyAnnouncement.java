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
//物业公告
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Table(name = "shopping_property_announcement")
public class PropertyAnnouncement extends IdEntity {


    //物业名称
    @OneToOne(cascade = CascadeType.ALL)
    private PropertySheet sheetName;

    //通知标题
    private String noticeTitle;

    //通知内容
    private String  noticeContent;

    //通知时间
    private Date noticeTime;


    public PropertySheet getSheetName() {
        return sheetName;
    }

    public void setSheetName(PropertySheet sheetName) {
        this.sheetName = sheetName;
    }

    public String getNoticeTitle() {
        return noticeTitle;
    }

    public void setNoticeTitle(String noticeTitle) {
        this.noticeTitle = noticeTitle;
    }

    public String getNoticeContent() {
        return noticeContent;
    }

    public void setNoticeContent(String noticeContent) {
        this.noticeContent = noticeContent;
    }

    public Date getNoticeTime() {
        return noticeTime;
    }

    public void setNoticeTime(Date noticeTime) {
        this.noticeTime = noticeTime;
    }
}
