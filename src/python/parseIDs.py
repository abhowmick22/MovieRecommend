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
with open('movie.metadata.tsv', 'r') as tsvin:
	csvfile = open('IDmap2.csv', 'ar+')
	tsvin = csv.reader(tsvin, delimiter = '\t')
	csvout = csv.writer(csvfile, delimiter=',')

	tsvRowNbr = 0;
	moviesMatched = 0
	lastwikiID = 0

	recovered = False
	# recover from checkpoint here
	log = csvfile.readlines()
	if not log :
		recovered = True
	else :
		temp = log[-1].split(',')
		lastwikiID = temp[0].rstrip() 

	for row in tsvin:
		tsvRowNbr += 1
		if tsvRowNbr < 35000:
			continue
			
		wikiID = row[0]
		if wikiID == lastwikiID :
			recovered = True
			continue
		if recovered is False :
			continue

		movieName = row[2]
		# match this wikiID to the movielens ID using fuzzy match
		identifier = movieName[0];
		movielensIDs = sorted(movielensIDs, key=lambda movie: movie[1])
		movielensFiltered = [movie for movie in movielensIDs if movie[1][0] == identifier]
		for item in movielensFiltered:
			movielensName = item[1]
			if nltk.metrics.edit_distance(movieName, movielensName) <= 1 :
				moviesMatched += 1
				print tsvRowNbr
				csvout.writerow((wikiID,item[0],movieName))				
				if moviesMatched%10 == 0:
					csvfile.flush()
				break

