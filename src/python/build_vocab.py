#!/usr/bin/env python

import string
import csv
import sys

# Input and output files
# Input file : Processed summaries of all the movies
# Output file : List of vocabulary words
inFile = open('../../data/processed_summaries_vocabtesting.txt', 'r')
csvin = csv.reader(inFile, delimiter=' ')
outFile = open('../../data/vocab_vocabtesting.txt', 'w')

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
            word = word.lower()
            if word != '' :
                totalWords += 1
            # populate the vocab dictionary
            # This is to create a vector of the document
            if word not in vocabulary and word != '' :
                index += 1
                vocabulary[word] = index
                # Printing status for every 500 words
                if(index % 500 == 0):
                    print 'Building words : %d found..' % index;

items = vocabulary.items()
items = sorted(items, key=lambda x : x[1])

print vocabulary
#print item[1]
#print item[0]

#for item in items:
    #outFile.write(str(item[1]) + ',' + str(item[0]) + '\r\n')

inFile.close()
outFile.close()
