from utils import importJson, writeCSV_Dict


def getCandidatesPerOperator(candidates):
	map = {}
	for candidate in candidates:
		key = candidate["opString"]
		if key not in map:
			map[key] = []
		map[key].append(candidate)

	for key in map.keys():
		print("{} : {}".format(key, len(map[key])))
	print(map)


def getRecordsPerMutant(results, analysisJson):
	total = 0
	candidatesSuccess = 0
	dynScoreAvg = 0
	staticScoreAvg = 0
	staticScoreMin = 0
	for analysis in analysisJson:
		if (analysis is None) or ('mutantClass' not in analysis) or (analysis['mutantClass'] is None):
			continue
		candidateName = analysis["candidate"]
		if results[candidateName]["failedTests"] is not None and len(results[candidateName]["failedTests"]) > 0:
			if results[candidateName]["failedTests"][0]["location"] == "overall":
				continue
		candidate = results[candidateName]["candidate"]
		candidatesSuccess+=1
		kyes = candidate['mutantMap'].keys()
		total += len(candidate['mutantMap'].keys())
		staticScoreAvg+=candidate['operatorScore']
		dynScoreAvg+=candidate['elementScore']
	print("{} candidates {}".format(candidatesSuccess,total))
	return total, staticScoreAvg/candidatesSuccess, dynScoreAvg/candidatesSuccess

def getRecordStats(results, analysisJson):
	total = 0
	for analysis in analysisJson:
		if (analysis is None) or ('mutantClass' not in analysis) or (analysis['mutantClass'] is None):
			continue
		candidateName = analysis["candidate"]
		if results[candidateName]["failedTests"] is not None and len(results[candidateName]["failedTests"]) > 0:
			if results[candidateName]["failedTests"][0]["location"] == "overall":
				continue
		kyes = results[candidateName]['mutationRecordMap'].keys()
		total+= len(kyes)
	print(total)
	return total

def cleanStats(stats, limit = 50):
	eq=[]
	returnArr=[]
	for analysis in stats:
		if (analysis is None) or ('mutantClass' not in analysis) or (analysis['mutantClass'] is None):
			continue
		if analysis['mutantClass'] == 'eq':
			eq.append(analysis)
		else:
			returnArr.append(analysis)

	for eqItem in eq:
		if len(returnArr) >= 50:
			break;
		returnArr.append(eqItem)
	return returnArr


if __name__ == '__main__':
	subjects = ['addressbook', 'claroline', 'collabtive', 'mrbs', 'mantisbt', 'ppma']
	# subjects = ['mrbs']
	missing = []

# if __name__ == '__main__':
	allStats = []

	for subject in subjects:
		candidatesJson = importJson('../trace/'
									+subject +'/record/mutationCandidates.json')
		recordsJson = importJson('../mutant/'+
								 subject + '/record/mutationRecords.json')
		resultsJson = importJson(
		'../mutant/'+
		subject+'/2/report/mutationResults.json')

		analysisJson = importJson('analyses/'+subject+'.json')

		if candidatesJson is None or recordsJson is None or resultsJson is None or analysisJson is None:
			missing.append(subject)
			continue

		analysisJson = cleanStats(analysisJson)

		print("number of candidates is {}".format(str(len(candidatesJson))))
		print("number of records is {}".format(str(len(recordsJson))))
		print("number of results is {}".format(str(len(resultsJson.keys()))))
		numRecords, staticScore, dynScore = getRecordsPerMutant(resultsJson, analysisJson)
		numApplied = getRecordStats(resultsJson, analysisJson)

		stats = {'subject': subject, 'candidates': len(candidatesJson), 'staticScore': staticScore,
			 'dynamicScore': dynScore, 'elements': numRecords,
			 'average': numRecords / 50, 'applied': numApplied}
		allStats.append(stats)

	print(missing)
	writeCSV_Dict(stats.keys(), allStats, 'candidateStats.csv')
	# getCandidatesPerOperator(candidatesJson)