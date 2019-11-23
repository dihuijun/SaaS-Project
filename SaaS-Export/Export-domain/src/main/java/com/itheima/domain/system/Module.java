package com.itheima.domain.system;

import java.io.Serializable;
import java.util.Objects;

public class Module implements Serializable {
    private String id;

    /**
     */
    private String parentId;

    /**
     */
    private String parentName;

    /**
     */
    private String name;

    /**
     */
    private Integer layerNum;

    /**
     */
    private Integer isLeaf;

    /**
     */
    private String ico;

    /**
     */
    private String cpermission;

    /**
     */
    private String curl;

    /**
     * 0 主菜单/1 二级菜单/2按钮
     */
    private Integer ctype;

    /**
     * 1启用0停用
     */
    private Integer state;

    /**
     * 从属关系
     *  0：sass系统内部菜单
     *  1：租用企业菜单
     */
    private Integer belong;

    /**
     */
    private String cwhich;

    /**
     */
    private Integer quoteNum;

    /**
     */
    private String remark;

    /**
     */
    private Integer orderNo;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Module module = (Module) o;
        return Objects.equals(id, module.id) &&
                Objects.equals(parentId, module.parentId) &&
                Objects.equals(parentName, module.parentName) &&
                Objects.equals(name, module.name) &&
                Objects.equals(layerNum, module.layerNum) &&
                Objects.equals(isLeaf, module.isLeaf) &&
                Objects.equals(ico, module.ico) &&
                Objects.equals(cpermission, module.cpermission) &&
                Objects.equals(curl, module.curl) &&
                Objects.equals(ctype, module.ctype) &&
                Objects.equals(state, module.state) &&
                Objects.equals(belong, module.belong) &&
                Objects.equals(cwhich, module.cwhich) &&
                Objects.equals(quoteNum, module.quoteNum) &&
                Objects.equals(remark, module.remark) &&
                Objects.equals(orderNo, module.orderNo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLayerNum() {
        return layerNum;
    }

    public void setLayerNum(Integer layerNum) {
        this.layerNum = layerNum;
    }

    public Integer getIsLeaf() {
        return isLeaf;
    }

    public void setIsLeaf(Integer isLeaf) {
        this.isLeaf = isLeaf;
    }

    public String getIco() {
        return ico;
    }

    public void setIco(String ico) {
        this.ico = ico;
    }

    public String getCpermission() {
        return cpermission;
    }

    public void setCpermission(String cpermission) {
        this.cpermission = cpermission;
    }

    public String getCurl() {
        return curl;
    }

    public void setCurl(String curl) {
        this.curl = curl;
    }

    public Integer getCtype() {
        return ctype;
    }

    public void setCtype(Integer ctype) {
        this.ctype = ctype;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getBelong() {
        return belong;
    }

    public void setBelong(Integer belong) {
        this.belong = belong;
    }

    public String getCwhich() {
        return cwhich;
    }

    public void setCwhich(String cwhich) {
        this.cwhich = cwhich;
    }

    public Integer getQuoteNum() {
        return quoteNum;
    }

    public void setQuoteNum(Integer quoteNum) {
        this.quoteNum = quoteNum;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    @Override
    public String toString() {
        return "Module{" +
                "id='" + id + '\'' +
                ", parentId='" + parentId + '\'' +
                ", parentName='" + parentName + '\'' +
                ", name='" + name + '\'' +
                ", layerNum=" + layerNum +
                ", isLeaf=" + isLeaf +
                ", ico='" + ico + '\'' +
                ", cpermission='" + cpermission + '\'' +
                ", curl='" + curl + '\'' +
                ", ctype=" + ctype +
                ", state=" + state +
                ", belong=" + belong +
                ", cwhich='" + cwhich + '\'' +
                ", quoteNum=" + quoteNum +
                ", remark='" + remark + '\'' +
                ", orderNo=" + orderNo +
                '}';
    }
}
