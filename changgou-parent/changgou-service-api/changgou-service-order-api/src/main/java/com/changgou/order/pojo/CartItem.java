package com.changgou.order.pojo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author pengmin
 * @date 2020/12/2 10:41
 */
@Table(name = "tb_cart_item")
public class CartItem implements Serializable {
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(name = "cart_sku_id")
    private Long cartSkuId;

    @Column(name = "username")
    private String username;

    @Column(name = "num")
    private Integer num;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    public CartItem() {
    }

    public CartItem(String id, Long cartSkuId, String username, Integer num, Date createTime, Date updateTime) {
        this.id = id;
        this.cartSkuId = cartSkuId;
        this.username = username;
        this.num = num;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getCartSkuId() {
        return cartSkuId;
    }

    public void setCartSkuId(Long cartSkuId) {
        this.cartSkuId = cartSkuId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
