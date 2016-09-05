package com.shopping.manage.admin.action;

import com.shopping.core.annotation.SecurityMapping;
import com.shopping.core.domain.virtual.SysMap;
import com.shopping.core.enums.ConfirmStatus;
import com.shopping.core.enums.PayStatus;
import com.shopping.core.mv.JModelAndView;
import com.shopping.core.query.support.IPageList;
import com.shopping.core.tools.CommUtil;
import com.shopping.foundation.domain.PropertyFee;
import com.shopping.foundation.domain.User;
import com.shopping.foundation.domain.query.PropertyFeeQueryObject;
import com.shopping.foundation.service.IPropertyFeeService;
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
public class PropertyFeeManageAction {
    @Autowired
    private ISysConfigService configService;

    @Autowired
    private IUserConfigService userConfigService;

    @Autowired
    private IPropertyFeeService feeService;

    @SecurityMapping(display = false, rsequence = 0, title = "物业表列表", value = "/admin/propertyFee_list.htm*", rtype = "admin", rname = "物业管理", rcode = "propertyFee_admin", rgroup = "运营")
    @RequestMapping({"/admin/propertyFee_list.htm"})
    public ModelAndView propertyFee_list(HttpServletRequest request, HttpServletResponse response, String currentPage, String orderBy, String orderType, String userId, String payStatus,String confirmStatus) {
        ModelAndView mv = new JModelAndView("admin/blue/propertyFee_list.html",
                this.configService.getSysConfig(), this.userConfigService
                .getUserConfig(), 0, request, response);
        PropertyFeeQueryObject qo = new PropertyFeeQueryObject(currentPage, mv, orderBy, orderType);
        if (!CommUtil.null2String(userId).equals("")) {
            qo.addQuery("obj.userId.userName", new SysMap("userName", userId), "=");
        }
        if (!CommUtil.null2String(payStatus).equals("")) {
            qo.addQuery("obj.payStatus", new SysMap("payStatus",
                    PayStatus.getEnumFromString(CommUtil.null2String(payStatus))), "=");
        }
        if (!CommUtil.null2String(confirmStatus).equals("")) {
            qo.addQuery("obj.confirmStatus", new SysMap("confirmStatus",
                    ConfirmStatus.getEnumFromString(CommUtil.null2String(confirmStatus))), "=");
        }
        IPageList pList = this.feeService.list(qo);
        CommUtil.saveIPageList2ModelAndView("", "", "", pList, mv);
        return mv;
    }


