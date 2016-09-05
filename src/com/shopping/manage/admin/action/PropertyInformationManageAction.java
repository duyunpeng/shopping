package com.shopping.manage.admin.action;

import com.shopping.core.annotation.SecurityMapping;
import com.shopping.core.domain.virtual.SysMap;
import com.shopping.core.mv.JModelAndView;
import com.shopping.core.query.support.IPageList;
import com.shopping.core.tools.CommUtil;
import com.shopping.foundation.domain.PropertyInformation;
import com.shopping.foundation.domain.PropertySheet;
import com.shopping.foundation.domain.User;
import com.shopping.foundation.domain.query.PropertyInformationQueryObject;
import com.shopping.foundation.service.IPropertyInformationService;
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
public class PropertyInformationManageAction {
    @Autowired
    private ISysConfigService configService;

    @Autowired
    private IUserConfigService userConfigService;

    @Autowired
    private IPropertyInformationService informationService;

    @SecurityMapping(display = false, rsequence = 0, title = "物业表列表", value = "/admin/propertyInformation_list.htm*", rtype = "admin", rname = "物业管理", rcode = "propertyInformation_admin", rgroup = "运营")
    @RequestMapping({"/admin/propertyInformation_list.htm"})
    public ModelAndView propertyInformation_list(HttpServletRequest request, HttpServletResponse response, String currentPage, String orderBy, String orderType, String userId, String informationName) {
        ModelAndView mv = new JModelAndView("admin/blue/propertyInformation_list.html",
                this.configService.getSysConfig(), this.userConfigService
                .getUserConfig(), 0, request, response);
        PropertyInformationQueryObject qo = new PropertyInformationQueryObject(currentPage, mv, orderBy, orderType);
        if (!CommUtil.null2String(userId).equals("")) {
            qo.addQuery("obj.userId.userName", new SysMap("userName", userId), "=");
        }
        if (!CommUtil.null2String(informationName).equals("")) {
            qo.addQuery("obj.informationName.propertyName", new SysMap("propertyName", informationName), "=");
        }

        IPageList pList = this.informationService.list(qo);
        CommUtil.saveIPageList2ModelAndView("", "", "", pList, mv);
        return mv;
    }


