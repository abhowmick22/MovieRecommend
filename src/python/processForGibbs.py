# Script to process the summaries in the format needed by the Gibbs implementation
import sys;
import re;

# Reading processed summaries and vocabulary 
summaryFile = open('../../data/nostemming/processed_summaries.txt', 'r');
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
featureFile = open('../../data/gibbs/summaryfeatures.txt', 'wb');

movieCount = 0;
for line in summaryLines:
    movieCount = movieCount + 1;

# Printing the movie count in the first line
featureFile.write(str(movieCount) + '\n');

# For each movie summary find the indices of words and dump them as feature vectors
iterId = 0;
for line in summaryLines:
    feature = '';

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
            if(feature == ''):
                feature = i;

            else:
                feature = feature + ' ' + i;
   
    # Dumping the feature vector into the file, only if number of features using the curent dictionary is greater than 1
    if(len(feature) > 0):
        featureFile.write(feature + '\n');

featureFile.close()
