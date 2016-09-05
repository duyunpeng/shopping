package com.shopping.manage.admin.action;

import com.shopping.core.annotation.SecurityMapping;
import com.shopping.core.domain.virtual.SysMap;
import com.shopping.core.enums.CollectionStatus;
import com.shopping.core.enums.PackageStatus;
import com.shopping.core.enums.PostageStatus;
import com.shopping.core.enums.SignStatus;
import com.shopping.core.mv.JModelAndView;
import com.shopping.core.query.support.IPageList;
import com.shopping.core.tools.CommUtil;
import com.shopping.foundation.domain.Area;
import com.shopping.foundation.domain.Tenement;
import com.shopping.foundation.domain.User;
import com.shopping.foundation.domain.query.TenementQueryObject;
import com.shopping.foundation.service.IAreaService;
import com.shopping.foundation.service.ISysConfigService;
import com.shopping.foundation.service.ITenementService;
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
import java.util.Date;
import java.util.List;

/**
 * Created by dyp on 2016/8/22.
 */
@Controller
public class TenementManageAction {

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private IUserConfigService userConfigService;

    @Autowired
    private ITenementService tenementService;

    @Autowired
    private IAreaService areaService;

    @SecurityMapping(display = false, rsequence = 0, title = "物业列表", value = "/admin/tenement_list.htm*", rtype = "admin", rname = "物业管理", rcode = "tenement_admin", rgroup = "运营")
    @RequestMapping({"/admin/tenement_list.htm"})
    public ModelAndView tenement_list(HttpServletRequest request, HttpServletResponse response, String currentPage, String orderBy, String orderType, String user, String fileType, String collectionStatus, String signStatus, String postageStatus) {
        ModelAndView mv = new JModelAndView("admin/blue/tenement_list.html",
                this.configService.getSysConfig(), this.userConfigService
                .getUserConfig(), 0, request, response);
        TenementQueryObject qo = new TenementQueryObject(currentPage, mv, orderBy, orderType);
        if (!CommUtil.null2String(user).equals("")) {
            qo.addQuery("obj.user.userName", new SysMap("userName", user), "=");
        }
        if (!CommUtil.null2String(fileType).equals("")) {
            qo.addQuery("obj.fileType", new SysMap("fileType",
                    PackageStatus.getEnumFromString(CommUtil.null2String(fileType))), "=");
        }
        if (!CommUtil.null2String(collectionStatus).equals("")) {
            qo.addQuery("obj.collectionStatus", new SysMap("collectionStatus",
                    CollectionStatus.getEnumFromString(CommUtil.null2String(collectionStatus))), "=");
        }
        if (!CommUtil.null2String(signStatus).equals("")) {
            qo.addQuery("obj.signStatus", new SysMap("signStatus",
                    SignStatus.getEnumFromString(CommUtil.null2String(signStatus))), "=");
        }
        if (!CommUtil.null2String(postageStatus).equals("")) {
            qo.addQuery("obj.postageStatus", new SysMap("postageStatus",
                    PostageStatus.getEnumFromString(CommUtil.null2String(postageStatus))), "=");
        }
        IPageList pList = this.tenementService.list(qo);
        CommUtil.saveIPageList2ModelAndView("", "", "", pList, mv);
        return mv;
    }


