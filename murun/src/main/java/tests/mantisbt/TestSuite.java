package tests.mantisbt;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import tests.*;

@RunWith(Suite.class)
@SuiteClasses({

		AddUserTest.class,
		AddUserWrongTest.class,
		AddUserEmptyTest.class,	
		AddProjectTest.class,	
		AddProjectWrongTest.class,
		AddProjectEmptyTest.class,
		AddCategoryTest.class,
		AddCategoryWrongTest.class,
		AddCategoryEmptyTest.class,	
		AddIssueTest.class,
		AddIssueEmptyTest.class,
		AssignIssueTest.class,
		ViewSummaryIssueTest.class,
		UpdateIssuePriorityTest.class,
		UpdateIssueSeverityTest.class,
		UpdateIssueStatusNewTest.class,
		UpdateIssueStatusFeedbackTest.class,
		UpdateIssueStatusAcknowledgedTest.class,
		UpdateIssueStatusConfirmedTest.class,
		UpdateIssueStatusAssignedTest.class,
//		UpdateIssueStatusResolvedTest.class,
		UpdateIssueSummaryTest.class,
		UpdateProjectDescriptionTest.class,
		UpdateProjectStatusTest.class,
		UpdateProjectViewStatusTest.class,	
		UpdateCategoryTest.class,
		UpdateCategoryEmptyTest.class,
		UpdateUserTest.class,
		UpdateUserEmptyTest.class,		
		DeleteCategoryTest.class,
		DeleteIssueTest.class,
		DeleteProjectTest.class,
		DeleteUserTest.class,
		LoginNegativeTest.class,
		LogoutTest.class,
		AddMultipleUsersTest.class,
		DeleteMultipleUsersTest.class,
		AddMultipleSubprojectsTest.class,
		UnlinkMultipleSubprojectsTest.class,
		LinkMultipleSubprojectsTest.class,
		DeleteMultipleSubprojectsTest.class
		
})

public class TestSuite {
}