    @SecurityMapping(display = false, rsequence = 0, title = "物业详情", value = "/admin/propertyInformation_view.htm*", rtype = "admin", rname = "物业管理", rcode = "propertyInformation_admin", rgroup = "运营")
    @RequestMapping({"/admin/propertyInformation_view.htm"})
    public ModelAndView propertyInformation_view(HttpServletRequest request, HttpServletResponse response, String id) {
        ModelAndView mv = new JModelAndView("admin/blue/propertyInformation_view.html",
                this.configService.getSysConfig(), this.userConfigService
                .getUserConfig(), 0, request, response);
        PropertyInformation obj = this.informationService.getObjById(CommUtil.null2Long(id));
        mv.addObject("obj", obj);
        return mv;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "物业添加", value = "/admin/propertyInformation_add.htm*", rtype = "admin", rname = "物业管理", rcode = "propertyInformation_admin", rgroup = "运营")
    @RequestMapping({"/admin/propertyInformation_add.htm"})
    public ModelAndView propertyInformation_add(HttpServletRequest request, HttpServletResponse response, String currentPage) {
        ModelAndView mv = new JModelAndView("admin/blue/propertyInformation_add.html",
                this.configService.getSysConfig(), this.userConfigService
                .getUserConfig(), 0, request, response);
        mv.addObject("currentPage", currentPage);
        return mv;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "物业编辑", value = "/admin/propertyInformation_edit.htm*", rtype = "admin", rname = "物业管理", rcode = "propertyInformation_admin", rgroup = "运营")
    @RequestMapping({"/admin/propertyInformation_edit.htm"})
    public ModelAndView propertyInformation_edit(HttpServletRequest request, HttpServletResponse response, String id, String currentPage) {
        ModelAndView mv = new JModelAndView("admin/blue/propertyInformation_edit.html",
                this.configService.getSysConfig(), this.userConfigService
                .getUserConfig(), 0, request, response);
        if ((id != null) && (!id.equals(""))) {
            PropertyInformation information = this.informationService.getObjById(
                    Long.valueOf(Long.parseLong(id)));
            mv.addObject("obj", information);
            mv.addObject("currentPage", currentPage);
            mv.addObject("edit", Boolean.valueOf(true));
        }
        return mv;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "物业删除", value = "/admin/propertyInformation_delete.htm*", rtype = "admin", rname = "物业管理", rcode = "propertyInformation_admin", rgroup = "运营")
    @RequestMapping({"/admin/propertyInformation_delete.htm"})
    public String propertyInformation_delete(HttpServletRequest request, HttpServletResponse response, String mulitId, String currentPage) {
        String[] ids = mulitId.split(",");
        for (String id : ids) {
            if (!id.equals("")) {
                PropertyInformation information = this.informationService.getObjById(Long.parseLong(id));
                information.setInformationName(null);
                information.setUserId(null);
                this.informationService.delete(Long.valueOf(id));
            }
        }
        return "redirect:propertyInformation_list.htm?currentPage=" + currentPage;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "物业保存", value = "/admin/propertyInformation_save.htm*", rtype = "admin", rname = "物业管理", rcode = "propertyInformation_admin", rgroup = "运营")
    @RequestMapping({"/admin/propertyInformation_save.htm"})
    public String propertyInformation_save(HttpServletRequest request, HttpServletResponse response, String id, String currentPage, String userId, String informationName, String messageHeader, String propertyAnnouncementContent, String informationTime) throws ParseException {
        if (!id.equals("")) {
            PropertyInformation information = this.informationService.getObjById(Long.parseLong(id));
            if (null != userId && !userId.equals("")) {
                User user = new User();
                user.setUserName(userId);
                information.setUserId(user);
            }
            if (null != informationName && !informationName.equals("")) {
                PropertySheet sheet = new PropertySheet();
                sheet.setPropertyName(informationName);
                information.setInformationName(sheet);
            }
            if (null != messageHeader && !messageHeader.equals("")) {
                information.setMessageHeader(messageHeader);
            }
            if (null != propertyAnnouncementContent && !propertyAnnouncementContent.equals("")) {
                information.setPropertyAnnouncementContent(propertyAnnouncementContent);
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (null != informationTime && !informationTime.equals("")) {
                Date parse = sdf.parse(informationTime);
                information.setInformationTime(parse);
            }
            this.informationService.save(information);
        }
        return "redirect:propertyInformation_list.htm?currentPage=" + currentPage;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "物业添加", value = "/admin/propertyInformation_new.htm*", rtype = "admin", rname = "物业管理", rcode = "propertyInformation_admin", rgroup = "运营")
    @RequestMapping({"/admin/propertyInformation_new.htm"})
    public ModelAndView propertyAnnouncement_new(HttpServletRequest request, HttpServletResponse response, String currentPage, String orderBy, String orderType, String userId, String informationName, String messageHeader, String propertyAnnouncementContent, String informationTime) throws ParseException {
        ModelAndView mv = new JModelAndView("admin/blue/propertyInformation_list.html",
                this.configService.getSysConfig(), this.userConfigService
                .getUserConfig(), 0, request, response);
        PropertyInformation information = new PropertyInformation();
        if (null != userId && !userId.equals("")) {
            User user = new User();
            user.setUserName(userId);
            information.setUserId(user);
        }
        if (null != informationName && !informationName.equals("")) {
            PropertySheet sheet = new PropertySheet();
            sheet.setPropertyName(informationName);
            information.setInformationName(sheet);
        }
        if (null != messageHeader && !messageHeader.equals("")) {
            information.setMessageHeader(messageHeader);
        }
        if (null != propertyAnnouncementContent && !propertyAnnouncementContent.equals("")) {
            information.setPropertyAnnouncementContent(propertyAnnouncementContent);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (null != informationTime && !informationTime.equals("")) {
            Date parse = sdf.parse(informationTime);
            information.setInformationTime(parse);
        }
        information.setAddTime(new Date());
        this.informationService.save(information);
        PropertyInformationQueryObject qo = new PropertyInformationQueryObject(currentPage, mv, orderBy, orderType);
        IPageList pList = this.informationService.list(qo);
        CommUtil.saveIPageList2ModelAndView("", "", "", pList, mv);
        return mv;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "物业导出表格", value = "/admin/propertyInformation_export_excel.htm*", rtype = "admin", rname = "物业管理", rcode = "propertyInformation_admin", rgroup = "运营")
    @RequestMapping({"/admin/propertyInformation_export_excel.htm"})
    public void propertyInformation_export_excel(HttpServletRequest request, HttpServletResponse response, String currentPage, String orderBy, String orderType, String userId, String informationName) throws IOException {
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
        cell.setCellValue("物业名称");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("消息标题");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellValue("物业公告内容");
        cell.setCellStyle(style);

        cell = row.createCell(5);
        cell.setCellValue("公告时间");
        cell.setCellStyle(style);

        PropertyInformationQueryObject qo = new PropertyInformationQueryObject();
        if (!CommUtil.null2String(userId).equals("")) {
            qo.addQuery("obj.userId.userName", new SysMap("userName", userId), "=");
        }
        if (!CommUtil.null2String(informationName).equals("")) {
            qo.addQuery("obj.informationName.propertyName", new SysMap("propertyName", informationName), "=");
        }
        IPageList pList = informationService.list(qo);
        List<PropertyInformation> lists = pList.getResult();
        if (null != lists) {
            for (int i = 0; i < lists.size(); i++) {
                row = sheet.createRow((int) i + 1);
                PropertyInformation information = lists.get(i);
                row.createCell(0).setCellValue(information.getId());
                row.createCell(1).setCellValue(information.getUserId().getUserName());
                row.createCell(2).setCellValue(information.getInformationName().getPropertyName());
                row.createCell(3).setCellValue(information.getMessageHeader());
                row.createCell(4).setCellValue(information.getPropertyAnnouncementContent());
                row.createCell(5).setCellValue(information.getInformationTime().toString());
            }
        }
        if (null == lists) {
            for (int i = 0; i < 6; i++) {
                row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue("");
                row.createCell(1).setCellValue("");
                row.createCell(2).setCellValue("");
                row.createCell(3).setCellValue("");
                row.createCell(4).setCellValue("");
                row.createCell(5).setCellValue("");
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
