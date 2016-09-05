package com.shopping.foundation.service.impl;

import com.shopping.core.dao.IGenericDAO;
import com.shopping.core.query.GenericPageList;
import com.shopping.core.query.PageObject;
import com.shopping.core.query.support.IPageList;
import com.shopping.core.query.support.IQueryObject;
import com.shopping.foundation.domain.PropertyRecord;
import com.shopping.foundation.service.IPropertyRecordService;
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
public class PropertyRecordServiceImpl implements IPropertyRecordService {

    @Resource(name = "propertyRecordDAO")
    private IGenericDAO<PropertyRecord> propertyRecordDAO;

    @Override
    public boolean save(PropertyRecord propertyRecord) {
        try {
            this.propertyRecordDAO.save(propertyRecord);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public boolean delete(Long id) {
        try {
            this.propertyRecordDAO.remove(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public PropertyRecord getObjById(Long id) {
        PropertyRecord record = this.propertyRecordDAO.get(id);
        if (record != null) {
            return record;
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
        GenericPageList pList = new GenericPageList(PropertyRecord.class, query,
                params, this.propertyRecordDAO);
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
    public PropertyRecord getObjByProperty(String propertyName, String value) {
        return this.propertyRecordDAO.getBy(propertyName, value);
    }

    @Override
    public List<PropertyRecord> query(String paramString, Map paramMap, int paramInt1, int paramInt2) {
        return this.propertyRecordDAO.query(paramString, paramMap, paramInt1, paramInt2);
    }
}
