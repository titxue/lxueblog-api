package com.titxu.blog.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import com.alibaba.fastjson.JSON;
import com.titxu.blog.dao.mapper.SysUserMapper;
import com.titxu.blog.dao.pojo.SysUser;
import com.titxu.blog.service.LoginService;
import com.titxu.blog.service.SysUserService;
import com.titxu.blog.service.ThreadService;
import com.titxu.blog.utils.JwtUtils;
import com.titxu.blog.utils.QiniuUtils;
import com.titxu.blog.utils.Tools;
import com.titxu.blog.utils.result.CodeMsg;
import com.titxu.blog.utils.result.Result;
import com.titxu.blog.vo.params.LoginParams;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private QiniuUtils qiniuUtils;
    /**
     * 登录
     * 1.检查参数是否合法
     * 2.根据用户名和密码去user表查询
     * 3.登录失败
     * 4.存在 使用jwt生产token 返回前端
     * 5.token保存到redis  token对应user info 设置过期时间
     *
     * @param loginParams
     * @return
     */
    @Autowired
    private ThreadService threadService;

    @Override
    public Result login(LoginParams loginParams) {
        String account = loginParams.getAccount();
        String password = loginParams.getPassword();

        if (StringUtils.isBlank(account) || StringUtils.isBlank(password)) {
            return Result.fail(CodeMsg.PARAMS_ERROR.getRetCode(), CodeMsg.PARAMS_ERROR.getMessage());
        }
        password = Tools.getSlatPassword(password);
        SysUser sysUser = sysUserService.findUser(account, password);
        if (sysUser == null) {
            return Result.fail(CodeMsg.ACCOUNT_PASSWORD_NOT_EXIST.getRetCode(), CodeMsg.TOKEN_PERMISSION_NOT.getMessage());
        }


        int updateById = this.sysUserMapper.updateById(sysUser);
        if (updateById < 0) {
            return Result.fail(CodeMsg.LOGIN_ERROR.getRetCode(), CodeMsg.LOGIN_ERROR.getMessage());
        }

        String token = JwtUtils.createToken(sysUser.getId());
        redisTemplate.opsForValue().set("TOKEN_" + token, JSON.toJSONString(sysUser), 1, TimeUnit.DAYS);


        return Result.success(token);


    }

    @Override
    public Result logout(String token) {
        redisTemplate.delete("TOKEN_" + token);

        return Result.success("退出成功");
    }


    /**
     * 返回二维码url 和uuid
     * @return
     */
    @SneakyThrows
    @Override
    public Result loginQr() {
        String uuid = IdUtil.randomUUID();
        // 生成指定url对应的二维码到文件，宽和高都是300像素
        BufferedImage generate = QrCodeUtil.generate(uuid, 300, 300);
        MultipartFile multipartFile = toMultipartFile(generate);
        boolean upload = qiniuUtils.upload(multipartFile, uuid);
        Map<String, String> map = new HashMap<>();
        map.put("uuid",uuid);
        map.put("img",QiniuUtils.url + uuid);
        if (upload) {
            return Result.success(map);
        }
        return Result.fail(CodeMsg.IMAGE_UPLOAD_ERROR.getRetCode(), CodeMsg.IMAGE_UPLOAD_ERROR.getMessage());
    }

    /**
     * 类型转换 BufferedImage > MultipartFile
     * @param bufferedImage
     * @return
     */
    @SneakyThrows
    private MultipartFile toMultipartFile(BufferedImage bufferedImage) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "jpg", os);
        InputStream input = new ByteArrayInputStream(os.toByteArray());
        MultipartFile multipartFile = new MockMultipartFile("file", "file.jpg", "text/plain", input);

        return multipartFile;


    }

}
