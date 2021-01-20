package com.adc.da.budget.vo;

import com.adc.da.util.utils.StringUtils;
import lombok.Data;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

@Data
public class UserProjectCustomVO {

    private String id;

    private String name;

    private String parentId;

    private String userId;

    private String userName;
    /**0不显示 1显示*/
    private String status;

    /** 1业务2 项目 3任务 4子任务 */
    private int type;

    private String projectType;

    private String contractNO;

    private String contractName;

    private String contractOwner;

    /**
     * 层级
     * 1 业务
     * 2 项目  业务任务
     * 3 项目任务 业务子任务
     * 4 项目子任务
     */
    private int treeLevel;

    /**
     *  是否是叶子
     */
    private boolean leafFlag;

    public UserProjectCustomVO() {

    }

    public UserProjectCustomVO(String id, String name, String parentId, String userId, String userName,
        String status, int type, String projectType,
        String contractNO, String contractName, String contractOwner) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
        this.userId = userId;
        this.userName = userName;
        this.status = status;
        this.type = type;
        this.projectType = projectType;
        this.contractNO = contractNO;
        this.contractName = contractName;
        this.contractOwner = contractOwner;
    }

    // 获取汉字的首字母大写
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
                    if (temp != null && temp.length > 0) {
                        pybf.append(temp[0]);// 取首字母
                    } else {
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
