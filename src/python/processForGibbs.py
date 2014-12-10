# Script to process the summaries in the format needed by the Gibbs implementation
import sys;
import re;

# Reading processed summaries and vocabulary 
summaryFile = open('../../data/stemming/processed_summaries.txt', 'r');

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

# For each movie summary find the indices of words and dump them as feature vectors
iterId = 0;
featureLines = [];
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

        # Checking for each letter to be alphabet
        considerWord = True;

        for letter in list(i):
            # If any of the word is not alphabet, ignore the word
            if(ord(letter) <= 96 or ord(letter) >= 123):
                considerWord = False;
                break;

        # Continue with next word if not to be considered
        if(considerWord == False):
            continue;
        
        elif(feature == ''):
            feature = i;

        else:
            feature = feature + ' ' + i;

    # Dumping the feature vector into the file, only if number of features using the curent dictionary is greater than 1
    if(len(feature) > 0):
        featureLines.append(feature + '\n');
        movieCount = movieCount + 1;

# Dumping in the file
# Printing the movie count in the first line
featureFile.write(str(movieCount) + '\n');
for i in featureLines:
    featureFile.write(i);
featureFile.close()
