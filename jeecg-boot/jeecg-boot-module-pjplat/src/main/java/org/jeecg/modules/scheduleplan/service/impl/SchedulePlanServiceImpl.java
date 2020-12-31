package org.jeecg.modules.scheduleplan.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jeecg.modules.scheduleplan.entity.SchedulePlan;
import org.jeecg.modules.scheduleplan.entity.ModalTree;
import org.jeecg.modules.scheduleplan.mapper.SchedulePlanMapper;
import org.jeecg.modules.scheduleplan.service.ISchedulePlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.sql.Array;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 里程碑计划
 * @Author: jeecg-boot
 * @Date:   2020-11-06
 * @Version: V1.0
 */
@Service
public class SchedulePlanServiceImpl extends ServiceImpl<SchedulePlanMapper, SchedulePlan> implements ISchedulePlanService {
    @Autowired
    SchedulePlanMapper schedulePlanMapper;

    @Override
    public List<ModalTree> getMenu(Long prjId) {
        List<SchedulePlan> schedulePlanList = schedulePlanMapper.getInitMenu(prjId);
        List<ModalTree> treeList = getInitSchedule();
        treeList=getChildrenSchedule(schedulePlanList,treeList);
        return treeList;
    }

    @Override
    public JSONObject getGantt(Long prjId) {
        List<SchedulePlan> schedulePlanList = schedulePlanMapper.getInitMenu(prjId);
        JSONArray array = new JSONArray();
        JSONObject object = new JSONObject();
        if(schedulePlanList.size()>0) {
            schedulePlanList.sort(new Comparator<SchedulePlan>() {
                @Override
                public int compare(SchedulePlan schedulePlan, SchedulePlan t1) {
                    if(schedulePlan.getSpNodeInFactBegin()!=null&&t1.getSpNodeInFactBegin()!=null) {
                        if (schedulePlan.getSpNodeInFactBegin().before(t1.getSpNodeInFactBegin()))
                            return -1;

                    }
                        return 1;
                }
            });
            Date minStart = new Date();
            if (schedulePlanList.get(0).getSpNodeInFactBegin()!=null)
                minStart=schedulePlanList.get(0).getSpNodeInFactBegin();
            schedulePlanList.sort(new Comparator<SchedulePlan>() {
                @Override
                public int compare(SchedulePlan schedulePlan, SchedulePlan t1) {
                    if(schedulePlan.getSpNodeEndTime()!=null&&t1.getSpNodeEndTime()!=null) {
                        if (schedulePlan.getSpNodeEndTime().before(t1.getSpNodeEndTime()))
                            return 1;
                    }
                        return -1;
                }
            });
            Date maxEnd=new Date();
            if (schedulePlanList.get(0).getSpNodeInFactEnd()!=null) {
                maxEnd = schedulePlanList.get(0).getSpNodeInFactEnd();
            }
            schedulePlanList.sort(new Comparator<SchedulePlan>() {
                @Override
                public int compare(SchedulePlan schedulePlan, SchedulePlan t1) {
                    if(schedulePlan.getSpNodeInFactEnd()!=null&&t1.getSpNodeInFactEnd()!=null) {
                        if (schedulePlan.getSpNodeInFactEnd().before(t1.getSpNodeInFactEnd()))
                            return 1;
                    }
                        return -1;
                }
            });
            if (schedulePlanList.get(0).getSpNodeInFactEnd()!=null) {
                if (!schedulePlanList.get(0).getSpNodeInFactEnd().before(maxEnd))
                    maxEnd = schedulePlanList.get(0).getSpNodeInFactEnd();
            }
            schedulePlanList.sort(new Comparator<SchedulePlan>() {
                @Override
                public int compare(SchedulePlan schedulePlan, SchedulePlan t1) {
                    if(schedulePlan.getSpNodeBeginTime()!=null&&t1.getSpNodeBeginTime()!=null) {
                        if (schedulePlan.getSpNodeBeginTime().before(t1.getSpNodeBeginTime()))
                            return 1;
                    }
                        return -1;
                }
            });
            if (schedulePlanList.get(0).getSpNodeBeginTime().before(minStart))
                minStart = schedulePlanList.get(0).getSpNodeBeginTime();
            schedulePlanList.sort(new Comparator<SchedulePlan>() {
                @Override
                public int compare(SchedulePlan schedulePlan, SchedulePlan t1) {
                    if(schedulePlan.getSpNodeBeginTime()!=null&&t1.getSpNodeBeginTime()!=null) {
                        if (schedulePlan.getSpNodeBeginTime().before(t1.getSpNodeBeginTime()))
                            return -1;
                    }else{
                        if (t1.getSpNodeBeginTime().before(schedulePlan.getSpNodeBeginTime()))
                            return 1;
                    }
                    return 0;
                }
            });
            schedulePlanList.forEach(item -> {


                JSONObject jsonObject = new JSONObject();
                jsonObject.put("status", item.getSpNodeState());

                jsonObject.put("node", item.getSpNodeName());
                jsonObject.put("id", item.getId());
                jsonObject.put("name", "计划");
                JSONArray gtArray = new JSONArray();
                JSONObject plan = new JSONObject();
                plan.put("name", "计划时间");
                plan.put("start", item.getSpNodeBeginTime());
                plan.put("end", item.getSpNodeEndTime());
                gtArray.add(plan);
                jsonObject.put("gtArray", gtArray);
                array.add(jsonObject);
                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("id", item.getId());
                jsonObject1.put("name", "实际");
                JSONArray gtArray1 = new JSONArray();
                JSONObject inFact = new JSONObject();
                inFact.put("name", "实际时间");
                inFact.put("start", item.getSpNodeInFactBegin()==null?item.getSpNodeBeginTime():item.getSpNodeInFactBegin());
                inFact.put("end", item.getSpNodeInFactEnd()==null?item.getSpNodeEndTime():item.getSpNodeInFactEnd());

                gtArray1.add(inFact);
                jsonObject1.put("gtArray", gtArray1);
                array.add(jsonObject1);
            });

            object.put("start", minStart);
            object.put("end", maxEnd);
            object.put("array", array);
        }else {
            object.put("start", null);
            object.put("end", null);
            object.put("array", null);
        }
        return object;
    }

