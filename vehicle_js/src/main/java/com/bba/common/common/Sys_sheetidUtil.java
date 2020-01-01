package com.bba.common.common;

import com.bba.common.vo.ResultVO;
import com.bba.util.HttpRequestUtils;
import com.bba.util.JSONUtils;

public class Sys_sheetidUtil {


    public static final String HT_CUS = "HT_CUS";

    public static final String HT_CARRIER = "HT_CARRIER";

    public static final String JS_VIN_AMOUNT = "JS_VIN_AMOUNT";

    public static final String JS_DZ_SHEET = "JS_DZ_SHEET";

    public static final String JS_VIN_DOWN_AMOUNT = "JS_VIN_DOWN_AMOUNT";

    public static final String JS_LEDGER = "JS_LEDGER";

    public static final String JS_RECEIVABLE_INVOICE = "JS_RECEIVABLE_INVOICE";

    public static final String JS_PAYMENT_INVOICE = "JS_PAYMENT_INVOICE";

    public static String getSys_sheetid(String key){
        String json = HttpRequestUtils.sendPost("http://localhost:9101/vehicle_js/sysInfo/SheelNo/getSys_sheetid",
                "p_within_code=TMS&p_whcenter=TMS&p_tablename="+key);
        ResultVO resultVO = JSONUtils.toJSONObject(ResultVO.class, json);
        if("0".equals(resultVO.getResultCode())){
            return String.valueOf(resultVO.getResultDataFull());
        }
        throw new RuntimeException(String.valueOf(resultVO.getResultDataFull()));
    }

}
