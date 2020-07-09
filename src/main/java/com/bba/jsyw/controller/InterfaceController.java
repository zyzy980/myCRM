package com.bba.jsyw.controller;
import com.bba.common.interceptor.Log;
import com.bba.common.vo.ResultVO;
import com.bba.jsyw.service.api.IInterfaceService;
import com.bba.jsyw.vo.InterfaceResultVO;
import com.bba.jsyw.vo.Tr_BusinessVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * 业务系统--结算系统接口
 * @Author: Suwendaidi
 * @Date: 2019/7/24 11:51
 * */
@Controller
@RequestMapping("interface/vljsdata")
public class InterfaceController {

	@Autowired
	private IInterfaceService interfaceService;
	/**
	 * 接收业务数据*********************************************（暂时弃用）
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/js_businessData", method = { RequestMethod.POST,RequestMethod.GET })
	public @ResponseBody InterfaceResultVO Vl_businessDataToJs(HttpServletRequest request, HttpServletResponse response) throws Exception {
        /**接口逻辑：业务数据交车之后，将数据推送到结算系统*/
        /**数据过来时，需要先判断运输方式是否存在于结算系统，存在跳过不存在插入到系统*/
		/*request.setCharacterEncoding("utf-8");
		String jsonData = request.getParameter("jsonData");*/
        BufferedReader reader = null;
        StringBuilder sb1 = new StringBuilder();
        try {
            reader = new BufferedReader(new InputStreamReader(request.getInputStream(), "utf-8"));
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb1.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != reader) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String jsonData = sb1.toString();
        return interfaceService.Vl_BusinessDataToJs(jsonData);
    }


    @Log(value = "业务管理--更新业务数据")
    @RequestMapping(value = "/GetBusinessData", method = { RequestMethod.POST })
    public @ResponseBody ResultVO GetEstablish(HttpServletRequest request, HttpSession session){
	    /**点击按钮，刷新所有未更新到系统的数据*/
        /**第一步，切换数据库，抓取未推送的数据*/
        List<Tr_BusinessVO> list = new ArrayList<Tr_BusinessVO>();
        try {
            list = interfaceService.getVlBusinessList();
            interfaceService.updateVLdataToY();
        } catch (Exception e) {
            throw new RuntimeException("获取VL数据失败，请联系管理员"+e.getMessage());
        }
        if (list.size()==0) {
            return ResultVO.failResult("没有可以结算的数据，请稍后再试");
        } else {
            //1、插入数据到业务+明细
            try {
                interfaceService.insertToBusinessAndDetail(list);
            }catch (Exception e) {
                //失败后，回写数据
                interfaceService.updateVLdataToN(list);
                throw new RuntimeException("插入数据失败，请联系管理员"+e.getMessage());
            }
        }
        return ResultVO.successResult("获取结算数据成功");
    }
}
