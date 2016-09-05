package com.shopping.foundation.service.impl;

import com.shopping.core.dao.IGenericDAO;
import com.shopping.core.query.GenericPageList;
import com.shopping.core.query.PageObject;
import com.shopping.core.query.support.IPageList;
import com.shopping.core.query.support.IQueryObject;
import com.shopping.foundation.domain.ServiceProvider;
import com.shopping.foundation.service.IServiceProviderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by dyp on 2016/8/26.
 */
@Service
@Transactional(readOnly = false)
public class ServiceProviderServiceImpl implements IServiceProviderService{
    @Resource(name = "serviceProviderDAO")
    private IGenericDAO<ServiceProvider> providerDAO;

    @Override
    public boolean save(ServiceProvider provider) {
        try {
            this.providerDAO.save(provider);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public boolean delete(Long id) {
        try {
            this.providerDAO.remove(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public ServiceProvider getObjById(Long id) {
        ServiceProvider provider = this.providerDAO.get(id);
        if (provider != null) {
            return provider;
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
        GenericPageList pList = new GenericPageList(ServiceProvider.class, query,
                params, this.providerDAO);
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
    public ServiceProvider getObjByProperty(String propertyName, String value) {
        return this.providerDAO.getBy(propertyName, value);
    }

    @Override
    public List<ServiceProvider> query(String paramString, Map paramMap, int paramInt1, int paramInt2) {
        return this.providerDAO.query(paramString, paramMap, paramInt1, paramInt2);
    }
}
