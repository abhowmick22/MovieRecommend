# This is the master script that clubs all the subparts together.
# 1. Generating words from unprocessed summaries
# 2. Dumping them in a temporary file that can be used for generating features for summaries
# 3. Generating vocabulary from the words collected above, after applying some simple rules
# 4. Dumping the processed summaries as feature vectors based on the indices in the vocabulary generated in 3
# 5. Dividing the set into training and testing sets

# Generating words from unprocessed summaries

import nltk
import string
import re
import sys
import csv, codecs, cStringIO
from nltk.stem.snowball import SnowballStemmer
from nltk.corpus import stopwords

# Including necessary methods
from parseAndConstructDictionary import *
from sortAndDumpDictionary import *
from dumpSummaryFeatures import *

# Parsing the raw summaries file, creating dictionary dumping words for processed Summaries
plotSummariesPath = 'plot_summaries.txt';
processedSummariesPath = '../../processedSummaries.txt';
dictionary = parseAndConstructDictionary(plotSummariesPath, processedSummariesPath);

# Sorting and dumping the dictionary
newDictionaryPath = '../../processedDictionary.txt';
sortAndDumpDictionary(dictionary, newDictionaryPath);

# Dumping the new features for given dictionary
summaryFeaturePath = '../../summaryFeatures.txt';
dumpSummaryFeatures(processedSummariesPath, newDictionaryPath, summarFeaturePath);
