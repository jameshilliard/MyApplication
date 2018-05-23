package com.lry.songmachine.util;

import java.util.List;

public class Utils {

    public static final int KEY_PERMISSION_REQUEST_CODE = 1;
    public static final int KEY_DIALOG_PERMISSION_REQUEST_CODE = 2;

    //rx遍历扫描的文件路径
    public static final String KEY_SCANNING_PATH = Method.getInnerSDcardPath() + "/Movies";

    // emmc中的文件路径
    //public static final String KEY_SCANNING_PATH = Method.getInnerSDcardPath();

    public static String KEY_EXTRA_VIDEO_PATH = getExtraPath();

    public static final int NUMBER_PER_SCREEN = 9; //每页显示多少个item,记得看Gridview分为几列

    public static final String KEY_CURRENT_SCREEN = "current_screen";

    public static final String KEY_TOGGLE_SCREEN = "toggle_screen";


    /**
     * 从获得的所有外部设备中选出挂载的U盘设备
     * @return
     */
    private static String getExtraPath() {
        String videoPath = null;
        List<String> path = Method.getAllExterSdcardPath();
        for (int i = 0; i < path.size(); i++) {
            if (path.get(i).contains("emulated")) {
                continue;
            } else {
                videoPath = path.get(i);
            }
        }
        return videoPath;
    }
}
