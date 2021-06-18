package com.rabiitmq.mq;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;


@Data
public class Entity {

    @Excel(name = "start_charge_seq")
    private String sq;

    @Excel(name = "charge_details")
    private String chargeDetails;

    @Excel(name = "policy_infos")
    private String policyInfos;

    @Excel(name = "note")
    private String note = "";

    @Excel(name = "lovel")
    private String lovel = "";
}
@Data
class ChargeDetail{

    @JSONField(name ="ElecPrice")
    private double elecPrice;

    @JSONField(name ="DetailPower")
    private double detailPower;

    @JSONField(name ="SevicePrice")
    private double sevicePrice;

    @JSONField(name ="ServicePrice")
    private double servicePrice;

    @JSONField(name ="DetailEndTime")
    private String detailEndTime;

    @JSONField(name ="DetailElecMoney")
    private double detailElecMoney;

    @JSONField(name ="DetailStartTime")
    private String detailStartTime;

}
@Data
class policyInfo {

    @JSONField(name ="elecPrice")
    private double elecPrice;

    @JSONField(name ="startTime")
    private String startTime;

    @JSONField(name ="servicePrice")
    private double servicePrice;
    //@JSONField(name ="servicePrice")

   // private double servicePrice;
}
