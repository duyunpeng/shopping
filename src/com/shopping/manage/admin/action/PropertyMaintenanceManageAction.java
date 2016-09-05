package com.shopping.manage.admin.action;

import com.shopping.core.annotation.SecurityMapping;
import com.shopping.core.domain.virtual.SysMap;
import com.shopping.core.enums.DealStatus;
import com.shopping.core.enums.DeleteStatus;
import com.shopping.core.enums.RepairStatus;
import com.shopping.core.mv.JModelAndView;
import com.shopping.core.query.support.IPageList;
import com.shopping.core.tools.CommUtil;
import com.shopping.foundation.domain.PropertyMaintenance;
import com.shopping.foundation.domain.User;
import com.shopping.foundation.domain.query.PropertyMaintenanceQueryObject;
import com.shopping.foundation.service.IPropertyMaintenanceService;
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
 * Created by dyp on 2016/8/30.
 */
@Controller
public class PropertyMaintenanceManageAction {
    @Autowired
    private ISysConfigService configService;

    @Autowired
    private IUserConfigService userConfigService;

    @Autowired
    private IPropertyMaintenanceService maintenanceService;

    @SecurityMapping(display = false, rsequence = 0, title = "物业表列表", value = "/admin/propertyMaintenance_list.htm*", rtype = "admin", rname = "物业管理", rcode = "propertyMaintenance_admin", rgroup = "运营")
    @RequestMapping({"/admin/propertyMaintenance_list.htm"})
    public ModelAndView propertyMaintenance_list(HttpServletRequest request, HttpServletResponse response, String currentPage, String orderBy, String orderType, String userId, String repairStatus,String dealStatus,String maintenanceStatus) {
        ModelAndView mv = new JModelAndView("admin/blue/propertyMaintenance_list.html",
                this.configService.getSysConfig(), this.userConfigService
                .getUserConfig(), 0, request, response);
        PropertyMaintenanceQueryObject qo = new PropertyMaintenanceQueryObject(currentPage, mv, orderBy, orderType);
        if (!CommUtil.null2String(userId).equals("")) {
            qo.addQuery("obj.userId.userName", new SysMap("userName", userId), "=");
        }
        if (!CommUtil.null2String(repairStatus).equals("")) {
            qo.addQuery("obj.repairStatus", new SysMap("repairStatus",
                    RepairStatus.getEnumFromString(CommUtil.null2String(repairStatus))), "=");
        }
        if (!CommUtil.null2String(dealStatus).equals("")) {
            qo.addQuery("obj.dealStatus", new SysMap("dealStatus",
                    DealStatus.getEnumFromString(CommUtil.null2String(dealStatus))), "=");
        }
        if (!CommUtil.null2String(maintenanceStatus).equals("")) {
            qo.addQuery("obj.maintenanceStatus", new SysMap("maintenanceStatus",
                    DeleteStatus.getEnumFromString(CommUtil.null2String(maintenanceStatus))), "=");
        }
        IPageList pList = this.maintenanceService.list(qo);
        CommUtil.saveIPageList2ModelAndView("", "", "", pList, mv);
        return mv;
    }


