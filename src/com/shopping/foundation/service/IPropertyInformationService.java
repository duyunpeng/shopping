package com.shopping.foundation.service;

import com.shopping.core.query.support.IPageList;
import com.shopping.core.query.support.IQueryObject;
import com.shopping.foundation.domain.PropertyInformation;

import java.util.List;
import java.util.Map;

/**
 * Created by dyp on 2016/8/26.
 */
public interface IPropertyInformationService {
    public abstract boolean save(PropertyInformation information);

    public abstract PropertyInformation getObjById(Long id);

    public abstract boolean delete(Long id);

    public abstract IPageList list(IQueryObject paramIQueryObject);

    public abstract PropertyInformation getObjByProperty(String paramString1, String paramString2);

    public abstract List<PropertyInformation> query(String paramString, Map paramMap, int paramInt1, int paramInt2);
}
