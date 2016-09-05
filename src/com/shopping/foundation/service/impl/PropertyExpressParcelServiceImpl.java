package com.shopping.foundation.service.impl;

import com.shopping.core.dao.IGenericDAO;
import com.shopping.core.query.GenericPageList;
import com.shopping.core.query.PageObject;
import com.shopping.core.query.support.IPageList;
import com.shopping.core.query.support.IQueryObject;
import com.shopping.foundation.domain.PropertyExpressParcel;
import com.shopping.foundation.service.IPropertyExpressParcelService;
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
public class PropertyExpressParcelServiceImpl implements IPropertyExpressParcelService {
    @Resource(name = "propertyExpressParcelDAO")
    private IGenericDAO<PropertyExpressParcel> parcelDAO;

    @Override
    public boolean save(PropertyExpressParcel parcel) {
        try {
            this.parcelDAO.save(parcel);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public boolean delete(Long id) {
        try {
            this.parcelDAO.remove(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public PropertyExpressParcel getObjById(Long id) {
        PropertyExpressParcel parcel = this.parcelDAO.get(id);
        if (parcel != null) {
            return parcel;
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
        GenericPageList pList = new GenericPageList(PropertyExpressParcel.class, query,
                params, this.parcelDAO);
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
    public PropertyExpressParcel getObjByProperty(String propertyName, String value) {
        return this.parcelDAO.getBy(propertyName, value);
    }

    @Override
    public List<PropertyExpressParcel> query(String paramString, Map paramMap, int paramInt1, int paramInt2) {
        return this.parcelDAO.query(paramString, paramMap, paramInt1, paramInt2);
    }
}
