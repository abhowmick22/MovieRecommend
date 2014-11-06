# Script to separate test and training dataset using the matching available in IDmap

import sys;
import re;

# Reading processed summaries and vocabulary 
featureFile = open('../../data/summaryFeatures_nostemming.txt', 'r');
testIdFile = open('../../data/IDmap.csv', 'r');

# Checking for existance of files
if(featureFile == None or testIdFile == None):
    sys.exit('Summary file / ID file not found\n');
   
# Reading summary and vocabulary files
# Stripping \n sign
featureLines = [s.rstrip("\n\r") for s in featureFile.readlines()]
idLines = [s.rstrip("\n\r") for s in testIdFile.readlines()]

# Closing files
featureFile.close();
testIdFile.close();

# Separating the parts of the line
mapIds = [i.split(',') for i in idLines];
mapMovieId = [int(i[0]) for i in mapIds];


# Reading the movieId from summary file
splitFeatures = [i.split(',') for i in featureLines];
featMovieId = [int(i[0]) for i in splitFeatures];

# Reading the features using regular expressions
#string = [re.findall(r'\[([^]]*)\]', i) for i in featureLines];
#features = [[int(i) for i in j[0].split(',') if i != ''] for j in string];

# Going through the ids
curIndex = 0;

# Opening a file to dump feature for the movie plots in training and testing sets
trainFile = open('../../data/summariesTrain_nostemming.txt', 'wb');
testFile = open('../../data/summariesTest_nostemming.txt', 'wb');

noMatches = 0;
matches = [];
# Looping around the corpus
for i in xrange(0, len(featMovieId)):
    # Match found; write in train set
    if(featMovieId[i] in mapMovieId):
        noMatches = noMatches + 1; 
        matches.append(featMovieId[i]);
        trainFile.write(featureLines[i] + '\n');

    # Write in test set
    else:
        pass
        testFile.write(featureLines[i] + '\n');

#[mapMovieId.remove(x) for x in matches];
#print mapMovieId;
print noMatches, len(featMovieId), len(mapMovieId)
