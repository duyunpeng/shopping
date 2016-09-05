package com.shopping.manage.admin.action;

import com.shopping.core.annotation.SecurityMapping;
import com.shopping.core.domain.virtual.SysMap;
import com.shopping.core.enums.DeleteStatus;
import com.shopping.core.mv.JModelAndView;
import com.shopping.core.query.support.IPageList;
import com.shopping.core.tools.CommUtil;
import com.shopping.foundation.domain.PropertyExpressParcel;
import com.shopping.foundation.domain.PropertySheet;
import com.shopping.foundation.domain.query.PropertyExpressParcelQueryObject;
import com.shopping.foundation.domain.query.PropertySheetQueryObject;
import com.shopping.foundation.service.IPropertyExpressParcelService;
import com.shopping.foundation.service.ISysConfigService;
import com.shopping.foundation.service.IUserConfigService;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by dyp on 2016/8/29.
 */
@Controller
public class PropertyExpressParcelManageAction {
    @Autowired
    private ISysConfigService configService;

    @Autowired
    private IUserConfigService userConfigService;

    @Autowired
    private IPropertyExpressParcelService expressParcelService;

    @SecurityMapping(display = false, rsequence = 0, title = "物业表列表", value = "/admin/propertyExpressParcel_list.htm*", rtype = "admin", rname = "物业管理", rcode = "propertyExpressParcel_admin", rgroup = "运营")
    @RequestMapping({"/admin/propertyExpressParcel_list.htm"})
    public ModelAndView propertyExpressParcel_list(HttpServletRequest request, HttpServletResponse response, String currentPage, String orderBy, String orderType, String parcelName,String expressParcelStatus) {
        ModelAndView mv = new JModelAndView("admin/blue/propertyExpressParcel_list.html",
                this.configService.getSysConfig(), this.userConfigService
                .getUserConfig(), 0, request, response);
        PropertySheetQueryObject qo = new PropertySheetQueryObject(currentPage, mv, orderBy, orderType);
        if (!CommUtil.null2String(parcelName).equals("")) {
            qo.addQuery("obj.parcelName.propertyName", new SysMap("propertyName", parcelName), "=");
        }
        if (!CommUtil.null2String(expressParcelStatus).equals("")) {
            qo.addQuery("obj.expressParcelStatus", new SysMap("expressParcelStatus",
                    DeleteStatus.getEnumFromString(CommUtil.null2String(expressParcelStatus))), "=");
        }

        IPageList pList = this.expressParcelService.list(qo);
        CommUtil.saveIPageList2ModelAndView("", "", "", pList, mv);
        return mv;
    }


