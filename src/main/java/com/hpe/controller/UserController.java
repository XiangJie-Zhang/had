package com.hpe.controller;


import com.alibaba.fastjson.JSONObject;
import com.hpe.entity.User;
import com.hpe.service.IUserService;
import com.hpe.service.NetDiskService;
import com.hpe.utils.HDFSUtils;
import com.hpe.utils.email.CodeRandom;
import com.hpe.utils.email.MailUtil;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zxj
 * @since 2019-04-10
 */
@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    IUserService iUserService;
    private static Logger logger = Logger.getLogger(UserController.class);
    private String emailCode = "";
    @Autowired
    private NetDiskService netDiskService;
    /**
     * @todo 用户登录
     * @since 获取当前用户，
     * 判断用户是否已经认证登录，
     * 用账号密码创建UsernamePasswordToken，
     * 调用subject的login方法
     * @param
     * @return
     */
    @RequestMapping(method = RequestMethod.POST,value = "login" ,produces = "text/plain;charset=utf-8")
    @ResponseBody
    public String login(@RequestParam("userCode")String userCode, @RequestParam("password")String password){
        JSONObject json = new JSONObject();
        Subject currentUser = SecurityUtils.getSubject();
        // 若果用户已经登陆，踢下
        if(currentUser.isAuthenticated()){
            currentUser.logout();
        }
        // 用户未登录
        if (!currentUser.isAuthenticated()) {
            UsernamePasswordToken upToken = new UsernamePasswordToken(userCode, password);
            upToken.setRememberMe(false);
            try {
                currentUser.login(upToken);
                json.put("code",200);
            } catch (IncorrectCredentialsException ice) {
                System.out.println("邮箱/密码不匹配！");
                json.put("code",300);
                json.put("msg", "The username and password do not match!");
            } catch (LockedAccountException lae) {
                System.out.println("账户已被冻结！");
            } catch (AuthenticationException ae) {
                System.out.println(ae.getMessage());
                json.put("msg", "账户不存在");
            }
        }else {
            json.put("msg", "当前用户已经登陆了");
        }
        return json.toString();
    }



    /**
     * 使用md5盐值加密用于注册用户信息，盐即为用户Id;用户id与用户名相同
     * @param user
     * @return
     */
    @RequestMapping(value = "registerUser", method = RequestMethod.POST)
    @ResponseBody
    public String registerUser(User user){
        JSONObject json = new JSONObject();
        User userByUserCode = iUserService.findUserByUserCode(user.getUserName());
        /**
         * 判断注册的用户是否存在
         */
        if (userByUserCode!=null){
            json.put("code", 300);
            json.put("msg", userByUserCode.getUserName() + "User already exists!");
            return json.toString();
        }

        /**
         * 判断用户输入的邮箱验证码是否错误
         */
        if (!emailCode.equals(user.getRealName())){
            json.put("code", 500);
            json.put("msg", "Mailbox verification code error!");
            return json.toString();
        }
        //加密密码
        String salt = HDFSUtils.salt();
        user.setSalt(salt);
        String password = new SimpleHash("MD5", user.getPasswd(), salt, 1024).toString();
        user.setPasswd(password);
        int i = 0;
        try {
            i = iUserService.addUser(user);
            netDiskService.createOrUpdateDir(user.getUserName(), "");
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        if (i != 0){
            json.put("code",200);
            json.put("msg", "register success!");
        }else {
            json.put("code",501);
            json.put("msg", "register failure!");
        }
        return json.toString();
    }

    /**
     * 接收邮箱，用于发送邮箱验证码
     * @param email
     * @return
     */
    @RequestMapping(value = "email",method = RequestMethod.POST)
    @ResponseBody
    public String sendEamil(String email){
        JSONObject json = new JSONObject();
        // 获取随机验证码
        emailCode = CodeRandom.getNumber();

        // 发送邮件给用户
        Runnable emailObject = new MailUtil(email, emailCode);
        Thread thread = new Thread(emailObject);

        try {
            thread.run();
            json.put("code",200);
        } catch (Exception e) {
            json.put("code",500);
            logger.error("邮件发送错误");
            logger.error(e.getMessage());
        }
        return json.toString();
    }


    @RequestMapping(value = "getUserMessage", method = RequestMethod.POST)
    @ResponseBody
    public String getUserMessage(@RequestParam("userCode") String userName){
        // 设置返回对象
        JSONObject json = new JSONObject();

        try {
            // 查询此用户是否存在
            User user = iUserService.findUserByUserCode(userName);
            // 用户不存在的话返回错误信息
            if (user == null){
                json.put("code", 500);
                json.put("msg", "User does not exist!");
                return json.toString();
            }else {
                json.put("code", 200);
                json.put("content", user);
            }
        }catch (Exception e){
            logger.error(e.getMessage());
        }

        return json.toString();
    }


    @RequestMapping(value = "updateUser", method = RequestMethod.POST)
    @ResponseBody
    public String updateUser(User user){
        // 设置返回对象
        JSONObject json = new JSONObject();
        int i = 0;
       if (user != null && !user.getUserName().isEmpty()  && !user.getUserId().isEmpty()){
           i = iUserService.updateUser(user);
       }
       User loginUser = (User) SecurityUtils.getSubject().getPrincipal();
       loginUser.setUserName(user.getUserName());
       if (i != 0){
           json.put("code", 200);
       }else {
           json.put("code", 500);
           json.put("msg", "Update failed!");
       }

        return json.toString();
    }
}
