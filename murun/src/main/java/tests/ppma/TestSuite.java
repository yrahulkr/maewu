package tests.ppma;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;


@RunWith(Suite.class)
@SuiteClasses({

		PasswordManagerAddEntryTest.class,
		PasswordManagerSearchEntryByNameTest.class,
		PasswordManagerSearchEntryByUsernameTest.class,
		PasswordManagerSearchEntryByUrlTest.class,
		PasswordManagerSearchEntryByTagsTest.class,
		PasswordManagerSearchEntryByCommentTest.class,
		PasswordManagerSearchEntryByTagListTest.class,
		PasswordManagerEditEntryTest.class,

		PasswordManagerSearchTagsTest.class,
		PasswordManagerRemoveTagsTest.class,
		PasswordManagerCheckEntryTagsRemovedTest.class,
		PasswordManagerRemoveEntryTest.class,
		PasswordManagerSearchEntryNegativeTest.class,
		PasswordManagerSearchTagNegativeTest.class,
		PasswordManagerAddTagTest.class,
		PasswordManagerEditTagTest.class,
		PasswordManagerRemoveTagTest.class,

		PasswordManagerAssignTagToEntryTest.class,
		PasswordManagerAddMultipleEntriesTest.class,
		PasswordManagerSearchMultipleEntriesTest.class,
		PasswordManagerCheckUsedTagsTest.class,
		PasswordManagerSearchAndRemoveMultipleTagsTest.class,
		PasswordManagerRemoveMultipleEntriesTest.class
})

public class TestSuite {}
