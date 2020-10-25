package com.tang.project.utils;

import com.tang.project.dto.DataPermissionBase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataPermissionUtil {


    public static List<? extends DataPermissionBase> dataPermissionPack(List<? extends DataPermissionBase> dataList) {
        if (dataList == null || dataList.size() == 0) {
            return new ArrayList<>();
        }

        for (DataPermissionBase data : dataList) {


            data.setOperation(Arrays.asList("111", "222"));
        }

        return dataList;
    }


}
