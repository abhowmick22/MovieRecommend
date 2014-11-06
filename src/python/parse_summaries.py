#!/usr/bin/env python

import nltk
import string
import re
import sys
import csv, codecs, cStringIO
from nltk.stem.snowball import SnowballStemmer
from nltk.corpus import stopwords

inFile = open('plot_summaries.txt', 'r')
outFile = open('processed_summaries_nostemming.txt', 'w')
csvout = csv.writer(outFile, delimiter=',')
for line in inFile.readlines() :
	elems = line.split('\t', 1)
	wikiID = elems[0]
	summary = elems[1].rstrip().decode('utf-8')
	# Process the summary now
	# Tokenize
	words = nltk.word_tokenize(summary)
	# remove punctuations
	punctuations = list(string.punctuation)
	words = [word for word in words if word not in punctuations]
	# Remove stopwords
	stop = stopwords.words("english")
	words = [word for word in words if word not in stop]
	# Stemming
	stemmer = SnowballStemmer("english", ignore_stopwords=True)
#	if isinstance(words[0], str):
#		print "ordinary string"
#	elif isinstance(words[0], unicode):
#		print "unicode string"
#	else:
#		print "not a string"
	#words = [stemmer.stem(word) for word in words]
	#print words
	#words=words.replace(u'\xa0',u'')
	#words=words.replace(u'\u000a',u'p')
	#words=words.replace(u'\u201c',u'\"')
	#words=words.replace(u'\u201d',u'\"')
	words = [word.replace(u'\u2014',u'-') for word in words]
	words = [word.encode('ascii', 'ignore') for word in words]
	#print wikiID
	#outFile.write(words)
	csvout.writerow((wikiID, words))