    @SecurityMapping(display = false, rsequence = 0, title = "物业详情", value = "/admin/propertyFee_view.htm*", rtype = "admin", rname = "物业管理", rcode = "propertyFee_admin", rgroup = "运营")
    @RequestMapping({"/admin/propertyFee_view.htm"})
    public ModelAndView propertyFee_view(HttpServletRequest request, HttpServletResponse response, String id) {
        ModelAndView mv = new JModelAndView("admin/blue/propertyFee_view.html",
                this.configService.getSysConfig(), this.userConfigService
                .getUserConfig(), 0, request, response);
        PropertyFee obj = this.feeService.getObjById(CommUtil.null2Long(id));
        mv.addObject("obj", obj);
        return mv;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "物业添加", value = "/admin/propertyFee_add.htm*", rtype = "admin", rname = "物业管理", rcode = "propertyFee_admin", rgroup = "运营")
    @RequestMapping({"/admin/propertyFee_add.htm"})
    public ModelAndView propertyFee_add(HttpServletRequest request, HttpServletResponse response, String currentPage) {
        ModelAndView mv = new JModelAndView("admin/blue/propertyFee_add.html",
                this.configService.getSysConfig(), this.userConfigService
                .getUserConfig(), 0, request, response);
        mv.addObject("currentPage", currentPage);
        return mv;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "物业编辑", value = "/admin/propertyFee_edit.htm*", rtype = "admin", rname = "物业管理", rcode = "propertyFee_admin", rgroup = "运营")
    @RequestMapping({"/admin/propertyFee_edit.htm"})
    public ModelAndView propertyFee_edit(HttpServletRequest request, HttpServletResponse response, String id, String currentPage) {
        ModelAndView mv = new JModelAndView("admin/blue/propertyFee_edit.html",
                this.configService.getSysConfig(), this.userConfigService
                .getUserConfig(), 0, request, response);
        if ((id != null) && (!id.equals(""))) {
            PropertyFee fee = this.feeService.getObjById(
                    Long.valueOf(Long.parseLong(id)));
            mv.addObject("obj", fee);
            mv.addObject("currentPage", currentPage);
            mv.addObject("edit", Boolean.valueOf(true));
        }
        return mv;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "物业删除", value = "/admin/propertyFee_delete.htm*", rtype = "admin", rname = "物业管理", rcode = "propertyFee_admin", rgroup = "运营")
    @RequestMapping({"/admin/propertyFee_delete.htm"})
    public String propertyFee_delete(HttpServletRequest request, HttpServletResponse response, String mulitId, String currentPage) {
        String[] ids = mulitId.split(",");
        for (String id : ids) {
            if (!id.equals("")) {
                PropertyFee fee = this.feeService.getObjById(Long.parseLong(id));
                fee.setUserId(null);
                this.feeService.delete(Long.valueOf(id));
            }
        }
        return "redirect:propertyFee_list.htm?currentPage=" + currentPage;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "物业保存", value = "/admin/propertyFee_save.htm*", rtype = "admin", rname = "物业管理", rcode = "propertyFee_admin", rgroup = "运营")
    @RequestMapping({"/admin/propertyFee_save.htm"})
    public String propertyFee_save(HttpServletRequest request, HttpServletResponse response, String id, String currentPage, String userId, String payStatus, String createTime, String account, String createPerson, String totalCost, String paidFee, String confirmStatus) throws ParseException {
        if (!id.equals("")) {
            PropertyFee fee = this.feeService.getObjById(Long.parseLong(id));
            if (null != userId && !userId.equals("")) {
                User user = new User();
                user.setUserName(userId);
                fee.setUserId(user);
            }
            if (null != payStatus && !payStatus.equals("")) {
                fee.setPayStatus(PayStatus.getEnumFromString(payStatus));
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (null != createTime && !createTime.equals("")) {
                Date create = sdf.parse(createTime);
                fee.setCreateTime(create);
            }
            if (null != account && !account.equals("")) {
                fee.setAccount(account);
            }
            if (null != createPerson && !createPerson.equals("")) {
                fee.setCreatePerson(createPerson);
            }
            if (null != totalCost && !totalCost.equals("")) {
                fee.setTotalCost(Double.parseDouble(totalCost));
            }
            if (null != paidFee && !paidFee.equals("")) {
                fee.setPaidFee(Double.parseDouble(paidFee));
            }
            if (null != confirmStatus && !confirmStatus.equals("")) {
                fee.setConfirmStatus(ConfirmStatus.getEnumFromString(confirmStatus));
            }
            this.feeService.save(fee);
        }
        return "redirect:propertyFee_list.htm?currentPage=" + currentPage;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "物业添加", value = "/admin/propertyFee_new.htm*", rtype = "admin", rname = "物业管理", rcode = "propertyFee_admin", rgroup = "运营")
    @RequestMapping({"/admin/propertyFee_new.htm"})
    public ModelAndView propertyFee_new(HttpServletRequest request, HttpServletResponse response, String currentPage, String orderBy, String orderType, String userId, String payStatus, String createTime, String account, String createPerson, String totalCost, String paidFee, String confirmStatus) throws ParseException {
        ModelAndView mv = new JModelAndView("admin/blue/propertyFee_list.html",
                this.configService.getSysConfig(), this.userConfigService
                .getUserConfig(), 0, request, response);
        PropertyFee fee = new PropertyFee();
        if (null != userId && !userId.equals("")) {
            User user = new User();
            user.setUserName(userId);
            fee.setUserId(user);
        }
        if (null != payStatus && !payStatus.equals("")) {
            fee.setPayStatus(PayStatus.getEnumFromString(payStatus));
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (null != createTime && !createTime.equals("")) {
            Date create = sdf.parse(createTime);
            fee.setCreateTime(create);
        }
        if (null != account && !account.equals("")) {
            fee.setAccount(account);
        }
        if (null != createPerson && !createPerson.equals("")) {
            fee.setCreatePerson(createPerson);
        }
        if (null != totalCost && !totalCost.equals("")) {
            fee.setTotalCost(Double.parseDouble(totalCost));
        }
        if (null != paidFee && !paidFee.equals("")) {
            fee.setPaidFee(Double.parseDouble(paidFee));
        }
        if (null != confirmStatus && !confirmStatus.equals("")) {
            fee.setConfirmStatus(ConfirmStatus.getEnumFromString(confirmStatus));
        }
        fee.setAddTime(new Date());
        this.feeService.save(fee);
        PropertyFeeQueryObject qo = new PropertyFeeQueryObject(currentPage, mv, orderBy, orderType);
        IPageList pList = this.feeService.list(qo);
        CommUtil.saveIPageList2ModelAndView("", "", "", pList, mv);
        return mv;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "物业导出表格", value = "/admin/propertyFee_export_excel.htm*", rtype = "admin", rname = "物业管理", rcode = "propertyFee_admin", rgroup = "运营")
    @RequestMapping({"/admin/propertyFee_export_excel.htm"})
    public void propertyFee_export_excel(HttpServletRequest request, HttpServletResponse response, String currentPage, String orderBy, String orderType, String userId,String payStatus,String confirmStatus) throws IOException {
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
        cell.setCellValue("用户");
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellValue("缴费状态");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("创建时间");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellValue("创建账户");
        cell.setCellStyle(style);

        cell = row.createCell(5);
        cell.setCellValue("创建人");
        cell.setCellStyle(style);

        cell = row.createCell(6);
        cell.setCellValue("总费用");
        cell.setCellStyle(style);

        cell = row.createCell(7);
        cell.setCellValue("已交费用");
        cell.setCellStyle(style);

        cell = row.createCell(8);
        cell.setCellValue("是否确认");
        cell.setCellStyle(style);

        PropertyFeeQueryObject qo = new PropertyFeeQueryObject();
        if (!CommUtil.null2String(userId).equals("")) {
            qo.addQuery("obj.userId.userName", new SysMap("userName", userId), "=");
        }
        if (!CommUtil.null2String(payStatus).equals("")) {
            qo.addQuery("obj.payStatus", new SysMap("payStatus",
                    PayStatus.getEnumFromString(CommUtil.null2String(payStatus))), "=");
        }
        if (!CommUtil.null2String(confirmStatus).equals("")) {
            qo.addQuery("obj.confirmStatus", new SysMap("confirmStatus",
                    ConfirmStatus.getEnumFromString(CommUtil.null2String(confirmStatus))), "=");
        }
        IPageList pList = feeService.list(qo);
        List<PropertyFee> lists = pList.getResult();
        if (null != lists) {
            for (int i = 0; i < lists.size(); i++) {
                row = sheet.createRow((int) i + 1);
                PropertyFee fee = lists.get(i);
                row.createCell(0).setCellValue(fee.getId());
                row.createCell(1).setCellValue(fee.getUserId().getUserName());
                row.createCell(2).setCellValue(fee.getPayStatus().getName());
                row.createCell(3).setCellValue(fee.getCreateTime().toString());
                row.createCell(4).setCellValue(fee.getAccount());
                row.createCell(5).setCellValue(fee.getCreatePerson());
                row.createCell(6).setCellValue(fee.getTotalCost());
                row.createCell(7).setCellValue(fee.getPaidFee());
                row.createCell(8).setCellValue(fee.getConfirmStatus().getName());
            }
        }
        if (null == lists) {
            for (int i = 0; i < 9; i++) {
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
