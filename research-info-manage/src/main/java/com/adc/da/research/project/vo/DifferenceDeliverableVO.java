package com.adc.da.research.project.vo;

import java.util.Objects;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: yanyujie
 * @Date: 2020/12/04/17:14
 * @Description:
 */
public class DifferenceDeliverableVO {
    private String deliverableName;
    private String deliverableTarget;
    private String createUserName;

    public String getDeliverableName() {
        return deliverableName;
    }

    public void setDeliverableName(String deliverableName) {
        this.deliverableName = deliverableName;
    }

    public String getDeliverableTarget() {
        return deliverableTarget;
    }

    public void setDeliverableTarget(String deliverableTarget) {
        this.deliverableTarget = deliverableTarget;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DifferenceDeliverableVO that = (DifferenceDeliverableVO) o;
        return Objects.equals(deliverableName, that.deliverableName) && Objects.equals(deliverableTarget, that.deliverableTarget);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deliverableName, deliverableTarget);
    }
}
