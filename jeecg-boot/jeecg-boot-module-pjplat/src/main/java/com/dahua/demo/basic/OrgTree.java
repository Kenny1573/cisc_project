package com.dahua.demo.basic;

import com.dahua.demo.util.BaseUserInfo;
import com.dahua.demo.util.HttpEnum;
import com.dahua.demo.util.HttpTestUtils;
import com.google.gson.Gson;

import java.util.List;
import java.util.Map;

/**
 * 分级获取组织接口
 *  注意：使用前请根据实际情况修改请求的参数，修改完后调用main方法即可
 * 	需要修改的地方为content中id，nodeType，typeCode等参数
 */
class OrgTree extends BaseUserInfo
{
    public static final String ACTION = "/videoService/devicesManager/deviceTree";

    public static int a=-1;//机构层级标志

    public static String mark = "";//打印结果的空格标志

    private static String getOrgTree(String ip, int port, String token) throws Exception{
        //先获取根组织Id
        /**
         * id : 类型string ，必填。要查询组织的惟一编码。查询根组织时不需要填值
         * nodeType : 类型int ，必填。固定为1,表示组织
         * typeCode : 类型string ，必填。检索类型，查询组织"01"。
         */
        String content = "?id=" +
                        "&nodeType=3" +
                        "&typeCode=01" +
                        "&page=1" +
                        "&pageSize=100";
        String response=HttpTestUtils.httpRequest(HttpEnum.GET,ip,port,ACTION,token,content);
        System.out.println(response);
        Map<String, Object> rsp = new Gson().fromJson(response, Map.class);
        List<Map<String, Object>> arr = (List<Map<String, Object>>)rsp.get("results");
        if (arr!=null&&arr.size()>0) {
            String rootId = (String) arr.get(0).get("id");
            System.out.println("Root Org Code is "+rootId);
            //获取根组织下的组织列表，此处为获取一级组织下的组织的id，需要获取二级组织的话，可以将此处的rootid替换为获取的一级组织的id
            content = "?id="+rootId+"&nodeType=1&typeCode=01&page=1&pageSize=100";
            String response1=HttpTestUtils.httpRequest(HttpEnum.GET,ip,port,ACTION,token,content);
            rsp = new Gson().fromJson(response1, Map.class);
            arr = (List<Map<String, Object>>)rsp.get("results");
            for (Map<String, Object> node : arr)
            {
                System.out.println("一级组织的orgCode为:"+node.get("orgName")+"="+node.get("id"));
            }
        }
        return response;
    }
    //此方法可以获取到当前环境所有的组织机构树，和每一个具体的机构所包含的通道
    private static void getSub(String code) throws Exception {
        String content = "?id="+code+"&nodeType=1&typeCode=01&page=1&pageSize=100";
        String response=HttpTestUtils.httpRequest(HttpEnum.GET,ip, Integer.valueOf(port),ACTION,token,content);
        List<Map<String,Object>> arr = (List<Map<String, Object>>)new Gson().fromJson(response, Map.class).get("results");
        if(arr!=null&&arr.size()>0){
            for (Map<String, Object> node : arr)
            {
                a++;
                mark+="   ";
                System.out.println(mark+a+"级组织为:"+node.get("orgName")+"=(组织编码)"+node.get("id"));
                DevInfo.getOrgDevTree((String) node.get("id"), mark);
                getSub((String) node.get("id"));
                mark=mark.substring(0,mark.length()-3);
                a--;
            }
        }else{
            return ;
        }
    }

    //两个方法按需使用，一般只使用一个
    public static void main(String[] args) throws Exception {
        //此方法只能查询根组织以及一级组织的组织名称和组织编号
//        getOrgTree(ip, Integer.valueOf(port), token);
        //此方法为获取组织设备树的方法，可以获取到当前平台所有的组织以及组织下属的通道。
        getSub("");
    }


}



