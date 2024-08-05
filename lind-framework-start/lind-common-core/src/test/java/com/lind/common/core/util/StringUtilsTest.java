package com.lind.common.core.util;

import org.junit.jupiter.api.Test;

import static com.lind.common.core.util.StringUtils.isBlank;
import static com.lind.common.core.util.StringUtils.isNotBlank;
import static com.lind.common.core.util.StringUtils.join;
import static com.lind.common.core.util.StringUtils.split;

/**
 * @author lind
 * @date 2023/7/13 10:52
 * @since 1.0.0
 */
public class StringUtilsTest {

    @Test
    public void main() {
        System.out.println(isBlank("  "));
        System.out.println(isNotBlank("qwe"));
        System.out.println(split("a,b,cc,", ","));
        System.out.println(join(new String[] {"a", "b", "c"}, ","));
        String email = "abc@sina.com";
        System.out.println(email.substring(email.indexOf("@") + 1));
    }

}
