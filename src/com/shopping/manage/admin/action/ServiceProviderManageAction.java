package com.shopping.manage.admin.action;

import com.shopping.core.annotation.SecurityMapping;
import com.shopping.core.domain.virtual.SysMap;
import com.shopping.core.enums.DeleteStatus;
import com.shopping.core.mv.JModelAndView;
import com.shopping.core.query.support.IPageList;
import com.shopping.core.tools.CommUtil;
import com.shopping.foundation.domain.Area;
import com.shopping.foundation.domain.ServiceProvider;
import com.shopping.foundation.domain.User;
import com.shopping.foundation.domain.query.ServiceProviderQueryObject;
import com.shopping.foundation.domain.query.TenementQueryObject;
import com.shopping.foundation.service.IAreaService;
import com.shopping.foundation.service.IServiceProviderService;
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
public class ServiceProviderManageAction {
    @Autowired
    private ISysConfigService configService;

    @Autowired
    private IUserConfigService userConfigService;

    @Autowired
    private IServiceProviderService providerService;

    @Autowired
    private IAreaService areaService;

    @SecurityMapping(display = false, rsequence = 0, title = "物业列表", value = "/admin/serviceProvider_list.htm*", rtype = "admin", rname = "物业管理", rcode = "serviceProvider_admin", rgroup = "运营")
    @RequestMapping({"/admin/serviceProvider_list.htm"})
    public ModelAndView serviceProvider_list(HttpServletRequest request, HttpServletResponse response, String currentPage, String orderBy, String orderType, String userId,String serviceProviderStatus) {
        ModelAndView mv = new JModelAndView("admin/blue/serviceProvider_list.html",
                this.configService.getSysConfig(), this.userConfigService
                .getUserConfig(), 0, request, response);
        TenementQueryObject qo = new TenementQueryObject(currentPage, mv, orderBy, orderType);
        if (!CommUtil.null2String(userId).equals("")) {
            qo.addQuery("obj.userId.userName", new SysMap("userName", userId), "=");
        }
        if (!CommUtil.null2String(serviceProviderStatus).equals("")) {
            qo.addQuery("obj.serviceProviderStatus", new SysMap("serviceProviderStatus",
                    DeleteStatus.getEnumFromString(CommUtil.null2String(serviceProviderStatus))), "=");
        }
        IPageList pList = this.providerService.list(qo);
        CommUtil.saveIPageList2ModelAndView("", "", "", pList, mv);
        return mv;
    }


