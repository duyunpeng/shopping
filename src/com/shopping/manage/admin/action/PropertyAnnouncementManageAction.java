package com.shopping.manage.admin.action;

import com.shopping.core.annotation.SecurityMapping;
import com.shopping.core.domain.virtual.SysMap;
import com.shopping.core.mv.JModelAndView;
import com.shopping.core.query.support.IPageList;
import com.shopping.core.tools.CommUtil;
import com.shopping.foundation.domain.PropertyAnnouncement;
import com.shopping.foundation.domain.PropertySheet;
import com.shopping.foundation.domain.query.PropertyAnnouncementQueryObject;
import com.shopping.foundation.domain.query.PropertySheetQueryObject;
import com.shopping.foundation.service.IPropertyAnnouncementService;
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
public class PropertyAnnouncementManageAction {
    @Autowired
    private ISysConfigService configService;

    @Autowired
    private IUserConfigService userConfigService;

    @Autowired
    private IPropertyAnnouncementService announcementService;

    @SecurityMapping(display = false, rsequence = 0, title = "物业表列表", value = "/admin/propertyAnnouncement_list.htm*", rtype = "admin", rname = "物业管理", rcode = "propertyAnnouncement_admin", rgroup = "运营")
    @RequestMapping({"/admin/propertyAnnouncement_list.htm"})
    public ModelAndView propertyAnnouncement_list(HttpServletRequest request, HttpServletResponse response, String currentPage, String orderBy, String orderType, String sheetName) {
        ModelAndView mv = new JModelAndView("admin/blue/propertyAnnouncement_list.html",
                this.configService.getSysConfig(), this.userConfigService
                .getUserConfig(), 0, request, response);
        PropertyAnnouncementQueryObject qo = new PropertyAnnouncementQueryObject(currentPage, mv, orderBy, orderType);
        if (!CommUtil.null2String(sheetName).equals("")) {
            qo.addQuery("obj.sheetName.propertyName", new SysMap("propertyName", sheetName), "=");
        }

        IPageList pList = this.announcementService.list(qo);
        CommUtil.saveIPageList2ModelAndView("", "", "", pList, mv);
        return mv;
    }


