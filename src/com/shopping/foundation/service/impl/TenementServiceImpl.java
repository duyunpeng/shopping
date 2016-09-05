package com.shopping.foundation.service.impl;

import com.shopping.core.dao.IGenericDAO;
import com.shopping.core.query.GenericPageList;
import com.shopping.core.query.PageObject;
import com.shopping.core.query.support.IPageList;
import com.shopping.core.query.support.IQueryObject;
import com.shopping.foundation.domain.Tenement;
import com.shopping.foundation.service.ITenementService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by dyp on 2016/8/22.
 */
@Service
@Transactional
public class TenementServiceImpl implements ITenementService {

    @Resource(name = "tenementDAO")
    private IGenericDAO<Tenement> tenementDAO;

    @Override
    public boolean save(Tenement tenement) {
        try {
            this.tenementDAO.save(tenement);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public boolean delete(Long id) {
        try {
            this.tenementDAO.remove(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Tenement getObjById(Long id) {
        Tenement tenement = this.tenementDAO.get(id);
        if (tenement != null) {
            return tenement;
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
        GenericPageList pList = new GenericPageList(Tenement.class, query,
                params, this.tenementDAO);
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
    public Tenement getObjByProperty(String propertyName, String value) {
        return this.tenementDAO.getBy(propertyName, value);
    }

    @Override
    public List<Tenement> query(String paramString, Map paramMap, int paramInt1, int paramInt2) {
        return this.tenementDAO.query(paramString, paramMap, paramInt1, paramInt2);
    }
}
