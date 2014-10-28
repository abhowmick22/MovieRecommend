#!/usr/bin/env python

import string
import csv
import sys

inFile = open('../../data/processed_summaries.txt', 'r')
csvin = csv.reader(inFile, delimiter=' ')
outFile = open('../../data/vocab.txt', 'w')

vocabulary = {}
index = 0
totalWords = 0

for row in csvin :
	#row = row.split(',')
	cells = []
	for elem in row:
		words = elem.split(',')
		for word in words[1:] :
			word = word.strip('["\']')
			if word != '' :
				totalWords += 1
			# populate the vocab dictionary
			if word not in vocabulary and word != '' :
				index += 1
				vocabulary[word] = index
			# This is to create a vector of the document
			'''
			if word != '' :
				cells.append(word)

			'''

items = vocabulary.items()
items = sorted(items, key=lambda x : x[1])

for item in items:
	outFile.write(str(item[1]) + ',' + str(item[0]) + '\r\n')


inFile.close()
outFile.close()
