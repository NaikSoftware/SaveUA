package ua.naiksoftware.waronline.game.unit;

public enum UnitCode {

	// минирование, разминирование, обнаружение мин, лечение, восстановление
	// мостов,
	// проходимость слабая, скорость большая на дороге
	ING_AVTO,

	// проходимость большая (вода), слабо стреляет, из близка нормально, в
	// укрытии слабо уязвим
	SOLDIER,

	// проходимость большая (вода), нормально стреляет, броня слабая
	BTR_4E,

	// проходимость малая, броня средняя, урон средний, дистанция стрельбы
	// короткая
	HOTCHKISS,

	// проходимость большая, броня слабая, урон большой, дистанция стрельбы
	// средняя
	T34_85,

	// проходимость средняя, броня средняя, урон средний, дистанция стрельбы
	// средняя
	PANZER,

	// проходимость большая, броня большая, но слабая, урон большой, дистанция
	// стрельбы большая, борта уязвимые
	TIGER,

	// проходимость низкая, броня средняя, урон самый большой дистанция стрельбы
	// самая большая
	ARTILLERY
}