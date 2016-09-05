package com.shopping.foundation.domain;

import com.shopping.core.domain.IdEntity;
import com.shopping.core.enums.DeleteStatus;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by dyp on 2016/8/26.
 */
//物业快递包裹
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Table(name = "shopping_property_express_parcel")
public class PropertyExpressParcel extends IdEntity{

    //物业名称
    @OneToOne(cascade = CascadeType.ALL)
    private PropertySheet parcelName;

    //快递单号
    private String courierNumber;

    //发货地址
    private String shippingAddress;

    //发货人
    private String shippingPerson;

    //收货地址
    private String receiptAddress;

    //收货人
    private String receiptPerson;

    //创建时间
    private Date createTime;

    //创建人
    private String createPerson;

    //是否删除
    private DeleteStatus expressParcelStatus;

    //删除人
    private String deletePerson;

    //删除时间
    private Date deleteTime;

    //发货人电话
    private String shippingPhoneNumber;

    //收货人电话
    private String receiptPhoneNumber;

    public PropertySheet getParcelName() {
        return parcelName;
    }

    public void setParcelName(PropertySheet parcelName) {
        this.parcelName = parcelName;
    }

    public String getCourierNumber() {
        return courierNumber;
    }

    public void setCourierNumber(String courierNumber) {
        this.courierNumber = courierNumber;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getShippingPerson() {
        return shippingPerson;
    }

    public void setShippingPerson(String shippingPerson) {
        this.shippingPerson = shippingPerson;
    }

    public String getReceiptAddress() {
        return receiptAddress;
    }

    public void setReceiptAddress(String receiptAddress) {
        this.receiptAddress = receiptAddress;
    }

    public String getReceiptPerson() {
        return receiptPerson;
    }

    public void setReceiptPerson(String receiptPerson) {
        this.receiptPerson = receiptPerson;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreatePerson() {
        return createPerson;
    }

    public void setCreatePerson(String createPerson) {
        this.createPerson = createPerson;
    }

    public DeleteStatus getExpressParcelStatus() {
        return expressParcelStatus;
    }

    public void setExpressParcelStatus(DeleteStatus expressParcelStatus) {
        this.expressParcelStatus = expressParcelStatus;
    }

    public String getDeletePerson() {
        return deletePerson;
    }

    public void setDeletePerson(String deletePerson) {
        this.deletePerson = deletePerson;
    }

    public Date getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(Date deleteTime) {
        this.deleteTime = deleteTime;
    }

    public String getShippingPhoneNumber() {
        return shippingPhoneNumber;
    }

    public void setShippingPhoneNumber(String shippingPhoneNumber) {
        this.shippingPhoneNumber = shippingPhoneNumber;
    }

    public String getReceiptPhoneNumber() {
        return receiptPhoneNumber;
    }

    public void setReceiptPhoneNumber(String receiptPhoneNumber) {
        this.receiptPhoneNumber = receiptPhoneNumber;
    }
}
