import os

from utils import importJson, writeCSV_Dict

categories = ["none", "missingElements", "wrongElements", "wrongElementState", "brokenLink", "brokenEventHandling", "unExpectedElements", "wrongSize", "wrongColor", "wrongPosition"]

def getScoresPerOperator(analysisJson):
	stats = []
	for record in analysisJson:
		try:
			if record is None or record['mutantClass'] is None:
				continue
			opString = record["candidate"].split("_")[0]
			clasz = record['mutantClass']
			severityScore = record['severityScore']
			stubbornScore = record['stubbornScore']
			subject = record['subject']
			killed = record['killed']
			stat = {'operator': opString,
					'class': clasz,
					'bugSeverity': severityScore,
					'stubborn': stubbornScore,
					'killed': killed,
					'subject':subject,
					'categories':record['selectedCategories'],
					'labels':record['selectedLabels'],
					'testRequirement': record['reqTestList'],
					'candidate': record['candidate']
					}
			stats.append(stat)
			# print(record)
		except Exception as ex:
			print(ex)

	return stats


def cleanStats(stats, limit = 50):
	eq=[]
	returnArr=[]
	for stat in stats:
		if stat['class'] == 'eq':
			eq.append(stat)
		else:
			returnArr.append(stat)

	for eqItem in eq:
		if len(returnArr) >= 50:
			break;
		returnArr.append(eqItem)
	return returnArr

if __name__ == '__main__':
	subjects = ['addressbook', 'claroline', 'collabtive', 'mrbs', 'mantisbt', 'ppma']
	# subjects = ['mrbs']
	missing = []
	stats = []
	for subject in subjects:
		analysisJson = importJson(os.path.join('analyses', subject+".json"))
		if analysisJson is None:
			print("Cant analyze {}".format(subject))
			missing.append(subject)
			continue
		# print("number of records is {}".format(str(len(analysisJson))))
		subjectStats = getScoresPerOperator(analysisJson)
		subjectStats = cleanStats(subjectStats)
		print("number of stats is {}".format(str(len(subjectStats))))
		if subjectStats is not None:
			stats.extend(subjectStats)

	print(missing)
	print(stats[0].keys())
	writeCSV_Dict(stats[0].keys(), stats, 'manualStats.csv')
