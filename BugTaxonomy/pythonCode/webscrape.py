import os
import re
from time import sleep

from selenium import webdriver
from bs4 import BeautifulSoup
from lxml import html
from pythonCode.utils import exportJson, importJson

from github import Github
import markdown

tokenIndex = 0
API_LIMIT = False
tokens = ["ab1d4119b91042b5fd8d336bf6b49e0fcfe58760", "0fa78de6aa9260626371ee73be09358700392808"]

bugsJson = []

errored = []
succesful = []

options = webdriver.ChromeOptions()
options.add_argument('--ignore-certificate-errors')
options.add_argument('--incognito')
options.add_argument('--headless')
driver = None

#GitHub Issues
pgwebLink = 'https://github.com/sosedoff/pgweb/issues?'
craterLink = 'https://github.com/bytefury/crater/issues?'
phplistLink = 'https://github.com/phpList/phplist3/issues?'
koelLink = 'https://github.com/koel/koel/issues?'
faveoLink = 'https://github.com/ladybirdweb/faveo-helpdesk/issues?'
reactiveLink = 'https://github.com/AdaptiveConsulting/ReactiveTraderCloud/issues?'

addressbookLink = 'https://sourceforge.net/p/php-addressbook/bugs/search/?q=status%3Dopen++or+status%3Dclosed+or+status+%3D+wont-fix&limit=250'
clarolineLink = 'https://sourceforge.net/p/claroline/bugs/search/?q=status%3Awont-fix+or+status%3Aclosed+or+status%3Aopen&limit=250'
tikiLink = 'https://sourceforge.net/p/tikiwiki/bugs/search/?q=status%3Aclosed-rejected+or+status%3Aclosed-wont-fix+or+status%3Aclosed-later+or+status%3Aclosed-duplicate+or+status%3Aclosed-out-of-date+or+status%3Aclosed-postponed+or+status%3Aclosed-accepted+or+status%3Aclosed-remind+or+status%3Aclosed-works-for-me+or+status%3Aclosed+or+status%3Aclosed-invalid+or+status%3Aclosed-fixed+or+status%3Aopen'
mrbsLink = 'https://sourceforge.net/p/mrbs/bugs/search/?q=status%3Awont-fix+or+status%3Aclosed+or+status%3Aopen&limit=250'
tuduLink = 'https://sourceforge.net/p/tudu/bugs/search/?q=status%3Aclosed-invalid+or+status%3Aclosed-duplicate+or+status%3Aclosed-rejected+or+status%3Aclosed-works-for-me+or+status%3Aclosed+or+status%3Aclosed-wont-fix+or+status%3Aclosed-fixed'

subjects = {
	'addressbook': {'name': 'addressbook', 'link': addressbookLink, 'parser': 'sourceforge'},
	'claroline': {'name': 'claroline', 'link': clarolineLink, 'parser': 'sourceforge'},
	'tikiwiki': {'name': 'tikiwiki', 'link': tikiLink, 'parser': 'sourceforge'},
	'mrbs': {'name': 'mrbs', 'link': mrbsLink, 'parser': 'sourceforge'},
	'tudu' : {'name': 'tudu', 'link': tuduLink, 'parser':'sourceforge'},

	'pgweb': {'name': 'pgweb', 'link': pgwebLink, 'parser': 'github'},
	'crater': {'name': 'crater', 'link': craterLink, 'parser': 'github'},
	'phplist': {'name': 'phplist', 'link': phplistLink, 'parser': 'github'},
	'koel': {'name': 'koel', 'link': koelLink, 'parser': 'github'},
	'reactive': {'name': 'reactive', 'link': reactiveLink, 'parser': 'github'}
	# 'faveo': {'name': 'faveo', 'link': faveoLink, 'parser': 'github'},
}

def parseBugLinks_Github(link, subject="unknown", existingBugs = []):
	global driver, succesful, errored, bugsJson

	existingBugs = [bug['link'] for bug in existingBugs]

	pageNum = 0
	while True:
		sleep(2)
		print(pageNum)
		scrapeLink = link + 'page=' + str(pageNum) + '&q=is%3Aissue'
		print(scrapeLink)
		driver.get(scrapeLink)
		bugPage = driver.page_source
		# bugPageTree = html.fromstring(bugPage)
		soup = BeautifulSoup(bugPage, 'lxml')
		allBugLinks = soup.find_all('a', id=re.compile("issue_[0-9]+_link"))
		if len(allBugLinks) == 0:
			print("No more bugs - page{}".format(pageNum))
			print(soup.get_text(strip=True))
			break
		else:
			if DRY_RUN:
				allBugScrapeLinks = [bugLink['href'] for bugLink in allBugLinks]
				print(allBugScrapeLinks)
			elif DUMMY_COUNTER > 0 and len(bugsJson) >= DUMMY_COUNTER:
					return
			else:
				for bugLink in allBugLinks:
					print(bugLink['href'])
					# bugScrapeLink = 'https://github.com' + bugLink['href']
					if bugLink['href'] in existingBugs:
						print("skipping {}".format(bugLink['href']))
						continue
					buildJsonForBug_Github_api(bugLink['href'], subject)
					if API_LIMIT:
						print("API limit reached.. stopping for now")
						return
		pageNum+=1

		print(succesful)
		print(errored)
		print("successful : {}".format(str(len(succesful))))
		print("errored : {}".format(str(len(errored))))



