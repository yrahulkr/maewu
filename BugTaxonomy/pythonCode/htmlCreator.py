import random

from jinja2 import Environment, FileSystemLoader
import os
import shutil
import json
from datetime import datetime
from pythonCode.webscrape import subjects
from pythonCode.utils import importJson

# # Capture our current directory
# THIS_DIR = os.path.dirname(os.path.abspath(__file__))

def output_html_doc_dict(templatedir, templateHtml, destination, jsonData, Title='Bug Analysis', saveJsonName=None
	,preLoadedBins = None, crawlPath=None):
	j2_env = Environment(loader=FileSystemLoader(templatedir),
						 trim_blocks=True)
	dict = {"jsonData":jsonData, "title":Title, "saveJsonName":saveJsonName}
	output = ''
	print(dict)
	print("outputting with the dict {0}".format(dict))
	output = j2_env.get_template(templateHtml).render(dict=dict)
	with open(destination, "w") as f:
		f.write(output)



###########################################################################
## CommandLine Argument Validation ############
###########################################################################

def confirmOutputPath(outputPath, resources, overwrite=None):
	abortNum = 0
	MAX_RETRY_NUM = 3
	outputPathConfirmed = False
	while not outputPathConfirmed:
		if(os.path.exists(outputPath)) :
			print("OUTPUT PATH ALREADY EXISTS")
			response = None
			if overwrite ==None :
				response = input("DO you want to overwrite? (Y/N)").strip().lower()
			else:
				if overwrite:
					response = 'y'
				else:
					response = 'n'

			if(response=='y'):
				print("okay overwriting.")
				outputPathConfirmed = True
			else:
				if(abortNum >= MAX_RETRY_NUM):
					print("EXCEEDED MAX RETRY!! ")
					return False

				outputPath = os.path.join(os.path.abspath(input("Provide a new output path.").strip()), '')
				abortNum += 1

		else:	
		# 	os.makedirs(os.path.dirname(OUTPUT_PATH), exist_ok=True)
		# 	#os.makedirs(os.path.dirname(OUTPUT_PATH + IMAGES), exist_ok=True)
			print("CREATING OUTPUT PATH {0}".format(outputPath))
			shutil.copytree(resources, outputPath)
			outputPathConfirmed = True	

	return outputPathConfirmed, outputPath

###########################################################################
## Tests ############
###########################################################################


#testMultiFolder()



###########################################################################
## Main Code ############
###########################################################################

def addGitHubLink(datapoint):
	datapoint['link'] = 'https://www.github.com' + datapoint['link']
	return datapoint

# def filterAutoBugs(datapoint):
# 	return "View on Bugsnag" in datapoint["description"]

def randomPairOuput(bugsJsonFolder, OUTPUT_PATH, OUTPUT_HTML_NAME, TITLE, SAVE_JSON_NAME="bugAnalysis_manual.json"):
	TEMPLATE_DIR = os.path.join(os.path.abspath("../HTMLStuff/"), '')
	RESOURCES = os.path.join(os.path.abspath("../HTMLStuff/resources/"), '')
	TEMPLATE_HTML = "bugAnalysis.html"
	overwrite = True
	# saveJsonName =
	allBugs = []
	for subject in subjects.values():
		subjectBugs = importJson(os.path.abspath(os.path.join("..", "output", subject["name"] + "_bugs.json")))
		if subject["parser"] == "github":
			subjectBugs = list(map(addGitHubLink, subjectBugs))
			# subjectBugs = filter(filterAutoBugs, subjectBugs)
		allBugs.extend(subjectBugs)
		print("{} bugs in {}".format(len(subjectBugs), subject["name"]))

	# print(allBugs)
	print(str(len(allBugs)))
	randomBugs = random.sample(allBugs, RANDOM_NUMBER)
	for randomBug in randomBugs:
		randomBug["response"] = -1
		randomBug["tags"] = ""
		randomBug["comments"] = ""
	randomBugLinks = [randomBug['link'] for randomBug in randomBugs]
	print(randomBugLinks)
	TEMPLATE_HTML_PATH = os.path.abspath(os.path.join(TEMPLATE_DIR, TEMPLATE_HTML))
	outputPathConfirmed, OUTPUT_PATH = confirmOutputPath(OUTPUT_PATH, RESOURCES, overwrite=overwrite)
	outputJsonString = json.dumps(randomBugs)

	output_html_doc_dict(TEMPLATE_DIR, TEMPLATE_HTML, os.path.join(OUTPUT_PATH, OUTPUT_HTML_NAME), outputJsonString, TITLE,
				saveJsonName=SAVE_JSON_NAME, preLoadedBins=None)



def oracleClassification(OUTPUT_PATH
	,OUTPUT_HTML_NAME
	,TITLE
	,outputJson = None
	,saveJsonName = "oracleResults.json"
	,overwrite = None
	,RESOURCES = os.path.join(os.path.abspath("../HTMLStuff/resources/"), '')
	,TEMPLATE_DIR = os.path.join(os.path.abspath("../HTMLStuff/"), '')
	,TEMPLATE_HTML = "testOracleAnalysis.html"
	):
	# print("Not implemented yet")
	TEMPLATE_HTML_PATH = os.path.abspath(os.path.join(TEMPLATE_DIR, TEMPLATE_HTML))
	outputPathConfirmed, OUTPUT_PATH = confirmOutputPath(OUTPUT_PATH, RESOURCES, overwrite = overwrite)

	outputJsonString = json.dumps(outputJson)
	output_html_doc_dict(TEMPLATE_DIR, TEMPLATE_HTML, os.path.join(OUTPUT_PATH, OUTPUT_HTML_NAME), outputJsonString, TITLE,
				saveJsonName=saveJsonName, preLoadedBins=None)


RANDOM_NUMBER = 100
if __name__ == '__main__':

	bugsJsonFolder = "output"

	OUTPUT_PATH = os.path.join(os.path.abspath(bugsJsonFolder), "randomPairs")
	outputId = str(RANDOM_NUMBER) + "_" + str(datetime.now().strftime("%Y%m%d-%H%M%S"))
	OUTPUT_HTML_NAME = "bugAnalysis_" + outputId + ".html"
	SAVE_JSON_NAME = "bugAnalysisOutput_" + outputId + ".json"
	TITLE = "Bug Analysis"
	randomPairOuput(bugsJsonFolder, OUTPUT_PATH, OUTPUT_HTML_NAME, TITLE, SAVE_JSON_NAME=SAVE_JSON_NAME)
	#