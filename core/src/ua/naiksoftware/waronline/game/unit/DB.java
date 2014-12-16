package ua.naiksoftware.waronline.game.unit;

import com.badlogic.gdx.utils.ArrayMap;

/**
 *
 * @author Naik
 */
public class DB {

	public static final ArrayMap<UnitCode, Integer> looks = new ArrayMap<UnitCode, Integer>() {
		{
			put(UnitCode.ING_AVTO, 2);
			put(UnitCode.SOLDIER, 3);
			put(UnitCode.BTR_4E, 4);
			put(UnitCode.HOTCHKISS, 2);
			put(UnitCode.T34_85, 2);
			put(UnitCode.PANZER, 3);
			put(UnitCode.TIGER, 3);
			put(UnitCode.ARTILLERY, 2);
		}
	};

	public static ArrayMap<UnitCode, Integer> lifes = new ArrayMap<UnitCode, Integer>() {
		{
			put(UnitCode.ING_AVTO, 200);
			put(UnitCode.SOLDIER, 100);
			put(UnitCode.BTR_4E, 150);
			put(UnitCode.HOTCHKISS, 300);
			put(UnitCode.T34_85, 250);
			put(UnitCode.PANZER, 350);
			put(UnitCode.TIGER, 400);
			put(UnitCode.ARTILLERY, 250);
		}
	};

	public static final ArrayMap<UnitCode, Integer> maxCells = new ArrayMap<UnitCode, Integer>() {
		{
			put(UnitCode.ING_AVTO, 4);
			put(UnitCode.SOLDIER, 3);
			put(UnitCode.BTR_4E, 5);
			put(UnitCode.HOTCHKISS, 4);
			put(UnitCode.T34_85, 5);
			put(UnitCode.PANZER, 4);
			put(UnitCode.TIGER, 4);
			put(UnitCode.ARTILLERY, 3);
		}
	};

	public static final ArrayMap<UnitCode, Integer> maxCellsShot = new ArrayMap<UnitCode, Integer>() {
		{
			put(UnitCode.ING_AVTO, 3);
			put(UnitCode.SOLDIER, 2);
			put(UnitCode.BTR_4E, 2);
			put(UnitCode.HOTCHKISS, 2);
			put(UnitCode.T34_85, 3);
			put(UnitCode.PANZER, 3);
			put(UnitCode.TIGER, 2);
			put(UnitCode.ARTILLERY, 2);
		}
	};

	public static final ArrayMap<UnitCode, Integer> attackRadius = new ArrayMap<UnitCode, Integer>() {
		{
			put(UnitCode.ING_AVTO, 1);
			put(UnitCode.SOLDIER, 2);
			put(UnitCode.BTR_4E, 2);
			put(UnitCode.HOTCHKISS, 3);
			put(UnitCode.T34_85, 3);
			put(UnitCode.PANZER, 3);
			put(UnitCode.TIGER, 4);
			put(UnitCode.ARTILLERY, 5);
		}
	};

	public static final ArrayMap<UnitCode, Integer> shootingForce = new ArrayMap<UnitCode, Integer>() {
		{
			put(UnitCode.ING_AVTO, -50);
			put(UnitCode.SOLDIER, -50);
			put(UnitCode.BTR_4E, -50);
			put(UnitCode.HOTCHKISS, -90);
			put(UnitCode.T34_85, -70);
			put(UnitCode.PANZER, -100);
			put(UnitCode.TIGER, -120);
			put(UnitCode.ARTILLERY, -200);
		}
	};

	public static final ArrayMap<UnitCode, Integer> armorFront = new ArrayMap<UnitCode, Integer>() {
		{
			put(UnitCode.ING_AVTO, 20);
			put(UnitCode.SOLDIER, 15);
			put(UnitCode.BTR_4E, 15);
			put(UnitCode.HOTCHKISS, 40);
			put(UnitCode.T34_85, 30);
			put(UnitCode.PANZER, 50);
			put(UnitCode.TIGER, 60);
			put(UnitCode.ARTILLERY, 30);
		}
	};
	public static final ArrayMap<UnitCode, Integer> armorSide = new ArrayMap<UnitCode, Integer>() {
		{
			put(UnitCode.ING_AVTO, 15);
			put(UnitCode.SOLDIER, 10);
			put(UnitCode.BTR_4E, 15);
			put(UnitCode.HOTCHKISS, 20);
			put(UnitCode.T34_85, 25);
			put(UnitCode.PANZER, 40);
			put(UnitCode.TIGER, 50);
			put(UnitCode.ARTILLERY, 15);
		}
	};
	public static final ArrayMap<UnitCode, Integer> armorRear = new ArrayMap<UnitCode, Integer>() {
		{
			put(UnitCode.ING_AVTO, 10);
			put(UnitCode.SOLDIER, 5);
			put(UnitCode.BTR_4E, 10);
			put(UnitCode.HOTCHKISS, 15);
			put(UnitCode.T34_85, 25);
			put(UnitCode.PANZER, 30);
			put(UnitCode.TIGER, 40);
			put(UnitCode.ARTILLERY, 10);
		}
	};

	public static final ArrayMap<UnitCode, Integer> soundShot = new ArrayMap<UnitCode, Integer>() {
		{
			put(UnitCode.ING_AVTO, 10);
			put(UnitCode.SOLDIER, 5);
			put(UnitCode.BTR_4E, 10);
			put(UnitCode.HOTCHKISS, 15);
			put(UnitCode.T34_85, 25);
			put(UnitCode.PANZER, 30);
			put(UnitCode.TIGER, 40);
			put(UnitCode.ARTILLERY, 10);
		}
	};
}
