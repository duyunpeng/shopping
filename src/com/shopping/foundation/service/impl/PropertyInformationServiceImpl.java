package com.shopping.foundation.service.impl;

import com.shopping.core.dao.IGenericDAO;
import com.shopping.core.query.GenericPageList;
import com.shopping.core.query.PageObject;
import com.shopping.core.query.support.IPageList;
import com.shopping.core.query.support.IQueryObject;
import com.shopping.foundation.domain.PropertyInformation;
import com.shopping.foundation.service.IPropertyInformationService;
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
public class PropertyInformationServiceImpl implements IPropertyInformationService {
    @Resource(name = "propertyInformationDAO")
    private IGenericDAO<PropertyInformation> informationDAO;

    @Override
    public boolean save(PropertyInformation information) {
        try {
            this.informationDAO.save(information);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public boolean delete(Long id) {
        try {
            this.informationDAO.remove(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public PropertyInformation getObjById(Long id) {
        PropertyInformation information = this.informationDAO.get(id);
        if (information != null) {
            return information;
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
        GenericPageList pList = new GenericPageList(PropertyInformation.class, query,
                params, this.informationDAO);
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
    public PropertyInformation getObjByProperty(String propertyName, String value) {
        return this.informationDAO.getBy(propertyName, value);
    }

    @Override
    public List<PropertyInformation> query(String paramString, Map paramMap, int paramInt1, int paramInt2) {
        return this.informationDAO.query(paramString, paramMap, paramInt1, paramInt2);
    }
}
