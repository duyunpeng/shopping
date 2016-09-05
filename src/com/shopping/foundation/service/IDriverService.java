package com.shopping.foundation.service;

import com.shopping.foundation.domain.Area;
import com.shopping.foundation.domain.Driver;

import java.util.List;
import java.util.Map;

/**
 * Created by jm on 16-8-24.
 */
public abstract interface IDriverService {

    public abstract boolean update(Driver paramDriver);

    public abstract Driver getObjById(Long paramLong);

    public abstract List<Driver> query(String paramString, Map paramMap, int paramInt1, int paramInt2);

}
