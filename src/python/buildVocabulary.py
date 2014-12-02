# Script to take processed summaries, dump them as a feature vector of indices with respect to the vocabulary
import sys;
import re;
import nltk
import string
from nltk.corpus import stopwords

# Reading processed summaries and vocabulary 
summaryFile = open('../../data/nostemming/processed_summaries_1.txt', 'r');
#vocabFile = open('../../data/sortedVocab_majorremoved.txt', 'r');
#vocabFile = open('../../data/sortedVocab_nostemming.txt', 'r');

# Checking for existance of files
if(summaryFile == None):
    sys.exit('Summary file not found\n');
   
# Reading summary and vocabulary files
# Stripping \n sign
summaryLines = [s.rstrip("\n\r") for s in summaryFile.readlines()]

# Closing files
summaryFile.close();

# Opening a file to dump feature for the movie plots
sortedVocabFile = open('../../data/nostemming/raw_vocabulary_1.txt', 'wb');

# For each movie summary find the indices of words and dump them as feature vectors
iterId = 0;

# Dictionary based on first letter for faster checking
dictionary = {};

# Removing stop words
stopWords = stopwords.words("english");
# Converting characters to remove coding effects
stopWords = [word.replace(u'\u2014',u'-') for word in stopWords]
stopWords = [word.encode('ascii', 'ignore') for word in stopWords]

for i in xrange(97,123):
    dictionary[chr(i)] = {};

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

    for word in string:
        word = word.lower();

        # Checking if the word is actually within a-z range
        if(len(word) == 0):
            continue;

        acceptWord = True;
        # All letters must be in this range
        for char in word:
            letter = ord(char);
            if(letter <= 96 or letter >= 123):
                acceptWord = False;
                break;
        
        if(word in stopWords):
            continue;

        if(not acceptWord):
            continue;

        else:
            startLetter = ord(word[0]);
            if(startLetter > 96 and startLetter < 123):
                # Checking if word is present in dictinary
                if(word not in dictionary[word[0]].keys()):
                    dictionary[word[0]][word] = 1;

                # Else update its count
                else:
                    dictionary[word[0]][word] += 1;

# Sorting dictionary and dumping them
curLength = 0;
sortedKeys = dictionary.keys();
sortedKeys.sort();

for i in sortedKeys:
    dictKeys = dictionary[i].keys();
    dictKeys.sort();


    for j in xrange(0, len(dictKeys)):
        curLength += 1;
        sortedVocabFile.write(str(curLength) + ',' + str(dictKeys[j])+',' + str(dictionary[i][dictKeys[j]]) +'\n');


sortedVocabFile.close();
