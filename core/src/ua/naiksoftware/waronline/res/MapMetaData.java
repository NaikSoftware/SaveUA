package ua.naiksoftware.waronline.res;

import ua.naiksoftware.waronline.game.MapObjCode;

public class MapMetaData {

    /**
     * size x*x
     * 
     * @param code
     * @return array, where 1 - is have intersect cell, else 0
     */
    public static final int[][] objIntersect(MapObjCode code) {
        switch (code) {
            case HATA_1:
            case FORT:
            case HATA_2:
            case HATA_3:
            case HATA_4:
                return new int[][]{{1, 1}, {1, 1}};
            case ATB_1:
                return new int[][]{{1, 0, 0}, {1, 0, 0}, {1, 0, 0}};
            case ATB_2:
                return new int[][]{{1, 0, 0}, {1, 0, 0}, {1, 1, 0}};
            case CHURCH:
            case REMAINS2:
            case TENT:
                return new int[][]{{1, 0}, {1, 0}};
            case REMAINS1:
                return new int[][]{{1, 1}, {1, 1}};
            case TREE_1:
            case TREE_2:
            case WELL:
                return new int[][]{{1}};
            case STOLB_1:
            case STOLB_2:
            case KPP:
                return new int[][]{{1, 0}, {0, 0}};
        }
        return null;
    }
}