    @SecurityMapping(display = false, rsequence = 0, title = "物业详情", value = "/admin/propertyExpressParcel_view.htm*", rtype = "admin", rname = "物业管理", rcode = "propertyExpressParcel_admin", rgroup = "运营")
    @RequestMapping({"/admin/propertyExpressParcel_view.htm"})
    public ModelAndView propertyExpressParcel_view(HttpServletRequest request, HttpServletResponse response, String id) {
        ModelAndView mv = new JModelAndView("admin/blue/propertyExpressParcel_view.html",
                this.configService.getSysConfig(), this.userConfigService
                .getUserConfig(), 0, request, response);
        PropertyExpressParcel obj = this.expressParcelService.getObjById(CommUtil.null2Long(id));
        mv.addObject("obj", obj);
        return mv;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "物业添加", value = "/admin/propertyExpressParcel_add.htm*", rtype = "admin", rname = "物业管理", rcode = "propertyExpressParcel_admin", rgroup = "运营")
    @RequestMapping({"/admin/propertyExpressParcel_add.htm"})
    public ModelAndView propertyExpressParcel_add(HttpServletRequest request, HttpServletResponse response, String currentPage) {
        ModelAndView mv = new JModelAndView("admin/blue/propertyExpressParcel_add.html",
                this.configService.getSysConfig(), this.userConfigService
                .getUserConfig(), 0, request, response);
        mv.addObject("currentPage", currentPage);
        return mv;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "物业编辑", value = "/admin/propertyExpressParcel_edit.htm*", rtype = "admin", rname = "物业管理", rcode = "propertyExpressParcel_admin", rgroup = "运营")
    @RequestMapping({"/admin/propertyExpressParcel_edit.htm"})
    public ModelAndView propertyExpressParcel_edit(HttpServletRequest request, HttpServletResponse response, String id, String currentPage) {
        ModelAndView mv = new JModelAndView("admin/blue/propertyExpressParcel_edit.html",
                this.configService.getSysConfig(), this.userConfigService
                .getUserConfig(), 0, request, response);
        if ((id != null) && (!id.equals(""))) {
            PropertyExpressParcel expressParcel = this.expressParcelService.getObjById(
                    Long.valueOf(Long.parseLong(id)));
            mv.addObject("obj", expressParcel);
            mv.addObject("currentPage", currentPage);
            mv.addObject("edit", Boolean.valueOf(true));
        }
        return mv;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "物业删除", value = "/admin/propertyExpressParcel_delete.htm*", rtype = "admin", rname = "物业管理", rcode = "propertyExpressParcel_admin", rgroup = "运营")
    @RequestMapping({"/admin/propertyExpressParcel_delete.htm"})
    public String propertyExpressParcel_delete(HttpServletRequest request, HttpServletResponse response, String mulitId, String currentPage) {
        String[] ids = mulitId.split(",");
        for (String id : ids) {
            if (!id.equals("")) {
                PropertyExpressParcel parcel = this.expressParcelService.getObjById(Long.parseLong(id));
                parcel.setParcelName(null);
                this.expressParcelService.delete(Long.valueOf(id));
            }
        }
        return "redirect:propertyExpressParcel_list.htm?currentPage=" + currentPage;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "物业保存", value = "/admin/propertyExpressParcel_save.htm*", rtype = "admin", rname = "物业管理", rcode = "propertyExpressParcel_admin", rgroup = "运营")
    @RequestMapping({"/admin/propertyExpressParcel_save.htm"})
    public String propertyExpressParcel_save(HttpServletRequest request, HttpServletResponse response, String id, String currentPage,String parcelName, String courierNumber, String shippingAddress, String shippingPerson,
                                             String receiptAddress,String receiptPerson ,String createTime,String createPerson,String expressParcelStatus,String deletePerson,String deleteTime,String shippingPhoneNumber,String receiptPhoneNumber) throws ParseException {
        if (!id.equals("")) {
            PropertyExpressParcel expressParcel = expressParcelService.getObjById(Long.parseLong(id));
            if (null != parcelName && !parcelName.equals("")) {
                PropertySheet sheet = new PropertySheet();
                sheet.setPropertyName(parcelName);
                expressParcel.setParcelName(sheet);
            }
            if (null != courierNumber && !courierNumber.equals("")) {
                expressParcel.setCourierNumber(courierNumber);
            }
            if (null != shippingAddress && !shippingAddress.equals("")) {
                expressParcel.setShippingAddress(shippingAddress);
            }
            if (null != shippingPerson && !shippingPerson.equals("")) {
                expressParcel.setShippingPerson(shippingPerson);
            }
            if (null != receiptAddress && !receiptAddress.equals("")) {
                expressParcel.setReceiptAddress(receiptAddress);
            }
            if (null != receiptPerson && !receiptPerson.equals("")) {
                expressParcel.setReceiptPerson(receiptPerson);
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (null != createTime && !createTime.equals("")) {
                Date create = sdf.parse(createTime);
                expressParcel.setCreateTime(create);
            }
            if (null != deleteTime && !deleteTime.equals("")) {
                Date delete = sdf.parse(deleteTime);
                expressParcel.setDeleteTime(delete);
            }
            if (null != createPerson && !createPerson.equals("")) {
                expressParcel.setCreatePerson(createPerson);
            }
            if (null != expressParcelStatus && !expressParcelStatus.equals("")) {
                expressParcel.setExpressParcelStatus(DeleteStatus.getEnumFromString(expressParcelStatus));
            }
            if (null != deletePerson && !deletePerson.equals("")) {
                expressParcel.setDeletePerson(deletePerson);
            }
            if (null != shippingPhoneNumber && !shippingPhoneNumber.equals("")) {
                expressParcel.setShippingPhoneNumber(shippingPhoneNumber);
            }
            if (null != receiptPhoneNumber && !receiptPhoneNumber.equals("")) {
                expressParcel.setReceiptPhoneNumber(receiptPhoneNumber);
            }
            this.expressParcelService.save(expressParcel);
        }
        return "redirect:propertyExpressParcel_list.htm?currentPage=" + currentPage;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "物业添加", value = "/admin/propertyExpressParcel_new.htm*", rtype = "admin", rname = "物业管理", rcode = "propertyExpressParcel_admin", rgroup = "运营")
    @RequestMapping({"/admin/propertyExpressParcel_new.htm"})
    public ModelAndView propertyExpressParcel_new(HttpServletRequest request, HttpServletResponse response, String currentPage, String orderBy, String orderType, String parcelName, String courierNumber, String shippingAddress, String shippingPerson,
                                                  String receiptAddress,String receiptPerson ,String createTime,String createPerson,String expressParcelStatus,String deletePerson,String deleteTime,String shippingPhoneNumber,String receiptPhoneNumber) throws ParseException {
        ModelAndView mv = new JModelAndView("admin/blue/propertyExpressParcel_list.html",
                this.configService.getSysConfig(), this.userConfigService
                .getUserConfig(), 0, request, response);
        PropertyExpressParcel expressParcel = new PropertyExpressParcel();
        if (null != parcelName && !parcelName.equals("")) {
            PropertySheet sheet = new PropertySheet();
            sheet.setPropertyName(parcelName);
            expressParcel.setParcelName(sheet);
        }
        if (null != courierNumber && !courierNumber.equals("")) {
            expressParcel.setCourierNumber(courierNumber);
        }
        if (null != shippingAddress && !shippingAddress.equals("")) {
            expressParcel.setShippingAddress(shippingAddress);
        }
        if (null != shippingPerson && !shippingPerson.equals("")) {
            expressParcel.setShippingPerson(shippingPerson);
        }
        if (null != receiptAddress && !receiptAddress.equals("")) {
            expressParcel.setReceiptAddress(receiptAddress);
        }
        if (null != receiptPerson && !receiptPerson.equals("")) {
            expressParcel.setReceiptPerson(receiptPerson);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (null != createTime && !createTime.equals("")) {
            Date create = sdf.parse(createTime);
            expressParcel.setCreateTime(create);
        }
        if (null != deleteTime && !deleteTime.equals("")) {
            Date delete = sdf.parse(deleteTime);
            expressParcel.setDeleteTime(delete);
        }
        if (null != createPerson && !createPerson.equals("")) {
            expressParcel.setCreatePerson(createPerson);
        }
        if (null != expressParcelStatus && !expressParcelStatus.equals("")) {
            expressParcel.setExpressParcelStatus(DeleteStatus.getEnumFromString(expressParcelStatus));
        }
        if (null != deletePerson && !deletePerson.equals("")) {
            expressParcel.setDeletePerson(deletePerson);
        }
        if (null != shippingPhoneNumber && !shippingPhoneNumber.equals("")) {
            expressParcel.setShippingPhoneNumber(shippingPhoneNumber);
        }
        if (null != receiptPhoneNumber && !receiptPhoneNumber.equals("")) {
            expressParcel.setReceiptPhoneNumber(receiptPhoneNumber);
        }
        expressParcel.setAddTime(new Date());
        this.expressParcelService.save(expressParcel);
        PropertyExpressParcelQueryObject qo = new PropertyExpressParcelQueryObject(currentPage, mv, orderBy, orderType);
        IPageList pList = this.expressParcelService.list(qo);
        CommUtil.saveIPageList2ModelAndView("", "", "", pList, mv);
        return mv;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "物业导出表格", value = "/admin/propertyExpressParcel_export_excel.htm*", rtype = "admin", rname = "物业管理", rcode = "propertyExpressParcel_admin", rgroup = "运营")
    @RequestMapping({"/admin/propertyExpressParcel_export_excel.htm"})
    public void propertyExpressParcel_export_excel(HttpServletRequest request, HttpServletResponse response, String currentPage, String orderBy, String orderType, String parcelName,String expressParcelStatus) throws IOException {
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("物业表");
        HSSFRow row = sheet.createRow((int) 0);
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);


        sheet.setDefaultColumnWidth(20);
        sheet.setDefaultRowHeight((short) 30);

        HSSFCell cell = row.createCell(0);
        cell.setCellValue("id");
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellValue("物业名称");
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellValue("快递单号");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("发货地址");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellValue("发货人");
        cell.setCellStyle(style);

        cell = row.createCell(5);
        cell.setCellValue("收货地址");
        cell.setCellStyle(style);

        cell = row.createCell(6);
        cell.setCellValue("收货人");
        cell.setCellStyle(style);

        cell = row.createCell(7);
        cell.setCellValue("创建时间");
        cell.setCellStyle(style);

        cell = row.createCell(8);
        cell.setCellValue("创建人");
        cell.setCellStyle(style);

        cell = row.createCell(9);
        cell.setCellValue("是否删除");
        cell.setCellStyle(style);

        cell = row.createCell(10);
        cell.setCellValue("删除人");
        cell.setCellStyle(style);

        cell = row.createCell(11);
        cell.setCellValue("删除时间");
        cell.setCellStyle(style);

        cell = row.createCell(12);
        cell.setCellValue("发货人电话");
        cell.setCellStyle(style);

        cell = row.createCell(13);
        cell.setCellValue("收货人电话");
        cell.setCellStyle(style);

        PropertyExpressParcelQueryObject qo = new PropertyExpressParcelQueryObject();
        if (!CommUtil.null2String(parcelName).equals("")) {
            qo.addQuery("obj.parcelName.propertyName", new SysMap("propertyName", parcelName), "=");
        }
        if (!CommUtil.null2String(expressParcelStatus).equals("")) {
            qo.addQuery("obj.expressParcelStatus", new SysMap("expressParcelStatus",
                    DeleteStatus.getEnumFromString(CommUtil.null2String(expressParcelStatus))), "=");
        }
        IPageList pList = expressParcelService.list(qo);
        List<PropertyExpressParcel> lists = pList.getResult();
        if (null != lists) {
            for (int i = 0; i < lists.size(); i++) {
                row = sheet.createRow((int) i + 1);
                PropertyExpressParcel expressParcel = lists.get(i);
                row.createCell(0).setCellValue(expressParcel.getId());
                row.createCell(1).setCellValue(expressParcel.getParcelName().getPropertyName());
                row.createCell(2).setCellValue(expressParcel.getCourierNumber());
                row.createCell(3).setCellValue(expressParcel.getShippingAddress());
                row.createCell(4).setCellValue(expressParcel.getShippingPerson());
                row.createCell(5).setCellValue(expressParcel.getReceiptAddress());
                row.createCell(6).setCellValue(expressParcel.getReceiptPerson());
                row.createCell(7).setCellValue(expressParcel.getCreateTime().toString());
                row.createCell(8).setCellValue(expressParcel.getCreatePerson());
                row.createCell(9).setCellValue(expressParcel.getExpressParcelStatus().getName());
                row.createCell(10).setCellValue(expressParcel.getDeletePerson());
                row.createCell(11).setCellValue(expressParcel.getDeleteTime().toString());
                row.createCell(12).setCellValue(expressParcel.getShippingPhoneNumber());
                row.createCell(13).setCellValue(expressParcel.getReceiptPhoneNumber());
            }
        }
        if (null == lists) {
            for (int i = 0; i < 14; i++) {
                row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue("");
                row.createCell(1).setCellValue("");
                row.createCell(2).setCellValue("");
                row.createCell(3).setCellValue("");
                row.createCell(4).setCellValue("");
                row.createCell(5).setCellValue("");
                row.createCell(6).setCellValue("");
                row.createCell(7).setCellValue("");
                row.createCell(8).setCellValue("");
                row.createCell(9).setCellValue("");
                row.createCell(10).setCellValue("");
                row.createCell(11).setCellValue("");
                row.createCell(12).setCellValue("");
                row.createCell(13).setCellValue("");
            }
        }

        String fileName = "物业管理表";
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        wb.write(os);
        byte[] content = os.toByteArray();
        InputStream is = new ByteArrayInputStream(content);
        response.reset();
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String((fileName + ".xls").getBytes(), "iso-8859-1"));
        ServletOutputStream out = response.getOutputStream();
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(is);
            bos = new BufferedOutputStream(out);
            byte[] buff = new byte[2048];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                bis.close();
            }
            if (bos != null) {
                bos.close();
            }
        }
    }
}
