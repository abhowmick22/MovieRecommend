#!/usr/bin/env python

import nltk
import sys
from nltk.metrics.distance import edit_distance
import csv
import re

# read the movielens list in first
movielensIDs = []
for line in open('movies.dat', 'r'):
	words = line.rstrip().split('::')
	item = (words[0],re.sub(r'\([^)]*\)','',words[1]).rstrip())
	movielensIDs.append(item)

# for each movie in summary corpus, write out the output file 
# Format of output file is:
# Wikipedia ID, MovieLens ID (None indicates no movie review present)
with open('movie.metadata.tsv', 'r') as tsvin, open('IDmap.csv', 'ar+b') as csvfile:
	tsvin = csv.reader(tsvin, delimiter = '\t')
	csvout = csv.writer(csvfile, delimiter=',')

	tsvRowNbr = 0;
	moviesMatched = 0

	# recover from checkpoint here
	#logfile = open('IDmap.csv','r')
	temp = csvfile.readlines()[-1].split(',')
	lastwikiID = temp[0].rstrip() 

	#logfile.close()
	recovered = False

	for row in tsvin:
		tsvRowNbr += 1
		if tsvRowNbr <= 0 :
			continue
			
		wikiID = row[0]
		if wikiID == lastwikiID :
			recovered = True
			continue
		if recovered is False :
			continue

		movieName = row[2]
		# match this wikiID to the movielens ID using fuzzy match
		for item in movielensIDs:
			movielensName = item[1]
			if nltk.metrics.edit_distance(movieName, movielensName) <= 1 :
				moviesMatched += 1
				print tsvRowNbr
				csvout.writerow((wikiID,item[0],movieName))				
				if moviesMatched%10 == 0:
					csvfile.flush()
				break

