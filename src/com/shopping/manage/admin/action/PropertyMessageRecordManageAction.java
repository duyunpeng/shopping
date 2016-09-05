package com.shopping.manage.admin.action;

import com.shopping.core.annotation.SecurityMapping;
import com.shopping.core.domain.virtual.SysMap;
import com.shopping.core.enums.PackageStatus;
import com.shopping.core.mv.JModelAndView;
import com.shopping.core.query.support.IPageList;
import com.shopping.core.tools.CommUtil;
import com.shopping.foundation.domain.PropertyMessageRecord;
import com.shopping.foundation.domain.User;
import com.shopping.foundation.domain.query.PropertyMessageRecordQueryObject;
import com.shopping.foundation.domain.query.PropertySheetQueryObject;
import com.shopping.foundation.service.IPropertyMessageRecordService;
import com.shopping.foundation.service.ISysConfigService;
import com.shopping.foundation.service.IUserConfigService;
import com.shopping.foundation.service.IUserService;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;
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
 * Created by dyp on 16-8-27.
 */
@Controller
public class PropertyMessageRecordManageAction {
    @Autowired
    private ISysConfigService configService;

    @Autowired
    private IUserConfigService userConfigService;

    @Autowired
    private IPropertyMessageRecordService messageRecordService;

    @Autowired
    private IUserService userService;

    @SecurityMapping(display = false, rsequence = 0, title = "物业表列表", value = "/admin/propertyMessageRecord_list.htm*", rtype = "admin", rname = "物业管理", rcode = "propertyMessageRecord_admin", rgroup = "运营")
    @RequestMapping({"/admin/propertyMessageRecord_list.htm"})
    public ModelAndView propertyMessageRecord_list(HttpServletRequest request, HttpServletResponse response, String currentPage, String orderBy, String orderType, String account, String sendTime) {
        ModelAndView mv = new JModelAndView("admin/blue/propertyMessageRecord_list.html",
                this.configService.getSysConfig(), this.userConfigService
                .getUserConfig(), 0, request, response);
        PropertySheetQueryObject qo = new PropertySheetQueryObject(currentPage, mv, orderBy, orderType);
        if (!CommUtil.null2String(account).equals("")) {
            qo.addQuery("obj.account.userName", new SysMap("userName", account), "=");
        }
        if (!CommUtil.null2String(sendTime).equals("")) {
            qo.addQuery("obj.sendTime", new SysMap("sendTime",
                    PackageStatus.getEnumFromString(CommUtil.null2String(sendTime))), "=");
        }
        IPageList pList = this.messageRecordService.list(qo);
        CommUtil.saveIPageList2ModelAndView("", "", "", pList, mv);
        return mv;
    }