def buildJsonForBug_Github_api(link, subject = "unknown"):
	global tokens, tokenIndex, API_LIMIT
	linkSplit = link.split("/")
	repo = linkSplit[1] + "/" + linkSplit[2]

	issueNumber = int(linkSplit[4])

	try:
		g = Github(tokens[tokenIndex])
		repo = g.get_repo(repo)
		issue = repo.get_issue(issueNumber)
	except Exception as ex:
		if "API rate limit exceeded" in str(ex):
			if tokenIndex < len(tokens)-1:
				tokenIndex += 1
				buildJsonForBug_Github_api(link, subject)
			else:
				API_LIMIT = True

			return
		print("Unknown exception {}".format(ex))

	try:
		# link = link.replace("https://github.com/","")

		# print(str(issue.labels))
		# print(issue.state)
		# print(issue.body)
		issueBody = markdown.markdown(issue.body)
		comments = [markdown.markdown(comment.body) for comment in issue.get_comments().get_page(0)]

		# print(comments)

		description = []
		description.append(issueBody)
		description.append(comments)

		bugJson = {"subject": subject,
				   "link": link,
				   "title": issue.title,
				   "status": issue.state,
				   "labels": [label.name for label in issue.labels],
				   "priority": None,
				   "description": description
				   }
		# issue.state()
		print(bugJson)
		bugsJson.append(bugJson)
		succesful.append(link)
	except Exception as ex:
		errored.append(link)
		print("link {} errored {} ".format(link, str(ex)))



def buildJsonForBug_Github(link, subject="unknown"):
	global driver, succesful, errored, bugsJson
	if DRY_RUN:
		return
	elif DUMMY_COUNTER > 0 and len(bugsJson) >= DUMMY_COUNTER:
		return
	driver.get(link)
	bugPage = driver.page_source
	try:
		soup = BeautifulSoup(bugPage, 'lxml')
		# allSidebarItems = soup.find_all('div', class="discussion-sidebar-item"))
		allSidebarItems = soup.findAll("div", {"class": "discussion-sidebar-item"})
		labels = ""
		foundLabels = False
		for sideBarItem in allSidebarItems:
			for string in sideBarItem.stripped_strings:
				if foundLabels:
					labels += repr(string) + ","
				if "Labels" in repr(string):
					foundLabels = True

			if foundLabels:
				break

		print(labels)

		title = soup.find("span", {"class":"js-issue-title"}).get_text(strip=True)
		print(title)

		status = soup.find("span", title=re.compile("Status:.*")).get_text(" ", strip=True)
		# statusText = [text for text in status.stripped_strings]
		print(status)

		comments = soup.select('.comment-body')
		# descriptions = [comment.get_text(strip="True") for comment in comments]
		descriptions = [str(comment) for comment in comments]

		print(descriptions)

		bugJson = {"subject": subject,
					"link": link,
				   "title": title,
				   "status": status,
				   "labels": labels,
				   "priority": None,
				   "description": descriptions
				   }

		bugsJson.append(bugJson)
		succesful.append(link)
	except Exception as ex:
		errored.append(link)
		print("link {} errored {} ".format(link, str(ex)))
		soup = BeautifulSoup(bugPage, 'lxml')
		print(soup.get_text(strip=True))


#SourceForge
def parseBugLinks_Sourceforge(link, subject = "unknown", existingBugs=[]):
	global succesful, errored, driver, bugsJson
	existingBugs = [bug['link'] for bug in existingBugs]

	pageNum = 0
	while True:
		scrapeLink = link + "&page=" + str(pageNum)
		driver.get(scrapeLink)
		page_source = driver.page_source
		soup = BeautifulSoup(page_source, 'lxml')
		allBugLinks = soup.find_all('a', href=re.compile("bugs/[0-9]+"))
		if len(allBugLinks) == 0:
			print("No more bugs from {}".format(str(pageNum)))
			break

		links = []
		for bugLinkEl in allBugLinks:
			bugLink = bugLinkEl['href']
			if bugLink not in links:
				links.append(bugLink)
		# print(bugLink['href'])
		# print(allBugLinks)

		if DRY_RUN:
			print(links)
		elif DUMMY_COUNTER > 0 and len(bugsJson) >= DUMMY_COUNTER:
				return
		else:
			for bugLink in links:
				fullBugLink = 'https://sourceforge.net' + bugLink
				if fullBugLink in existingBugs:
					print("skipping {}".format(bugLink))
					continue
				buildJsonForBug_Sourceforge(fullBugLink, subject)
		pageNum += 1

	print(succesful)
	print(errored)

