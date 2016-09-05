package com.shopping.foundation.service.impl;

import com.shopping.core.dao.IGenericDAO;
import com.shopping.core.query.GenericPageList;
import com.shopping.core.query.PageObject;
import com.shopping.core.query.support.IPageList;
import com.shopping.core.query.support.IQueryObject;
import com.shopping.foundation.domain.PropertyMessageRecord;
import com.shopping.foundation.domain.PropertySheet;
import com.shopping.foundation.domain.query.PropertyMessageRecordQueryObject;
import com.shopping.foundation.service.IPropertyMessageRecordService;
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
public class PropertyMessageRecordServiceImpl implements IPropertyMessageRecordService {
    @Resource(name = "propertyMessageRecordDAO")
    private IGenericDAO<PropertyMessageRecord> messageRecordDAO;

    @Override
    public boolean save(PropertyMessageRecord messageRecord) {
        try {
            this.messageRecordDAO.save(messageRecord);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public boolean delete(Long id) {
        try {
            this.messageRecordDAO.remove(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public PropertyMessageRecord getObjById(Long id) {
        PropertyMessageRecord messageRecord = this.messageRecordDAO.get(id);
        if (messageRecord != null) {
            return messageRecord;
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
        GenericPageList pList = new GenericPageList(PropertyMessageRecord.class, query,
                params, this.messageRecordDAO);
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
    public PropertyMessageRecord getObjByProperty(String propertyName, String value) {
        return this.messageRecordDAO.getBy(propertyName, value);
    }

    @Override
    public List<PropertyMessageRecord> query(String paramString, Map paramMap, int paramInt1, int paramInt2) {
        return this.messageRecordDAO.query(paramString, paramMap, paramInt1, paramInt2);
    }
}
