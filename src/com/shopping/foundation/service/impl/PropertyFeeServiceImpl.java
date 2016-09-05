package com.shopping.foundation.service.impl;

import com.shopping.core.dao.IGenericDAO;
import com.shopping.core.query.GenericPageList;
import com.shopping.core.query.PageObject;
import com.shopping.core.query.support.IPageList;
import com.shopping.core.query.support.IQueryObject;
import com.shopping.foundation.domain.PropertyFee;
import com.shopping.foundation.domain.PropertySheet;
import com.shopping.foundation.service.IPropertyFeeService;
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
public class PropertyFeeServiceImpl implements IPropertyFeeService {
    @Resource(name = "propertyFeeDAO")
    private IGenericDAO<PropertyFee> feeDAO;

    @Override
    public boolean save(PropertyFee fee) {
        try {
            this.feeDAO.save(fee);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public boolean delete(Long id) {
        try {
            this.feeDAO.remove(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public PropertyFee getObjById(Long id) {
        PropertyFee fee = this.feeDAO.get(id);
        if (fee != null) {
            return fee;
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
        GenericPageList pList = new GenericPageList(PropertyFee.class, query,
                params, this.feeDAO);
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
    public PropertyFee getObjByProperty(String propertyName, String value) {
        return this.feeDAO.getBy(propertyName, value);
    }

    @Override
    public List<PropertyFee> query(String paramString, Map paramMap, int paramInt1, int paramInt2) {
        return this.feeDAO.query(paramString, paramMap, paramInt1, paramInt2);
    }
}