    @SecurityMapping(display = false, rsequence = 0, title = "物业详情", value = "/admin/propertyAnnouncement_view.htm*", rtype = "admin", rname = "物业管理", rcode = "propertyAnnouncement_admin", rgroup = "运营")
    @RequestMapping({"/admin/propertyAnnouncement_view.htm"})
    public ModelAndView propertyAnnouncement_view(HttpServletRequest request, HttpServletResponse response, String id) {
        ModelAndView mv = new JModelAndView("admin/blue/propertyAnnouncement_view.html",
                this.configService.getSysConfig(), this.userConfigService
                .getUserConfig(), 0, request, response);
        PropertyAnnouncement obj = this.announcementService.getObjById(CommUtil.null2Long(id));
        mv.addObject("obj", obj);
        return mv;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "物业添加", value = "/admin/propertyAnnouncement_add.htm*", rtype = "admin", rname = "物业管理", rcode = "propertyAnnouncement_admin", rgroup = "运营")
    @RequestMapping({"/admin/propertyAnnouncement_add.htm"})
    public ModelAndView propertySheet_add(HttpServletRequest request, HttpServletResponse response, String currentPage) {
        ModelAndView mv = new JModelAndView("admin/blue/propertyAnnouncement_add.html",
                this.configService.getSysConfig(), this.userConfigService
                .getUserConfig(), 0, request, response);
        mv.addObject("currentPage", currentPage);
        return mv;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "物业编辑", value = "/admin/propertyAnnouncement_edit.htm*", rtype = "admin", rname = "物业管理", rcode = "propertyAnnouncement_admin", rgroup = "运营")
    @RequestMapping({"/admin/propertyAnnouncement_edit.htm"})
    public ModelAndView propertyAnnouncement_edit(HttpServletRequest request, HttpServletResponse response, String id, String currentPage) {
        ModelAndView mv = new JModelAndView("admin/blue/propertyAnnouncement_edit.html",
                this.configService.getSysConfig(), this.userConfigService
                .getUserConfig(), 0, request, response);
        if ((id != null) && (!id.equals(""))) {
            PropertyAnnouncement announcement = this.announcementService.getObjById(
                    Long.valueOf(Long.parseLong(id)));
            mv.addObject("obj", announcement);
            mv.addObject("currentPage", currentPage);
            mv.addObject("edit", Boolean.valueOf(true));
        }
        return mv;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "物业删除", value = "/admin/propertyAnnouncement_delete.htm*", rtype = "admin", rname = "物业管理", rcode = "propertyAnnouncement_admin", rgroup = "运营")
    @RequestMapping({"/admin/propertyAnnouncement_delete.htm"})
    public String propertyAnnouncement_delete(HttpServletRequest request, HttpServletResponse response, String mulitId, String currentPage) {
        String[] ids = mulitId.split(",");
        for (String id : ids) {
            if (!id.equals("")) {
                PropertyAnnouncement announcement = this.announcementService.getObjById(Long.parseLong(id));
                announcement.setSheetName(null);
                this.announcementService.delete(Long.valueOf(id));
            }
        }
        return "redirect:propertyAnnouncement_list.htm?currentPage=" + currentPage;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "物业保存", value = "/admin/propertyAnnouncement_save.htm*", rtype = "admin", rname = "物业管理", rcode = "propertyAnnouncement_admin", rgroup = "运营")
    @RequestMapping({"/admin/propertyAnnouncement_save.htm"})
    public String propertyAnnouncement_save(HttpServletRequest request, HttpServletResponse response, String id, String currentPage, String sheetName, String noticeTitle, String noticeContent, String noticeTime) throws ParseException {
        if (!id.equals("")) {
            PropertyAnnouncement announcement = announcementService.getObjById(Long.parseLong(id));
            PropertySheet sheet = new PropertySheet();
            if (null != sheetName && !sheetName.equals("")) {
                sheet.setPropertyName(sheetName);
                announcement.setSheetName(sheet);
            }
            if (null != noticeTitle && !noticeTitle.equals("")) {
                announcement.setNoticeTitle(noticeTitle);
            }
            if (null != noticeContent && !noticeContent.equals("")) {
                announcement.setNoticeContent(noticeContent);
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (null != noticeTime && !noticeTime.equals("")) {
                Date parse = sdf.parse(noticeTime);
                announcement.setNoticeTime(parse);
            }
            this.announcementService.save(announcement);
        }
        return "redirect:propertyAnnouncement_list.htm?currentPage=" + currentPage;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "物业添加", value = "/admin/propertyAnnouncement_new.htm*", rtype = "admin", rname = "物业管理", rcode = "propertyAnnouncement_admin", rgroup = "运营")
    @RequestMapping({"/admin/propertyAnnouncement_new.htm"})
    public ModelAndView propertyAnnouncement_new(HttpServletRequest request, HttpServletResponse response, String currentPage, String orderBy, String orderType, String sheetName, String noticeTitle, String noticeContent, String noticeTime) throws ParseException {
        ModelAndView mv = new JModelAndView("admin/blue/propertyAnnouncement_list.html",
                this.configService.getSysConfig(), this.userConfigService
                .getUserConfig(), 0, request, response);
        PropertyAnnouncement announcement = new PropertyAnnouncement();
        PropertySheet sheet = new PropertySheet();
        if (null != sheetName && !sheetName.equals("")) {
            sheet.setPropertyName(sheetName);
            announcement.setSheetName(sheet);
        }
        if (null != noticeTitle && !noticeTitle.equals("")) {
            announcement.setNoticeTitle(noticeTitle);
        }
        if (null != noticeContent && !noticeContent.equals("")) {
            announcement.setNoticeContent(noticeContent);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (null != noticeTime && !noticeTime.equals("")) {
            Date parse = sdf.parse(noticeTime);
            announcement.setNoticeTime(parse);
        }
        announcement.setAddTime(new Date());
        this.announcementService.save(announcement);
        PropertySheetQueryObject qo = new PropertySheetQueryObject(currentPage, mv, orderBy, orderType);
        IPageList pList = this.announcementService.list(qo);
        CommUtil.saveIPageList2ModelAndView("", "", "", pList, mv);
        return mv;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "物业导出表格", value = "/admin/propertyAnnouncement_export_excel.htm*", rtype = "admin", rname = "物业管理", rcode = "propertyAnnouncement_admin", rgroup = "运营")
    @RequestMapping({"/admin/propertyAnnouncement_export_excel.htm"})
    public void propertyAnnouncement_export_excel(HttpServletRequest request, HttpServletResponse response, String currentPage, String orderBy, String orderType, String sheetName) throws IOException {
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
        cell.setCellValue("物业id");
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellValue("通知标题");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("通知内容");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellValue("通知时间");
        cell.setCellStyle(style);

        PropertyAnnouncementQueryObject qo = new PropertyAnnouncementQueryObject();
        if (!CommUtil.null2String(sheetName).equals("")) {
            qo.addQuery("obj.sheetName.propertyName", new SysMap("propertyName", sheetName), "=");
        }
        IPageList pList = announcementService.list(qo);
        List<PropertyAnnouncement> lists = pList.getResult();
        if (null != lists) {
            for (int i = 0; i < lists.size(); i++) {
                row = sheet.createRow((int) i + 1);
                PropertyAnnouncement announcement = lists.get(i);
                row.createCell(0).setCellValue(announcement.getId());
                row.createCell(1).setCellValue(announcement.getSheetName().getPropertyName());
                row.createCell(2).setCellValue(announcement.getNoticeTitle());
                row.createCell(3).setCellValue(announcement.getNoticeContent());
                row.createCell(4).setCellValue(announcement.getNoticeTime().toString());
            }
        }
        if (null == lists) {
            for (int i = 0; i < 5; i++) {
                row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue("");
                row.createCell(1).setCellValue("");
                row.createCell(2).setCellValue("");
                row.createCell(3).setCellValue("");
                row.createCell(4).setCellValue("");
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
