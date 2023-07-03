import os

from utils import importJson, writeCSV, writeCSV_Dict


def getRecordsPerOperator(candidates, subject):
	map = {}
	for candidateName in candidates:
		candidate = candidates[candidateName]['candidate']
		key = candidate["opString"]
		if key not in map:
			map[key] = []
		map[key].append(candidates[candidateName])

	stats = []
	for key in map.keys():
		stat = {}
		killed = 0
		total = len(map[key])
		live = 0

		for item in map[key]:
			if item["failedTests"] is not None and len(item["failedTests"])>0:
				if item["failedTests"][0]["location"] == "overall":
					continue
			if item["failed"] == True:
				killed+=1
			else:
				live+=1
		stat = {"subject":subject, "name":key, "killed":killed, "live":live, "total":total}
		print(stat)
		print("{} : {}".format(key, len(map[key])))
		stats.append(stat)
	print(stats)
	return stats
	# print(map)


if __name__ == '__main__':
	subjects = ['addressbook', 'claroline', 'collabtive', 'mrbs', 'mantisbt', 'ppma']
	stats = []
	missing=[]
	for subject in subjects:
		jsonFile = '/../mutant/'+subject+'/2/report/mutationResults.json'
		if not os.path.exists(jsonFile):
			missing.append(subject)
			continue
		candidatesJson = importJson(jsonFile)
		print("number of candidates is {}".format(str(len(candidatesJson))))
		subject_stats = getRecordsPerOperator(candidatesJson, subject)
		stats.extend(subject_stats)

	print("missng {}".format(missing))
	writeCSV_Dict(['subject', 'name', 'killed', 'live', 'total'], stats, 'stats.csv')
