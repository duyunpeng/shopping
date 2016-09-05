package com.shopping.manage.admin.action;

import com.shopping.core.annotation.SecurityMapping;
import com.shopping.core.domain.virtual.SysMap;
import com.shopping.core.enums.PackageStatus;
import com.shopping.core.mv.JModelAndView;
import com.shopping.core.query.support.IPageList;
import com.shopping.core.tools.CommUtil;
import com.shopping.foundation.domain.*;
import com.shopping.foundation.domain.query.PropertySheetQueryObject;
import com.shopping.foundation.service.*;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dyp on 2016/8/26.
 */
@Controller
public class PropertySheetManageAction {

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private IUserConfigService userConfigService;

    @Autowired
    private IAreaService areaService;

    @Autowired
    private IPropertySheetService propertySheetService;

    @Autowired
    private IPropertyAnnouncementService propertyAnnouncementService;

    @Autowired
    private IPropertyExpressParcelService propertyExpressParcelService;

    @SecurityMapping(display = false, rsequence = 0, title = "物业表列表", value = "/admin/propertySheet_list.htm*", rtype = "admin", rname = "物业管理", rcode = "propertySheet_admin", rgroup = "运营")
    @RequestMapping({"/admin/propertySheet_list.htm"})
    public ModelAndView propertySheet_list(HttpServletRequest request, HttpServletResponse response, String currentPage, String orderBy, String orderType, String userId, String propertyName) {
        ModelAndView mv = new JModelAndView("admin/blue/propertySheet_list.html",
                this.configService.getSysConfig(), this.userConfigService
                .getUserConfig(), 0, request, response);
        PropertySheetQueryObject qo = new PropertySheetQueryObject(currentPage, mv, orderBy, orderType);
        if (!CommUtil.null2String(userId).equals("")) {
            qo.addQuery("obj.userId.userName", new SysMap("userName", userId), "=");
        }
        if (!CommUtil.null2String(propertyName).equals("")) {
            qo.addQuery("obj.propertyName", new SysMap("propertyName",
                    PackageStatus.getEnumFromString(CommUtil.null2String(propertyName))), "=");
        }

        IPageList pList = this.propertySheetService.list(qo);
        CommUtil.saveIPageList2ModelAndView("", "", "", pList, mv);
        return mv;
    }


