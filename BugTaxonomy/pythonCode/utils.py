import csv
import json

def importJson(jsonFile):
	try:
		with open(jsonFile, encoding='utf-8') as data_file:
			data = json.loads(data_file.read())
			return data
	except Exception as ex:
		print("Exception occured while importing json from : " + jsonFile)
		print(ex)
		return None

def exportJson(jsonData, file):
	with open(file, "w") as write_file:
		json.dump(jsonData, write_file)

def writeCSV_Dict(csvFields, csvRows, dst):
	# print(csvRows)
	with open(dst, 'w') as csvfile:
		writer = csv.DictWriter(csvfile, fieldnames=csvFields)
		writer.writeheader()

		for row in csvRows:
			writer.writerow(row)

def writeCSV(rows, dest):
	with open(dest, 'w') as csvFile:
		writer = csv.writer(csvFile, rows)
		for row in rows:
			writer.writerow(row)
			writer.writerow(row)