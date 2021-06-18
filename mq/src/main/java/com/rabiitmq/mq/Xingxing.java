package com.rabiitmq.mq;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.alibaba.fastjson.JSON;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @ClassName Xingxing
 * @Description TODO
 * @Author gmb
 * @Date 2021/6/17 0017 18:49
 */
public class Xingxing {
    public static void main(String[] args) throws Exception{
        InputStream in = new FileInputStream("D:\\hd\\rabittmq\\mq\\src\\main\\resources\\111.xls");
        ImportParams params =new ImportParams();
        List<Entity> list = ExcelImportUtil.importExcel(in, Entity.class, params);
        List<Entity> errDatas = new ArrayList<>();
        for (Entity entity:list){
            String seq = entity.getSq();
            String details = entity.getChargeDetails();
            String policyInfos = entity.getPolicyInfos();
            List<ChargeDetail> chargeDetails = JSON.parseArray(details, ChargeDetail.class);
            List<policyInfo> policyInfoList = JSON.parseArray(policyInfos, policyInfo.class);
            if (policyInfoList!=null&&policyInfoList.size()>0){
                policyInfoList = policyInfoList.stream().sorted(Comparator.comparing(p -> p.getStartTime())).collect(Collectors.toList());
            }
            if (chargeDetails!=null&&chargeDetails.size()>0){
                for (ChargeDetail c:chargeDetails){
                    double elecPrice = c.getElecPrice();
                    double sevicePrice = c.getSevicePrice();
                    String detailStartTime = c.getDetailStartTime();
                    SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    SimpleDateFormat simpleDateFormat2 =new SimpleDateFormat("HH");
                    Date startTimes = simpleDateFormat.parse(detailStartTime);
                    String format = simpleDateFormat2.format(startTimes);
                    int hour = Integer.parseInt(format);
                    if (policyInfoList!=null&&policyInfoList.size()>0){
                        for (int j =0;j<policyInfoList.size()-1;j++){
                            policyInfo p = policyInfoList.get(j);
                            policyInfo p1 = policyInfoList.get(j+1);
                            String startTime = p.getStartTime().substring(0,2);
                            int start = Integer.parseInt(startTime);
                            String endTime = p1.getStartTime().substring(0,2);
                            int end = Integer.parseInt(endTime);
                            if (hour >= start && hour < end){
                                if (elecPrice!=p.getElecPrice()||sevicePrice!=p.getServicePrice()){
                                    String err ="开始时间；"+detailStartTime+"：结算"+elecPrice+"+"+sevicePrice;
                                    String push = "推送："+p.getElecPrice()+"+"+p.getServicePrice();
                                    entity.setNote(entity.getNote()+err+push);
                                    BigDecimal pushPrice = new BigDecimal(elecPrice).add(new BigDecimal(sevicePrice));
                                    BigDecimal endPrice = new BigDecimal(p.getElecPrice()).add(new BigDecimal(p.getServicePrice()));
                                    if(pushPrice.compareTo(endPrice)>0){
                                        entity.setLovel("结算>推送：");
                                        errDatas.add(entity);
                                    }/*else {
                                        entity.setLovel("结算<推送："+(pushPrice.subtract(endPrice)
                                                .setScale(5, RoundingMode.HALF_UP)));
                                    }*/
                                }
                                break;
                            }
                        }
                    }
                }
            }

        }
        errDatas = errDatas.stream().distinct().collect(Collectors.toList());
        ExportParams exportParams = new ExportParams();
        exportParams.setType(ExcelType.XSSF);
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet("数据");
        for (int k =0;k<errDatas.size();k++){
            Row row = sheet.createRow(k);
            row.createCell(0).setCellValue(errDatas.get(k).getSq());
            row.createCell(1).setCellValue(errDatas.get(k).getChargeDetails());
            row.createCell(2).setCellValue(errDatas.get(k).getPolicyInfos());
            row.createCell(3).setCellValue(errDatas.get(k).getNote());
            row.createCell(4).setCellValue(errDatas.get(k).getLovel());
        }
       // Workbook workbook = ExcelExportUtil.exportExcel(exportParams, Entity.class, errDatas);
        FileOutputStream fileOutputStream = new FileOutputStream("D:\\hd\\rabittmq\\mq\\src\\main\\resources\\114.xls");
        workbook.write(fileOutputStream);
        fileOutputStream.close();
        workbook.close();
        in.close();
        System.out.println("异常"+errDatas.size());
    }
}
