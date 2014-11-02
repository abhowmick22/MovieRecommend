# Script to take processed summaries, dump them as a feature vector of indices with respect to the vocabulary
import sys;
import re;

# Reading processed summaries and vocabulary 
summaryFile = open('../../data/processed_summaries.txt', 'r');
vocabFile = open('../../data/sortedVocab.txt', 'r');

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
words = [];

# Initialize the index with 'a' at 0
index = {'a':0};

# Next search letter
searchLetter = 'b';

# Collecting words
words = [];

for i in vocabLines:
    splits = i.split(',');
    words.append(splits[1]);

    # Indexing
    curLetter = splits[1][0];
    if(curLetter == searchLetter):
        index[searchLetter] = vocabLines.index(i);
        searchLetter = chr(ord(searchLetter) + 1);

# Appending a dummy entry for handling 'z' and its next ascii character '{'
index['{'] = len(words);

# Opening a file to dump feature for the movie plots
featureFile = open('../../data/summaryFeatures.txt', 'wb');

# For each movie summary find the indices of words and dump them as feature vectors
iterId = 0;
for line in summaryLines:
    feature = [];

    lineSplit = line.split(',');
    movieId = int(lineSplit[0]);

    # Debug message
    iterId = iterId + 1;
    if(iterId % 100 == 0):
        print iterId

    # Reading the set of words using regular expressions
    string = re.findall(r'\[([^]]*)\]', line);
    string = re.findall('\'([^\']*)\'', string[0]);

    for i in string:
        if(len(i) < 1):
            continue;

        startLetter = i[0];
        # Word present in the vocabulary 
        if(ord(startLetter) > 96 and ord(startLetter) < 123):
            subWordList = words[index[startLetter]:index[chr(ord(startLetter)+1)]];
            if(i in subWordList):
                feature.append(index[startLetter] + subWordList.index(i));
   
    # Dumping the feature vector into the file
    featureFile.write(str(movieId) + ', ' +  str(feature) + '\n');

featureFile.close()
