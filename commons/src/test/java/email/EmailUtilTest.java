/*
  Copyright (C) 2018-2021 YouYu information technology (Shanghai) Co., Ltd.
  <p>
  All right reserved.
  <p>
  This software is the confidential and proprietary
  information of YouYu Company of China.
  ("Confidential Information"). You shall not disclose
  such Confidential Information and shall use it only
  in accordance with the terms of the contract agreement
  you entered into with YouYu inc.
 */
package email;

import com.ricoandilet.commons.utils.email.BaseEmailUtil;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

/**
 * @author: rico
 * @date: 2022/12/10
 **/
public class EmailUtilTest {

  @Test
  void baseEmail() {

    Assert.isTrue(
        BaseEmailUtil.sendTextEmail(
            "Hello,rico",
            "ricomusk@126.com",
            "ricomusk@outlook.com",
            "ricoandilet@outlook.com",
            "text2"),"send email fail.");
           }

}
