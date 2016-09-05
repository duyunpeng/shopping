package com.shopping.foundation.domain;

import com.shopping.core.domain.IdEntity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jm on 16-8-24.
 */
@Cache(usage= CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Table(name="shopping_driver")
public class Driver extends IdEntity{

    //司机名称
    private String driverName;

    //司机电话
    private String phone;

    //负责区域
    @ManyToOne(fetch = FetchType.LAZY)
    private Area area;

    //订单
    @OneToMany(mappedBy = "ec" ,cascade = { javax.persistence.CascadeType.REMOVE })
    List<OrderForm> ofs = new ArrayList<OrderForm>();

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public List<OrderForm> getOfs() {
        return ofs;
    }

    public void setOfs(List<OrderForm> ofs) {
        this.ofs = ofs;
    }
}
