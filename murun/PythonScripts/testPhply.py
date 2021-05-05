from phply.phpparse import make_parser
from phply.phplex import lexer
from phply.phpast import InlineHTML
import os
from bs4 import BeautifulSoup
from pathlib import Path


def parseFile(filepath):
	allHTML = ""
	try:
		parser = make_parser()
		parsed = parser.parse(open(filepath).read(), debug=False, lexer=lexer, tracking=True)
		print(parsed)
		for node in parsed:
			# node.
			if isinstance(node, InlineHTML):
				allHTML += node.data
		parser = None
		parsed = None
	except Exception as ex:
		errored.append(file)
		print(ex)

	return allHTML



def getJsFromPhp(files, errored):
	allHTML = ''

	for file in files:
		# print("file" + str(file))
		filepath = os.path.join(dir, file)
		# print("filepath" + filepath)
		try:
			open(file).read()
			allHTML += parseFile(file)
		except Exception as ex:
			print('Error reading')
			errored.append(filepath)
			continue
	soup = BeautifulSoup(allHTML, 'html5lib')
	allScript = soup.find_all('script')

	outputJs = ''
	for script in allScript:
		outputJs += script.get_text()
	print(outputJs)
	return outputJs, errored



'''
'''
if __name__ == '__main__':
	dir = '../WebMutator/RelatedWork/nishiura_webmutation/AjaxMutator-master/sample/data/'\
		  +'claroline'
	files = list(Path(dir).rglob("*.php"))

	outputJs = ''
	errored = []
	# outputJs, errored = getJsFromPhp(files)

	jsFiles = list(Path(dir).rglob("*.js"))
	for jsFile in jsFiles:
		try:
			outputJs += open(jsFile).read()
		except Exception as ex:
			errored.append(jsFile)
			print(ex)

	outputJsFile = os.path.join(dir, "jsSingle.js")
	with open(outputJsFile, 'w') as writeFile:
		writeFile.write(outputJs)
		print("written to {}".format(outputJsFile))

	print(len(errored))