    List<ModalTree> getInitSchedule(){
        List<ModalTree> list= new LinkedList<>();
        list.add(new ModalTree(1L,"前期管理阶段"));
        list.add(new ModalTree(2L,"施工管理阶段"));
        list.add(new ModalTree(3L,"验收管理阶段"));
        return list;
    }

    //递归获取子菜单
    public List<ModalTree> getChildrenSchedule(List<SchedulePlan> schedulePlanList, List<ModalTree> treeList){

        treeList.forEach(parent->{

            List<ModalTree> s1=schedulePlanList.stream().filter(x -> x.getSpNodeParent()==parent.getKey()).map(item -> new ModalTree(item.getId(),item.getSpNodeName(),item.getSpNodeParent(),item.getSpNodeBeginTime(),item.getSpNodeEndTime(),item.getSpNodeInFactBegin(),item.getSpNodeInFactEnd(),new ArrayList<ModalTree>())).collect(Collectors.toList());
            s1.sort(new Comparator<ModalTree>() {
                @Override
                public int compare(ModalTree modalTree, ModalTree t1) {
                    if(modalTree.getPlanStart().before(t1.getPlanStart()))
                    return -1;
                    else if(t1.getPlanStart().before(modalTree.getPlanStart()))
                        return 1;
                    else
                        return 0;
                }
            });
            parent.setChildren(s1);
            if(s1.size()>0)
                getChildrenSchedule(schedulePlanList, s1);
        });
        return treeList;
    }




}