    @SecurityMapping(display = false, rsequence = 0, title = "物业详情", value = "/admin/propertyMessageRecord_view.htm*", rtype = "admin", rname = "物业管理", rcode = "propertyMessageRecord_admin", rgroup = "运营")
    @RequestMapping({"/admin/propertyMessageRecord_view.htm"})
    public ModelAndView propertyMessageRecord_view(HttpServletRequest request, HttpServletResponse response, String id) {
        ModelAndView mv = new JModelAndView("admin/blue/propertyMessageRecord_view.html",
                this.configService.getSysConfig(), this.userConfigService
                .getUserConfig(), 0, request, response);
        PropertyMessageRecord obj = this.messageRecordService.getObjById(CommUtil.null2Long(id));
        mv.addObject("obj", obj);
        return mv;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "物业添加", value = "/admin/propertyMessageRecord_add.htm*", rtype = "admin", rname = "物业管理", rcode = "propertyMessageRecord_admin", rgroup = "运营")
    @RequestMapping({"/admin/propertyMessageRecord_add.htm"})
    public ModelAndView propertyMessageRecord_add(HttpServletRequest request, HttpServletResponse response, String currentPage) {
        ModelAndView mv = new JModelAndView("admin/blue/propertyMessageRecord_add.html",
                this.configService.getSysConfig(), this.userConfigService
                .getUserConfig(), 0, request, response);
        mv.addObject("currentPage", currentPage);
        return mv;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "根据电话查找用户", value = "/admin/propertyMessage_findPhone.htm*", rtype = "admin", rname = "物业管理", rcode = "propertyMessageRecord_admin", rgroup = "运营")
    @RequestMapping({"/admin/propertyMessage_findPhone.htm"})
    public String propertyMessage_findPhone(HttpServletRequest request, HttpServletResponse response, String currentPage,String phone) {
        String s = "";
        Map map = new HashMap();
        map.put("phone",phone);
        List<User> accounts = this.userService.query("select obj from User obj where obj.telephone=:phone", map, -1, -1);
        if(accounts != null && accounts.size()!=0){
            s="{name:"+true+"}";
        }else{
            s="{name:"+false+"}";
        }
        printAjax(s,response);
        return null;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "物业编辑", value = "/admin/propertyMessageRecord_edit.htm*", rtype = "admin", rname = "物业管理", rcode = "propertyMessageRecord_admin", rgroup = "运营")
    @RequestMapping({"/admin/propertyMessageRecord_edit.htm"})
    public ModelAndView propertyMessageRecord_edit(HttpServletRequest request, HttpServletResponse response, String id, String currentPage) {
        ModelAndView mv = new JModelAndView("admin/blue/propertyMessageRecord_edit.html",
                this.configService.getSysConfig(), this.userConfigService
                .getUserConfig(), 0, request, response);
        if ((id != null) && (!id.equals(""))) {
            PropertyMessageRecord messageRecord = this.messageRecordService.getObjById(
                    Long.valueOf(Long.parseLong(id)));
            mv.addObject("obj", messageRecord);
            mv.addObject("currentPage", currentPage);
            mv.addObject("edit", Boolean.valueOf(true));
        }
        return mv;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "物业删除", value = "/admin/propertyMessageRecord_delete.htm*", rtype = "admin", rname = "物业管理", rcode = "propertyMessageRecord_admin", rgroup = "运营")
    @RequestMapping({"/admin/propertyMessageRecord_delete.htm"})
    public String propertyMessageRecord_delete(HttpServletRequest request, HttpServletResponse response, String mulitId, String currentPage) {
        String[] ids = mulitId.split(",");
        for (String id : ids) {
            if (!id.equals("")) {
                PropertyMessageRecord record = this.messageRecordService.getObjById(Long.parseLong(id));
                record.setAccount(null);
                this.messageRecordService.delete(Long.valueOf(id));
            }
        }
        return "redirect:propertyMessageRecord_list.htm?currentPage=" + currentPage;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "物业保存", value = "/admin/propertyMessageRecord_save.htm*", rtype = "admin", rname = "物业管理", rcode = "propertyMessageRecord_admin", rgroup = "运营")
    @RequestMapping({"/admin/propertyMessageRecord_save.htm"})
    public String propertyMessageRecord_save(HttpServletRequest request, HttpServletResponse response, String id, String currentPage, String account, String sendTime) throws ParseException {
        if (!id.equals("")) {
            PropertyMessageRecord messageRecord = messageRecordService.getObjById(Long.parseLong(id));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (null != sendTime && !sendTime.equals("")) {
                Date parse = sdf.parse(sendTime);
                messageRecord.setSendTime(parse);
            }
            if (null!=account&&!account.equals("")) {
                User user = new User();
                user.setUserName(account);
                messageRecord.setAccount(user);
            }
            this.messageRecordService.save(messageRecord);
        }
        return "redirect:propertyMessageRecord_list.htm?currentPage=" + currentPage;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "物业添加", value = "/admin/propertyMessageRecord_new.htm*", rtype = "admin", rname = "物业管理", rcode = "propertyMessageRecord_admin", rgroup = "运营")
    @RequestMapping({"/admin/propertyMessageRecord_new.htm"})
    public String propertyMessageRecord_new(HttpServletRequest request, HttpServletResponse response, String currentPage, String orderBy, String orderType, String account, String sendTime,String phone) throws ParseException, ApiException {
        PropertyMessageRecord messageRecord = new PropertyMessageRecord();
        Map map = new HashMap();
        map.put("phone",phone);
        List<User> accounts = this.userService.query("select obj from User obj where obj.telephone=:phone", map, -1, -1);
        if (null!=accounts && accounts.size() !=0) {
            messageRecord.setAccount(accounts.get(0));
        }
        messageRecord.setSendTime(new Date());
        messageRecord.setAddTime(new Date());
        this.messageRecordService.save(messageRecord);

        //发送短信
        String appkey="23447278";
        String url ="http://gw.api.taobao.com/router/rest";
        String secret="ebfb919e76fc083db1a4cca11775dad2";
        TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
        AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
        req.setExtend("123456");
        req.setSmsType("normal");
        req.setSmsFreeSignName("清风12");
        req.setSmsParamString("{\"name\":\""+messageRecord.getAccount().getUsername()+"\"}");
        req.setRecNum(phone);
        req.setSmsTemplateCode("SMS_14215566");
        AlibabaAliqinFcSmsNumSendResponse rsp = client.execute(req);
        System.out.println(rsp.getBody());


        return "redirect:propertyMessageRecord_list.htm?currentPage=" + currentPage;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "物业导出表格", value = "/admin/propertyMessageRecord_new_export_excel.htm*", rtype = "admin", rname = "物业管理", rcode = "propertyMessageRecord_admin", rgroup = "运营")
    @RequestMapping({"/admin/propertyMessageRecord_new_export_excel.htm"})
    public void propertyMessageRecord_new_export_excel(HttpServletRequest request, HttpServletResponse response, String currentPage, String orderBy, String orderType, String account, String sendTime) throws IOException {
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("物业短消息发送记录");
        HSSFRow row = sheet.createRow((int) 0);
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);


        sheet.setDefaultColumnWidth(20);
        sheet.setDefaultRowHeight((short) 30);

        HSSFCell cell = row.createCell(0);
        cell.setCellValue("接受用户");
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellValue("发送时间");
        cell.setCellStyle(style);



        PropertyMessageRecordQueryObject qo = new PropertyMessageRecordQueryObject();
        if (!CommUtil.null2String(account).equals("")) {
            qo.addQuery("obj.account", new SysMap("account", account), "=");
        }
        if (!CommUtil.null2String(sendTime).equals("")) {
            qo.addQuery("obj.sendTime", new SysMap("sendTime", account), "=");
        }
        IPageList pList = messageRecordService.list(qo);
        List<PropertyMessageRecord> lists = pList.getResult();
        if (null != lists) {
            for (int i = 0; i < lists.size(); i++) {
                row = sheet.createRow((int) i + 1);
                PropertyMessageRecord messageRecord = lists.get(i);
                row.createCell(0).setCellValue(messageRecord.getId());
                row.createCell(1).setCellValue(messageRecord.getSendTime().toString());
            }
        }
        if (null == lists) {
            for (int i = 0; i < 2; i++) {
                row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue("");
                row.createCell(1).setCellValue("");
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
