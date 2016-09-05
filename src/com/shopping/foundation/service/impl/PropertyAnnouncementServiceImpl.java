package com.shopping.foundation.service.impl;

import com.shopping.core.dao.IGenericDAO;
import com.shopping.core.query.GenericPageList;
import com.shopping.core.query.PageObject;
import com.shopping.core.query.support.IPageList;
import com.shopping.core.query.support.IQueryObject;
import com.shopping.foundation.domain.PropertyAnnouncement;
import com.shopping.foundation.service.IPropertyAnnouncementService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by dyp on 2016/8/26.
 */
@Service
@Transactional
public class PropertyAnnouncementServiceImpl implements IPropertyAnnouncementService {

    @Resource(name = "propertyAnnouncementDAO")
    private IGenericDAO<PropertyAnnouncement> announcementDAO;

    @Override
    public boolean save(PropertyAnnouncement announcement) {
        try {
            this.announcementDAO.save(announcement);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public boolean delete(Long id) {
        try {
            this.announcementDAO.remove(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public PropertyAnnouncement getObjById(Long id) {
        PropertyAnnouncement announcement = this.announcementDAO.get(id);
        if (announcement != null) {
            return announcement;
        }
        return null;
    }

    @Override
    public IPageList list(IQueryObject properties) {
        if (properties == null) {
            return null;
        }
        String query = properties.getQuery();
        Map params = properties.getParameters();
        GenericPageList pList = new GenericPageList(PropertyAnnouncement.class, query,
                params, this.announcementDAO);
        if (properties != null) {
            PageObject pageObj = properties.getPageObj();
            if (pageObj != null) {
                pList.doList(pageObj.getCurrentPage() == null ? 0 : pageObj
                        .getCurrentPage().intValue(), pageObj.getPageSize() == null ? 0 :
                        pageObj.getPageSize().intValue());
            } else {
                pList.doList(0, -1);
            }
        }
        return pList;
    }


    @Override
    public PropertyAnnouncement getObjByProperty(String propertyName, String value) {
        return this.announcementDAO.getBy(propertyName, value);
    }

    @Override
    public List<PropertyAnnouncement> query(String paramString, Map paramMap, int paramInt1, int paramInt2) {
        return this.announcementDAO.query(paramString, paramMap, paramInt1, paramInt2);
    }
}
