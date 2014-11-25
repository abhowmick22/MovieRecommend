# Script to take processed summaries, dump them as a feature vector of indices with respect to the vocabulary
import sys;
import re;

# Reading processed summaries and vocabulary 
summaryFile = open('../../data/summaryFeatures_nostemming.txt', 'r');
vocabFile = open('../../data/sortedVocab_nostemming.txt', 'r');

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

# Collecting words
words = [];

for i in vocabLines:
    splits = i.split(',');
    words.append(splits[1]);

# Creating counts for the words in dictionary
counts = [0 for i in xrange(0, len(words))];

# For each movie summary find the indices of words and dump them as feature vectors
iterId = 0;
for line in summaryLines:
    feature = [];

    lineSplit = line.split(',');
    movieId = int(lineSplit[0]);

    # Debug message
    #iterId = iterId + 1;
    #if(iterId % 100 == 0):
    #    print iterId

    # Reading the set of words using regular expressions
    string = re.findall(r'\[([^]]*)\]', line);

    # Splitting the word indices
    string = string[0].split(', ');

    for i in string:
        if(len(i) < 1):
            continue;

        counts[int(i)] += 1;

#featureFile.close()
# Sorting the words based on frequency
sortedWordId = sorted(xrange(len(counts)), key = lambda x: counts[x]);

# Printing the top 10 words and frequencies
noTopWords = len(sortedWordId);
for i in xrange(1, noTopWords + 1):
    if(counts[sortedWordId[-1*i]] == 0):
        break;
    print '%25s : %d' % (words[sortedWordId[-1*i]], counts[sortedWordId[-1*i]])
