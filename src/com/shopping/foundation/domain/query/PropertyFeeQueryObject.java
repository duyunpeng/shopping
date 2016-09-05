package com.shopping.foundation.domain.query;

import com.shopping.core.query.QueryObject;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by dyp on 2016/8/26.
 */
public class PropertyFeeQueryObject extends QueryObject {
    public PropertyFeeQueryObject(String currentPage, ModelAndView mv, String orderBy, String orderType) {
        super(currentPage, mv, orderBy, orderType);
    }

    public PropertyFeeQueryObject() {
    }
}