def buildJsonForBug_Sourceforge(link, subject = "unknown"):
	global driver, succesful, errored, bugsJson
	if DRY_RUN:
		return
	elif DUMMY_COUNTER > 0 and len(bugsJson) >= DUMMY_COUNTER:
		return
	scrapeLink = link
	driver.get(scrapeLink)
	bugPage = driver.page_source
	soup = BeautifulSoup(bugPage, 'lxml')
	bugPageTree = html.fromstring(bugPage)
	try:
		headerElements = bugPageTree.xpath('//*[@id="content_base"]/div[2]/div[1]/div/div')
		for headerElement in headerElements:
			elementText = headerElement.text_content().strip()
			if elementText.startswith('Labels'):
				labelXpath = elementText.split(':')[1].strip()
			elif elementText.startswith('Status:'):
				statusXpath = elementText.split(':')[1].strip()
			elif elementText.startswith('Priority:'):
				priorityXpath = elementText.split(':')[1].strip()
		# bugSoup = BeautifulSoup(bugPage, 'lxml')
		# labelXpath = bugPageTree.xpath('//*[@id="content_base"]/div[2]/div[1]/div/div[4]')[0].text_content().strip()
		# statusXpath = bugPageTree.xpath('//*[@id="content_base"]/div[2]/div[1]/div/div[2]/span')[
		# 	0].text_content().strip()
		# priorityXpath = bugPageTree.xpath('//*[@id="content_base"]/div[2]/div[1]/div/div[6]')[0].text_content().strip()
		descriptionXpath = html.tostring(bugPageTree.xpath('//*[@id="ticket_content"]')[0], encoding='unicode')
		titleXpath = bugPageTree.xpath('//*[@id="content_base"]/div[2]/h2')[0].text_content().strip()

		# titleElement = bugPageTree.xpath(titleXpath)

		commentEls = soup.select('.display_post')
		# comments = [comment.get_text(strip="True") for comment in commentEls]
		comments = [str(comment) for comment in commentEls]

		description = []
		description.append(descriptionXpath)
		description.extend(comments)

		bugJson = {"subject": subject,
					"link": scrapeLink,
				   "title": titleXpath,
				   "status": statusXpath,
				   "labels": labelXpath,
				   "priority": priorityXpath,
				   "description": description
				   }
		print(bugJson)
		succesful.append(scrapeLink)
		bugsJson.append(bugJson)
	except Exception as e:
		print(e)
		errored.append(scrapeLink)

def cleanup():
	global driver
	driver.quit()


SUBJECT = 'addressbook'

def testGitHubBug():
	buildJsonForBug_Github('https://github.com/sosedoff/pgweb/issues/476')
	cleanup()

def testGithub2():
	buildJsonForBug_Github_api('/sosedoff/pgweb/issues/281')


DRY_RUN = False
DUMMY_COUNTER = -1

if __name__ == "__main__":

	# buildJsonForBug("https://sourceforge.net/p/php-addressbook/bugs/122/")
	#
	# parseBugLinks_Sourceforge(mrbsLink)
	# parseBugLinks_Sourceforge(clarolineLink)
	# parseBugLinks_Sourceforge(addressbookLink)
	# parseBugLinks_Sourceforge(tikiLink)

	# parseBugLinks_Github(pgwebLink)
	# cleanup()
	# subjects = {
	# 	'pgweb': {'name': 'pgweb', 'link': pgwebLink, 'parser': 'github'}
	# }

	# testGithub2()
	driver = webdriver.Chrome(
		"//.m2/repository/webdriver/chromedriver/mac64/87.0.4280.88/chromedriver", options=options)

	for subject in subjects.values():
		existingBugs = importJson(os.path.join("..", "output", subject["name"] + "_bugs.json"))
		if existingBugs == None:
			existingBugs = []

		if subject['parser'] == 'sourceforge':
			print("Using {} parser for {}".format(subject['parser'], subject['link']))
			parseBugLinks_Sourceforge(subject['link'], subject["name"], existingBugs)

		elif subject['parser'] == 'github':
			print("Using {} parser for {}".format(subject['parser'], subject['link']))
			parseBugLinks_Github(subject['link'], subject["name"], existingBugs)
		else:
			print("no parser defined")

		existingBugs.extend(bugsJson)
		exportJson(existingBugs, os.path.join("..", "output", subject["name"] + "_bugs.json"))
		bugsJson = []

	# testGitHubBug()

	# driver.get("https://sourceforge.net/p/php-addressbook/bugs/search/?q=status%3Dopen++or+status%3Dclosed+or+status+%3D+wont-fix&limit=250")
