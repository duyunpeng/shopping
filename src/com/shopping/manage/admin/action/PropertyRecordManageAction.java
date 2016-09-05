package com.shopping.manage.admin.action;

import com.shopping.core.annotation.SecurityMapping;
import com.shopping.core.domain.virtual.SysMap;
import com.shopping.core.enums.PackageStatus;
import com.shopping.core.enums.PayStatus;
import com.shopping.core.mv.JModelAndView;
import com.shopping.core.query.support.IPageList;
import com.shopping.core.tools.CommUtil;
import com.shopping.foundation.domain.PropertyRecord;
import com.shopping.foundation.domain.User;
import com.shopping.foundation.domain.query.PropertyRecordQueryObject;
import com.shopping.foundation.domain.query.PropertySheetQueryObject;
import com.shopping.foundation.service.IAreaService;
import com.shopping.foundation.service.IPropertyRecordService;
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
 * Created by dyp on 16-8-27.
 */
@Controller
public class PropertyRecordManageAction {

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private IUserConfigService userConfigService;

    @Autowired
    private IAreaService areaService;

    @Autowired
    private IPropertyRecordService recordService;

    @SecurityMapping(display = false, rsequence = 0, title = "物业表列表", value = "/admin/propertyRecord_list.htm*", rtype = "admin", rname = "物业管理", rcode = "propertyRecord_admin", rgroup = "运营")
    @RequestMapping({"/admin/propertyRecord_list.htm"})
    public ModelAndView propertySheet_list(HttpServletRequest request, HttpServletResponse response, String currentPage, String orderBy, String orderType, String userId, String account, String payStatus) {
        ModelAndView mv = new JModelAndView("admin/blue/propertyRecord_list.html",
                this.configService.getSysConfig(), this.userConfigService
                .getUserConfig(), 0, request, response);
        PropertySheetQueryObject qo = new PropertySheetQueryObject(currentPage, mv, orderBy, orderType);
        if (!CommUtil.null2String(userId).equals("")) {
            qo.addQuery("obj.userId.userName", new SysMap("userName", userId), "=");
        }
        if (!CommUtil.null2String(account).equals("")) {
            qo.addQuery("obj.account", new SysMap("account",
                    PackageStatus.getEnumFromString(CommUtil.null2String(account))), "=");
        }
        if (!CommUtil.null2String(payStatus).equals("")) {
            qo.addQuery("obj.payStatus", new SysMap("payStatus",
                    PayStatus.getEnumFromString(CommUtil.null2String(payStatus))), "=");
        }

        IPageList pList = this.recordService.list(qo);
        CommUtil.saveIPageList2ModelAndView("", "", "", pList, mv);
        return mv;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "物业详情", value = "/admin/propertyRecord_view.htm*", rtype = "admin", rname = "物业管理", rcode = "propertyRecord_admin", rgroup = "运营")
    @RequestMapping({"/admin/propertyRecord_view.htm"})
    public ModelAndView propertyRecord_view(HttpServletRequest request, HttpServletResponse response, String id) {
        ModelAndView mv = new JModelAndView("admin/blue/propertyRecord_view.html",
                this.configService.getSysConfig(), this.userConfigService
                .getUserConfig(), 0, request, response);
        PropertyRecord obj = this.recordService.getObjById(CommUtil.null2Long(id));
        mv.addObject("obj", obj);
        return mv;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "物业编辑", value = "/admin/propertyRecord_edit.htm*", rtype = "admin", rname = "物业管理", rcode = "propertyRecord_admin", rgroup = "运营")
    @RequestMapping({"/admin/propertyRecord_edit.htm"})
    public ModelAndView propertyRecord_edit(HttpServletRequest request, HttpServletResponse response, String id, String currentPage) {
        ModelAndView mv = new JModelAndView("admin/blue/propertyRecord_edit.html",
                this.configService.getSysConfig(), this.userConfigService
                .getUserConfig(), 0, request, response);
        if ((id != null) && (!id.equals(""))) {
            PropertyRecord propertyRecord = this.recordService.getObjById(
                    Long.valueOf(Long.parseLong(id)));
            mv.addObject("obj", propertyRecord);
            mv.addObject("currentPage", currentPage);
            mv.addObject("edit", Boolean.valueOf(true));
        }
        List areas = this.areaService.query("select obj from Area obj where obj.parent.id is null", null, -1, -1);
        mv.addObject("areas", areas);
        return mv;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "物业添加", value = "/admin/propertyRecord_add.htm*", rtype = "admin", rname = "物业管理", rcode = "propertyRecord_admin", rgroup = "运营")
    @RequestMapping({"/admin/propertyRecord_add.htm"})
    public ModelAndView propertySheet_add(HttpServletRequest request, HttpServletResponse response, String currentPage) {
        ModelAndView mv = new JModelAndView("admin/blue/propertyRecord_add.html",
                this.configService.getSysConfig(), this.userConfigService
                .getUserConfig(), 0, request, response);
        List areas = this.areaService.query("select obj from Area obj where obj.parent.id is null", null, -1, -1);
        mv.addObject("areas", areas);
        mv.addObject("currentPage", currentPage);
        return mv;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "物业添加", value = "/admin/propertyRecord_new.htm*", rtype = "admin", rname = "物业管理", rcode = "propertyRecord_admin", rgroup = "运营")
    @RequestMapping({"/admin/propertyRecord_new.htm"})
    public ModelAndView tenement_new(HttpServletRequest request, HttpServletResponse response, String currentPage, String orderBy, String orderType, String paymentTime, String payStatus, String detailedPayment, String createTime, String totalCost, String userId, String account) throws ParseException {
        ModelAndView mv = new JModelAndView("admin/blue/propertyRecord_list.html",
                this.configService.getSysConfig(), this.userConfigService
                .getUserConfig(), 0, request, response);
        PropertyRecord record = new PropertyRecord();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (null != paymentTime && !paymentTime.equals("")) {
            Date datePaymentTime = sdf.parse(paymentTime);
            record.setPaymentTime(datePaymentTime);
        }
        if (null != createTime && !createTime.equals("")) {
            Date parseCreateTime = sdf.parse(createTime);
            record.setCreateTime(parseCreateTime);
        }
        if (null != payStatus && !payStatus.equals("")) {
            record.setPayStatus(PayStatus.getEnumFromString(payStatus));
        }
        if (null != detailedPayment && !detailedPayment.equals("")) {
            record.setDetailedPayment(detailedPayment);
        }
        if (null != totalCost && !totalCost.equals("")) {
            record.setTotalCost(Double.parseDouble(totalCost));
        }
        if (null != userId && !userId.equals("")) {
            User user = new User();
            user.setUserName(userId);
            record.setUserId(user);
        }
        if (null != account && !account.equals("")) {
            record.setAccount(account);
        }
        record.setAddTime(new Date());
        PropertyRecordQueryObject qo = new PropertyRecordQueryObject(currentPage, mv, orderBy, orderType);
        this.recordService.save(record);
        IPageList pList = this.recordService.list(qo);
        CommUtil.saveIPageList2ModelAndView("", "", "", pList, mv);
        return mv;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "物业删除", value = "/admin/propertyRecord_delete.htm*", rtype = "admin", rname = "物业管理", rcode = "propertyRecord_admin", rgroup = "运营")
    @RequestMapping({"/admin/propertyRecord_delete.htm"})
    public String propertyRecord_delete(HttpServletRequest request, HttpServletResponse response, String mulitId, String currentPage) {
        String[] ids = mulitId.split(",");
        for (String id : ids) {
            if (!id.equals("")) {
                PropertyRecord record = this.recordService.getObjById(Long.parseLong(id));
                record.setUserId(null);
                this.recordService.delete(Long.valueOf(id));
            }
        }
        return "redirect:propertyRecord_list.htm?currentPage=" + currentPage;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "物业保存", value = "/admin/propertyRecord_save.htm*", rtype = "admin", rname = "物业管理", rcode = "propertyRecord_admin", rgroup = "运营")
    @RequestMapping({"/admin/propertyRecord_save.htm"})
    public String propertyRecord_save(HttpServletRequest request, HttpServletResponse response, String id, String currentPage, String paymentTime, String payStatus, String detailedPayment, String createTime, String totalCost, String userId, String account) throws ParseException {
        PropertyRecord record = this.recordService.getObjById(Long.parseLong(id));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (null != paymentTime && !paymentTime.equals("")) {
            Date datePaymentTime = sdf.parse(paymentTime);
            record.setPaymentTime(datePaymentTime);
        }
        if (null != createTime && !createTime.equals("")) {
            Date parseCreateTime = sdf.parse(createTime);
            record.setCreateTime(parseCreateTime);
        }
        if (null != payStatus && !payStatus.equals("")) {
            record.setPayStatus(PayStatus.getEnumFromString(payStatus));
        }
        if (null != detailedPayment && !detailedPayment.equals("")) {
            record.setDetailedPayment(detailedPayment);
        }
        if (null != totalCost && !totalCost.equals("")) {
            record.setTotalCost(Double.parseDouble(totalCost));
        }
        if (null != userId && !userId.equals("")) {
            User user = new User();
            user.setUserName(userId);
            record.setUserId(user);
        }
        if (null != account && !account.equals("")) {
            record.setAccount(account);
        }
        this.recordService.save(record);
        return "redirect:propertyRecord_list.htm?currentPage=" + currentPage;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "物业导出表格", value = "/admin/propertyRecord_export_excel.htm*", rtype = "admin", rname = "物业管理", rcode = "propertyRecord_admin", rgroup = "运营")
    @RequestMapping({"/admin/propertyRecord_export_excel.htm"})
    public void propertyRecord_export_excel(HttpServletRequest request, HttpServletResponse response, String currentPage, String orderBy, String orderType, String user, String userId, String propertyName,String payStatus) throws IOException {
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
        cell.setCellValue("缴费时间");
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellValue("缴费状态");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("缴费详细");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellValue("创建时间");
        cell.setCellStyle(style);

        cell = row.createCell(5);
        cell.setCellValue("总费用");
        cell.setCellStyle(style);

        cell = row.createCell(6);
        cell.setCellValue("用户");
        cell.setCellStyle(style);

        cell = row.createCell(7);
        cell.setCellValue("用户账户");
        cell.setCellStyle(style);

        PropertyRecordQueryObject qo = new PropertyRecordQueryObject();
        if (!CommUtil.null2String(userId).equals("")) {
            qo.addQuery("obj.userId.userName", new SysMap("userName", userId), "=");
        }
        if (!CommUtil.null2String(propertyName).equals("")) {
            qo.addQuery("obj.account", new SysMap("account",
                    PackageStatus.getEnumFromString(CommUtil.null2String(propertyName))), "=");
        }
        if (!CommUtil.null2String(payStatus).equals("")) {
            qo.addQuery("obj.payStatus", new SysMap("payStatus",
                    PayStatus.getEnumFromString(CommUtil.null2String(payStatus))), "=");
        }
        IPageList pList = recordService.list(qo);
        List<PropertyRecord> lists = pList.getResult();
        if (null != lists) {
            for (int i = 0; i < lists.size(); i++) {
                row = sheet.createRow((int) i + 1);
                PropertyRecord record = lists.get(i);
                row.createCell(0).setCellValue(record.getId());
                row.createCell(1).setCellValue(record.getPaymentTime().toString());
                row.createCell(2).setCellValue(record.getPayStatus().getName());
                row.createCell(3).setCellValue(record.getDetailedPayment());
                row.createCell(4).setCellValue(record.getCreateTime().toString());
                row.createCell(5).setCellValue(record.getTotalCost());
                row.createCell(6).setCellValue(record.getUserId().getUserName());
                row.createCell(7).setCellValue(record.getAccount());
            }
        }
        if (null == lists) {
            for (int i = 0; i < 8; i++) {
                row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue("");
                row.createCell(1).setCellValue("");
                row.createCell(2).setCellValue("");
                row.createCell(3).setCellValue("");
                row.createCell(4).setCellValue("");
                row.createCell(5).setCellValue("");
                row.createCell(6).setCellValue("");
                row.createCell(7).setCellValue("");
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
