package com.yjb.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author yjb
 * @since 2019-09-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Login extends Model<Login> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "login_id", type = IdType.AUTO)
    private String loginId;

    private LocalDateTime loginAdate;

    private LocalDateTime loginCdate;

    private String loginEmail;

    private String loginIp;

    private String loginPwd;

    private String loginUsername;


    @Override
    protected Serializable pkVal() {
        return this.loginId;
    }

}
