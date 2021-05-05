package main;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import tests.AddAndRemoveLateMilestoneTest;
import tests.AddAndRemoveMultipleTasksTest;
import tests.AddMilestoneTest;
import tests.AddMultipleProjectsTest;
import tests.AddProjectTest;
import tests.AddRoleTest;
import tests.AddTaskDesktopPresentTest;
import tests.AddTasklistTest;
import tests.AddTasksTest;
import tests.AddUserAlreadyPresentTest;
import tests.AddUserEmptyValuesTest;
import tests.AddUserTest;
import tests.AssignUserToProjectTest;
import tests.CloseMilestoneTest;
import tests.CloseProjectTest;
import tests.CloseTasklistTest;
import tests.CloseTasksProjectPercentageTest;
import tests.CloseTasksTest;
import tests.RemoveUserFromProjectTest;
import tests.EditMilestoneTest;
import tests.EditProjectTest;
import tests.EditRoleTest;
import tests.EditUserRoleTest;
import tests.LoginUserTest;
import tests.OpenMilestoneTest;
import tests.OpenProjectTest;
import tests.OpenTasklistTest;
import tests.OpenTasksProjectPercentageTest;
import tests.OpenTasksTest;
import tests.RemoveMilestoneTest;
import tests.RemoveMultipleProjectsTest;
import tests.RemoveProjectTest;
import tests.RemoveRoleTest;
import tests.RemoveTaskDesktopNotPresentTest;
import tests.RemoveTasklistTest;
import tests.RemoveTasksTest;
import tests.RemoveUserTest;
import tests.SearchClosedProjectTest;
import tests.SearchMultipleProjectsTest;
import tests.SearchProjectTest;

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