    @SecurityMapping(display = false, rsequence = 0, title = "物业详情", value = "/admin/tenement_list.htm*", rtype = "admin", rname = "物业管理", rcode = "tenement_admin", rgroup = "运营")
    @RequestMapping({"/admin/tenement_view.htm"})
    public ModelAndView tenement_view(HttpServletRequest request, HttpServletResponse response, String id) {
        ModelAndView mv = new JModelAndView("admin/blue/tenement_view.html",
                this.configService.getSysConfig(), this.userConfigService
                .getUserConfig(), 0, request, response);
        Tenement obj = this.tenementService.getObjById(CommUtil.null2Long(id));
        mv.addObject("obj", obj);
        return mv;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "物业添加", value = "/admin/tenement_add.htm*", rtype = "admin", rname = "物业管理", rcode = "tenement_admin", rgroup = "运营")
    @RequestMapping({"/admin/tenement_add.htm"})
    public ModelAndView tenement_add(HttpServletRequest request, HttpServletResponse response, String currentPage) {
        ModelAndView mv = new JModelAndView("admin/blue/tenement_add.html",
                this.configService.getSysConfig(), this.userConfigService
                .getUserConfig(), 0, request, response);
        List areas = this.areaService.query("select obj from Area obj where obj.parent.id is null",null,-1,-1);
        mv.addObject("areas",areas);
        mv.addObject("currentPage", currentPage);

        return mv;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "物业添加", value = "/admin/tenement_new.htm*", rtype = "admin", rname = "物业管理", rcode = "tenement_admin", rgroup = "运营")
    @RequestMapping({"/admin/tenement_new.htm"})
    public ModelAndView tenement_new(HttpServletRequest request, HttpServletResponse response, String currentPage, String user, String fileType, String orderBy, String orderType, String area, String houseArea, String collectionStatus, String signStatus, String postageStatus) {
        ModelAndView mv = new JModelAndView("admin/blue/tenement_list.html",
                this.configService.getSysConfig(), this.userConfigService
                .getUserConfig(), 0, request, response);
        User user1 = new User();
        user1.setAddTime(new Date());
        user1.setUserName(user);
        Tenement tenement = new Tenement();
        tenement.setAddTime(new Date());
        tenement.setHouseArea(Double.parseDouble(houseArea));
        Area area1 = this.areaService.getObjById(Long.parseLong(area));
        tenement.setArea(area1);

        if (!CommUtil.null2String(user).equals("")) {
            tenement.setUser(user1);
        }
        if (!CommUtil.null2String(fileType).equals("")) {
            tenement.setFileType(PackageStatus.getEnumFromString(fileType));
        }
        if (!CommUtil.null2String(collectionStatus).equals("")) {
            tenement.setCollectionStatus(CollectionStatus.getEnumFromString(collectionStatus));
        }
        if (!CommUtil.null2String(signStatus).equals("")) {
            tenement.setSignStatus(SignStatus.getEnumFromString(signStatus));
        }
        if (!CommUtil.null2String(postageStatus).equals("")) {
            tenement.setPostageStatus(PostageStatus.getEnumFromString(postageStatus));
        }
        this.tenementService.save(tenement);
        TenementQueryObject qo = new TenementQueryObject(currentPage, mv, orderBy, orderType);
        IPageList pList = this.tenementService.list(qo);
        CommUtil.saveIPageList2ModelAndView("", "", "", pList, mv);
        return mv;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "物业删除", value = "/admin/tenement_delete.htm*", rtype = "admin", rname = "物业管理", rcode = "tenement_admin", rgroup = "运营")
    @RequestMapping({"/admin/tenement_delete.htm"})
    public String tenement_delete(HttpServletRequest request, HttpServletResponse response, String mulitId, String currentPage) {
        String[] ids = mulitId.split(",");
        for (String id : ids) {
            if (!id.equals("")) {
                this.tenementService.delete(Long.valueOf(id));
            }
        }
        return "redirect:tenement_list.htm?currentPage=" + currentPage;
    }


