package com.shopping.foundation.service;

import com.shopping.core.query.support.IPageList;
import com.shopping.core.query.support.IQueryObject;
import com.shopping.foundation.domain.Tenement;

import java.util.List;
import java.util.Map;

/**
 * Created by dyp on 2016/8/22.
 */
public abstract interface ITenementService {
    public abstract boolean save(Tenement tenement);

    public abstract Tenement getObjById(Long id);

    public abstract boolean delete(Long id);

    public abstract IPageList list(IQueryObject paramIQueryObject);

    public abstract Tenement getObjByProperty(String paramString1, String paramString2);

    public abstract List<Tenement> query(String paramString, Map paramMap, int paramInt1, int paramInt2);
}
