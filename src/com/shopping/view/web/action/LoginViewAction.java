 package com.shopping.view.web.action;
 
 import java.io.*;
 import java.net.HttpURLConnection;
 import java.net.URL;
 import java.security.Security;
 import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.httpclient.HttpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.shopping.core.mv.JModelAndView;
import com.shopping.core.security.support.SecurityUserHolder;
import com.shopping.core.tools.CommUtil;
import com.shopping.core.tools.Md5Encrypt;
import com.shopping.foundation.domain.Album;
import com.shopping.foundation.domain.IntegralLog;
import com.shopping.foundation.domain.User;
import com.shopping.foundation.service.IAlbumService;
import com.shopping.foundation.service.IIntegralLogService;
import com.shopping.foundation.service.IRoleService;
import com.shopping.foundation.service.ISysConfigService;
import com.shopping.foundation.service.IUserConfigService;
import com.shopping.foundation.service.IUserService;
import com.shopping.uc.api.UCClient;
import com.shopping.uc.api.UCTools;
import com.shopping.view.web.tools.ImageViewTools;
 
 @Controller
 public class LoginViewAction
 {
 
   @Autowired
   private ISysConfigService configService;
 
   @Autowired
   private IUserConfigService userConfigService;
 
   @Autowired
   private IRoleService roleService;
 
   @Autowired
   private IUserService userService;
 
   @Autowired
   private IIntegralLogService integralLogService;
 
   @Autowired
   private IAlbumService albumService;
 
   @Autowired
   private ImageViewTools imageViewTools;
 
   @Autowired
   private UCTools ucTools;
 
   /**
	 * 用户登录跳转页面
	 * @param request
	 * @param response
	 * @param url
	 * @return
	 */
   @RequestMapping({"/user/login.htm"})
   public ModelAndView login(HttpServletRequest request, HttpServletResponse response, String url) {
	   
     ModelAndView mv = new JModelAndView("login.html", this.configService.getSysConfig(), this.userConfigService.getUserConfig(), 1, request, response);

     String shopping_view_type = CommUtil.null2String(request.getSession(false).getAttribute("shopping_view_type"));
     
     if ((shopping_view_type != null) && (!shopping_view_type.equals("")) && (shopping_view_type.equals("wap"))) {
    	 mv = new JModelAndView("/wap/login.html", this.configService.getSysConfig(),
					this.userConfigService.getUserConfig(), 1, request, response);
     }
 
     request.getSession(false).removeAttribute("verify_code");
     boolean domain_error = CommUtil.null2Boolean(request.getSession(false).getAttribute("domain_error"));
     if ((url != null) && (!url.equals(""))) {
       request.getSession(false).setAttribute("refererUrl", url);
     }
     if (domain_error) {
       mv = new JModelAndView("error.html", this.configService.getSysConfig(), this.userConfigService.getUserConfig(), 1, request, response);
       if ((shopping_view_type != null) && (!shopping_view_type.equals("")) && (shopping_view_type.equals("wap"))) {
    	   mv = new JModelAndView("wap/error.html", this.configService.getSysConfig(), this.userConfigService.getUserConfig(), 1, request, response);
       }
     }
     else {
       mv.addObject("imageViewTools", this.imageViewTools);
     }
     mv.addObject("uc_logout_js", request.getSession(false).getAttribute("uc_logout_js"));
     
     return mv;
   }
   
   /**
	 * 注册跳转页面
	 * @param request
	 * @param response
	 * @return
	 */
   @RequestMapping({"/register.htm"})
   public ModelAndView register(HttpServletRequest request, HttpServletResponse response)
   {
     ModelAndView mv = new JModelAndView("register.html", this.configService.getSysConfig(), 
       this.userConfigService.getUserConfig(), 1, request, response);
     
     String shopping_view_type = CommUtil.null2String(request.getSession(false).getAttribute("shopping_view_type"));
		
	 if ((shopping_view_type != null) && (!shopping_view_type.equals("")) && (shopping_view_type.equals("wap"))) {
		 mv = new JModelAndView("wap/register.html", this.configService.getSysConfig(), 
			       this.userConfigService.getUserConfig(), 1, request, response);
	 }
     request.getSession(false).removeAttribute("verify_code");
     return mv;
   }
   
   /**
	 * 注册保存
	 * @param request
	 * @param response
	 * @param userName
	 * @param password
	 * @param email
	 * @param code
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */
   @RequestMapping({"/register_finish.htm"})
   public String register_finish(HttpServletRequest request, HttpServletResponse response, String userName, String password, String email, String code)
     throws HttpException, IOException
   {
     boolean reg = true;
     if ((code != null) && (!code.equals(""))) {
       code = CommUtil.filterHTML(code);
     }
     //System.out.println(this.configService.getSysConfig().isSecurityCodeRegister());
     if (this.configService.getSysConfig().isSecurityCodeRegister())
     {
       if (!request.getSession(false).getAttribute("verify_code").equals(code)) {
         reg = false;
       }
     }
     Map params = new HashMap();
     params.put("userName", userName);
     List users = this.userService.query("select obj from User obj where obj.userName=:userName", params, -1, -1);
     if ((users != null) && (users.size() > 0)) {
       reg = false;
     }
     if (reg) {
       User user = new User();
       user.setUserName(userName);
       user.setUserRole("BUYER");
       user.setAddTime(new Date());
       user.setEmail(email);
       user.setPassword(Md5Encrypt.md5(password).toLowerCase());
       params.clear();
       params.put("type", "BUYER");
       List roles = this.roleService.query("select obj from Role obj where obj.type=:type", params, -1, -1);
       user.getRoles().addAll(roles);
       if (this.configService.getSysConfig().isIntegral()) {
         user.setIntegral(this.configService.getSysConfig().getMemberRegister());
         this.userService.save(user);
         IntegralLog log = new IntegralLog();
         log.setAddTime(new Date());
         log.setContent("用户注册增加" + this.configService.getSysConfig().getMemberRegister() + "分");
         log.setIntegral(this.configService.getSysConfig().getMemberRegister());
         log.setIntegral_user(user);
         log.setType("reg");
         this.integralLogService.save(log);
       } else {
         this.userService.save(user);
       }
 
       Album album = new Album();
       album.setAddTime(new Date());
       album.setAlbum_default(true);
       album.setAlbum_name("默认相册");
       album.setAlbum_sequence(-10000);
       album.setUser(user);
       this.albumService.save(album);
       request.getSession(false).removeAttribute("verify_code");
       if (this.configService.getSysConfig().isUc_bbs()) {
         UCClient client = new UCClient();
         String ret = client.uc_user_register(userName, password, email);
         int uid = Integer.parseInt(ret);
         if (uid <= 0) {
           if (uid == -1)
             System.out.print("用户名不合法");
           else if (uid == -2)
             System.out.print("包含要允许注册的词语");
           else if (uid == -3)
             System.out.print("用户名已经存在");
           else if (uid == -4)
             System.out.print("Email 格式有误");
           else if (uid == -5)
             System.out.print("Email 不允许注册");
           else if (uid == -6)
             System.out.print("该 Email 已经被注册");
           else
             System.out.print("未定义");
         }
         else {
           this.ucTools.active_user(userName, user.getPassword(), email);
         }
       }
       return "redirect:shopping_login.htm?username=" + 
         CommUtil.encode(userName) + "&password=" + password + "&encode=true";
     }
     return "redirect:register.htm";
   }
   
   /**
	 * 登录操作成功之后跳转判断
	 * @param request
	 * @param response
	 * @return
	 */
   @RequestMapping({"/user_login_success.htm"})
   public ModelAndView user_login_success(HttpServletRequest request, HttpServletResponse response)
   {
     ModelAndView mv = new JModelAndView("success.html", this.configService.getSysConfig(), this.userConfigService.getUserConfig(), 1, request, response);
     String url = CommUtil.getURL(request) + "/index.htm";
     String shopping_view_type = CommUtil.null2String(request.getSession(false).getAttribute("shopping_view_type"));
     //跳转到微信端
     if ((shopping_view_type != null) && (!shopping_view_type.equals("")) && (shopping_view_type.equals("wap"))) {
       String store_id = CommUtil.null2String(request.getSession(false).getAttribute("store_id"));
       mv = new JModelAndView("wap/success.html", 
         this.configService.getSysConfig(), 
         this.userConfigService.getUserConfig(), 1, request, response);
       url = CommUtil.getURL(request) + "/wap/index.htm?store_id=" + store_id;
     }
     HttpSession session = request.getSession(false);
     if ((session.getAttribute("refererUrl") != null) && 
       (!session.getAttribute("refererUrl").equals(""))) {
       url = (String)session.getAttribute("refererUrl");
       session.removeAttribute("refererUrl");
     }
     if (this.configService.getSysConfig().isUc_bbs()) {
       String uc_login_js = CommUtil.null2String(request.getSession(false).getAttribute("uc_login_js"));
       mv.addObject("uc_login_js", uc_login_js);
     }
     //第三方登录：QQ、新浪等
     String bind = CommUtil.null2String(request.getSession(false).getAttribute("bind"));
     if (!bind.equals("")) {
    	 mv = new JModelAndView(bind + "_login_bind.html", 
         this.configService.getSysConfig(), 
         this.userConfigService.getUserConfig(), 1, request, response);
       User user = SecurityUserHolder.getCurrentUser();

         mv.addObject("user", user);
       request.getSession(false).removeAttribute("bind");

     }
       String loginIp = request.getHeader("x-forwarded-for");
       if(loginIp == null || loginIp.length() == 0 || "unknown".equalsIgnoreCase(loginIp)) {
           loginIp = request.getHeader("Proxy-Client-loginIp");
       }
       if(loginIp == null || loginIp.length() == 0 || "unknown".equalsIgnoreCase(loginIp)) {
           loginIp = request.getHeader("WL-Proxy-Client-loginIp");
       }
       if(loginIp == null || loginIp.length() == 0 || "unknown".equalsIgnoreCase(loginIp)) {
           loginIp = request.getRemoteAddr();
       }
       String address = "";
       try {
           address = this.getAddresses("loginIp="+loginIp, "utf-8");
       } catch (UnsupportedEncodingException e) {
           e.printStackTrace();
       }
       mv.addObject("address",address);
     mv.addObject("op_title", "登录成功");
     mv.addObject("url", url);
     return mv;
   }

     /**
      * @param content 请求的参数 格式为：name=xxx&pwd=xxx
      *                服务器端请求编码。如GBK,UTF-8等
      * @return
      * @throws UnsupportedEncodingException
      */
     public String getAddresses(String content, String encodingString)
             throws UnsupportedEncodingException {
         // 这里调用pconline的接口
         String urlStr = "http://loginIp.taobao.com/service/getloginIpInfo.php";
         // 从http://whois.pconline.com.cn取得loginIp所在的省市区信息
         String returnStr = this.getResult(urlStr, content, encodingString);
         if (returnStr != null) {
             // 处理返回的省市区信息
             System.out.println(returnStr);
             String[] temp = returnStr.split(",");
             if (temp.length < 3) {
                 return "0";//无效loginIp，局域网测试
             }
             String region = (temp[5].split(":"))[1].replaceAll("\"", "");
             region = decodeUnicode(region);// 省份

             String country = "";
             String area = "";
             String city = "";
             String county = "";
             String isp = "";
             String address = "";
             for (int i = 0; i < temp.length; i++) {
                 switch (i) {
                     case 1:
                         country =  (temp[i].split(":"))[2].replaceAll("\"", "");
                         country =  decodeUnicode(country);//国家
                         break;
                     case 3:
                         area = (temp[i].split(":"))[1].replaceAll("\"", "");
                         area =  decodeUnicode(area);//地区
                         break;
                     case 5:
                         region = (temp[i].split(":"))[1].replaceAll("\"", "");
                         region =  decodeUnicode(region);//省份
                         break;
                     case 7:
                         city = (temp[i].split(":"))[1].replaceAll("\"", "");
                         city = decodeUnicode(city);//市区
                         break;
                     case 9:
                         county = (temp[i].split(":"))[1].replaceAll("\"", "");
                         county =  decodeUnicode(county);//地区
                         break;
                     case 11:
                         isp = (temp[i].split(":"))[1].replaceAll("\"", "");
                         isp =  decodeUnicode(isp);//ISP公司
                         break;
                 }
             }
             System.out.println(country + "=" + area + "=" + region + "=" + city + "=" + county + "=" + isp);
             address = country+area+region;
             if(null != county && !county.equals("")){
                address += county;
             }
             if(null != city && !city.equals("")){
                 address += city;
             }
             return address;
         }
         return null;
     }

     /**
      * @param urlStr
      *            请求的地址
      * @param content
      *            请求的参数 格式为：name=xxx&pwd=xxx
      * @param encoding
      *            服务器端请求编码。如GBK,UTF-8等
      * @return
      */
     private String getResult(String urlStr, String content, String encoding) {
         URL url = null;
         HttpURLConnection connection = null;
         try {
             url = new URL(urlStr);
             connection = (HttpURLConnection) url.openConnection();// 新建连接实例
             connection.setConnectTimeout(2000);// 设置连接超时时间，单位毫秒
             connection.setReadTimeout(2000);// 设置读取数据超时时间，单位毫秒
             connection.setDoOutput(true);// 是否打开输出流 true|false
             connection.setDoInput(true);// 是否打开输入流true|false
             connection.setRequestMethod("POST");// 提交方法POST|GET
             connection.setUseCaches(false);// 是否缓存true|false
             connection.connect();// 打开连接端口
             DataOutputStream out = new DataOutputStream(connection
                     .getOutputStream());// 打开输出流往对端服务器写数据
             out.writeBytes(content);// 写数据,也就是提交你的表单 name=xxx&pwd=xxx
             out.flush();// 刷新
             out.close();// 关闭输出流
             BufferedReader reader = new BufferedReader(new InputStreamReader(
                     connection.getInputStream(), encoding));// 往对端写完数据对端服务器返回数据
             // ,以BufferedReader流来读取
             StringBuffer buffer = new StringBuffer();
             String line = "";
             while ((line = reader.readLine()) != null) {
                 buffer.append(line);
             }
             reader.close();
             return buffer.toString();
         } catch (IOException e) {
             e.printStackTrace();
         } finally {
             if (connection != null) {
                 connection.disconnect();// 关闭连接
             }
         }
         return null;
     }
     /**
      * unicode 转换成 中文
      *
      * @author fanhui 2007-3-15
      * @param theString
      * @return
      */
     public static String decodeUnicode(String theString) {
         char aChar;
         int len = theString.length();
         StringBuffer outBuffer = new StringBuffer(len);
         for (int x = 0; x < len;) {
             aChar = theString.charAt(x++);
             if (aChar == '\\') {
                 aChar = theString.charAt(x++);
                 if (aChar == 'u') {
                     int value = 0;
                     for (int i = 0; i < 4; i++) {
                         aChar = theString.charAt(x++);
                         switch (aChar) {
                             case '0':
                             case '1':
                             case '2':
                             case '3':
                             case '4':
                             case '5':
                             case '6':
                             case '7':
                             case '8':
                             case '9':
                                 value = (value << 4) + aChar - '0';
                                 break;
                             case 'a':
                             case 'b':
                             case 'c':
                             case 'd':
                             case 'e':
                             case 'f':
                                 value = (value << 4) + 10 + aChar - 'a';
                                 break;
                             case 'A':
                             case 'B':
                             case 'C':
                             case 'D':
                             case 'E':
                             case 'F':
                                 value = (value << 4) + 10 + aChar - 'A';
                                 break;
                             default:
                                 throw new IllegalArgumentException(
                                         "Malformed      encoding.");
                         }
                     }
                     outBuffer.append((char) value);
                 } else {
                     if (aChar == 't') {
                         aChar = '\t';
                     } else if (aChar == 'r') {
                         aChar = '\r';
                     } else if (aChar == 'n') {
                         aChar = '\n';
                     } else if (aChar == 'f') {
                         aChar = '\f';
                     }
                     outBuffer.append(aChar);
                 }
             } else {
                 outBuffer.append(aChar);
             }
         }
         return outBuffer.toString();
     }

     /**
	 * 登录模态窗口
	 * @param request
	 * @param response
	 * @return
	 */
   @RequestMapping({"/user_dialog_login.htm"})
   public ModelAndView user_dialog_login(HttpServletRequest request, HttpServletResponse response)
   {
     ModelAndView mv = new JModelAndView("user_dialog_login.html", this.configService.getSysConfig(), 
       this.userConfigService.getUserConfig(), 1, request, response);
     return mv;
   }
   
   
   /** wap登录业务逻辑begin */
   
    @RequestMapping({ "/user/wap/login.htm" })
	public ModelAndView waplogin(HttpServletRequest request, HttpServletResponse response, String url) {
		
		ModelAndView mv = new JModelAndView("wap/login.html", this.configService.getSysConfig(),
				this.userConfigService.getUserConfig(), 1, request, response);
		request.getSession(false).removeAttribute("verify_code");
		
		boolean domain_error = CommUtil.null2Boolean(request.getSession(false).getAttribute("domain_error"));
		if ((url != null) && (!url.equals(""))) {
			request.getSession(false).setAttribute("refererUrl", url);
		}
		if (domain_error)
			mv = new JModelAndView("wap/error.html", this.configService.getSysConfig(), this.userConfigService.getUserConfig(), 1, request, response);
		else {
			mv.addObject("imageViewTools", this.imageViewTools);
		}
		mv.addObject("uc_logout_js", request.getSession(false).getAttribute("uc_logout_js"));
		
		/*String shopping_view_type = CommUtil.null2String(request.getSession(false).getAttribute("shopping_view_type"));
		
		if ((shopping_view_type != null) && (!shopping_view_type.equals("")) && (shopping_view_type.equals("wap"))) {
			//String store_id = CommUtil.null2String(request.getSession(false).getAttribute("store_id"));
			mv = new JModelAndView("wap/success.html", this.configService.getSysConfig(),
					this.userConfigService.getUserConfig(), 1, request, response);
			mv.addObject("op_title", "成功！");
			mv.addObject("url", CommUtil.getURL(request) + "/wap/index.htm");
		}*/

		return mv;
	}

	
}


 
 
 