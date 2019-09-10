package io.github.viscent.mtia.ch6.case01;

import java.text.DecimalFormat;

public class SmsVerficationCodeSender {
    /**
     * 生成并下发验证码短信到指定的手机号码。
     *
     * @param msisdn 短信接收方号码。
     */
    @SuppressWarnings("unused")
    public void sendVerificationSms(final String msisdn) {
        String verificationCode = generateVerificationCode();
        // 省略其他代码
    }

    private String generateVerificationCode() {
        // 生成强随机数验证码
        int verificationCode = ThreadSpecificSecureRandom.INSTANCE.nextInt(999999);
        DecimalFormat df = new DecimalFormat("000000");
        String txtVerCode = df.format(verificationCode);
        return txtVerCode;
    }
}
