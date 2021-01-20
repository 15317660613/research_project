package com.adc.da.budget.dto;

import lombok.Data;

import java.io.Serializable;


/**
 * 项目日程响应DTO
 * created by chenhaidong 2018/11/22
 */
@Data
public class ProjectScheduleResponseDTO implements Serializable {
    //日报ID
    private String id;
    //任务id
//    private String taskId;
    //任务投入工时
    private String taskWorktime;
    //日报名称
    private String dailyName;
    //工作描述
    private String description;
    //是否完成
    private Boolean complete;
    //日报创建人名称
    private String personName;
    // 参与人员
    private String personId;
    //日报创建时间
    private String dailyCreateTime;
    //任务id列表
    private String[] taskIdArray;
    //任务处理状态
    private String[] taskStatusArray;
    //处理任务的工时数据
    private float[] worktimeArray;
    //项目id
    private String projectId;
    //项目名称
    private String projectName;
    //任务名称列表
    private String[] taskNameArray;


//    @Override
//    public ProjectScheduleResponseDTO clone() {
//        ProjectScheduleResponseDTO projectScheduleResponseDTO = null;
//        ObjectOutputStream out = null;
//        ObjectInputStream in = null;
//        try{
//            projectScheduleResponseDTO = (ProjectScheduleResponseDTO)super.clone();

            //将该对象序列化成流,因为写在流里的是对象的一个拷贝
//            ByteArrayOutputStream bout = new ByteArrayOutputStream();
//            out = new ObjectOutputStream(bout);
//            out.writeObject(this);
//            out.close();
//
//            // read a clone of the object from the byte array
//            in = new ObjectInputStream(new ByteArrayInputStream(bout.toByteArray()));
//            projectScheduleResponseDTO = (ProjectScheduleResponseDTO)in.readObject();
//            in.close();
//        }catch(CloneNotSupportedException e) {
//            e.printStackTrace();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        finally {
//            try {
//                if(out != null) out.close();
//                if(in != null) in.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return projectScheduleResponseDTO;
//    }


}
