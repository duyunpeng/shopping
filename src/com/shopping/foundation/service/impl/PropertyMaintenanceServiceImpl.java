package com.shopping.foundation.service.impl;

import com.shopping.core.dao.IGenericDAO;
import com.shopping.core.query.GenericPageList;
import com.shopping.core.query.PageObject;
import com.shopping.core.query.support.IPageList;
import com.shopping.core.query.support.IQueryObject;
import com.shopping.foundation.domain.PropertyMaintenance;
import com.shopping.foundation.service.IPropertyMaintenanceService;
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
public class PropertyMaintenanceServiceImpl implements IPropertyMaintenanceService {
    @Resource(name = "propertyMaintenanceDAO")
    private IGenericDAO<PropertyMaintenance> maintenanceDAO;

    @Override
    public boolean save(PropertyMaintenance maintenance) {
        try {
            this.maintenanceDAO.save(maintenance);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public boolean delete(Long id) {
        try {
            this.maintenanceDAO.remove(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public PropertyMaintenance getObjById(Long id) {
        PropertyMaintenance maintenance = this.maintenanceDAO.get(id);
        if (maintenance != null) {
            return maintenance;
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
        GenericPageList pList = new GenericPageList(PropertyMaintenance.class, query,
                params, this.maintenanceDAO);
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
    public PropertyMaintenance getObjByProperty(String propertyName, String value) {
        return this.maintenanceDAO.getBy(propertyName, value);
    }

    @Override
    public List<PropertyMaintenance> query(String paramString, Map paramMap, int paramInt1, int paramInt2) {
        return this.maintenanceDAO.query(paramString, paramMap, paramInt1, paramInt2);
    }
}
