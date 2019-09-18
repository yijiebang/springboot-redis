package com.yjb.web.service.impl;

import com.yjb.web.entity.Login;
import com.yjb.web.mapper.LoginMapper;
import com.yjb.web.service.ILoginService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yjb
 * @since 2019-09-17
 */
@Service
public class LoginServiceImpl extends ServiceImpl<LoginMapper, Login> implements ILoginService {

}
