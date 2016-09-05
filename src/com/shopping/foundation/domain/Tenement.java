package com.shopping.foundation.domain;

import com.shopping.core.domain.IdEntity;
import com.shopping.core.enums.CollectionStatus;
import com.shopping.core.enums.PackageStatus;
import com.shopping.core.enums.PostageStatus;
import com.shopping.core.enums.SignStatus;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Created by dyp on 2016/8/22.
 */
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Table(name = "shopping_tenement")
public class Tenement extends IdEntity {

    //业主名称
    @OneToOne(cascade = CascadeType.ALL)
    private User user;

    //文件类型
    private PackageStatus fileType;

    //区域属性
    @OneToOne(cascade = CascadeType.ALL)
    private Area area;

    //房屋面积大小
    private double houseArea;

    //代收状态
    private CollectionStatus collectionStatus;

    //签收状态
    private SignStatus signStatus;

    //邮费类型
    private PostageStatus postageStatus;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public PackageStatus getFileType() {
        return fileType;
    }

    public void setFileType(PackageStatus fileType) {
        this.fileType = fileType;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public double getHouseArea() {
        return houseArea;
    }

    public void setHouseArea(double houseArea) {
        this.houseArea = houseArea;
    }

    public CollectionStatus getCollectionStatus() {
        return collectionStatus;
    }

    public void setCollectionStatus(CollectionStatus collectionStatus) {
        this.collectionStatus = collectionStatus;
    }

    public SignStatus getSignStatus() {
        return signStatus;
    }

    public void setSignStatus(SignStatus signStatus) {
        this.signStatus = signStatus;
    }

    public PostageStatus getPostageStatus() {
        return postageStatus;
    }

    public void setPostageStatus(PostageStatus postageStatus) {
        this.postageStatus = postageStatus;
    }
}
