from pythonCode.utils import importJson
import os

def getAnalysisFiles(fileName):
	return 'bugAnalysis' in fileName

analyzedBugsFolder = os.path.join(os.path.abspath('..'), 'output')
files = os.listdir(analyzedBugsFolder)
analysisFiles = list(filter(getAnalysisFiles, files))

analyzedBugs=[]
for analysisFile in analysisFiles:
	print(analysisFile)
	analyzedBugsPart = importJson(os.path.join(analyzedBugsFolder, analysisFile))
	print(len(analyzedBugsPart))
	analyzedBugs.extend(analyzedBugsPart)

notUI = 0
cosmetic = 0
behaviour = 0
unknown = 0
total = 0
uiImpact = 0

categoryTypes = ['UI Unknown', 'Wrong Event', 'External Integration', 'NO UI Manifestation', 'Feature Request', 'Appearance',
				  'Missing Elements', 'Unexpected Elements', 'Installation', 'Code Optimizations', 'Privacy and Security']
for bug in analyzedBugs:


for bug in analyzedBugs:
	categories = bug['response']
	if bug['subject'] == 'faveo':
		continue
	# print(categories)
	if categories == -1:
		# Not analyzed
		continue

	total+=1
	if 'UI Unknown' in categories:
		unknown+=1
		continue

	if 'No UI Manifestation' in categories:
		notUI +=1
		continue

	uiBug = False
	if 'Appearance' in categories:
		cosmetic += 1
		uiBug = True

	if ('Missing Elements' in categories) or ('Unexpected Elements' in categories) or ('Wrong Event' in categories):
		behaviour += 1
		uiBug = True

	if uiBug:
		uiImpact += 1
	else:
		notUI +=1

stats = {"total": total,
		"notUI" : notUI,
		 "unknown": unknown,
		 "uiImpact": uiImpact,
		 "cosmetic": cosmetic,
		 "behaviour": behaviour}
# print(analysisFiles)
print(stats)