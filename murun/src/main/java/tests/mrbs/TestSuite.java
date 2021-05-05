package tests.mrbs;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({

	AddBuildingTest.class,
	AddRoomTest.class,
	CheckBuildingRoomTest.class,
	AddEntryTest.class,
	CheckEntryTest.class,

	AddLongNameBuildingNegativeTest.class,
	AddLongNameRoomNegativeTest.class,
	SearchEntryNegativeTest.class,
	RemoveEntryTest.class,
	AddMultipleEntriesSameRoomSameDayTest.class,
	AddMultipleEntriesSameRoomDifferentDaysTest.class,
	SearchMultipleEntriesTest.class,
	RemoveRoomTest.class,
	AddMultipleRoomsTest.class,
	CheckMultipleBuildingRoomsTest.class,
	AddMultipleEntriesDifferentRoomsSameDayTest.class,
	AddMultipleEntriesDifferentRoomsDifferentDaysTest.class,
	CheckMultipleEntriesTest.class,
	RemoveMultipleEntriesTest.class,
	RemoveMultipleRoomsTest.class,
	AddAndRemoveSerialEntryTest.class,
	RemoveBuildingTest.class
	/*
//	SearchEntryTest.class,
//	AddConflictualEntryNegativeTest.class,
*/

})

public class TestSuite {}
