package com.ricoandilet.springtools.exception;

import cn.hutool.core.lang.Singleton;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;

/**
 * @author rico
 */
public interface IResponseEnum {
    /**
     * get business error code
     * @return code
     */
    String getErrorCode();

    /**
     * get business error message
     * @return message
     */
    String getErrorMessage();

    /**
     * get localized business error message
     * @param args params
     * @return message
     */
    default String getLocalizedMessage(Object... args) {
        String code = I18N_KEY_PREFIX + this;
        ResourceBundleMessageSource source = Singleton.get(ResourceBundleMessageSource.class);
        source.setBasenames("i18n.commons.messages","i18n.messages","messages");
        return source.getMessage(code, args, getErrorMessage(), LocaleContextHolder.getLocale());
    }

    /**
     * Internationalized Key prefix
     */
    String I18N_KEY_PREFIX = "error.";
}
