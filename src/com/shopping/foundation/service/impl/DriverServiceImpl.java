package com.shopping.foundation.service.impl;

import com.shopping.core.dao.IGenericDAO;
import com.shopping.foundation.domain.Area;
import com.shopping.foundation.domain.Driver;

import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import com.shopping.foundation.service.IDriverService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DriverServiceImpl
        implements IDriverService
{

    @Resource(name="driverDAO")
    private IGenericDAO<Driver> driverDAO;


    @Override
    public boolean update(Driver driver) {
        try {
            this.driverDAO.update(driver);
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Driver getObjById(Long id) {
        Driver driver = this.driverDAO.get(id);
        if(null != driver){
            return driver;
        }
        return null;
    }

    @Override
    public List<Driver> query(String query, Map params, int begin, int max) {
        return this.driverDAO.query(query, params, begin, max);
    }
}



 
 