    @SecurityMapping(display = false, rsequence = 0, title = "物业详情", value = "/admin/propertySheet_view.htm*", rtype = "admin", rname = "物业管理", rcode = "propertySheet_admin", rgroup = "运营")
    @RequestMapping({"/admin/propertySheet_view.htm"})
    public ModelAndView propertySheet_view(HttpServletRequest request, HttpServletResponse response, String id) {
        ModelAndView mv = new JModelAndView("admin/blue/propertySheet_view.html",
                this.configService.getSysConfig(), this.userConfigService
                .getUserConfig(), 0, request, response);
        PropertySheet obj = this.propertySheetService.getObjById(CommUtil.null2Long(id));
        mv.addObject("obj", obj);
        return mv;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "物业添加", value = "/admin/propertySheet_add.htm*", rtype = "admin", rname = "物业管理", rcode = "propertySheet_admin", rgroup = "运营")
    @RequestMapping({"/admin/propertySheet_add.htm"})
    public ModelAndView propertySheet_add(HttpServletRequest request, HttpServletResponse response, String currentPage) {
        ModelAndView mv = new JModelAndView("admin/blue/propertySheet_add.html",
                this.configService.getSysConfig(), this.userConfigService
                .getUserConfig(), 0, request, response);
        List areas = this.areaService.query("select obj from Area obj where obj.parent.id is null", null, -1, -1);
        mv.addObject("areas", areas);
        mv.addObject("currentPage", currentPage);
        return mv;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "物业查询", value = "/admin/propertySheet_findById.htm*", rtype = "admin", rname = "物业管理", rcode = "propertySheet_admin", rgroup = "运营")
    @RequestMapping({"/admin/propertySheet_findById.htm"})
    public String  propertySheet_findById(HttpServletRequest request, HttpServletResponse response, String id, String currentPage) {
        String s ="";
        Map map = new HashMap();
        map.put("propertySheetId",Long.parseLong(id));
        List<PropertyExpressParcel> query = this.propertyExpressParcelService.query("select obj from PropertyExpressParcel obj where obj.parcelName.id=:propertySheetId", map, -1, -1);
        List<PropertyAnnouncement> propertyAnnouncements = this.propertyAnnouncementService.query("select obj from PropertyAnnouncement obj where obj.sheetName.id=:propertySheetId", map, -1, -1);
        if((null != query && query.size()!=0) || (null != propertyAnnouncements && propertyAnnouncements.size()!=0)){
            s="{name:"+true+"}";
        }else{
            s="{name:"+false+"}";
        }
        printAjax(s,response);
        return null;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "物业编辑", value = "/admin/propertySheet_edit.htm*", rtype = "admin", rname = "物业管理", rcode = "propertySheet_admin", rgroup = "运营")
    @RequestMapping({"/admin/propertySheet_edit.htm"})
    public ModelAndView propertySheet_edit(HttpServletRequest request, HttpServletResponse response, String id, String currentPage) {
        ModelAndView mv = new JModelAndView("admin/blue/propertySheet_edit.html",
                this.configService.getSysConfig(), this.userConfigService
                .getUserConfig(), 0, request, response);
        if ((id != null) && (!id.equals(""))) {
            PropertySheet propertySheet = this.propertySheetService.getObjById(
                    Long.valueOf(Long.parseLong(id)));
            mv.addObject("obj", propertySheet);
            mv.addObject("currentPage", currentPage);
            mv.addObject("edit", Boolean.valueOf(true));
        }
        List areas = this.areaService.query("select obj from Area obj where obj.parent.id is null", null, -1, -1);
        mv.addObject("areas", areas);
        return mv;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "物业删除", value = "/admin/propertySheet_delete.htm*", rtype = "admin", rname = "物业管理", rcode = "propertySheet_admin", rgroup = "运营")
    @RequestMapping({"/admin/propertySheet_delete.htm"})
    public String propertySheet_delete(HttpServletRequest request, HttpServletResponse response, String mulitId, String currentPage) {
        String[] ids = mulitId.split(",");
        for (String id : ids) {
            if (!id.equals("")) {
                Map map = new HashMap();
                map.put("id", Long.parseLong(id));
                List<PropertyExpressParcel> expressParcel = this.propertyExpressParcelService.query("select obj from PropertyExpressParcel obj where obj.parcelName.id=:id", map, -1, -1);
                /*if (null != expressParcel && expressParcel.size() != 0) {
                    for (PropertyExpressParcel gc : expressParcel) {
                        PropertyExpressParcel parcel = this.propertyExpressParcelService.getObjById(gc.getId());
                        parcel.setParcelName(null);
                        this.propertyExpressParcelService.save(parcel);
                        *//*parcel.setParcelName(null);
                        this.propertyExpressParcelService.delete(gc.getId());*//*
                    }
                }
                List<PropertyAnnouncement> propertyAnnouncement = this.propertyAnnouncementService.query("select obj from PropertyAnnouncement obj where obj.sheetName.id=:id ", map, -1, -1);
                if (null != propertyAnnouncement && propertyAnnouncement.size() != 0) {
                    for (PropertyAnnouncement gc : propertyAnnouncement) {
                        PropertyAnnouncement announcement = this.propertyAnnouncementService.getObjById(gc.getId());
                        announcement.setSheetName(null);
                        this.propertyAnnouncementService.save(announcement);
                      *//*  announcement.setSheetName(null);
                        this.propertyAnnouncementService.delete(gc.getId());*//*
                    }
                }*/
                PropertySheet sheet = this.propertySheetService.getObjById(Long.parseLong(id));
                sheet.setUserId(null);
                sheet.setAreaId(null);
                this.propertySheetService.delete(Long.valueOf(id));
            }
        }
        return "redirect:propertySheet_list.htm?currentPage=" + currentPage;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "物业保存", value = "/admin/propertySheet_save.htm*", rtype = "admin", rname = "物业管理", rcode = "propertySheet_admin", rgroup = "运营")
    @RequestMapping({"/admin/propertySheet_save.htm"})
    public String propertySheet_save(HttpServletRequest request, HttpServletResponse response, String id, String currentPage, String userId, String areaId, String createTime, String settlementTime, String propertyName) throws ParseException {
        if (!id.equals("")) {
            PropertySheet propertySheet = propertySheetService.getObjById(Long.parseLong(id));
            if (null != areaId && !areaId.equals("")) {
                Area area = this.areaService.getObjById(Long.parseLong(areaId));
                propertySheet.setAreaId(area);
            }
            if (null != userId && !userId.equals("")) {
                User user = new User();
                user.setUserName(userId);
                propertySheet.setUserId(user);
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (null != createTime && !createTime.equals("")) {
                Date create = sdf.parse(createTime);
                propertySheet.setCreateTime(create);
            }
            if (null != settlementTime && !settlementTime.equals("")) {
                Date settlement = sdf.parse(settlementTime);
                propertySheet.setSettlementTime(settlement);
            }
            if (null != propertyName && !propertyName.equals("")) {
                propertySheet.setPropertyName(propertyName);
            }
            this.propertySheetService.save(propertySheet);
        }
        return "redirect:propertySheet_list.htm?currentPage=" + currentPage;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "物业添加", value = "/admin/propertySheet_new.htm*", rtype = "admin", rname = "物业管理", rcode = "propertySheet_admin", rgroup = "运营")
    @RequestMapping({"/admin/propertySheet_new.htm"})
    public ModelAndView propertySheet_new(HttpServletRequest request, HttpServletResponse response, String currentPage, String orderBy, String orderType, String userId, String areaId, String createTime, String settlementTime, String propertyName) throws ParseException {
        ModelAndView mv = new JModelAndView("admin/blue/propertySheet_list.html",
                this.configService.getSysConfig(), this.userConfigService
                .getUserConfig(), 0, request, response);
        PropertySheet propertySheet = new PropertySheet();
        if (null != areaId && !areaId.equals("")) {
            Area area = this.areaService.getObjById(Long.parseLong(areaId));
            propertySheet.setAreaId(area);
        }
        if (null != userId && !userId.equals("")) {
            User user = new User();
            user.setUserName(userId);
            propertySheet.setUserId(user);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (null != createTime && !createTime.equals("")) {
            Date create = sdf.parse(createTime);
            propertySheet.setCreateTime(create);
        }
        if (null != settlementTime && !settlementTime.equals("")) {
            Date settlement = sdf.parse(settlementTime);
            propertySheet.setSettlementTime(settlement);
        }
        if (null != propertyName && !propertyName.equals("")) {
            propertySheet.setPropertyName(propertyName);
        }
        propertySheet.setAddTime(new Date());
        this.propertySheetService.save(propertySheet);
        PropertySheetQueryObject qo = new PropertySheetQueryObject(currentPage, mv, orderBy, orderType);
        IPageList pList = this.propertySheetService.list(qo);
        CommUtil.saveIPageList2ModelAndView("", "", "", pList, mv);
        return mv;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "物业导出表格", value = "/admin/propertySheet_export_excel.htm*", rtype = "admin", rname = "物业管理", rcode = "propertySheet_admin", rgroup = "运营")
    @RequestMapping({"/admin/propertySheet_export_excel.htm"})
    public void propertySheet_export_excel(HttpServletRequest request, HttpServletResponse response, String currentPage, String orderBy, String userId, String propertyName) throws IOException {
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
        cell.setCellValue("用户");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("小区");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellValue("创建时间");
        cell.setCellStyle(style);

        cell = row.createCell(5);
        cell.setCellValue("最后结算时间");
        cell.setCellStyle(style);

        cell = row.createCell(6);
        cell.setCellValue("物业名称");
        cell.setCellStyle(style);

        PropertySheetQueryObject qo = new PropertySheetQueryObject();
        if (!CommUtil.null2String(userId).equals("")) {
            qo.addQuery("obj.userId.userName", new SysMap("userName", userId), "=");
        }
        if (!CommUtil.null2String(propertyName).equals("")) {
            qo.addQuery("obj.propertyName", new SysMap("propertyName",
                    PackageStatus.getEnumFromString(CommUtil.null2String(propertyName))), "=");
        }
        IPageList pList = propertySheetService.list(qo);
        List<PropertySheet> lists = pList.getResult();
        if (null != lists) {
            for (int i = 0; i < lists.size(); i++) {
                row = sheet.createRow((int) i + 1);
                PropertySheet propertySheet = lists.get(i);
                row.createCell(0).setCellValue(propertySheet.getId());
                row.createCell(1).setCellValue(propertySheet.getUserId().getUserName());
                row.createCell(2).setCellValue(propertySheet.getAreaId().getAreaName());
                row.createCell(3).setCellValue(propertySheet.getCreateTime().toString());
                row.createCell(4).setCellValue(propertySheet.getSettlementTime().toString());
                row.createCell(5).setCellValue(propertySheet.getPropertyName());
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
                row.createCell(6).setCellValue("");
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
    public void printAjax(String s,HttpServletResponse response){
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html");
        try {
            PrintWriter out = response.getWriter();
            out.print(s);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
