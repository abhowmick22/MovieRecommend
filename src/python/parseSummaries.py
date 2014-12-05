#!/usr/bin/env python

import nltk
import string
import re
import sys
import csv, codecs, cStringIO
from nltk.stem.snowball import SnowballStemmer
from nltk.corpus import stopwords

inFile = open('../../data/plot_summaries_test.txt', 'r')
outFile = open('../../data/nostemming/processed_summaries_test.txt', 'w')
csvout = csv.writer(outFile, delimiter=',')

# Generating list of punctuations and stopwords to compare the words against;
punctuations = list(string.punctuation);

stopWords = stopwords.words("english");
# Converting characters to remove coding effects
stopWords = [word.replace(u'\u2014',u'-') for word in stopWords]
stopWords = [word.encode('ascii', 'ignore') for word in stopWords]

# Initializing the stemmer for stemming words
stemmer = SnowballStemmer("english", ignore_stopwords=True);

# Count of the iteration
count = 0;

for line in inFile.readlines() :
    elems = line.split('\t', 1)
    wikiID = elems[0]
    summary = elems[1].rstrip().decode('utf-8')

    # Process the summary now
    # Tokenize to extract words
    words = nltk.word_tokenize(summary)

    # remove punctuations
    words = [word for word in words if word not in punctuations]

    # Converting all the words into smaller case
    words = [word.lower() for word in words];

    # Remove stopwords
    words = [word for word in words if word not in stopWords];

    # Stemming the words
    #words = [stemmer.stem(word) for word in words]

    # Converting characters to remove coding effects
    words = [word.replace(u'\u2014',u'-') for word in words]
    words = [word.encode('ascii', 'ignore') for word in words]

    # Printing the current status
    count = count + 1;
    if(count % 100 == 0):
        print 'Processing : %d ..' % count;

    #print wikiID
    #outFile.write(words)
    # Writing the words of the current text summary into the file
    csvout.writerow((wikiID, words))

inFile.close()
outFile.close()
