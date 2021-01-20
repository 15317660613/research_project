package com.adc.da.budget.vo;

import com.adc.da.util.utils.StringUtils;
import lombok.Data;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * 信息化模块树
 */
@Data
public class ListTreeVO{

    private String id;

    private String name;

    /**
     * 上级节点， 0表示为第一层
     */
    private String parentId;

    /**
     *
     * 0 业务
     * 1 项目
     * 2 任务
     * 3 子任务
     *
     */
    private int type;

    private String projectType;

    private String contractNO;

    private String contractName;

    private String contractOwner;

    /**
     * 负责人id
     */
    private String userId;

    /**
     * 负责人姓名
     */
    private String userName;

   // public ListTreeVO(){}

    public ListTreeVO(String id, String name, String parentId, int type,String userId,String userName) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
        this.type = type;
        this.userId = userId;
        this.userName = userName;
    }

    public ListTreeVO(String id, String name, String parentId, int type, String projectType,String userId,String userName) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
        this.type = type;
        this.projectType = projectType;
        this.userId = userId;
        this.userName = userName;
    }

    public ListTreeVO(String id, String name, String parentId, int type, String projectType,
                      String contractNO, String contractName, String contractOwner,String userId,String userName) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
        this.type = type;
        this.projectType = projectType;
        this.contractNO = contractNO;
        this.contractName = contractName;
        this.contractOwner = contractOwner;
        this.userId = userId;
        this.userName = userName;
    }

    /**
     * 获取汉字的首字母大写
      */
    public String getFirstSpell() {
        StringBuffer pybf = new StringBuffer();
        if (StringUtils.isEmpty(this.getName())) {
            return "z";
        } else {
            HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
            defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
            defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
            char firstChar = this.getName().charAt(0);
                if (firstChar > 128) { //如果已经是字母就不用转换了
                    try {
                        //获取当前汉字的全拼
                        String[] temp = PinyinHelper.toHanyuPinyinStringArray(firstChar, defaultFormat);
                        if (temp != null&&temp.length>0) {
                            pybf.append(temp[0]);// 取首字母
                        }else {
                            pybf.append(firstChar);
                        }
                    } catch (BadHanyuPinyinOutputFormatCombination e) {
                        e.printStackTrace();
                    }
                } else {
                    if (firstChar >= 'a' && firstChar <= 'z') {
                        firstChar -= 32;
                    }
                    pybf.append(firstChar
                    );
                }
            }
            return pybf.toString();
        }

}
