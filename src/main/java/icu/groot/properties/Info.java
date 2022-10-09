package icu.groot.properties;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @JavaVersion : java version 1.8+
 * @Author : Alan
 * @ClassName : Info
 * @Version : 1.0.0
 * @Date : 2022/8/25 19:31
 * @Description :
 */
@Data
@Component
public class Info {
    private String birthdayTa;
    private String birthdayMe;
    private String jilianri;
    private String administrativeCode;
    private String note;
}
