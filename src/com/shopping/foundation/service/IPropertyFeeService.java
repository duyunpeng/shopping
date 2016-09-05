package com.shopping.foundation.service;

import com.shopping.core.query.support.IPageList;
import com.shopping.core.query.support.IQueryObject;
import com.shopping.foundation.domain.PropertyFee;

import java.util.List;
import java.util.Map;

/**
 * Created by dyp on 2016/8/26.
 */
public interface IPropertyFeeService {
    public abstract boolean save(PropertyFee fee);

    public abstract PropertyFee getObjById(Long id);

    public abstract boolean delete(Long id);

    public abstract IPageList list(IQueryObject paramIQueryObject);

    public abstract PropertyFee getObjByProperty(String paramString1, String paramString2);

    public abstract List<PropertyFee> query(String paramString, Map paramMap, int paramInt1, int paramInt2);
}
