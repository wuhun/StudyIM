package studyim.cn.edu.cafa.studyim.util;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import studyim.cn.edu.cafa.studyim.model.Friend;
import tools.com.lvliangliang.wuhuntools.util.WuhunPingyinTool;

/**
 *  根据拼音进行排序整理的工具类
 */
public class SortUtils {

    public static void sortContacts(List<Friend> list) {
        Collections.sort(list);//排序后由于#号排在上面，故得把#号的部分集合移动到集合的最下面

        List<Friend> specialList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            //将属于#号的集合分离开来
            String pinYinFirstLetter = WuhunPingyinTool.getPinYinFirstCharIsLetter(list.get(i).getNoteName());
            if (pinYinFirstLetter.equalsIgnoreCase("#")) {
                specialList.add(list.get(i));
            }
        }

        if (specialList.size() != 0) {
            list.removeAll(specialList);//先移出掉顶部的#号部分
            list.addAll(list.size(), specialList);//将#号的集合添加到集合底部
        }

    }

}
