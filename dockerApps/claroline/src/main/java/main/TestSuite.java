package main;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import tests.AddCourseEventTest;
import tests.AddCourseExerciseQuestionsTest;
import tests.AddCourseExerciseTest;
import tests.AddCourseTest;
import tests.AddDeniedCourseTest;
import tests.AddEmptyCourseTest;
import tests.AddEmptyUserTest;
import tests.AddMultipleUsersTest;
import tests.AddPasswordCourseTest;
import tests.AddTwiceUserTest;
import tests.AddUserTest;
import tests.AddWrongEmailUserTest;
import tests.AddWrongPasswordUserTest;
import tests.DoCourseExerciseQuestionsMultipleUsersTest;
import tests.DoCourseExerciseQuestionsTest;
import tests.EnrolDeniedCourseTest;
import tests.EnrolMultipleUsersTest;
import tests.EnrolPasswordCourseGoodPasswordUserTest;
import tests.EnrolPasswordCourseWrongPasswordUserTest;
import tests.EnrolUserTest;
import tests.LoginUserTest;
import tests.MakeCourseExerciseVisibleTest;
import tests.RemoveCourseEventTest;
import tests.RemoveCourseExerciseQuestionsTest;
import tests.RemoveCourseExerciseTest;
import tests.RemoveCourseTest;
import tests.RemoveEnrolMultipleUsersTest;
import tests.RemoveEnrolUserTest;
import tests.RemoveMultipleUsersTest;
import tests.RemoveUserTest;
import tests.SearchAdminTest;
import tests.SearchAllowedCourseTest;
import tests.SearchAndRemoveDeniedCourseTest;
import tests.SearchAndRemovePasswordCourseTest;
import tests.SearchCourseTest;
import tests.SearchMultipleUsersTest;
import tests.SearchStudentTest;
import tests.SearchTeacherTest;
import tests.SearchUserTest;
import tests.ViewProfileStatisticsUserTest;

@RunWith(Suite.class)
@SuiteClasses({

	/* single user test cases. */
	AddUserTest.class,
	SearchUserTest.class,
 	LoginUserTest.class,
	AddCourseTest.class,
	SearchCourseTest.class,
	EnrolUserTest.class,

	AddCourseEventTest.class,
	AddCourseExerciseTest.class,
	MakeCourseExerciseVisibleTest.class,
	AddCourseExerciseQuestionsTest.class,
	DoCourseExerciseQuestionsTest.class,
	ViewProfileStatisticsUserTest.class,

	/* multiple users test cases. */
	AddMultipleUsersTest.class,
	SearchMultipleUsersTest.class,
	SearchStudentTest.class,
	SearchTeacherTest.class,
	SearchAdminTest.class,
	EnrolMultipleUsersTest.class,
	DoCourseExerciseQuestionsMultipleUsersTest.class,
	RemoveEnrolMultipleUsersTest.class,
	RemoveMultipleUsersTest.class,

	/* negative test cases. */
	AddEmptyUserTest.class,
	AddWrongEmailUserTest.class,
	AddTwiceUserTest.class,
	AddWrongPasswordUserTest.class,
	AddEmptyCourseTest.class,
	AddDeniedCourseTest.class,
	EnrolDeniedCourseTest.class,
	AddPasswordCourseTest.class,
	EnrolPasswordCourseWrongPasswordUserTest.class,
	EnrolPasswordCourseGoodPasswordUserTest.class,

	SearchAllowedCourseTest.class,
	SearchAndRemovePasswordCourseTest.class,
	SearchAndRemoveDeniedCourseTest.class,
	RemoveCourseExerciseQuestionsTest.class,
	RemoveCourseEventTest.class,
	RemoveCourseExerciseTest.class,
	RemoveEnrolUserTest.class,
	RemoveUserTest.class,
	RemoveCourseTest.class

})

public class TestSuite {}
