package ltd.pvt.ujjwalgarg.virtuallibrarianwithhome;

/**
 * Created by Naman on 19-Sep-16.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListData {
    public static HashMap<String, List<String>> getData() {
    HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();

    List<String> cse = new ArrayList<String>();
    cse.add("OOPS");
    cse.add("ICP");
    cse.add("DATA STRUCTURES");
    cse.add("COA");
    cse.add("COMPUTER NETWORKS");
    cse.add("SOFTWARE ENGINEERING");
    cse.add("UNIX");
    cse.add("ALGORITHMS");
    cse.add("OPERATING SYSTEM");
        cse.add("ARTIFICIAL INTELLIGENCE");

    List<String> ece = new ArrayList<String>();
    ece.add("BEDC");
    ece.add("ECA");
    ece.add("SIGNALS AND SYSTEMS");
    ece.add("ANALOGUE ELECTRONICS");
    ece.add("DIGITAL ELECTRONICS");
    ece.add("DIGITAL COMMUNICATION");
    ece.add("DIGITAL SYSTEM PROCESSING");
        ece.add("TELECOMMUNICATION NETWORKS");
        ece.add("MICROPROCCESSORS AND CONTROLLERS");

    List<String> physics = new ArrayList<String>();
    physics.add("THERMODYNAMICS");
    physics.add("OPTICS");
    physics.add("MATERIAL SCIENCE");
    physics.add("QUANTUM MECHANICS");
    physics.add("MAGNETISM");

    List<String> maths = new ArrayList<String>();
    maths.add("PROBABILITY AND RANDOM PROCESS");
    maths.add("MATRICES");
    maths.add("THEORY OF NUMBERS");
    maths.add("TRIGONOMETRY");
    maths.add("LINEAR EQUATIONS");

    List<String> hss = new ArrayList<String>();
    hss.add("PRESENTATION AND COMMUNICATION");
    hss.add("TECHNOLOGY AND GOVERNANCE");
    hss.add("FINANCIAL ACCOUNTING");
    hss.add(" PLANNING AND COMMISSION");
    hss.add("SOCIOLOGY");


    expandableListDetail.put("HSS",hss);
    expandableListDetail.put("CSE", cse);
    expandableListDetail.put("MATHS", maths);
    expandableListDetail.put("ECE", ece);
    expandableListDetail.put("PHYSICS", physics);

    return expandableListDetail;
}
}


