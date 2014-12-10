# Script to process the vocabulary and clean the tail
import sys;
import re;
import operator;

# Reading processed summaries and vocabulary 
summaryFile = open('../../data/stemming/processed_summaries.txt', 'r');
vocabFile = open('../../data/stemming/raw_vocabulary.txt', 'r');
#vocabFile = open('../../data/sortedVocab_nostemming.txt', 'r');

# Checking for existance of files
if(summaryFile == None or vocabFile == None):
    sys.exit('Summary file / Vocabulary file not found\n');
   
# Reading summary and vocabulary files
# Stripping \n sign
summaryLines = [s.rstrip("\n\r") for s in summaryFile.readlines()]
vocabLines = [s.rstrip("\n\r") for s in vocabFile.readlines()]

# Closing files
summaryFile.close();
vocabFile.close();

# Compute the index based on starting letter
# Initialize the index with 'a' at 0
index = {'a':0};

# Next search letter
searchLetter = 'b';

# Collecting words
words = [];
minCount = 5;
topPercentile = 0.05; #Top 5 percentile
maxCount = 10000;

# Counts of words
counts = {};
totalCount = 0;
newVocabLines = [];

for i in vocabLines:
    splits = i.split(',');
    
    # Add to the vocabulary only if count is within a range
    if(int(splits[2]) > minCount):
        words.append(splits[1]);
        counts[splits[1]] = int(splits[2]);
        totalCount += int(splits[2]);
        # Adding it to another list for indexing
        newVocabLines.append(i);

# Sorting words
sortedWords = sorted(counts.items(), key=operator.itemgetter(1));
sortedWords.reverse();

# Removing words that form topPercentile
topCount = 0;
topThreshold = topPercentile * totalCount;
topWords = [];
for i in sortedWords:
    topCount += i[1];
    if(topCount <= topThreshold):
        topWords.append(i[0]);


# Restructing the vocabulary and dumping it
newVocabFile = open('../../data/stemming/clean_vocabulary.txt', 'wb');
vocabCount = 0;
for i in newVocabLines:
    splits = i.split(',');

    # if word is in the head, ignore
    if(splits[1] in topWords):
        continue;
    
    newVocabFile.write(str(vocabCount) + ',' + splits[1] + '\n');
    vocabCount += 1;

newVocabFile.close();
