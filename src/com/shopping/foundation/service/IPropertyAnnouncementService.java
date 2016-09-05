package com.shopping.foundation.service;

import com.shopping.core.query.support.IPageList;
import com.shopping.core.query.support.IQueryObject;
import com.shopping.foundation.domain.PropertyAnnouncement;

import java.util.List;
import java.util.Map;

/**
 * Created by dyp on 2016/8/26.
 */
public interface IPropertyAnnouncementService {
    public abstract boolean save(PropertyAnnouncement announcement);

    public abstract PropertyAnnouncement getObjById(Long id);

    public abstract boolean delete(Long id);

    public abstract IPageList list(IQueryObject paramIQueryObject);

    public abstract PropertyAnnouncement getObjByProperty(String paramString1, String paramString2);

    public abstract List<PropertyAnnouncement> query(String paramString, Map paramMap, int paramInt1, int paramInt2);
}
