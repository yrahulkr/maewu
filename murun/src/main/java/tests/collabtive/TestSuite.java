package tests.collabtive;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	
	AddProjectTest.class, // wait
	EditProjectTest.class, // wait
	SearchProjectTest.class,
	CloseProjectTest.class,
	SearchClosedProjectTest.class,
	OpenProjectTest.class,
	AddRoleTest.class,
	EditRoleTest.class,
	AddUserTest.class, // mouse over
	EditUserRoleTest.class, // mouse over
	LoginUserTest.class,
	AssignUserToProjectTest.class,
	AddMilestoneTest.class,
	EditMilestoneTest.class, // mouse over + wait
	CloseMilestoneTest.class,
	OpenMilestoneTest.class,
	AddTasklistTest.class,
	CloseTasklistTest.class, // side effect: test closes the milestone as well.
	OpenTasklistTest.class,
	AddAndRemoveMultipleTasksTest.class,
	AddTaskDesktopPresentTest.class,
	RemoveTaskDesktopNotPresentTest.class,
	AddTasksTest.class,
	CloseTasksTest.class,
	OpenTasksTest.class,
	CloseTasksProjectPercentageTest.class,
	OpenTasksProjectPercentageTest.class,
	AddUserAlreadyPresentTest.class,
	AddUserEmptyValuesTest.class,
	AddAndRemoveLateMilestoneTest.class,
	AddMultipleProjectsTest.class,
	SearchMultipleProjectsTest.class,
	RemoveMultipleProjectsTest.class,
	RemoveUserFromProjectTest.class, // mouse over + wait
	RemoveTasksTest.class,
	RemoveTasklistTest.class,
	RemoveMilestoneTest.class,	// wait
	RemoveRoleTest.class,
	RemoveUserTest.class,
	RemoveProjectTest.class,

})

public class TestSuite {
}