    @SecurityMapping(display = false, rsequence = 0, title = "物业详情", value = "/admin/serviceProvider_view.htm*", rtype = "admin", rname = "物业管理", rcode = "serviceProvider_admin", rgroup = "运营")
    @RequestMapping({"/admin/serviceProvider_view.htm"})
    public ModelAndView serviceProvider_view(HttpServletRequest request, HttpServletResponse response, String id) {
        ModelAndView mv = new JModelAndView("admin/blue/serviceProvider_view.html",
                this.configService.getSysConfig(), this.userConfigService
                .getUserConfig(), 0, request, response);
        ServiceProvider obj = this.providerService.getObjById(CommUtil.null2Long(id));
        mv.addObject("obj", obj);
        return mv;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "物业添加", value = "/admin/serviceProvider_add.htm*", rtype = "admin", rname = "物业管理", rcode = "serviceProvider_admin", rgroup = "运营")
    @RequestMapping({"/admin/serviceProvider_add.htm"})
    public ModelAndView serviceProvider_add(HttpServletRequest request, HttpServletResponse response, String currentPage) {
        ModelAndView mv = new JModelAndView("admin/blue/serviceProvider_add.html",
                this.configService.getSysConfig(), this.userConfigService
                .getUserConfig(), 0, request, response);
        List areas = this.areaService.query("select obj from Area obj where obj.parent.id is null", null, -1, -1);
        mv.addObject("areas", areas);
        mv.addObject("currentPage", currentPage);

        return mv;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "物业添加", value = "/admin/serviceProvider_new.htm*", rtype = "admin", rname = "物业管理", rcode = "serviceProvider_admin", rgroup = "运营")
    @RequestMapping({"/admin/serviceProvider_new.htm"})
    public ModelAndView serviceProvider_new(HttpServletRequest request, HttpServletResponse response, String currentPage, String orderBy, String orderType, String userId, String areaId, String serviceName, String serviceContent, String createTime, String serviceProviderStatus, String deleteAccount, String deleteTime, String createAccount) throws ParseException {
        ModelAndView mv = new JModelAndView("admin/blue/serviceProvider_list.html",
                this.configService.getSysConfig(), this.userConfigService
                .getUserConfig(), 0, request, response);
        ServiceProvider provider = new ServiceProvider();
        if (null != userId && !userId.equals("")) {
            User user = new User();
            user.setUserName(userId);
            provider.setUserId(user);
        }
        if (null != areaId && !areaId.equals("")) {
            Area area = this.areaService.getObjById(Long.parseLong(areaId));
            provider.setAreaId(area);
        }
        if (null != serviceName && !serviceName.equals("")) {
            provider.setServiceName(serviceName);
        }
        if (null != serviceContent && !serviceContent.equals("")) {
            provider.setServiceContent(serviceContent);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (null != createTime && !createTime.equals("")) {
            Date create = sdf.parse(createTime);
            provider.setCreateTime(create);
        }
        if (null != deleteTime && !deleteTime.equals("")) {
            Date delete = sdf.parse(deleteTime);
            provider.setDeleteTime(delete);
        }
        if (null != serviceProviderStatus && !serviceProviderStatus.equals("")) {
            provider.setServiceProviderStatus(DeleteStatus.getEnumFromString(serviceProviderStatus));
        }
        if (null != deleteAccount && !deleteAccount.equals("")) {
            provider.setDeleteAccount(deleteAccount);
        }
        if (null != createAccount && !createAccount.equals("")) {
            provider.setCreateAccount(createAccount);
        }
        provider.setAddTime(new Date());
        this.providerService.save(provider);

        ServiceProviderQueryObject qo = new ServiceProviderQueryObject(currentPage, mv, orderBy, orderType);
        IPageList pList = this.providerService.list(qo);
        CommUtil.saveIPageList2ModelAndView("", "", "", pList, mv);
        return mv;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "物业删除", value = "/admin/serviceProvider_delete.htm*", rtype = "admin", rname = "物业管理", rcode = "serviceProvider_admin", rgroup = "运营")
    @RequestMapping({"/admin/serviceProvider_delete.htm"})
    public String serviceProvider_delete(HttpServletRequest request, HttpServletResponse response, String mulitId, String currentPage) {
        String[] ids = mulitId.split(",");
        for (String id : ids) {
            if (!id.equals("")) {
                ServiceProvider provider = this.providerService.getObjById(Long.parseLong(id));
                provider.setUserId(null);
                provider.setAreaId(null);
                this.providerService.delete(Long.valueOf(id));
            }
        }
        return "redirect:serviceProvider_list.htm?currentPage=" + currentPage;
    }


    @SecurityMapping(display = false, rsequence = 0, title = "物业编辑", value = "/admin/serviceProvider_edit.htm*", rtype = "admin", rname = "物业管理", rcode = "serviceProvider_admin", rgroup = "运营")
    @RequestMapping({"/admin/serviceProvider_edit.htm"})
    public ModelAndView serviceProvider_edit(HttpServletRequest request, HttpServletResponse response, String id, String currentPage) {
        ModelAndView mv = new JModelAndView("admin/blue/serviceProvider_edit.html",
                this.configService.getSysConfig(), this.userConfigService
                .getUserConfig(), 0, request, response);
        if ((id != null) && (!id.equals(""))) {
            ServiceProvider provider = this.providerService.getObjById(
                    Long.valueOf(Long.parseLong(id)));
            mv.addObject("obj", provider);
            mv.addObject("currentPage", currentPage);
            mv.addObject("edit", Boolean.valueOf(true));
        }
        List areas = this.areaService.query("select obj from Area obj where obj.parent.id is null", null, -1, -1);
        mv.addObject("areas", areas);
        return mv;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "物业保存", value = "/admin/serviceProvider_save.htm*", rtype = "admin", rname = "物业管理", rcode = "serviceProvider_admin", rgroup = "运营")
    @RequestMapping({"/admin/serviceProvider_save.htm"})
    public String serviceProvider_save(HttpServletRequest request, HttpServletResponse response, String id, String currentPage, String userId, String areaId, String serviceName, String serviceContent, String createTime, String serviceProviderStatus, String deleteAccount, String deleteTime, String createAccount) throws ParseException {
        if (!id.equals("")) {
            ServiceProvider provider = providerService.getObjById(Long.parseLong(id));
            if (null != userId && !userId.equals("")) {
                User user = new User();
                user.setUserName(userId);
                provider.setUserId(user);
            }
            if (null != areaId && !areaId.equals("")) {
                Area area = this.areaService.getObjById(Long.parseLong(areaId));
                provider.setAreaId(area);
            }
            if (null != serviceName && !serviceName.equals("")) {
                provider.setServiceName(serviceName);
            }
            if (null != serviceContent && !serviceContent.equals("")) {
                provider.setServiceContent(serviceContent);
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (null != createTime && !createTime.equals("")) {
                Date create = sdf.parse(createTime);
                provider.setCreateTime(create);
            }
            if (null != deleteTime && !deleteTime.equals("")) {
                Date delete = sdf.parse(deleteTime);
                provider.setDeleteTime(delete);
            }
            if (null != serviceProviderStatus && !serviceProviderStatus.equals("")) {
                provider.setServiceProviderStatus(DeleteStatus.getEnumFromString(serviceProviderStatus));
            }
            if (null != deleteAccount && !deleteAccount.equals("")) {
                provider.setDeleteAccount(deleteAccount);
            }
            if (null != createAccount && !createAccount.equals("")) {
                provider.setCreateAccount(createAccount);
            }
            this.providerService.save(provider);
        }
        return "redirect:serviceProvider_list.htm?currentPage=" + currentPage;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "物业导出表格", value = "/admin/serviceProvider_export_excel.htm*", rtype = "admin", rname = "物业管理", rcode = "serviceProvider_admin", rgroup = "运营")
    @RequestMapping({"/admin/serviceProvider_export_excel.htm"})
    public void serviceProvider_export_excel(HttpServletRequest request, HttpServletResponse response, String currentPage, String orderBy, String orderType, String userId,String serviceProviderStatus) throws IOException {
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("物业管理表");
        HSSFRow row = sheet.createRow((int) 0);
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);


        sheet.setDefaultColumnWidth(20);
        sheet.setDefaultRowHeight((short) 30);

        HSSFCell cell = row.createCell(0);
        cell.setCellValue("id编号");
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellValue("用户");
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellValue("小区");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("维修商名称");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellValue("维修内容");
        cell.setCellStyle(style);

        cell = row.createCell(5);
        cell.setCellValue("创建时间");
        cell.setCellStyle(style);

        cell = row.createCell(6);
        cell.setCellValue("是否删除");
        cell.setCellStyle(style);

        cell = row.createCell(7);
        cell.setCellValue("删除账户");
        cell.setCellStyle(style);

        cell = row.createCell(8);
        cell.setCellValue("删除时间");
        cell.setCellStyle(style);

        cell = row.createCell(9);
        cell.setCellValue("创建账户");
        cell.setCellStyle(style);

        ServiceProviderQueryObject qo = new ServiceProviderQueryObject();
        if (!CommUtil.null2String(userId).equals("")) {
            qo.addQuery("obj.userId.userName", new SysMap("userName", userId), "=");
        }
        if (!CommUtil.null2String(serviceProviderStatus).equals("")) {
            qo.addQuery("obj.serviceProviderStatus", new SysMap("serviceProviderStatus",
                    DeleteStatus.getEnumFromString(CommUtil.null2String(serviceProviderStatus))), "=");
        }
        IPageList pList = this.providerService.list(qo);
        List<ServiceProvider> lists = pList.getResult();
        if (null != lists) {
            for (int i = 0; i < lists.size(); i++) {
                row = sheet.createRow((int) i + 1);
                ServiceProvider provider = lists.get(i);
                row.createCell(0).setCellValue(provider.getId());
                row.createCell(1).setCellValue(provider.getUserId().getUserName());
                row.createCell(2).setCellValue(provider.getAreaId().getAreaName());
                row.createCell(3).setCellValue(provider.getServiceName());
                row.createCell(4).setCellValue(provider.getServiceContent());
                row.createCell(5).setCellValue(provider.getCreateTime().toString());
                row.createCell(6).setCellValue(provider.getServiceProviderStatus().getName());
                row.createCell(7).setCellValue(provider.getDeleteAccount());
                row.createCell(8).setCellValue(provider.getDeleteTime().toString());
                row.createCell(9).setCellValue(provider.getCreateAccount());

            }
        }
        if (null == lists) {
            for (int i = 0; i < 10; i++) {
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
