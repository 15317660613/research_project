package com.adc.da.knowledge.util;

import com.adc.da.sys.dao.OrgEODao;
import com.adc.da.sys.entity.OrgEO;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.StringUtils;
import org.apache.commons.lang.StringEscapeUtils;

import java.util.*;



public class CommonUtil {
    private CommonUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 按指定大小，分隔集合，将集合按规定个数分为n个部分
     *
     * @param <T>
     * @param list
     * @param len
     * @return
     */
    public static <T> List<List<T>> splitList(List<T> list, int len) {

        if (list == null || list.isEmpty() || len < 1) {
            return Collections.emptyList();
        }

        List<List<T>> result = new ArrayList<>();

        int size = list.size();
        int count = (size + len - 1) / len;

        for (int i = 0; i < count; i++) {
            List<T> subList = list.subList(i * len, (Math.min((i + 1) * len, size)));
            result.add(subList);
        }

        return result;
    }


    public static String unescapeHtml(String value) {
      String str =  StringEscapeUtils.unescapeHtml(value);
      return str.replace("&amp;","&");
    }


    /**
     * @param userList
     * @return
     */
    public static List<Map<String, String>> userIdUsnameMapKv(List<UserEO> userList) {
        sortUserEOListDESC(userList);
        List<Map<String, String>> mapList = new ArrayList<>();
        for (UserEO userEO : userList) {
            Map<String, String> map = new HashMap<>();
            map.put("id", userEO.getUsid());
            map.put("name", userEO.getUsname());
            mapList.add(map);
        }
        return mapList;
    }

    public static void sortUserEOListDESC(List<UserEO> userEOList){
        Collections.sort(userEOList, new Comparator<UserEO>() {
            @Override
            public int compare(UserEO o1, UserEO o2) {
                if (StringUtils.isEmpty(o1.getExtInfo())) {
                    return 1;
                }
                if (StringUtils.isEmpty(o2.getExtInfo())) {
                    return -1;
                }
                return Integer.parseInt(o2.getExtInfo())-Integer.parseInt(o1.getExtInfo());
            }
        });
    }


    /**
     * @param userList
     * @return
     */
    public static Map<String, UserEO> userIdUserEOMap(List<UserEO> userList) {
        sortUserEOListDESC(userList);
        Map<String, UserEO> map = new HashMap<>();
        for (UserEO userEO : userList) {
            map.put("id", userEO);
        }
        return map;
    }

    /**
     * @param userList
     * @return
     */
    public static ArrayList<Map<String, String>> userIdDeptNamesMapKv(List<UserEO> userList, OrgEODao orgEODao) {
        sortUserEOListDESC(userList);
        ArrayList<Map<String, String>> userIdDeptNameMapList = new ArrayList<>();
        for (UserEO userEO : userList) {
            Map<String, String> userIdDeptNameMap = new HashMap<>();
            userIdDeptNameMap.put("id", userEO.getUsid());
            userIdDeptNameMap.put("name", getDeptNamesStr(userEO,orgEODao));
            userIdDeptNameMapList.add(userIdDeptNameMap);
        }
        return userIdDeptNameMapList;
    }
    /**
     * @param userList
     * @return
     */
    public static List<Map<String, String>> userIdDeptNamesMapKv(List<UserEO> userList, List<OrgEO> orgEOList) {
        sortUserEOListDESC(userList);
        List<Map<String, String>> userIdDeptNameMapList = new ArrayList<>();
        for (UserEO userEO : userList) {
            Map<String, String> userIdDeptNameMap = new HashMap<>();
            userIdDeptNameMap.put("id", userEO.getUsid());
            userIdDeptNameMap.put("name", getDeptNamesStr(userEO,orgEOList));
            userIdDeptNameMapList.add(userIdDeptNameMap);
        }
        return userIdDeptNameMapList;
    }

    /**
     * @param userList
     * @return
     */
    public static List<String> getUserNameList(List<UserEO> userList) {
        sortUserEOListDESC(userList);
        List<String> userNameList= new ArrayList<>();
        for (UserEO userEO : userList) {
            userNameList.add(userEO.getUsname());
        }
        return userNameList;
    }
    /**
     * @param userList
     * @return
     */
    public static String[] getUserNameArr(List<UserEO> userList) {
        sortUserEOListDESC(userList);
        List<String> userNameSet= new ArrayList<>();
        for (UserEO userEO : userList) {
            userNameSet.add(userEO.getUsname());
        }
        return userNameSet.toArray(new String[userNameSet.size()]);
    }

