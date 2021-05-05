package tests.addressbook;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({

	AddressBookAddAddressBookTest.class,
	AddressBookSearchAddressBookNameTest.class,
	AddressBookSearchAddressBookEmailTest.class,
	AddressBookAddGroupTest.class,
	AddressBookAssignToGroupTest.class,
	AddressBookSearchByGroupTest.class,
	AddressBookCheckBirthdayInfoTest.class,
	AddressBookCheckAddressBookTest.class,
	AddressBookPrintAddressBookTest.class,
	AddressBookEditAddressBookTest.class,
	AddressBookEditGroupTest.class,
	AddressBookRemoveFromGroupTest.class,
	AddressBookRemoveGroupTest.class,
	AddressBookRemoveAddressBookTest.class,
	
	AddressBookAddMultipleAddressBookTest.class,
	AddressBookSearchMultipleAddressBookNameTest.class,
	AddressBookAddMultipleGroupsTest.class,
	AddressBookAssignToMultipleGroupsTest.class,
	AddressBookSearchByMultipleGroupsTest.class,
	AddressBookCheckMultipleBirthdaysInfoTest.class,
	AddressBookCheckMultipleAddressBookTest.class,
	AddressBookPrintMultipleAddressBookTest.class,
	AddressBookEditMultipleAddressBookTest.class,
	AddressBookEditMultipleGroupsTest.class,
	AddressBookRemoveFromMultipleGroupsTest.class,
	AddressBookRemoveMultipleGroupsTest.class,
	AddressBookRemoveMultipleAddressBookTest.class,

//    AddressBookSearchAddressBookTelephoneNegativeTest.class

})

public class TestSuite {}
