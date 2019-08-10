package com.nowcoder.community.controller;

import com.nowcoder.community.Service.UserService;
import com.nowcoder.community.annotation.LoginRequired;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.util.CommunityUtils;
import com.nowcoder.community.util.HostHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;


@Controller
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @Value("${community.path.upload}")
    private String uploadPath;

    @Value("${community.path.domain}")
    private String domain;

    @LoginRequired
    @RequestMapping(path = "/setting", method = RequestMethod.GET)
    public String getSitePage() {
        return "site/setting";
    }

    /**
     * 把上传的图片存到服务器，并修改对应user的url
     * @param headerImage SpringMVC提供的专有类型，页面如果传入多个就用MultipartFile数组
     * @return
     */
    @LoginRequired
    @RequestMapping(path = "/upload", method = RequestMethod.POST)
    public String uploadHeader(MultipartFile headerImage, Model model) {
        if (headerImage == null) {
            model.addAttribute("error","您还没有选择图片");
            return "/site/setting";
        }
        /**
         * 读取文件的后缀，暂存，判断文件格式是否正确
         * 生成随机文件名，并且加上后缀
         * 确定文件存放的路径
         * 更新当前用户的的头像的路径（web访问路径）
         */
        String filename = headerImage.getOriginalFilename();
        String suffix = filename.substring(filename.lastIndexOf("."));
        if (StringUtils.isBlank(suffix)) {
            model.addAttribute("error","文件格式不正确");
            return "/site/setting";
        }
        filename = CommunityUtils.generateUUID() + suffix;
        File dest = new File(uploadPath+"/"+filename);
        try {
            //transferTo的方法直接把headerImage存到指定的目标文件
            headerImage.transferTo(dest);
        } catch (IOException e) {
            logger.error("上传文件失败: " + e.getMessage());
            throw new RuntimeException("上传文件失败,服务器发生异常!", e);
        }
        // 更新当前用户的头像的路径(web访问路径)
        // http://localhost:8080/user/header/xxx.png
        //通过hostHolder获取到当前登录的用户
        User user = hostHolder.getUser();
        //多线程问题？？
        //user.setHeaderUrl(domain+"/user/header/"+filename);
        String headerUrl = domain+"/user/header/"+filename;
        userService.updateHeader(user.getId(),headerUrl);

        return "redirect:/index";
    }

    /**
     * 这个方法很特别，给浏览器返回的不是网页也不是字符串，而是一个二进制数据，
     * 手动调response来写
     * @param fileName
     * @param response
     */

    @RequestMapping(path = "/header/{fileName}", method = RequestMethod.GET)
    @ResponseBody
    public void getHeader(@PathVariable("fileName") String fileName, HttpServletResponse response) {
        //解析服务器存放路径
        fileName = uploadPath + "/" + fileName;
        //读取文件后缀,因为向浏览器输出的是图片，所以需要根据图片格式进行设置
        String suffix = fileName.substring(fileName.lastIndexOf('.') + 1);
        //给response设置响应图片response.setContentType("image/png")
        response.setContentType("image/" + suffix);
        //图片是二进制，需要字节流
        //放在这个括号里，在执行完try-catch之后会自动关闭，释放资源
        //开一个文件输入流
        FileInputStream fis = null;
        //从response中获取输出流
        OutputStream os = null;
        //一批一批输出效率更高，而不是一个字节一个字节地输出
        try {
            fis = new FileInputStream(fileName);
            os = response.getOutputStream();
            byte[] buffer = new byte[1024];
            int b = 0;
            while ((b = fis.read(buffer)) != -1) {
                //从0开始一直到读完（ b = -1）
                os.write(buffer,0,b);
            }

        } catch (IOException e) {
            logger.error("读取头像失败: " + e.getMessage());
        }finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
    @LoginRequired
    @RequestMapping(path = "/updatePassword",method = RequestMethod.POST)
    public String updatePassword(Model model,String oldPassword, String newPassword) {
        /**
         * 传入新密码和旧密码
         * 1、输出新、旧密码不能为空
         * 2、判断旧密码是否正确
         * 3、更改密码(加密)
         */
        if (StringUtils.isBlank(oldPassword)){
            model.addAttribute("oldPasswordMsg","请输入原始密码");
            return "/site/setting";
        }
        if (StringUtils.isBlank(newPassword)) {
            model.addAttribute("newPasswordMsg","密码不能为空");
            return "/site/setting";
        }
        User user = hostHolder.getUser();
        if (!CommunityUtils.md5(oldPassword + user.getSalt()).equals(user.getPassword())) {
            model.addAttribute("oldPasswordMsg","密码错误");
            return "/site/setting";
        }
        userService.updatePassword(user.getId(),newPassword);
        return "redirect:/index";
    }
}