    /**
     * @param userList
     * @return
     */
    public static String[] getUserIdArr(List<UserEO> userList) {
        sortUserEOListDESC(userList);
        List<String> userIdSet= new ArrayList<>();
        for (UserEO userEO : userList) {
            userIdSet.add(userEO.getUsid());
        }
        return userIdSet.toArray(new String[userIdSet.size()]);
    }

    public static String getDeptNamesStr(UserEO userEO,OrgEODao orgEODao){
        List<String> deptNameList= new ArrayList<>();
        if (CollectionUtils.isNotEmpty(userEO.getOrgEOList())){
            OrgEO smallerOrgEO = sortOrgEOList(userEO.getOrgEOList()).get(0);
            deptNameList.add(smallerOrgEO.getName());
            OrgEO parentOrgEO = orgEODao.getOrgEOById(smallerOrgEO.getParentId());
            if (null != parentOrgEO ){
                deptNameList.add(parentOrgEO.getName());
            }
        }
        return StringUtils.join(deptNameList,',');
    }

    public static String getDeptNamesStr(UserEO userEO,List<OrgEO> orgEOList){
        List<String> deptNameList= new ArrayList<>();
        if (CollectionUtils.isNotEmpty(userEO.getOrgEOList())){
            OrgEO smallerOrgEO = sortOrgEOList(userEO.getOrgEOList()).get(0);
            deptNameList.add(smallerOrgEO.getName());
            OrgEO parentOrgEO = getOrgEOById(smallerOrgEO.getParentId(),orgEOList);
            if (null != parentOrgEO ){
                deptNameList.add(parentOrgEO.getName());
            }
        }
        return StringUtils.join(deptNameList,',');
    }

    public static OrgEO getOrgEOById(String orgId,List<OrgEO> orgEOList){
        for (OrgEO orgEO : orgEOList){
            if (StringUtils.equals(orgId,orgEO.getId())){
                return orgEO;
            }
        }

        return null;
    }


    public static List<String> getUserNamesArrayList(List<UserEO> userList){
        sortUserEOListDESC(userList);
        List<String> userNameList = new ArrayList<>();
        for (UserEO userEO : userList) {
            userNameList.add(userEO.getUsname());
        }
        return userNameList;
    }


    /**
     * @author dingqiang
     * @param arr 目标数组
     * @param key 目标字符串
     * @return 如果存在就返回对应的数组下标，不存在就返回-1
     */
    public static int arrayContains(String [] arr ,String key){
        if(CollectionUtils.isEmpty(arr)){
            return -1;
        }else {
            for (int i = 0 ; i < arr.length ; i ++){
                if (StringUtils.equals(arr[i],key)){
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * @author dingqiang
     * @param arr 目标数组
     * @param keys 目标字符串
     * @return 如果存在就返回对应的数组下标，不存在就返回-1
     */
    public static int arrayContainsSetOne(String [] arr ,Set<String> keys){
        if(CollectionUtils.isEmpty(arr)){
            return -1;
        }else if(CollectionUtils.isEmpty(keys)) {
            return  -1;
        }else{
            for (int i = 0 ; i < arr.length ; i ++) {
                for (String key : keys){
                    if (StringUtils.equals(arr[i], key)) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }

    public static boolean setContainsSetOne(Set<String> resSet ,Set<String> keys){
        if(CollectionUtils.isEmpty(resSet)){
            return false;
        }else if(CollectionUtils.isEmpty(keys)) {
            return  false;
        }else{
            for (String resKey : resSet) {
                for (String key : keys){
                    if (StringUtils.equals(resKey, key)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * @author dingqiang
     * @param arr 目标数组
     * @param index 要删去的值得下标
     * @return 返回删除后的数组
     */
    public static String[] removeOneByArrayIndex(String [] arr ,int index){
        List<String> arrayList = new ArrayList<>();
        if(CollectionUtils.isEmpty(arr)){
            return arr;
        }else if(index+1 > arr.length) {
            return  arr;
        }else{
            for (int i = 0 ; i < arr.length ; i ++) {
                    if (i != index){
                        arrayList.add(arr[i]);
                    }
                }
            return arrayList.toArray(new String[arrayList.size()]);
        }
    }


    //升序
    public static List<OrgEO> sortOrgEOList(List<OrgEO> orgEOList){
        Collections.sort(orgEOList, new Comparator<OrgEO>() {
            @Override
            public int compare(OrgEO o1, OrgEO o2) {
                return o2.getParentIds().length()- o1.getParentIds().length();
            }
        });
        return orgEOList ;
    }
}
