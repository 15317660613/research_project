package com.adc.da.research.project.vo;

import java.util.Objects;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: yanyujie
 * @Date: 2020/12/04/16:53
 * @Description:
 */
public class DifferencePeriodicalVO {
    private String ext1;
    private String ext2;
    private String createUserName;

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public String getExt1() {
        return ext1;
    }

    public void setExt1(String ext1) {
        this.ext1 = ext1;
    }

    public String getExt2() {
        return ext2;
    }

    public void setExt2(String ext2) {
        this.ext2 = ext2;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DifferencePeriodicalVO that = (DifferencePeriodicalVO) o;
        return Objects.equals(ext1, that.ext1) && Objects.equals(ext2, that.ext2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ext1, ext2);
    }
}