    @SecurityMapping(display = false, rsequence = 0, title = "物业编辑", value = "/admin/tenement_edit.htm*", rtype = "admin", rname = "物业管理", rcode = "tenement_admin", rgroup = "运营")
    @RequestMapping({"/admin/tenement_edit.htm"})
    public ModelAndView tenement_edit(HttpServletRequest request, HttpServletResponse response, String id, String currentPage) {
        ModelAndView mv = new JModelAndView("admin/blue/tenement_edit.html",
                this.configService.getSysConfig(), this.userConfigService
                .getUserConfig(), 0, request, response);
        if ((id != null) && (!id.equals(""))) {
            Tenement tenement = this.tenementService.getObjById(
                    Long.valueOf(Long.parseLong(id)));
            mv.addObject("obj", tenement);
            mv.addObject("currentPage", currentPage);
            mv.addObject("edit", Boolean.valueOf(true));
        }
        List areas = this.areaService.query("select obj from Area obj where obj.parent.id is null",null,-1,-1);
        mv.addObject("areas",areas);
        return mv;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "物业保存", value = "/admin/tenement_save.htm*", rtype = "admin", rname = "物业管理", rcode = "tenement_admin", rgroup = "运营")
    @RequestMapping({"/admin/tenement_save.htm"})
    public String tenement_save(HttpServletRequest request, HttpServletResponse response, String id, String currentPage, String fileType, String user, String area, String houseArea, String collectionStatus, String signStatus, String postageStatus) {
        if (!id.equals("")) {
            Tenement tenement = tenementService.getObjById(Long.parseLong(id));
            User user1 = new User();
            user1.setUserName(user);
            tenement.setUser(user1);
            tenement.setFileType(PackageStatus.getEnumFromString(fileType));
            Area area1 = this.areaService.getObjById(Long.parseLong(area));
            tenement.setArea(area1);
            tenement.setHouseArea(Double.parseDouble(houseArea));
            tenement.setCollectionStatus(CollectionStatus.getEnumFromString(collectionStatus));
            tenement.setSignStatus(SignStatus.getEnumFromString(signStatus));
            tenement.setPostageStatus(PostageStatus.getEnumFromString(postageStatus));
            tenementService.save(tenement);
        }
        return "redirect:tenement_list.htm?currentPage=" + currentPage;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "物业导出表格", value = "/admin/export_excel.htm*", rtype = "admin", rname = "物业管理", rcode = "tenement_admin", rgroup = "运营")
    @RequestMapping({"/admin/export_excel.htm"})
    public void export_excel(HttpServletRequest request, HttpServletResponse response, String currentPage, String orderBy, String orderType, String user, String fileType, String collectionStatus, String signStatus, String postageStatus) throws IOException {
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("物业管理表");
        HSSFRow row = sheet.createRow((int) 0);
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);


        sheet.setDefaultColumnWidth(20);
        sheet.setDefaultRowHeight((short)30);

        HSSFCell cell = row.createCell(0);
        cell.setCellValue("id编号");
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellValue("业主名称");
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellValue("包裹类型");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("代收状态");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellValue("签收状态");
        cell.setCellStyle(style);

        cell = row.createCell(5);
        cell.setCellValue("邮费类型");
        cell.setCellStyle(style);

        cell = row.createCell(6);
        cell.setCellValue("所属区域");
        cell.setCellStyle(style);

        cell = row.createCell(7);
        cell.setCellValue("房屋面积(平方米)");
        cell.setCellStyle(style);

        TenementQueryObject qo = new TenementQueryObject();
        if (!CommUtil.null2String(user).equals("")) {
            qo.addQuery("obj.user.userName", new SysMap("userName", user), "=");
        }
        if (!CommUtil.null2String(fileType).equals("")) {
            qo.addQuery("obj.fileType", new SysMap("fileType",
                    PackageStatus.getEnumFromString(CommUtil.null2String(fileType))), "=");
        }
        if (!CommUtil.null2String(collectionStatus).equals("")) {
            qo.addQuery("obj.collectionStatus", new SysMap("collectionStatus",
                    CollectionStatus.getEnumFromString(CommUtil.null2String(collectionStatus))), "=");
        }
        if (!CommUtil.null2String(signStatus).equals("")) {
            qo.addQuery("obj.signStatus", new SysMap("signStatus",
                    SignStatus.getEnumFromString(CommUtil.null2String(signStatus))), "=");
        }
        if (!CommUtil.null2String(postageStatus).equals("")) {
            qo.addQuery("obj.postageStatus",new SysMap("postageStatus",
                    PostageStatus.getEnumFromString(CommUtil.null2String(postageStatus))),"=");
        }
        IPageList pList = this.tenementService.list(qo);
        List<Tenement> lists = pList.getResult();
        if (null != lists) {
            for (int i = 0; i < lists.size(); i++) {
                row = sheet.createRow((int) i + 1);
                Tenement tenement = lists.get(i);
                row.createCell(0).setCellValue(tenement.getId());
                row.createCell(1).setCellValue(tenement.getUser().getUserName());
                row.createCell(2).setCellValue(tenement.getFileType().getName());
                row.createCell(3).setCellValue(tenement.getCollectionStatus().getName());
                row.createCell(4).setCellValue(tenement.getSignStatus().getName());
                row.createCell(5).setCellValue(tenement.getPostageStatus().getName());
                row.createCell(6).setCellValue(tenement.getArea().getAreaName());
                row.createCell(7).setCellValue(tenement.getHouseArea());
            }
        }
        if (null == lists) {
            for (int i = 0; i < 8; i++) {
                row = sheet.createRow(i+1);
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
