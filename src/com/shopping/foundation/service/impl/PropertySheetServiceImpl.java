package com.shopping.foundation.service.impl;

import com.shopping.core.dao.IGenericDAO;
import com.shopping.core.query.GenericPageList;
import com.shopping.core.query.PageObject;
import com.shopping.core.query.support.IPageList;
import com.shopping.core.query.support.IQueryObject;
import com.shopping.foundation.domain.PropertySheet;
import com.shopping.foundation.service.IPropertySheetService;
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
public class PropertySheetServiceImpl implements IPropertySheetService {
    @Resource(name = "propertySheetDAO")
    private IGenericDAO<PropertySheet> propertySheetDAO;

    @Override
    public boolean save(PropertySheet propertySheet) {
        try {
            this.propertySheetDAO.save(propertySheet);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public boolean delete(Long id) {
        try {
            this.propertySheetDAO.remove(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public PropertySheet getObjById(Long id) {
        PropertySheet propertySheet = this.propertySheetDAO.get(id);
        if (propertySheet != null) {
            return propertySheet;
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
        GenericPageList pList = new GenericPageList(PropertySheet.class, query,
                params, this.propertySheetDAO);
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
    public PropertySheet getObjByProperty(String propertyName, String value) {
        return this.propertySheetDAO.getBy(propertyName, value);
    }

    @Override
    public List<PropertySheet> query(String paramString, Map paramMap, int paramInt1, int paramInt2) {
        return this.propertySheetDAO.query(paramString, paramMap, paramInt1, paramInt2);
    }
}