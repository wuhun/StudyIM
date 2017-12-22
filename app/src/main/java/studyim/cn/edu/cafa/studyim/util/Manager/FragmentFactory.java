package studyim.cn.edu.cafa.studyim.util.Manager;

import studyim.cn.edu.cafa.studyim.fragment.main.MainContactFragment;
import studyim.cn.edu.cafa.studyim.fragment.main.MainHomeFragment;
import studyim.cn.edu.cafa.studyim.fragment.main.MainMeFragment;
import studyim.cn.edu.cafa.studyim.fragment.main.MainResourceFragment;
import studyim.cn.edu.cafa.studyim.fragment.main.MainStudyFragment;
import studyim.cn.edu.cafa.studyim.fragment.study.StudyAllFragment;
import studyim.cn.edu.cafa.studyim.fragment.study.StudyClassFragment;
import studyim.cn.edu.cafa.studyim.fragment.study.StudyLeadFragment;

/**
 * ================================================
 * 作    者：悟魂(了解自己，感悟灵魂，做最好的自己)
 * 创建日期：2017/12/21 0021
 * 版    本：1.0
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class FragmentFactory {

    private static FragmentFactory mInstance;

    private FragmentFactory(){};

    public static FragmentFactory getInstance(){
        if(mInstance == null) {
            synchronized (FragmentFactory.class) {
                if(mInstance == null)
                    mInstance = new FragmentFactory();
            }
        }
        return mInstance;
    }

    private MainStudyFragment mainStudyFragment;
    private MainResourceFragment mainResourceFragment;
    private MainContactFragment mainContactFragment;
    private MainHomeFragment mainHomeFragment;
    private MainMeFragment mainMeFragment;

    public MainMeFragment getMainMeFragment(){
        if(mainMeFragment == null) {
            synchronized (MainMeFragment.class){
                if(mainMeFragment == null) {
                    mainMeFragment = new MainMeFragment();
                }
            }
        }
        return mainMeFragment;
    }

    public MainHomeFragment getMainHomeFragment(){
        if(mainHomeFragment == null) {
            synchronized (MainHomeFragment.class){
                if(mainHomeFragment == null) {
                    mainHomeFragment = new MainHomeFragment();
                }
            }
        }
        return mainHomeFragment;
    }

    public MainContactFragment getMainContactFragment(){
        if(mainContactFragment == null) {
            synchronized (MainContactFragment.class){
                if(mainContactFragment == null) {
                    mainContactFragment = new MainContactFragment();
                    
                }
            }
        }
        return mainContactFragment;
    }

    public MainResourceFragment getMainResourceFragment(){
        if(mainResourceFragment == null) {
            synchronized (MainResourceFragment.class){
                if(mainResourceFragment == null) {
                    mainResourceFragment = new MainResourceFragment();
                }
            }
        }
        return mainResourceFragment;
    }

    public MainStudyFragment getMainStudyFragment(){
        if(mainStudyFragment == null) {
            synchronized (MainStudyFragment.class){
                if(mainStudyFragment == null) {
                    mainStudyFragment = new MainStudyFragment();
                }
            }
        }
        return mainStudyFragment;
    }

//    myStudyFragment中的
    private StudyAllFragment studyAllFragment;
    private StudyClassFragment studyClassFragment;
    private StudyLeadFragment studyLeadFragment;

    public StudyLeadFragment getStudyLeadFragment(){
        if(studyLeadFragment == null) {
            synchronized (StudyLeadFragment.class){
                if(studyLeadFragment == null) {
                    studyLeadFragment = new StudyLeadFragment();
                }
            }
        }
        return studyLeadFragment;
    }

    public StudyClassFragment getStudyClassFragment(){
        if(studyClassFragment == null) {
            synchronized (StudyClassFragment.class){
                if(studyClassFragment == null) {
                    studyClassFragment = new StudyClassFragment();
                }
            }
        }
        return studyClassFragment;
    }

    public StudyAllFragment getstudyAllFragment(){
        if(studyAllFragment == null) {
            synchronized (StudyAllFragment.class){
                if(studyAllFragment == null) {
                    studyAllFragment = new StudyAllFragment();
                }
            }
        }
        return studyAllFragment;
    }

}
