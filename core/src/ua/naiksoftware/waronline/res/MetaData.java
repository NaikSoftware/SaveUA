package ua.naiksoftware.waronline.res;

import ua.naiksoftware.waronline.game.ObjCode;

public class MetaData {
	
	public static int objRange(ObjCode code) {
		switch (code) {
			case TREE_1:
			case TREE_2:
			case WELL:
				return 1;
			case HATA_1:
			case FORT:
			case CHURCH:
			case REMAINS1:
			case REMAINS2:
			case HATA_2:
			case HATA_3:
			case HATA_4:
			case TENT:
			case STOLB_1:
			case STOLB_2:
			case KPP:
				return 2;
			case ATB:
				return 3;
		}
		return 0;
	}
	
	public static int[][] objIntersect(ObjCode code) {
		switch (code) {
			case HATA_1:
			case FORT:
			case HATA_2:
			case HATA_3:
			case HATA_4:
				return  new int[][] {{1, 1}, {1, 1}};
			case ATB:
				return new int[][] {{1, 1, 1}, {0, 0, 1}, {1, 1, 1}};
			case CHURCH:
			case REMAINS2:
			case TENT:
				return new int[][] {{1, 1}, {0, 0}};
			case REMAINS1:
				return new int[][] {{1, 1}, {1, 1}};
			case TREE_1:
			case TREE_2:
			case WELL:
				return new int[][] {{1}};
			case STOLB_1:
			case STOLB_2:
			case KPP:
				return new int[][] {{1, 0}, {0, 0}};
		}
		return null;
	}
}