    @SecurityMapping(display = false, rsequence = 0, title = "物业详情", value = "/admin/propertyMaintenance_view.htm*", rtype = "admin", rname = "物业管理", rcode = "propertyMaintenance_admin", rgroup = "运营")
    @RequestMapping({"/admin/propertyMaintenance_view.htm"})
    public ModelAndView propertyMaintenance_view(HttpServletRequest request, HttpServletResponse response, String id) {
        ModelAndView mv = new JModelAndView("admin/blue/propertyMaintenance_view.html",
                this.configService.getSysConfig(), this.userConfigService
                .getUserConfig(), 0, request, response);
        PropertyMaintenance obj = this.maintenanceService.getObjById(CommUtil.null2Long(id));
        mv.addObject("obj", obj);
        return mv;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "物业添加", value = "/admin/propertyMaintenance_add.htm*", rtype = "admin", rname = "物业管理", rcode = "propertyMaintenance_admin", rgroup = "运营")
    @RequestMapping({"/admin/propertyMaintenance_add.htm"})
    public ModelAndView propertyMaintenance_add(HttpServletRequest request, HttpServletResponse response, String currentPage) {
        ModelAndView mv = new JModelAndView("admin/blue/propertyMaintenance_add.html",
                this.configService.getSysConfig(), this.userConfigService
                .getUserConfig(), 0, request, response);
        mv.addObject("currentPage", currentPage);
        return mv;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "物业编辑", value = "/admin/propertyMaintenance_edit.htm*", rtype = "admin", rname = "物业管理", rcode = "propertyMaintenance_admin", rgroup = "运营")
    @RequestMapping({"/admin/propertyMaintenance_edit.htm"})
    public ModelAndView propertyMaintenance_edit(HttpServletRequest request, HttpServletResponse response, String id, String currentPage) {
        ModelAndView mv = new JModelAndView("admin/blue/propertyMaintenance_edit.html",
                this.configService.getSysConfig(), this.userConfigService
                .getUserConfig(), 0, request, response);
        if ((id != null) && (!id.equals(""))) {
            PropertyMaintenance maintenance = this.maintenanceService.getObjById(
                    Long.valueOf(Long.parseLong(id)));
            mv.addObject("obj", maintenance);
            mv.addObject("currentPage", currentPage);
            mv.addObject("edit", Boolean.valueOf(true));
        }
        return mv;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "物业删除", value = "/admin/propertyMaintenance_delete.htm*", rtype = "admin", rname = "物业管理", rcode = "propertyMaintenance_admin", rgroup = "运营")
    @RequestMapping({"/admin/propertyMaintenance_delete.htm"})
    public String propertyMaintenance_delete(HttpServletRequest request, HttpServletResponse response, String mulitId, String currentPage) {
        String[] ids = mulitId.split(",");
        for (String id : ids) {
            if (!id.equals("")) {
                PropertyMaintenance maintenance = this.maintenanceService.getObjById(Long.parseLong(id));
                maintenance.setUserId(null);
                this.maintenanceService.delete(Long.valueOf(id));
            }
        }
        return "redirect:propertyMaintenance_list.htm?currentPage=" + currentPage;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "物业保存", value = "/admin/propertyMaintenance_save.htm*", rtype = "admin", rname = "物业管理", rcode = "propertyMaintenance_admin", rgroup = "运营")
    @RequestMapping({"/admin/propertyMaintenance_save.htm"})
    public String propertyMaintenance_save(HttpServletRequest request, HttpServletResponse response, String id, String currentPage, String userId, String repairStatus, String title, String createTime, String dealTime, String dealStatus, String maintenanceStatus, String createAccount, String deleteAccount, String deleteTime) throws ParseException {
        if (!id.equals("")) {
            PropertyMaintenance maintenance = this.maintenanceService.getObjById(Long.parseLong(id));
            if (null != userId && !userId.equals("")) {
                User user = new User();
                user.setUserName(userId);
                maintenance.setUserId(user);
            }
            if (null != repairStatus && !repairStatus.equals("")) {
                maintenance.setRepairStatus(RepairStatus.getEnumFromString(repairStatus));
            }
            if (null != title && !title.equals("")) {
                maintenance.setTitle(title);
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (null != createTime && !createTime.equals("")) {
                Date create = sdf.parse(createTime);
                maintenance.setCreateTime(create);
            }
            if (null != deleteTime && !deleteTime.equals("")) {
                Date delete = sdf.parse(deleteTime);
                maintenance.setDeleteTime(delete);
            }
            if (null != dealTime && !dealTime.equals("")) {
                Date deal = sdf.parse(dealTime);
                maintenance.setDeleteTime(deal);
            }
            if (null != dealStatus && !dealStatus.equals("")) {
                maintenance.setDealStatus(DealStatus.getEnumFromString(dealStatus));
            }
            if (null != maintenanceStatus && !maintenanceStatus.equals("")) {
                maintenance.setMaintenanceStatus(DeleteStatus.getEnumFromString(maintenanceStatus));
            }
            if (null != createAccount && !createAccount.equals("")) {
                maintenance.setCreateAccount(createAccount);
            }
            if (null != deleteAccount && deleteAccount.equals("")) {
                maintenance.setDeleteAccount(deleteAccount);
            }
            this.maintenanceService.save(maintenance);
        }
        return "redirect:propertyMaintenance_list.htm?currentPage=" + currentPage;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "物业添加", value = "/admin/propertyMaintenance_new.htm*", rtype = "admin", rname = "物业管理", rcode = "propertyMaintenance_admin", rgroup = "运营")
    @RequestMapping({"/admin/propertyMaintenance_new.htm"})
    public ModelAndView propertyMaintenance_new(HttpServletRequest request, HttpServletResponse response, String currentPage, String orderBy, String orderType, String userId, String repairStatus, String title, String createTime, String dealTime, String dealStatus, String maintenanceStatus, String createAccount, String deleteAccount, String deleteTime) throws ParseException {
        ModelAndView mv = new JModelAndView("admin/blue/propertyMaintenance_list.html",
                this.configService.getSysConfig(), this.userConfigService
                .getUserConfig(), 0, request, response);
        PropertyMaintenance maintenance = new PropertyMaintenance();
        if (null != userId && !userId.equals("")) {
            User user = new User();
            user.setUserName(userId);
            maintenance.setUserId(user);
        }
        if (null != repairStatus && !repairStatus.equals("")) {
            maintenance.setRepairStatus(RepairStatus.getEnumFromString(repairStatus));
        }
        if (null != title && !title.equals("")) {
            maintenance.setTitle(title);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (null != createTime && !createTime.equals("")) {
            Date create = sdf.parse(createTime);
            maintenance.setCreateTime(create);
        }
        if (null != deleteTime && !deleteTime.equals("")) {
            Date delete = sdf.parse(deleteTime);
            maintenance.setDeleteTime(delete);
        }
        if (null != dealTime && !dealTime.equals("")) {
            Date deal = sdf.parse(dealTime);
            maintenance.setDeleteTime(deal);
        }
        if (null != dealStatus && !dealStatus.equals("")) {
            maintenance.setDealStatus(DealStatus.getEnumFromString(dealStatus));
        }
        if (null != maintenanceStatus && !maintenanceStatus.equals("")) {
            maintenance.setMaintenanceStatus(DeleteStatus.getEnumFromString(maintenanceStatus));
        }
        if (null != createAccount && !createAccount.equals("")) {
            maintenance.setCreateAccount(createAccount);
        }
        if (null != deleteAccount && deleteAccount.equals("")) {
            maintenance.setDeleteAccount(deleteAccount);
        }
        maintenance.setAddTime(new Date());
        this.maintenanceService.save(maintenance);
        PropertyMaintenanceQueryObject qo = new PropertyMaintenanceQueryObject(currentPage, mv, orderBy, orderType);
        IPageList pList = this.maintenanceService.list(qo);
        CommUtil.saveIPageList2ModelAndView("", "", "", pList, mv);
        return mv;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "物业导出表格", value = "/admin/propertyMaintenance_export_excel.htm*", rtype = "admin", rname = "物业管理", rcode = "propertyMaintenance_admin", rgroup = "运营")
    @RequestMapping({"/admin/propertyMaintenance_export_excel.htm"})
    public void propertyMaintenance_export_excel(HttpServletRequest request, HttpServletResponse response, String currentPage, String userId, String repairStatus,String dealStatus,String maintenanceStatus) throws IOException {
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
        cell.setCellValue("维修类别");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("标题");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellValue("创建时间");
        cell.setCellStyle(style);

        cell = row.createCell(5);
        cell.setCellValue("处理时间");
        cell.setCellStyle(style);

        cell = row.createCell(6);
        cell.setCellValue("处理状态");
        cell.setCellStyle(style);

        cell = row.createCell(7);
        cell.setCellValue("是否删除");
        cell.setCellStyle(style);

        cell = row.createCell(8);
        cell.setCellValue("创建账户");
        cell.setCellStyle(style);

        cell = row.createCell(9);
        cell.setCellValue("删除账户");
        cell.setCellStyle(style);

        cell = row.createCell(10);
        cell.setCellValue("删除时间");
        cell.setCellStyle(style);


        PropertyMaintenanceQueryObject qo = new PropertyMaintenanceQueryObject();
        if (!CommUtil.null2String(userId).equals("")) {
            qo.addQuery("obj.userId.userName", new SysMap("userName", userId), "=");
        }
        if (!CommUtil.null2String(repairStatus).equals("")) {
            qo.addQuery("obj.repairStatus", new SysMap("repairStatus",
                    RepairStatus.getEnumFromString(CommUtil.null2String(repairStatus))), "=");
        }
        if (!CommUtil.null2String(dealStatus).equals("")) {
            qo.addQuery("obj.dealStatus", new SysMap("dealStatus",
                    DealStatus.getEnumFromString(CommUtil.null2String(dealStatus))), "=");
        }
        if (!CommUtil.null2String(maintenanceStatus).equals("")) {
            qo.addQuery("obj.maintenanceStatus", new SysMap("maintenanceStatus",
                    DeleteStatus.getEnumFromString(CommUtil.null2String(maintenanceStatus))), "=");
        }
        IPageList pList = maintenanceService.list(qo);
        List<PropertyMaintenance> lists = pList.getResult();
        if (null != lists) {
            for (int i = 0; i < lists.size(); i++) {
                row = sheet.createRow((int) i + 1);
                PropertyMaintenance maintenance = lists.get(i);
                row.createCell(0).setCellValue(maintenance.getId());
                row.createCell(1).setCellValue(maintenance.getUserId().getUserName());
                row.createCell(2).setCellValue(maintenance.getRepairStatus().getName());
                row.createCell(3).setCellValue(maintenance.getTitle());
                row.createCell(4).setCellValue(maintenance.getCreateTime().toString());
                row.createCell(5).setCellValue(maintenance.getDealTime().toString());
                row.createCell(6).setCellValue(maintenance.getDealStatus().getName());
                row.createCell(7).setCellValue(maintenance.getMaintenanceStatus().toString());
                row.createCell(8).setCellValue(maintenance.getCreateAccount());
                row.createCell(9).setCellValue(maintenance.getDeleteAccount());
                row.createCell(10).setCellValue(maintenance.getDeleteTime().toString());
            }
        }
        if (null == lists) {
            for (int i = 0; i < 11; i++) {
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
